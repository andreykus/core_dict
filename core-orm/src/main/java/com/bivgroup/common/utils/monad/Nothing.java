package com.bivgroup.common.utils.monad;

import java.util.NoSuchElementException;

/**
 * Created by bush on 15.07.2016.
 */
public final class Nothing<T> extends Maybe<T> {

    private static final Nothing INSTANCE = new Nothing();

    private Nothing() {
        super();
    }

    public static <U> Nothing<U> instance() {
        return INSTANCE;
    }

    @Override
    public <U> U get() {
        throw new NoSuchElementException("Nothing.get");
    }

    @Override
    protected <U> Maybe<U> unit(U value) {
        return INSTANCE;
    }

    @Override
    protected <U> Maybe<U> yield(Function<T, U> f) {
        return INSTANCE;
    }

    @Override
    protected <U> Maybe<U> join() {
        return INSTANCE;
    }

    @Override
    public boolean isJust() {
        return false;
    }

    @Override
    public boolean isNothing() {
        return true;
    }

    @Override
    public String toString() {
        return "Nothing";
    }
}
