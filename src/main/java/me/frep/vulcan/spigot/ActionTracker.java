package me.frep.vulcan.spigot;

import java.util.ArrayDeque;

import org.bukkit.util.Vector;


public class ActionTracker {
    private final PlayerData playerData;
    private float yaw;
    private float pitch;
    private float previousYaw;
    private float previousPitch;
    private float yawDelta;
    private float pitchDelta;
    private float previousYawDelta;
    private float previousPitchDelta;
    private float yawAcceleration;
    private float pitchAcceleration;
    private float previousYawAcceleration;
    private float previousPitchAcceleration;
    private float gcdYaw;
    private float gcdPitch;
    private float adjustedPitchDelta;
    private float adjustedYawDelta;
    private float previousAdjustedPitchDelta;
    private float previousAdjustedYawDelta;
    private boolean yawRecentlyChanged;
    private boolean pitchRecentlyChanged;
    private boolean recentRotation;
    private double sensitivityGcd;
    private double sensitivity;
    private final ArrayDeque<Integer> sensitivitySamples = new ArrayDeque<>();
    private int sensitivityMode;
    private int lastSensitivitySampleTick;
    private int lastPitchSampleTick;
    private int lastYawSampleTick;
    private int gcdYawTicks;
    private int gcdPitchTicks;
    public ActionTracker(PlayerData playerData) {
        this.playerData = playerData;
    }

    public void handleRotation(long timestamp, float yaw, float pitch) {
        this.previousYaw = this.yaw;
        this.previousPitch = this.pitch;
        this.yaw = yaw;
        this.pitch = pitch;
        this.previousYawDelta = this.yawDelta;
        this.previousPitchDelta = this.pitchDelta;
        this.yawDelta = Math.abs(yaw - this.previousYaw);
        this.pitchDelta = Math.abs(pitch - this.previousPitch);
        this.previousPitchAcceleration = this.pitchAcceleration;
        this.previousYawAcceleration = this.yawAcceleration;
        this.yawAcceleration = Math.abs(this.yawDelta - this.previousYawDelta);
        this.pitchAcceleration = Math.abs(this.pitchDelta - this.previousPitchDelta);
        float gcdDivisor = (float) (this.sensitivity * 0.6 + 0.2);
        gcdDivisor = gcdDivisor * gcdDivisor * gcdDivisor * 1.2f;
        this.gcdYaw = this.yawDelta / gcdDivisor;
        this.gcdPitch = this.pitchDelta / gcdDivisor;
        this.gcdYawTicks = (int) (this.yawDelta / gcdDivisor);
        this.gcdPitchTicks = (int) (this.pitchDelta / gcdDivisor);
        this.updateSensitivity(timestamp);
        float predictedYaw = (float) (this.yawDelta * 1.073742 + this.yawDelta + 0.15);
        float predictedPitch = (float) (this.pitchDelta * 1.073742 - (this.pitchDelta - 0.15));
        this.previousAdjustedPitchDelta = this.adjustedPitchDelta;
        this.previousAdjustedYawDelta = this.adjustedYawDelta;
        this.adjustedPitchDelta = Math.abs(this.pitchDelta - Math.abs(this.pitchDelta - predictedPitch));
        this.adjustedYawDelta = Math.abs(this.yawDelta - Math.abs(this.yawDelta - predictedYaw));
        if (this.pitchDelta <= 0.1f) {
            return;
        }
        if (this.sensitivityMode >= 25) {
            return;
        }
        this.resetTickDeque();
    }

    public boolean isSensitivityInExpectedRange() {
        return this.sensitivityMode > 0 && this.sensitivityMode < 200;
    }

    public float getLookAngle() {
        BlockBreakTracker tracker = this.playerData.getBlockBreakTracker();
        Vector position = new Vector(tracker.getLocationX(), 0.0, tracker.getLocationZ());
        Vector look = new Vector(
            -Math.sin(this.yaw * (float) Math.PI / 180.0f) * 0.5,
            0.0,
            Math.cos(this.yaw * (float) Math.PI / 180.0f) * 0.5
        );
        return (float) position.angle(look);
    }
    private void updateSensitivity(long timestamp) {
        int currentTick = VulcanBootstrap.INSTANCE.getKeepAliveRunnable().getCurrentTick();
        float yawChange = Math.abs(this.yawDelta - this.previousYawDelta);
        float pitchChange = Math.abs(this.pitchDelta - this.previousPitchDelta);
        float yawDiff = Math.abs(yawChange - this.yawDelta);
        float pitchDiff = Math.abs(pitchChange - this.pitchDelta);
        if (yawDiff > 1.0f && pitchDiff > 1.0f) {
            this.lastSensitivitySampleTick = currentTick;
        }
        if (this.pitchDelta < 20.0f && this.sensitivityGcd < 0.0) {
            this.lastPitchSampleTick = currentTick;
        }
        if (this.yawRecentlyChanged && this.pitchRecentlyChanged) {
            this.lastYawSampleTick = currentTick;
        }
        this.yawRecentlyChanged = currentTick - this.lastSensitivitySampleTick > 3;
        this.pitchRecentlyChanged = currentTick - this.lastPitchSampleTick < 3;
        this.recentRotation = currentTick - this.lastYawSampleTick < 8;
    }

    private void resetTickDeque() {
        float gcd = (float) TrigUtil.gcd((double) this.pitchDelta, (double) this.previousPitchDelta);
        double cubeRoot = Math.cbrt(0.8333 * (double) gcd);
        double mapped = 1.666 * cubeRoot - 0.3333;
        double sensitivityPercent = mapped * 200.0;
        this.sensitivityGcd = sensitivityPercent;
        this.sensitivitySamples.add((int) sensitivityPercent);
        if (this.sensitivitySamples.size() != 40) {
            return;
        }
        this.sensitivityMode = TrigUtil.mostFrequentCount(this.sensitivitySamples);
        if (this.isSensitivityInExpectedRange()) {
            this.sensitivity = MovementConstants.sensitivityForPercent(this.sensitivityMode);
        }
        this.sensitivitySamples.clear();
    }

    public boolean isTickBelowFifty() {
        return this.sensitivityMode < 50;
    }

    public PlayerData getPlayerData() {
        return this.playerData;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public float getPreviousYaw() {
        return this.previousYaw;
    }

    public float getPreviousPitch() {
        return this.previousPitch;
    }

    public float getYawDelta() {
        return this.yawDelta;
    }

    public float getPitchDelta() {
        return this.pitchDelta;
    }

    public float getYawAcceleration() {
        return this.yawAcceleration;
    }

    public float getPitchAcceleration() {
        return this.pitchAcceleration;
    }

    public float getPreviousYawDelta() {
        return this.previousYawDelta;
    }

    public float getPreviousPitchDelta() {
        return this.previousPitchDelta;
    }

    public boolean isRecentRotation() {
        return this.recentRotation;
    }

    public double getSensitivity() {
        return this.sensitivity;
    }
}
