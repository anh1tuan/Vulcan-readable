package me.frep.vulcan.spigot;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import me.clip.placeholderapi.PlaceholderAPI;
import me.frep.vulcan.api.event.VulcanDisableAlertsEvent;
import me.frep.vulcan.api.event.VulcanEnableAlertsEvent;
import me.frep.vulcan.spigot.check.AbstractCheck;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.geysermc.floodgate.FloodgateAPI;
import org.geysermc.floodgate.api.FloodgateApi;

public class AlertMessageBuilder {
    private final Set alertPlayers = new HashSet();
    private final Set verboseAlertPlayers = new HashSet();
    private final DecimalFormat decimalFormat = new DecimalFormat("##0.00");

    public void toggleAlerts(Player player) {
        if (player == null) {
            return;
        }
        if (this.alertPlayers.contains(player)) {
            this.alertPlayers.remove(player);
            player.sendMessage(ColorUtil.translateColors(ConfigValues.alertsToggledOffMessage));
            if (ConfigValues.apiExportEnabled) {
                VulcanDisableAlertsEvent event = new VulcanDisableAlertsEvent(player);
                AsyncScheduler.runTask(() -> Bukkit.getPluginManager().callEvent(event));
            }
            return;
        }
        this.alertPlayers.add(player);
        player.sendMessage(ColorUtil.translateColors(ConfigValues.alertsToggledOnMessage));
        if (ConfigValues.apiExportEnabled) {
            VulcanEnableAlertsEvent event = new VulcanEnableAlertsEvent(player);
            AsyncScheduler.runTask(() -> Bukkit.getPluginManager().callEvent(event));
        }
    }

    public void toggleVerboseAlerts(Player player) {
        if (player == null) {
            return;
        }
        if (this.verboseAlertPlayers.contains(player)) {
            this.verboseAlertPlayers.remove(player);
            player.sendMessage(ColorUtil.translateColors(ConfigValues.alertsToggledOffMessage));
            return;
        }
        this.verboseAlertPlayers.add(player);
        player.sendMessage(ColorUtil.translateColors(ConfigValues.alertsToggledOnMessage));
    }

    public void handleAlert(AbstractCheck check, PlayerData data, String information) {
        if (data == null || data.getPlayer() == null || check == null || this.shouldIgnoreAlert(data)) {
            return;
        }
        String checkLabel = this.formatCheckLabel(check);
        String experimental = check.getCheckInfo() != null && check.getCheckInfo().experimental()
            ? ColorUtil.translateColors(ConfigValues.alertsExperimentalSymbol)
            : "";
        String extra = information == null ? "" : information;
        String type = check.getCheckInfo() == null ? "?" : Character.toString(check.getCheckInfo().type());
        String console = this.applyAlertPlaceholders(ConfigValues.alertsConsoleFormat, check, data, checkLabel, extra, type, experimental);
        if (ConfigValues.printToConsole) {
            ServerCompatibility.logTranslatedInfo(ColorUtil.translateColors(console));
        }
        TextComponent component = this.buildHoverComponent(check, data, checkLabel, extra, type, experimental);
        if (ConfigValues.settingsAsyncAlerts) {
            this.alertPlayers.forEach(subscriber -> sendComponent(component, subscriber));
        } else {
            AsyncScheduler.runTask(() -> this.alertPlayers.forEach(subscriber -> sendComponent(component, subscriber)));
        }
        if (ConfigValues.messagesCustomCommands != null && !ConfigValues.messagesCustomCommands.isEmpty()) {
            AsyncScheduler.runTask(() -> dispatchCustomCommands(check, data, checkLabel, extra, type, experimental));
        }
        if (ConfigValues.discordWebhookAlertsEnabled
            && ConfigValues.discordWebhookAlertsUrl != null
            && !ConfigValues.discordWebhookAlertsUrl.equalsIgnoreCase("insert-webhook-url-here")) {
            VulcanBootstrap.INSTANCE.getExecutorService().execute(() -> sendAlertWebhook(check, data, extra));
        }
        AlertFileLogger.logAlert(check, data, extra);
    }

