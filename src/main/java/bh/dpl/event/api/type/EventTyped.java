package bh.dpl.event.api.type;

import bh.dpl.event.api.Event;

public abstract class EventTyped extends Event implements Typed
{
    private final byte type;
    
    protected EventTyped(final byte eventType) {
        this.type = eventType;
    }
    
    @Override
    public byte getType() {
        return this.type;
    }
}
