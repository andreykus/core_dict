package com.bivgroup.core.dictionary.dao;

import com.bivgroup.common.orm.OrmProviderImpl;
import com.bivgroup.core.dictionary.dao.enums.RowStatus;
import com.bivgroup.core.dictionary.dao.hierarchy.HierarchyDAO;
import com.bivgroup.core.dictionary.entity.SadAttribute;
import com.bivgroup.core.dictionary.entity.SadEntity;
import com.bivgroup.core.dictionary.entity.enums.Category;
import com.bivgroup.core.dictionary.entity.enums.TypeExport;
import com.bivgroup.core.dictionary.exceptions.DictionaryException;
import com.bivgroup.core.dictionary.generator.GenerateMetadata;
import com.bivgroup.core.dictionary.generator.GenerateMetadataImpl;
import oracle.jdbc.pool.OracleDataSource;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.tuple.DynamicMapInstantiator;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.sql.DataSource;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by andreykus on 12.09.2016.
 */
public class GenerateTest {
    static String URL = "jdbc:oracle:thin:@192.168.100.159:1521:ORCD";
    static String USER_NAME = "SBER_DEV_02";
    static String SCHEMA_NAME = USER_NAME;
    static String PASSWORD = "1";
    public OracleDataSource dataSource;
    public OrmProviderImpl provider;
    public GenerateMetadata gen;
    static String ENTITY_NAME = "en_Test";
    static String ENTITY_LIST_NAME = "en_Test1List";

    Long id1 = null;
    Long id2 = null;
    Long id3 = null;

    /**
     * Инициируем соединение и заполняем данными
     */
    @BeforeClass
    public void setUp() throws SQLException, DatabaseUnitException, MalformedURLException {

        dataSource = new OracleDataSource();
        dataSource.setURL(URL);
        dataSource.setUser(USER_NAME);
        dataSource.setPassword(PASSWORD);
        IDatabaseConnection con = new DatabaseDataSourceConnection(dataSource,SCHEMA_NAME);
        provider = new OrmProviderImpl(dataSource, "");
        gen = new GenerateMetadataImpl(dataSource);
        gen.setType(TypeExport.CLASS);

        SadEntity ent = gen.getSession().get(SadEntity.class, 1L);
        SadAttribute attr = gen.getSession().get(SadAttribute.class, 1L);

//        IDataSet dataSet = new FlatXmlDataSetBuilder().build(new File("src/test/resources/com/bivgroup/db/TestDictionaryModule.xml"));
//        DatabaseOperation.CLEAN_INSERT.execute(con, dataSet);
//
//        dataSet = new FlatXmlDataSetBuilder().build(new File("src/test/resources/com/bivgroup/db/TestDictionary.xml"));
//        DatabaseOperation.CLEAN_INSERT.execute(con, dataSet);
//
//        dataSet = new FlatXmlDataSetBuilder().build(new File("src/test/resources/com/bivgroup/db/TestDictionaryDep.xml"));
//        DatabaseOperation.INSERT.execute(con, dataSet);

//        dataSet = new FlatXmlDataSetBuilder().build(new File("src/test/resources/com/bivgroup/db/TestDictionaryParent.xml"));
//        DatabaseOperation.INSERT.execute(con, dataSet);
//
//        dataSet = new FlatXmlDataSetBuilder().build(new File("src/test/resources/com/bivgroup/db/TestDictionaryParentDep.xml"));
//        DatabaseOperation.INSERT.execute(con, dataSet);
    }


    class Dao extends TemplateGenericDAO {
        public Dao() {
            super(ENTITY_NAME, gen.getSession());
        }

        public Dao(String entity) {
            super(entity, gen.getSession());
        }

        @Override
        public DataSource getDataSource() {
            return null;
        }

        @Override
        public String getModuleName() {
            return null;
        }

        @Override
        public Map crudByHierarchy(Map transientInstance) throws DictionaryException {
            return null;
        }

        @Override
        public Map crudByHierarchy(String entityName, Map transientInstance) throws DictionaryException {
            return null;
        }

        @Override
        public List crudByHierarchy(String entityName, List<Map> transientInstance) throws DictionaryException {
            return null;
        }

        @Override
        public Map crudByHierarchy(String entityName, Map transientInstance, boolean isUseProxy) throws DictionaryException {
            return null;
        }
    }

    class Dao1 extends TemplateGenericDAO {
        public Dao1() {
            super("en_Test", gen.getSession());
        }

