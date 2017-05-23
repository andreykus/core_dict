package com.bivgroup.common.utils;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * Created by bush on 07.10.2016.
 * утилита - методы для генератора
 */
public class ToolCore {

    /**
     * название модуля
     *
     * @param clazz -  класс
     * @return - название модуля
     */
    public String getModule(Class clazz) {
        Manifest manifest = getManifestInfo(clazz);
        if (manifest != null && manifest.getMainAttributes() != null) {
            Attributes attr = manifest.getMainAttributes();
            return attr.getValue("Implementation-Title")!=null?attr.getValue("Implementation-Title"):"unknow";
        }
        return "unknow";
    }

    /**
     * получить версию
     * @param clazz - класс
     * @return - версия
     */
    public String getVersion(Class clazz) {
        Manifest manifest = getManifestInfo(clazz);
        if (manifest != null && manifest.getMainAttributes() != null) {
            Attributes attr = manifest.getMainAttributes();
            return attr.getValue("Implementation-Version")!=null?attr.getValue("Implementation-Version"):"unknow";
        }
        return "unknow";
    }

    /**
     * получить манифест библиотеки
     * @param clazz -  класс
     * @return - манифест
     */
    public Manifest getManifestInfo(Class clazz) {
        Enumeration resEnum;
        try {
            resEnum = clazz.getClassLoader().getResources(JarFile.MANIFEST_NAME);
            while (resEnum.hasMoreElements()) {
                try {
                    URL url = (URL) resEnum.nextElement();
                    InputStream is = url.openStream();
                    if (is != null) {
                        Manifest manifest = new Manifest(is);
                        return manifest;
                    }
                } catch (Exception e) {
                }
            }
        } catch (IOException e1) {
        }
        return null;
    }

    /**
     * Найти классы пакета в директории
     * @param directory -  директория
     * @param packageName - название пакета
     * @return -  список классов
     * @throws ClassNotFoundException -  исключение, не найден класс
     */
    private List<Class> findClasses(File directory, String packageName)
            throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file,
                        packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                Class clazz = Class.forName(packageName
                        + '.'
                        + file.getName().substring(0,
                        file.getName().length() - 6));
                classes.add(clazz);
            }
        }
        return classes;
    }

    /**
     * все класс на пакете
     * @param packageName -  название пакета
     * @return - список классов
     */
    public List<Class> getAllClassOnPackage(String packageName) {
        List<Class> classes = new ArrayList<Class>();
        try {
            ClassLoader classLoader = Thread.currentThread()
                    .getContextClassLoader();
            assert classLoader != null;
            String path = packageName.replace('.', '/');
            Enumeration<URL> resources = classLoader.getResources(path);
            List<File> dirs = new ArrayList<File>();
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                dirs.add(new File(resource.getFile()));
            }
            for (File directory : dirs) {
                classes.addAll(findClasses(directory, packageName));
            }
        } catch (Exception ex) {
            //TODO message
        }
        return classes;
    }

    /**
     * конвертировать поток в строку
     * @param stream - поток
     * @return - строка
     * @throws IOException - исключение
     */
    public String convertStreamToString(InputStream stream) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(stream);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int rez = bis.read();
        while(rez !=-1){
            baos.write((byte)rez);
            rez = bis.read();
        }
        return baos.toString();

    }
}
