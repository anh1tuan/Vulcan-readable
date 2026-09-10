package me.frep.vulcan.spigot.check.data;

import java.util.List;
import me.frep.vulcan.api.check.ICheckData;

public class CheckData implements ICheckData {
    private boolean enabled;
    private boolean punishable;
    private boolean broadcastPunishment;
    private boolean hotbarShuffle;
    private boolean randomRotation;
    private boolean setbackEnabled;
    private boolean staffAlertsEnabled;
    private int maxViolations;
    private int alertInterval;
    private int minimumVlToNotify;
    private int maxPing;
    private int minimumVlToShuffleHotbar;
    private int randomRotationInterval;
    private int minimumVlToRandomlyRotate;
    private double minimumTps;
    private double maxBuffer;
    private double bufferDecay;
    private double bufferMultiple;
    private List punishmentCommands;
    private final String checkName;

    public CheckData(String checkName) {
        this.checkName = checkName;
    }

    @Override
    public String toString() {
        return "CheckData{name=" + this.checkName
            + ", enabled=" + this.enabled
            + ", punishable=" + this.punishable
            + ", broadcastPunishment=" + this.broadcastPunishment
            + "}";
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public boolean isPunishable() {
        return this.punishable;
    }

    @Override
    public boolean isBroadcastPunishment() {
        return this.broadcastPunishment;
    }

    @Override
    public boolean isHotbarShuffle() {
        return this.hotbarShuffle;
    }

    @Override
    public boolean isRandomRotation() {
        return this.randomRotation;
    }

    @Override
    public int getMaxViolations() {
        return this.maxViolations;
    }

    @Override
    public int getAlertInterval() {
        return this.alertInterval;
    }

    @Override
    public int getMinimumViolationLevelToNotify() {
        return this.minimumVlToNotify;
    }

    @Override
    public int getMaxPing() {
        return this.maxPing;
    }

    @Override
    public int getMinimumViolationLevelToRandomlyRotate() {
        return this.minimumVlToRandomlyRotate;
    }

    @Override
    public int getMinimumViolationLevelToShuffleHotbar() {
        return this.minimumVlToShuffleHotbar;
    }

    @Override
    public double getMinimumTps() {
        return this.minimumTps;
    }

    @Override
    public double getMaxBuffer() {
        return this.maxBuffer;
    }

    @Override
    public double getBufferDecay() {
        return this.bufferDecay;
    }

    @Override
    public double getBufferMultiple() {
        return this.bufferMultiple;
    }

    @Override
    public List getPunishmentCommands() {
        return this.punishmentCommands;
    }

    @Override
    public int getRandomRotationInterval() {
        return this.randomRotationInterval;
    }

    public boolean isSetbackEnabled() {
        return this.setbackEnabled;
    }

    public boolean isStaffAlertsEnabled() {
        return this.staffAlertsEnabled;
    }

    public String getCheckName() {
        return this.checkName;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setPunishable(boolean punishable) {
        this.punishable = punishable;
    }

    public void setBroadcastPunishment(boolean broadcastPunishment) {
        this.broadcastPunishment = broadcastPunishment;
    }

    public void setHotbarShuffle(boolean hotbarShuffle) {
        this.hotbarShuffle = hotbarShuffle;
    }

    public void setRandomRotation(boolean randomRotation) {
        this.randomRotation = randomRotation;
    }

    public void setSetbackEnabled(boolean setbackEnabled) {
        this.setbackEnabled = setbackEnabled;
    }

    public void setStaffAlertsEnabled(boolean staffAlertsEnabled) {
        this.staffAlertsEnabled = staffAlertsEnabled;
    }

    public void setMaxViolations(int maxViolations) {
        this.maxViolations = maxViolations;
    }

    public void setAlertInterval(int alertInterval) {
        this.alertInterval = alertInterval;
    }

    public void setMinimumVlToNotify(int minimumVlToNotify) {
        this.minimumVlToNotify = minimumVlToNotify;
    }

    public void setMaxPing(int maxPing) {
        this.maxPing = maxPing;
    }

    public void handle(int maxPing) {
        this.setMaxPing(maxPing);
    }

    public void setMinimumVlToRandomlyRotate(int minimumVlToRandomlyRotate) {
        this.minimumVlToRandomlyRotate = minimumVlToRandomlyRotate;
    }

    public void setMinimumVlToShuffleHotbar(int minimumVlToShuffleHotbar) {
        this.minimumVlToShuffleHotbar = minimumVlToShuffleHotbar;
    }

    public void setRandomRotationInterval(int randomRotationInterval) {
        this.randomRotationInterval = randomRotationInterval;
    }

    public void setMinimumTps(double minimumTps) {
        this.minimumTps = minimumTps;
    }

    public void setMaxBuffer(double maxBuffer) {
        this.maxBuffer = maxBuffer;
    }

    public void setBufferDecay(double bufferDecay) {
        this.bufferDecay = bufferDecay;
    }

    public void setBufferMultiple(double bufferMultiple) {
        this.bufferMultiple = bufferMultiple;
    }

    public void setPunishmentCommands(List punishmentCommands) {
        this.punishmentCommands = punishmentCommands;
    }

}
