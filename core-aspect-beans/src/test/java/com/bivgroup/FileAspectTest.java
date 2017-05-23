package com.bivgroup;

import com.bivgroup.common.orm.OrmProviderImpl;
import com.bivgroup.core.aspect.annotations.AspectMethod;
import com.bivgroup.core.aspect.bean.AspectCfg;
import com.bivgroup.core.aspect.bean.AspectsOnEntity;
import com.bivgroup.core.aspect.bean.AspectsOnModule;
import com.bivgroup.core.aspect.bean.BinaryFile;
import com.bivgroup.core.aspect.exceptions.AspectException;
import com.bivgroup.core.aspect.xml.XMLProcessorImpl;

import org.dbunit.DatabaseUnitException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.*;
import java.net.MalformedURLException;
import java.sql.SQLException;
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
        provider = new OrmProviderImpl(dataSource, "Generator");
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
        Map test(String name, Map param);
    }

    public class DAOTest implements DAOTestInterface {
        public DAOTest () {
          //  super(provider);
        }
        public Map test(String name, Map param){
            return new HashMap<>();
        }
    }

    @Test
    public void testsavexml() throws AspectException{
        File file = new File("c:/projects/core/core-aspect/src/test/resources/com/bivgroup/test.xml");
        AspectsOnEntity asEn = new AspectsOnEntity();
        asEn.setEntityName("ffff");
        AspectCfg cf= new AspectCfg();
        cf.setName("sdfsd");
        BinaryFile bfa = new BinaryFile();
//        bfa.setObjTableName("ddd");
//        bfa.setObjTablePKFieldName("fdsfdf");
        cf.setAspect(bfa);
        asEn.getAspectCfg().add(cf);
        AspectsOnModule colo  = new AspectsOnModule();
        colo.getAspectsOnEntity().add(asEn);
        colo.getAspectsOnEntity().add(asEn);
        XMLProcessorImpl xml = new XMLProcessorImpl();
        OutputStream  out = xml.marshalingConfig(cf);
        Assert.assertNotNull(out);
       // String asd = "<AspectCfg><name>BinaryFile</name><aspect xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"BinaryFile\"><objTableName>ddd</objTableName><objTablePKFieldName>fdsfdf</objTablePKFieldName></aspect></AspectCfg>";
        InputStream is = new ByteArrayInputStream(((ByteArrayOutputStream) out).toByteArray());
        AspectCfg el = xml.unmarshalingConfig(is);
        Assert.assertNotNull(el);
    }




}
