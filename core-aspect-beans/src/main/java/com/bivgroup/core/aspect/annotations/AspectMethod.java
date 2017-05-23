package com.bivgroup.core.aspect.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by bush on 06.12.2016.
 *  аннотация аспекта на методе
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AspectMethod {
    /**
     * действие
     * @return -  действие
     */
    Action action() default Action.READ;
}
