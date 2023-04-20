package com.tortoise.network;


public class SensorDataPacket extends AbstractPacket{

    public float getValue() {
        return sensorData.getValue();
    }

    public SensorData getSensorData() {
        return sensorData;
    }

    public byte getMode() {
        return sensorData.getMode();
    }

    public int getId() {
        return id;
    }

    protected SensorData sensorData;
    protected int id;

    public SensorDataPacket() {
        this.sensorData = new SensorData();
    }

    public SensorDataPacket(int id, float value, byte mode) {
        this.id = id;
        this.sensorData = new SensorData(value, mode);
    }

    public void print() {
        System.out.print("Sensor-");
        System.out.print(id);
        System.out.print(": ");
        System.out.println(sensorData.getValue());
    }

    @Override
    public void decode(byte[] data) {
        int intBits = data[4] << 24 | (data[5] & 0xFF) << 16 | (data[6] & 0xFF) << 8 | (data[7] & 0xFF);
        float _v = Float.intBitsToFloat(intBits);
        this.id = (int) ((data[0] & 0xFF)  << 24 | (data[1] & 0xFF) << 16 | (data[2] & 0xFF) << 8 | (data[3] & 0xFF));
        byte _m = data[8];
        this.sensorData.setValue(_v);
        this.sensorData.setMode(_m);
    }

    @Override
    public byte[] encode() {
        System.out.println("Encoding");
        int intBits =  Float.floatToIntBits(sensorData.getValue());
        return new byte[] {
            (byte) ((id >> 24) & 0xff), (byte) ((id >> 16) & 0xff), (byte) ((id >> 8) & 0xff), (byte) ((id) & 0xff),
            (byte) ((intBits >> 24) & 0xff), (byte) ((intBits >> 16) & 0xff), (byte) ((intBits >> 8) & 0xff), (byte) ((intBits) & 0xff),
                sensorData.getMode(),
        };
    }
}
