package com.bivgroup.core.service.impl;

import com.bivgroup.common.Constants;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;

/**
 * Created by bush on 01.08.2016.
 */
public class CoreManagerTest extends AbstractTest {

    @Test
    public void getState() throws Exception {
        Map<String, Object> rez = new CoreManagerImpl(dataSource).getState();
    }

    @Test
    public void getStateList() throws Exception {
        List<Map<String, Object>> rez = new CoreManagerImpl(dataSource).getStateList();
    }

    //-------------------------------------------------------------------------------------------------------------------

    @Test
    public void getSystemMetadataURL() throws Exception {
        String rez = coreFacade.getSystemMetadataURL(1001L);
        Assert.assertEquals(rez, "http:\\\\test");
    }

    @Test
    public void dsAccountSettingFindByLoginAndName() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("NAMES", Arrays.asList("test", "test11"));
        param.put(Constants.PARAM_LOGIN, "test");
        Map<String, Object> rez = coreFacade.dsAccountSettingFindByLoginAndName(param);
        Assert.assertTrue(rez != null);
        Assert.assertEquals(rez.get(Constants.STATUS_FIELD_NAME), Constants.STATUS_OK);
        Assert.assertEquals(rez.get("test"), "1001");
        Assert.assertEquals(rez.get("test11"), "test11");

    }

    @Test
    public void depStructure() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("DEPARTMENTID", 1002L);
        Map<String, Object> rez = coreFacade.depStructure(param);
        Assert.assertTrue(rez != null);
        Assert.assertTrue(rez.containsKey("depTree"));
        Assert.assertTrue(((Map<String, Object>) ((List) rez.get("depTree")).get(0)).containsKey("Children"));
        Assert.assertTrue(((List) ((Map<String, Object>) ((List) rez.get("depTree")).get(0)).get("Children")).size() == 2);
    }

    @Test
    public void depStructureInfo() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("DEPARTMENTID", 1003L);
        Map<String, Object> rez = coreFacade.depStructureInfo(param);
        Assert.assertTrue(rez != null);
        Assert.assertTrue(rez.size() > 0);
        Assert.assertTrue(rez.get("DEPADDRESS") != null);
        Assert.assertEquals((String) ((Map) rez.get("DEPADDRESS")).get("POSTALCODE"), "152934");
    }

    @Test
    public void dsDepartmentFindListChildIDByID() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("DepartmentID", 1002L);
        param.put("ChildSearchMode", 1);
        Map<String, Object> rez = coreFacade.dsDepartmentFindListChildIDByID(param);
        Assert.assertTrue(rez != null);
        Assert.assertTrue(rez.size() > 0);
        Assert.assertTrue(rez.get("DepartmentChildIDList") != null);
        Assert.assertTrue(((List) rez.get("DepartmentChildIDList")).size() > 0);
        Assert.assertEquals(((Map) ((List) rez.get("DepartmentChildIDList")).get(0)).get("DepartmentID").toString(), "1002");
    }

    @Test
    public void dsDepartmentFindListParentIDByID() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("DepartmentID", 1004L);
        param.put("ParentSearchMode", 1);
        Map<String, Object> rez = coreFacade.dsDepartmentFindListParentIDByID(param);
        Assert.assertTrue(rez != null);
        Assert.assertTrue(rez.size() > 0);
        Assert.assertTrue(rez.get("DepartmentParentIDList") != null);
        Assert.assertTrue(((List) rez.get("DepartmentParentIDList")).size() > 0);
        Assert.assertEquals(((Map) ((List) rez.get("DepartmentParentIDList")).get(0)).get("DepartmentID").toString(), "1004");
    }

    @Test
    public void dsDepartmentChildList() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("DEPARTMENTID", 1002L);
        Map<String, Object> rez = coreFacade.dsDepartmentChildList(param);
        Assert.assertTrue(rez != null);
        Assert.assertTrue(rez.size() > 0);
        Assert.assertTrue(rez.containsKey(Constants.RESULT_FIELD_NAME));
    }

    @Test
    public void depStructureAdd() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("DEPTLEVEL", 1002L);
        param.put("PARENTDEPARTMENT", 1003L);
        coreFacade.depStructureAdd(param);
    }

    @Test
    public void depemployeeinfofull() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("EMPLOYEEID", 1001L);
        Map<String, Object> rez = coreFacade.depemployeeinfofull(param);
        Assert.assertTrue(rez != null);
        Assert.assertTrue(rez.size() > 0);
        Assert.assertEquals((String) rez.get("EMPLOYEENAME"), "ivamov ivamov ivamovich");
    }

    @Test
    public void getExchangeList() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("purposeCurrency", "USD");
        param.put(Constants.PAGEABLE_LIST_PAGE_SIZE_NAME, 1);
        param.put(Constants.PAGEABLE_LIST_PAGE_NUM_NAME, 1);
        Map<String, Object> rez = coreFacade.getExchangeList(param);
        Assert.assertTrue(rez != null);
        Assert.assertTrue(rez.size() > 0);
        Assert.assertEquals(rez.get(Constants.PAGEABLE_LIST_COUNT_ON_PAGE_NAME), 1);
        Assert.assertEquals(((List) rez.get(Constants.PAGEABLE_LIST_NAME)).size(), 1);
        Assert.assertEquals(((Map) ((List) rez.get(Constants.PAGEABLE_LIST_NAME)).get(0)).get("TARGETCUR").toString(), "RUR");

        param.put(Constants.PAGEABLE_LIST_PAGE_NUM_NAME, 2);
        Map<String, Object> rez1 = coreFacade.getExchangeList(param);
        Assert.assertTrue(rez1 != null);
        Assert.assertTrue(rez1.size() > 0);
        Assert.assertEquals(rez1.get(Constants.PAGEABLE_LIST_COUNT_ON_PAGE_NAME), 1);
        Assert.assertEquals(((List) rez1.get(Constants.PAGEABLE_LIST_NAME)).size(), 1);
        Assert.assertEquals(((Map) ((List) rez1.get(Constants.PAGEABLE_LIST_NAME)).get(0)).get("TARGETCUR").toString(), "EUR");
    }

    @Test
    public void admGetCurrencyPairsList() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();

        param.put(Constants.PAGEABLE_LIST_PAGE_SIZE_NAME, 1);
        param.put(Constants.PAGEABLE_LIST_PAGE_NUM_NAME, 1);
        Map<String, Object> rez = coreFacade.admGetCurrencyPairsList(param);
        Assert.assertTrue(rez != null);
        Assert.assertTrue(rez.size() > 0);
        Assert.assertEquals(rez.get(Constants.PAGEABLE_LIST_COUNT_ON_PAGE_NAME), 1);
        Assert.assertEquals(((List) rez.get(Constants.PAGEABLE_LIST_NAME)).size(), 1);
        Assert.assertEquals(((Map) ((List) rez.get(Constants.PAGEABLE_LIST_NAME)).get(0)).get("BASECURRENCY").toString(), "РУБЛЬ");

        param.put(Constants.PAGEABLE_LIST_PAGE_NUM_NAME, 2);
        Map<String, Object> rez1 = coreFacade.admGetCurrencyPairsList(param);
        Assert.assertTrue(rez1 != null);
        Assert.assertTrue(rez1.size() > 0);
        Assert.assertEquals(rez1.get(Constants.PAGEABLE_LIST_COUNT_ON_PAGE_NAME), 1);
        Assert.assertEquals(((List) rez1.get(Constants.PAGEABLE_LIST_NAME)).size(), 1);
        Assert.assertEquals(((Map) ((List) rez1.get(Constants.PAGEABLE_LIST_NAME)).get(0)).get("BASECURRENCY").toString(), "ДОЛЛАР");
    }

    @Test
    public void admRefCurrencyList() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();

        param.put(Constants.PAGEABLE_LIST_PAGE_SIZE_NAME, 1);
        param.put(Constants.PAGEABLE_LIST_PAGE_NUM_NAME, 1);
        Map<String, Object> rez = coreFacade.admRefCurrencyList(param);
        Assert.assertTrue(rez != null);
        Assert.assertTrue(rez.size() > 0);
        Assert.assertEquals(rez.get(Constants.PAGEABLE_LIST_COUNT_ON_PAGE_NAME), 1);
        Assert.assertEquals(((List) rez.get(Constants.PAGEABLE_LIST_NAME)).size(), 1);
        Assert.assertEquals(((Map) ((List) rez.get(Constants.PAGEABLE_LIST_NAME)).get(0)).get("CURRENCYNAME").toString(), "ЕВРО");

        param.put(Constants.PAGEABLE_LIST_PAGE_NUM_NAME, 2);
        Map<String, Object> rez1 = coreFacade.admRefCurrencyList(param);
        Assert.assertTrue(rez != null);
        Assert.assertTrue(rez1.size() > 0);
        Assert.assertEquals(rez1.get(Constants.PAGEABLE_LIST_COUNT_ON_PAGE_NAME), 1);
        Assert.assertEquals(((List) rez1.get(Constants.PAGEABLE_LIST_NAME)).size(), 1);
        Assert.assertEquals(((Map) ((List) rez1.get(Constants.PAGEABLE_LIST_NAME)).get(0)).get("CURRENCYNAME").toString(), "РУБЛЬ");
    }

    @Test
    public void admparticipantbyid() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("USERID", 1001L);
        Map<String, Object> rez = coreFacade.admparticipantbyid(param);
        Assert.assertTrue(rez != null);
        Assert.assertTrue(rez.size() > 0);
        Assert.assertEquals((String) rez.get("EMPLOYEENAME"), "ivamov ivamov ivamovich");
    }

    @Test
    public void dsUserGetCurrentID() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        Map<String, Object> rez = coreFacade.dsUserGetCurrentID(param, "test");
        Assert.assertTrue(rez != null);
        Assert.assertTrue(rez.size() > 0);
        Assert.assertEquals(rez.get("USERACCOUNTID").toString(), "1002");
    }

    @Test
    public void admuserrolebyaccount() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("USERACCOUNTID", 1002L);
        param.put(Constants.PAGEABLE_LIST_PAGE_SIZE_NAME, 1);

        param.put(Constants.PAGEABLE_LIST_PAGE_NUM_NAME, 1);
        Map<String, Object> rez = coreFacade.admuserrolebyaccount(param);
        Assert.assertTrue(rez != null);
        Assert.assertTrue(rez.size() > 0);
        Assert.assertEquals(rez.get(Constants.PAGEABLE_LIST_COUNT_ON_PAGE_NAME), 1);
        Assert.assertEquals(((List) rez.get(Constants.PAGEABLE_LIST_NAME)).size(), 1);
        Assert.assertEquals(((Map) ((List) rez.get(Constants.PAGEABLE_LIST_NAME)).get(0)).get("ROLESYSNAME").toString(), "dba");

        param.put(Constants.PAGEABLE_LIST_PAGE_NUM_NAME, 2);
        Map<String, Object> rez1 = coreFacade.admuserrolebyaccount(param);
        Assert.assertTrue(rez1 != null);
        Assert.assertTrue(rez1.size() > 0);
        Assert.assertEquals(rez1.get(Constants.PAGEABLE_LIST_COUNT_ON_PAGE_NAME), 1);
        Assert.assertEquals(((List) rez1.get(Constants.PAGEABLE_LIST_NAME)).size(), 1);
        Assert.assertEquals(((Map) ((List) rez1.get(Constants.PAGEABLE_LIST_NAME)).get(0)).get("ROLESYSNAME").toString(), "adm");
    }

    @Test
    public void admuserrightfilterlist() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put(Constants.PARAM_LOGIN, "test");
        param.put("RIGHTSYSNAME", "test");
        Map<String, Object> rez = coreFacade.admuserrightfilterlist(param);
        Assert.assertTrue(rez != null);
        Assert.assertTrue(rez.size() > 0);
        Assert.assertTrue(rez.get(Constants.RESULT_FIELD_NAME) != null);
        Assert.assertTrue(((List) rez.get(Constants.RESULT_FIELD_NAME)).size() > 0);
    }

    @Test
    public void admuserrightlistfilterlist() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put(Constants.PARAM_LOGIN, "test");
        param.put("RIGHTSYSNAMESLIST", Arrays.asList("test"));
        Map<String, Object> rez = coreFacade.admuserrightlistfilterlist(param);
        Assert.assertTrue(rez != null);
        Assert.assertTrue(rez.size() > 0);
        Assert.assertTrue(rez.get(Constants.RESULT_FIELD_NAME) != null);
        Assert.assertTrue(((Map) rez.get(Constants.RESULT_FIELD_NAME)) != null);
        Assert.assertTrue(((Map) rez.get(Constants.RESULT_FIELD_NAME)).get("test") != null);
    }

    @Test
    public void admrightcheck() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put(Constants.PARAM_LOGIN, "test");
        param.put("RIGHTSYSNAME", "test");
        Map<String, Object> rez = coreFacade.admrightcheck(param);
        Assert.assertTrue(rez != null);
        Assert.assertTrue(rez.size() > 0);
        Assert.assertEquals(rez.get(Constants.STATUS_FIELD_NAME).toString(), Constants.STATUS_OK);
        Assert.assertEquals(rez.get(Constants.RESULT_FIELD_NAME).toString(), Constants.RESULT_TRUE);
    }

    @Test
    public void admrightchecklist() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put(Constants.PARAM_LOGIN, "test");
        List<String> param1 = new ArrayList<String>();
        param1.add("test");
        param.put("RIGHTSYSNAMESLIST", param1);
        Map<String, Object> rez = coreFacade.admrightchecklist(param);
        Assert.assertTrue(rez != null);
        Assert.assertTrue(rez.size() > 0);
        Assert.assertEquals(rez.get(Constants.STATUS_FIELD_NAME).toString(), Constants.STATUS_OK);
        Assert.assertEquals(rez.get(Constants.RESULT_FIELD_NAME).toString(), Constants.RESULT_TRUE);
    }

    @Test
    public void dsGetUsersByRole() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("ROLEID", 1001L);
        Map<String, Object> rez = coreFacade.dsGetUsersByRole(param);
        Assert.assertTrue(rez != null);
        Assert.assertTrue(rez.size() > 0);
        Assert.assertTrue(((List) rez.get(Constants.RESULT_FIELD_NAME)).size() > 0);
        Assert.assertEquals(((Map) ((List) rez.get(Constants.RESULT_FIELD_NAME)).get(0)).get("FIO").toString(), "ivamov ivamov ivamovich");
    }

    @Test(enabled = false)
    public void dsNumberFindByMask() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("MASKID", 1001L);
        Map<String, Object> rez = coreFacade.dsNumberFindByMask(param);
        Assert.assertTrue(rez != null);
        Assert.assertTrue(rez.size() > 0);

    }

    @Test
    public void createCurrencyExchange() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();

        param.put("CourseValue", 1.1);
        param.put("CourseDate", new Date());
        param.put("UnitNumber", 1);
        param.put("ExchangeType", "USD-RUR");

        Map<String, Object> rez = coreFacade.createCurrencyExchange(param);
        Assert.assertTrue(rez != null);
        Assert.assertTrue(rez.size() > 0);
        Assert.assertEquals(rez.get("CurrencyExchangeId").toString(), "1");
    }

    @Test
    public void depStructureUpdate() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        Map<String, Object> param1 = new HashMap<String, Object>();
        param1.put("POSTALCODE", "www");
        param.put("DEPPHONE", "www");
        param.put("DEPARTMENTID", 1001L);
        param.put("DEPADDRESS", param1);

        Map<String, Object> rez = coreFacade.depStructureUpdate(param);
        Assert.assertTrue(rez != null);
        Assert.assertTrue(rez.size() > 0);
        Assert.assertEquals(rez.get("DEPARTMENTID").toString(), "1001");
    }

    @Test
    public void depEmployeeAdd() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        Map<String, Object> param1 = new HashMap<String, Object>();
        param1.put("POSTALCODE", "www");

        param.put("DEPARTMENTID", 1001L);
        param.put("DEPADDRESS", param1);
        param.put("PARTICIPANTID", 1001L);
        param.put("STARTWORKDATE", new Date());
        param.put("ENDWORKDATE", new Date());

        Map<String, Object> rez = coreFacade.depEmployeeAdd(param);
        Assert.assertTrue(rez != null);
        Assert.assertTrue(rez.size() > 0);
        Assert.assertEquals(rez.get("EMPLOYEEID").toString(), "1");
    }

    @Test(dependsOnMethods = "depEmployeeAdd")
    public void depEmployeeUpdate() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        Map<String, Object> param1 = new HashMap<String, Object>();
        param1.put("POSTALCODE", "www");
        param.put("EMPLOYEEID", 1L);
        param.put("DEPARTMENTID", 1001L);
        param.put("DEPADDRESS", param1);
        param.put("PARTICIPANTID", 1001L);
        param.put("STARTWORKDATE", new Date());
        param.put("ENDWORKDATE", new Date());

        Map<String, Object> rez = coreFacade.depEmployeeUpdate(param);
        Assert.assertTrue(rez != null);
        Assert.assertTrue(rez.size() > 0);
        Assert.assertEquals(rez.get("AUDITEVENTOBJECTID").toString(), "1");
    }

    @Test
    public void admAccountFind() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put(Constants.PARAM_LOGIN, "test");
        Map<String, Object> rez = coreFacade.admAccountFind(param);
        Assert.assertTrue(rez != null);
        Assert.assertTrue(rez.size() > 0);
        Assert.assertTrue(rez.get(Constants.PAGEABLE_LIST_NAME) != null);
        Assert.assertTrue(((List) rez.get(Constants.PAGEABLE_LIST_NAME)).size() > 0);
    }

    @Test
    public void checklogin() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();

        param.put("username", "test1");
        param.put("password", "test");
        Map<String, Object> rez = coreFacade.checklogin(param);
        Assert.assertTrue(rez != null);
        Assert.assertTrue(rez.size() > 0);
        Assert.assertEquals(rez.get(Constants.RESULT_FIELD_NAME).toString(), Constants.STATUS_OK);
        Assert.assertTrue(rez.containsKey("AUTHMETHOD"));
    }

    @Test
    public void dsStartMassOper() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();

        param.put("OPERNAME", "test");
        param.put("MASSOPERTYPE", "test");
        Map<String, Object> rez = coreFacade.dsStartMassOper(param);
        Assert.assertTrue(rez != null);
        Assert.assertTrue(rez.size() > 0);
        Assert.assertEquals(rez.get(Constants.RESULT_FIELD_NAME).toString(), Constants.STATUS_OK);
        Assert.assertEquals(rez.get("MASSOPERID").toString(), "1");
    }

    @Test(dependsOnMethods = "dsStartMassOper")
    public void dsFinishMassOper() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();

        param.put("MASSOPERID", 1L);
        param.put("STATUS", "test");
        param.put("DETAIL", "test");
        Map<String, Object> rez = coreFacade.dsFinishMassOper(param);
        Assert.assertTrue(rez != null);
        Assert.assertTrue(rez.size() > 0);
        Assert.assertEquals(rez.get(Constants.RESULT_FIELD_NAME).toString(), Constants.STATUS_OK);

    }

    @Test
    public void admEmplAccountById() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("USERACCOUNTID", 1003L);
        Map<String, Object> rez = coreFacade.admEmplAccountById(param);
        Assert.assertTrue(rez != null);
        Assert.assertTrue(rez.size() > 0);
        Assert.assertEquals(rez.get(Constants.STATUS_FIELD_NAME).toString(), Constants.STATUS_OK);
        Assert.assertEquals(rez.get("FIRSTNAME").toString(), "ivamov");
    }

    @Test
    public void admroleuseradd() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("USERACCOUNTID", 1003L);
        Map<String, Object> rez = coreFacade.admroleuseradd(param);
        Assert.assertTrue(rez != null);
        Assert.assertTrue(rez.size() > 0);
        Assert.assertEquals(rez.get(Constants.STATUS_FIELD_NAME).toString(), Constants.STATUS_OK);
        Assert.assertEquals(rez.get(Constants.RESULT_FIELD_NAME).toString(), Constants.RESULT_TRUE);
    }

    @Test(priority = 1)
    public void admAccountRemove() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("USERACCOUNTID", 1003L);
        Map<String, Object> rez = coreFacade.admAccountRemove(param);
        Assert.assertTrue(rez != null);
        Assert.assertTrue(rez.size() > 0);
        Assert.assertEquals(rez.get(Constants.RESULT_FIELD_NAME).toString(), Constants.RESULT_TRUE);
    }


}