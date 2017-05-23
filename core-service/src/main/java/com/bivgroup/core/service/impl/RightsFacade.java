package com.bivgroup.core.service.impl;

import com.bivgroup.common.orm.interfaces.OrmProvider;
import com.bivgroup.core.service.interfaces.CoreManager;
import com.bivgroup.common.Constants;
import com.bivgroup.common.utils.converter.CheckProcessor;
import com.bivgroup.common.utils.converter.DataConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by bush on 11.08.2016.
 */
public class RightsFacade {
    private transient Logger logger = LogManager.getLogger(this.getClass());

    private static final TreeSet<String> FORBIDDEN_ROLES_PAIRS = new TreeSet();

    private OrmProvider orm;
    private CoreManager core;
    private CheckProcessor check;
    private DataConverter converter;

    private Map<String, List> adminRoleMap = new HashMap();

    static {
        //initCommandAuditDescription();
        FORBIDDEN_ROLES_PAIRS.add("dsa:dsso");
        FORBIDDEN_ROLES_PAIRS.add("dsa:tbp");
        FORBIDDEN_ROLES_PAIRS.add("dsa:dsg");
        FORBIDDEN_ROLES_PAIRS.add("dca:dsso");
        FORBIDDEN_ROLES_PAIRS.add("dca:tbp");
        FORBIDDEN_ROLES_PAIRS.add("dca:dsg");
        FORBIDDEN_ROLES_PAIRS.add("dsa:dsso");
        FORBIDDEN_ROLES_PAIRS.add("dsa:tbp");
        FORBIDDEN_ROLES_PAIRS.add("dsa:dsg");
        FORBIDDEN_ROLES_PAIRS.add("dsso:dsa");
        FORBIDDEN_ROLES_PAIRS.add("dsso:dca");
        FORBIDDEN_ROLES_PAIRS.add("dsso:dsg");
        FORBIDDEN_ROLES_PAIRS.add("tbp:dsa");
        FORBIDDEN_ROLES_PAIRS.add("tbp:dca");
        FORBIDDEN_ROLES_PAIRS.add("tbp:dca");
        FORBIDDEN_ROLES_PAIRS.add("dca:dsso");
        FORBIDDEN_ROLES_PAIRS.add("dca:tbp");
        FORBIDDEN_ROLES_PAIRS.add("dca:dsg");
    }

    public RightsFacade(CoreManager core, OrmProvider orm) {
        this.orm = orm;
        this.core = core;
        this.check = new CheckProcessor();
        this.converter = new DataConverter();
    }


    public Map<String, Object> admuserrightfilterlist(Map<String, Object> params) throws Exception {
        return this.createResult(params, Constants.STATUS_OK, this.getUserRightFilters(params));
    }

    public Map<String, Object> admuserrightlistfilterlist(Map<String, Object> params) throws Exception {
        return this.createResult(params, Constants.STATUS_OK, this.getUserRightListFilters(params));
    }

    public Map<String, Object> admuserrolebyaccount(Map<String, Object> param) throws Exception {
        Map rez = orm.getCommonListPageRezult("admRoleListByAccount", param);
        return rez;
    }

    public Map<String, Object> admrightchecklist(Map<String, Object> params) throws Exception {
        return this.createResult(params, Constants.STATUS_OK, Boolean.valueOf(this.checkPermissionsList(params)));
    }

    public Map<String, Object> admrightcheck(Map<String, Object> params) throws Exception {
        return this.checkAdminRights(params) ? this.createResult(params, Constants.STATUS_OK, Boolean.valueOf(true)) : this.createResult(params, Constants.STATUS_OK, Boolean.valueOf(this.admRightCheckInternal(params)));
    }

    public Map<String, Object> admaccountrolelist(Map<String, Object> params) throws Exception {
        check.checkRequiredParams(new String[]{"USERACCOUNTID"}, params);
        if (!params.containsKey(Constants.PARAM_ORDERBY)) {
            params.put(Constants.PARAM_ORDERBY, "ROLENAME");
        }

        if (!params.containsKey(Constants.PARAM_SEARCHNAME)) {
            params.put(Constants.PARAM_SEARCHNAME, "");
        }
        Map result = new HashMap();
        result.put(Constants.RESULT_FIELD_NAME, orm.getCommonListRezult("admAccountRoleList", params));
        return result;
    }

