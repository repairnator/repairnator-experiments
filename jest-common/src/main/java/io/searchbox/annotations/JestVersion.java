package io.searchbox.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Sebastian Hilbig
 */


@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JestVersion {
}
