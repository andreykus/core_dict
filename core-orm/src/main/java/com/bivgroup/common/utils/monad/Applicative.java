package com.bivgroup.common.utils.monad;

/**
 * Created by bush on 15.07.2016.
 */
public abstract class Applicative<F, T> extends Functor<F, T> {

    public abstract <U> U get();

    protected abstract <U> Applicative<?, U> unit(U value);

    protected final <U> Function<Applicative<F, T>, Applicative<?, U>> apply(final Applicative<Function<T, U>, U> ff) {
        return new Function<Applicative<F, T>, Applicative<?, U>>() {
            public Applicative<?, U> apply(Applicative<F, T> ft) {
                Function<T, U> f = ff.get();
                T t = ft.get();
                return unit(f.apply(t));
            }
        };
    }
}