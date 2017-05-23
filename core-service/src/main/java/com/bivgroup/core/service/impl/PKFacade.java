package com.bivgroup.core.service.impl;

import com.bivgroup.common.orm.OrmException;
import com.bivgroup.common.orm.interfaces.OrmProvider;
import com.bivgroup.common.orm.sequence.SequenceContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by bush on 09.08.2016.
 */
public class PKFacade {
    private transient Logger logger = LogManager.getLogger(this.getClass());

    private SequenceContainer sc;

    public PKFacade(OrmProvider orm) {
        this.sc = new SequenceContainer(orm);
    }

    public Long getNewId(String tableName) throws OrmException {
        if (tableName == null) {
            throw new OrmException("No TABLENAME Param");
        } else {
            return sc.getNextId(tableName);
        }
    }

}
