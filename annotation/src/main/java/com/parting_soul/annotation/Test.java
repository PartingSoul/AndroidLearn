package com.parting_soul.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author parting_soul
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface Test {
}
