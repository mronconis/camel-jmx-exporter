# Camel JMX Exporter
Camel JMX exporter is a simple tool for export data from Camel MBean.

## Usage
```
java -jar target/camel-jmx-exporter.jar \
    <hostname> \
    <port> \
    <user> \
    <password> \
    <command>
```

## Run
```
java -jar target/camel-jmx-exporter.jar \
    localhost \
    9090 \
    foo \
    bar \
    dumpRoutesAsXml
```

## Enabling JMX on projects based with Camel and Springboot


## Enabling JMX on projects based with Camel and Quarkus
Dependencies
```
<dependency>
    <groupId>org.apache.camel.quarkus</groupId>
    <artifactId>camel-quarkus-management</artifactId>
</dependency>
```

JMX credentials (file: jmxremote.password)
```
monitorRole foo
controlRole bar
```

System properties
```
-Dcom.sun.management.jmxremote=true \
-Dcom.sun.management.jmxremote.port=9090 \
-Dorg.apache.camel.jmx=true \
-Dcom.sun.management.jmxremote.password.file=jmxremote.password \
-Dcom.sun.management.jmxremote.ssl=false \
```
