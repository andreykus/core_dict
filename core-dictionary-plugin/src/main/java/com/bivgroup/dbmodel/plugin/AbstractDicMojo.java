package com.bivgroup.dbmodel.plugin;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.project.MavenProject;
import org.twdata.maven.mojoexecutor.MojoExecutor;

import static org.twdata.maven.mojoexecutor.MojoExecutor.executionEnvironment;

/**
 * Created by bush on 08.11.2016.
 * Абстракция maven плагина
 */
abstract class AbstractDicMojo extends AbstractMojo {

    /**
     * параметр - проект, группа, артефакт
     */
    @Component
    protected MavenProject mavenProject;
    /**
     * сессия maven
     */
    @Component
    protected MavenSession mavenSession;
    @Component
    protected BuildPluginManager pluginManager;
    /** исполнитель */
    protected MojoExecutor.ExecutionEnvironment _pluginEnv;
    /**
     * плагин maven
     */
    protected Plugin _pluginWagon;

    /**
     * инициируем параметры плагина
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        _pluginEnv = executionEnvironment(mavenProject, mavenSession, pluginManager);
//        if (!"".equalsIgnoreCase(mavenProject.getPackaging())) {
//            throw new MojoExecutionException("packaging type not " + "");
//        }
    }
}
