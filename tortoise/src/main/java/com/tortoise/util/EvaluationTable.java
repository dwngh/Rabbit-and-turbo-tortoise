package com.tortoise.util;

import java.util.HashMap;
import java.util.Map;

public class EvaluationTable {
    public class SensorData {
        public int id;
        public float value;
        public long timestamp;
        public boolean isResolved;
        public long delay;
        public SensorData() {}
        public SensorData(int _id, float _value, long _timestamp) {
            this.id = _id;
            this.value = _value;
            this.timestamp = _timestamp;
            this.isResolved = false;
            this.delay = -1;
        }
    }

    private HashMap<Integer, SensorData> sensorDataHashMap;

    public EvaluationTable() {
        sensorDataHashMap = new HashMap<>();
    }

    public void updateValue(int sid, float sensorValue, long sensorTimestamp) {
//        System.out.println("Updating value: " + Float.toString(sensorValue));
        if (sensorDataHashMap.containsKey(sid)) {
            SensorData sd = sensorDataHashMap.get(sid);
            if (!sd.isResolved) {
                System.out.println("Network problem detected");
            } else {
                sd.isResolved = false;
                sd.value = sensorValue;
                sd.timestamp = sensorTimestamp;
                sd.delay = -1;
            }
        } else {
            sensorDataHashMap.put(sid, new SensorData(sid, sensorValue, sensorTimestamp));
            System.out.println("Added node-" + Integer.toString(sid) + " to the hashmap");
        }
    }

    public void calculateValue(int sid, float sensorValue, long monitorTimestamp, int packetSize) {
//        System.out.println("Calculate value: " + Float.toString(sensorValue));
        if (sensorDataHashMap.containsKey(sid)) {
            SensorData sd = sensorDataHashMap.get(sid);
            if (sd.isResolved) {
                System.out.println("Internal error detected");
            } else {
                sd.isResolved = true;
                if (sensorValue != sd.value) {
                    System.out.println("Packet loss detected");
                } else {
                    sd.delay = monitorTimestamp - sd.timestamp;
//                    System.out.println("Delay of node-" + Integer.toString(sd.id) + " is " + Long.toString(sd.delay));
                }
            }
        }
    }

    public float getAverageDelay() {
        float avg = 0;
        int count = 0;
        for (Map.Entry<Integer, SensorData> set : sensorDataHashMap.entrySet()) {
            if (set.getValue().delay >= 0) {
                avg += (float) set.getValue().delay;
                count++;
            }
        }
        if (count == 0) return 0;
        return avg / count;
    }
}
