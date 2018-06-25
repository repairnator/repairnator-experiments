package com.hashmap.haf.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface IgniteFunction {
    String functionClazz();
    String packageName() default "";
    String service();
    Configuration[] configs();
}
