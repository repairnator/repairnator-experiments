package edu.harvard.iq.dataverse.api.datadeposit;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.swordapp.server.ContainerAPI;
import org.swordapp.server.ContainerManager;
import org.swordapp.server.StatementManager;
import org.swordapp.server.servlets.SwordServlet;

public class SWORDv2ContainerServlet extends SwordServlet {

    @Inject
    ContainerManagerImpl containerManagerImpl;
    @Inject
    StatementManagerImpl statementManagerImpl;
    private ContainerManager cm;
    private ContainerAPI api;
    private StatementManager sm;
    private final ReentrantLock lock = new ReentrantLock();
    
    
    @Override
    public void init() throws ServletException {
        super.init();

        // load the container manager implementation
        this.cm = containerManagerImpl;

        // load the statement manager implementation
        this.sm = statementManagerImpl;

        // initialise the underlying servlet processor
        this.api = new ContainerAPI(this.cm, this.sm, this.config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            lock.lock();
            setRequest(req);
            this.api.get(req, resp);
            setRequest(null);
        } finally {
            lock.unlock();
        }
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            lock.lock();
            setRequest(req);
            this.api.head(req, resp);
            setRequest(null);
        } finally {
            lock.unlock();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            lock.lock();
            setRequest(req);
            this.api.put(req, resp);
            setRequest(null);
        } finally {
            lock.unlock();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            lock.lock();
            setRequest(req);
            this.api.post(req, resp);
            setRequest(null);
        } finally {
            lock.unlock();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            lock.lock();
            setRequest(req);
            this.api.delete(req, resp);
            setRequest(null);
        } finally {
            lock.unlock();
        }
    }
    
    private void setRequest( HttpServletRequest r ) {
        containerManagerImpl.setHttpRequest(r);
        statementManagerImpl.setHttpRequest(r);
    }
}