    protected void checkRoleAssignment(Map<String, Object> params) throws Exception {
        String roleToAssignSysname = "";
        List roleToAssign = orm.getCommonListRezult("admUserRoleByID", params);
        if (roleToAssign != null && roleToAssign.size() > 0) {
            roleToAssignSysname = String.valueOf(((Map) roleToAssign.get(0)).get("ROLESYSNAME"));
        }

        ArrayList assignedRolesList = new ArrayList();
        ArrayList loginsList = new ArrayList();
        orm.getCommonListRezult("getUserIDByAccountInfo", params);
        List resultList = orm.getCommonListRezult("getUserIDByAccountInfo", params);
        Iterator i$;
        if (resultList != null && resultList.size() > 0) {
            HashMap isRoleToAssignAdministrative = new HashMap();
            if (((Map) resultList.get(0)).containsKey("UserID")) {
                isRoleToAssignAdministrative.put("USERID", ((Map) resultList.get(0)).get("UserID"));
            } else {
                isRoleToAssignAdministrative.put("USERID", ((Map) resultList.get(0)).get("USERID"));
            }

            List isAssignmentToDSSO = orm.getCommonListRezult("admAccountFind", isRoleToAssignAdministrative);
            if (isAssignmentToDSSO != null && isAssignmentToDSSO.size() > 0) {
                i$ = isAssignmentToDSSO.iterator();

                while (i$.hasNext()) {
                    Object assignLogin = i$.next();
                    if (assignLogin instanceof Map) {
                        loginsList.add(((Map) assignLogin).get(Constants.PARAM_LOGIN).toString());
                        Map pair = new HashMap();
                        pair.put("USERACCOUNTID", ((Map) assignLogin).get("USERACCOUNTID"));
                        Map reversePair = this.admaccountrolelist(pair);
                        if (reversePair != null && reversePair.get(Constants.RESULT_FIELD_NAME) != null && reversePair.get(Constants.RESULT_FIELD_NAME) instanceof List) {
                            assignedRolesList.addAll((List) reversePair.get(Constants.RESULT_FIELD_NAME));
                        }
                    }
                }
            }
        }

        boolean isRoleToAssignAdministrative1 = roleToAssignSysname.equalsIgnoreCase("dsa") || roleToAssignSysname.equalsIgnoreCase("dsso") || roleToAssignSysname.equalsIgnoreCase("dca") || roleToAssignSysname.equalsIgnoreCase("tbp") || roleToAssignSysname.equalsIgnoreCase("dsg");
        String assignLogin1;
        if (isRoleToAssignAdministrative1) {
            label100:
            {
                Iterator isAssignmentToDSSO1 = assignedRolesList.iterator();

                String pair1;
                String reversePair1;
                do {
                    if (!isAssignmentToDSSO1.hasNext()) {
                        break label100;
                    }

                    Map i$1 = (Map) isAssignmentToDSSO1.next();
                    assignLogin1 = String.valueOf(i$1.get("ROLESYSNAME"));
                    pair1 = (roleToAssignSysname + ":" + assignLogin1).toLowerCase();
                    reversePair1 = (assignLogin1 + ":" + roleToAssignSysname).toLowerCase();
                } while (!FORBIDDEN_ROLES_PAIRS.contains(pair1) && !FORBIDDEN_ROLES_PAIRS.contains(reversePair1));

                throw new Exception("CannotAssignRole");
            }
        }

        if (!isRoleToAssignAdministrative1) {
            boolean isAssignmentToDSSO2 = false;
            i$ = loginsList.iterator();

            while (i$.hasNext()) {
                assignLogin1 = (String) i$.next();
                if ("dsso".equalsIgnoreCase(assignLogin1)) {
                    isAssignmentToDSSO2 = true;
                    break;
                }
            }

            if (isAssignmentToDSSO2) {
                throw new Exception("CannotAssignRole");
            }
        }

    }

    public boolean isDenyIfNoRoles() {
        return Boolean.FALSE;
    }


