package me.frep.vulcan.spigot;

import java.util.function.Function;

public class ExemptionChecker {
    private final PlayerData playerData;

    public ExemptionChecker(PlayerData playerData) {
        this.playerData = playerData;
    }

    public boolean isExempt(ExemptType type) {
        return type.test(this.playerData);
    }

    public boolean isExempt(ExemptType... types) {
        if (types == null) {
            return false;
        }
        for (ExemptType type : types) {
            if (this.isExempt(type)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAnyExempt(Function function) {
        return Boolean.TRUE.equals(function.apply(this.playerData));
    }
}
