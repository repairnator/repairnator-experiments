package com.firefly.utils.function;

/**
 * Represents a function with three arguments.
 */
public interface Func3<T1, T2, T3, R> extends Function {
    R call(T1 t1, T2 t2, T3 t3);
}
