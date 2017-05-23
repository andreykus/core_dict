package com.bivgroup.core.service.impl;

import com.bivgroup.common.orm.interfaces.OrmProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by bush on 12.08.2016.
 */
public class ExtraAttribFacade {
    private transient Logger logger = LogManager.getLogger(this.getClass());

    private OrmProvider orm;

    public ExtraAttribFacade(OrmProvider orm) {
        this.orm = orm;
    }


}
