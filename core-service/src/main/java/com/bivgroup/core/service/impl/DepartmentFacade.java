package com.bivgroup.core.service.impl;

import com.bivgroup.common.orm.interfaces.OrmProvider;
import com.bivgroup.common.utils.converter.DataConverter;
import com.bivgroup.core.service.interfaces.CoreManager;
import com.bivgroup.common.Constants;
import com.bivgroup.common.utils.converter.CheckProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Created by bush on 08.08.2016.
 */
public class DepartmentFacade {
    private transient Logger logger = LogManager.getLogger(this.getClass());

    private OrmProvider orm;
    private CoreManager core;
    private CheckProcessor check;

    public DepartmentFacade(CoreManager core, OrmProvider orm) {
        this.core = core;
        this.orm = orm;
        this.check = new CheckProcessor();

    }

    public Map<String, Object> depStructure(Map<String, Object> param) throws Exception {
        ArrayList childs = new ArrayList();
        HashMap node = new HashMap();
        String licenseOwner = "test";
        Object startDepth = "0";
        if (param.containsKey("DEPARTMENTID")) {
            startDepth = param.get("DEPARTMENTID");
        }

        node.put("NODEID", startDepth);
        node.put("NAME", licenseOwner);
        node.put("Children", childs);
        HashMap depParam = new HashMap(1);
        depParam.put("DEPARTMENTID", startDepth);
        List depInfo = orm.getCommonListRezult("depStructureByID", depParam);
        if (depInfo.size() > 0) {
            node.put("NAME", ((Map) depInfo.get(0)).get("DEPTSHORTNAME"));
        }

        Map node1 = this.buildStructureTree(node);
        ArrayList resultTree = new ArrayList();
        resultTree.add(node1);
        HashMap result = new HashMap();
        result.put("depTree", resultTree);
        return result;
    }


    private Map<String, Object> buildStructureTree(Map<String, Object> node) throws Exception {
        HashMap param = new HashMap(1);
        param.put("PARENTID", node.get("NODEID"));
        List result = orm.getCommonListRezult("getStructureNodes", param);
        if (result.size() > 0) {
            ArrayList chld = new ArrayList();
            Iterator i$ = result.iterator();

            while (i$.hasNext()) {
                Object tree = i$.next();
                chld.add(this.buildStructureTree((Map) tree));
            }

            node.put("Children", chld);
        }

        node.put("Name", node.get("NAME"));
        node.put("ID", node.get("NODEID"));
        node.put("Description", node.get("NAME"));
        return node;
    }


    public Map<String, Object> depStructureAdd(Map<String, Object> params) throws Exception {

        Integer newLevel = this.getLevelWeight(params.get("DEPTLEVEL"));
        if (newLevel == null) {
            throw new Exception("Error find level");
        } else {
            params.put("LEVELWEIGHT", newLevel);
            Map parentParams = new HashMap();
            parentParams.put("DEPARTMENTID", params.get("PARENTDEPARTMENT"));
            Map parent = this.depStructureInfo(parentParams);
            Integer parentLevel = Integer.valueOf(((Number) parent.get("LEVELWEIGHT")).intValue());
            if (parentLevel.intValue() >= newLevel.intValue() && parentLevel.intValue() < 1000) {
                throw new Exception("UnableDepartmentAddParentLevel");
            } else {
                params.put("DEPARTMENTID", core.getNewId("DEP_DEPARTMENT"));
                if (params.containsKey("DEPADDRESS")) {
                    this.saveAddress(params.get("DEPARTMENTID"), Integer.valueOf(1), (Map) params.get("DEPADDRESS"));
                }

                HashMap searchParams = new HashMap();
                if (params.containsKey("DEPTCODE") && params.get("DEPTCODE") != null) {
                    searchParams.put("DEPTCODE", params.get("DEPTCODE"));
                    List query = orm.getCommonListRezult("admDepartment", searchParams);
                    if (query != null && query.size() > 0) {
                        throw new Exception("UnableDepartmentAddAlreadyExist");
                    }
                }

                orm.performNonSelectingQuery("depStructureAdd", params);
                if (params.get("PARENTDEPARTMENT") != null) {
                    HashMap result = new HashMap();
                    result.put("DEPPARENTID", core.getNewId("DEP_DEPPARENT"));
                    result.put("DEPARTMENTID", params.get("DEPARTMENTID"));
                    result.put("PARENTDEPARTMENT", params.get("PARENTDEPARTMENT"));
                    result.put("RELATIONSHIP", Integer.valueOf(1));
                    orm.performNonSelectingQuery("depParentAdd", result);
                }


                Map result1 = new HashMap();
                result1.put("DEPARTMENTID", params.get("DEPARTMENTID"));
                HashMap eventParams1 = new HashMap();
                eventParams1.put("DepartmentID", params.get("DEPARTMENTID"));
                eventParams1.put("ParentID", params.get("PARENTDEPARTMENT"));
                if (params.containsKey("DEPTFULLNAME")) {
                    eventParams1.put("DepartmentName", params.get("DEPTFULLNAME"));
                }

                if (params.containsKey("LEVELWEIGHT")) {
                    eventParams1.put("DepartmentType", params.get("LEVELWEIGHT"));
                }

                result1.put("eventParams", eventParams1);
                return result1;
            }
        }
    }

