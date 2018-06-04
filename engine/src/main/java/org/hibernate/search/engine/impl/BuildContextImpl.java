/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.engine.impl;

import org.hibernate.search.engine.spi.BuildContext;
import org.hibernate.search.engine.spi.ServiceManager;


/**
 * @author Yoann Rodiere
 */
public class BuildContextImpl implements BuildContext {

	private final ServiceManager serviceManager;

	public BuildContextImpl(ServiceManager serviceManager) {
		this.serviceManager = serviceManager;
	}

	@Override
	public ServiceManager getServiceManager() {
		return serviceManager;
	}

}
