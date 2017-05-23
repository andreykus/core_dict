package com.bivgroup.dbmodel.plugin;

import com.bivgroup.core.dictionary.entity.enums.TypeExport;
import com.bivgroup.core.dictionary.exceptions.DictionaryException;
import com.bivgroup.dbmodel.plugin.generators.Constants;
import com.bivgroup.dbmodel.plugin.generators.GeneratorClass;
import com.bivgroup.dbmodel.plugin.generators.GeneratorDBModel;
import com.bivgroup.dbmodel.plugin.inobject.DataBase;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.codehaus.plexus.util.xml.Xpp3Dom;

import static org.twdata.maven.mojoexecutor.MojoExecutor.*;

/**
 * Created by bush on 08.11.2016.
 * Плагин генерации модуля для работы со словарногй системой
 * Основной класс плагина
 */

@Mojo(name = "buildmodule",
        defaultPhase = LifecyclePhase.PROCESS_RESOURCES,
        requiresDependencyResolution = ResolutionScope.TEST,
        requiresOnline = false, requiresProject = true,
        threadSafe = false)

//@Mojo(name = "buildmodule",
//        defaultPhase = LifecyclePhase.PACKAGE,
//        requiresOnline = false, requiresProject = true,
//        threadSafe = false)

//@Execute(
//        lifecycle = "install",
//        phase = LifecyclePhase.COMPILE,
//        goal = "buildmodule"
//        )

public class DictionaryMojo
        extends AbstractDicMojo {

    /**параметр -  выходной каталог*/
    @Parameter(property = "outputDirectory", required = true, defaultValue = "${project.basedir}/src/main")
    protected String outputDirectory;

//    @Component
//    protected BuildPluginManager pluginManager;

    /**параметр -  тип экспорта*/
    @Parameter(property = "typeOutObject", defaultValue = "XML")
    private TypeExport typeOutObject;

    /**параметр -  БД*/
    @Parameter(property = "DataBase", required = true)
    protected DataBase dataBase;

    /**параметр -  БД в JNDI*/
    @Parameter(property = "jndi")
    protected String jndi;

    /**
     * Основной метод исполненния плагина
     *
     * @throws MojoExecutionException - исключение maven
     * @throws MojoFailureException - исключение maven
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        super.execute();
        validateParameters();
        //clean();
        //генерим метамодель
        generateDbModel();
        //генерим доп классы: DAO
        generateClass();
        //copyDependencies();
        //compile();
        //resources();
        //toJar();
    }

    /**
     * Проверим корректность входных параметров
     *
     * @throws MojoExecutionException - исключение maven
     */
    private void validateParameters() throws MojoExecutionException {
        if (mavenProject == null) {
            throw new MojoExecutionException("Must specify Maven Project");
        }
        if (mavenProject.getArtifactId() == null) {
            throw new MojoExecutionException("Must specify Maven Project ArtifactId");
        }
        if (mavenProject.getGroupId() == null) {
            throw new MojoExecutionException("Must specify Maven Project GroupId");
        }
        if (dataBase == null) {
            throw new MojoExecutionException("Must specify Data Base");
        }
        if (dataBase.getUrl() == null) {
            throw new MojoExecutionException("Must specify Data Base URL");
        }
        if (dataBase.getDataSourceClass() == null) {
            throw new MojoExecutionException("Must specify Data Base DATA SOURCES");
        }
    }

    /**
     * Генератор файлов ресурсов и классов entity (xml, hbm, java  ..)
     *
     * @throws MojoExecutionException - исключение maven
     */
    private void generateDbModel() throws MojoExecutionException {
        Resource resourceDir = new Resource();
        resourceDir.setDirectory("src/main/resources");
        _pluginEnv.getMavenProject().addResource(resourceDir);
        new GeneratorDBModel(outputDirectory, mavenProject, typeOutObject, dataBase, jndi, getLog()).generate();
    }

    /**
     * Генератор доп классов: DAO ..
     *
     * @throws MojoExecutionException - исключение maven
     */
    private void generateClass() throws MojoExecutionException {
        try {
            new GeneratorClass(outputDirectory, mavenProject, jndi, getLog()).generate();
        } catch (DictionaryException ex) {
            throw new MojoExecutionException(ex.getMessage(), ex);
        }
    }

    /**
     * копирование зависимостей
     *
     * @throws MojoExecutionException - исключение maven
     */
    private void copyDependencies() throws MojoExecutionException {
        Plugin pluginDependency = plugin("org.apache.maven.plugins", "maven-dependency-plugin", "2.8");
        final Xpp3Dom configuration = configuration(element(name("useSubDirectoryPerScope"), "true"));
//        if (null != excludeGroupIds && !excludeGroupIds.trim().isEmpty()) {
//            cfg.addChild(element(name("excludeGroupIds"), excludeGroupIds).toDom());
//        }
        executeMojo(pluginDependency, goal("copy-dependencies"), configuration, _pluginEnv);
        getLog().info("copy Dependency");
    }

    /**
     * копирование ресурсов
     *
     * @throws MojoExecutionException - исключение maven
     */
    private void resources() throws MojoExecutionException {
        Plugin pluginDependency = plugin("org.apache.maven.plugins", "maven-resources-plugin", "3.0.1");
        final Xpp3Dom configuration = configuration(element(name("outputDirectory"), Constants.DIR_DELIMETER));
        executeMojo(pluginDependency, goal("copy-resources"), configuration, _pluginEnv);
        getLog().info("copy-resources");
    }

    /**
     * очистка
     *
     * @throws MojoExecutionException - исключение maven
     */
    private void clean() throws MojoExecutionException {
        Plugin pluginDependency = plugin("org.apache.maven.plugins", "maven-clean-plugin", "3.0.0");
        Xpp3Dom configuration = new Xpp3Dom("configuration");

        Xpp3Dom include = new Xpp3Dom("include");
        include.setValue("**/*");
        Xpp3Dom includes = new Xpp3Dom("includes");
        includes.addChild(include);
        Xpp3Dom fileset1 = new Xpp3Dom("fileset");
        Xpp3Dom directory1 = new Xpp3Dom("directory");
        directory1.setValue("src/main");
        fileset1.addChild(directory1);
        fileset1.addChild(includes);

        Xpp3Dom arguments = new Xpp3Dom("filesets");
        arguments.addChild(fileset1);

        configuration.addChild(arguments);
        executeMojo(pluginDependency, goal("clean"), configuration, _pluginEnv);
        getLog().info("clean");
    }

    /**
     * компиляция
     *
     * @throws MojoExecutionException - исключение maven
     */
    private void compile() throws MojoExecutionException {
        Plugin pluginDependency = plugin("org.apache.maven.plugins", "maven-compiler-plugin", "3.3");
        final Xpp3Dom configuration = configuration();
        executeMojo(pluginDependency, goal("compile"), configuration, _pluginEnv);
        getLog().info("compile");
    }

    /**
     * архивирование
     *
     * @throws MojoExecutionException - исключение maven
     */
    private void toJar() throws MojoExecutionException {
        Plugin pluginDependency = plugin("org.apache.maven.plugins", "maven-jar-plugin", "3.0.2");
        Xpp3Dom configuration = new Xpp3Dom("configuration");
//        Xpp3Dom include  = new Xpp3Dom("include");
//        include.setValue("/src/main/resources/*");
//        Xpp3Dom arguments = new Xpp3Dom("includes");
//        arguments.addChild(include);
//        configuration.addChild(arguments);
        executeMojo(pluginDependency, goal("jar"), configuration, _pluginEnv);
        getLog().info("to JAR");
    }

}
