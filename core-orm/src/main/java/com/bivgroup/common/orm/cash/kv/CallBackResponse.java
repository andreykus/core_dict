package com.bivgroup.common.orm.cash.kv;

/**
 * Created by bush on 19.08.2016.
 */
public abstract interface CallBackResponse<T> {

    public abstract void onComplete(Response<T> paramResponse);

    public abstract void onFailure(Throwable paramThrowable);

}
