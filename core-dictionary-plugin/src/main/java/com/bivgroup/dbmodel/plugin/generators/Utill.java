package com.bivgroup.dbmodel.plugin.generators;

import com.bivgroup.common.orm.OrmException;
import com.bivgroup.core.dictionary.exceptions.DictionaryException;
import com.bivgroup.core.dictionary.generator.visitors.util.NameGenerator;
import org.apache.maven.project.MavenProject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by bush on 09.11.2016.
 * Утилиты для плагина
 */
public class Utill {

    /**
     * преобразование каталога из  для WIN
     *
     * @param dir - директория
     */
    public void replaceForUnix(String dir) {
        if (dir.contains("\\")) dir.replace(Constants.DIR_DELIMETER, "\\");
    }

    /**
     * Сохранения файла
     *
     * @param outputDirectory - выходная директория
     * @param fileName - название файла
     * @param obj - содержимое файла
     * @throws OrmException - исключение ORM
     */
    public void saveFile(String outputDirectory, String fileName, String obj) throws OrmException {
        try {
            final Path infoPath = Paths.get(outputDirectory, fileName);
            if (infoPath.getParent() != null) {
                Files.createDirectories(infoPath.getParent());
            }
            Files.write(infoPath, obj.getBytes());
        } catch (IOException e) {
            throw new OrmException("Error creating file ", e);
        } finally {
        }
    }

    /**
     * Преобразование строки: удаление "-", первый символ в верхнем регисте
     *
     * @param artifact - нименование артефакта
     * @return - строка
     */
    public String convertName(String artifact) {
        artifact = artifact.replace("-", "");
        char c[] = artifact.toCharArray();
        c[0] = Character.toUpperCase(c[0]);
        return new String(c);
    }

    /**
     * Название пакета из плагина
     *
     * @param module - плагин
     * @return - название пакета
     * @throws DictionaryException - исключение словарной системы
     */
    public String getModulePackage(MavenProject module) throws DictionaryException {
        if (module == null) return NameGenerator.DEFAULT_PACKAGE_NAME;
        if (module != null && module.getArtifactId() == null)
            throw new DictionaryException(String.format("On Module not set requred param ArtifactId name %1s", module.getArtifactId()));
        return new StringBuffer().append(module.getGroupId() != null ? module.getGroupId() : NameGenerator.DEFAULT_PACKAGE_NAME).append(Constants.PACKAGE_DELIMETER).append(module.getArtifactId()).toString();
    }

    /**
     * Преобразование строки: из пакета в каталог
     *
     * @param group - группа (пакет)
     * @return - каталог
     */
    public String getDirByGroup(String group) {
        return group.replace(Constants.PACKAGE_DELIMETER, Constants.DIR_DELIMETER);
    }

}
