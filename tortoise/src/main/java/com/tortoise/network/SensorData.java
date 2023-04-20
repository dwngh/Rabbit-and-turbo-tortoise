package com.tortoise.network;

public class SensorData {
    private float value;
    private byte mode;

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public byte getMode() {
        return mode;
    }

    public void setMode(byte mode) {
        this.mode = mode;
    }

    public SensorData() {}

    public SensorData(float _value, byte _mode) {
        this.value = _value;
        this.mode = _mode;
    }
}
