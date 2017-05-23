package com.bivgroup.core.aspect.visitors.impl.binaryfiles;

import com.bivgroup.core.aspect.annotations.AspectMethodParam;
import com.bivgroup.core.aspect.visitors.AspectDelegator;
import com.bivgroup.core.dictionary.exceptions.DictionaryException;

import java.util.Map;

/**
 * Created by bush on 14.12.2016.
 */
public interface BinaryFilesDelegator extends AspectDelegator {
    void chek(@AspectMethodParam(nameParam = "entityName") String entityName);

    void deleteBinaryFileInfo(@AspectMethodParam(nameParam = "entityName") String entityName, @AspectMethodParam(nameParam = "params") Map<String, Object> params) throws DictionaryException;

    Map<String, Object> createBinaryFileInfo(@AspectMethodParam(nameParam = "entityName") String entityName, @AspectMethodParam(nameParam = "params") Map<String, Object> params) throws DictionaryException;

    Map<String, Object> binaryFileBrowseListByParam(@AspectMethodParam(nameParam = "entityName") String entityName, @AspectMethodParam(nameParam = "params") Map<String, Object> params) throws DictionaryException;
}
