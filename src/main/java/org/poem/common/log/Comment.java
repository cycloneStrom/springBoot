package org.poem.common.log;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by poem on 2016/6/18.
 */
@Target({TYPE, METHOD, FIELD})
@Retention(RUNTIME)
public @interface Comment {
    String value() default "";
}
