package com.tortoise.component;

import java.io.IOException;
import java.sql.Timestamp;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.DeliverCallback;
import com.tortoise.Tortoise;
import com.tortoise.network.ControlDataPacket;
import com.tortoise.network.EvaluationDataPacket;
import com.tortoise.network.SensorDataPacket;
import com.tortoise.util.FlashMemory;
import com.tortoise.util.Simulator;

public class Sensor extends Node {
    protected Simulator gen;
    protected boolean compression;
    protected FlashMemory mem;
    private float value;

    private NetworkEvaluator networkEvaluator;
    public static byte NO_COMPRESSION = 0;
    public static byte ENA_COMPRESSION = 1;
    public static byte EVALUATION = 2;

    protected DeliverCallback deliverCallback = (consumerTag, delivery) -> {
        ControlDataPacket p = new ControlDataPacket();
        p.decode(delivery.getBody());
        if (p.getId() == this.id) {
            this.compression = p.getValue();
            System.out.println("Set compression of " + Integer.toString(id) + ": " + String.valueOf(p.getValue()));
        }
    };

    CancelCallback cancelCallback = consumerTag -> {
    };

    public Sensor() {
        super();
        this.gen = new Simulator(id * 17, 9, 3);
        this.conn.initOutChannel(Tortoise.SENSOR_EXCHANGE);
        this.compression = true;
        this.mem = new FlashMemory();
        this.isAlive = true;
    }

    public Sensor(NetworkEvaluator networkEvaluator) {
        super();
        if (networkEvaluator.attachSensor(id)) this.networkEvaluator = networkEvaluator;;
        this.gen = new Simulator(id * 17, 9, 3);
        this.conn.initOutChannel(Tortoise.SENSOR_EXCHANGE);
        this.compression = false;
        this.mem = new FlashMemory();
        this.isAlive = true;
    }

    public void disableCompression() {
        compression = false;
    }

    public void run() {
        try {
            this.conn.initInChannel(Tortoise.CONTROL_EXCHANGE, "consume-sensor-" + Integer.toString(id),
                    this.deliverCallback, cancelCallback);
            float tmp;
            while (this.isAlive) {
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
        System.out.println("Sensor-" + Integer.toString(id) + " has terminated");
    }

    protected void publish(float _value) {
        this.value = _value;
        try {
            byte mode = compression ? ENA_COMPRESSION : NO_COMPRESSION;
            if (networkEvaluator != null) {
                networkEvaluator.updateCurrentSensor(_value, System.currentTimeMillis());
                mode = EVALUATION;
            }
            SensorDataPacket p = new SensorDataPacket(this.id, _value, mode);
            this.conn.publish(Tortoise.SENSOR_EXCHANGE, p.encode());

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
