package com.bivgroup.core.service.impl;

import com.bivgroup.common.Constants;
import com.bivgroup.common.orm.interfaces.OrmProvider;
import com.bivgroup.common.utils.converter.CheckProcessor;
import com.bivgroup.common.utils.converter.DataConverter;
import com.bivgroup.core.service.interfaces.CoreManager;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Created by bush on 08.08.2016.
 */
public class AdminFacade {
    private transient Logger logger = LogManager.getLogger(this.getClass());

    static final String NAME_PARAMS = "NAMES";
    static final String SETTING_SYS_NAME = "SETTINGSYSNAME";
    static final String SETTING_SYS_VALUE = "SETTINGVALUE";
    static final String SYS_NAME = "SYSNAME";
    static final String SYS_VALUE = "VALUE";
    static final String USER_ACCOUNT_ID = "USERACCOUNTID";

    private OrmProvider orm;
    private CoreManager core;
    private CheckProcessor check;
    private DataConverter converter;

    public AdminFacade(CoreManager core, OrmProvider orm) {
        this.orm = orm;
        this.core = core;
        this.check = new CheckProcessor();
        this.converter = new DataConverter();
    }


    PasswordStrengthVerifier createPasswordStrengthVerifier() {
        return new PasswordStrengthVerifier(6, 40, 2, 3, 2);
    }

    public Map<String, Object> admaccountremove(Map<String, Object> params) throws Exception {
        orm.performNonSelectingQuery("admAccountRemove", params);
        orm.performNonSelectingQuery("admAccountRoleRemoveAll", params);
        HashMap result = new HashMap(1);
        result.put(Constants.RESULT_FIELD_NAME, Constants.RESULT_TRUE);
        return result;
    }


    public Map<String, Object> admEmplAccountById(Map<String, Object> params) throws Exception {
        try {
            Map map = null;
            List resultList = orm.getCommonListRezult("admEmplAccountById", params);
            if (resultList.size() > 0) {
                map = (Map) resultList.get(0);
                map.put(Constants.STATUS_FIELD_NAME, Constants.STATUS_OK);
            }
            return map;
        } catch (Exception var5) {
            logger.error(var5);
            throw new Exception(var5);
        }
    }

    public Map<String, Object> admuseradd(Map<String, Object> params) throws Exception {
        params.put("USERID", core.getNewId("CORE_USER"));
        if (params.get(Constants.PARAM_STATUS) == null) {
            params.put(Constants.PARAM_STATUS, Status.ACTIVE.getName());
        }

        if (params.get("BLOCKIFINACTIVE") == null) {
            params.put("BLOCKIFINACTIVE", Boolean.valueOf(true));
        }

        orm.performNonSelectingQuery("admUserAdd", params);
        HashMap result = new HashMap(4);
        result.put(Constants.RESULT_FIELD_NAME, Constants.RESULT_TRUE);
        result.put("USERID", params.get("USERID"));
        return result;
    }

    public Map<String, Object> dsGetUsersByRole(Map<String, Object> params) throws Exception {
        HashMap result = new HashMap(1);
        check.checkRequiredParams(new String[]{"ROLEID"}, params);
        if (params.get(Constants.PARAM_SEARCHTEXT) != null && !"".equals(params.get(Constants.PARAM_SEARCHTEXT))) {
            params.put(Constants.PARAM_SEARCHTEXT, "%" + params.get(Constants.PARAM_SEARCHTEXT) + "%");
        } else {
            params.remove(Constants.PARAM_SEARCHTEXT);
        }

        List resultList = orm.getCommonListRezult("admUsersByRole", params);
        result.put(Constants.STATUS_FIELD_NAME, Constants.STATUS_OK);
        result.put(Constants.RESULT_FIELD_NAME, resultList);
        converter.convertFloatToDate(result);
        return result;
    }


    public Map<String, Object> dsUserGetCurrentID(Map<String, Object> param, String login) throws Exception {
        new HashMap();
        HashMap params = new HashMap();
        params.put(Constants.PARAM_LOGIN, login);
        return orm.getCommonRecordRezult("getUserIDByAccountInfo", params);
    }

