package bh.dpl.event.impl;

import bh.dpl.event.api.CancellableEvent;

/**
 * @author Kev1nLeft
 */

public class SlowDownEvent extends CancellableEvent {
    public float strafeMultiplier;
    public float forwardMultiplier;

    public SlowDownEvent(final float strafeMultiplier, final float forwardMultiplier) {
        this.strafeMultiplier = strafeMultiplier;
        this.forwardMultiplier = forwardMultiplier;
    }

    public float getForwardMultiplier() {
        return forwardMultiplier;
    }

    public float getStrafeMultiplier() {
        return strafeMultiplier;
    }

    public void setForwardMultiplier(float forwardMultiplier) {
        this.forwardMultiplier = forwardMultiplier;
    }

    public void setStrafeMultiplier(float strafeMultiplier) {
        this.strafeMultiplier = strafeMultiplier;
    }
}
