package com.bivgroup.dbmodel.plugin.generators;

import com.bivgroup.common.orm.OrmProviderImpl;
import com.bivgroup.core.dictionary.entity.SadModule;
import com.bivgroup.core.dictionary.entity.enums.TypeExport;
import com.bivgroup.core.dictionary.exceptions.DictionaryException;
import com.bivgroup.core.dictionary.generator.ExternalConsumerConfig;
import com.bivgroup.core.dictionary.generator.GenerateMetadata;
import com.bivgroup.core.dictionary.generator.GenerateMetadataImpl;
import com.bivgroup.dbmodel.plugin.inobject.DataBase;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import javax.sql.DataSource;
import java.lang.reflect.Method;

/**
 * Created by bush on 09.11.2016.
 * Генератор модели БД по словарной системе
 */
public class GeneratorDBModel implements GeneratorObject {
    /**
     * PERSISTENT UNIT для модкля генератора
     */
    static String UNIT_NAME = "Generator";
    /**
     * выходная директолрия
     */
    String outputDirectory;
    /**
     * параметры подключения к БД
     */
    DataBase dataBase;
    /**
     * логгер
     */
    Log logger;
    /**
     * параметры maven проекта
     */
    MavenProject mavenProject;
    /**
     * тип выходной конфигурации
     */
    TypeExport typeOutObject;
    /**
     * алиас jndi
     */
    String jndi;

    /**
     * Конструктор Генератор модели БД по словарной системе
     *
     * @param outputDirectory -  выходная директория
     * @param mavenProject    -  параметры maven проекта
     * @param typeOutObject   - тип выходной конфигурации
     * @param dataBase        - параметры подключения к БД
     * @param jndi            - алиас jndi
     * @param logger          - логгер
     */
    public GeneratorDBModel(String outputDirectory, MavenProject mavenProject, TypeExport typeOutObject, DataBase dataBase, String jndi, Log logger) {
        this.outputDirectory = outputDirectory;
        this.dataBase = dataBase;
        this.mavenProject = mavenProject;
        this.logger = logger;
        this.typeOutObject = typeOutObject;
        this.jndi = jndi;
    }

    /**
     * Создать генератор модели по типу экспорта
     *
     * @return - генератор
     * @throws DictionaryException - исключение словарной системы
     */
    protected GenerateMetadata buildGenerateMetadata() throws DictionaryException {
        OrmProviderImpl provider = new OrmProviderImpl((DataSource) getDataSource(), UNIT_NAME);
        GenerateMetadataImpl generator = new GenerateMetadataImpl(provider);
        generator.setType(typeOutObject);
        return generator;
    }

    /**
     * Создать DataSource по параметрам
     *
     * @return - DataSource
     * @throws DictionaryException - исключение словарной системы
     */
    private DataSource getDataSource() throws DictionaryException {
        DataSource dataSource;
        try {
            Class clazz = Class.forName(dataBase.getDataSourceClass());
            dataSource = (DataSource) clazz.newInstance();
            Method setURL = clazz.getDeclaredMethod("setURL", String.class);
            Method setUser = clazz.getDeclaredMethod("setUser", String.class);
            Method setPassword = clazz.getDeclaredMethod("setPassword", String.class);
            setURL.invoke(dataSource, dataBase.getUrl());
            if (dataBase.getLogin() != null) setUser.invoke(dataSource, dataBase.getLogin());
            if (dataBase.getPassword() != null) setPassword.invoke(dataSource, dataBase.getPassword());
        } catch (Exception ex) {
            throw new DictionaryException(ex);
        }
        return dataSource;
    }

    /**
     * Получить потребителя словарной системы, от типа экспорта
     *
     * @param outputDirectory - выходная директория
     * @param mavenProject    - модуль
     * @param typeOutObject   - тип экспорта
     * @param jndi            - JNDI
     * @param logger          - логгер
     * @return - потребитель
     */
    private ExternalConsumerConfig getImporterConsumer(String outputDirectory, MavenProject mavenProject, TypeExport typeOutObject, String jndi, Log logger) {
        switch (typeOutObject) {
            case XML:
                return new FileConfigConsumer(outputDirectory, mavenProject, jndi, logger);
            case CLASS:
                return new FileClassConsumer(outputDirectory, mavenProject, jndi, logger);
            case ASPECT:
                return new FileAspectConsumer(outputDirectory, mavenProject, jndi, logger);
        }
        return null;
    }

    /**
     * Генерация метамодели - реализация
     *
     * @throws MojoExecutionException - исключение maven
     */
    private void generateDbModel() throws MojoExecutionException {
        try {
            GenerateMetadata generator = buildGenerateMetadata();
            generator.setConsumer(getImporterConsumer(outputDirectory, mavenProject, typeOutObject, jndi, logger));
            generator.setConsumer(getImporterConsumer(outputDirectory, mavenProject, TypeExport.ASPECT, jndi, logger));
            SadModule module = new SadModule();
            module.setArtifactId(mavenProject.getArtifactId());
            module.setGroupId(mavenProject.getGroupId());
            //generator.generateAll();
            generator.generateByModuleWithDependency(module);
        } catch (Exception e) {
            throw new MojoExecutionException("Exception ", e);
        }
    }

    /**
     * Генерация метамодели
     *
     * @throws MojoExecutionException - исключение maven
     */
    @Override
    public void generate() throws MojoExecutionException {
        generateDbModel();
    }
}