        @Override
        public DataSource getDataSource() {
            return null;
        }

        @Override
        public String getModuleName() {
            return null;
        }

        @Override
        public Map crudByHierarchy(Map transientInstance) throws DictionaryException {
            return null;
        }

        @Override
        public Map crudByHierarchy(String entityName, Map transientInstance) throws DictionaryException {
            return null;
        }
        @Override
        public Map crudByHierarchy(String entityName, Map transientInstance,boolean n) throws DictionaryException {
            return null;
        }
        @Override
        public List crudByHierarchy(String entityName, List<Map> transientInstance) throws DictionaryException {
            return null;
        }
    }

    class Dao3 extends HierarchyDAO {
        public Dao3() {
            super( provider );
        }
    }

    private void processRezult(Map rez, Dao3 dao) throws DictionaryException{

        Map rez1 = (Map) rez.get(ENTITY_NAME);
        Assert.assertNotNull(rez1);
        Assert.assertFalse(rez1.isEmpty());

        //проверим наличие записи по идентификатору
        Assert.assertNotNull(rez1.get("ID"));
        if (rez1.get("ID") != null) {
            id1 = (Long) rez1.get("ID");
            Map rez2 = dao.findById(ENTITY_NAME, id1);
            Assert.assertNotNull(rez1);
            Assert.assertNotNull(rez1.get(ENTITY_LIST_NAME));
            Assert.assertTrue(((List) rez1.get(ENTITY_LIST_NAME)).size() == 2);

            if (((List) rez1.get(ENTITY_LIST_NAME)).size() == 2) {
                Iterator it = ((List) rez1.get(ENTITY_LIST_NAME)).iterator();
                id2 = (Long) ((Map) it.next()).get("ID");
                id3 = (Long) ((Map) it.next()).get("ID");
            }
        }

    }

    @Test()
    public void testSad() throws DictionaryException{

        //EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("jpaPersistenceCore");
        //EntityManager entityManager = entityManagerFactory.createEntityManager();
        Session session = gen.getSession(); // coreFacade.getOrmProvider().getSession();
        session.getTransaction().begin();

        SadEntity wen = new SadEntity();
        wen.setName("First");
        wen.setSysname("First");
        wen.setTablename("First");


        SadAttribute attr = new SadAttribute();
        attr.setSysname("ss");
        attr.setDatatype("java.lang.Long");
        attr.setName("ss");
        attr.setCategory(Category.FIELD);
        attr.setEntity(wen);
        attr.setFieldname("dsds");


        SadAttribute attr1 = new SadAttribute();
        attr1.setSysname("id");
        attr1.setDatatype("java.lang.Long");
        attr1.setName("id");
        attr1.setCategory(Category.FIELD);
        attr1.setEntity(wen);
        attr1.setFieldname("id");
        attr1.setIsprimarykey(1L);


        wen.getAttributes().add(attr);
        wen.getAttributes().add(attr1);


        session.save(wen);

        session.flush();

        Dao dao = new Dao("com.bivgroup.core.entity.en_First");
        dao.findById(1L);

        session.getTransaction().commit();


        provider.getSession().get(SadAttribute.class, 1L);

        session.close();


    }

    @Test
    public void testGenerte() throws Exception {
        gen.generateAll();
    }


    /**
     * Тест удаления записи из вложенного листа
     *
     * @throws Exception
     */
    @Test(dependsOnMethods = "testDaoHierarchyAdd")
    public void testDaoHierarchyDelete() throws Exception {
        Transaction t = gen.getSession().beginTransaction();

        Dao3 hier = new Dao3();
        Collection<Map<String, Object>> ti = new ArrayList<Map<String, Object>>();

        //1-я запись в списе - модифицируем
        Map paramIn = new HashMap<String, Object>();
        paramIn.put("ID", id2);
        paramIn.put("NAME", "FIRST11");
        paramIn.put("TEST_ID1", 1001L);
        paramIn.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.MODIFIED.getId());
        ti.add(paramIn);

