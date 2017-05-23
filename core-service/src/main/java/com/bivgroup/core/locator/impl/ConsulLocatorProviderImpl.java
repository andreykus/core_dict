package com.bivgroup.core.locator.impl;

import com.bivgroup.core.locator.interfaces.LocatorProvider;
import com.bivgroup.core.locator.ServiceNotFoundException;
import com.google.common.net.HostAndPort;
import com.orbitz.consul.*;
import com.orbitz.consul.model.ConsulResponse;
import com.orbitz.consul.model.State;
import com.orbitz.consul.model.catalog.CatalogNode;
import com.orbitz.consul.model.catalog.CatalogService;
import com.orbitz.consul.model.event.Event;
import com.orbitz.consul.model.health.HealthCheck;
import com.orbitz.consul.model.health.Node;
import com.orbitz.consul.model.health.ServiceHealth;
import com.orbitz.consul.option.CatalogOptions;
import com.orbitz.consul.option.ImmutableCatalogOptions;
import com.orbitz.consul.option.QueryOptions;
import com.orbitz.consul.util.LeaderElectionUtil;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by bush on 16.08.2016.
 */
public class ConsulLocatorProviderImpl implements LocatorProvider {
    Consul consul;
    AgentClient agentClient;
    CatalogClient catalogClient;
    HealthClient healthClient;
    KeyValueClient kvClient;
    StatusClient statusClient;
    EventClient eventClient;

    String DEFAULT_CONSUL_ADDRESS = "10.16.100.115";
    Integer DEFAULT_CONSUL_PORT = 8500;
    Integer DEFAULT_TIMEOUT = 10000;
    Integer DEFAULT_READ_TIMEOUT = 3600000;
    Integer DEFAULT_WRITE_TIMEOUT = 900;
    String DEFAULT_SERVICE_PROTOCOL = "http";
    Integer DEFAULT_SERVICE_PORT = 8080;
    Long DEFAULT_SERVICE_HEATH_CHECK_TIME = 1000L;

    String node;
    String datacentr;

    public ConsulLocatorProviderImpl() {
        initConsul();
    }

    private void initConsul() {
        this.consul = Consul.builder()
                .withHostAndPort(HostAndPort.fromParts(DEFAULT_CONSUL_ADDRESS, DEFAULT_CONSUL_PORT))
                .withConnectTimeoutMillis(DEFAULT_TIMEOUT)
                .withReadTimeoutMillis(DEFAULT_READ_TIMEOUT)
                .withWriteTimeoutMillis(DEFAULT_WRITE_TIMEOUT)
                .build();

        this.agentClient = consul.agentClient();
        this.catalogClient = consul.catalogClient();
        this.kvClient = consul.keyValueClient();
        this.healthClient = consul.healthClient();
        this.statusClient = consul.statusClient();
        this.eventClient = consul.eventClient();
    }

    private Integer getLocalPort() {
        Object port = null;
        try {
            port = ManagementFactory.getPlatformMBeanServer().getAttribute(new ObjectName("jboss.as:socket-binding-group=standard-sockets,socket-binding=http"), "port");
        } catch (MalformedObjectNameException ex) {
            port = DEFAULT_SERVICE_PORT;
        } finally {
            if (port == null) {
                port = DEFAULT_SERVICE_PORT;
            }
            return (Integer) port;
        }
    }

    private String getLocalAdress() {
        Object address = null;
        try {
            address = ManagementFactory.getPlatformMBeanServer().getAttribute(new ObjectName("jboss.as:interface=public"), "inet-address");
        } catch (MalformedObjectNameException ex) {
            address = DEFAULT_CONSUL_ADDRESS;
        } finally {
            if (address == null) {
                address = DEFAULT_CONSUL_ADDRESS;
            }
            return address.toString();
        }
    }

    public void registerService(String serviceName, URL checkurl) throws MalformedURLException {
        UUID uid = UUID.nameUUIDFromBytes(serviceName.getBytes());
        agentClient.register(getLocalPort(), new URL(checkurl.getProtocol(), getLocalAdress(), getLocalPort(), checkurl.getFile()), DEFAULT_SERVICE_HEATH_CHECK_TIME, serviceName, uid.toString());
    }

    private List<String> getDc() {
        return catalogClient.getDatacenters();
    }

    private List<Node> getNodes(String node) {
        QueryOptions qo = QueryOptions.blockSeconds(2, new BigInteger(Integer.toString(Integer.MAX_VALUE))).build();
        CatalogOptions co = null;
        if (datacentr != null) {
            co = ImmutableCatalogOptions.builder().datacenter(datacentr).build();
        }
        List<Node> response = catalogClient.getNodes(co, qo).getResponse();
        return response;
    }

