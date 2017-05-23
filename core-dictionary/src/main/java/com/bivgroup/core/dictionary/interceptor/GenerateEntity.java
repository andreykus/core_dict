package com.bivgroup.core.dictionary.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by andreykus on 23.09.2016.
 * Аннотация для метки Entity, кторые обрабатываются в  RunTime
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface GenerateEntity {
}
