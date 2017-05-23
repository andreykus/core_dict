package com.bivgroup.core.dictionary.dao;

import com.bivgroup.common.orm.OrmException;
import com.bivgroup.common.orm.OrmProviderImpl;
import com.bivgroup.core.dictionary.entity.SadEntity;
import com.bivgroup.core.dictionary.entity.SadModule;
import com.bivgroup.core.dictionary.generator.GenerateMetadataImpl;
import oracle.jdbc.pool.OracleDataSource;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by bush on 18.10.2016.
 */
public class DaoTest {
    static String URL = "jdbc:oracle:thin:@10.10.100.250:1521:ORCD";
    static String USER_NAME = "SBER_DEV_02";
    static String SCHEMA_NAME = USER_NAME;
    static String PASSWORD = "1";
    public OracleDataSource dataSource;
    public OrmProviderImpl provider;
    public DictionaryDAO dao;

    @BeforeClass
    public void setUp() throws SQLException, DatabaseUnitException, MalformedURLException {


        dataSource = new OracleDataSource();
        dataSource.setURL(URL);
        dataSource.setUser(USER_NAME);
        dataSource.setPassword(PASSWORD);
        IDatabaseConnection con = new DatabaseDataSourceConnection(dataSource,SCHEMA_NAME);
        provider = new OrmProviderImpl(dataSource, GenerateMetadataImpl.PERSISTENT_UNIT );
        dao = new DictionaryDAO(provider);
//        IDataSet dataSet = new FlatXmlDataSetBuilder().build(new File("src/test/resources/com/bivgroup/db/TestDictionaryModule.xml"));
//        DatabaseOperation.CLEAN_INSERT.execute(con, dataSet);

    }

    @Test()
    public void testGetModulesDepend() throws OrmException {
        SadModule module = provider.getSession().get(SadModule.class, 1001L);
        List<SadModule> list = dao.getAllDependModules(module);
        Assert.assertTrue(list != null);
        if (list != null) {
            Assert.assertTrue(!list.isEmpty());
            Assert.assertEquals(list.size(), 1);
            if (list.size() == 3) {
                //TODO может быть другой порядок
                Assert.assertEquals(list.get(0).getId(), new Long(1001L));
                Assert.assertEquals(list.get(1).getId(), new Long(1002L));
                Assert.assertEquals(list.get(2).getId(), new Long(1003L));
            }
        }
    }

    @Test()
    public void testGetEntityByModule() throws OrmException {
        SadModule module = provider.getSession().get(SadModule.class, 1001L);
        List<SadEntity> list = dao.getAllEntityByModule(module);
        Assert.assertTrue(list != null);
        if (list != null) {
            Assert.assertTrue(!list.isEmpty());
            Assert.assertEquals(list.size(), 1);
            if (list.size() == 1) {
                //TODO может быть другой порядок
                Assert.assertEquals(list.get(0).getId(), new Long(1001L));
            }
        }
    }


}
