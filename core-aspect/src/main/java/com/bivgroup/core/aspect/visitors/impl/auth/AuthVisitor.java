package com.bivgroup.core.aspect.visitors.impl.auth;

import com.bivgroup.core.aspect.annotations.Action;
import com.bivgroup.core.aspect.annotations.AspectClass;
import com.bivgroup.core.aspect.bean.Aspect;
import com.bivgroup.core.aspect.exceptions.AspectException;
import com.bivgroup.core.aspect.visitors.AbstractAspectVisitor;
import com.bivgroup.core.aspect.visitors.AspectVisitor;
import com.bivgroup.core.dictionary.dao.hierarchy.ExtendGenericDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by bush on 26.12.2016.
 */
@AspectClass(name = AuthVisitor.AUTH_ASPECT_NAME, delegator = AuthDelegator.class, callOrder = 1000000)
public class AuthVisitor extends AbstractAspectVisitor<Object> implements AuthDelegator {
    public static final String AUTH_ASPECT_NAME = "Auth";
    public static final String CREATE_DATE_NAME = "CREATEDATE";
    public static final String UPDATE_DATE_NAME = "UPDATEDATE";
    protected Logger logger = LogManager.getLogger(this.getClass());

    public AuthVisitor(AspectVisitor next, ExtendGenericDAO externaldao, Aspect aspect) throws AspectException {
        super(next, externaldao, aspect);
    }

    @Override
    public Object visitPred(Action action, Method method, Object[] args, Object sources) throws AspectException {
        this.source = sources;
        if (Action.CREATE.equals(action)) createDate(args);
        if (Action.UPDATE.equals(action)) updateDate(args);
        processNextPred(action, method, args);
        return sources;
    }

    @Override
    public Object visitPost(Action action, Method method, Object[] args, Object element, Object sources) throws AspectException {
        this.source = sources;
        processNextPost(action, method, args, element);
        return sources;
    }


    private void updateDate(Object[] args) {
        if (args != null && args.length > 1 && (args[1] instanceof Map)) {
            Map map = (Map) args[1];
            map.put(UPDATE_DATE_NAME, Calendar.getInstance().getTime());
        }
    }

    private void createDate(Object[] args) {
        if (args != null && args.length > 1 && (args[1] instanceof Map)) {
            Map map = (Map) args[1];
            map.put(CREATE_DATE_NAME, Calendar.getInstance().getTime());
        }
    }


}
