package bh.dpl.value.impl;

import bh.dpl.value.Value;

/**
 * @author KevinLeft
 */

public class BoolValue extends Value<Boolean> {
    public BoolValue(final String name, final Boolean value) {
        super(name);
        this.setValue(value);
    }

    public BoolValue(final String name, final Boolean value, final Dependency dependenc) {
        super(name, dependenc);
        this.setValue(value);
    }

    @Override
    public Boolean getConfigValue() {
        return (Boolean)this.value;
    }
}