    public Map<String, Object> admparticipantbyid(Map<String, Object> params) throws Exception {
        HashMap ex = new HashMap(1);
        List resultList = orm.getCommonListRezult("admUserIsEmployee", params);
        List resultEmpList = null;
        if (resultList.size() > 0) {
            params.put("EMPLOYEEID", ((Map) resultList.get(0)).get("EMPLOYEEID"));

            resultEmpList = orm.getCommonListRezult("depEmployeeByID", params);
        } else {
            resultEmpList = orm.getCommonListRezult("admParticipantByID", params);
        }

        if (resultEmpList != null && resultEmpList.size() > 0) {
            ex.putAll((Map) resultEmpList.get(0));
            ex.put(Constants.RESULT_FIELD_NAME, Constants.STATUS_OK);
        } else {
            ex.put(Constants.RESULT_FIELD_NAME, Constants.RESULT_FALSE);
        }

        return ex;

    }

    public Map<String, Object> dsAccountSettingFindByLoginAndName(Map<String, Object> params) throws Exception {
        HashMap result = new HashMap();
        if (params.get(NAME_PARAMS) != null && params.get(NAME_PARAMS) instanceof List && !((List) params.get(NAME_PARAMS)).isEmpty()) {
            List<Map<String, Object>> resultList = orm.getCommonListRezult(SqlMapQuery.SYS_SETTING.getName(), params);
            Iterator i$ = resultList.iterator();
            Map setting;
            Iterator i$1;
            String name;
            while (i$.hasNext()) {
                setting = (Map) i$.next();
                i$1 = ((List) params.get(NAME_PARAMS)).iterator();

                while (i$1.hasNext()) {
                    name = (String) i$1.next();
                    if (setting.get(SETTING_SYS_NAME).toString().equalsIgnoreCase(name)) {
                        result.put(name, setting.get(SETTING_SYS_VALUE));
                    }
                }
            }
            if (params.get(Constants.PARAM_LOGIN) != null) {
                resultList = orm.getCommonListRezult(SqlMapQuery.IDS_BY_LOGIN.getName(), params);
                if (!resultList.isEmpty()) {
                    params.put(USER_ACCOUNT_ID, ((Map) resultList.get(0)).get(USER_ACCOUNT_ID));
                    resultList = orm.getCommonListRezult(SqlMapQuery.ACCOUNT_SETTINGS.getName(), params);
                    i$ = resultList.iterator();

                    while (i$.hasNext()) {
                        setting = (Map) i$.next();
                        i$1 = ((List) params.get(NAME_PARAMS)).iterator();

                        while (i$1.hasNext()) {
                            name = (String) i$1.next();
                            if (setting.get(SYS_NAME).toString().equalsIgnoreCase(name)) {
                                result.put(name, setting.get(SYS_VALUE));
                            }
                        }
                    }
                }
            }
        } else {
            result.put(Constants.STATUS_FIELD_NAME, Constants.STATUS_ERROR);
        }

        result.put(Constants.STATUS_FIELD_NAME, Constants.STATUS_OK);
        return result;
    }

    public Map<String, Object> dsParticipantAdd(Map<String, Object> params) throws Exception {
        params.put("PARTICIPANTID", core.getNewId("CORE_PARTICIPANT"));
        if (params.get("PARTICIPANTTYPE") == null) {
            params.put("PARTICIPANTTYPE", params.get("USERTYPE"));
        }

        if (params.get("PARTICIPANTEXTID") != null) {
            params.put("EXTERNALID", params.get("PARTICIPANTEXTID"));
        }

        Boolean blocked = Boolean.valueOf(converter.defaultMapKeyConvert(params, Status.BLOCKED.getName(), "false").toString());
        params.put("STATE", blocked.booleanValue() ? Status.BLOCKED.getName() : Status.ACTIVE.getName());
        if (params.get("CREATIONDATE") == null) {
            params.put("CREATIONDATE", converter.convertDate(new Date()));
        } else {
            converter.convertDateToFloat(params);
        }


        orm.performNonSelectingQuery("admParticipantAdd", params);
        HashMap result = new HashMap();
        result.put("AUDITEVENTOBJECTID", params.get("PARTICIPANTID"));
        result.put("PARTICIPANTID", params.get("PARTICIPANTID"));
        result.put(Constants.RESULT_FIELD_NAME, Constants.RESULT_TRUE);
        return result;
    }

