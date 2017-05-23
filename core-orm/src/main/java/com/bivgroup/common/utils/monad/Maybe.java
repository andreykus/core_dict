package com.bivgroup.common.utils.monad;

/**
 * Created by bush on 15.07.2016.
 */
public abstract class Maybe<T> extends AbstractMonad<Maybe<T>, T> {

    public static <U> Maybe<U> instance(U value) {
        return (Maybe<U>) (value != null ? Just.instance(value) : Nothing.instance());
    }

    @Override
    public final <U> Maybe<U> map(Function<T, U> f) {
        return (Maybe<U>) super.map(f);
    }

    @Override
    public final <U> Maybe<U> bind(Function<T, ? extends AbstractMonad<?, U>> f) {
        return (Maybe<U>) super.bind(f);
    }

    @Override
    protected <U> Maybe<U> fail(RuntimeException e) {
        return Nothing.instance();
    }

    public abstract boolean isJust();

    public abstract boolean isNothing();

}
