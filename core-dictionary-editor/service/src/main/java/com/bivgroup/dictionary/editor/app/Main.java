package com.bivgroup.dictionary.editor.app;

import com.bivgroup.dictionary.editor.restcontollers.RestApi;
import com.bivgroup.dictionary.editor.restcontollers.DictioanryApi;
import com.bivgroup.dictionary.editor.servlet.FileServlet;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.jaxrs.JAXRSArchive;
import org.wildfly.swarm.logging.LoggingFraction;
import org.wildfly.swarm.topology.TopologyArchive;
import org.wildfly.swarm.undertow.WARArchive;


/**
 * Created by bush on 17.01.2017.
 */
public class Main {
    public static void main(String... args) throws Exception {
        Swarm swarm = new Swarm();

        JAXRSArchive deploymentrs = ShrinkWrap.create(JAXRSArchive.class);
        deploymentrs.addClass(RestApi.class);
        deploymentrs.addClass(DictioanryApi.class);
        deploymentrs.as(TopologyArchive.class)
                .advertise("recommendations");
        deploymentrs.addAllDependencies();

        WARArchive deploymentw = ShrinkWrap.create(WARArchive.class);
        deploymentw.addClass(FileServlet.class);
        deploymentw.as(TopologyArchive.class)
                .advertise("recommendations");
        deploymentw.addAllDependencies();

        swarm
                .fraction(LoggingFraction.createDefaultLoggingFraction())
                .start();
        swarm.deploy(deploymentw).deploy(deploymentrs);
    }
}
