package com.bivgroup.core.service.impl;

import com.bivgroup.common.Constants;
import com.bivgroup.common.orm.interfaces.OrmProvider;
import com.bivgroup.common.utils.converter.DataConverter;
import com.bivgroup.core.service.interfaces.CoreManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Created by bush on 10.08.2016.
 */
public class ExchangeFacade {
    private transient Logger logger = LogManager.getLogger(this.getClass());

    private OrmProvider orm;
    private CoreManager core;
    private DataConverter converter;

    public ExchangeFacade(CoreManager core, OrmProvider orm) {
        this.core = core;
        this.orm = orm;
        this.converter = new DataConverter();
    }

    public Map<String, Object> admRefCurrencyList(Map<String, Object> params) throws Exception {
        if ("".equals(params.get(Constants.PARAM_SEARCHCOLUMN))) {
            params.remove(Constants.PARAM_SEARCHCOLUMN);
        }

        if ("".equals(params.get(Constants.PARAM_SEARCHTEXT))) {
            params.remove(Constants.PARAM_SEARCHTEXT);
        }

        Map result = orm.getCommonListPageRezult("admRefCurrencyList", params);
        return result;
    }


    public Map<String, Object> admGetCurrencyPairsList(Map<String, Object> params) throws Exception {
        if ("".equals(params.get("BASECURRENCY"))) {
            params.remove("BASECURRENCY");
        }

        if ("".equals(params.get("QUOTEDCURRENCY"))) {
            params.remove("QUOTEDCURRENCY");
        }

        if ("".equals(params.get("STARTDATE"))) {
            params.remove("STARTDATE");
        }

        if ("".equals(params.get("ENDDATE"))) {
            params.remove("ENDDATE");
        }

        if ("".equals(params.get(Constants.PARAM_STATUS))) {
            params.remove(Constants.PARAM_STATUS);
        }

        if (!params.containsKey(Constants.PARAM_ORDERBY)) {
            params.put(Constants.PARAM_ORDERBY, "BASECURRENCY");
        }


        converter.convertDateToFloat(params);
        Map result = orm.getCommonListPageRezult("getCurrencyPairsList", params);//QueryBuilder.getList(context, "getExchangeListCount", "getExchangeList", params);
        converter.convertFloatToDate(result);
        return result;
    }

    public Map<String, Object> getExchangeList(Map<String, Object> params) throws Exception {
        Date e = new Date();
        Date searchEndDate = new Date();
        if (params.containsKey("searchStartDate")) {
            e = (Date) params.get("searchStartDate");
        }

        if (params.containsKey("searchEndDate")) {
            searchEndDate = (Date) params.get("searchEndDate");
        }

        GregorianCalendar endCal = new GregorianCalendar();
        endCal.setTime(searchEndDate);
        endCal.set(10, 23);
        endCal.set(12, 59);
        endCal.set(13, 59);
        searchEndDate = endCal.getTime();
        GregorianCalendar stCal = new GregorianCalendar();
        stCal.setTime(e);
        stCal.set(10, 0);
        stCal.set(12, 0);
        stCal.set(13, 1);
        e = stCal.getTime();
        if ("".equals(params.get("exchangeType"))) {
            params.remove("exchangeType");
        }

        if ("".equals(params.get("purposeCurrency"))) {
            params.remove("purposeCurrency");
        }

        //params.put("searchStartDate", ServiceUtils.truncateDayTime(e));
        // params.put("searchEndDate", ServiceUtils.getDateWithEndDayTime(searchEndDate));
        converter.convertDateToFloat(params);
        params.put(Constants.PARAM_ORDERBY, "EXCHANGEID");
        Map result = orm.getCommonListPageRezult("getExchangeList", params);
        return result;
    }

    private Map<String, Object> getCurrencyExchangeDataForSave(Map<String, Object> currencyExchange) throws Exception {
        HashMap params = new HashMap(10);
        params.put("CourseValue", currencyExchange.get("CurrencyValue"));
        Date courseDate = null;
        if (currencyExchange.get("CourseDate") instanceof Date) {
            courseDate = (Date) currencyExchange.get("CourseDate");
        }

        params.put("CourseDate", Integer.valueOf(converter.convertDate(courseDate).intValue()));
        params.put("UnitNumber", currencyExchange.get("UnitNumber"));
        HashMap selectParams = new HashMap();
        selectParams.put("CourseTypeName", currencyExchange.get("ExchangeType"));
        Map exchangeType = orm.getCommonRecordRezult("getExchangeTypeByName", selectParams);
        params.put("ExchangeTypeId", exchangeType.get("EXCHANGETYPEID"));
        return params;
    }


    public Map createCurrencyExchange(Map<String, Object> currencyExchange) throws Exception {
        Map params = this.getCurrencyExchangeDataForSave(currencyExchange);
        Long currencyExchangeId = core.getNewId("REF_EXCHANGE");
        params.put("CurrencyExchangeId", currencyExchangeId);
        params.put("CourseValue", currencyExchange.get("CourseValue"));
        params.put("CurrencyPairId", currencyExchange.get("CurrencyPairId"));
        if (params.containsKey("CourseDate") && params.get("CourseDate") instanceof Date) {
            GregorianCalendar cpRes = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
            cpRes.setTime((Date) params.get("CourseDate"));
            cpRes.set(11, 0);
            cpRes.set(12, 0);
            params.put("CourseDate", cpRes.getTime());
        }

        converter.convertDateToFloat(currencyExchange);
        List cpRes1 = orm.getCommonListRezult("checkCurrencyPairDate", currencyExchange);
        if (cpRes1 != null && cpRes1.size() != 0) {
            List sRes = orm.getCommonListRezult("checkExistingRate", params);
            if (sRes != null && sRes.size() != 0) {
                throw new Exception("RateAlreadyExists");
            } else {
                orm.performNonSelectingQuery("createCurrencyExchange", params);
                HashMap resultMap = new HashMap();
                resultMap.put("CurrencyExchangeId", currencyExchangeId);
                return resultMap;
            }
        } else {
            throw new Exception("CurrencyPairInactive");
        }
    }
}