    private Integer getLevelWeight(Object depLevelId) throws Exception {
        HashMap params = new HashMap();
        params.put("DEPTLEVELID", depLevelId);
        List result = orm.getCommonListRezult("depLevelByID", params);
        if (result.size() > 0) {
            Map level = (Map) result.get(0);
            return Integer.valueOf(((Number) level.get("LEVELWEIGHT")).intValue());
        } else {
            return null;
        }
    }

    private void saveAddress(Object objectId, Object objectType, Map address) throws Exception {
        Map loadedAddress = this.loadAddress(objectId, objectType);
        if (loadedAddress != null) {
            orm.performNonSelectingQuery("removeAddress", loadedAddress);
        }

        address.put("ADDRID", core.getNewId("DEP_ADDRESS"));
        address.put("OBJECTID", objectId);
        address.put("OBJECTTYPE", objectType);
        if (address.get("1") instanceof Map) {
            address.put("FIRST_LEVEL", ((Map) address.get("1")).get("CODE"));
        }

        if (address.get("2") instanceof Map) {
            address.put("SECOND_LEVEL", ((Map) address.get("2")).get("CODE"));
        }

        if (address.get("3") instanceof Map) {
            address.put("THIRD_LEVEL", ((Map) address.get("3")).get("CODE"));
        }

        if (address.get("4") instanceof Map) {
            address.put("FOURTH_LEVEL", ((Map) address.get("4")).get("CODE"));
        }

        if (address.get("5") instanceof Map) {
            address.put("FIFTH_LEVEL", ((Map) address.get("5")).get("CODE"));
        }

        orm.performNonSelectingQuery("saveAddress", address);
    }

    private Map loadAddress(Object objectId, Object objectType) throws Exception {
        HashMap params = new HashMap();
        params.put("OBJECTID", objectId);
        params.put("OBJECTTYPE", objectType);
        List addressList = orm.getCommonListRezult("loadAddress", params);
        if (addressList.size() <= 0) {
            return null;
        } else {
            Map address = (Map) addressList.get(0);
            String[] prefix = new String[]{"", "FL", "SL", "TL", "FOL", "FS"};

            for (int i = 1; i < prefix.length; ++i) {
                HashMap level = new HashMap();
                level.put("CODE", address.get(prefix[i] + "_CODE"));
                level.put("NAME", address.get(prefix[i] + "_NAME"));
                level.put("TYPE", address.get(prefix[i] + "_TYPE"));
                address.put(String.valueOf(i), level);
                address.remove(prefix[i] + "_CODE");
                address.remove(prefix[i] + "_NAME");
                address.remove(prefix[i] + "_TYPE");
            }

            return address;
        }
    }

