package com.tortoise.network;


public class SensorDataPacket extends AbstractPacket{

    protected float value;
    protected int id;

    public SensorDataPacket() {}

    public SensorDataPacket(int id, float value) {
        this.id = id;
        this.value = value;
    }

    public void print() {
        System.out.print("Sensor-");
        System.out.print(id);
        System.out.print(": ");
        System.out.println(value);
    }

    @Override
    public void decode(byte[] data) {
        int intBits = data[4] << 24 | (data[5] & 0xFF) << 16 | (data[6] & 0xFF) << 8 | (data[7] & 0xFF);
        this.value = Float.intBitsToFloat(intBits);
        this.id = (int) ((data[0] & 0xFF)  << 24 | (data[1] & 0xFF) << 16 | (data[2] & 0xFF) << 8 | (data[3] & 0xFF));
    }

    @Override
    public byte[] encode() {
        int intBits =  Float.floatToIntBits(value);
        return new byte[] {
            (byte) ((id >> 24) & 0xff), (byte) ((id >> 16) & 0xff), (byte) ((id >> 8) & 0xff), (byte) ((id) & 0xff),
            (byte) ((intBits >> 24) & 0xff), (byte) ((intBits >> 16) & 0xff), (byte) ((intBits >> 8) & 0xff), (byte) ((intBits) & 0xff),
        };
    }
}
