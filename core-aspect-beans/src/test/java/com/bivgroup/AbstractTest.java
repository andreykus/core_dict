package com.bivgroup;


import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.sql.SQLException;

/**
 * Created by andreykus on 26.11.2016.
 */
public abstract class AbstractTest {
    DataSource dataSource;
    IDatabaseConnection con;


    protected DataSource getDataSource(String className, String url, String user_name, String password) throws Exception{
        DataSource dataSource;

            Class clazz = Class.forName(className);
            dataSource = (DataSource) clazz.newInstance();
            Method setURL = clazz.getDeclaredMethod("setURL", String.class);
            Method setUser = clazz.getDeclaredMethod("setUser", String.class);
            Method setPassword = clazz.getDeclaredMethod("setPassword", String.class);

            setURL.invoke(dataSource, url);
            if (user_name != null) setUser.invoke(dataSource, user_name);
            if (password != null) setPassword.invoke(dataSource, password);



        return dataSource;
    }

    abstract void init() throws Exception;

    /**
     * Инициируем соединение и заполняем данными
     */
    @Parameters({"className", "url", "shema_name", "user_name", "password"})
    @BeforeClass
    public void setUp(@org.testng.annotations.Optional("org.h2.jdbcx.JdbcDataSource") String className, @org.testng.annotations.Optional("jdbc:h2:file:C:/projects/core/core-dictionary/demo;MV_STORE=FALSE;MVCC=FALSE") String url, @org.testng.annotations.Optional String shema_name, @org.testng.annotations.Optional("sa") String user_name, @org.testng.annotations.Optional String password) throws Exception {
//        className = "oracle.jdbc.pool.OracleDataSource";
//        url = "jdbc:oracle:thin:@192.168.100.159:1521:ORCD";
//        shema_name =   "SBER_DEV_02";
//        user_name = "SBER_DEV_02";
//        password = "1";
        dataSource = getDataSource(className, url, user_name, password);
        con = new DatabaseDataSourceConnection(dataSource, shema_name);
        init();
    }
}
