package com.bivgroup.core.dictionary.dynamic;

import org.hibernate.EntityNameResolver;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.property.access.spi.Getter;
import org.hibernate.property.access.spi.Setter;
import org.hibernate.proxy.ProxyFactory;
import org.hibernate.tuple.Instantiator;
import org.hibernate.tuple.entity.EntityMetamodel;
import org.hibernate.tuple.entity.PojoEntityTuplizer;

//TODO удалить
public class MyEntityTuplizer extends PojoEntityTuplizer {

    public MyEntityTuplizer(EntityMetamodel entityMetamodel, PersistentClass mappedEntity) {
        super(entityMetamodel, mappedEntity);
    }

    public EntityNameResolver[] getEntityNameResolvers() {
        // Register it with the org.hibernate.impl.SessionFactoryImpl (which is the implementation class for org.hibernate.SessionFactory) using the registerEntityNameResolver method.

        return new EntityNameResolver[]{MyEntityNameResolver.INSTANCE};
    }

    @Override
    protected Instantiator buildInstantiator(EntityMetamodel entityMetamodel, PersistentClass persistentClass) {
        return new MyEntityInstantiator(persistentClass.getEntityName());
    }

    public String determineConcreteSubclassEntityName(Object entityInstance, SessionFactoryImplementor factory) {
        String entityName = ProxyHelper.extractEntityName(entityInstance);
        if (entityName == null) {
            entityName = super.determineConcreteSubclassEntityName(entityInstance, factory);
        }
        return entityName;
    }


    @Override
    protected ProxyFactory buildProxyFactory(PersistentClass persistentClass, Getter idGetter, Setter idSetter) {
        // allows defining a custom proxy factory, which is responsible for
        // generating lazy proxies for a given entity.
        //
        // Here we simply use the default...
        return super.buildProxyFactory(persistentClass, idGetter, idSetter);
    }

    public static class MyEntityNameResolver implements EntityNameResolver {
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

    //...
}
