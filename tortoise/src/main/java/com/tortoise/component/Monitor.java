package com.tortoise.component;

import java.io.IOException;
import java.util.HashMap;

import com.rabbitmq.client.*;
import com.tortoise.Tortoise;
import com.tortoise.network.ControlDataPacket;
import com.tortoise.network.SensorData;
import com.tortoise.network.SensorDataPacket;


public class Monitor extends Node {
    private HashMap<Integer, SensorData> sensorData = new HashMap<Integer, SensorData>();

    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
        SensorDataPacket p = new SensorDataPacket();
        p.decode(delivery.getBody());
        sensorData.put(p.getId(), p.getSensorData());
        p.print();
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
            while (true) {
                Thread.sleep(Tortoise.TIME_WINDOW);
            }
        } catch (Exception e) {

        }

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
    
}
