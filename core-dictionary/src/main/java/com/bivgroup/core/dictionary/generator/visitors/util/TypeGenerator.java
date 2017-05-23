package com.bivgroup.core.dictionary.generator.visitors.util;

import com.bivgroup.core.dictionary.exceptions.DictionaryException;
import org.hibernate.mapping.SimpleValue;
import org.hibernate.type.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * Created by andreykus on 19.09.2016.
 * Генератор тип по полю в БД, на выходе в метаданных приведен к типа ORM
 */
public enum TypeGenerator {
    /**
     * приведение Long
     */
    LONG(Long.class, LongType.INSTANCE.getName()),
    /**
     * приведение Short
     */
    SHORT(Short.class, ShortType.INSTANCE.getName()),
    /**
     * приведение Integer
     */
    INTEGER(Integer.class, IntegerType.INSTANCE.getName()),
    /**
     * приведение Byte
     */
    BYTE(Byte.class, ByteType.INSTANCE.getName()),
    /**
     * приведение Float
     */
    FLOAT(Float.class, FloatType.INSTANCE.getName()),
    /**
     * приведение Double
     */
    DUBLE(Double.class, DoubleType.INSTANCE.getName()),
    /**
     * приведение Character
     */
    CHARACTER(Character.class, CharacterType.INSTANCE.getName()),
    /**
     * приведение String
     */
    STRING(String.class, StringType.INSTANCE.getName()),
    /**
     * приведение BigDecimal
     */
    BIG_DECIMAL(BigDecimal.class, BigDecimalType.INSTANCE.getName()),
    /**
     * приведение BigInteger
     */
    BIG_INTEGER(BigInteger.class, BigIntegerType.INSTANCE.getName()),
    /**
     * приведение Binary
     */
    BINARY(byte[].class, BinaryType.INSTANCE.getName()),
    /**
     * приведение Date
     */
    DATE(Date.class, DateType.INSTANCE.getName()),
    /**
     * приведение Date
     */
    DATE1(java.sql.Date.class, DateType.INSTANCE.getName()),
    /**
     * приведение Time
     */
    TIME(java.sql.Time.class, TimeType.INSTANCE.getName()),
    /**
     * приведение Timestamp
     */
    TIMESTAMP(java.sql.Timestamp.class, TimestampType.INSTANCE.getName()),
    /**
     * приведение Calendar
     */
    CALENDAR(java.util.Calendar.class, CalendarType.INSTANCE.getName()),
    /**
     * приведение Clob
     */
    CLOB(java.sql.Clob.class, ClobType.INSTANCE.getName()),
    /**
     * приведение Blob.
     */
    BLOB(java.sql.Blob.class, BlobType.INSTANCE.getName());

    /**
     * тип java
     */
    private Class javaType;
    /**
     * тип hibernate
     */
    private String hibernateType;

    /**
     * Конструктор Генератор тип по полю в БД, на выходе в метаданных приведен к типа ORM
     *
     * @param javaType
     * @param hibernateType
     */
    private TypeGenerator(Class javaType, String hibernateType) {
        this.javaType = javaType;
        this.hibernateType = hibernateType;
    }

    /**
     * тип по классу
     *
     * @param clazz - класс
     * @return - тип
     */
    private static TypeGenerator getByClass(Class clazz) {
        for (TypeGenerator gen : TypeGenerator.values()) {
            if (gen.javaType.equals(clazz)) {
                return gen;
            }
        }
        return null;
    }

    /**
     * установить для значения свойства
     *
     * @param value        - значения свойства
     * @param nameJavaType -  тип из БД
     * @throws DictionaryException -  исключение словарной сиситемы
     */
    public static void setType(SimpleValue value,
                               String nameJavaType) throws DictionaryException {
        if (nameJavaType == null) throw new DictionaryException("for set type not set name type ");

        Class javaType = null;
        try {
            javaType = Class.forName(nameJavaType);
        } catch (ClassNotFoundException e) {
            throw new DictionaryException(String.format("this class %1s not found in java", nameJavaType));
        }
        TypeGenerator gen = getByClass(javaType);
        if (gen == null)
            throw new DictionaryException(String.format("this class %1s not found in TypeGenerator", nameJavaType));
        String nameType = gen.hibernateType;
        if (value != null && nameType != null) {
            value.setTypeName(nameType);
        }
    }

}

