package me.frep.vulcan.api.check;

public interface Check {
    String getCategory();

    String getName();

    char getType();

    char getDisplayType();

    int getMaxViolationLevel();

    int getViolationLevel();

    double getMaxBuffer();

    boolean isExperimental();

    double getBufferDecay();

    double getBufferMultiple();

    int getMinimumViolationLevelToNotify();

    int getAlertInterval();

    double getBuffer();

    void setViolationLevel(int violationLevel);

    boolean isPunishable();

    void setBuffer(double buffer);

    String getDescription();

    String getComplexType();

    String getDisplayName();
}
