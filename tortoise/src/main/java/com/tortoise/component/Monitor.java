package com.tortoise.component;

import java.io.IOException;

import com.rabbitmq.client.*;
import com.tortoise.Tortoise;
import com.tortoise.network.ControlDataPacket;
import com.tortoise.network.SensorDataPacket;


public class Monitor extends Node {

    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
        SensorDataPacket p = new SensorDataPacket();
        p.decode(delivery.getBody());
        p.print();
    };

    CancelCallback cancelCallback = consumerTag -> { };

    public Monitor(String name) {
        super(name);
        this.conn.initOutChannel(Tortoise.CONTROL_EXCHANGE);
    }

    @Override
    public void run() {
        this.conn.initInChannel(Tortoise.SENSOR_EXCHANGE, "monitor", this.deliverCallback, cancelCallback);
        //// Just for testing
        // int count = 0;
        // try {
        //     while (true) {
        //         count++;
        //         System.out.println(count);
        //         if (count == 15) {
        //             this.publish(1, (byte) 0);
        //         };
        //         if (count == 30) {
        //             this.publish(1, (byte) 1);
        //         }
        //         Thread.sleep(500);
        //     }
        // } catch (Exception e) {

        // }

    }

    protected void publish(int id, byte _value) {
        ControlDataPacket p = new ControlDataPacket(id, _value);
        try {
            this.conn.publish(Tortoise.CONTROL_EXCHANGE, p.encode());
        } catch (IOException e) {
            System.err.println("IO Error when publishing");
        }
    }
    
}
