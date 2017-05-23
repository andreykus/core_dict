package com.bivgroup.core.aspect.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by bush on 27.12.2016.
 * аннотация параметра на методе аспекта
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface AspectMethodParam {
    /**
     * название параметра
     * @return - название параметра
     */
    String nameParam();
}