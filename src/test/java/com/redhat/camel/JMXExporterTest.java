package com.redhat.camel;

import org.junit.Test;

public class JMXExporterTest {

    private String HOSTNAME = "localhost";
    private int PORT = 9090;
    private String[] CREDENTIALS = {"controlRole", "foo"};

    @Test
    public void stat() {
        try {
            String command = "stat";

            String output = new App().run(HOSTNAME, PORT, CREDENTIALS, command);

            assert output != null;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void dumpRoutesAsXml() {
        try {
            String command = "dumpRoutesAsXml";

            String output = new App().run(HOSTNAME, PORT, CREDENTIALS, command);

            assert output != null;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
