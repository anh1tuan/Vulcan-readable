package me.frep.vulcan.api.check;

import java.util.List;

public interface ICheckData {
    boolean isEnabled();

    boolean isPunishable();

    boolean isBroadcastPunishment();

    boolean isHotbarShuffle();

    boolean isRandomRotation();

    int getMaxViolations();

    int getAlertInterval();

    int getMinimumViolationLevelToNotify();

    int getMaxPing();

    int getMinimumViolationLevelToRandomlyRotate();

    int getMinimumViolationLevelToShuffleHotbar();

    double getMinimumTps();

    double getMaxBuffer();

    double getBufferDecay();

    double getBufferMultiple();

    List getPunishmentCommands();

    int getRandomRotationInterval();
}
