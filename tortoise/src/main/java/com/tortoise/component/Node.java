package com.tortoise.component;

import com.tortoise.network.AMQP_Connection;

public abstract class Node implements Runnable{
    protected static int COUNTER = 0;
    protected int id;
    protected AMQP_Connection conn;
    protected Thread t;
    protected String name;

    public Node() {
        this.id = COUNTER++;
        this.name = "Node-" + Integer.toString(this.id);
        this.conn = new AMQP_Connection();
    }

    public int getId() {
        return id;
    }

    public abstract void run();

    public void start() {
        if (t == null) {
            t = new Thread(this, name);
            t.start();
        }
    }
}
