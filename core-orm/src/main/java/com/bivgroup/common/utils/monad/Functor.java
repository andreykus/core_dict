package com.bivgroup.common.utils.monad;

/**
 * Created by bush on 15.07.2016.
 */
public abstract class Functor<F, T> {
    protected abstract <U> Function<? extends Functor<F, T>, ? extends Functor<?, U>> fmap(Function<T, U> f);
}
