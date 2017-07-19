package com.suryani.manage.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target({ METHOD })
public @interface LogOperationRequired {
    String value();

    int whenLog() default WhenLog.after;
}