    public Map<String, Object> admrightremove(Map<String, Object> params) throws Exception {
        try {
            ArrayList e = new ArrayList();
            Object rightOb = params.get("RIGHTID");
            if (rightOb instanceof Iterable) {
                Iterator currentDate = ((Iterable) rightOb).iterator();

                while (currentDate.hasNext()) {
                    Object i$ = currentDate.next();
                    e.add(i$);
                }
            } else {
                e.add(rightOb);
            }

            Double currentDate1 = DataConverter.convertDateToDuble(Calendar.getInstance().getTime());
            Iterator i$1 = e.iterator();

            while (i$1.hasNext()) {
                Object rightId = i$1.next();
                params.put("RIGHTID", rightId);
                params.put("ENDDATE", currentDate1);
                String checkQueryName = null;
                String updateName = null;
                String removeName = null;
                if (params.get("RIGHTOWNER").equals("ROLE")) {
                    checkQueryName = "admRightUserRoleFind";
                    updateName = "admRightUserRoleUpdate";
                    removeName = "admRightUserRoleRemove";
                } else if (params.get("RIGHTOWNER").equals("USERGROUP")) {
                    checkQueryName = "admRightUserGroupFind";
                    updateName = "admRightUserGroupUpdate";
                    removeName = "admRightUserGroupRemove";
                } else if (params.get("RIGHTOWNER").equals("ACCOUNT")) {
                    checkQueryName = "admRightAccountFind";
                    updateName = "admRightAccountUpdate";
                    removeName = "admRightAccountRemove";
                } else {
                    if (!params.get("RIGHTOWNER").equals("DEPARTMENT")) {
                        System.err.println("***** не правильное значение параметра RIGHTOWNER");
                        throw new Exception("WrongRIGHTOWNERParameter");
                    }

                    checkQueryName = "admRightDepartmentFind";
                    updateName = "admRightDepartmentUpdate";
                    removeName = "admRightDepartmentRemove";
                }

                params.put("CURRENTDATE", currentDate1);
                List assigned = orm.getCommonListRezult(checkQueryName, params);
                if (assigned != null && assigned.size() > 0) {
                    params.put("RIGHTOBJECTID", ((Map) assigned.get(0)).get("RIGHTOBJECTID"));
                    Object isException = ((Map) assigned.get(0)).get("ISEXCEPTION");
                    if (!params.containsKey("EXCEPTIONMODE")) {
                        params.put("EXCEPTIONMODE", Integer.valueOf(0));
                    }

                    if (!params.containsKey("ISEXCEPTION")) {
                        params.put("ISEXCEPTION", Integer.valueOf(0));
                    }

                    orm.performNonSelectingQuery(removeName, params);
                }
            }

            return this.createResult(params, Constants.STATUS_OK, Constants.RESULT_TRUE);
        } catch (Exception var13) {
            throw new Exception(var13);
        }
    }

    private void blockAssignedNonRoleRights(Map<String, Object> params) throws Exception {
        String roleToAssignSysname = "";
        List roleToAssign = orm.getCommonListRezult("admUserRoleByID", params);
        if (roleToAssign != null && roleToAssign.size() > 0) {
            roleToAssignSysname = String.valueOf(((Map) roleToAssign.get(0)).get("ROLESYSNAME"));
        }

        if (roleToAssignSysname.equalsIgnoreCase("dsa") || roleToAssignSysname.equalsIgnoreCase("dsso") || roleToAssignSysname.equalsIgnoreCase("dca") || roleToAssignSysname.equalsIgnoreCase("tbp") || roleToAssignSysname.equalsIgnoreCase("dsg")) {
            HashMap rightParams = new HashMap();
            rightParams.put("USERACCOUNTID", params.get("USERACCOUNTID"));

            List rightsList = orm.getCommonListRezult("admRightAccountAllAssigned", rightParams);
            ArrayList idList = new ArrayList();
            Iterator removeRightMap = rightsList.iterator();

            while (removeRightMap.hasNext()) {
                Map right = (Map) removeRightMap.next();
                idList.add(right.get("RIGHTID"));
            }

            if (idList.size() > 0) {
                Map removeRightMap1 = new HashMap();
                removeRightMap1.put("RIGHTID", idList);
                removeRightMap1.put("RIGHTOWNER", "ACCOUNT");
                removeRightMap1.put("OBJECTID", params.get("USERACCOUNTID"));
                this.admrightremove(removeRightMap1);
            }
        }

    }