    public Map<String, Object> dsAccountCreate(Map<String, Object> params) throws Exception {
        HashMap chkparam = new HashMap(1);
        chkparam.put("VALUE", params.get(Constants.PARAM_LOGIN));
        params.put("RETPASSWORD", params.get(Constants.PARAM_PASSWORD));
        List resultList = orm.getCommonListRezult("checkExist", chkparam);
        HashMap result = new HashMap(1);
        result.putAll((Map) resultList.get(0));

        Integer maxUsersCnt = Integer.valueOf(0);
        Integer maxPrivileged = Integer.valueOf(0);

        List concurrentCountResult = orm.getCommonListRezult("dsGetConcurrentUsersCount");

        List privilegedCountResult = orm.getCommonListRezult("dsGetPrivilegedUsersCount");

        if (!concurrentCountResult.isEmpty() && ((Map) concurrentCountResult.get(0)).containsKey("ConcurrentCount")) {
            maxUsersCnt = Integer.valueOf(((Number) ((Map) concurrentCountResult.get(0)).get("ConcurrentCount")).intValue());
        }

        if (!privilegedCountResult.isEmpty() && ((Map) privilegedCountResult.get(0)).containsKey("PrivilegedCount")) {
            maxPrivileged = Integer.valueOf(((Number) ((Map) privilegedCountResult.get(0)).get("PrivilegedCount")).intValue());
        }

        if (((Number) result.get("QTY")).intValue() != 0) {
            throw new Exception("Данный логин уже используется. Невозможно добавить учетную запись, если указаный логин уже существует.");
        } else {
            String userTypeSysName = String.valueOf(converter.defaultMapKeyConvert(params, "USERTYPE", "employee"));
            String userTypeId = "1";
            resultList = orm.getCommonListRezult("dsGetUsertypeRefIdBySysname", params);
            if (resultList.size() > 0 && ((Map) resultList.get(0)).get("CODE") != null) {
                userTypeId = ((Map) resultList.get(0)).get("CODE").toString();
                params.put("USERTYPE", userTypeId);
            } else {
                params.put("USERTYPE", Integer.valueOf(1));
            }

            Map checkResult;
            if (userTypeSysName.equalsIgnoreCase("employee")) {
                params.put("POSITION", "Сотрудник");
                Boolean eventParams = Boolean.valueOf(converter.defaultMapKeyConvert(params, Status.BLOCKED.getName(), "false").toString());
                params.put(Constants.PARAM_STATUS, eventParams.booleanValue() ? Status.BLOCKED.getName() : Status.ACTIVE.getName());
                if (Boolean.valueOf(converter.defaultMapKeyConvert(params, "NOTINLIST", "true").toString()).booleanValue()) {
                    checkResult = this.depemployeeadd(params);
                    if (checkResult.get(Constants.RESULT_FIELD_NAME).equals(Constants.RESULT_TRUE)) {
                        params.put("EMPLOYEEID", checkResult.get("EMPLOYEEID"));
                    }
                } else {
                    if (params.get("LASTNAME") != null && params.get("LASTNAME").toString().equals("")) {
                        params.remove("LASTNAME");
                    }

                    if (params.get("FIRSTNAME") != null && params.get("FIRSTNAME").toString().equals("")) {
                        params.remove("FIRSTNAME");
                    }

                    if (params.get("MIDDLENAME") != null && params.get("MIDDLENAME").toString().equals("")) {
                        params.remove("MIDDLENAME");
                    }

                    this.depemployeeupdate(params);
                }

                if (params.get("EMPLOYEEID") != null) {
                    resultList = orm.getCommonListRezult("dsGetUserForEmployee", params);
                    if (resultList.size() > 0 && ((Map) resultList.get(0)).get("USERID") != null) {
                        params.put("USERID", ((Map) resultList.get(0)).get("USERID"));
                    } else {
                        params.put("OBJECTTYPE", userTypeId);
                        params.put("OBJECTID", params.get("EMPLOYEEID"));
                        checkResult = this.admuseradd(params);
                        if (checkResult.get(Constants.RESULT_FIELD_NAME).equals(Constants.RESULT_TRUE)) {
                            params.put("USERID", checkResult.get("USERID"));
                        }
                    }
                }
            } else {
                if (!Boolean.valueOf(converter.defaultMapKeyConvert(params, "PARTICIPANTNOTINLIST", "true").toString()).booleanValue()) {
                    params.put("PARTICIPANTNAME", params.get("PARTICIPANTEXTNAME"));
                }

                if (params.get("PARTICIPANTTYPE") == null) {
                    params.put("PARTICIPANTTYPE", userTypeId);
                }

                Map eventParams1 = this.dsParticipantAdd(params);
                if (eventParams1.get(Constants.RESULT_FIELD_NAME).equals(Constants.RESULT_TRUE)) {
                    params.put("PARTICIPANTID", eventParams1.get("PARTICIPANTID"));
                }

                if (params.get("PARTICIPANTID") != null) {
                    params.put("OBJECTTYPE", userTypeId);
                    params.put("OBJECTID", params.get("PARTICIPANTID"));
                    checkResult = this.admuseradd(params);
                    if (checkResult.get(Constants.RESULT_FIELD_NAME).equals(Constants.RESULT_TRUE)) {
                        params.put("USERID", checkResult.get("USERID"));
                    }
                }
            }

            if (this.isDenyIfNoRoles()) {
                params.put(Constants.PARAM_STATUS, Status.BLOCKED.getName());
            }

            if (params.get("USERID") != null) {
                String eventParams2 = (String) params.get(Constants.PARAM_PASSWORD);
                if (eventParams2 == null) {
                    eventParams2 = "12345678";
                }


                PasswordStrengthVerifier.Result checkResult1 = createPasswordStrengthVerifier().isPasswordValid(eventParams2);

                params.put("PWDEXPDATE", Double.valueOf(converter.convertDate(new Date()).doubleValue() - 1.0D));
                params.put(Constants.PARAM_PASSWORD, DigestUtils.shaHex(eventParams2));

                params.put("CREATIONDATE", converter.convertDate(new Date()));
                params.put("AUTHMETHOD", Integer.valueOf(0));
                if (params.get("ISCONCURRENT") == null) {
                    params.put("ISCONCURRENT", Integer.valueOf(1));
                }

                Long userAccountId = core.getNewId("CORE_USERACCOUNT");
                params.put("USERACCOUNTID", userAccountId);
                orm.performNonSelectingQuery("admAccountAdd", params);
                if (params.get("LOCALE") != null && !converter.isEmpty(String.valueOf(params.get("LOCALE")))) {
                    params.put("USRSETTINGSYSNAME", "LOCALE");
                    params.put("USRSETTINGNAME", "LOCALE");
                    params.put("USRSETTINGVALUE", params.get("LOCALE"));
                    this.dsAccountSettingSave(params);
                }

                if (params.get("DEFAULT_PROJECT_SYSNAME") != null && !converter.isEmpty(String.valueOf(params.get("DEFAULT_PROJECT_SYSNAME")))) {
                    params.put("USRSETTINGSYSNAME", "DEFAULT_PROJECT_SYSNAME");
                    params.put("USRSETTINGNAME", "DEFAULT_PROJECT_SYSNAME");
                    params.put("USRSETTINGVALUE", params.get("DEFAULT_PROJECT_SYSNAME"));
                    this.dsAccountSettingSave(params);
                }

                if (params.get("TZTYPE") != null && !converter.isEmpty(String.valueOf(params.get("TZTYPE")))) {
                    params.put("USRSETTINGSYSNAME", "TIMEZONE_TYPE");
                    params.put("USRSETTINGNAME", "TIMEZONE_TYPE");
                    params.put("USRSETTINGVALUE", params.get("TZTYPE"));
                    this.dsAccountSettingSave(params);
                }

                if (params.get("TZDIFF") != null && !converter.isEmpty(String.valueOf(params.get("TZDIFF")))) {
                    params.put("USRSETTINGSYSNAME", "TIMEZONE_DIFF");
                    params.put("USRSETTINGNAME", "TIMEZONE_DIFF");
                    params.put("USRSETTINGVALUE", params.get("TZDIFF"));
                    this.dsAccountSettingSave(params);
                }

                if (params.get("TZNAME") != null && !converter.isEmpty(String.valueOf(params.get("TZNAME")))) {
                    params.put("USRSETTINGSYSNAME", "TIMEZONE_NAME");
                    params.put("USRSETTINGNAME", "TIMEZONE_NAME");
                    params.put("USRSETTINGVALUE", params.get("TZNAME"));
                    this.dsAccountSettingSave(params);
                }
            }

            result.clear();
            result.put("USERACCOUNTID", params.get("USERACCOUNTID"));
            result.put("USERID", params.get("USERID"));
            result.put("EMPLOYEEID", params.get("EMPLOYEEID"));
            result.put("PARTICIPANTID", params.get("PARTICIPANTID"));
            result.put(Constants.RESULT_FIELD_NAME, Constants.RESULT_TRUE);
            HashMap eventParams3 = new HashMap();
            eventParams3.put("USERACCOUNTID", params.get("USERACCOUNTID"));
            result.put("eventParams", eventParams3);
            return result;
        }
    }

