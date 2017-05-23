package com.bivgroup.core.dictionary.dynamic;

import org.hibernate.EntityNameResolver;

//TODO удалить
public class MyEntityNameResolver implements EntityNameResolver {
    public static final MyEntityNameResolver INSTANCE = new MyEntityNameResolver();

    public String resolveEntityName(Object entity) {
        return ProxyHelper.extractEntityName(entity);
    }

    public boolean equals(Object obj) {
        return getClass().equals(obj.getClass());
    }

    public int hashCode() {
        return getClass().hashCode();
    }
}
