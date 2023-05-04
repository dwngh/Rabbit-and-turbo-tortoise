package com.tortoise.component;

import com.tortoise.util.Aggregator;

public class CapabilityEvaluator {
    public static final int NUMBER_OF_WINDOWS = 3;

    private SensorCluster sensorCluster;
    private Monitor gateway;
    private Aggregator aggregator;

    public CapabilityEvaluator(Monitor monitor) {
        this.gateway = monitor;
        this.aggregator = new Aggregator();
        this.sensorCluster = new SensorCluster(this.aggregator);
        this.gateway.bindToEvaluator(this.aggregator);
    }

    public void initSession(int numberOfSensors) {
        System.out.println("CE initSession");
        this.sensorCluster.initSession(numberOfSensors);
    }

    public void beginSession() {
        System.out.println("CE beginSession");
        this.aggregator.beginSession();
        this.sensorCluster.beginSession();
    }
}
