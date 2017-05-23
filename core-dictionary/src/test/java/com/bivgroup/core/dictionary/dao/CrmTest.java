package com.bivgroup.core.dictionary.dao;

import com.bivgroup.common.orm.OrmProviderImpl;
import com.bivgroup.core.dictionary.dao.enums.RowStatus;
import com.bivgroup.core.dictionary.dao.hierarchy.HierarchyDAO;
import com.bivgroup.core.dictionary.entity.SadAttribute;
import com.bivgroup.core.dictionary.entity.SadEntity;
import com.bivgroup.core.dictionary.entity.SadModule;
import com.bivgroup.core.dictionary.exceptions.DictionaryException;
import com.bivgroup.core.dictionary.generator.GenerateMetadata;
import com.bivgroup.core.dictionary.generator.GenerateMetadataImpl;
import org.dbunit.DatabaseUnitException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.Transaction;
import org.hibernate.tuple.DynamicMapInstantiator;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by andreykus on 27.10.2016.
 */
public class CrmTest extends AbstractTest {
    OrmProviderImpl provider;
    GenerateMetadata gen;

    void init() throws DatabaseUnitException, SQLException, MalformedURLException {
        provider = new OrmProviderImpl(dataSource, "Generator");
        gen = new GenerateMetadataImpl(provider);

        SadEntity ent = gen.getSession().get(SadEntity.class, 1L);
        SadAttribute attr = gen.getSession().get(SadAttribute.class, 1L);

        IDataSet dataSet = new FlatXmlDataSetBuilder().build(new File("src/test/resources/com/bivgroup/db/crm/TestDictionaryDelete.xml"));
        DatabaseOperation.DELETE_ALL.execute(con, dataSet);

        dataSet = new FlatXmlDataSetBuilder().build(new File("src/test/resources/com/bivgroup/db/crm/TestDictionaryModule.xml"));
        DatabaseOperation.INSERT.execute(con, dataSet);

        dataSet = new FlatXmlDataSetBuilder().build(new File("src/test/resources/com/bivgroup/db/crm/TestDictionaryClassificators.xml"));
        DatabaseOperation.INSERT.execute(con, dataSet);

        dataSet = new FlatXmlDataSetBuilder().build(new File("src/test/resources/com/bivgroup/db/crm/TestDictionaryAccount.xml"));
        DatabaseOperation.INSERT.execute(con, dataSet);

        dataSet = new FlatXmlDataSetBuilder().build(new File("src/test/resources/com/bivgroup/db/crm/TestDictionaryExtAccount.xml"));
        DatabaseOperation.INSERT.execute(con, dataSet);

        dataSet = new FlatXmlDataSetBuilder().build(new File("src/test/resources/com/bivgroup/db/crm/TestDictionaryAddress.xml"));
        DatabaseOperation.INSERT.execute(con, dataSet);

        dataSet = new FlatXmlDataSetBuilder().build(new File("src/test/resources/com/bivgroup/db/crm/TestDictionaryDocument.xml"));
        DatabaseOperation.INSERT.execute(con, dataSet);

        dataSet = new FlatXmlDataSetBuilder().build(new File("src/test/resources/com/bivgroup/db/crm/TestDictionaryContact.xml"));
        DatabaseOperation.INSERT.execute(con, dataSet);

        dataSet = new FlatXmlDataSetBuilder().build(new File("src/test/resources/com/bivgroup/db/crm/TestDictionaryBank.xml"));
        DatabaseOperation.INSERT.execute(con, dataSet);

        dataSet = new FlatXmlDataSetBuilder().build(new File("src/test/resources/com/bivgroup/db/crm/TestDictionaryContragentChildren.xml"));
        DatabaseOperation.INSERT.execute(con, dataSet);

        dataSet = new FlatXmlDataSetBuilder().build(new File("src/test/resources/com/bivgroup/db/crm/TestDictionaryTemp.xml"));
        DatabaseOperation.INSERT.execute(con, dataSet);

        dataSet = new FlatXmlDataSetBuilder().build(new File("src/test/resources/com/bivgroup/db/crm/TestDictionaryClientExt.xml"));
        DatabaseOperation.INSERT.execute(con, dataSet);

        dataSet = new FlatXmlDataSetBuilder().build(new File("src/test/resources/com/bivgroup/db/crm/TestDictionaryClient.xml"));
        DatabaseOperation.INSERT.execute(con, dataSet);

        dataSet = new FlatXmlDataSetBuilder().build(new File("src/test/resources/com/bivgroup/db/crm/TestDictionaryContragent.xml"));
        DatabaseOperation.INSERT.execute(con, dataSet);

        dataSet = new FlatXmlDataSetBuilder().build(new File("src/test/resources/com/bivgroup/db/crm/TestDictionaryAddressDependency.xml"));
        DatabaseOperation.INSERT.execute(con, dataSet);

        dataSet = new FlatXmlDataSetBuilder().build(new File("src/test/resources/com/bivgroup/db/crm/TestDictionaryDocumentDependency.xml"));
        DatabaseOperation.INSERT.execute(con, dataSet);

        dataSet = new FlatXmlDataSetBuilder().build(new File("src/test/resources/com/bivgroup/db/crm/TestDictionaryClientDependency.xml"));
        DatabaseOperation.INSERT.execute(con, dataSet);

        dataSet = new FlatXmlDataSetBuilder().build(new File("src/test/resources/com/bivgroup/db/crm/TestDictionaryContragentDependency.xml"));
        DatabaseOperation.INSERT.execute(con, dataSet);

        dataSet = new FlatXmlDataSetBuilder().build(new File("src/test/resources/com/bivgroup/db/crm/TestDictionaryContragentDependencyPlus.xml"));
        DatabaseOperation.INSERT.execute(con, dataSet);

        dataSet = new FlatXmlDataSetBuilder().build(new File("src/test/resources/com/bivgroup/db/crm/TestDictionaryContactDependency.xml"));
        DatabaseOperation.INSERT.execute(con, dataSet);

        dataSet = new FlatXmlDataSetBuilder().build(new File("src/test/resources/com/bivgroup/db/crm/TestDictionaryTermination.xml"));
        DatabaseOperation.INSERT.execute(con, dataSet);

        dataSet = new FlatXmlDataSetBuilder().build(new File("src/test/resources/com/bivgroup/db/crm/TestDictionaryTerminationExt.xml"));
        DatabaseOperation.INSERT.execute(con, dataSet);

        dataSet = new FlatXmlDataSetBuilder().build(new File("src/test/resources/com/bivgroup/db/crm/TestDictionaryAspect.xml"));
        DatabaseOperation.INSERT.execute(con, dataSet);

        dataSet = new FlatXmlDataSetBuilder().build(new File("src/test/resources/com/bivgroup/db/crm/TestDictionaryAspectModule.xml"));
        DatabaseOperation.INSERT.execute(con, dataSet);

    }

