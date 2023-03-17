package com.tortoise;

import com.tortoise.component.Node;
import com.tortoise.component.Sensor;
import com.tortoise.component.Monitor;

public class Tortoise {
    public final static String HOST = "localhost";
    public final static int PORT = 5672;
    public final static String USERNAME = "guest";
    public final static String PASSWORD = "guest";
    public final static String CONTROL_EXCHANGE = "tortoise.control";
    public final static String SENSOR_EXCHANGE = "tortoise.sensor";
 
    public static void main(String[] argv) throws Exception {
        Node s = new Sensor("sensor-0");
        // ((Sensor) s).disableCompression();
        s.start();
        Node s1 = new Sensor("sensor-1");
        s1.start();
        Node s3 = new Monitor("monitor-3");
        s3.start();
    }
 
}