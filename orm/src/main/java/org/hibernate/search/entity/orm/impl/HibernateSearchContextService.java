/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.entity.orm.impl;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

import javax.transaction.Status;
import javax.transaction.Synchronization;

import org.hibernate.BaseSessionEventListener;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.hibernate.action.spi.AfterTransactionCompletionProcess;
import org.hibernate.action.spi.BeforeTransactionCompletionProcess;
import org.hibernate.engine.spi.ActionQueue;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.search.engine.SearchManager;
import org.hibernate.search.engine.SearchMappingRepository;
import org.hibernate.search.entity.orm.mapping.HibernateOrmSearchManager;
import org.hibernate.search.entity.orm.logging.impl.Log;
import org.hibernate.search.entity.orm.mapping.HibernateOrmMapping;
import org.hibernate.search.entity.pojo.mapping.ChangesetPojoWorker;
import org.hibernate.search.entity.pojo.mapping.PojoSearchManager;
import org.hibernate.search.entity.pojo.mapping.PojoWorker;
import org.hibernate.search.util.impl.common.LoggerFactory;
import org.hibernate.service.Service;

public class HibernateSearchContextService implements Service {

	private volatile SearchMappingRepository mappingRepository;
	private volatile HibernateOrmMapping mapping;

	/*
	 * FIXME support "enlist in transaction"? This only makes sense when index managers support it,
	 * maybe there's something to change here...
	 */
	private boolean enlistInTransaction = false;

	private static final String SEARCH_MANAGER_KEY =
			HibernateSearchContextService.class.getName() + "#SEARCH_MANAGER_KEY";

	private static final String WORKER_PER_TRANSACTION_MAP_KEY =
			HibernateSearchContextService.class.getName() + "#WORKER_PER_TRANSACTION_KEY";

	public void initialize(SearchMappingRepository mappingRepository, HibernateOrmMapping mapping) {
		this.mappingRepository = mappingRepository;
		this.mapping = mapping;
	}

	public SearchMappingRepository getMappingRepository() {
		if ( mappingRepository != null ) {
			return mappingRepository;
		}
		else {
			throw LoggerFactory.make( Log.class, MethodHandles.lookup() ).hibernateSearchNotInitialized();
		}
	}

	public HibernateOrmMapping getMapping() {
		if ( mapping != null ) {
			return mapping;
		}
		else {
			throw LoggerFactory.make( Log.class, MethodHandles.lookup() ).hibernateSearchNotInitialized();
		}
	}

	/**
	 * @param sessionImplementor A Hibernate session
	 *
	 * @return The {@link SearchManager} to use within the context of the given session.
	 */
	@SuppressWarnings("unchecked")
	public HibernateOrmSearchManager getSearchManager(SessionImplementor sessionImplementor) {
		TransientReference<HibernateOrmSearchManager> reference =
				(TransientReference<HibernateOrmSearchManager>) sessionImplementor.getProperties().get( SEARCH_MANAGER_KEY );
		HibernateOrmSearchManager searchManager = reference == null ? null : reference.get();
		if ( searchManager == null ) {
			searchManager = getMapping().createSearchManager( sessionImplementor );
			reference = new TransientReference<>( searchManager );
			sessionImplementor.setProperty( SEARCH_MANAGER_KEY, reference );

			// Make sure we will ultimately close the query manager
			sessionImplementor.getEventListenerManager().addListener( new SearchManagerClosingListener( sessionImplementor ) );
		}
		return searchManager;
	}

	/**
	 * @param sessionImplementor A Hibernate session
	 *
	 * @return The {@link PojoWorker} to use for changes to entities in the given session.
	 */
	@SuppressWarnings("unchecked")
	public PojoWorker getCurrentWorker(SessionImplementor sessionImplementor) {
		PojoSearchManager searchManager = getSearchManager( sessionImplementor );
		if ( sessionImplementor.isTransactionInProgress() ) {
			final Transaction transactionIdentifier = sessionImplementor.accessTransaction();
			TransientReference<Map<Transaction, ChangesetPojoWorker>> reference =
					(TransientReference<Map<Transaction, ChangesetPojoWorker>>) sessionImplementor.getProperties()
							.get( WORKER_PER_TRANSACTION_MAP_KEY );
			Map<Transaction, ChangesetPojoWorker> workerPerTransaction = reference == null ? null : reference.get();
			if ( workerPerTransaction == null ) {
				workerPerTransaction = new HashMap<>();
				reference = new TransientReference<>( workerPerTransaction );
				sessionImplementor.setProperty( WORKER_PER_TRANSACTION_MAP_KEY, reference );
			}
			ChangesetPojoWorker worker = workerPerTransaction.get( transactionIdentifier );
			if ( worker == null ) {
				worker = searchManager.createWorker();
				workerPerTransaction.put( transactionIdentifier, worker );
				Synchronization txSync = createTransactionWorkQueueSynchronization(
						worker, workerPerTransaction, transactionIdentifier
				);
				registerSynchronization( sessionImplementor, txSync );
			}
			return worker;
		}
		else if ( false ) {
			/*
			 * TODO handle the "simulated" transaction when "a Flush listener is registered".
			 * See:
			 *  - FullTextIndexEventListener (in Search 5 and here)
			 *  - the else block in org.hibernate.search.event.impl.EventSourceTransactionContext#registerSynchronization in Search 5
			 */
			throw new UnsupportedOperationException( "Not implemented yet" );
		}
		else {
			// TODO add a warning when configuration expects transactions, but none was found
//			if ( transactionExpected ) {
//				// this is a workaround: isTransactionInProgress should return "true"
//				// for correct configurations.
//				log.pushedChangesOutOfTransaction();
//			}
			// TODO Create a ChangesetWorker (to handle automatic reindexing of containing types),
			// but ensure changes will be applied without waiting for a call to worker.execute()
			// TODO also ensure synchronicity if necessary (getStreamWorker(Synchronization.SYNCHRONOUS)? Something else?)
			throw new UnsupportedOperationException( "Not implemented yet" );
		}
	}

