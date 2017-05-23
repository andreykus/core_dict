package com.bivgroup.dbmodel.plugin.generators;

/**
 * Created by andreykus on 13.11.2016.
 * Константы для плагина
 */
public interface Constants {
    /**
     * шаблон
     */
    final String JPA_TEMPLATE = "persistence.vm";
    /**
     * результирующий файл
     */
    final String CFG_NAME_JPA = "persistence.xml";
    /**
     * каталог выходных классов
     */
    final String CLASS_DIR = "/java";
    /**
     * каталог ресурсов
     */
    final String RESOURCES_DIR = "/resources/META-INF";
    /**
     * каталог ресурсов
     */
    final String RESOURCES_DIR_NOT_METAINF = "/resources";
    /**
     * шаблон
     */
    final String DAO_MAP_TEMPLATE = "hbmxml.vm";
    /**
     * результирующий файл
     */
    final String CFG_NAME_HIBERNATE = "hibernate.cfg.xml";
    /**
     * каталог с шаблонами
     */
    final String DIR_TEMPLATE = "/template/";
    /**
     * параметр - список классов
     */
    final String PARAM_CLASS_LIST = "classList";
    /**
     * параметр - список map
     */
    final String PARAM_MAP_LIST = "mapList";
    /**
     * параметр - название класса
     */
    final String PARAM_CLASS_NAME = "className";
    /**
     * параметр - название unit
     */
    final String PARAM_UNIT_NAME = "unitName";
    /**
     * параметр - название пакета
     */
    final String PARAM_PACKAGE_NAME = "packageName";
    /**
     * параметр - название конфига
     */
    final String PARAM_CFG_NAME = "cfgName";
    /**
     * параметр - название алиаса jndi
     */
    final String PARAM_JNDI = "jndi";
    /**
     * расширение файла конфигурации
     */
    final String EXTEND_FILE_CFG = ".hdm";
    /**
     * аспект файл
     */
    final String ASPECT_FILE_CFG = "aspects.xml";
    /**
     * расширение файла класса
     */
    final String EXTEND_FILE_CLASS = ".java";
    /**
     * разделитель пакета
     */
    final String PACKAGE_DELIMETER = ".";
    /**
     * разделитель каталогов
     */
    final String DIR_DELIMETER = "/";
}
