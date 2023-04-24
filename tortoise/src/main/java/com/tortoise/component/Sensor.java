package com.tortoise.component;

import java.io.IOException;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.DeliverCallback;
import com.tortoise.Tortoise;
// import com.tortoise.network.ControlDataPacket;
// import com.tortoise.network.SensorDataPacket;
import com.tortoise.util.FlashMemory;
import com.tortoise.util.Simulator;

public class Sensor extends Node {
    protected Simulator gen;
    protected boolean compression;
    protected FlashMemory mem;
    private float value;

    public static byte NO_COMPRESSION = 0;
    public static byte ENA_COMPRESSION = 1;


    CancelCallback cancelCallback = consumerTag -> {
    };

    public Sensor() {
        super();
        this.gen = new Simulator(id * 17, 9, 3);
        this.conn.initOutChannel(Tortoise.SENSOR_EXCHANGE);
        this.compression = true;
        this.mem = new FlashMemory();
    }

    public void disableCompression() {
        compression = false;
    }

    public void run() {
        try {
            float tmp;
            while (true) {
                tmp = gen.calculateNextValue();
                if (compression) {
                    tmp = mem.push(tmp);
                    if (tmp != 0) {
                        publish(tmp);
                    }
                } else
                    publish(tmp);
                Thread.sleep(Tortoise.TIME_WINDOW);
            }
        } catch (Exception e) {

        }
    }

    protected void publish(float _value) {
        this.value = _value;
        try {
            this.conn.publish(Tortoise.SENSOR_EXCHANGE, null);
        } catch (IOException e) {
            System.err.println("IO Error when publishing");
        }
    }

    public boolean isCompression() {
        return compression;
    }

    public float getValue() {
        return value;
    }
}