    void initData() throws MalformedURLException, DatabaseUnitException, SQLException {
        // данные  для методов
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(new File("src/test/resources/com/bivgroup/db/crm/data/TestDictionaryTypeDocumentData.xml"));
        DatabaseOperation.REFRESH.execute(con, dataSet);
        dataSet = new FlatXmlDataSetBuilder().build(new File("src/test/resources/com/bivgroup/db/crm/data/TestDictionaryStatustData.xml"));
        DatabaseOperation.REFRESH.execute(con, dataSet);
    }

    @Test
    public void testCleanMap() throws Exception {
        Dao3 hier = new Dao3();
        Map<String, Object> test_m = new HashMap<String, Object>();
        test_m.put("dsds", "dsdsd");
        Map in = new HashMap<>();
        in.put("fgfg", "dfdf");
        in.put("null_1", null);
        test_m.put("dsdsdsd", in);
        test_m.put("null", null);
        hier.cleanMap(test_m);
        Assert.assertTrue(!test_m.containsKey("null"));
        Assert.assertTrue(!((Map) test_m.get("dsdsdsd")).containsKey("null_1"));
    }

    @Test(dependsOnMethods = "testGenerte")
    public void testcreateDriver1() throws DictionaryException {
        Transaction t = gen.getSession().beginTransaction();
        Dao3 hier = new Dao3();
        hier.setIsInsertRootInO2M(Boolean.TRUE);
        Map ob = hier.findById("ContractPMember", 10122L);
//        hier.getSession().flush();
//        hier.getSession().clear();
        t.commit();


        t = gen.getSession().beginTransaction();
        Dao3 hier1 = new Dao3();
        hier1.setIsInsertRootInO2M(Boolean.TRUE);
        ob.put(RowStatus.ROWSTATUS_PARAM_NAME, 3);
        ((Map) ob.get("TypeID_EN")).put(RowStatus.ROWSTATUS_PARAM_NAME, 1);
        hier1.crudByHierarchy("ContractPMember", Arrays.asList(ob));
        //       hier1.getSession().flush();
        //       hier1.getSession().clear();
        t.commit();

    }