    private Map<String, Object> getParentDepartmentTimezone(Number parentId) throws Exception {
        HashMap params = new HashMap();
        params.put("DEPARTMENTID", parentId);
        List resultList = orm.getCommonListRezult("depStructureByID", params);
        if (resultList != null) {
            HashMap returnMap = new HashMap();
            Map parentMap = (Map) resultList.get(0);
            if (parentMap.containsKey("DEPTZNAME") && parentMap.containsKey("DEPTZDIFF")) {
                returnMap.put("name", parentMap.get("DEPTZNAME"));
                returnMap.put("diff", parentMap.get("DEPTZDIFF"));
                return returnMap;
            }
            if (parentMap.containsKey("DEPPARENTTZ") && parentMap.get("DEPPARENTTZ") != null && ((Number) parentMap.get("DEPPARENTTZ")).intValue() == 1 && parentMap.containsKey("PARENTDEPARTMENT") && parentMap.get("PARENTDEPARTMENT") != null) {
                return this.getParentDepartmentTimezone(Long.valueOf(((Number) parentMap.get("PARENTDEPARTMENT")).longValue()));
            }
        }
        return null;
    }

    public Map<String, Object> depStructureInfo(Map<String, Object> params) throws Exception {
        List resultList = orm.getCommonListRezult("depStructureByID", params);
        if (resultList.size() == 0) {
            throw new Exception("Unable to find structure department");
        } else {
            Map result = (Map) resultList.get(0);
            result.put("DEPTNAME", result.get("DEPTFULLNAME"));
            result.put("DEPADDRESS", this.loadAddress(result.get("DEPARTMENTID"), Integer.valueOf(1)));
            resultList = orm.getCommonListRezult("depStructureManager", params);
            if (resultList.size() > 0) {
                result.putAll((Map) resultList.get(0));
            }
            if (result.get("PARENTDEPARTMENT") != null) {
                Map parentResult;
                if (result.containsKey("DEPPARENTTZ") && result.get("DEPPARENTTZ") != null && ((Number) result.get("DEPPARENTTZ")).intValue() == 1) {
                    parentResult = null;
                    if ((parentResult = this.getParentDepartmentTimezone((Number) result.get("PARENTDEPARTMENT"))) != null) {
                        result.put("DEPTZNAME", parentResult.get("name"));
                        result.put("DEPTZDIFF", parentResult.get("diff"));
                    }
                }
                params.put("DEPARTMENTID", result.get("PARENTDEPARTMENT"));
                resultList = orm.getCommonListRezult("depStructureByID", params);
                if (resultList != null) {
                    parentResult = (Map) resultList.get(0);
                    result.put("PARENTDEPTNAME", parentResult.get("DEPTFULLNAME"));
                    result.put("PARENTDEPTFULLNAME", parentResult.get("DEPTFULLNAME"));
                }
            } else {
                result.put("PARENTDEPARTMENT", (Object) null);
            }
            return result;
        }
    }

    private List getChildren(Object childId) throws Exception {
        HashMap params = new HashMap();
        params.put("DepartmentID", childId);
        List searchResult = orm.getCommonListRezult("dsDepartmentFindListChildIDByID", params);
        ArrayList foundRes = new ArrayList();
        foundRes.addAll(searchResult);
        if (searchResult.size() > 0) {
            Iterator i$ = searchResult.iterator();

            while (i$.hasNext()) {
                Map child = (Map) i$.next();
                foundRes.addAll(this.getChildren(child.get("ChildID")));
            }
        }

        return foundRes;
    }

    public Map<String, Object> dsDepartmentFindListChildIDByID(Map<String, Object> params) throws Exception {

        boolean ChildSearchMode = false;
        if (params.containsKey("ChildSearchMode")) {
            if (params.get("ChildSearchMode") instanceof Number) {
                ChildSearchMode = ((Number) params.get("ChildSearchMode")).intValue() == 1;
            }

            if (params.get("ChildSearchMode") instanceof String) {
                ChildSearchMode = params.get("ChildSearchMode").toString().equals("1");
            }
        }
        HashMap result = new HashMap();
        List searchResult = orm.getCommonListRezult("dsDepartmentFindListChildIDByID", params);
        if (ChildSearchMode) {
            ArrayList foundRes = new ArrayList();
            foundRes.addAll(searchResult);
            Iterator i$ = searchResult.iterator();

            while (i$.hasNext()) {
                Map child = (Map) i$.next();
                foundRes.addAll(this.getChildren(child.get("ChildID")));
            }

            result.put("DepartmentChildIDList", foundRes);
        } else {
            result.put("DepartmentChildIDList", searchResult);
        }
        return result;
    }

