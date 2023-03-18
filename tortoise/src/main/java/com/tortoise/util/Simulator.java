package com.tortoise.util;

import java.util.Random;

public class Simulator {
    private final Random _random;
    private final float _mean;
    private final float _standardDeviation;
    private final float _stepSizeFactor;
    // _value is of type double to reduce necessity of casting to float
    private float _value;

    public Simulator(int seed, float mean, float standardDeviation)
    {
        _random = new Random(seed);
        _mean = mean;
        _standardDeviation = Math.abs(standardDeviation);
        // we define a _stepSizeFactor that is used when calculating the 
        // next value
        _stepSizeFactor = _standardDeviation / 10;
        // we set a starting _value which is not exactly _mean (it could be 
        // but my personal preference is to not have each data set start on 
        // the same value)
        _value = _mean;
    }

    private static final byte[] factors = {-1, 1};

    public float calculateNextValue() {
        // first calculate how much the value will be changed
        double valueChange = _random.nextDouble() * _stepSizeFactor;
        // second decide if the value is increased or decreased
        int factor = factors[decideFactor()];

        // apply valueChange and factor to _value and return
        _value += valueChange * factor;
        return _value;
    }

    private int decideFactor() {
        // the distance from the _mean
        double distance;  
        int continueDirection;
        int changeDirection;

        // depending on if the current value is smaller or bigger than the mean
        // the direction changes are flipped: 0 means a factor of -1 is applied
        // 1 means a factor of 1 is applied
        if (_value > _mean) {
            distance = _value - _mean;
            continueDirection = 1;
            changeDirection = 0;
        } else {
            distance = _mean - _value;
            continueDirection = 0;
            changeDirection = 1;
        }

        // the chance is calculated by taking half of the _standardDeviation
        // and subtracting the distance divided by 50. This is done because 
        // chance with a distance of zero would mean a 50/50 chance for the
        // randomValue to be higher or lower.
        // The division by 50 was found by empiric testing different values
        double chance = (_standardDeviation / 2.2) - (distance / 50);
        double randomValue = _random.nextDouble() * _standardDeviation;

        // if the random value is smaller than the chance we continue in the
        // current direction if not we change the direction.
        return randomValue < chance ? continueDirection : changeDirection;
    }
}