    @Test
    public void testGenerte() throws Exception {
        gen.generateAll();
    }

    @Test(enabled = false)
    public void testGenerteByModule() throws Exception {
        SadModule module = provider.getSession().get(SadModule.class, 10010L);
        gen.generateByModule(module);
    }

    @Test(enabled = true)
    public void testGenerateByModuleWithDependency() throws Exception {
        SadModule module = provider.getSession().get(SadModule.class, 10010L);
        SadModule m = new SadModule();

        m.setArtifactId("aspect");
        m.setGroupId("com.bivgroup");
        gen.generateByModuleWithDependency(m);
    }

    class Dao3 extends HierarchyDAO {
        public Dao3() {
            super(provider);
        }
    }

    @Test(dependsOnMethods = "testGenerte")
    public void testcrudByHierarchy() throws DictionaryException {
        Transaction t = gen.getSession().beginTransaction();

        Dao3 hier = new Dao3();
        hier.save("ClientProfile", new HashMap<>());

        t.commit();
    }

    @Test(dependsOnMethods = "testGenerte")
    public void testdeleteByHierarchy() throws DictionaryException {
        Transaction t = gen.getSession().beginTransaction();

        Dao3 hier = new Dao3();
        hier.setIsInsertRootInO2M(Boolean.TRUE);

        Map root = new HashMap<String, Object>();

        //справочник учетки
        Map account = new HashMap<String, Object>();
        account.put("phone", "216035");
        account.put("EID", 1001);
        account.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.UNMODIFIED.getId());
        //справочник типов документов
        Map type_doc = new HashMap<String, Object>();
        type_doc.put("EID", 1001);
        type_doc.put("Name", "Паспорт");
        type_doc.put("Sysname", "PAS");
        type_doc.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.UNMODIFIED.getId());
        //en_KindDocument
        //справочник состояния
        Map sost = new HashMap<String, Object>();

        sost.put("Name", "Приянт");
        sost.put("Sysname", "rec");
        sost.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.UNMODIFIED.getId());
        // en_KindStatus

        //документ клиента1
        Map doc_client = new HashMap<String, Object>();
        doc_client.put("ID", 32);
        doc_client.put("EID", 1001);
        doc_client.put("Series", "VI");
        doc_client.put("No", "322223");
        doc_client.put("TypeID", 1001);
        doc_client.put("StateID", 1001);
        doc_client.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.UNMODIFIED.getId());
        //документ клиента2
        Map doc_client1 = new HashMap<String, Object>();
        doc_client.put("ID", 32);
        doc_client1.put("EID", 1002);
        doc_client1.put("Series", "XI");
        doc_client1.put("No", "22222");
        doc_client1.put("TypeID_EN", type_doc);
        doc_client1.put("StateID_EN", sost);
        doc_client1.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.UNMODIFIED.getId());

        //список документов
        Collection<Map<String, Object>> list_doc = new ArrayList<Map<String, Object>>();
        list_doc.add(doc_client);
        list_doc.add(doc_client1);


        //клиент - физ лицо
        Map param1 = new HashMap<String, Object>();

        param1.put("Name", "Андрей");
        param1.put("Surname", "Кустов");
        param1.put("Patronymic", "Викторович");
        param1.put("Documents", list_doc);
        param1.put("EID", 1001);
        param1.put("ID", 42);
        param1.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.DELETED.getId());

        //        doc_client.put("Client",param1);
        //        Map param111 = new HashMap<String, Object>();
        //        param111.put("ID", 362L);
        //        param111.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.MODIFIED.getId());
        //        doc_client.put("en_Client",param1);

        root.put("PClient", param1);

        Map rez1 = hier.crudByHierarchy(root);


        gen.getSession().flush();
        gen.getSession().clear();

        t.commit();

    }

    @Test(dependsOnMethods = "testGenerte")
    public void testcreateDriver() throws DictionaryException {
        Transaction t = gen.getSession().beginTransaction();

        Dao3 hier = new Dao3();
        hier.setIsInsertRootInO2M(Boolean.TRUE);

        Map root = new HashMap<String, Object>();

        //справочник учетки
        Map account = new HashMap<String, Object>();
        account.put("phone", "216035");
        account.put("EID", 1001);
        account.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.INSERTED.getId());
        //справочник типов документов
        Map type_doc = new HashMap<String, Object>();
        type_doc.put("EID", 1001);
        type_doc.put("Name", "Паспорт");
        type_doc.put("Sysname", "PAS");
        type_doc.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.INSERTED.getId());
        //en_KindDocument
        //справочник состояния
        Map sost = new HashMap<String, Object>();

        sost.put("Name", "Приянт");
        sost.put("Sysname", "rec");
        sost.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.INSERTED.getId());
        // en_KindStatus

        //документ клиента1
        Map doc_client = new HashMap<String, Object>();
        doc_client.put("EID", 1001);
        doc_client.put("Series", "VI");
        doc_client.put("No", "322223");
        doc_client.put("TypeID", 1001);
        doc_client.put("StateID", 1001);
        doc_client.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.INSERTED.getId());
        //документ клиента2
        Map doc_client1 = new HashMap<String, Object>();
        doc_client1.put("EID", 1002);
        doc_client1.put("Series", "XI");
        doc_client1.put("No", "22222");
        doc_client1.put("TypeID_EN", type_doc);
        doc_client1.put("StateID_EN", sost);
        doc_client1.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.INSERTED.getId());
        //список документов
        Collection<Map<String, Object>> list_doc = new ArrayList<Map<String, Object>>();
        list_doc.add(doc_client);
        list_doc.add(doc_client1);


        //ребенок клиента1
        Map child_client12 = new HashMap<String, Object>();
        child_client12.put("EID", 1001);
        child_client12.put("Name", "Андрей");
        child_client12.put("DateOfBirth", new Date());
        child_client12.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.INSERTED.getId());
        //ребенок клиента2
        Map child_client11 = new HashMap<String, Object>();
        child_client11.put("EID", 1002);
        child_client11.put("Name", "Андрей1");
        child_client11.put("DateOfBirth", new Date());
        child_client11.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.INSERTED.getId());
        //список документов
        Collection<Map<String, Object>> list_child = new ArrayList<Map<String, Object>>();
        list_child.add(child_client11);
        list_child.add(child_client12);

        //клиент - водитель
        Map param1 = new HashMap<String, Object>();
        param1.put("DateOfExp", new Date());
        param1.put("StartDate", new Date());
        param1.put("EndDate", new Date());
        param1.put("Name", "Андрей");
        param1.put("Surname", "Кустов");
        param1.put("Patronymic", "Викторович");
        param1.put("Documents", list_doc);
        param1.put("Childs", list_child);
        param1.put("EID", 1001);
        param1.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.INSERTED.getId());

        //        doc_client.put("Client",param1);
        //        Map param111 = new HashMap<String, Object>();
        //        param111.put("ID", 362L);
        //        param111.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.MODIFIED.getId());
        //        doc_client.put("en_Client",param1);

        root.put("ContractDriver", param1);

        Map rez1 = hier.crudByHierarchy(root);


        gen.getSession().flush();
        gen.getSession().clear();

        t.commit();

        Assert.assertNotNull(rez1.get("ContractDriver"));
        Assert.assertNotNull(((Map) rez1.get("ContractDriver")).get("ID"));


    }

    @Test(dependsOnMethods = "testGenerte", enabled = false)
    public void testdeleteDriver() throws DictionaryException {

        Transaction t = gen.getSession().beginTransaction();
        Dao3 hier = new Dao3();
        Map indel = new HashMap<String, Object>();
        //((Map) rez1.get("ContractDriver")).get("ID")
        indel.put("ID", 22L);
        hier.delete("ContractDriver", indel);

        gen.getSession().flush();
        gen.getSession().clear();
        t.commit();
    }

    @Test(dependsOnMethods = "testGenerte")
    public void testdDriver() throws DictionaryException {
        Transaction t = gen.getSession().beginTransaction();

        Dao3 hier = new Dao3();
        hier.setIsInsertRootInO2M(Boolean.TRUE);

        Map root = new HashMap<String, Object>();

        //справочник учетки
        Map account = new HashMap<String, Object>();
        account.put("phone", "216035");
        account.put("EID", 1001);
        account.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.UNMODIFIED.getId());
        //справочник типов документов
        Map type_doc = new HashMap<String, Object>();
        type_doc.put("EID", 1001);
        type_doc.put("Name", "Паспорт");
        type_doc.put("Sysname", "PAS");
        type_doc.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.UNMODIFIED.getId());
        //en_KindDocument
        //справочник состояния
        Map sost = new HashMap<String, Object>();

        sost.put("Name", "Приянт");
        sost.put("Sysname", "rec");
        sost.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.UNMODIFIED.getId());
        // en_KindStatus

        //документ клиента1
        Map doc_client = new HashMap<String, Object>();
        doc_client.put("EID", 1001);
        doc_client.put("Series", "VI");
        doc_client.put("No", "322223");
        //  doc_client.put("TypeID", 1001);
        // doc_client.put("StateID", 1001);
        doc_client.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.UNMODIFIED.getId());
        //документ клиента2
        Map doc_client1 = new HashMap<String, Object>();
        doc_client1.put("EID", 1002);
        doc_client1.put("Series", "XI");
        doc_client1.put("No", "22222");
        //   doc_client1.put("TypeID_EN", type_doc);
        //  doc_client1.put("StateID_EN", sost);
        doc_client1.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.UNMODIFIED.getId());
        //список документов
        Collection<Map<String, Object>> list_doc = new ArrayList<Map<String, Object>>();
        list_doc.add(doc_client);
        list_doc.add(doc_client1);


        //ребенок клиента1
        Map child_client12 = new HashMap<String, Object>();
        child_client12.put("EID", 1001);
        child_client12.put("Name", "Андрей");
        child_client12.put("DateOfBirth", new Date());
        child_client12.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.UNMODIFIED.getId());
        //ребенок клиента2
        Map child_client11 = new HashMap<String, Object>();
        child_client11.put("EID", 1002);
        child_client11.put("Name", "Андрей1");
        child_client11.put("DateOfBirth", new Date());
        child_client11.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.UNMODIFIED.getId());
        //список документов
        Collection<Map<String, Object>> list_child = new ArrayList<Map<String, Object>>();
        list_child.add(child_client11);
        list_child.add(child_client12);

        //клиент - водитель
        Map param1 = new HashMap<String, Object>();
        param1.put("DateOfExp", new Date());
        param1.put("StartDate", new Date());
        param1.put("EndDate", new Date());
        param1.put("Name", "Андрей");
        param1.put("Surname", "Кустов");
        param1.put("Patronymic", "Викторович");
        param1.put("Documents", list_doc);
        param1.put("Childs", list_child);
        param1.put("EID", 1001);
        param1.put("ID", 8112L);
        param1.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.DELETED.getId());

        //        doc_client.put("Client",param1);
        //        Map param111 = new HashMap<String, Object>();
        //        param111.put("ID", 362L);
        //        param111.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.MODIFIED.getId());
        //        doc_client.put("en_Client",param1);

        root.put("ContractDriver", param1);

        Map rez1 = hier.crudByHierarchy(root);


        gen.getSession().flush();
        gen.getSession().clear();

        t.commit();

        Assert.assertNotNull(rez1.get("ContractDriver"));
        //Assert.assertNotNull(((Map) rez1.get("ContractDriver")).get("ID"));


    }


    @Test(dependsOnMethods = "testGenerte")
    public void testcrudByHierarchy1() throws DictionaryException {
        Transaction t = gen.getSession().beginTransaction();

        Dao3 hier = new Dao3();

        //справочник учетки
        Map account = new HashMap<String, Object>();
        account.put("phone", "216035");
        account.put("EID", 1001L);
        account.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.INSERTED.getId());
        //справочник типов документов
        Map type_doc = new HashMap<String, Object>();
        type_doc.put("EID", 1001L);
        type_doc.put("Name", "Паспорт");
        type_doc.put("Sysname", "PAS");
        type_doc.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.INSERTED.getId());
        //en_KindDocument
        //справочник состояния
        Map sost = new HashMap<String, Object>();

        sost.put("Name", "Приянт");
        sost.put("Sysname", "rec");
        sost.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.INSERTED.getId());
        // en_KindStatus

        //документ клиента
        Map doc_client = new HashMap<String, Object>();
        doc_client.put("EID", 1001L);
        doc_client.put("Series", "VI");
        doc_client.put("No", "322223");
        doc_client.put("TypeID", 1001L);
        doc_client.put("StateID", 1001L);
        doc_client.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.INSERTED.getId());

        Map doc_client1 = new HashMap<String, Object>();
        doc_client1.put("EID", 1001L);
        doc_client1.put("Series", "VI");
        doc_client1.put("No", "322223");
        doc_client1.put("TypeID_EN", type_doc);
        doc_client1.put("StateID_EN", sost);
        doc_client1.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.INSERTED.getId());


        //список документов
        Collection<Map<String, Object>> list_doc = new ArrayList<Map<String, Object>>();
        list_doc.add(doc_client);
        list_doc.add(doc_client1);


        //клиент - физ лицо
        Map param1 = new HashMap<String, Object>();

        param1.put("Name", "Андрей");
        param1.put("Surname", "Кустов");
        param1.put("Patronymic", "Викторович");
        param1.put("Documents", list_doc);
        param1.put("EID", 1001L);
        param1.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.INSERTED.getId());

        Map rez1 = hier.crudByHierarchy("PClient", param1);

        gen.getSession().flush();
        gen.getSession().clear();

        t.commit();
    }

    @Test(dependsOnMethods = "testGenerte", enabled = false)
    public void testcrudByHierarchy2() throws DictionaryException {
        Transaction t = gen.getSession().beginTransaction();

        Dao3 hier = new Dao3();

        //справочник учетки
        Map account = new HashMap<String, Object>();
        account.put("phone", "216035");
        account.put("EID", 1001L);
        account.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.INSERTED.getId());
        //справочник типов документов
        Map type_doc = new HashMap<String, Object>();
        type_doc.put("EID", 1001L);
        type_doc.put("Name", "Паспорт");
        type_doc.put("Sysname", "PAS");
        type_doc.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.INSERTED.getId());
        //en_KindDocument
        //справочник состояния
        Map sost = new HashMap<String, Object>();

        sost.put("Name", "Приянт");
        sost.put("Sysname", "rec");
        sost.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.INSERTED.getId());
        // en_KindStatus

        //документ клиента
        Map doc_client = new HashMap<String, Object>();
        doc_client.put("EID", 1001L);
        doc_client.put("Series", "VI");
        doc_client.put("No", "322223");
        doc_client.put("TypeID", 1001L);
        doc_client.put("StateID", 1001L);
        doc_client.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.INSERTED.getId());

        Map doc_client1 = new HashMap<String, Object>();
        doc_client1.put("EID", 1001L);
        doc_client1.put("Series", "VI");
        doc_client1.put("No", "322223");
        doc_client1.put("TypeID_EN", type_doc);
        doc_client1.put("StateID_EN", sost);
        doc_client1.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.INSERTED.getId());


        //список документов
        Collection<Map<String, Object>> list_doc = new ArrayList<Map<String, Object>>();
        list_doc.add(doc_client);
        list_doc.add(doc_client1);


        //клиент - физ лицо
        Map param1 = new HashMap<String, Object>();

        param1.put("Name", "Андрей");
        param1.put("Surname", "Кустов");
        param1.put("Patronymic", "Викторович");
        param1.put("Documents", list_doc);
        param1.put("EID", 1001L);
        param1.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.INSERTED.getId());
        List p_l = new ArrayList<>();
        p_l.add(param1);
        List rez1 = hier.crudByHierarchy("PClient", p_l);

        gen.getSession().flush();
        gen.getSession().clear();

        t.commit();

        t = gen.getSession().beginTransaction();

        gen.getSession().flush();
        gen.getSession().clear();

        t.commit();
    }
}
