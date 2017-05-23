package com.bivgroup.core.service.impl;

import com.bivgroup.common.Constants;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by andreykus on 25.08.2016.
 */
public class CoreAccountTest extends AbstractTest {

    @Test(priority = 1)
    public void dsAccountCreate() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("ROLEID", 1001L);
        param.put("PARTICIPANTID", 1001L);

        param.put("STARTWORKDATE", new Date());
        param.put("ENDWORKDATE", new Date());

        param.put("MANAGERID", 1001L);
        param.put("DEPARTMENTID", 1001L);
        param.put("EMPSTATUS", Status.ACTIVE.getName());
        param.put("EMPPOSITION", 1);
        param.put("EMPCODE", 1);
        param.put("EMPLOYEENAME", "");

        param.put("DESCRIPTION", "");
        param.put(Constants.PARAM_LOGIN, "");
        param.put("EXTERNALID", 1);

        Map<String, Object> rez = coreFacade.dsAccountCreate(param);
        Assert.assertTrue(rez != null);
        Assert.assertTrue(rez.size() > 0);
        Assert.assertEquals(rez.get("USERACCOUNTID"), 1L);
    }

    @Test(priority = 2)
    public void dsAccountUpdate() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("USERACCOUNTID", 1L);
        param.put("EMPLOYEEID", 1001L);
        param.put("OBJECTTYPE", 1);
        param.put(Constants.PARAM_LOGIN, "login");
        param.put("ENDWORKDATE", new Date());
        param.put("DEFAULT_PROJECT_SYSNAME", "test");

//        param.put("CODE", 1);
//        param.put("STATUS","ACTIVE");
//        param.put("MIDDLENAME", "login");
//        param.put("DEPARTMENTID", 1001L);
//        param.put("LASTNAME", "login");
//        param.put("MANAGER", 1001L);
//        param.put("FIRSTNAME", "login");
//        param.put("PHONE2", "login");
//        param.put("EMAIL", "login");
//        param.put("PHONE1", "login");


        Map<String, Object> rez = coreFacade.dsAccountUpdate(param);
        Assert.assertTrue(rez != null);
        Assert.assertTrue(rez.size() > 0);
        Assert.assertEquals(rez.get(Constants.RESULT_FIELD_NAME), Constants.RESULT_TRUE);
    }
}