    public boolean isDenyIfNoRoles() {
        return Boolean.FALSE;
    }

    protected boolean checkAccountIsAdmin(Map<String, Object> params, String[] forbiddenAdminLogins) throws Exception {
        String login = null;
        if (params.containsKey("USERACCOUNTID") || params.containsKey(Constants.PARAM_LOGIN)) {
            List resultList = orm.getCommonListRezult("getUserIDByAccountInfo", params);
            if (resultList.size() > 0) {
                login = ((Map) resultList.get(0)).get(Constants.PARAM_LOGIN).toString();
            }
        }

        if (login != null) {
            Arrays.sort(forbiddenAdminLogins);
            return Arrays.binarySearch(forbiddenAdminLogins, params.get(Constants.PARAM_LOGIN).toString()) > -1;
        } else {
            return false;
        }
    }

    public Map<String, Object> dsAccountUpdate(Map<String, Object> params) throws Exception {
        if (this.checkAccountIsAdmin(params, new String[]{"dca", "dsa", "dsso"})) {
            throw new Exception("CannotUpdateAccount");
        } else {

            Integer maxUsersCnt = Integer.valueOf(0);
            Integer maxPrivileged = Integer.valueOf(0);
            List concurrentCountResult = orm.getCommonListRezult("dsGetConcurrentUsersCount");
            List privilegedCountResult = orm.getCommonListRezult("dsGetPrivilegedUsersCount");
            if (!concurrentCountResult.isEmpty() && ((Map) concurrentCountResult.get(0)).containsKey("ConcurrentCount")) {
                maxUsersCnt = Integer.valueOf(((Number) ((Map) concurrentCountResult.get(0)).get("ConcurrentCount")).intValue());
            }

            if (!privilegedCountResult.isEmpty() && ((Map) privilegedCountResult.get(0)).containsKey("PrivilegedCount")) {
                maxPrivileged = Integer.valueOf(((Number) ((Map) privilegedCountResult.get(0)).get("PrivilegedCount")).intValue());
            }

            List resultList = null;
            params.put("USERTYPE", params.get("OBJECTTYPE"));
            if (!params.containsKey(Constants.PARAM_STATUS)) {
                Boolean eventParams = Boolean.valueOf(converter.defaultMapKeyConvert(params, Status.BLOCKED.getName(), "false").toString());
                params.put(Constants.PARAM_STATUS, eventParams.booleanValue() ? Status.BLOCKED.getName() : Status.ACTIVE.getName());
            }

            HashMap eventParams1;
            if (params.get(Constants.PARAM_STATUS).toString().equals(Status.ACTIVE.getName())) {
                if (this.isDenyIfNoRoles()) {
                    eventParams1 = new HashMap();
                    eventParams1.put("USERACCOUNTID", params.get("USERACCOUNTID"));
                    List result = orm.getCommonListRezult("admRoleListByAccount", eventParams1);
                    if (result == null || result.size() == 0) {
                        throw new Exception("roles.CannotUnblockUserWithoutRoles");
                    }
                }

                eventParams1 = new HashMap();
                eventParams1.put("USERACCOUNTID", params.get("USERACCOUNTID"));
                eventParams1.put("LASTLOGINEVENT", converter.convertDate(new Date()));

                orm.performNonSelectingQuery("UPDATEUSERLOGINTIME", eventParams1);
            }

            if (params.get("OBJECTTYPE").toString().equalsIgnoreCase("1")) {
                params.put("POSITION", "Сотрудник");
                if (params.get("EMPLOYEEID") != null) {
                    String eventParams2 = (String) params.get(Constants.PARAM_STATUS);
                    params.remove(Constants.PARAM_STATUS);
                    this.depemployeeupdate(params);
                    params.put(Constants.PARAM_STATUS, eventParams2);
                    resultList = orm.getCommonListRezult("dsGetUserForEmployee", params);
                    if (resultList.size() > 0 && ((Map) resultList.get(0)).get("USERID") != null) {
                        params.put("USERID", ((Map) resultList.get(0)).get("USERID"));
                    }
                }
            } else {
                this.dsParticipantUpdate(params);
                if (params.get("PARTICIPANTID") != null) {
                    resultList = orm.getCommonListRezult("dsGetUserForParticipant", params);
                    if (resultList.size() > 0 && ((Map) resultList.get(0)).get("USERID") != null) {
                        params.put("USERID", ((Map) resultList.get(0)).get("USERID"));
                    }
                }
            }

            if (params.get("USERID") != null && params.get("USERACCOUNTID") != null) {
                params.put("AUTHMETHOD", Integer.valueOf(0));
                if (params.get("ISCONCURRENT") == null) {
                    params.put("ISCONCURRENT", Integer.valueOf(1));
                }

                params.put("USERSTATUS", Status.ACTIVE.getName());

                orm.performNonSelectingQuery("admUserUpdate", params);
                orm.performNonSelectingQuery("admAccountUpdate", params);
                if (params.get("LOCALE") != null && !converter.isEmpty(String.valueOf(params.get("LOCALE")))) {
                    params.put("USRSETTINGSYSNAME", "LOCALE");
                    params.put("USRSETTINGNAME", "LOCALE");
                    params.put("USRSETTINGVALUE", params.get("LOCALE"));
                    this.dsAccountSettingSave(params);
                }

                if (params.get("DEFAULT_PROJECT_SYSNAME") != null && !converter.isEmpty(String.valueOf(params.get("DEFAULT_PROJECT_SYSNAME")))) {
                    params.put("USRSETTINGSYSNAME", "DEFAULT_PROJECT_SYSNAME");
                    params.put("USRSETTINGNAME", "DEFAULT_PROJECT_SYSNAME");
                    params.put("USRSETTINGVALUE", params.get("DEFAULT_PROJECT_SYSNAME"));
                    this.dsAccountSettingSave(params);
                }

                if (params.get("TZTYPE") != null && !converter.isEmpty(String.valueOf(params.get("TZTYPE")))) {
                    params.put("USRSETTINGSYSNAME", "TIMEZONE_TYPE");
                    params.put("USRSETTINGNAME", "TIMEZONE_TYPE");
                    params.put("USRSETTINGVALUE", params.get("TZTYPE"));
                    this.dsAccountSettingSave(params);
                }

                if (params.get("TZDIFF") != null && !converter.isEmpty(String.valueOf(params.get("TZDIFF")))) {
                    params.put("USRSETTINGSYSNAME", "TIMEZONE_DIFF");
                    params.put("USRSETTINGNAME", "TIMEZONE_DIFF");
                    params.put("USRSETTINGVALUE", params.get("TZDIFF"));
                    this.dsAccountSettingSave(params);
                }

                if (params.get("TZNAME") != null && !converter.isEmpty(String.valueOf(params.get("TZNAME")))) {
                    params.put("USRSETTINGSYSNAME", "TIMEZONE_NAME");
                    params.put("USRSETTINGNAME", "TIMEZONE_NAME");
                    params.put("USRSETTINGVALUE", params.get("TZNAME"));
                    this.dsAccountSettingSave(params);
                }
            }

            eventParams1 = new HashMap();
            eventParams1.put("USERACCOUNTID", params.get("USERACCOUNTID"));
            HashMap result2 = new HashMap();
            result2.put(Constants.RESULT_FIELD_NAME, Constants.RESULT_TRUE);
            result2.put("eventParams", eventParams1);
            return result2;

        }
    }

