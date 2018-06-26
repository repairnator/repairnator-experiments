
package patterns.chain;

/**
 * HandlerTwo Class.
 */
public class HandlerTwo extends AbstractHandler {

    /**
     * Instantiates a new handler two.
     */
    public HandlerTwo() {
        super();
    }

    /**
     * Instantiates a new handler two.
     *
     * @param next
     *            the next
     */
    public HandlerTwo(final HandlerInterface next) {
        super(next);
    }

    /*
     * (non-Javadoc)
     *
     * @see patterns.chain.Handler#handleRequest()
     */
    @Override
    public void handleRequest(final RequestInterface request) {
        doSomething(request);
        super.handleRequest(request);
    }

    /**
     * Do something.
     *
     * @param request
     *            the request
     */
    private void doSomething(final RequestInterface request) {
        this.log.info("doSomething {}", request);
    }

}
