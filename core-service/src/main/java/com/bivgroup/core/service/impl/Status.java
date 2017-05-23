package com.bivgroup.core.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by bush on 01.09.2016.
 */
public enum Status {
    ACTIVE("ACTIVE"),
    BLOCKED("BLOCKED"),
    DELETED("DELETED"),
    UNKNOW("UNKNOW");

    private String status;
    private transient Logger logger = LogManager.getLogger(Status.class);

    Status(String status) {
        this.status = status;
    }

    public Status getByValue(String name) {
        Status out = Status.UNKNOW;
        try {
            out = Status.valueOf(name);
        } catch (Exception ex) {
            logger.error(String.format("not found status for : %1s ", name));
        }
        return out;
    }

    public String getName() {
        return status;
    }
}