	private Synchronization createTransactionWorkQueueSynchronization(ChangesetPojoWorker worker,
			Map<Transaction, ChangesetPojoWorker> workerPerTransaction, Object transactionIdentifier) {
		if ( enlistInTransaction ) {
			return new InTransactionWorkQueueSynchronization(
					worker, workerPerTransaction, transactionIdentifier
			);
		}
		else {
			return new PostTransactionWorkQueueSynchronization(
					worker, workerPerTransaction, transactionIdentifier
			);
		}
	}

	private void registerSynchronization(SessionImplementor sessionImplementor, Synchronization synchronization) {
		//use {Before|After}TransactionCompletionProcess instead of registerSynchronization because it does not
		//swallow transactions.
		/*
		 * HSEARCH-540: the pre process must be both a BeforeTransactionCompletionProcess and a TX Synchronization.
		 *
		 * In a resource-local tx env, the beforeCommit phase is called after the flush, and prepares work queue.
		 * Also, any exceptions that occur during that are propagated (if a Synchronization was used, the exceptions
		 * would be eaten).
		 *
		 * In a JTA env, the before transaction completion is called before the flush, so not all changes are yet
		 * written. However, Synchronization-s do propagate exceptions, so they can be safely used.
		 */
		final ActionQueue actionQueue = sessionImplementor.getActionQueue();
		SynchronizationAdapter adapter = new SynchronizationAdapter( synchronization );

		boolean isLocal = isLocalTransaction( sessionImplementor );
		if ( isLocal ) {
			//if local tx never use Synchronization
			actionQueue.registerProcess( (BeforeTransactionCompletionProcess) adapter );
		}
		else {
			//TODO could we remove the action queue registration in this case?
			actionQueue.registerProcess( (BeforeTransactionCompletionProcess) adapter );
			sessionImplementor.accessTransaction().registerSynchronization( adapter );
		}

		//executed in all environments
		actionQueue.registerProcess( (AfterTransactionCompletionProcess) adapter );
	}

	private boolean isLocalTransaction(SessionImplementor sessionImplementor) {
		return !sessionImplementor
				.getTransactionCoordinator()
				.getTransactionCoordinatorBuilder()
				.isJta();
	}

	private static class SearchManagerClosingListener extends BaseSessionEventListener {
		private final SessionImplementor sessionImplementor;

		private SearchManagerClosingListener(SessionImplementor sessionImplementor) {
			this.sessionImplementor = sessionImplementor;
		}

		@Override
		public void end() {
			TransientReference<HibernateOrmSearchManager> reference =
					(TransientReference<HibernateOrmSearchManager>) sessionImplementor.getProperties().get( SEARCH_MANAGER_KEY );
			HibernateOrmSearchManager searchManager = reference == null ? null : reference.get();
			if ( searchManager != null ) {
				searchManager.close();
			}
		}
	}

	/**
	 * An adapter for synchronizations, allowing to register them as
	 * {@link BeforeTransactionCompletionProcess} or {@link AfterTransactionCompletionProcess} too,
	 * without running the risk of executing their methods twice.
	 * <p>
	 * Also, suppresses any call to {@link Synchronization#afterCompletion(int)} so that
	 * it can be executed later, in {@link AfterTransactionCompletionProcess#doAfterTransactionCompletion(boolean, SharedSessionContractImplementor)}.
	 *
	 * @see HibernateSearchContextService#registerSynchronization(SessionImplementor, Synchronization)
	 */
	private static class SynchronizationAdapter implements Synchronization,
			BeforeTransactionCompletionProcess, AfterTransactionCompletionProcess {

		private static final Log log = LoggerFactory.make( Log.class, MethodHandles.lookup() );

		private final Synchronization delegate;
		private boolean beforeExecuted = false;
		private boolean afterExecuted = false;

		SynchronizationAdapter(Synchronization delegate) {
			this.delegate = delegate;
		}

		@Override
		public void beforeCompletion() {
			doBeforeCompletion();
		}

		@Override
		public void afterCompletion(int status) {
			log.tracef(
					"Transaction's afterCompletion is expected to be executed"
					+ " through the AfterTransactionCompletionProcess interface, ignoring: %s", delegate
			);
		}

		@Override
		public void doBeforeTransactionCompletion(SessionImplementor sessionImplementor) {
			try {
				doBeforeCompletion();
			}
			catch (Exception e) {
				throw new HibernateException( "Error while indexing in Hibernate Search (before transaction completion)", e );
			}
		}
		@Override
		public void doAfterTransactionCompletion(boolean success, SharedSessionContractImplementor sessionImplementor) {
			try {
				doAfterCompletion( success ? Status.STATUS_COMMITTED : Status.STATUS_ROLLEDBACK );
			}
			catch (Exception e) {
				throw new HibernateException( "Error while indexing in Hibernate Search (after transaction completion)", e );
			}
		}

		private void doBeforeCompletion() {
			if ( beforeExecuted ) {
				log.tracef(
						"Transaction's beforeCompletion() phase already been processed, ignoring: %s", delegate
				);
			}
			else {
				delegate.beforeCompletion();
				beforeExecuted = true;
			}
		}

		private void doAfterCompletion(int status) {
			if ( afterExecuted ) {
				log.tracef(
						"Transaction's afterCompletion() phase already been processed, ignoring: %s", delegate
				);
			}
			else {
				delegate.afterCompletion( status );
				afterExecuted = true;
			}
		}
	}
}
