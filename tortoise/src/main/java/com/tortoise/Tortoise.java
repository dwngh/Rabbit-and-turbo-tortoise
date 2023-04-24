package com.tortoise;

import com.tortoise.component.Node;
import com.tortoise.component.Sensor;
import com.tortoise.component.Monitor;
import com.tortoise.ui.MonitorDashboard;
import com.tortoise.ui.SensorManager;

public class Tortoise {
    public final static String HOST = "localhost";
    public final static int PORT = 5672;
    public final static String USERNAME = "guest";
    public final static String PASSWORD = "guest";
    public final static String CONTROL_EXCHANGE = "tortoise.control";
    public final static String SENSOR_EXCHANGE = "tortoise.sensor";
    public final static int TIME_WINDOW = 300;
 
    public static void main(String[] argv) throws Exception {
        MonitorDashboard md = new MonitorDashboard();
        SensorManager sm = new SensorManager();
        Node s = new Sensor();
        s.start();
        sm.bind((Sensor) s);
        Monitor s3 = new Monitor();
        s3.start();
        s3.setMonitorDashboard(md);
    }
 
}