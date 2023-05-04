package com.tortoise.component;

import com.tortoise.util.Aggregator;

import java.util.ArrayList;
import java.util.List;

public class SensorCluster {
    private List<Sensor> sensors;
    private int turnLeft;
    private Aggregator aggregator;
    private boolean isOccurringSession;

    public SensorCluster(Aggregator _aggregator) {
        this.sensors = new ArrayList<>();
        this.aggregator = _aggregator;
        this.isOccurringSession = false;
    }

    public void initSession(int sensorQuantity) {
        turnLeft = sensorQuantity;
        initSensors(sensorQuantity);
        isOccurringSession = true;
    }

    public void beginSession() {
        startSensors();
    }

    public void doSendData(int size) {
        // System.out.println(Integer.toString(id) + " sent " + Integer.toString(size));
        this.aggregator.aggregateSentData(size);
    }

    public synchronized void popOut() {
        turnLeft--;
        if (turnLeft <= 0) {
            this.aggregator.endSession();
        }
    }

    private void initSensors(int quantity) {
        sensors.clear();
        for (int i = 0; i < quantity; i++) {
            sensors.add(new Sensor(this));
            System.out.println(i);
        }
        System.out.println("SC initialized " + Integer.toString(quantity) + " sensors...");
    }

    private void startSensors() {
        for (Sensor s : sensors) {
            s.start();
        }
        System.out.println("SC started all sensors...");
    }

    private void terminateSensors() {
        for (Sensor s : sensors) {
            s.terminate();
        }
        System.out.println("SC terminated all sensors...");
    }
}
