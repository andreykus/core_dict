package com.bivgroup.dbmodel.plugin.generators;

import com.bivgroup.common.orm.ExportObject;
import com.bivgroup.common.orm.OrmException;
import com.bivgroup.common.utils.observer.Observer;
import com.bivgroup.core.dictionary.entity.enums.TypeExport;
import com.bivgroup.core.dictionary.generator.ExternalConsumerConfig;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bush on 09.12.2016.
 * Потребитель словарной системы, получает при генерации stream xml аспекта
 */
public class FileAspectConsumer extends AbstractGenerator implements ExternalConsumerConfig {
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
    public FileAspectConsumer(String outputDirectory, MavenProject mavenProject, String jndi, Log logger) {
        this.outputDirectory = outputDirectory + Constants.RESOURCES_DIR;
        this.outputDirectorynotmmeta = outputDirectory + Constants.RESOURCES_DIR_NOT_METAINF;
        this.logger = logger;
        this.listFiles = new ArrayList<String>();
        this.mavenProject = mavenProject;
        this.jndi = jndi;
        this.ut = new Utill();
    }

    /**
     * Обработчик полученных данных
     *
     * @param configs - то, что пришло от словарной системы
     * @throws OrmException - исключение ORM
     */
    @Override
    public void processConfig(List<ExportObject> configs) throws OrmException {
        logger.info("start save Aspect");
        ut.replaceForUnix(outputDirectory);
        for (ExportObject config : configs) {
            //сохраняем как файл
            ut.saveFile(outputDirectorynotmmeta, Constants.ASPECT_FILE_CFG, config.body.toString());
            listFiles.add(Constants.ASPECT_FILE_CFG);
            logger.info(String.format(" generate Aspect to dir %1s  file %2s  ", outputDirectory, Constants.ASPECT_FILE_CFG));
        }
    }

    @Override
    public void register(Observer Observer) {
        //do nothing
    }

    @Override
    public void unRegistrer(Observer Observer) {
        //do nothing
    }

    @Override
    public void notifyObservers(Object... arg) throws OrmException {
        //do nothing
    }

    /**
     * тип экспорта
     *
     * @return - тип
     */
    @Override
    public TypeExport getTypeObject() {
        return TypeExport.ASPECT;
    }
}
