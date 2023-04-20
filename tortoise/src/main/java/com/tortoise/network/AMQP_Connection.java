package com.tortoise.network;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.*;
import com.tortoise.Tortoise;

public class AMQP_Connection {
    private Connection connection;
    private Channel publish;
    private Channel consume;
    private String consumerTag;

    public AMQP_Connection() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Tortoise.HOST);
        factory.setPort(Tortoise.PORT);
        factory.setUsername(Tortoise.USERNAME);
        factory.setPassword(Tortoise.PASSWORD);
        try {
            this.connection = factory.newConnection();
        } catch (IOException e) {
            System.err.println(" IO Exception");
        } catch (TimeoutException e) {
            System.err.println("Timeout Exception");
        }
    }


    public void initOutChannel(String exchange) {
        try {
            this.publish = connection.createChannel();
            this.publish.exchangeDeclare(exchange, "direct");
        } catch (IOException e) {

        }
    }

    public String getConsumerTag() {
        return consumerTag;
    }

    public void initInChannel(String consume_queue, DeliverCallback deliverCallback, CancelCallback cancelCallback) {
        try {
            this.consume = connection.createChannel();
            this.consume.queueDeclare(consume_queue, false, false, false, null);
            this.consumerTag = this.consume.basicConsume(consume_queue, true, deliverCallback, cancelCallback);
        } catch (IOException e) {

        }
    }

    public void initInChannel(String exchange, String consume_queue, DeliverCallback deliverCallback, CancelCallback cancelCallback) {
        try {
            this.consume = connection.createChannel();
            this.consume.queueDeclare(consume_queue, false, false, false, null);
            this.consume.queueBind(consume_queue, exchange, "info");
            this.consumerTag = this.consume.basicConsume(consume_queue, true, deliverCallback, cancelCallback);
        } catch (IOException e) {

        }
    }

    public void publish(String exchange, byte[] data) throws IOException {
        if (this.publish != null)
            this.publish.basicPublish(exchange, "info", null, data);
    }
}
