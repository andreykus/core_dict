package com.bivgroup.dbmodel.plugin.generators;

import com.bivgroup.core.dictionary.exceptions.DictionaryException;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bush on 09.11.2016.
 * Генератор класса DAO модуля
 */
public class GeneratorClass extends AbstractGenerator implements GeneratorObject {
    /**
     * логгер
     */
    Log logger;
    /**
     * выходная директория
     */
    String outputDirectory;
    final String CLASS_DIR = "/java";
    /**файл шаблона дао*/
    final String DAO_CLASS_TEMPLATE = "ModuleDAO.vm";
    /**
     * данные maven проекта
     */
    MavenProject mavenProject;
    /**
     * алиас jndi
     */
    String jndi;
    /**
     * утилита сохранения файлов
     */
    Utill ut;

    /**
     * Конструктор Генератор класса DAO модуля
     *
     * @param outputDirectory - выходная директория
     * @param mavenProject    - данные maven проекта
     * @param jndi            - алиас jndi
     * @param logger          - логгер
     * @throws DictionaryException - исключение словарной системы
     */
    public GeneratorClass(String outputDirectory, MavenProject mavenProject, String jndi, Log logger) throws DictionaryException {
        this.logger = logger;
        this.ut = new Utill();
        this.outputDirectory = outputDirectory + CLASS_DIR + Constants.DIR_DELIMETER + ut.getDirByGroup(ut.getModulePackage(mavenProject));
        this.mavenProject = mavenProject;
        this.jndi = jndi;
    }

    /**
     * Сгененрить по шаблону
     * @throws MojoExecutionException - исключение maven
     */
    @Override
    public void generate() throws MojoExecutionException {
        try {
            ut.replaceForUnix(outputDirectory);
            Map param = new HashMap<>();
            param.put(Constants.PARAM_CLASS_NAME, ut.convertName(mavenProject.getArtifactId()));
            param.put(Constants.PARAM_PACKAGE_NAME, ut.getModulePackage(mavenProject));
            param.put(Constants.PARAM_JNDI, jndi);
            param.put(Constants.PARAM_UNIT_NAME, ut.convertName(mavenProject.getArtifactId()));
            String clazz = generateObj(param, Constants.DIR_TEMPLATE + DAO_CLASS_TEMPLATE);
            ut.saveFile(outputDirectory, ut.convertName(mavenProject.getArtifactId()) + Constants.EXTEND_FILE_CLASS, clazz);
        } catch (Exception ex) {
            throw new MojoExecutionException("Exception ", ex);
        }
    }
}
