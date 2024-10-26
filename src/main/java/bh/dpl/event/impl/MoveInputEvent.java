package bh.dpl.event.impl;

import bh.dpl.event.api.Event;

/**
 * @author Kev1nLeft
 */

public class MoveInputEvent extends Event {
    private float forward, strafe;

    public MoveInputEvent(float forward, float strafe){
        this.forward = forward;
        this.strafe = strafe;
    }

    public float getForward() {
        return forward;
    }

    public float getStrafe() {
        return strafe;
    }

    public void setForward(float forward) {
        this.forward = forward;
    }

    public void setStrafe(float strafe) {
        this.strafe = strafe;
    }
}