        //2-я запись в списе - удаляем
        Map paramIn1 = new HashMap<String, Object>();
        paramIn1.put("ID", id3);
        paramIn1.put("NAME", "TOO");
        paramIn1.put("TEST_ID1", 1001L);
        paramIn1.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.DELETED.getId());
        ti.add(paramIn1);

        //основной объект- модифицируем
        Map param = new HashMap<String, Object>();
        param.put(ENTITY_LIST_NAME, ti);
        param.put("TEST_ID1", 1002L);
        param.put("NAME", "FIRST_TEST");
        param.put("EN_ID", 1001L);
        param.put("ID", id1);
        param.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.MODIFIED.getId());

        //создадим  root map
        Map root = new HashMap<String, Object>();
        root.put(ENTITY_NAME, param);

        Map rez = hier.crudByHierarchy(root);

        gen.getSession().flush();
        gen.getSession().clear();

        t.commit();
    }

    /**
     * Тест добавления записи с вложенным листом
     *
     * @throws Exception
     */
    @Test(dependsOnMethods = "testGenerte")
    public void testDaoHierarchyAdd() throws Exception {
        Transaction t = gen.getSession().beginTransaction();

        Dao3 hier = new Dao3();
        Collection<Map<String, Object>> ti = new ArrayList<Map<String, Object>>();

        //1-я запись в списе
        Map paramIn = new HashMap<String, Object>();
        paramIn.put("NAME", "FIRST1");
        paramIn.put("TEST_ID1", 1001L);
        paramIn.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.INSERTED.getId());
        ti.add(paramIn);

        //2-я запись в списе
        Map paramIn1 = new HashMap<String, Object>();
        paramIn1.put("NAME", "TOO");
        paramIn1.put("TEST_ID1", 1001L);
        paramIn1.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.INSERTED.getId());
        ti.add(paramIn1);


        //создадим  отношения Один и привяжем к нему Многие
        Map param = new HashMap<String, Object>();
        param.put(ENTITY_LIST_NAME, ti);
        param.put("TEST_ID1", 1002L);
        param.put("NAME", "FIRST");
        param.put("EN_ID", 1001L);
        param.put("ID", 1001L);
        param.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.INSERTED.getId());


        //создадим  root map
        Map root = new HashMap<String, Object>();
        root.put(ENTITY_NAME, param);

        Map rez = hier.crudByHierarchy(root);

        processRezult(rez, hier);

        gen.getSession().flush();
        gen.getSession().clear();

        t.commit();
    }


    @Test(dependsOnMethods = "testGenerte")
    public void testDaoSimple() throws Exception {
        //2-а этапа: создание, чтение

        Dao1 one = new Dao1();
        Dao many = new Dao();

        Transaction t = gen.getSession().beginTransaction();

        //создадим  отношения Многие
        //1-я запись в списе
        Map paramIn = new HashMap<String, Object>();
        paramIn.put("NAME", "FIRST1");
        paramIn.put("TEST_ID1", 1001L);
        paramIn.put(DynamicMapInstantiator.KEY, "en_Test1");
        many.save(paramIn);
        //2-я запись в списе
        Map paramIn1 = new HashMap<String, Object>();
        paramIn1.put("NAME", "FIRST");
        paramIn1.put("TEST_ID1", 1001L);
        paramIn1.put(DynamicMapInstantiator.KEY, "en_Test1");
        many.save(paramIn1);

        Collection<Map<String, Object>> ti = new ArrayList<Map<String, Object>>();
        ti.add(paramIn1);
        ti.add(paramIn);

        //создадим  отношения Один и привяжем к нему Многие
        Map param = new HashMap<String, Object>();
        param.put(ENTITY_LIST_NAME, ti);
        param.put("TEST_ID1", 1002L);
        param.put("NAME", "FIRST");
        param.put("EN_ID", 1001L);
        paramIn1.put(DynamicMapInstantiator.KEY, ENTITY_NAME);

        Map rez2 = one.save(param);

        gen.getSession().flush();
        Assert.assertNotNull(rez2);
        Assert.assertNotNull(rez2.get("ID"));

        t.commit();

        //проверим наличие записи по идентификатору
        if (rez2.get("ID") != null) {
            Map rez1 = one.findById((Long) rez2.get("ID"));
            Assert.assertNotNull(rez1);
            Assert.assertNotNull(rez1.get(ENTITY_LIST_NAME));
            Assert.assertTrue(((List) rez1.get(ENTITY_LIST_NAME)).size() > 1);
        }

        //проверим наличие записей по полю
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("NAME", "FIRST");
        List rezlist = one.findByExample(params);
        Assert.assertNotNull(rezlist);
        Assert.assertFalse(rezlist.isEmpty());


        gen.getSession().get(SadAttribute.class, 1L);

    }


}