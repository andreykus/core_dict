package com.bivgroup.core.service;

import com.bivgroup.common.orm.OrmProviderImpl;
import com.bivgroup.core.service.impl.CoreManagerImpl;
import com.bivgroup.core.service.interfaces.CoreManager;
import com.bivgroup.common.orm.interfaces.OrmProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by bush on 17.08.2016.
 */
public class CoreService {
    private transient Logger logger = LogManager.getLogger(this.getClass());

    private List providerRegistr;
    private OrmProvider orm = null;


    public CoreService(DataSource ds) {
        orm = new OrmProviderImpl(ds, CoreManagerImpl.PERSISTENCE_NAME);
    }

    void initProviderRegistr() {
        providerRegistr = Arrays.asList();
    }


    public <T> T invoke(String methodName, Map params) throws Exception {
        T rez = null;
        Class type = ((T) new Object()).getClass();
        try {
            Method me = CoreManager.class.getMethod(methodName, params.getClass());
            CoreManager cm = new CoreManagerImpl(null);
            rez = (T) me.invoke(cm, params);
        } catch (NoSuchMethodException ex) {
            rez = (T) unknowmethodlist(methodName, params);
        }
        return rez;
    }

    private List<Map<String, Object>> unknowmethodlist(String methodName, Map params) throws Exception {
        return orm.getCommonListRezult(methodName, params);
    }

    private Map unknowmethodmap(String methodName, Map params) throws Exception {
        return orm.getCommonRecordRezult(methodName, params);
    }
}
