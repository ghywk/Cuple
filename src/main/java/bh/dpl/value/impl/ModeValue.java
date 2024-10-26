package bh.dpl.value.impl;

import bh.dpl.utils.exception.MemberNotFoundException;
import bh.dpl.value.Value;

/**
 * @author KevinLeft
 */

public class ModeValue<V extends Enum<?>> extends Value<V> {
    private final V[] modes;

    public ModeValue(final String name, final V[] modes, final V value) {
        super(name);
        this.modes = modes;
        this.setValue(value);
    }

    public ModeValue(final String name, final V[] modes, final V value, final Dependency dependenc) {
        super(name, dependenc);
        this.modes = modes;
        this.setValue(value);
    }

    public boolean is(final String mode) {
        return this.getValue().name().equalsIgnoreCase(mode);
    }

    public void setMode(final String mode) {
        for (final V e : this.modes) {
            if (e.name().equalsIgnoreCase(mode)) {
                this.setValue(e);
                return;
            }
        }
        throw new MemberNotFoundException("Not Found Mode.");
    }

    public boolean isValid(final String name) {
        for (final V e : this.modes) {
            if (e.name().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public void next() {
        int index = arrayIndex(modes, getValue()) + 1;
        if (index == modes.length) {
            setMode(modes[0].name());
            return;
        }
        setMode(modes[index].name());
    }

    public int arrayIndex(V[] array, V target) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(target)) return i;
        }

        throw new MemberNotFoundException("Array: " + target);
    }

    @Override
    public String getConfigValue() {
        return this.getValue().name();
    }

    public V get() {
        return this.getValue();
    }
}
