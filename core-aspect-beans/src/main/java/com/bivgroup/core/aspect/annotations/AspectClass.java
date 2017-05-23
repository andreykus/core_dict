package com.bivgroup.core.aspect.annotations;

import java.lang.annotation.*;

/**
 * Created by andreykus on 21.11.2016.
 * аннотация аспекта
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AspectClass {
    /**
     * название аспекта
     */
    public String name();

    /**
     * делегатор
     * @return -делегатор
     */
    public Class delegator();

    /**
     * порядок выполнения
     * @return -порядок
     */
    public int callOrder() default 10000000;
}