package com.tortoise.component;

import java.io.IOException;
import java.util.HashMap;

import com.rabbitmq.client.*;
import com.tortoise.Tortoise;
import com.tortoise.network.ControlDataPacket;
import com.tortoise.network.SensorData;
import com.tortoise.network.SensorDataPacket;
import com.tortoise.ui.MonitorDashboard;
import com.tortoise.util.Aggregator;


public class Monitor extends Node {
    private HashMap<Integer, SensorData> sensorData = new HashMap<Integer, SensorData>();
    private MonitorDashboard md = null;
    private Aggregator aggregator;

    public void setMonitorDashboard(MonitorDashboard md) {
        this.md = md;
        md.setMonitor(this);
    }

    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
        SensorDataPacket p = new SensorDataPacket();
        if (aggregator != null && delivery.getBody().length > p.getSize()) {
            System.out.println("Agg");
            aggregator.aggregateReceivedData(delivery.getBody().length);
        } else {
            p.decode(delivery.getBody());
            sensorData.put(p.getId(), p.getSensorData());
            System.out.println("Sensor");
            // p.print();
        }
    };

    CancelCallback cancelCallback = consumerTag -> { };

    public Monitor() {
        super();
        this.conn.initOutChannel(Tortoise.CONTROL_EXCHANGE);
    }

    @Override
    public void run() {
        this.conn.initInChannel(Tortoise.SENSOR_EXCHANGE, "monitor", this.deliverCallback, cancelCallback);
        //// Just for testing
        try {
            while (isAlive) {
                if (md != null) {
                    md.process(sensorData);
                }
                Thread.sleep(Tortoise.TIME_WINDOW);
            }
        } catch (Exception e) {

        }
        System.out.println("Monitor-" + Integer.toString(id) + " has terminated");
    }

    protected void publish(int id, byte _value) {
        ControlDataPacket p = new ControlDataPacket(id, _value);
        try {
            this.conn.publish(Tortoise.CONTROL_EXCHANGE, p.encode());
        } catch (IOException e) {
            System.err.println("IO Error when publishing");
        }
    }

    public void changeMode(int id, byte mode) {
        this.publish(id, mode);
    }

    public void bindToEvaluator(Aggregator ag) {
        this.aggregator = ag;
    }
    
}
