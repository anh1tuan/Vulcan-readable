package me.frep.vulcan.spigot;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import java.awt.Color;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import me.clip.placeholderapi.PlaceholderAPI;
import me.frep.vulcan.api.event.VulcanDiscordWebhookPunishEvent;
import me.frep.vulcan.api.event.VulcanPunishEvent;
import me.frep.vulcan.api.event.VulcanPunishmentLogCreateEvent;
import me.frep.vulcan.spigot.check.AbstractCheck;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class AlertPlaceholders {
    private static boolean debug;

    public void handlePunishment(AbstractCheck check, PlayerData data) {
        if (check == null || data == null || data.getPlayer() == null || !check.isPunishable()) {
            return;
        }
        long now = System.currentTimeMillis();
        if (now - data.getLastPunishmentTime() < (long) ConfigValues.settingsPunishmentDelay) {
            return;
        }
        if (data.getPlayer().hasPermission("vulcan.bypass." + check.getCheckName().toLowerCase())) {
            return;
        }
        data.setLastPunishmentTime(now);
        if (ConfigValues.apiExportEnabled) {
            VulcanPunishEvent event = new VulcanPunishEvent(data.getPlayer(), check);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return;
            }
        }
        if (ConfigValues.punishmentFileEnabled) {
            AlertFileLogger.logPunishment(check, data);
            if (ConfigValues.apiExportEnabled) {
                Bukkit.getPluginManager().callEvent(new VulcanPunishmentLogCreateEvent(data.getPlayer()));
            }
        }
        int randomId = ThreadLocalRandom.current().nextInt(50000);
        if (ConfigValues.discordWebhookPunishmentsEnabled
            && !ConfigValues.discordWebhookPunishmentsUrl.equalsIgnoreCase("insert-webhook-url-here")) {
            VulcanDiscordWebhookPunishEvent webhookEvent = new VulcanDiscordWebhookPunishEvent(data.getPlayer());
            if (!webhookEvent.isCancelled()) {
                VulcanBootstrap.INSTANCE.getExecutorService().execute(() -> sendPunishmentWebhook(data, check));
                StatsManager.recordStat("punishments", Integer.valueOf(StatsManager.getTotalPunishments() + 1));
            }
        }
        if (ConfigValues.punishmentsMessage != null && !ConfigValues.punishmentsMessage.equalsIgnoreCase("")) {
            this.sendAlertMessage(formatPunishmentMessage(check, data, randomId));
        }
        Boolean broadcast = (Boolean) ConfigValues.punishmentCommandsBySeverity.get(check.getCheckName());
        if (Boolean.TRUE.equals(broadcast) && ConfigValues.punishmentsBroadcast != null) {
            ConfigValues.punishmentsBroadcast.forEach(raw -> broadcastPunishment(data, check, randomId, (String) raw));
        }
        List commands = (List) ConfigValues.punishmentCommandListByCheck.get(check.getCheckName());
        if (commands != null && !commands.isEmpty()) {
            new ScheduledTask(this, commands, data, randomId, check).runTask(VulcanBootstrap.INSTANCE.getVulcanPlugin());
        }
        data.getChecks().forEach(chk -> { if (chk instanceof AbstractCheck) resetCheckViolations((AbstractCheck) chk); });
    }

    public void sendAlertMessage(String message) {
        VulcanBootstrap.INSTANCE.getAlertMessageBuilder().sendAlertMessage(message);
    }

    public void sendPluginMessage(Player player, String channel) {
        if (!ConfigValues.settingsPluginMessaging || player == null || channel == null) {
            return;
        }
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("Forward");
        output.writeUTF(channel);
        player.sendPluginMessage((Plugin) VulcanBootstrap.INSTANCE.getVulcanPlugin(), "BungeeCord", output.toByteArray());
    }

    public static String spigot() {
        return LicenseConfig.spigot();
    }

    public static String nonce() {
        return LicenseConfig.nonce();
    }

    public static String resource() {
        return LicenseConfig.resource();
    }

    public static void setDebug(boolean value) {
        debug = value;
    }

    public static boolean isDebug() {
        return debug;
    }

    public static boolean shouldProcessPunishments() {
        return !isDebug();
    }

    private static void resetCheckViolations(AbstractCheck check) {
        check.setViolationLevel(0);
    }

    private static String attackingName(PlayerData data) {
        if (data.getReachEntityHelper() == null || data.getReachEntityHelper().getAttackingPlayer() == null) {
            return "None";
        }
        return data.getReachEntityHelper().getAttackingPlayer().getName();
    }

    private static int maxVl(AbstractCheck check) {
        Integer value = (Integer) ConfigValues.maxViolationsByCheck.get(check.getCheckName());
        return value == null ? 0 : value.intValue();
    }

    private String formatPunishmentMessage(AbstractCheck check, PlayerData data, int randomId) {
        String message = ConfigValues.punishmentsMessage
            .replaceAll("%prefix%", ConfigValues.resetCommandSyntax)
            .replaceAll("%id%", Integer.toString(randomId))
            .replace("%player%", data.getUsername())
            .replaceAll("%uuid%", data.getPlayer().getUniqueId().toString())
            .replaceAll("%server-name%", ConfigValues.settingsServerName)
            .replaceAll("%category%", AlertFileLogger.formatCheckCategory(check.getCategory().toLowerCase()))
            .replaceAll("%complex-type%", check.getCheckInfo().complexType())
            .replaceAll("%description%", check.getCheckInfo().description())
            .replaceAll("%check%", check.getCheckInfo().name())
            .replaceAll("%target%", attackingName(data))
            .replaceAll("%version%", data.getUser().getClientVersion().getReleaseName())
            .replaceAll("%client-brand%", data.getClientBrand())
            .replaceAll("%type%", Character.toString(check.getCheckInfo().type()))
            .replaceAll("%vl%", Integer.toString(check.getViolationLevel()));
        if (VulcanBootstrap.INSTANCE.isPlaceholderApiPresent()) {
            return PlaceholderAPI.setPlaceholders(data.getPlayer(), message);
        }
        return message;
    }

    private static void broadcastPunishment(PlayerData data, AbstractCheck check, int randomId, String template) {
        if (template == null || data.getPlayer() == null) {
            return;
        }
        String message = template
            .replace("%player%", data.getUsername())
            .replaceAll("%version%", data.getUser().getClientVersion().getReleaseName())
            .replaceAll("%client-brand%", data.getClientBrand())
            .replaceAll("%check%", check.getCheckInfo().name())
            .replaceAll("%server-name%", ConfigValues.settingsServerName)
            .replaceAll("%uuid%", data.getPlayer().getUniqueId().toString())
            .replaceAll("%target%", attackingName(data))
            .replaceAll("%complex-type%", check.getCheckInfo().complexType())
            .replaceAll("%category%", AlertFileLogger.formatCheckCategory(check.getCategory().toLowerCase()))
            .replaceAll("%id%", Integer.toString(randomId))
            .replaceAll("%description%", check.getCheckInfo().description())
            .replaceAll("%type%", Character.toString(check.getCheckInfo().type()));
        ServerCompatibility.broadcastTranslatedMessage(message);
    }

    private static void sendPunishmentWebhook(PlayerData data, AbstractCheck check) {
        try {
            DiscordWebhookClient webhook = new DiscordWebhookClient(ConfigValues.discordWebhookPunishmentsUrl);
            webhook.setAvatarUrl(ConfigValues.discordWebhookPunishmentsAvatar);
            webhook.setUsername(ConfigValues.discordWebhookPunishmentsUsername);
            DiscordEmbed embed = new DiscordEmbed();
            embed.setTitle(ConfigValues.discordWebhookPunishmentsTitle);
            embed.setDescription(ConfigValues.discordWebhookPunishmentsThumbnail
                .replaceAll("%uuid%", data.getPlayer().getUniqueId().toString())
                .replace("%player%", data.getUsername()));
            embed.setColor(new Color(
                ConfigValues.discordWebhookPunishmentsColorR,
                ConfigValues.discordWebhookPunishmentsColorG,
                ConfigValues.discordWebhookPunishmentsColorB
            ));
            String experimental = check.getCheckInfo().experimental()
                ? ColorUtil.translateColors(ConfigValues.alertsExperimentalSymbol)
                : "";
            String description = ConfigValues.discordWebhookPunishmentsDescription
                .replace("%player%", data.getUsername())
                .replaceAll("%check%", check.getCheckInfo().name())
                .replaceAll("%server-name%", ConfigValues.settingsServerName)
                .replaceAll("%uuid%", data.getPlayer().getUniqueId().toString())
                .replaceAll("%x%", TrigUtil.formatDecimal(data.getBlockBreakTracker().getLocationX()))
                .replaceAll("%y%", TrigUtil.formatDecimal(data.getBlockBreakTracker().getLocationY()))
                .replaceAll("%z%", TrigUtil.formatDecimal(data.getBlockBreakTracker().getLocationZ()))
                .replaceAll("%cps%", Double.toString(data.getClickTracker().getCps()))
                .replaceAll("%violations%", Integer.toString(data.getTotalViolations()))
                .replaceAll("%world%", data.getPlayer().getWorld().getName())
                .replaceAll("%max-vl%", Integer.toString(maxVl(check)))
                .replaceAll("%client-brand%", data.getClientBrand())
                .replaceAll("%category%", AlertFileLogger.formatCheckCategory(check.getCategory().toLowerCase()))
                .replaceAll("%complex-type%", check.getCheckInfo().complexType())
                .replaceAll("%version%", data.getUser().getClientVersion().getReleaseName())
                .replaceAll("%vl%", Integer.toString(check.getViolationLevel()))
                .replaceAll("%target%", attackingName(data))
                .replaceAll("%description%", check.getDescription())
                .replaceAll("%dev%", experimental)
                .replaceAll("%type%", Character.toString(check.getCheckInfo().type()))
                .replaceAll("%tps%", TrigUtil.formatDecimal(ServerCompatibility.getTicksPerSecond()))
                .replaceAll("%check%", check.getName());
            embed.setDescription(description);
            if (ConfigValues.discordWebhookPunishmentsDescriptionField) {
                embed.addField("Description", check.getDescription(), true);
            }
            if (ConfigValues.discordWebhookPunishmentsServerNameField) {
                embed.addField("Server Name", ConfigValues.settingsServerName, true);
            }
            if (ConfigValues.discordWebhookPunishmentsClientBrandField) {
                embed.addField("%client-brand%", data.getClientBrand(), true);
            }
            if (ConfigValues.discordWebhookPunishmentsClientVersionField) {
                embed.addField("Version", data.getUser().getClientVersion().getReleaseName(), true);
            }
            if (ConfigValues.discordWebhookPunishmentsPingTpsField) {
                embed.addField("Connection", data.getCheckState().getPing() + "ms | "
                    + TrigUtil.formatDecimal(ServerCompatibility.getTicksPerSecond()), true);
            }
            if (ConfigValues.discordWebhookPunishmentsLocationField) {
                embed.addField("Location", "(" + data.getPlayer().getWorld().getName()
                    + ", " + data.getBlockBreakTracker().getLocationX()
                    + ", " + data.getBlockBreakTracker().getLocationY()
                    + ", " + data.getBlockBreakTracker().getLocationZ() + ")", true);
            }
            webhook.addEmbed(embed);
            if (ConfigValues.discordWebhookPunishmentsContent != null
                && !ConfigValues.discordWebhookPunishmentsContent.equals("")) {
                webhook.setContent(ConfigValues.discordWebhookPunishmentsContent.replaceAll("%player%", data.getUsername()));
            }
            if (ConfigValues.discordWebhookCooldown) {
                if (VulcanBootstrap.INSTANCE.getDiscordWebhookCooldownCount() < ConfigValues.discordWebhookCooldownMaxWebhooks) {
                    webhook.execute();
                }
                VulcanBootstrap.INSTANCE.setDiscordWebhookCooldownCount(VulcanBootstrap.INSTANCE.getDiscordWebhookCooldownCount() + 1);
            } else {
                webhook.execute();
            }
        } catch (Exception ignored) {
        }
    }

    static {
        AlertPlaceholders.setDebug(false);
    }
}