    public void handleApiAlert(Player player, String checkLabel, String extra, String type) {
        if (player == null) {
            return;
        }
        PlayerData data = VulcanBootstrap.INSTANCE.getHookManager().getPlayerData(player);
        if (data == null || data.getPlayer() == null || this.shouldIgnoreAlert(data)) {
            return;
        }
        String experimental = extra == null ? "" : extra;
        String label = checkLabel == null ? "unknown" : checkLabel;
        String typeLabel = type == null ? "?" : type;
        String console = this.applyAlertPlaceholders(ConfigValues.alertsConsoleFormat, null, data, label, experimental, typeLabel, experimental);
        if (ConfigValues.printToConsole) {
            ServerCompatibility.logTranslatedInfo(ColorUtil.translateColors(console));
        }
        String formatted = this.applyAlertPlaceholders(ConfigValues.alertsFormat, null, data, label, experimental, typeLabel, experimental);
        for (Object subscriber : this.alertPlayers) {
            if (subscriber instanceof Player) {
                ((Player) subscriber).sendMessage(ColorUtil.translateColors(formatted));
            }
        }
        if (ConfigValues.messagesCustomCommands != null && !ConfigValues.messagesCustomCommands.isEmpty()) {
            AsyncScheduler.runTask(() -> dispatchCustomCommands(null, data, label, experimental, typeLabel, experimental));
        }
    }

    public void sendAlertMessage(String message) {
        this.sendToAlertSubscribers(message);
    }

    private void sendToAlertSubscribers(String message) {
        if (message == null) {
            return;
        }
        this.alertPlayers.forEach(subscriber -> {
            if (subscriber instanceof Player) {
                ((Player) subscriber).sendMessage(ColorUtil.translateColors(message));
            }
        });
    }

    public Set getAlertPlayers() {
        return this.alertPlayers;
    }

    public Set getVerboseAlertPlayers() {
        return this.verboseAlertPlayers;
    }

