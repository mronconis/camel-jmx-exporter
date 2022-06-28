package com.redhat.camel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.JMX;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.camel.api.management.mbean.ManagedCamelContextMBean;

public class JMXExporter {
    
    private MBeanServerConnection server;

    public JMXExporter(String host, int port, String[] credentials) throws IOException {
        String connectionUri = String.format("service:jmx:rmi:///jndi/rmi://%s:%d/jmxrmi", host, port);
        JMXServiceURL url = new JMXServiceURL(connectionUri);
        Map<String, Object> env = new HashMap<>();
        env.put(JMXConnector.CREDENTIALS, credentials);
        JMXConnector jmxc = JMXConnectorFactory.connect(url, env);
        server = jmxc.getMBeanServerConnection();
    }

    public String stat() throws Exception {
        StringBuilder output = new StringBuilder();

        for (String camelId : findCamelIds()) {
            output.append(String.format("CamelId: %s", camelId));
        }
        output.append("\n");

        ObjectName on = new ObjectName("org.apache.camel:type=routes,*");

        for (ObjectName result : server.queryNames(on, null)) {
            String keyProps = result.getCanonicalKeyPropertyListString();
            ObjectName objectInfoName = new ObjectName("org.apache.camel:" + keyProps);
            String routeId = (String) server.getAttribute(objectInfoName, "RouteId");
            String description = (String) server.getAttribute(objectInfoName, "Description");
            String state = (String) server.getAttribute(objectInfoName, "State");
            output.append(String.format("RouteId: %s, desc: %s, state: %s", routeId, description, state));
            output.append("\n");
        }

        return output.toString();
    }

    public String dumpRoutesAsXml() throws Exception  {
        StringBuilder output = new StringBuilder();

        String camelId = findCamelIds().get(0);

        if (camelId == null || camelId.isEmpty())
            return "";

        ObjectName on = new ObjectName("org.apache.camel:context="+camelId+",type=context,name=\""+camelId+"\"");
        ManagedCamelContextMBean proxy = JMX.newMBeanProxy(server, on, ManagedCamelContextMBean.class);
        String xml = proxy.dumpRoutesAsXml();

        output.append(enrichRoutesXml(xml, camelId));

        return output.toString();
    }

    private List<String> findCamelIds() throws MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException {
        List<String> camelIds = new ArrayList<>();

        ObjectName on = new ObjectName("org.apache.camel:type=context,*");

        for (ObjectName result : server.queryNames(on, null)) {
            String keyProps = result.getCanonicalKeyPropertyListString();
            ObjectName objectInfoName = new ObjectName("org.apache.camel:" + keyProps);
            camelIds.add((String) server.getAttribute(objectInfoName, "CamelId"));
        }

        return camelIds;
    }

    private String enrichRoutesXml(String xml, String routeId) {
        return xml
            .replaceAll("<routes xmlns=\"http://camel.apache.org/schema/spring\">", 
                "<routes xmlns=\"http://camel.apache.org/schema/spring\" id=\""+routeId+"\">");
    }
}