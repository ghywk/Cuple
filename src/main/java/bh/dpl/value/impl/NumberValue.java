package bh.dpl.value.impl;

import bh.dpl.value.Value;

/**
 * @author KevinLeft
 */

public class NumberValue extends Value<Double> {
    double max;
    double min;

    public NumberValue(final String name, final double val, final double min, final double max ){
        super(name);
        this.setValue(val);
        this.max = max;
        this.min = min;
    }

    public NumberValue(final String name, final double val, final double min, final double max, final Dependency dependenc) {
        super(name, dependenc);
        this.setValue(val);
        this.max = max;
        this.min = min;
    }

    public Double getMax() {
        return this.max;
    }

    public Double getMin() {
        return this.min;
    }

    @Override
    public Double getConfigValue() {
        return this.getValue();
    }
}
