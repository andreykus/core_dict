package com.bivgroup.dbmodel.plugin.generators;

import org.apache.maven.plugin.MojoExecutionException;

/**
 * Created by bush on 09.11.2016.
 * Интерфейс генерации
 */
public interface GeneratorObject {
    /**
     * Сгенерировать
     *
     * @throws MojoExecutionException - исключение maven
     */
    void generate() throws MojoExecutionException;
}
