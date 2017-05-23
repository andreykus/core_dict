package com.bivgroup.dbmodel.plugin.generators;

import com.bivgroup.common.orm.ExportObject;
import com.bivgroup.common.orm.OrmException;
import com.bivgroup.common.utils.observer.Observer;
import com.bivgroup.core.dictionary.entity.enums.TypeExport;
import com.bivgroup.core.dictionary.generator.ExternalConsumerConfig;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by andreykus on 13.11.2016.
 * Потребитель словарной системы, получает при генерации stream классов Enity
 */
public class FileClassConsumer extends AbstractGenerator implements ExternalConsumerConfig {
    /**
     * логгер
     */
    Log logger;
    /**
     * список файлов
     */
    List<String> listFiles;
    /**
     * данные maven проекта
     */
    MavenProject mavenProject;
    /**
     * выходная директория
     */
    String outputDirectory;
    /**
     * алиас jndi
     */
    String jndi;
    /**
     * утилита сохранения файлов
     */
    Utill ut;

    /**
     * Конструктор Потребитель словарной системы
     *
     * @param outputDirectory - выходная директория
     * @param mavenProject    - данные maven проекта
     * @param jndi            - алиас jndi
     * @param logger          - логгер
     */
    public FileClassConsumer(String outputDirectory, MavenProject mavenProject, String jndi, Log logger) {
        this.outputDirectory = outputDirectory;
        this.logger = logger;
        this.listFiles = new ArrayList<String>();
        this.mavenProject = mavenProject;
        this.jndi = jndi;
        this.ut = new Utill();
    }

    /**
     * Сохранение persistence.xml
     *
     * @throws OrmException - исключение ORM
     */
    private void saveCfgXml() throws OrmException {
        try {
            Map params = new HashMap<>();
            params.put(Constants.PARAM_CLASS_LIST, listFiles);
            params.put(Constants.PARAM_UNIT_NAME, ut.convertName(mavenProject.getArtifactId()));
            params.put(Constants.PARAM_JNDI, jndi);
            String cfgpers = generateObj(params, Constants.DIR_TEMPLATE + Constants.JPA_TEMPLATE);
            ut.saveFile(outputDirectory + Constants.RESOURCES_DIR, Constants.CFG_NAME_JPA, cfgpers);
        } catch (Exception ex) {
            throw new OrmException(ex);
        }
    }

    /**
     * Обработчик полученных данных
     *
     * @param configs - то, что пришло от словарной системы
     * @throws OrmException - исключение ORM
     */
    @Override
    public void processConfig(List<ExportObject> configs) throws OrmException {
        logger.info("start save Modules");
        String outputDirectoryClass = outputDirectory + Constants.CLASS_DIR + Constants.DIR_DELIMETER;
        ut.replaceForUnix(outputDirectoryClass);
        for (ExportObject config : configs) {
            String fileName = config.className + Constants.EXTEND_FILE_CLASS;
            //сохранение классов - Entity
            ut.saveFile(outputDirectoryClass + ut.getDirByGroup(config.moduleName), fileName, config.body.toString());
            listFiles.add(config.moduleName + "." + config.className);
            logger.info(String.format(" generate DB Model to dir %1s  file %2s  ", outputDirectoryClass, fileName));
        }
        //прочие конфиги
        saveCfgXml();
        logger.info("finish save Modules");
    }

    @Override
    public void register(Observer Observer) {
        //DO nothing
    }

    @Override
    public void unRegistrer(Observer Observer) {
        //DO nothing
    }

    @Override
    public void notifyObservers(Object... arg) throws OrmException {
        //DO nothing
    }

    /**
     * тип экспорта
     *
     * @return - тип
     */
    @Override
    public TypeExport getTypeObject() {
        return TypeExport.CLASS;
    }
}
