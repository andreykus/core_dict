package com.bivgroup.core.service.impl;

import com.bivgroup.core.service.CoreService;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bush on 17.08.2016.
 */
public class CoreServiceTest extends AbstractTest {
    CoreService cs;

    @BeforeClass
    public void initService() {
        this.cs = new CoreService(dataSource);
    }

    @Test
    public void admUserIsEmployee() throws Exception {
        Map param = new HashMap();
        param.put("USERID", 1001L);
        List<Map> rez = cs.invoke("admUserIsEmployee", param);
        Assert.assertTrue(rez != null);
        Assert.assertTrue(rez.size() > 0);
        Assert.assertEquals(rez.get(0).get("EMPLOYEEID").toString(), "1001");
    }

    @Test
    public void admProjectBySysname() throws Exception {
        Map param = new HashMap();
        param.put("PROJECTSYSNAME", "test");
        List<Map> rez = cs.invoke("admProjectBySysname", param);
        Assert.assertTrue(rez != null);
        Assert.assertTrue(rez.size() > 0);
        Assert.assertEquals(rez.get(0).get("PROJECTID").toString(), "1001");
    }

    @Test
    public void admUserInfo() throws Exception {
        Map param = new HashMap();
        param.put("USERACCOUNTID", 1002L);
        List<Map> rez = cs.invoke("admUserInfo", param);
        Assert.assertTrue(rez != null);
        Assert.assertTrue(rez.size() > 0);
        Assert.assertEquals(rez.get(0).get("FIRSTNAME").toString(), "ivamov");
    }

    @Test
    public void depStructureByID() throws Exception {
        Map param = new HashMap();
        param.put("DEPARTMENTID", 1003L);
        List<Map> rez = cs.invoke("depStructureByID", param);
        Assert.assertTrue(rez != null);
        Assert.assertTrue(rez.size() > 0);
        Assert.assertEquals(rez.get(0).get("DEPTZNAME").toString(), "test");
    }

    @Test
    public void getExchangeTypes() throws Exception {
        Map param = new HashMap();
        List<Map> rez = cs.invoke("getExchangeTypes", param);
        Assert.assertTrue(rez != null);
        Assert.assertTrue(rez.size() > 0);
    }

    @Test
    public void getSysSettings() throws Exception {
        Map param = new HashMap();
        List<Map> rez = cs.invoke("getSysSettings", param);
        Assert.assertTrue(rez != null);
        Assert.assertTrue(rez.size() > 0);
    }

    @Test
    public void getSysSettingBySysName() throws Exception {
        Map param = new HashMap();
        param.put("SETTINGSYSNAME", "test");
        List<Map> rez = cs.invoke("getSysSettingBySysName", param);
        Assert.assertTrue(rez != null);
        Assert.assertTrue(rez.size() > 0);
        Assert.assertEquals(rez.get(0).get("SETTINGVALUE").toString(), "1001");
    }

    @Test
    public void admDepartment() throws Exception {
        Map param = new HashMap();
        param.put("DEPTSHORTNAME", "test");
        List<Map> rez = cs.invoke("admDepartment", param);
        Assert.assertTrue(rez != null);
        Assert.assertTrue(rez.size() > 0);
        Assert.assertEquals(rez.get(0).get("DEPTSHORTNAME").toString(), "test");

    }


}