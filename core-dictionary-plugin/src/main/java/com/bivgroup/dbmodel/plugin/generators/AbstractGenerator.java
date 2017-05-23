package com.bivgroup.dbmodel.plugin.generators;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.runtime.parser.ParseException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

/**
 * Created by bush on 10.11.2016.
 * Абстрактынй генератор - инициализация Velocity, генерация
 */
public abstract class AbstractGenerator {
    
    /**
     * генератор объекта -  строки 
     * 
     * @param params - параметры
     * @param template - шаблон
     * @return - строка конфигурации
     * @throws ParseException - ошибка разбора
     * @throws IOException - тсключение 
     */
    protected String generateObj(Map<String, Object> params, String template) throws ParseException, IOException {
        VelocityEngine ve = new VelocityEngine();
        Properties p = new Properties();
        p.setProperty("resource.loader", "class");
        p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        ve.init(p);
        Map<String, Object> internalParameters = params;
        VelocityContext context = new VelocityContext(internalParameters);
        StringWriter out = new StringWriter();
        ve.resourceExists(template);
        ve.mergeTemplate(template, RuntimeSingleton.getString("input.encoding", "utf-8"), context, out);
        return out.toString();
    }

}
