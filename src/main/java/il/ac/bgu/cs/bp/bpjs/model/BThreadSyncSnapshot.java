package il.ac.bgu.cs.bp.bpjs.model;

import java.io.Serializable;
import java.util.Optional;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Callable;
import org.mozilla.javascript.NativeContinuation;
import org.mozilla.javascript.Scriptable;

import il.ac.bgu.cs.bp.bpjs.execution.jsproxy.BThreadJsProxy;
import il.ac.bgu.cs.bp.bpjs.analysis.ContinuationProgramState;
import java.util.Objects;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.ScriptableObject;

/**
 * The state of a BThread at {@code bsync}.
 *
 * @author orelmosheweinstock
 * @author Michael
 */
public class BThreadSyncSnapshot implements Serializable {

    /**
     * Name of the BThread described
     */
    private String name;

    /**
     * The JavaScript function that will be called when {@code this} BThread
     * runs.
     */
    private Function entryPoint;

    /**
     * BThreads may specify a function that runs when they are removed because
     * of a {@code breakUpon} statement.
     */
    private Function interruptHandler = null;

    /**
     * Proxy to {@code this}, used from the JavaScript code.
     */
    private final BThreadJsProxy proxy = new BThreadJsProxy(this);

    /**
     * Scope for the JavaScript code execution.
     */
    private Scriptable scope;

    /**
     * Continuation of the code.
     */
    private NativeContinuation continuation;

    /**
     * BSync statement of the BThread at the time of the snapshot.
     */
    private BSyncStatement bSyncStatement;
    
    private transient ContinuationProgramState programState;

    public BThreadSyncSnapshot(String aName, Function anEntryPoint) {
        name = aName;
        entryPoint = anEntryPoint;
    }

    /**
     * Convenience constructor with default parameters.
     */
    public BThreadSyncSnapshot() {
        this(BThreadSyncSnapshot.class.getName(), null);
    }

    /**
     * Fully detailed constructor. Mostly useful for getting objects out of
     * serialized forms.
     *
     * @param name
     * @param entryPoint
     * @param interruptHandler
     * @param scope
     * @param continuation
     * @param bSyncStatement
     */
    public BThreadSyncSnapshot(String name, Function entryPoint, Function interruptHandler, Scriptable scope,
            NativeContinuation continuation, BSyncStatement bSyncStatement) {
        this.name = name;
        this.entryPoint = entryPoint;
        this.interruptHandler = interruptHandler;
        this.scope = scope;
        this.continuation = continuation;
        this.bSyncStatement = bSyncStatement;
    }

    /**
     * Creates the next snapshot of the BThread in a given run.
     *
     * @param aContinuation The BThread's continuation for the next sync.
     * @param aStatement The BThread's statement for the next sync.
     * @return a copy of {@code this} with updated continuation and statement.
     */
    public BThreadSyncSnapshot copyWith(NativeContinuation aContinuation, BSyncStatement aStatement) {
        BThreadSyncSnapshot retVal = new BThreadSyncSnapshot(name, entryPoint);
        retVal.continuation = aContinuation;
        retVal.setInterruptHandler(interruptHandler);
        retVal.setupScope(scope.getParentScope());

        retVal.bSyncStatement = aStatement;
        aStatement.setBthread(retVal);

        return retVal;
    }

    void setupScope(Scriptable programScope) {
        scope = (Scriptable) Context.javaToJS(proxy, programScope);
        scope.delete("equals");
        scope.delete("hashCode");
        scope.delete("toString");
        scope.delete("notify");
        scope.delete("notifyAll");
        scope.delete("wait");
        
        scope.setParentScope(programScope);
        // setup entryPoint's scope s.t. it knows our proxy
        Scriptable curScope = entryPoint.getParentScope();
        entryPoint.setParentScope(scope);
        scope.setParentScope(curScope);
        
    }

    public BSyncStatement getBSyncStatement() {
        return bSyncStatement;
    }

    public void setBSyncStatement(BSyncStatement stmt) {
        bSyncStatement = stmt;
        if (bSyncStatement.getBthread() != this) {
            bSyncStatement.setBthread(this);
        }
    }

    public Object getContinuation() {
        return continuation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "[BThreadSyncSnapshot: " + name + "]";
    }

    public Optional<Function> getInterrupt() {
        return Optional.ofNullable(interruptHandler);
    }

    public void setInterruptHandler(Function anInterruptHandler) {
        interruptHandler = anInterruptHandler;
    }

    public Scriptable getScope() {
        return scope;
    }

    public Function getEntryPoint() {
        return entryPoint;
    }
    
    @Deprecated
    public ContinuationProgramState getContinuationProgramState() {
        if ( programState == null ) {
            programState = new ContinuationProgramState((NativeContinuation) continuation);
        }
        return programState;
    }
    
    @Override
    public int hashCode() {
        return (31 * Objects.hashCode(getName())) ^ 
                    ((continuation != null ) ? continuation.getImplementation().hashCode() : 0);
    }

    @Override
    public boolean equals(Object obj) {

        // Quick circuit-breakers
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof BThreadSyncSnapshot)) {
            return false;
        }
        BThreadSyncSnapshot other = (BThreadSyncSnapshot) obj;
        if (!Objects.equals(getName(), other.getName())) {
            return false;
        }
        
        // OK, need to delve into the continuations themselves.
        Boolean res = (Boolean)ContextFactory.getGlobal().call(cx -> {
            // Must perform the comparison in a context with a top call, as some
            // standard objects (XML) need ScriptRuntime.getTopCallScope.
            ScriptableObject top = cx.initStandardObjects();
            Boolean internalRes = (Boolean)ScriptRuntime.doTopCall((Callable)(c, scope, thisObj, args) -> {
                return NativeContinuation.equalImplementations(continuation, other.continuation);
            }, cx, top, top, null, false);
            return internalRes;
        });
        
        return res;
        
    }

}
