package com.tortoise.util;

import com.tortoise.component.Monitor;

public class Aggregator {
    private int sentDataSize;
    private int receivedDataSize;
    private boolean isOccurringSession;

    public Aggregator() {}

    public void beginSession() {
        sentDataSize = 0;
        receivedDataSize = 0;
        isOccurringSession = true;
    }

    public void endSession() {
        if (isOccurringSession) {
            isOccurringSession = false;
            System.out.println("Sent data: " + Integer.toString(sentDataSize));
            System.out.println("Received data: " + Integer.toString(receivedDataSize));
        }
    }

    public synchronized void aggregateSentData(int size) {
        if (isOccurringSession) {
            sentDataSize += size;
        }
    }

    public void aggregateReceivedData(int size) {
        if (isOccurringSession) {
            receivedDataSize += size;
        }
    }

}