    private boolean shouldIgnoreAlert(PlayerData data) {
        Player player = data.getPlayer();
        if (player == null) {
            return true;
        }
        if (ConfigValues.settingsIgnoreFloodgate) {
            if (VulcanBootstrap.INSTANCE.isFloodgateV1Present() && FloodgateAPI.isBedrockPlayer(player)) {
                return true;
            }
            if (VulcanBootstrap.INSTANCE.isFloodgateV2Present()
                && FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId())) {
                return true;
            }
        }
        String brand = data.getClientBrand() == null ? "" : data.getClientBrand().toLowerCase();
        if (ConfigValues.settingsIgnoreVivecraft && brand.contains("vivecraft")) {
            return true;
        }
        if (ConfigValues.settingsIgnoreFlagsGeyserClientBrand && brand.contains("geyser")) {
            return true;
        }
        if (ConfigValues.settingsIgnoreGeyserUuids && player.getUniqueId().toString().startsWith("00000000")) {
            return true;
        }
        if (ConfigValues.settingsIgnoreGeyserPrefixes
            && player.getName().startsWith(ConfigValues.settingsIgnoreGeyserPrefix)) {
            return true;
        }
        return false;
    }

    private String formatCheckLabel(AbstractCheck check) {
        if (check == null || check.getCheckInfo() == null) {
            return check == null ? "unknown" : check.getCheckName();
        }
        return check.getCheckInfo().name() + " " + check.getCheckInfo().type();
    }

    private String applyAlertPlaceholders(String template, AbstractCheck check, PlayerData data, String checkLabel, String extra, String type, String experimental) {
        if (template == null || data.getPlayer() == null) {
            return "";
        }
        Integer maxVl = check == null ? Integer.valueOf(0) : (Integer) ConfigValues.maxViolationsByCheck.get(check.getCheckName());
        String category = check == null ? "" : AlertFileLogger.formatCheckCategory(check.getCategory().toLowerCase());
        String complexType = check == null || check.getCheckInfo() == null ? "" : check.getCheckInfo().complexType();
        String description = check == null || check.getCheckInfo() == null ? "" : check.getCheckInfo().description();
        String message = template
            .replace("%player%", data.getUsername())
            .replaceAll("%check%", checkLabel)
            .replaceAll("%dev%", extra == null ? "" : extra)
            .replaceAll("%type%", type)
            .replaceAll("%server-name%", ConfigValues.settingsServerName)
            .replaceAll("%vl%", Integer.toString(check == null ? 0 : check.getViolationLevel()))
            .replaceAll("%client-brand%", data.getClientBrand())
            .replaceAll("%max-vl%", Integer.toString(maxVl == null ? 0 : maxVl.intValue()))
            .replaceAll("%ping%", Integer.toString(CollisionUtil.getPlayerPing(data.getPlayer())))
            .replaceAll("%world%", data.getPlayer().getWorld().getName())
            .replaceAll("%x%", TrigUtil.formatDecimal(data.getBlockBreakTracker().getLocationX()))
            .replaceAll("%y%", TrigUtil.formatDecimal(data.getBlockBreakTracker().getLocationY()))
            .replaceAll("%z%", TrigUtil.formatDecimal(data.getBlockBreakTracker().getLocationZ()))
            .replaceAll("%tps%", TrigUtil.formatDecimal(ServerCompatibility.getTicksPerSecond()))
            .replaceAll("%violations%", Integer.toString(data.getTotalViolations()))
            .replaceAll("%combat-violations%", Integer.toString(data.getCombatViolations()))
            .replaceAll("%movement-violations%", Integer.toString(data.getMovementViolations()))
            .replaceAll("%player-violations%", Integer.toString(data.getPlayerViolations()))
            .replaceAll("%cps%", TrigUtil.formatDecimal(data.getClickTracker().getCps()))
            .replaceAll("%version%", data.getUser().getClientVersion().getReleaseName())
            .replaceAll("%uuid%", data.getPlayer().getUniqueId().toString())
            .replaceAll("%category%", category)
            .replaceAll("%complex-type%", complexType)
            .replaceAll("%description%", description)
            .replaceAll("%info%", extra == null ? "" : extra)
            .replaceAll("%target%", attackingName(data))
            .replaceAll("%dev%", experimental == null ? "" : experimental);
        if (VulcanBootstrap.INSTANCE.isPlaceholderApiPresent()) {
            return PlaceholderAPI.setPlaceholders(data.getPlayer(), message);
        }
        return message;
    }

    private TextComponent buildHoverComponent(AbstractCheck check, PlayerData data, String checkLabel, String extra, String type, String experimental) {
        String body = this.applyAlertPlaceholders(ConfigValues.alertsFormat, check, data, checkLabel, extra, type, experimental);
        TextComponent component = new TextComponent(TextComponent.fromLegacyText(ColorUtil.translateColors(body)));
        if (ConfigValues.alertsClickCommands != null) {
            Iterator iterator = ConfigValues.alertsClickCommands.iterator();
            if (iterator.hasNext()) {
                String command = (String) iterator.next();
                component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command
                    .replace("%check%", data.getPlayer().getName())
                    .replaceAll("%uuid%", data.getPlayer().getUniqueId().toString())
                    .replace("%player%", data.getUsername())));
            }
        }
        if (ConfigValues.alertsHoverMessage != null && !ConfigValues.alertsHoverMessage.isEmpty()) {
            StringBuilder hover = new StringBuilder();
            Iterator iterator = ConfigValues.alertsHoverMessage.iterator();
            while (iterator.hasNext()) {
                hover.append((String) iterator.next());
                if (iterator.hasNext()) {
                    hover.append('\n');
                }
            }
            String hoverText = this.applyAlertPlaceholders(hover.toString(), check, data, checkLabel, extra, type, experimental);
            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(ColorUtil.translateColors(hoverText))));
        }
        return component;
    }

    private static void sendComponent(TextComponent component, Object subscriber) {
        if (subscriber instanceof Player && component != null) {
            ((Player) subscriber).spigot().sendMessage((BaseComponent) component);
        }
    }

    private static String attackingName(PlayerData data) {
        if (data.getReachEntityHelper() == null || data.getReachEntityHelper().getAttackingPlayer() == null) {
            return "None";
        }
        return data.getReachEntityHelper().getAttackingPlayer().getName();
    }

    private static void dispatchCustomCommands(AbstractCheck check, PlayerData data, String checkLabel, String extra, String type, String experimental) {
        AlertMessageBuilder builder = VulcanBootstrap.INSTANCE.getAlertMessageBuilder();
        Iterator iterator = ConfigValues.messagesCustomCommands.iterator();
        while (iterator.hasNext()) {
            String command = builder.applyAlertPlaceholders((String) iterator.next(), check, data, checkLabel, extra, type, experimental);
            ServerCompatibility.dispatchConsoleCommand(command);
        }
    }

    private static void sendAlertWebhook(AbstractCheck check, PlayerData data) {
        sendAlertWebhook(check, data, "");
    }

    private static void sendAlertWebhook(AbstractCheck check, PlayerData data, String extra) {
        try {
            Integer interval = (Integer) ConfigValues.discordAlertIntervalByCheck.get(check.getCheckName());
            if (interval != null && interval.intValue() > 0 && check.getViolationLevel() % interval.intValue() != 0) {
                return;
            }
            DiscordWebhookClient webhook = new DiscordWebhookClient(ConfigValues.discordWebhookAlertsUrl);
            webhook.setAvatarUrl(ConfigValues.discordWebhookAlertsAvatar);
            webhook.setUsername(ConfigValues.discordWebhookAlertsUsername);
            DiscordEmbed embed = new DiscordEmbed();
            embed.setTitle(ConfigValues.discordWebhookAlertsTitle);
            embed.setThumbnailUrl(ConfigValues.discordWebhookAlertsThumbnail
                .replaceAll("%uuid%", data.getPlayer().getUniqueId().toString())
                .replaceAll("%check%", data.getPlayer().getName()));
            embed.setColor(new Color(
                ConfigValues.discordWebhookAlertsColorR,
                ConfigValues.discordWebhookAlertsColorG,
                ConfigValues.discordWebhookAlertsColorB
            ));
            String experimental = check.getCheckInfo() != null && check.getCheckInfo().experimental()
                ? ColorUtil.translateColors(ConfigValues.alertsExperimentalSymbol)
                : "";
            AlertMessageBuilder builder = VulcanBootstrap.INSTANCE.getAlertMessageBuilder();
            String checkLabel = builder.formatCheckLabel(check);
            String type = check.getCheckInfo() == null ? "?" : Character.toString(check.getCheckInfo().type());
            String description = builder.applyAlertPlaceholders(
                ConfigValues.discordWebhookAlertsDescription, check, data, checkLabel, extra, type, experimental
            );
            if (VulcanBootstrap.INSTANCE.isPlaceholderApiPresent()) {
                description = PlaceholderAPI.setPlaceholders(data.getPlayer(), description);
            }
            embed.setDescription(description);
            if (ConfigValues.discordWebhookAlertsDescriptionField) {
                embed.addField("Description", check.getDescription(), true);
            }
            if (ConfigValues.discordWebhookAlertsInformationField) {
                embed.addField("Information", extra == null ? "" : extra, true);
            }
            if (ConfigValues.discordWebhookAlertsServerNameField) {
                embed.addField("Server Name", ConfigValues.settingsServerName, true);
            }
            if (ConfigValues.discordWebhookAlertsClientBrandField) {
                embed.addField("%client-brand%", data.getClientBrand(), true);
            }
            if (ConfigValues.discordWebhookAlertsClientVersionField) {
                embed.addField("Version", data.getUser().getClientVersion().getReleaseName(), true);
            }
            if (ConfigValues.discordWebhookAlertsPingTpsField) {
                embed.addField("Connection", data.getCheckState().getPing() + "ms | "
                    + TrigUtil.formatDecimal(ServerCompatibility.getTicksPerSecond()), true);
            }
            if (ConfigValues.discordWebhookAlertsLocationField) {
                embed.addField("Location", "(" + data.getPlayer().getWorld().getName()
                    + ", " + data.getBlockBreakTracker().getLocationX()
                    + ", " + data.getBlockBreakTracker().getLocationY()
                    + ", " + data.getBlockBreakTracker().getLocationZ() + ")", true);
            }
            webhook.addEmbed(embed);
            if (ConfigValues.discordWebhookAlertsContent != null && !ConfigValues.discordWebhookAlertsContent.equals("")) {
                webhook.setContent(builder.applyAlertPlaceholders(
                    ConfigValues.discordWebhookAlertsContent, check, data, checkLabel, extra, type, experimental
                ));
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

    private void sendBungeePluginMessage(Player player, String channel) {
        if (!ConfigValues.settingsPluginMessaging || player == null || channel == null) {
            return;
        }
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        try {
            output.writeUTF("Forward");
            output.writeUTF(channel);
        } catch (Exception ignored) {}
        player.sendPluginMessage((Plugin) VulcanBootstrap.INSTANCE.getVulcanPlugin(), "BungeeCord", output.toByteArray());
    }
}
