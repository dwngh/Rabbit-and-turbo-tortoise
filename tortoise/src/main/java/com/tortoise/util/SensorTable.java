package com.tortoise.util;

import com.tortoise.Tortoise;
import com.tortoise.component.Sensor;
import com.tortoise.ui.SensorManager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SensorTable implements Runnable{
    private HashMap<Integer, Sensor> sensors;
    private SensorManager sm;
    protected Thread t;
    private boolean runFlag;

    public static String generateSameIndentString(String header, String value, int desire_length) {
        int numberOfSpace = desire_length - header.length() - value.length();
        if (numberOfSpace < 2) numberOfSpace = 2;
        String space = new String(new char[numberOfSpace - 1]).replace('\0', ' ');
        return (header + " " + value + space);
    }

    private String generateSensorInfo() {
        String tmp = "";
        Iterator<Map.Entry<Integer, Sensor>> it = sensors.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Sensor> entry = it.next();
            Sensor s = entry.getValue();
            tmp += generateSameIndentString("ID:", Integer.toString(s.getId()), 12);
            tmp += generateSameIndentString("Value:", Float.toString(s.getValue()), 20);
            tmp += generateSameIndentString("Mode:", s.isCompression() ? "COMPRESS" : "NO_COMPRESS", 20);
            tmp += "\n";
        }
        return tmp;
    }

    public SensorTable(SensorManager sm) {
        sensors = new HashMap<>();
        runFlag = true;
        this.sm = sm;
    }

    public void bind(Sensor s) {
        sensors.put(s.getId(), s);
    }

    public void unbind(Sensor s) {
        sensors.remove(s.getId());
    }

    public void unbind(int id) {
        sensors.remove(id);
    }

    public void terminate() {
        this.runFlag = false;
    }

    @Override
    public void run() {
        if (sm != null)
            while (runFlag) {
                sm.update(generateSensorInfo());
                try {
                    Thread.sleep(Tortoise.TIME_WINDOW / 2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
    }

    public void start() {
        if (t == null) {
            t = new Thread(this, "sensor-table");
            t.start();
        }
    }
}