    public Map<String, Object> dsAccountSettingSave(Map<String, Object> params) throws Exception {
        List resultList = orm.getCommonListRezult("dsUserSettingGetBySysName", params);
        //params.put("USRSETTINGVALUE", ConvertTypeUtil.convertType(params.get("USRSETTINGVALUE"), String.class));
        if (resultList.size() > 0 && ((Map) resultList.get(0)).get("USRSETTINGID") != null) {
            params.put("USRSETTINGID", ((Map) resultList.get(0)).get("USRSETTINGID"));
            orm.performNonSelectingQuery("dsUserSettingUpdate", params);
        } else {
            params.put("USRSETTINGID", core.getNewId("CORE_USRSETTING"));
            orm.performNonSelectingQuery("dsUserSettingInsert", params);
        }

        HashMap result = new HashMap();
        result.put(Constants.RESULT_FIELD_NAME, Constants.RESULT_TRUE);
        return result;
    }

    public Map<String, Object> dsParticipantUpdate(Map<String, Object> params) throws Exception {
        orm.performNonSelectingQuery("depParticipantUpdate", params);
        HashMap result = new HashMap();
        result.put("AUDITEVENTOBJECTID", params.get("PARTICIPANTID"));
        result.put("PARTICIPANTID", params.get("PARTICIPANTID"));
        result.put(Constants.RESULT_FIELD_NAME, Constants.RESULT_TRUE);
        return result;
    }

