package com.bivgroup.core.dictionary.dynamic;

import org.hibernate.HibernateException;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.tuple.Instantiator;

import java.io.Serializable;

/**
 * Created by bush on 12.10.2016.
 * TODO удалить
 */

public class MyEntityInstantiator implements Instantiator {
    private final String entityName;

    public MyEntityInstantiator(String entityName) {
        this.entityName = entityName;
    }

    public Object instantiate(Serializable id) {
        return null;
//            if ( Person.class.getName().equals( entityName ) ) {
//                return ProxyHelper.newPersonProxy( id );
//            }
//            if ( Customer.class.getName().equals( entityName ) ) {
//                return ProxyHelper.newCustomerProxy( id );
//            }
//            else if ( Company.class.getName().equals( entityName ) ) {
//                return ProxyHelper.newCompanyProxy( id );
//            }
//            else if ( Address.class.getName().equals( entityName ) ) {
//                return ProxyHelper.newAddressProxy( id );
//            }
//            else {
//                throw new IllegalArgumentException( "unknown entity for instantiation [" + entityName + "]" );
//            }
    }

    public Object instantiate() {
        return instantiate(null);
    }

    public boolean isInstance(Object object) {
        try {
            return ReflectHelper.classForName(entityName).isInstance(object);
        } catch (Throwable t) {
            throw new HibernateException("could not get handle to entity-name as interface : " + t);
        }
    }
}
