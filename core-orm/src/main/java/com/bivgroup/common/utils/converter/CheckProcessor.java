package com.bivgroup.common.utils.converter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * Created by bush on 15.08.2016.
 * проверка обязательных параметров
 */
public class CheckProcessor {
    /**
     * логгер
     */
    private transient Logger logger = LogManager.getLogger(this.getClass());

    /**
     *
     * @param requiredParams - обязательные параметры
     * @param param - входные параметры
     * @throws Exception - исключение
     */
    public  void checkRequiredParams(String[] requiredParams, Map<String, Object> param) throws Exception {
        if (param == null) {
            throw new Exception("NullInputParameters");
        } else {
            String[] arr = requiredParams;
            int len = requiredParams.length;

            for (int i = 0; i < len; ++i) {
                String paramName = arr[i];
                Object paramValue = param.get(paramName);
                if (paramValue == null) {
                    throw new Exception("MissReqParamsStart");
                }
                if ("".equals(paramValue.toString().trim())) {
                    throw new Exception("EmptyReqParamsStart");
                }
            }
        }
    }
}
