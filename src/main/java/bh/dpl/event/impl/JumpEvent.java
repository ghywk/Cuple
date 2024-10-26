package bh.dpl.event.impl;

import bh.dpl.event.api.Event;

/**
 * @author Kev1nLeft
 */

public class JumpEvent extends Event {
    public float yaw;

    public JumpEvent(float yaw){
        this.yaw = yaw;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
}
