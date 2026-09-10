package me.frep.vulcan.api.data;

public interface IPlayerData {
    public int getTotalViolations();

    public int getCombatViolations();

    public int getMovementViolations();

    public int getPlayerViolations();

    public int getAutoClickerViolations();

    public int getTimerViolations();

    public int getScaffoldViolations();

    public long getJoinTime();

    public int getJoinTicks();

    public String getClientBrand();

    public long getLastClientBrandAlert();
}

