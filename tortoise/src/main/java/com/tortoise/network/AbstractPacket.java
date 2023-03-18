package com.tortoise.network;

public abstract class AbstractPacket {
    public abstract void decode(byte[] data);
    public abstract byte[] encode();
}
