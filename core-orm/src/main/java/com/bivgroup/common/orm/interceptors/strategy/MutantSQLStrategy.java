package com.bivgroup.common.orm.interceptors.strategy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.InternalContextAdapterImpl;
import org.apache.velocity.runtime.RuntimeInstance;
import org.apache.velocity.runtime.parser.node.SimpleNode;
import org.hibernate.query.Query;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

/**
 * Created by andreykus on 25.08.2016.
 * Стратегия ппреобразования sql. Обработка chunk, chain как в Cayenne
 * {@link ProcessQueryStrategy}
 */
public class MutantSQLStrategy implements ProcessQueryStrategy {
    /**
     * логгер
     */
    private transient Logger logger = LogManager.getLogger(this.getClass());

    /**
     * модифицыруем sql
     * @param sql - sql
     * @param args - параметры
     * @return - sql
     */
    public String process(String sql, Object... args) {
        logger.debug(String.format("orm: sql before injection: %1s", sql));
        if (args.length > 1 && args[1] != null) {
            try {
                Map params = ((Map) args[1]);
                Query query = ((Query) args[0]);
                //Set<Parameter<?>> params = query.getParameters();
                RuntimeInstance velocityRuntime = new RuntimeInstance();
                velocityRuntime.addProperty("userdirective", ChainDirective.class.getName());
                velocityRuntime.addProperty("userdirective", ChunkDirective.class.getName());
                velocityRuntime.init();
                SimpleNode nodeTree = velocityRuntime.parse(new StringReader(sql), sql);
                Map<String, Object> internalParameters = params;
//            for (Parameter param : params) {
//                if (query.isBound(param)) {internalParameters.put(param.getName(), "");}
//            }
                VelocityContext context = new VelocityContext(internalParameters);
                InternalContextAdapterImpl ica = new InternalContextAdapterImpl(context);
                ica.pushCurrentTemplateName(sql);
                StringWriter out = new StringWriter(sql.length());
                try {
                    nodeTree.init(ica, velocityRuntime);
                    nodeTree.render(ica, out);
                    sql = out.toString();
                } finally {
                    ica.popCurrentTemplateName();
                }
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
        logger.debug(String.format("orm: sql after injection: %1s", sql));
        return sql;
    }
}
