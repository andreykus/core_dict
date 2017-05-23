package com.bivgroup.core.dictionary.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 *TODO удалить
 */
public class ProxyHelper {

    public static String extractEntityName(Object object) {
        // Our custom java.lang.reflect.Proxy instances actually bundle
        // their appropriate entity name, so we simply extract it from there
        // if this represents one of our proxies; otherwise, we return null
        if (Proxy.isProxyClass(object.getClass())) {
            InvocationHandler handler = Proxy.getInvocationHandler(object);
            if (DataProxyHandler.class.isAssignableFrom(handler.getClass())) {
                DataProxyHandler myHandler = (DataProxyHandler) handler;
                return myHandler.getEntityName();
            }
        }
        return null;
    }

    // various other utility methods ....

}
