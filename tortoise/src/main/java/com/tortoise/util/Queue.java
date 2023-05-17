package com.tortoise.util;

import java.util.ArrayList;

public class Queue <T extends Number>{
    private ArrayList<T> value;
    private int length;

    public Queue(int _l) {
        value = new ArrayList<>();
        length = _l;
    }

    public void enqueue(T element) {
        if (value.size() < length) {
            value.add(0, element);
        } else {
            dequeue();
            enqueue(element);
        }
    }

    public T dequeue() {
        T _tmp = value.get(value.size() - 1);
        value.remove(value.size() - 1);
        return _tmp;
    }

    public int size() {
        return value.size();
    }

    public int sum() {
        int agg = 0;
        for (T element : value) {
            agg += element.intValue();
        }
        return agg;
    }
}
