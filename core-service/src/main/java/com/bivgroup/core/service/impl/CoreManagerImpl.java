package com.bivgroup.core.service.impl;

import com.bivgroup.common.orm.OrmProviderImpl;
import com.bivgroup.common.orm.interfaces.OrmProvider;
import com.bivgroup.core.service.interfaces.CoreManager;
import com.bivgroup.common.orm.OrmException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by bush on 01.08.2016.
 * Реализация методов ядра
 */
public class CoreManagerImpl implements CoreManager {
    /**
     * логгер
     */
    private transient Logger logger = LogManager.getLogger(this.getClass());
    /**
     * название PERSISTENCE NAME
     */
    public static final String PERSISTENCE_NAME = "jpaPersistenceCore";
    /**
     * провайдер раьоты с ЮД
     */
    private OrmProvider orm;
    private ExchangeFacade exchange_util;
    private DepartmentFacade department_util;
    private EmployeFacade employe_util;
    private AdminFacade admin_util;
    private RightsFacade rights_util;
    private MaskFacade mask_util;
    private PKFacade pk_util;

    /**
     * Конструктор Реализация методов ядра
     * @param ds - DataSource
     */
    public CoreManagerImpl(DataSource ds) {
        this.orm = new OrmProviderImpl(ds, PERSISTENCE_NAME);
        this.exchange_util = new ExchangeFacade(this, orm);
        this.department_util = new DepartmentFacade(this, orm);
        this.employe_util = new EmployeFacade(this, orm);
        this.admin_util = new AdminFacade(this, orm);
        this.rights_util = new RightsFacade(this, orm);
        this.mask_util = new MaskFacade(orm);
        this.pk_util = new PKFacade(orm);
    }

    /**
     * получить провайдер работы с БД
     * @return -  провайдер
     */
    public OrmProvider getOrmProvider() {
        return orm;
    }

    public Long getNewId(String tableName) throws OrmException {
        return pk_util.getNewId(tableName);
    }

    public List<Map<String, Object>> getStateList() throws Exception {
        Map<String, Long> inParam = new HashMap<String, Long>();
        List<Map<String, Object>> rez = orm.getCommonListRezult("getStateObject", inParam);
        return rez;
    }

    public Map<String, Object> getState() throws Exception {
        Map<String, Long> inParam = new HashMap<String, Long>();
        Map<String, Object> rez = orm.getCommonRecordRezult("getStateObject", inParam);
        return rez;
    }

    public String getSystemMetadataURL(Long progectId) throws Exception {
        Map<String, Object> inParam = new HashMap<String, Object>();
        inParam.put("PROJECTID", progectId);
        String rez = orm.getCommonRezult("GETMETADATAURL", inParam);
        return rez;
    }

    public Map<String, Object> dsNumberFindByMask(Map<String, Object> params) throws Exception {
        return mask_util.dsNumberFindByMask(params);
    }

    public Map<String, Object> dsAccountSettingFindByLoginAndName(Map<String, Object> params) throws Exception {
        return admin_util.dsAccountSettingFindByLoginAndName(params);
    }

    public Map<String, Object> depStructure(Map<String, Object> params) throws Exception {
        return department_util.depStructure(params);
    }

    public Map<String, Object> depStructureAdd(Map<String, Object> params) throws Exception {
        return department_util.depStructureAdd(params);
    }

    public Map<String, Object> depStructureUpdate(Map<String, Object> params) throws Exception {
        return department_util.depStructureUpdate(params);
    }

    ;

    public Map<String, Object> depStructureInfo(Map<String, Object> params) throws Exception {
        return department_util.depStructureInfo(params);
    }

    public Map<String, Object> dsDepartmentFindListChildIDByID(Map<String, Object> params) throws Exception {
        return department_util.dsDepartmentFindListChildIDByID(params);
    }

    public Map<String, Object> dsDepartmentFindListParentIDByID(Map<String, Object> params) throws Exception {
        return department_util.dsDepartmentFindListParentIDByID(params);
    }