    public Map<String, Object> depemployeeupdate(Map<String, Object> params) throws Exception {
        converter.convertDateToFloat(params);
        check.checkRequiredParams(new String[]{"EMPLOYEEID"}, params);
        if (params.containsKey("LASTNAME")) {
            params.put("LASTNAME", params.get("LASTNAME").toString().toUpperCase());
        }

        if (params.containsKey("FIRSTNAME")) {
            params.put("FIRSTNAME", params.get("FIRSTNAME").toString().toUpperCase());
        }

        if (params.containsKey("MIDDLENAME")) {
            params.put("MIDDLENAME", params.get("MIDDLENAME").toString().toUpperCase());
        }

        if (!params.containsKey("ENDWORKDATE")) {
            params.put("ENDWORKDATE", "NULL");
        }

        orm.performNonSelectingQuery("depEmployeeUpdate", params);
        if (params.get(Constants.PARAM_STATUS) != null && Status.DELETED.getName().equals(params.get(Constants.PARAM_STATUS).toString().trim())) {
            Map result = (Map) orm.getCommonListRezult("depEmployeeGetUserID", params).get(0);
            result.put(Constants.PARAM_STATUS, Status.DELETED.getName());
            orm.performNonSelectingQuery("dsUserSetStatus", result);
            params.put("PARTICIPANTID", params.get("EMPLOYEEID"));
            List accounts = orm.getCommonListRezult("depEmployeeAccounts", params);
            Iterator i$ = accounts.iterator();

            while (i$.hasNext()) {
                Map account = (Map) i$.next();
                account.put(Constants.PARAM_STATUS, Status.DELETED.getName());
                orm.performNonSelectingQuery("admAccountBlock", account);
            }
        }

        HashMap result1 = new HashMap();
        result1.put("AUDITEVENTOBJECTID", params.get("EMPLOYEEID"));
        result1.put(Constants.RESULT_FIELD_NAME, Constants.RESULT_TRUE);
        result1.put(Constants.STATUS_FIELD_NAME, Constants.STATUS_OK);
        return result1;
    }

