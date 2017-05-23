
Работа с аспектами модуля
=========================

Аспекты накладываются на функционал DAO через Proxy объекты (при создании к интерфейсу объекта добавляются интерфейсы делегаторов).
При инициализации модуля создается хранилище аспектов на модуле, сущности. Так же для сущностей строится цепочка обработки привязанных аспектов
Внешнее расширение DAO реализовано делегаторами, привязанными к аспектам.
Пример вызова внешнего расширения:

        ExtendGenericDAO face = (ExtendGenericDAO) AspectProxy.newInstance(new DAOTest(), new Class[]{DAOTest.class});
        Transaction tr = face.getSession().getTransaction();
        tr.begin();
        Map r = face.findById("PClient", 1192L);
        r.put("INN", "3434341");
        r.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.MODIFIED.getId());
        r = face.crudByHierarchy("PClient", r);
        tr.commit();
        ((DAOTestInterface) face).test("PClient", new HashMap<>());
        ((BinaryFilesDelegator)face).chek("CDM_PClient");
    
### Описание Аспекта

 * Реализовать [AbstractAspectVisitor](..\visitors\AbstractAspectVisitor.java) с аннотацией @AspectClass
 * Описать интерфейс делегатора от [AspectDelegator](..\visitors\AspectDelegator.java)
 * Зарегистрировать аспект в фабрике [AspectFactory](..\builder\AspectFactory.java)
 