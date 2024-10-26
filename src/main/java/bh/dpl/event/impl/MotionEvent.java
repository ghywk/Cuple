package bh.dpl.event.impl;

import bh.dpl.event.api.Event;
import bh.dpl.event.type.EventType;

/**
 * @author Kev1nLeft
 */

public class MotionEvent extends Event {
    public double x,y,z;
    public float yaw,pitch;
    public boolean onGround;

    private final EventType type;

    public MotionEvent(double x, double y , double z, float yaw, float pitch, boolean onGround, EventType type) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
        this.type = type;
    }

    public MotionEvent(float yaw, float pitch, EventType type) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.type = type;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public EventType getType() {
        return type;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
