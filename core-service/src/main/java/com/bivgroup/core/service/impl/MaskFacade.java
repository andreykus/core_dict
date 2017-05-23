package com.bivgroup.core.service.impl;

import com.bivgroup.common.Constants;
import com.bivgroup.common.orm.interfaces.OrmProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by bush on 08.08.2016.
 */
public class MaskFacade {
    private transient Logger logger = LogManager.getLogger(this.getClass());

    private OrmProvider orm;

    private static final Pattern MASK_ELEMENTS_COMMON_PATTERN = Pattern.compile("(\\{N0?\\d*\\})|(\\{[a-zA-Z]{1}\\-[a-zA-Z]{1}\\})|(\\{[а-яА-Я]{1}\\-[а-яА-Я]{1}\\})|(\\{[a-zа-яA-ZА-Я]+\\})");
    private static final Pattern NUMERIC_COUNTER_MASK_PATTERN = Pattern.compile("N(0*)(\\d*)");
    private static final Pattern LITERAL_COUNTER_MASK_PATTERN = Pattern.compile("([a-zа-яA-ZА-Я]{1})\\-([a-zа-яA-ZА-Я]{1})");
    private static final Pattern COMMON_MASK_PATTERN = Pattern.compile("([a-zа-яA-ZА-Я]+)");
    private static final Pattern CHECK_SYSNAME_PATTERN = Pattern.compile("^([a-zA-Z]{1}[a-zA-Z\\d\\-_]*)$");

    public MaskFacade(OrmProvider orm) {
        this.orm = orm;
    }

    public Map<String, Object> dsCountersFindByMaskAndCriterion(Map<String, Object> params) throws Exception {
        HashMap result = new HashMap(1);
        List counters = orm.getCommonListRezult("dsGetCounters", params);
        if (counters != null && counters.size() > 0) {
            result.put(Constants.RESULT_FIELD_NAME, counters);
        }
        result.put(Constants.STATUS_FIELD_NAME, Constants.STATUS_OK);
        return result;
    }

    private Map<String, Object> getMaskCountersLastValues(Object maskId, String resolvedCriterion, Integer batch) throws Exception {
        HashMap result = new HashMap(1);
        Map counterParams = new HashMap();
        counterParams.put("MASKID", maskId);
        counterParams.put("CRITERION", resolvedCriterion);
        counterParams.put("BATCH", batch);
        Map countersMap = this.dsCountersFindByMaskAndCriterion(counterParams);
        List countersList = (List) countersMap.get(Constants.RESULT_FIELD_NAME);

        boolean lastNumericCounterValue = false;
        boolean lastLatinLiteralCounterValue = false;
        boolean lastCyrillicLiteralCounterValue = false;

        if (countersList.size() > 0) {
            String e;
            try {
                e = ((Map) countersList.get(0)).get("NUMVALUE").toString();
                int lastNumericCounterValue1 = Integer.parseInt(e);
                result.put("LASTNUMVALUE", Integer.valueOf(lastNumericCounterValue1));

            } catch (Exception var17) {
                ;
            }

            try {
                e = ((Map) countersList.get(0)).get("LATVALUE").toString();
                int lastLatinLiteralCounterValue1 = Integer.parseInt(e);
                result.put("LASTLATVALUE", Integer.valueOf(lastLatinLiteralCounterValue1));

            } catch (Exception var16) {
                ;
            }

            try {
                e = ((Map) countersList.get(0)).get("CYRVALUE").toString();
                int lastCyrillicLiteralCounterValue1 = Integer.parseInt(e);
                result.put("LASTCYRVALUE", Integer.valueOf(lastCyrillicLiteralCounterValue1));

            } catch (Exception var15) {
                ;
            }
        }

        return result;
    }

    private Map<String, Object> dsMaskFindById(Long maskId) throws Exception {
        Map<String, Long> inParam = new HashMap<String, Long>();
        inParam.put("MASKID", maskId);
        Map<String, Object> rez = orm.getCommonRecordRezult("dsMaskFindById", inParam);
        return rez;
    }


    public Map<String, Object> dsMaskFindById(Map<String, Object> params) throws Exception {
        //truncateEmptyParams(params);
        Map mask = orm.getCommonRecordRezult("dsMaskFindById", params);
        return mask;
    }

    public Map<String, Object> dsMaskFindByBrief(Map<String, Object> params) throws Exception {
        HashMap result = new HashMap(1);
        //truncateEmptyParams(params);
        Map mask = orm.getCommonRecordRezult("dsMaskFindByBrief", params);
        return mask;
    }

    private String resolveNumericCounterValue(String mask, int lastCounterValue) {
        boolean precedingZeros = false;
        int counterLength = -1;
        Matcher matcher = NUMERIC_COUNTER_MASK_PATTERN.matcher(mask);
        if (matcher.find()) {
            precedingZeros = matcher.group(1) != null && matcher.group(1).length() > 0;
            if (matcher.group(2) != null && matcher.group(2).length() > 0) {
                try {
                    counterLength = Integer.parseInt(matcher.group(2));
                } catch (NumberFormatException var8) {
                    counterLength = -1;
                }
            }
        }

        if (counterLength > 0 && String.valueOf(lastCounterValue).length() > counterLength) {
            lastCounterValue = 1;
        }

        StringBuilder counter = new StringBuilder();
        if (precedingZeros) {
            for (int i = 0; i < counterLength - String.valueOf(lastCounterValue).length(); ++i) {
                counter.append(0);
            }
        }

        counter.append(lastCounterValue);
        return counter.toString();
    }

    public String resolveCommonStatementValue(String mask, Map<String, Object> params, boolean isCriterion) {
        String result = isCriterion ? "" : mask;
        Matcher matcher = COMMON_MASK_PATTERN.matcher(mask);
        if (matcher.find()) {
            String value = String.valueOf(params.get(matcher.group(1)));
            if (value != null) {
                result = value;
            }
        }

        return result;
    }

