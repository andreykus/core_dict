package com.bivgroup.core.dictionary.dao.hierarchy.milestone;

import com.bivgroup.core.dictionary.common.AbstractCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by bush on 01.11.2016.
 *  TODO удалить
 */
public class PersistCollection implements AbstractCollection {
    public List<Map> listGraf;
    public PersistCollection() {
        this.listGraf = new ArrayList<Map>();
    }
}
