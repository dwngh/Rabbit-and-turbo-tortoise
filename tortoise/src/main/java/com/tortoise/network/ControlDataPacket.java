package com.tortoise.network;

public class ControlDataPacket extends AbstractPacket{

    protected byte value;
    protected int id;

    public boolean getValue() {
        return (value == 1);
    }

    public int getId() {
        return id;
    }

    public ControlDataPacket() {}

    public ControlDataPacket(int id, byte value) {
        this.value = value;
        this.id = id;
    }

    public void print() {
        System.out.print("Control-");
        System.out.print(id);
        System.out.print(": ");
        System.out.println(value);
    }

    @Override
    public void decode(byte[] data) {
        this.value = data[4];
        this.id = (int) ((data[0] & 0xFF)  << 24 | (data[1] & 0xFF) << 16 | (data[2] & 0xFF) << 8 | (data[3] & 0xFF));
    }

    @Override
    public byte[] encode() {
        return new byte[] {
            (byte) ((id >> 24) & 0xff), (byte) ((id >> 16) & 0xff), (byte) ((id >> 8) & 0xff), (byte) ((id) & 0xff), value
        };
    }

    @Override
    public int getSize() {
        return 5;
    }

}
