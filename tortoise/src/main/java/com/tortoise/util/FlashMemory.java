package com.tortoise.util;

public class FlashMemory {
    protected float[] mem = {0F, 0F, 0F, 0F};
    protected short index;

    public FlashMemory() {
        index = 0;
    }

    public float push(float value) {
        mem[index++] = value;
        if (index >= 4) {
            index = 0;
            return (mem[0] + mem[1] + mem[2] + mem[3]) / 4F;
        }
        return 0;
    }
}