    public Map<String, Object> depemployeeadd(Map<String, Object> params) throws Exception {

        params.put("EMPLOYEEID", core.getNewId("DEP_EMPLOYEE"));
        if (params.get("PARTICIPANTID") == null) {
            params.put("PARTICIPANTID", "NULL");
        }

        if (params.get("MANAGER") == null || params.get("MANAGER").equals("")) {
            params.put("MANAGER", "NULL");
        }

        if (params.get("STARTWORKDATE") == null) {
            params.put("STARTWORKDATE", "NULL");
        }

        if (params.get("ENDWORKDATE") == null) {
            params.put("ENDWORKDATE", "NULL");
        }

        if (params.get("CODE") == null) {
            params.put("CODE", "NULL");
        }

        if (params.get("PHONE1") == null) {
            params.put("PHONE1", "NULL");
        }

        if (params.get("PHONE2") == null) {
            params.put("PHONE2", "NULL");
        }

        if (params.get("EMAIL") == null) {
            params.put("EMAIL", "NULL");
        }

        converter.convertDateToFloat(params);
        if (params.containsKey("LASTNAME")) {
            params.put("LASTNAME", params.get("LASTNAME").toString().toUpperCase());
        }

        if (params.containsKey("FIRSTNAME")) {
            params.put("FIRSTNAME", params.get("FIRSTNAME").toString().toUpperCase());
        }

        if (params.containsKey("MIDDLENAME")) {
            params.put("MIDDLENAME", params.get("MIDDLENAME").toString().toUpperCase());
        }

        orm.performNonSelectingQuery("depEmployeeAdd", params);
        HashMap result = new HashMap();
        result.put("AUDITEVENTOBJECTID", params.get("EMPLOYEEID"));
        result.put("EMPLOYEEID", params.get("EMPLOYEEID"));
        result.put(Constants.RESULT_FIELD_NAME, Constants.RESULT_TRUE);
        return result;
    }


    public Map<String, Object> dsStartMassOper(Map<String, Object> params) throws Exception {
        HashMap result = new HashMap();
        check.checkRequiredParams(new String[]{"OPERNAME", "MASSOPERTYPE"}, params);
        List typesList = orm.getCommonListRezult("GETMASSOPERTYPEBYSYSNAME", params);
        if (typesList.size() != 1) {
            throw new IllegalStateException("MASSOPERTYPE not found");
        } else {
            params.put("OPERSTATUS", Status.ACTIVE.getName());
            params.put("OPERSTARTDATE", converter.convertDate(new Date()));
            params.put("OPERENDDATE", "");
            params.put("CURRENTPERCENT", Integer.valueOf(0));
            params.put("OPERSTARTER", "");// XMLUtil.getUserName(login));
            params.put("MASSOPERTYPEID", ((Map) typesList.get(0)).get("MASSOPERTYPEID"));
            long massoperid = core.getNewId("CORE_MASSOPER");
            params.put("ID", Long.valueOf(massoperid));
            orm.performNonSelectingQuery("ADDMASSOPER", params);
            result.put("MASSOPERID", Long.valueOf(massoperid));
            result.put(Constants.RESULT_FIELD_NAME, Constants.STATUS_OK);
            return result;
        }
    }

