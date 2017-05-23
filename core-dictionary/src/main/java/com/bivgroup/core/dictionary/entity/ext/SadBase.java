package com.bivgroup.core.dictionary.entity.ext;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Created by andreykus on 17.09.2016.*
 * Базовая Entity
 */

@MappedSuperclass
public class SadBase {
    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    /**
     * модуль
     */
    @Column(name = "MODUL")
    private String module;
    /**
     * версия
     */
    @Column(name = "VERSION")
    private Long version;
}