    public String resolveLiteralCounterValue(String mask, char lastCounterValue) {
        boolean moveBackward = false;
        char startChar = 65;
        char endChar = 65;
        Matcher matcher = LITERAL_COUNTER_MASK_PATTERN.matcher(mask);
        if (matcher.find()) {
            startChar = matcher.group(1) == null ? 65 : matcher.group(1).charAt(0);
            endChar = matcher.group(2) == null ? 65 : matcher.group(2).charAt(0);
            boolean var10000 = startChar > endChar;
        }

        if (lastCounterValue == 0) {
            lastCounterValue = 65;
        }

        if (lastCounterValue < Math.min(startChar, endChar)) {
            lastCounterValue = (char) Math.min(startChar, endChar);
        }

        if (lastCounterValue > Math.max(startChar, endChar)) {
            lastCounterValue = (char) Math.max(startChar, endChar);
        }

        HashMap result = new HashMap();
        result.put("NextCounterValue", Character.valueOf(lastCounterValue));
        StringBuilder counter = new StringBuilder();
        counter.append(lastCounterValue);
        return counter.toString();
    }

    public String parseMask(String mask, Map<String, Object> params, boolean isCriterion, int lastNumericCounterValue, char lastLatinLiteralCounterValue, char lastCyrillicLiteralCounterValue) throws Exception {
        StringBuilder autoNumber = new StringBuilder();
        int charsCounter = 0;

        int end;
        for (Matcher matcher = MASK_ELEMENTS_COMMON_PATTERN.matcher(mask); matcher.find(); charsCounter = end) {
            int start = matcher.start();
            end = matcher.end();
            if (start > charsCounter) {
                autoNumber.append(mask.substring(charsCounter, start));
            }

            String resolveResult = "";
            if (matcher.group(1) != null && !isCriterion) {
                resolveResult = this.resolveNumericCounterValue(matcher.group(1), lastNumericCounterValue);
            }

            if (matcher.group(2) != null && !isCriterion) {
                resolveResult = this.resolveLiteralCounterValue(matcher.group(2), lastLatinLiteralCounterValue);
            }

            if (matcher.group(3) != null && !isCriterion) {
                resolveResult = this.resolveLiteralCounterValue(matcher.group(3), lastCyrillicLiteralCounterValue);
            }

            if (matcher.group(4) != null) {
                resolveResult = this.resolveCommonStatementValue(matcher.group(4), params, isCriterion);
            }

            autoNumber.append(resolveResult == null ? "" : resolveResult);
        }

        if (charsCounter < mask.length() - 1) {
            autoNumber.append(mask.substring(charsCounter));
        }

        return autoNumber.toString();
    }

    public Map<String, Object> dsMaskFind(Map<String, Object> params) throws Exception {
        HashMap result = new HashMap(1);
        if (params.get("MASKID") != null) {
            return this.dsMaskFindById(params);
        } else if (params.get("SYSTEMBRIEF") != null) {
            return this.dsMaskFindByBrief(params);
        } else {
            result.put(Constants.STATUS_FIELD_NAME, Constants.STATUS_ERROR);
            result.put("FAULTCODE", "500");
            result.put("FAULTMESSAGE", "Neither MASKID nor SYSTEMBRIEF were not specified");
            return result;
        }
    }

    public Map<String, Object> dsNumberFindByMask(Map<String, Object> params) throws Exception {
        HashMap result = new HashMap(1);
        //this.checkParams(params);
        Map searchResult = this.dsMaskFind(params);
        Map maskInfo = searchResult;
        if (maskInfo != null && !maskInfo.isEmpty()) {
            String criterion = maskInfo.get("CRITERION") == null ? "" : String.valueOf(maskInfo.get("CRITERION"));
            String resolvedCriterion = criterion.length() > 0 ? this.parseMask(criterion, params, true, 0, '\u0000', '\u0000') : "";

            int batch = 1;
            if (params.get("BATCH") instanceof Number) {
                batch = Integer.parseInt(String.valueOf(params.get("BATCH")));
            }

            Map maskCountersLastValues = this.getMaskCountersLastValues(maskInfo.get("MASKID"), resolvedCriterion, Integer.valueOf(batch));
            int lastNumericCounterValue = 1;
            char lastLatinLiteralCounterValue = 65;
            char lastCyrillicLiteralCounterValue = 1040;
            if (maskCountersLastValues.get("LASTNUMVALUE") instanceof Number) {
                lastNumericCounterValue = ((Number) maskCountersLastValues.get("LASTNUMVALUE")).intValue();
            }

            if (maskCountersLastValues.get("LASTLATVALUE") instanceof Number) {
                lastLatinLiteralCounterValue = (char) ((Number) maskCountersLastValues.get("LASTLATVALUE")).intValue();
            }

            if (maskCountersLastValues.get("LASTCYRVALUE") instanceof Number) {
                lastCyrillicLiteralCounterValue = (char) ((Number) maskCountersLastValues.get("LASTCYRVALUE")).intValue();
            }

            String mask = maskInfo.get("MASK").toString();
            String resolvedMask = this.parseMask(mask, params, false, lastNumericCounterValue, lastLatinLiteralCounterValue, lastCyrillicLiteralCounterValue);


            result.put(Constants.RESULT_FIELD_NAME, resolvedMask);
            result.put(Constants.STATUS_FIELD_NAME, Constants.STATUS_OK);
            return result;
        } else {
            result.put(Constants.RESULT_FIELD_NAME, "");
            result.put(Constants.STATUS_FIELD_NAME, Constants.STATUS_OK);
            return result;
        }
    }
}
