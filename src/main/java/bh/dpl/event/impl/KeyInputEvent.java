package bh.dpl.event.impl;

import bh.dpl.event.api.Event;

/**
 * @author KevinLeft
 */

public class KeyInputEvent extends Event {
    public final int key;

    public KeyInputEvent(int key) {
        this.key = key;
    }
}