    public Map<String, Object> admroleuseradd(Map<String, Object> params) throws Exception {
        this.checkRoleAssignment(params);
        this.blockAssignedNonRoleRights(params);

        converter.convertDateToFloat(params);
        List exists = orm.getCommonListRezult("admRoleUserCheckExists", params);
        if (exists != null && exists.size() > 0) {
            throw new Exception("AccountAlreadyHasRole");
        } else {
            params.put("ROLEACCOUNTID", core.getNewId("CORE_ROLEACCOUNT"));

            orm.performNonSelectingQuery("admRoleUserAdd", params);
            HashMap result = new HashMap();
            result.put(Constants.STATUS_FIELD_NAME, Constants.STATUS_OK);
            result.put(Constants.RESULT_FIELD_NAME, Constants.RESULT_TRUE);
            result.put("AUDITEVENTOBJECTID", params.get("ROLEID"));
            if (this.isDenyIfNoRoles()) {
                HashMap statusParams = new HashMap();
                statusParams.put("USERACCOUNTID", params.get("USERACCOUNTID"));
                statusParams.put(Constants.PARAM_STATUS, Status.ACTIVE.getName());
                orm.performNonSelectingQuery("dsUserAccountSetStatus", statusParams);
            }

            return result;
        }
    }

    public String admuserrole(Map<String, Object> param) throws Exception {
        if (param.get(Constants.PARAM_SEARCHTEXT) == null) {
            param.put(Constants.PARAM_SEARCHTEXT, "");
        }

        if (param.get(Constants.PARAM_SEARCHCOLUMN) == null) {
            param.put(Constants.PARAM_SEARCHCOLUMN, "ROLESYSNAME");
        }

        if (param.get("SEARCHPROJECT") == null || "".equals(param.get("SEARCHPROJECT"))) {
            param.put("SEARCHPROJECT", "0");
        }

        Map e = orm.getCommonListPageRezult("admUserRole", param);
        // return (new XMLUtil(true, true)).createXML(e);
        return "";
    }

    private boolean checkPermissionsList(Map<String, Object> params) throws Exception {
        boolean result = false;
        check.checkRequiredParams(new String[]{"RIGHTSYSNAMESLIST", Constants.PARAM_LOGIN}, params);
        Object rightsListObj = params.get("RIGHTSYSNAMESLIST");
        if (rightsListObj instanceof List) {
            List rightsList = (List) rightsListObj;
            Iterator iterator = rightsList.iterator();
            result = true;
            while (iterator.hasNext()) {
                Object object = iterator.next();
                Map oneRightCheckParams = new HashMap();
                if (object instanceof String) {
                    oneRightCheckParams.put(Constants.PARAM_LOGIN, params.get(Constants.PARAM_LOGIN));
                    oneRightCheckParams.put("RIGHTSYSNAME", object.toString());
                }

                if (object instanceof Map) {
                    check.checkRequiredParams(new String[]{"RIGHTSYSNAME"}, (Map) object);
                    oneRightCheckParams.put(Constants.PARAM_LOGIN, params.get(Constants.PARAM_LOGIN));
                    oneRightCheckParams.putAll((Map) object);
                }

                result &= this.admRightCheckInternal(oneRightCheckParams);
                if (!result) {
                    return result;
                }
            }
        }

        return result;
    }


    protected Map<String, Object> createResult(Map<String, Object> param, String status, Object resultObject) {
        HashMap result = new HashMap();
        result.put(Constants.STATUS_FIELD_NAME, status);
        if (Constants.STATUS_OK.equals(status) && resultObject != null) {

            if (param.get("ReturnAsHashMap") == null) {
                param.put("ReturnAsHashMap", "");
            }
            if (Constants.RESULT_TRUE.equalsIgnoreCase((String) param.get("ReturnAsHashMap")) && resultObject instanceof Map) {
                result.putAll((Map) resultObject);
            } else {
                result.put(Constants.RESULT_FIELD_NAME, resultObject);
                result.put("AUDITEVENTOBJECTID", resultObject);
            }
        }

        return result;
    }

    protected List<Map<String, Object>> getUserRightFilters(Map<String, Object> params) throws Exception {
        check.checkRequiredParams(new String[]{"RIGHTSYSNAME", Constants.PARAM_LOGIN}, params);
        params.put("CURRENTDATE", converter.convertDate(Calendar.getInstance().getTime()));
        return orm.getCommonListRezult("admRightFilterValueCheckList", params);
    }

