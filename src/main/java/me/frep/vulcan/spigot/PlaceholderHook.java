package me.frep.vulcan.spigot;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;


public class PlaceholderHook
extends PlaceholderExpansion {
    private final VulcanPlugin plugin;

    public PlaceholderHook() {
        this.plugin = VulcanBootstrap.INSTANCE.getVulcanPlugin();
    }

    public boolean persist() {
        return true;
    }

    public boolean canRegister() {
        return true;
    }

    public String getAuthor() {
        return this.plugin.getDescription().getAuthors().toString();
    }

    public String getIdentifier() {
        return "vulcan";
    }

    public String getVersion() {
        return this.plugin.getDescription().getVersion();
    }
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null || identifier == null) {
            return "";
        }
        PlayerData data = VulcanBootstrap.INSTANCE.getHookManager().getPlayerData(player);
        if (data == null) {
            return "";
        }
        if (identifier.equalsIgnoreCase("cps")) {
            return String.valueOf(data.getClickTracker().getCps());
        }
        if (identifier.equalsIgnoreCase("ping")) {
            return String.valueOf(data.getCheckState().getPing());
        }
        if (identifier.equalsIgnoreCase("vl") || identifier.equalsIgnoreCase("total_violations")) {
            return Integer.toString(data.getTotalViolations());
        }
        if (identifier.equalsIgnoreCase("combat_violations")) {
            return Integer.toString(data.getCombatViolations());
        }
        if (identifier.equalsIgnoreCase("movement_violations")) {
            return Integer.toString(data.getMovementViolations());
        }
        if (identifier.equalsIgnoreCase("client_brand")) {
            return data.getClientBrand();
        }
        return "";
    }
}
