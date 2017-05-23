package com.bivgroup.core.service.impl;

import com.bivgroup.core.service.interfaces.CoreManager;
import com.bivgroup.common.orm.interfaces.OrmProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bush on 10.08.2016.
 */
public class EmployeFacade {
    private transient Logger logger = LogManager.getLogger(this.getClass());

    private OrmProvider orm;
    private CoreManager core;

    public EmployeFacade(CoreManager core, OrmProvider orm) {
        this.core = core;
        this.orm = orm;
    }


    public Map<String, Object> depemployeeinfofull(Map<String, Object> param) throws Exception {
        Map result = new HashMap();

        List resultList = orm.getCommonListRezult("depEmployeeInfoFull", param);
        if (resultList.size() != 1) {
            throw new Exception("employee.info.failed");
        } else {
            if (((Map) resultList.get(0)).get("MANAGER") == null) {
                ((Map) resultList.get(0)).put("MANAGER", this.getEmployeeManager(param.get("EMPLOYEEID")));
            }
            result = (Map) resultList.get(0);
            return result;
        }
    }

    private Object getEmployeeManager(Object employeeId) throws Exception {
        Map params = new HashMap();
        params.put("EMPLOYEEID", employeeId);
        Map info = this.depemployeeinfobyid(params);
        if (info == null) {
            return null;
        } else if (info.get("MANAGER") != null) {
            return info.get("MANAGER");
        } else {
            Map depinfo;
            for (Object depId = info.get("DEPARTMENTID"); depId != null; depId = depinfo.get("PARENTDEPARTMENT")) {
                Map dep = new HashMap();
                dep.put("DEPARTMENTID", depId);
                depinfo = core.depStructureInfo(dep);
                if (depinfo.get("MANAGER") != null) {
                    return depinfo.get("MANAGER");
                }
            }

            return null;
        }
    }

    public Map<String, Object> depemployeeinfobyid(Map<String, Object> params) throws Exception {
        try {
            Map map = null;
            List resultList = orm.getCommonListRezult("depEmployeeInfoByID", params);
            if (resultList.size() > 0) {
                map = (Map) resultList.get(0);
                map.put("AUDITEVENTOBJECTID", params.get("EMPLOYEEID"));
            }
            return map;
        } catch (Exception var5) {
            throw new Exception(var5);
        }
    }
}
