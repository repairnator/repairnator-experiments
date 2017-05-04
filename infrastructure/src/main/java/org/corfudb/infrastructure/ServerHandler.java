package org.corfudb.infrastructure;

import com.codahale.metrics.Timer;
import org.corfudb.protocols.wireprotocol.CorfuMsgType;

import java.lang.annotation.*;

/**
 * Created by mwei on 8/8/16.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ServerHandler {
    CorfuMsgType type();
    String opTimer() default ""; // Registry's name of timer to be used for this operation
}
