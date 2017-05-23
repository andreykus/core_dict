package com.bivgroup;

import com.bivgroup.common.orm.OrmProviderImpl;
import com.bivgroup.core.aspect.annotations.AspectMethod;
import com.bivgroup.core.aspect.annotations.AspectMethodParam;
import com.bivgroup.core.aspect.bean.AspectCfg;
import com.bivgroup.core.aspect.bean.AspectsOnEntity;
import com.bivgroup.core.aspect.bean.AspectsOnModule;
import com.bivgroup.core.aspect.bean.BinaryFile;
import com.bivgroup.core.aspect.exceptions.AspectException;
import com.bivgroup.core.aspect.proxy.AspectProxy;
import com.bivgroup.core.aspect.xml.XMLProcessorImpl;

import com.bivgroup.core.dictionary.dao.enums.RowStatus;
import com.bivgroup.core.dictionary.dao.hierarchy.ExtendGenericDAO;
import com.bivgroup.core.dictionary.dao.hierarchy.HierarchyDAO;
import com.bivgroup.core.dictionary.exceptions.DictionaryException;
import org.dbunit.DatabaseUnitException;
import org.hibernate.Criteria;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.tuple.DynamicMapInstantiator;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.*;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bush on 06.12.2016.
 */
public class FileAspectTest extends AbstractTest {
    OrmProviderImpl provider;
    // GenerateMetadata gen;

    @Override
    void init() throws DatabaseUnitException, SQLException, MalformedURLException {
        provider = new OrmProviderImpl(dataSource, "Crm");
        //  gen = new GenerateMetadataImpl(provider);
        //  SadEntity ent = gen.getSession().get(SadEntity.class, 1L);
        //SadAttribute attr = gen.getSession().get(SadAttribute.class, 1L);
//
//        IDataSet dataSet = new FlatXmlDataSetBuilder().build(new File("src/test/resources/com/bivgroup/TestDictionaryDelete.xml"));
//        DatabaseOperation.DELETE_ALL.execute(con, dataSet);
//
//        dataSet = new FlatXmlDataSetBuilder().build(new File("src/test/resources/com/bivgroup/TestDictionaryModule.xml"));
//        DatabaseOperation.INSERT.execute(con, dataSet);
//
//        dataSet = new FlatXmlDataSetBuilder().build(new File("src/test/resources/com/bivgroup/TestDictionaryAspectFile.xml"));
//        DatabaseOperation.INSERT.execute(con, dataSet);

    }

    public interface DAOTestInterface {
        @AspectMethod
        Map test(@AspectMethodParam(nameParam = "entityName") String entityName, Map params);
    }

    public class DAOTest extends HierarchyDAO implements ExtendGenericDAO, DAOTestInterface {
        public DAOTest() {
            super(provider);
        }

        public String getModuleName() {
            return "com.bivgroup.crm";
        }

        public Map test(String name, Map param) {
            return null;
        }
    }

    @Test
    public void testsavexml() throws AspectException {
        File file = new File("c:/projects/core/core-aspect/src/test/resources/com/bivgroup/test.xml");
        AspectsOnEntity asEn = new AspectsOnEntity();
        asEn.setEntityName("ffff");
        AspectCfg cf = new AspectCfg();
        cf.setName("sdfsd");
        BinaryFile bfa = new BinaryFile();
        bfa.setObjEntityName("ddd");
        bfa.setObjEntityPKFieldName("fdsfdf");
        cf.setAspect(bfa);
        asEn.getAspectCfg().add(cf);
        AspectsOnModule colo = new AspectsOnModule();
        colo.getAspectsOnEntity().add(asEn);
        colo.getAspectsOnEntity().add(asEn);
        XMLProcessorImpl xml = new XMLProcessorImpl();
        OutputStream out = xml.marshalingConfig(cf);
        Assert.assertNotNull(out);
        // String asd = "<AspectCfg><name>BinaryFile</name><aspect xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"BinaryFile\"><objTableName>ddd</objTableName><objTablePKFieldName>fdsfdf</objTablePKFieldName></aspect></AspectCfg>";
        InputStream is = new ByteArrayInputStream(((ByteArrayOutputStream) out).toByteArray());
        AspectCfg el = xml.unmarshalingConfig(is);
        Assert.assertNotNull(el);
    }

    @Test
    public void testproxymod() throws AspectException, DictionaryException {
        ExtendGenericDAO face = (ExtendGenericDAO) AspectProxy.newInstance(new DAOTest(), new Class[]{DAOTest.class});
        Transaction tr = face.getSession().getTransaction();
        tr.begin();
        Map r = face.findById("PClient", 1192L);
        r.put("INN", "3434341");
        r.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.MODIFIED.getId());
        r = face.crudByHierarchy("PClient", r);
        tr.commit();
        ((DAOTestInterface) face).test("PClient", new HashMap<>());
        // ((BinaryFilesDelegator)face).chek("CDM_PClient");
    }

    @Test
    public void testproxycreat() throws AspectException, DictionaryException {
        ExtendGenericDAO face = (ExtendGenericDAO) AspectProxy.newInstance(new DAOTest(), new Class[]{DAOTest.class});
        Transaction tr = face.getSession().getTransaction();
        tr.begin();
        Map r = new HashMap<>();
        r.put("INN", "343434111111111");
        r.put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.INSERTED.getId());
        r = face.crudByHierarchy("PClient", r);
        tr.commit();
    }

    @Test
    public void testproxymodH() throws AspectException, DictionaryException {
        ExtendGenericDAO face = (ExtendGenericDAO) AspectProxy.newInstance(new DAOTest(), new Class[]{DAOTest.class});
        Transaction tr = face.getSession().getTransaction();
        tr.begin();
        Map r = face.findById("ClientProfile", 1192L);
        ((Map) r.get("ClientID_EN")).put("INN", "343");
        ((Map) r.get("ClientID_EN")).put(RowStatus.ROWSTATUS_PARAM_NAME, RowStatus.MODIFIED.getId());
        r = face.crudByHierarchy("ClientProfile", r);
        tr.commit();
    }

    @Test
    public void testproxyCriter() throws AspectException, DictionaryException {
        System.out.println(Calendar.getInstance().get(Calendar.MONTH));
        ExtendGenericDAO face = (ExtendGenericDAO) AspectProxy.newInstance(new DAOTest(), new Class[]{DAOTest.class});
        Transaction tr = face.getSession().getTransaction();
        tr.begin();
        Criteria tbl2Criteria = face.createCriteria("ClientProfile");
        //tbl2Criteria.add(Restrictions.eq("id", id));
        Criteria tbl1Criteria = tbl2Criteria.createCriteria("ClientID_EN");//assuming thats the name of the tbl1 instance in tbl2 class
        tbl1Criteria.add(Restrictions.eq("ID", 2192L));
        face.findByCriteria("ClientProfile", tbl2Criteria);
        Map ttt1 = new HashMap<>();
        ttt1.put("ID", 2192L);
        ttt1.put("EID", 2192L);
        ttt1.put(DynamicMapInstantiator.KEY, "Client");
        Map ttt = new HashMap<>();
        ttt.put("ClientID_EN", ttt1);
        ttt.put(DynamicMapInstantiator.KEY, "ClientProfile");
        face.findByExample("ClientProfile", ttt);
    }
}