    protected Map<String, List<Map<String, Object>>> getUserRightListFilters(Map<String, Object> params) throws Exception {
        check.checkRequiredParams(new String[]{"RIGHTSYSNAMESLIST", Constants.PARAM_LOGIN}, params);
        HashMap result = new HashMap();
        Object rightsListObj = params.get("RIGHTSYSNAMESLIST");
        if (rightsListObj instanceof List) {
            List rightsList = (List) rightsListObj;

            Object object;
            HashMap oneRightFiltersParams;
            for (Iterator i$ = rightsList.iterator(); i$.hasNext(); result.put(object.toString(), this.getUserRightFilters(oneRightFiltersParams))) {
                object = i$.next();
                oneRightFiltersParams = new HashMap();
                if (object instanceof String) {
                    oneRightFiltersParams.put(Constants.PARAM_LOGIN, params.get(Constants.PARAM_LOGIN));
                    oneRightFiltersParams.put("RIGHTSYSNAME", object.toString());
                }
            }
        }

        return result;
    }

    private boolean calculateRightFilterCheckResult(Set<String> keys, Map<String, Boolean> positive, Map<String, Boolean> negative) {
        boolean result = true;

        String key;

        for (Iterator iss = keys.iterator(); iss.hasNext(); result = result && (Boolean) converter.defaultMapKeyConvert(positive, key, Boolean.valueOf(true)).booleanValue() && (Boolean) converter.defaultMapKeyConvert(negative, key, Boolean.valueOf(true)).booleanValue()) {
            key = (String) iss.next();
        }

        return result;
    }

    public boolean like(String paramValue, String filterValue) {
        String re = "";
        if (paramValue == null) {
            paramValue = "";
        }

        int i;
        String p;
        do {
            i = filterValue.indexOf(91);
            int k = i;
            if (i < 0) {
                k = filterValue.length();
            }

            p = filterValue.substring(0, k);
            p = p.replaceAll("%", "\\\\E.*?\\\\Q");
            p = p.replaceAll("_", "\\\\E.\\\\Q");
            re = re + "\\Q" + p + "\\E";
            if (i >= 0) {
                int e = filterValue.indexOf(93, k);
                if (e < 0) {

                    return false;
                }

                re = re + filterValue.substring(k, e + 1);
                filterValue = filterValue.substring(e + 1);
            }
        } while (i >= 0);

        p = null;

        Pattern p1;
        try {
            p1 = Pattern.compile(re, 32);
        } catch (PatternSyntaxException var8) {

            return false;
        }

        return p1.matcher(paramValue).matches();
    }

