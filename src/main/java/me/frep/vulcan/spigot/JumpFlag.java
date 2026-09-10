package me.frep.vulcan.spigot;

import java.util.HashSet;
import java.util.Set;

public class JumpFlag {
    private final double posX;
    private final double posY;
    private final double posZ;
    private boolean onGround;
    private boolean clientGround;
    private boolean jumping;
    private double deltaX;
    private double deltaY;
    private double deltaZ;
    private double distance;
    private float acceleration;
    private Set collidedBlocks;

    public JumpFlag(double x, double y, double z) {
        this.onGround = false;
        this.clientGround = false;
        this.jumping = false;
        this.deltaX = 0.0;
        this.deltaY = 0.0;
        this.deltaZ = 0.0;
        this.distance = 0.0;
        this.acceleration = 0.6f;
        this.collidedBlocks = new HashSet();
        this.posX = x;
        this.posY = y;
        this.posZ = z;
    }

    public double getPosX() {
        return this.posX;
    }

    public double getPosY() {
        return this.posY;
    }

    public double getPosZ() {
        return this.posZ;
    }

    public boolean isOnGround() {
        return this.onGround;
    }

    public boolean isClientGround() {
        return this.clientGround;
    }

    public boolean isJumping() {
        return this.jumping;
    }

    public double getDeltaX() {
        return this.deltaX;
    }

    public double getDeltaY() {
        return this.deltaY;
    }

    public double getDeltaZ() {
        return this.deltaZ;
    }

    public double getDistance() {
        return this.distance;
    }

    public float getAcceleration() {
        return this.acceleration;
    }

    public Set getCollidedBlocks() {
        return this.collidedBlocks;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public void setClientGround(boolean clientGround) {
        this.clientGround = clientGround;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }

    public void setDeltaX(double deltaX) {
        this.deltaX = deltaX;
    }

    public void setDeltaY(double deltaY) {
        this.deltaY = deltaY;
    }

    public void setDeltaZ(double deltaZ) {
        this.deltaZ = deltaZ;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }

    public void setCollidedBlocks(Set collidedBlocks) {
        this.collidedBlocks = collidedBlocks;
    }
}