    public Map<String, Object> dsFinishMassOper(Map<String, Object> params) throws Exception {
        HashMap result = new HashMap();

        check.checkRequiredParams(new String[]{Constants.PARAM_STATUS, "MASSOPERID"}, params);
        if (params.get(Constants.PARAM_STATUS).toString().equalsIgnoreCase(Constants.STATUS_OK)) {
            params.put("OPERSTATUS", "COMPLETED");
            params.put("CURRENTPERCENT", Integer.valueOf(100));
        } else {
            params.put("OPERSTATUS", Constants.STATUS_ERROR);
        }

        List massState = orm.getCommonListRezult("GETMASSOPERSTATEBYID", params);
        if (massState.size() != 1) {
            throw new Exception("MassOperNotFound");
        } else {
            orm.performNonSelectingQuery("CHANGEOPERSTATUS", params);
            result.put(Constants.RESULT_FIELD_NAME, Constants.STATUS_OK);
            return result;
        }
    }


    public Map<String, Object> admaccountfind(Map<String, Object> param) throws Exception {
        try {
            if ("".equals(param.get(Constants.PARAM_LOGIN))) {
                param.remove(Constants.PARAM_LOGIN);
            }

            if ("".equals(param.get("PHONE"))) {
                param.remove("PHONE");
            }

            if ("".equals(param.get("EMPLOYEENAME"))) {
                param.remove("EMPLOYEENAME");
            }

            if ("".equals(param.get("EMAIL"))) {
                param.remove("EMAIL");
            }

            if ("".equals(param.get("ROLE"))) {
                param.remove("ROLE");
            }

            if ("".equals(param.get("GROUP"))) {
                param.remove("GROUP");
            }

            if ("".equals(param.get("ACCOUNTTYPE"))) {
                param.remove("ACCOUNTTYPE");
            }

            if ("".equals(param.get("DEPARTMENT"))) {
                param.remove("DEPARTMENT");
            }

            if (param.containsKey("DEPARTMENT")) {
                param.put("DEPARTMENTID", param.get("DEPARTMENT"));
                StringBuilder e = new StringBuilder(param.get("DEPARTMENT").toString());
                Map departmentChildList = core.dsDepartmentChildList(param);
                if (departmentChildList.get(Constants.RESULT_FIELD_NAME) instanceof List) {
                    List depChildList = (List) departmentChildList.get(Constants.RESULT_FIELD_NAME);
                    Iterator i$ = depChildList.iterator();

                    while (i$.hasNext()) {
                        Map depInfo = (Map) i$.next();
                        e.append(", ");
                        e.append(depInfo.get("DEPARTMENTID"));
                    }

                    param.put("DEPARTMENT", e.toString());
                }
            }

            if ("".equals(param.get("OBJECTTYPE"))) {
                param.remove("OBJECTTYPE");
            }

            if (param.get("OBJECTTYPE") instanceof String) {
                param.put("OBJECTTYPE", Integer.valueOf((String) param.get("OBJECTTYPE")));
            }

            if (param.get("EMPLOYEENAME") != null) {
                param.put("EMPLOYEENAME", String.valueOf(param.get("EMPLOYEENAME")).toUpperCase());
            }


            param.put("CURRENT_DATE", DataConverter.convertDateToDuble(new Date()));

            param.put("TODATE", DataConverter.convertDateToDuble(new Date()));
            Map e1 = orm.getCommonListPageRezult("admAccountFind", param);
            converter.convertFloatToDate(e1);
            return e1;
        } catch (Exception var7) {
            logger.error(var7);
            throw new Exception(var7);
        }
    }


    public Map<String, Object> checklogin(Map<String, Object> params) throws Exception {
        HashMap result = new HashMap(1);
        List dataSQL = orm.getCommonListRezult("GETLOGINOLD", params);
        if (dataSQL.size() > 0) {
            result.put("AUTHMETHOD", ((Map) dataSQL.get(0)).get("AUTHMETHOD"));
        }
        result.put(Constants.RESULT_FIELD_NAME, Constants.STATUS_OK);

        return result;
    }

    public enum SqlMapQuery {
        SYS_SETTING("getSysSettings"),
        IDS_BY_LOGIN("getIDsByLogin"),
        ACCOUNT_SETTINGS("dsAccountSettings");
        private String query;

        SqlMapQuery(String query) {
            this.query = query;
        }

        public String getName() {
            return query;
        }
    }
}
