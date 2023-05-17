package com.tortoise;

import com.tortoise.component.*;
import com.tortoise.ui.EvaluationDashboard;
import com.tortoise.ui.MonitorDashboard;
import com.tortoise.ui.SensorManager;

public class Tortoise {
    public final static String HOST = "localhost";
    public final static int PORT = 5672;
    public final static String USERNAME = "guest";
    public final static String PASSWORD = "guest";
    public final static String CONTROL_EXCHANGE = "tortoise.control";
    public final static String SENSOR_EXCHANGE = "tortoise.sensor";
    public final static int TIME_WINDOW = 500;
 
    public static void main(String[] argv) throws Exception {
        MonitorDashboard md = new MonitorDashboard();
        SensorManager sm = new SensorManager();
        EvaluationDashboard ed = new EvaluationDashboard();
        System.out.println("Init");
        NetworkEvaluator ne = new NetworkEvaluator();
        ne.bind(ed);
        Node s = new Sensor();
        Sensor s1 = new Sensor(ne); // Indicate that this node is for evaluating network
        // ((Sensor) s).disableCompression();
        s1.start();
        s.start();
        sm.bind((Sensor) s);

        Monitor s3 = new Monitor();
        s3.bindToNetEvaluator(ne);
        s3.start();
        s3.setMonitorDashboard(md);
    }
 
}