package com.redhat.camel;

public class App {

    public static void main(String[] args) {
        try {
            String hostname = args[0].trim();
            int port = Integer.parseInt(args[1].trim());
            String[] credentials = {args[2].trim(), args[3].trim()};
            String command = args[4].trim();

            String output = new App().run(hostname, port, credentials, command);

            System.out.println(output);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected String run(String hostname, int port, String[] credentials, String command) throws Exception {
        JMXExporter exporter = new JMXExporter(hostname, port, credentials);

        return (String) JMXExporter.class.getMethod(command).invoke(exporter);
    }
}
