package bh.dpl.event.impl;

import bh.dpl.event.api.Event;

/**
 * @author Kev1nLeft
 */

public class StrafeEvent extends Event {
    public float yaw;

    public StrafeEvent(float yaw) {
        this.yaw = yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getYaw() {
        return yaw;
    }
}
