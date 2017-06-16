package com.firefly.example.error.handler;

import com.firefly.Version;
import com.firefly.codec.http2.model.HttpHeader;
import com.firefly.codec.http2.model.HttpStatus;
import com.firefly.server.http2.router.RoutingContext;
import com.firefly.server.http2.router.handler.error.AbstractErrorResponseHandler;

/**
 * @author Pengtao Qiu
 */
public class DefaultErrorResponseHandler extends AbstractErrorResponseHandler {

    @Override
    public void render(RoutingContext ctx, int status, Throwable t) {
        HttpStatus.Code code = HttpStatus.getCode(status);
        String title = status + " " + (code != null ? code.getMessage() : "error");
        String content;
        switch (code) {
            case NOT_FOUND: {
                content = "Custom error handler. The resource " + ctx.getURI().getPath() + " is not found";
            }
            break;
            case INTERNAL_SERVER_ERROR: {
                content = "Custom error handler. The server internal error. <br/>" + (t != null ? t.getMessage() : "");
            }
            break;
            default: {
                content = title + "<br/>" + (t != null ? t.getMessage() : "");
            }
            break;
        }
        ctx.setStatus(status).put(HttpHeader.CONTENT_TYPE, "text/html")
           .write("<!DOCTYPE html>")
           .write("<html>")
           .write("<head>")
           .write("<title>")
           .write(title)
           .write("</title>")
           .write("</head>")
           .write("<body>")
           .write("<h1> " + title + " </h1>")
           .write("<p>" + content + "</p>")
           .write("<hr/>")
           .write("<footer><em>powered by Firefly " + Version.value + "</em></footer>")
           .write("</body>")
           .end("</html>");
    }
}
