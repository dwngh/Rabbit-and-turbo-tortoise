package com.tortoise.network;

public class EvaluationDataPacket extends AbstractPacket{
    @Override
    public void decode(byte[] data) {

    }

    @Override
    public byte[] encode() {
        return new byte[90];
    }

    @Override
    public int getSize() {
        return 90;
    }
}
