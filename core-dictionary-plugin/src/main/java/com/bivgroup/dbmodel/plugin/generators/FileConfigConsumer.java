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
 * Created by andreykus on 03.11.2016.
 * Потребитель словарной системы, получает при генерации stream описание метамодели для orm
 */
public class FileConfigConsumer extends AbstractGenerator implements ExternalConsumerConfig {
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
    String outputDirectorynotmmeta;
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
    public FileConfigConsumer(String outputDirectory, MavenProject mavenProject, String jndi, Log logger) {
        this.outputDirectory = outputDirectory + Constants.RESOURCES_DIR;
        this.outputDirectorynotmmeta = outputDirectory + Constants.RESOURCES_DIR_NOT_METAINF;
        this.logger = logger;
        this.listFiles = new ArrayList<String>();
        this.mavenProject = mavenProject;
        this.jndi = jndi;
        this.ut = new Utill();
    }

    /**
     * Сохранение общих конфигов
     *
     * @throws OrmException - исключение ORM
     */
    private void saveCfgXml() throws OrmException {
        try {
            Utill ut = new Utill();
            Map params = new HashMap<>();
            String cfgfilename = mavenProject.getArtifactId() + "_" + Constants.CFG_NAME_HIBERNATE;
            params.put(Constants.PARAM_MAP_LIST, listFiles);
            params.put(Constants.PARAM_CLASS_NAME, ut.convertName(mavenProject.getArtifactId()));
            params.put(Constants.PARAM_UNIT_NAME, ut.convertName(mavenProject.getArtifactId()));
            params.put(Constants.PARAM_PACKAGE_NAME, ut.getModulePackage(mavenProject));
            params.put(Constants.PARAM_CFG_NAME, cfgfilename);
            params.put(Constants.PARAM_JNDI, jndi);
            //основной конфиг, содержащий hbm файлы
            String cfgmap = generateObj(params, Constants.DIR_TEMPLATE + Constants.DAO_MAP_TEMPLATE);
            ut.saveFile(outputDirectorynotmmeta, cfgfilename, cfgmap);
            //persistence.xml
            String cfgpers = generateObj(params, Constants.DIR_TEMPLATE + Constants.JPA_TEMPLATE);
            ut.saveFile(outputDirectory, Constants.CFG_NAME_JPA, cfgpers);

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
        Utill ut = new Utill();
        ut.replaceForUnix(outputDirectory);
        for (ExportObject config : configs) {
            String fileName = config.moduleName + Constants.EXTEND_FILE_CFG;
            //сохраняем отдельные hbm файлы -- один на модуль
            ut.saveFile(outputDirectorynotmmeta, fileName, config.body.toString());
            listFiles.add(fileName);
            logger.info(String.format(" generate DB Model to dir %1s  file %2s  ", outputDirectory, fileName));
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
        return TypeExport.XML;
    }
}