    protected boolean checkRightParams(Map<String, Object> rightParams, List<Map<String, Object>> filters) {
        if (rightParams == null) {
            rightParams = Collections.EMPTY_MAP;
        }

        String prevFilterId = null;
        Integer accessMode = Integer.valueOf(0);
        HashSet keys = new HashSet();
        Map positive = new HashMap();
        Map negative = new HashMap();
        Iterator result = filters.iterator();

        Map filter;
        String filterId;
        String key;
        String operation;
        String filterValue;
        Boolean anyValue;
        String paramValue;
        while (result.hasNext()) {
            filter = (Map) result.next();
            if (((Number) filter.get("ISEXCEPTION")).intValue() != 0) {
                filterId = "" + filter.get("SOURCETYPE") + " " + filter.get("SOURCE") + " (relation id: " + filter.get("RELATIONID") + ")";
                accessMode = Integer.valueOf(((Number) filter.get("EXCEPTIONMODE")).intValue());
                key = (String) filter.get("FILTER");
                if (key == null) {
                    return accessMode.intValue() == 1;
                }

                keys.add(key);
                operation = filter.get("OPERATION").toString();
                filterValue = filter.get("VALUE").toString().trim();
                anyValue = new Boolean(filter.get("ANYVALUE") != null && filter.get("ANYVALUE").toString().equalsIgnoreCase("true") || filter.get("ANYVALUE").toString().equalsIgnoreCase("1"));
                paramValue = null;
                if (rightParams.get(key) != null) {
                    paramValue = rightParams.get(key).toString().trim();
                }

                if (!OPERATION.EQUALS.equals(operation) && !OPERATION.IN.equals(operation)) {
                    if (!OPERATION.NOT_EQUALS.equals(operation) && !OPERATION.NOT_IN.equals(operation)) {
                        if (OPERATION.LIKE.equals(operation)) {
                            if (positive.get(key) == null) {
                                positive.put(key, Boolean.valueOf(false));
                            }
                            positive.put(key, Boolean.valueOf(((Boolean) positive.get(key)).booleanValue() || this.like(paramValue, filterValue) || anyValue.booleanValue()));
                        } else if (OPERATION.NOT_LIKE.equals(operation)) {
                            if (negative.get(key) == null) {
                                negative.put(key, Boolean.valueOf(true));
                            }
                            negative.put(key, Boolean.valueOf(((Boolean) negative.get(key)).booleanValue() && !this.like(paramValue, filterValue) && !anyValue.booleanValue()));
                        }
                    } else {
                        if (negative.get(key) == null) {
                            negative.put(key, Boolean.valueOf(true));
                        }
                        negative.put(key, Boolean.valueOf(((Boolean) negative.get(key)).booleanValue() && !filterValue.equals(paramValue) && !anyValue.booleanValue()));
                    }
                } else {
                    if (positive.get(key) == null) {
                        positive.put(key, Boolean.valueOf(false));
                    }
                    positive.put(key, Boolean.valueOf(((Boolean) positive.get(key)).booleanValue() || filterValue.equals(paramValue) || anyValue.booleanValue()));
                }

                boolean result1 = this.calculateRightFilterCheckResult(keys, positive, negative);

                return result1 && accessMode.intValue() == 1;
            }
        }

        result = filters.iterator();

        while (true) {
            while (true) {
                while (true) {
                    while (result.hasNext()) {
                        filter = (Map) result.next();
                        filterId = "" + filter.get("SOURCETYPE") + " " + filter.get("SOURCE") + " (relation id: " + filter.get("RELATIONID") + ")";
                        if (!filterId.equals(prevFilterId) && prevFilterId != null) {
                            boolean key1 = this.calculateRightFilterCheckResult(keys, positive, negative);
                            if (key1) {
                                return accessMode.intValue() == 1;
                            }

                            positive.clear();
                            negative.clear();
                            keys.clear();
                        }

                        prevFilterId = filterId;
                        accessMode = Integer.valueOf(((Number) filter.get("ACCESSMODE")).intValue());
                        key = (String) filter.get("FILTER");
                        if (key == null) {
                            return accessMode.intValue() == 1;
                        }

                        keys.add(key);
                        operation = filter.get("OPERATION").toString();
                        filterValue = filter.get("VALUE").toString().trim();
                        anyValue = new Boolean(filter.get("ANYVALUE") != null && filter.get("ANYVALUE").toString().equalsIgnoreCase("true") || filter.get("ANYVALUE").toString().equalsIgnoreCase("1"));
                        paramValue = null;
                        if (rightParams.get(key) != null) {
                            paramValue = rightParams.get(key).toString().trim();
                        }

                        if (!OPERATION.EQUALS.equals(operation) && !OPERATION.IN.equals(operation)) {
                            if (!OPERATION.NOT_EQUALS.equals(operation) && !OPERATION.NOT_IN.equals(operation)) {
                                if (OPERATION.LIKE.equals(operation)) {
                                    if (positive.get(key) == null) {
                                        positive.put(key, Boolean.valueOf(false));
                                    }
                                    positive.put(key, Boolean.valueOf(((Boolean) positive.get(key)).booleanValue() || this.like(paramValue, filterValue) || anyValue.booleanValue()));
                                } else if (OPERATION.NOT_LIKE.equals(operation)) {
                                    if (negative.get(key) == null) {
                                        negative.put(key, Boolean.valueOf(true));
                                    }
                                    negative.put(key, Boolean.valueOf(((Boolean) negative.get(key)).booleanValue() && !this.like(paramValue, filterValue) && !anyValue.booleanValue()));
                                }
                            } else {
                                if (negative.get(key) == null) {
                                    negative.put(key, Boolean.valueOf(true));
                                }
                                negative.put(key, Boolean.valueOf(((Boolean) negative.get(key)).booleanValue() && !filterValue.equals(paramValue) && !anyValue.booleanValue()));
                            }
                        } else {
                            if (positive.get(key) == null) {
                                positive.put(key, Boolean.valueOf(false));
                            }
                            positive.put(key, Boolean.valueOf(((Boolean) positive.get(key)).booleanValue() || filterValue.equals(paramValue) || anyValue.booleanValue()));
                        }
                    }

                    boolean result2 = this.calculateRightFilterCheckResult(keys, positive, negative);
                    return result2 && accessMode.intValue() == 1;
                }
            }
        }
    }