    private CatalogNode getNode(String node) {
        QueryOptions qo = QueryOptions.blockSeconds(2, new BigInteger(Integer.toString(Integer.MAX_VALUE))).build();
        CatalogOptions co = null;
        if (datacentr != null) {
            co = ImmutableCatalogOptions.builder().datacenter(datacentr).build();
        }
        CatalogNode response = catalogClient.getNode(node, co, qo).getResponse();
        return response;
    }

    private Map<String, List<String>> getServices(String service) {
        QueryOptions qo = QueryOptions.blockSeconds(2, new BigInteger(Integer.toString(Integer.MAX_VALUE))).build();
        CatalogOptions co = null;
        if (datacentr != null) {
            co = ImmutableCatalogOptions.builder().datacenter(datacentr).build();
        }
        Map<String, List<String>> response = catalogClient.getServices(co, qo).getResponse();
        return response;
    }

    private List<CatalogService> getService(String service) {
        QueryOptions qo = QueryOptions.blockSeconds(2, new BigInteger(Integer.toString(Integer.MAX_VALUE))).build();
        CatalogOptions co = null;
        if (datacentr != null) {
            co = ImmutableCatalogOptions.builder().datacenter(datacentr).build();
        }
        List<CatalogService> response = catalogClient.getService(service, co, qo).getResponse();
        return response;
    }

    public URL getUrlServiceByName(String service) throws Exception {
        List<CatalogService> list = getService(service);
        if (list.isEmpty()) throw new ServiceNotFoundException();
        CatalogService ser = list.get(0);
        int port = ser.getServicePort();
        String file = ser.getServiceAddress();
        String host = ser.getAddress();
        String protocol = DEFAULT_SERVICE_PROTOCOL;
        String name = new StringBuffer().append("/").append(ser.getServiceName()).append("/").append(ser.getServiceName()).toString();
        URL url = new URL(protocol, host, port, name);
        return url;
    }

    public void getServices1() {


        catalogClient.getNode(catalogClient.getNodes()
                .getResponse().iterator().next().getNode());
        final List<Node> nodesResp = catalogClient.getNodes().getResponse();
        for (Node node : nodesResp) {
            node.getTaggedAddresses();
            node.getTaggedAddresses().get().getWan();
            node.getTaggedAddresses().get().getWan().isEmpty();
        }


    }

    public void gett() throws Exception {
        // InetAddress inetAddress = InetAddress.getByName("name");
        //  String address = inetAddress.getHostAddress();


        //
        //
        //        List<ServiceHealth> nodes = healthClient.getHealthyServiceInstances("DataService").getResponse(); // discover only "passing" nodes
        //
        //
        //        String value = kvClient.getValueAsString("foo").get(); // bar
        //
        //
        //        for (String peer : statusClient.getPeers()) {
        //            System.out.println(peer); // 127.0.0.1:8300
        //        }
        //        System.out.println(statusClient.getLeader()); //


//        String serviceName = "my-service";
//        ServiceHealthCache svHealth = ServiceHealthCache.newCache(healthClient, serviceName);
//        boolean b = svHealth.addListener(new ConsulCache.Listener<HostAndPort, ServiceHealth>() {
//            @Override
//            public void notify(Map<HostAndPort, ServiceHealth> newValues) {
//                // do Something with updated server map
//            }
//        });
//        svHealth.start();


        LeaderElectionUtil leutil = new LeaderElectionUtil(consul);
        final String serviceName1 = "myservice100";
        final String serviceInfo = "serviceinfo";
        leutil.releaseLockForService(serviceName1);
        leutil.getLeaderInfoForService(serviceName1).isPresent();
        leutil.electNewLeaderForService(serviceName1, serviceInfo).get();
        leutil.releaseLockForService(serviceName1);


        Event fired = eventClient.fireEvent("1");
        for (Event event : eventClient.listEvents().getEvents()) {
            if (event.getName().equals("1") && event.getId().equals(fired.getId())) {

            }
        }


        List<String> peers = statusClient.getPeers();


        for (ServiceHealth health : healthClient.getAllServiceInstances("serviceName").getResponse()) {
            health.getService().getId();
            health.getChecks().size();
        }


        for (Map.Entry<String, HealthCheck> check : agentClient.getChecks().entrySet()) {
            check.getValue().getCheckId();
        }


        ConsulResponse<List<HealthCheck>> response1 = healthClient.getChecksByState(State.WARN);
        for (HealthCheck healthCheck : response1.getResponse()) {

        }


        ConsulResponse<List<HealthCheck>> response2 = healthClient.getServiceChecks("serviceName",
                ImmutableCatalogOptions.builder().datacenter("dc1").build(),
                QueryOptions.blockSeconds(20, new BigInteger("0")).build());


    }


}
