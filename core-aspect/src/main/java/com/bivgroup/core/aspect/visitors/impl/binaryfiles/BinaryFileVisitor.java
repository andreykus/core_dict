package com.bivgroup.core.aspect.visitors.impl.binaryfiles;

import com.bivgroup.core.aspect.annotations.Action;
import com.bivgroup.core.aspect.annotations.AspectClass;
import com.bivgroup.core.aspect.bean.Aspect;

import com.bivgroup.core.aspect.bean.BinaryFile;
import com.bivgroup.core.aspect.exceptions.AspectException;
import com.bivgroup.core.aspect.visitors.AbstractAspectVisitor;
import com.bivgroup.core.aspect.visitors.AspectVisitor;
import com.bivgroup.core.dictionary.dao.hierarchy.ExtendGenericDAO;
import com.bivgroup.core.dictionary.exceptions.DictionaryException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bush on 07.12.2016.
 */

@AspectClass(name = BinaryFileVisitor.BINARYFILE_ASPECT_NAME, delegator = BinaryFilesDelegator.class, callOrder = 1000000)
public class BinaryFileVisitor extends AbstractAspectVisitor<Object> implements BinaryFilesDelegator {
    public static final String BINARYFILE_ASPECT_NAME = "BinaryFile";
    protected Logger logger = LogManager.getLogger(this.getClass());

    public BinaryFileVisitor(AspectVisitor next, ExtendGenericDAO externaldao, Aspect aspect) throws AspectException {
        super(next, externaldao, aspect);
    }


    @Override
    public Object visitPred(Action action, Method method, Object[] args, Object sources) throws AspectException {
        this.source = sources;
        processNextPred(action, method, args);
        return sources;
    }

    @Override
    public Object visitPost(Action action, Method method, Object[] args, Object element, Object sources) throws AspectException {
        this.source = sources;
        processNextPost(action, method, args, element);
        return sources;
    }

    @Override
    public void chek(String entityName) {
        logger.info(BINARYFILE_ASPECT_NAME);
    }

    @Override
    public void deleteBinaryFileInfo(String entityName, Map<String, Object> params) throws DictionaryException {
        Map<String, Object> deleteParams = new HashMap<String, Object>();
        deleteParams.put("BINFILEID", params.get("BINFILEID"));
        deleteParams.put("OBJTABLENAME", ((BinaryFile) aspect).getObjEntityName());
        externaldao.delete("BindBinFile", deleteParams);
    }

    @Override
    public Map<String, Object> createBinaryFileInfo(String entityName, Map<String, Object> params) throws DictionaryException {
        params.put("OBJTABLENAME", ((BinaryFile) aspect).getObjEntityName());
        // params.put("CREATEUSERID", userId);
        return (Map<String, Object>) externaldao.onlySave("BindBinFile", params);
    }

    @Override
    public Map<String, Object> binaryFileBrowseListByParam(String entityName, Map<String, Object> params) throws DictionaryException {
        Map<String, Object> result = new HashMap<String, Object>();
        if ((null == params.get("BINFILEID")) && (null == params.get("OBJID"))) {
            throw new DictionaryException("Not enough the required parameters: at least one of the parameters BINFILEID or OBJID must be installed");
        }
        params.put("OBJTABLENAME", ((BinaryFile) aspect).getObjEntityName());

//
//        sb.append("  #chunk($OBJTABLENAME) T.OBJTABLENAME = #bind($OBJTABLENAME 'VARCHAR') #end \n");
//        sb.append("  #chunk($OBJID) T.OBJID = #bind($OBJID 'NUMERIC') #end \n");
//        sb.append("  #chunk($FILETYPENAME) T.FILETYPENAME = #bind($FILETYPENAME 'VARCHAR') #end \n");
//        sb.append("  #chunk($FILETYPEID) T.FILETYPEID = #bind($FILETYPEID 'NUMERIC') #end \n");
//        sb.append("  #chunk($BINFILEID) T.BINFILEID = #bind($BINFILEID 'NUMERIC') #end \n");


        List<Map<String, Object>> res = null;
        result.put("Result", res);
        return result;

    }
}