    protected boolean admRightCheckInternal(Map<String, Object> params) throws Exception {
//        if(StringUtils.isEmpty((String)params.get("LOGIN"))) {
//            params.put("LOGIN", this.getLogin());
//        }

        check.checkRequiredParams(new String[]{Constants.PARAM_LOGIN}, params);
        List filters = this.getUserRightFilters(params);
        boolean result = this.checkRightParams((Map) params.get("PARAMS"), filters);

        return result;
    }

    private boolean checkAdminRights(Map<String, Object> params) throws Exception {
        Object rightSysName = params.get("RIGHTSYSNAME");
        // new XMLUtil();
        // params.put("TODATE", DataConverter.convertDate(DSDateUtil.getDateWithoutTime(Calendar.getInstance().getTime())));

        List resultList = orm.getCommonListRezult("getRolesByAccount", params);
        if (resultList.size() > 0) {
            Iterator e = resultList.iterator();

            while (e.hasNext()) {
                Map role = (Map) e.next();
                if (this.adminRoleMap.containsKey(role.get("ROLESYSNAME")) && ((List) this.adminRoleMap.get(role.get("ROLESYSNAME"))).indexOf(rightSysName) >= 0) {
                    return true;
                }
            }
        }


        return false;
    }

    public enum OPERATION {
        EQUALS("==", "OperationEquals", "="),
        NOT_EQUALS("!=", "OperationNotEquals", "!="),
        LIKE("like", "OperationLike", "like"),
        NOT_LIKE("not like", "OperationNotLike", "not like"),
        IN("in", "OperationInSet", "="),
        NOT_IN("not in", "OperationNotInSet", "!=");

        private final String id;
        private String description;
        private String table_description;

        private OPERATION(String id, String description, String table_description) {
            this.id = id;
            this.description = description;
            this.table_description = table_description;
        }

        public String getId() {
            return this.id;
        }

        public String getDescription() {
            return this.description;
        }

        public String getTableDescription() {
            return this.table_description;
        }

        public boolean equals(String operation) {
            return this.getId().equalsIgnoreCase(operation);
        }

        public static OPERATION getOperationById(String operation) {
            OPERATION[] arr$ = values();
            int len$ = arr$.length;

            for (int i$ = 0; i$ < len$; ++i$) {
                OPERATION o = arr$[i$];
                if (o.equals(operation)) {
                    return o;
                }
            }

            return null;
        }

        public static OPERATION getOperationById(String operation, String locale) {
            OPERATION o = getOperationById(operation);
            if (o != null) {
                if (o == EQUALS) {
                    o.description = OPERATION.EQUALS.description;
                    o.table_description = "=";
                }

                if (o == NOT_EQUALS) {
                    o.description = OPERATION.NOT_EQUALS.description;
                    o.table_description = OPERATION.NOT_EQUALS.table_description;
                }

                if (o == LIKE) {
                    o.description = OPERATION.LIKE.description;
                    o.table_description = OPERATION.LIKE.table_description;
                }

                if (o == NOT_LIKE) {
                    o.description = OPERATION.NOT_LIKE.description;
                    o.table_description = OPERATION.NOT_LIKE.table_description;
                }

                if (o == IN) {
                    o.description = OPERATION.IN.description;
                    o.table_description = "=";
                }

                if (o == NOT_IN) {
                    o.description = OPERATION.NOT_IN.description;
                    o.table_description = OPERATION.NOT_IN.table_description;
                }
            }

            return o;
        }
    }


    public String lkpuserrole(Map<String, Object> params) throws Exception {
        try {
            Boolean e = Boolean.TRUE;
            if ("".equals(params.get("ROLESYSNAME"))) {
                params.remove("ROLESYSNAME");
            }

            if ("".equals(params.get("ROLENAME"))) {
                params.remove("ROLENAME");
            }

            if ("".equals(params.get("ROLEID"))) {
                params.remove("ROLEID");
            }

            Map result = orm.getCommonListPageRezult("lkpUserRole", params);
            return "";//new XMLUtil(true, true)).createXML(result);
        } catch (Exception var4) {

            throw new Exception(var4);
        }
    }
}
