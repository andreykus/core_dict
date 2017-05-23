package com.bivgroup.common.orm.cash.kv;

import java.math.BigInteger;

/**
 * Created by bush on 19.08.2016.
 */
public class CashKV
        extends AbstractCash<String, Object> {
    private CashKV(CallbackConsumer<Object> callbackConsumer) {
        super(callbackConsumer);
    }

    public static CashKV newCache(String kvClient, final String rootPath, final int watchSeconds) {

        CallbackConsumer<Object> callbackConsumer = new CallbackConsumer() {
            public void consume(BigInteger index, CallBackResponse callback) {
                // this.val$kvClient.getValues(rootPath, AbstractCash.watchParams(index, watchSeconds), callback);
            }

        };

        return new CashKV(callbackConsumer);
    }
}

