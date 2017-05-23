package com.bivgroup.common.utils.monad;

/**
 * Created by bush on 15.07.2016.
 */
public final class Just<T> extends Maybe<T> {

    private T value;

    private Just(T value) {
        this.value = value;
    }

    public static <U> Just<U> instance(U value) {
        return new Just<U>(value);
    }

    @Override
    public <U> U get() {
        return (U) value;
    }

    @Override
    protected <U> Maybe<U> unit(U value) {
        return Just.instance(value);
    }

    @Override
    public boolean isJust() {
        return true;
    }

    @Override
    public boolean isNothing() {
        return false;
    }

    @Override
    public String toString() {
        return "Just(" + get() + ")";
    }
}