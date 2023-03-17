package com.tortoise.component;

import java.io.IOException;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.DeliverCallback;
import com.tortoise.Tortoise;
import com.tortoise.network.ControlDataPacket;
import com.tortoise.network.SensorDataPacket;
import com.tortoise.util.FlashMemory;
import com.tortoise.util.Simulator;

public class Sensor extends Node {
    protected Simulator gen;
    protected boolean compression;
    protected FlashMemory mem;

    protected DeliverCallback deliverCallback = (consumerTag, delivery) -> {
        System.out.print(this.id);
        ControlDataPacket p = new ControlDataPacket();
        p.decode(delivery.getBody());
        p.print();
        if (p.getId() == this.id) {
            this.compression = p.getValue();
            System.out.println("Set compression of " + Integer.toString(id) + ": " + String.valueOf(p.getValue()));
        }
    };

    CancelCallback cancelCallback = consumerTag -> {
    };

    public Sensor(String name) {
        super(name);
        this.gen = new Simulator(id * 17, 20, 5);
        this.conn.initOutChannel(Tortoise.SENSOR_EXCHANGE);
        this.compression = true;
        this.mem = new FlashMemory();
    }

    public void disableCompression() {
        compression = false;
    }

    public void run() {
        try {
            this.conn.initInChannel(Tortoise.CONTROL_EXCHANGE, "consume-sensor-" + Integer.toString(id),
                    this.deliverCallback, cancelCallback);
            float tmp;
            while (true) {
                tmp = gen.CalculateNextValue();
                if (compression) {
                    tmp = mem.push(tmp);
                    if (tmp != 0) {
                        publish(tmp);
                    }
                } else
                    publish(tmp);
                Thread.sleep(300);
            }
        } catch (Exception e) {

        }
    }

    protected void publish(float _value) {
        SensorDataPacket p = new SensorDataPacket(this.id, _value);
        // System.out.println("Value of " + name + ": " + Float.toString(tmp));
        try {
            this.conn.publish(Tortoise.SENSOR_EXCHANGE, p.encode());
        } catch (IOException e) {
            System.err.println("IO Error when publishing");
        }
    }
}
