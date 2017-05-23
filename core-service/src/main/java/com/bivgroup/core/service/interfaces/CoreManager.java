package com.bivgroup.core.service.interfaces;

import com.bivgroup.common.orm.OrmException;
import com.bivgroup.common.orm.interfaces.OrmProvider;

import java.util.Map;

/**
 * Created by bush on 01.08.2016.
 */
public interface CoreManager {
    OrmProvider getOrmProvider();

    Map<String, Object> checklogin(Map<String, Object> params) throws Exception;

    Long getNewId(String tableName) throws OrmException;

    String getSystemMetadataURL(Long progectId) throws Exception;

    Map<String, Object> dsUserGetCurrentID(Map<String, Object> params, String login) throws Exception;

    Map<String, Object> dsGetUsersByRole(Map<String, Object> params) throws Exception;

    Map<String, Object> admparticipantbyid(Map<String, Object> params) throws Exception;

    Map<String, Object> dsAccountSettingFindByLoginAndName(Map<String, Object> params) throws Exception;

    Map<String, Object> depStructure(Map<String, Object> params) throws Exception;

    Map<String, Object> depStructureAdd(Map<String, Object> params) throws Exception;

    Map<String, Object> depStructureInfo(Map<String, Object> params) throws Exception;

    Map<String, Object> depStructureUpdate(Map<String, Object> params) throws Exception;

    Map<String, Object> dsDepartmentFindListChildIDByID(Map<String, Object> params) throws Exception;

    Map<String, Object> dsDepartmentFindListParentIDByID(Map<String, Object> params) throws Exception;

    Map<String, Object> dsDepartmentChildList(Map<String, Object> params) throws Exception;

    Map<String, Object> depemployeeinfofull(Map<String, Object> params) throws Exception;

    Map<String, Object> getExchangeList(Map<String, Object> params) throws Exception;

    Map<String, Object> admGetCurrencyPairsList(Map<String, Object> params) throws Exception;

    Map<String, Object> admRefCurrencyList(Map<String, Object> params) throws Exception;

    Map<String, Object> createCurrencyExchange(Map<String, Object> params) throws Exception;

    Map<String, Object> admrightcheck(Map<String, Object> params) throws Exception;

    Map<String, Object> admrightchecklist(Map<String, Object> params) throws Exception;

    Map<String, Object> admuserrolebyaccount(Map<String, Object> param) throws Exception;

    Map<String, Object> admuserrightfilterlist(Map<String, Object> params) throws Exception;

    Map<String, Object> admuserrightlistfilterlist(Map<String, Object> params) throws Exception;

    Map<String, Object> dsNumberFindByMask(Map<String, Object> params) throws Exception;

    Map<String, Object> dsAccountCreate(Map<String, Object> params) throws Exception;

    Map<String, Object> dsAccountUpdate(Map<String, Object> params) throws Exception;

    Map<String, Object> depEmployeeAdd(Map<String, Object> params) throws Exception;

    Map<String, Object> depEmployeeUpdate(Map<String, Object> params) throws Exception;

    Map<String, Object> admAccountFind(Map<String, Object> param) throws Exception;

    Map<String, Object> dsStartMassOper(Map<String, Object> params) throws Exception;

    Map<String, Object> dsFinishMassOper(Map<String, Object> params) throws Exception;

    Map<String, Object> admEmplAccountById(Map<String, Object> params) throws Exception;

    Map<String, Object> admAccountRemove(Map<String, Object> params) throws Exception;

    Map<String, Object> admroleuseradd(Map<String, Object> params) throws Exception;

    //depstructure
    //Map<String, Object> dsNumberFindByMask(Map<String, Object> param);
    //void getlocatordata();


}
