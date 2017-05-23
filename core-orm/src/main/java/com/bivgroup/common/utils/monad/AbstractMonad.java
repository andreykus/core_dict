package com.bivgroup.common.utils.monad;

/**
 * Created by bush on 15.07.2016.
 */
public abstract class AbstractMonad<M, T> extends Applicative<M, T> {

    protected AbstractMonad() {
    }

    protected <U> AbstractMonad<?, U> yield(Function<T, U> f) {
        Applicative<Function<T, U>, U> ff = (Applicative<Function<T, U>, U>) unit(f);
        return (AbstractMonad<?, U>) apply(ff).apply(this);
    }

    protected <U> AbstractMonad<?, U> join() {
        return get();
    }

    protected <U> AbstractMonad<?, U> fail(RuntimeException e) {
        throw e;
    }

    protected final <U> Function<AbstractMonad<M, T>, AbstractMonad<?, U>> fmap(final Function<T, U> f) {
        return new Function<AbstractMonad<M, T>, AbstractMonad<?, U>>() {
            public AbstractMonad<?, U> apply(final AbstractMonad<M, T> mt) {
                try {
                    return mt.yield(f);
                } catch (RuntimeException e) {
                    return mt.fail(e);
                }
            }
        };
    }

    public <U> AbstractMonad<?, U> map(Function<T, U> f) {
        return fmap(f).apply(this);
    }

    public <U> AbstractMonad<?, U> bind(Function<T, ? extends AbstractMonad<?, U>> f) {
        return map(f).join();
    }
}