    private Map getNormalizedDepthInfo(Map arg) {
        HashMap result = new HashMap();
        result.put("DepartmentID", arg.get("DEPARTMENTID"));
        result.put("ParentID", arg.get("PARENTDEPARTMENT"));
        result.put("DepartmentType", Integer.valueOf(((Number) arg.get("DEPTLEVEL")).intValue()));
        return result;
    }

    private Map getDepParent(Object parentId) throws Exception {
        HashMap param = new HashMap();
        param.put("DEPARTMENTID", parentId);
        List data = orm.getCommonListRezult("depStructureByID", param);
        return data != null && data.size() == 1 ? (Map) data.get(0) : null;
    }

    public Map<String, Object> dsDepartmentFindListParentIDByID(Map<String, Object> params) throws Exception {
        HashMap result = new HashMap();
        ArrayList departmentParentIDList = new ArrayList();
        result.put("DepartmentParentIDList", departmentParentIDList);
        boolean parentSearchMode = false;
        if (params.containsKey("ParentSearchMode") && ((Number) params.get("ParentSearchMode")).intValue() == 1) {
            parentSearchMode = true;
        }
        Map selfDepth = this.getDepParent(params.get("DepartmentID"));
        if (selfDepth == null) {
            return result;
        } else {
            departmentParentIDList.add(this.getNormalizedDepthInfo(selfDepth));
            Map parentDepth = null;
            Object parentId = selfDepth.get("PARENTDEPARTMENT");
            if (parentId != null) {
                do {
                    parentDepth = this.getDepParent(parentId);
                    if (parentDepth == null) {
                        break;
                    }
                    departmentParentIDList.add(this.getNormalizedDepthInfo(parentDepth));
                    if (!parentSearchMode) {
                        break;
                    }
                    parentId = parentDepth.get("PARENTDEPARTMENT");
                } while (parentId != null);
            }
            return result;
        }
    }


    public Map<String, Object> dsDepartmentChildList(Map<String, Object> params) throws Exception {
        if (params.get("DEPARTMENTID") == null) {
            params.put("DEPARTMENTID", Integer.valueOf(0));
        }

        List resultList = orm.getCommonListRezult("dsDepartmentChildList", params);
        if (resultList != null) {
            ArrayList resultListFromChilds = new ArrayList();
            Iterator i$ = resultList.iterator();

            while (i$.hasNext()) {
                Map depInfo = (Map) i$.next();
                Map deeperParams = new HashMap();
                deeperParams.put("DEPARTMENTID", depInfo.get("DEPARTMENTID"));
                Map deeperResultRaw = this.dsDepartmentChildList(deeperParams);
                if (deeperResultRaw.get(Constants.RESULT_FIELD_NAME) instanceof List) {
                    List deeperResult = (List) deeperResultRaw.get(Constants.RESULT_FIELD_NAME);
                    resultListFromChilds.addAll(deeperResult);
                }
            }

            resultList.addAll(resultListFromChilds);
        }
        HashMap result = new HashMap();
        result.put(Constants.RESULT_FIELD_NAME, resultList);
        return result;
    }