    public Map<String, Object> dsDepartmentChildList(Map<String, Object> params) throws Exception {
        return department_util.dsDepartmentChildList(params);
    }

    public Map<String, Object> depemployeeinfofull(Map<String, Object> params) throws Exception {
        return employe_util.depemployeeinfofull(params);
    }

    public Map<String, Object> getExchangeList(Map<String, Object> params) throws Exception {
        return exchange_util.getExchangeList(params);
    }

    public Map<String, Object> admGetCurrencyPairsList(Map<String, Object> params) throws Exception {
        return exchange_util.admGetCurrencyPairsList(params);
    }

    public Map<String, Object> admRefCurrencyList(Map<String, Object> params) throws Exception {
        return exchange_util.admRefCurrencyList(params);
    }

    public Map<String, Object> admparticipantbyid(Map<String, Object> params) throws Exception {
        return admin_util.admparticipantbyid(params);
    }

    public Map<String, Object> dsUserGetCurrentID(Map<String, Object> params, String login) throws Exception {
        return admin_util.dsUserGetCurrentID(params, login);
    }

    public Map<String, Object> admrightcheck(Map<String, Object> params) throws Exception {
        return rights_util.admrightcheck(params);
    }

    public Map<String, Object> admuserrolebyaccount(Map<String, Object> params) throws Exception {
        return rights_util.admuserrolebyaccount(params);
    }

    public Map<String, Object> admuserrightfilterlist(Map<String, Object> params) throws Exception {
        return rights_util.admuserrightfilterlist(params);
    }

    public Map<String, Object> admuserrightlistfilterlist(Map<String, Object> params) throws Exception {
        return rights_util.admuserrightlistfilterlist(params);
    }

    public Map<String, Object> dsGetUsersByRole(Map<String, Object> params) throws Exception {
        return admin_util.dsGetUsersByRole(params);
    }

    public Map<String, Object> createCurrencyExchange(Map<String, Object> params) throws Exception {
        return exchange_util.createCurrencyExchange(params);
    }

    public Map<String, Object> unknowmethos(Map<String, Object> params) throws Exception {
        return exchange_util.createCurrencyExchange(params);
    }

    public Map<String, Object> dsAccountCreate(Map<String, Object> params) throws Exception {
        return admin_util.dsAccountCreate(params);
    }

    public Map<String, Object> dsAccountUpdate(Map<String, Object> params) throws Exception {
        return admin_util.dsAccountUpdate(params);
    }

    public Map<String, Object> depEmployeeAdd(Map<String, Object> params) throws Exception {
        return admin_util.depemployeeadd(params);
    }

    public Map<String, Object> depEmployeeUpdate(Map<String, Object> params) throws Exception {
        return admin_util.depemployeeupdate(params);
    }

    public Map<String, Object> admAccountFind(Map<String, Object> params) throws Exception {
        return admin_util.admaccountfind(params);
    }

    public Map<String, Object> checklogin(Map<String, Object> params) throws Exception {
        return admin_util.checklogin(params);
    }

    public Map<String, Object> dsStartMassOper(Map<String, Object> params) throws Exception {
        return admin_util.dsStartMassOper(params);
    }

    public Map<String, Object> dsFinishMassOper(Map<String, Object> params) throws Exception {
        return admin_util.dsFinishMassOper(params);
    }

    public Map<String, Object> admrightchecklist(Map<String, Object> params) throws Exception {
        return rights_util.admrightchecklist(params);
    }

    public Map<String, Object> admEmplAccountById(Map<String, Object> params) throws Exception {
        return admin_util.admEmplAccountById(params);
    }

    public Map<String, Object> admAccountRemove(Map<String, Object> params) throws Exception {
        return admin_util.admaccountremove(params);
    }

    public Map<String, Object> admroleuseradd(Map<String, Object> params) throws Exception {
        return rights_util.admroleuseradd(params);
    }
}