    public Map<String, Object> depStructureUpdate(Map<String, Object> params) throws Exception {
        check.checkRequiredParams(new String[]{"DEPARTMENTID"}, params);
        long depId = ((Number) params.get("DEPARTMENTID")).longValue();
        HashMap searchParams = new HashMap();
        if (params.containsKey("DEPTCODE") && params.get("DEPTCODE") != null) {
            searchParams.put("DEPTCODE", params.get("DEPTCODE"));
            List query = orm.getCommonListRezult("admDepartment", searchParams);
            if (query != null && query.size() > 0 && ((Map) query.get(0)).containsKey("DEPARTMENTID")) {
                long result = ((Number) ((Map) query.get(0)).get("DEPARTMENTID")).longValue();
                if (result != depId) {
                    throw new Exception("UnableDepartmentAddAlreadyExist");
                }
            }
        }

        if (params.containsKey("DEPTLEVEL")) {
            Integer query1 = this.getLevelWeight(params.get("DEPTLEVEL"));
            if (query1 == null) {
                throw new Exception("UnableDepartmentUpdate");
            }

            params.put("LEVELWEIGHT", query1);
            Map result1 = new HashMap();
            params.put("ReturnAsHashMap", Constants.RESULT_TRUE);
            Map eventParams = this.depStructureInfo(params);
            HashMap depParentQuery = new HashMap();
            depParentQuery.put("DEPARTMENTID", eventParams.get("DEPARTMENTID"));
            depParentQuery.put("PARENTDEPARTMENT", eventParams.get("PARENTDEPARTMENT"));
            orm.performNonSelectingQuery("depParentRemove", depParentQuery);
            if (params.containsKey("PARENTDEPARTMENT")) {
                result1.put("DEPARTMENTID", params.get("PARENTDEPARTMENT"));
            } else {
                params.put("ReturnAsHashMap", Constants.RESULT_TRUE);
                result1.put("DEPARTMENTID", eventParams.get("PARENTDEPARTMENT"));
            }

            Integer parentLevel = Integer.valueOf(0);
            if (result1.get("DEPARTMENTID") != null) {
                result1.put("ReturnAsHashMap", Constants.RESULT_TRUE);
                Map selfLevel = this.depStructureInfo(result1);
                parentLevel = Integer.valueOf(((Number) selfLevel.get("LEVELWEIGHT")).intValue());
            }

            Integer selfLevel1 = Integer.valueOf(((Number) eventParams.get("LEVELWEIGHT")).intValue());
            HashMap childParams = new HashMap();
            childParams.put("ParentID", Long.valueOf(depId));
            List childList = orm.getCommonListRezult("dsDepartmentFindListIDByListType", childParams);
            Integer minChildType = Integer.valueOf('\uffff');
            if (!childList.isEmpty()) {
                Iterator i$ = childList.iterator();

                while (i$.hasNext()) {
                    Map childMap = (Map) i$.next();
                    if (childMap.containsKey("DepartmentType") && ((Number) childMap.get("DepartmentType")).intValue() < minChildType.intValue() && ((Number) childMap.get("DepartmentType")).intValue() != selfLevel1.intValue()) {
                        minChildType = Integer.valueOf(((Number) childMap.get("DepartmentType")).intValue());
                    }
                }
            }

            if (parentLevel.intValue() >= query1.intValue() && parentLevel.intValue() < 1000 || query1.intValue() >= minChildType.intValue() && query1.intValue() < 1000) {
                throw new Exception("UnableDepartmentUpdate");
            }
        }

        if (params.containsKey("DEPADDRESS")) {
            this.saveAddress(new Long(depId), Integer.valueOf(1), (Map) params.get("DEPADDRESS"));
        }

        params.put("DEPARTMENTID", Long.valueOf(depId));
        orm.performNonSelectingQuery("depStructureUpdate", params);
        HashMap result2 = new HashMap(2);
        result2.put("DEPARTMENTID", params.get("DEPARTMENTID"));
        HashMap eventParams1;
        if (params.get("PARENTDEPARTMENT") != null) {
            eventParams1 = new HashMap();
            eventParams1.put("DEPPARENTID", core.getNewId("DEP_DEPPARENT"));
            eventParams1.put("DEPARTMENTID", params.get("DEPARTMENTID"));
            eventParams1.put("PARENTDEPARTMENT", params.get("PARENTDEPARTMENT"));
            eventParams1.put("RELATIONSHIP", Integer.valueOf(1));
            orm.performNonSelectingQuery("depParentAdd", eventParams1);
        }

        eventParams1 = new HashMap();
        eventParams1.put("DepartmentID", params.get("DEPARTMENTID"));
        if (params.containsKey("PARENTDEPARTMENT")) {
            eventParams1.put("ParentID", params.get("PARENTDEPARTMENT"));
        }

        if (params.containsKey("DEPTFULLNAME")) {
            eventParams1.put("DepartmentName", params.get("DEPTFULLNAME"));
        }

        if (params.containsKey("LEVELWEIGHT")) {
            eventParams1.put("DepartmentType", params.get("LEVELWEIGHT"));
        }

        result2.put("eventParams", eventParams1);
        return result2;
    }

}
