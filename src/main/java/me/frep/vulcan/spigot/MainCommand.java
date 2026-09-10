package me.frep.vulcan.spigot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import me.frep.vulcan.spigot.check.AbstractCheck;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

public class MainCommand extends CommandBase implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if ("jday".equalsIgnoreCase(command.getName())) {
            if (!sender.hasPermission("vulcan.jday")) {
                this.sendFormattedMessage(sender, ConfigValues.messagesNoPermission);
                return true;
            }
            BanWaveManager.executeBanWave();
            return true;
        }

        if (!sender.hasPermission("vulcan.command") && !sender.hasPermission("vulcan.admin")) {
            this.sendFormattedMessage(sender, ConfigValues.messagesNoPermission);
            return true;
        }

        if (args.length == 0 || "help".equalsIgnoreCase(args[0])) {
            this.sendHelp(sender);
            return true;
        }

        String sub = args[0].toLowerCase();
        if ("reload".equals(sub)) {
            VulcanBootstrap.INSTANCE.reload();
            this.sendFormattedMessage(sender, ConfigValues.messagesReloadSuccess);
            return true;
        }
        if ("profile".equals(sub)) {
            return this.handleProfile(sender, args);
        }
        if ("ban".equals(sub)) {
            return this.handleBan(sender, args);
        }
        if ("disablecheck".equals(sub)) {
            return this.handleDisableCheck(sender, args);
        }
        if ("violations".equals(sub)) {
            return this.handleViolations(sender, args);
        }
        if ("cps".equals(sub)) {
            return this.handleCps(sender, args);
        }
        if ("kb".equals(sub) || "knockback".equals(sub)) {
            return this.handleKnockback(sender, args);
        }
        if ("connection".equals(sub)) {
            return this.handleConnection(sender, args);
        }
        if ("freeze".equals(sub)) {
            return this.handleFreeze(sender, args);
        }
        if ("rotate".equals(sub)) {
            return this.handleRotate(sender, args);
        }
        if ("shuffle".equals(sub)) {
            return this.handleShuffle(sender, args);
        }
        if ("top".equals(sub)) {
            return this.handleTop(sender);
        }
        if ("testalert".equals(sub)) {
            VulcanBootstrap.INSTANCE.getAlertMessageBuilder().sendAlertMessage(ConfigValues.messagesSentTestAlert);
            this.sendFormattedMessage(sender, ConfigValues.messagesSentTestAlert);
            return true;
        }

        this.sendFormattedMessage(sender, ConfigValues.messagesUnknownCommand);
        return true;
    }

    private void sendHelp(CommandSender sender) {
        List help = ConfigValues.commandsHelp;
        if (help == null) {
            return;
        }
        String version = VulcanBootstrap.INSTANCE.getVulcanPlugin() == null
            ? ""
            : VulcanBootstrap.INSTANCE.getVulcanPlugin().getDescription().getVersion();
        for (Object line : help) {
            this.sendFormattedMessage(sender, String.valueOf(line).replace("%version%", version));
        }
    }

    private PlayerData resolvePlayerData(CommandSender sender, String[] args, int nameIndex, String syntax) {
        if (args.length <= nameIndex) {
            this.sendFormattedMessage(sender, syntax);
            return null;
        }
        Player target = Bukkit.getPlayerExact(args[nameIndex]);
        if (target == null) {
            this.sendFormattedMessage(sender, ConfigValues.messagesInvalidTarget);
            return null;
        }
        PlayerData playerData = VulcanBootstrap.INSTANCE.getHookManager().getPlayerData(target);
        if (playerData == null) {
            this.sendFormattedMessage(sender, ConfigValues.messagesInvalidTarget);
        }
        return playerData;
    }

    private boolean handleProfile(CommandSender sender, String[] args) {
        PlayerData playerData = this.resolvePlayerData(sender, args, 1, ConfigValues.messagesProfileCommandSyntax);
        if (playerData == null) {
            return true;
        }
        this.sendProfile(sender, playerData);
        return true;
    }

    void sendProfile(CommandSender sender, PlayerData playerData) {
        Player player = playerData.getPlayer();
        String version = playerData.getUser() == null ? "Unknown" : playerData.getUser().getClientVersion().getReleaseName();
        String nearbyBlocks = playerData.getBlockBreakTracker().getNearbyMaterials() == null
            ? "None"
            : playerData.getBlockBreakTracker().getNearbyMaterials().toString();
        String nearbyEntities = String.valueOf(player.getNearbyEntities(4.0, 4.0, 4.0).size());
        StringBuilder potions = new StringBuilder();
        for (PotionEffect effect : player.getActivePotionEffects()) {
            if (potions.length() > 0) {
                potions.append(", ");
            }
            potions.append(effect.getType().getName()).append(" ").append(effect.getAmplifier() + 1);
        }
        List lines = ConfigValues.commandsProfile;
        if (lines == null) {
            return;
        }
        for (Object raw : lines) {
            String line = String.valueOf(raw)
                .replace("%player%", playerData.getUsername())
                .replace("%uuid%", player.getUniqueId().toString())
                .replace("%client-version%", version)
                .replace("%client-brand%", String.valueOf(playerData.getClientBrand()))
                .replace("%sensitivity%", String.valueOf((int) (playerData.getActionTracker().getSensitivity() * 100.0)))
                .replace("%cps%", String.valueOf(playerData.getClickTracker().getCps()))
                .replace("%move-speed%", String.valueOf(playerData.getStatusTracker().getWalkSpeed()))
                .replace("%total-violations%", Integer.toString(playerData.getTotalViolations()))
                .replace("%combat-violations%", Integer.toString(playerData.getCombatViolations()))
                .replace("%movement-violations%", Integer.toString(playerData.getMovementViolations()))
                .replace("%player-violations%", Integer.toString(playerData.getPlayerViolations()))
                .replace("%nearby-blocks%", nearbyBlocks)
                .replace("%nearby-entities%", nearbyEntities)
                .replace("%potion-effects%", potions.length() == 0 ? "None" : potions.toString());
            this.sendFormattedMessage(sender, line);
        }
    }

    private boolean handleBan(CommandSender sender, String[] args) {
        PlayerData playerData = this.resolvePlayerData(sender, args, 1, ConfigValues.messagesBanCommandSyntax);
        if (playerData == null) {
            return true;
        }
        List commands = ConfigValues.commandsBanCommands;
        if (commands != null) {
            for (Object raw : commands) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.valueOf(raw).replace("%player%", playerData.getUsername()));
            }
        }
        List broadcast = ConfigValues.commandsBanBroadcast;
        if (broadcast != null) {
            for (Object raw : broadcast) {
                Bukkit.broadcastMessage(ColorUtil.translateColors(String.valueOf(raw).replace("%player%", playerData.getUsername())));
            }
        }
        return true;
    }

    private boolean handleDisableCheck(CommandSender sender, String[] args) {
        if (args.length < 2) {
            this.sendFormattedMessage(sender, ConfigValues.messagesDisableCheckCommandSyntax);
            return true;
        }
        String checkName = args[1];
        if (ConfigValues.checkEnabledByName == null || !ConfigValues.checkEnabledByName.containsKey(checkName)) {
            this.sendFormattedMessage(sender, ConfigValues.messagesInvalidCheck);
            return true;
        }
        ConfigValues.checkEnabledByName.put(checkName, Boolean.FALSE);
        if (ConfigValues.enabledByCheck != null) {
            ConfigValues.enabledByCheck.put(checkName, Boolean.FALSE);
        }
        this.sendFormattedMessage(sender, ConfigValues.messagesDisabledCheck2.replace("%check%", checkName));
        return true;
    }

    private boolean handleViolations(CommandSender sender, String[] args) {
        PlayerData playerData = this.resolvePlayerData(sender, args, 1, ConfigValues.messagesViolationsCommandSyntax);
        if (playerData == null) {
            return true;
        }
        this.sendFormattedMessage(sender, "&7Checks for &f" + playerData.getUsername() + "&7:");
        List checks = playerData.getChecks();
        if (checks == null) {
            return true;
        }
        for (Object raw : checks) {
            if (!(raw instanceof AbstractCheck)) {
                continue;
            }
            AbstractCheck check = (AbstractCheck) raw;
            this.sendFormattedMessage(sender, "&8» &7" + check.getCheckName() + " &8- &c" + check.getViolationLevel() + " VL");
        }
        return true;
    }

    private boolean handleCps(CommandSender sender, String[] args) {
        PlayerData playerData = this.resolvePlayerData(sender, args, 1, ConfigValues.messagesCpsCommandSyntax);
        if (playerData == null) {
            return true;
        }
        this.sendFormattedMessage(sender, ConfigValues.commandsCps
            .replace("%player%", playerData.getUsername())
            .replace("%cps%", String.valueOf(playerData.getClickTracker().getCps())));
        return true;
    }

    private boolean handleKnockback(CommandSender sender, String[] args) {
        PlayerData playerData = this.resolvePlayerData(sender, args, 1, ConfigValues.messagesKbCommandSyntax);
        if (playerData == null) {
            return true;
        }
        Player player = playerData.getPlayer();
        player.setVelocity(new Vector(0.0, 0.4, 0.0));
        this.sendFormattedMessage(sender, ConfigValues.messagesKbTestSuccess.replace("%player%", playerData.getUsername()));
        return true;
    }

    private boolean handleConnection(CommandSender sender, String[] args) {
        PlayerData playerData = this.resolvePlayerData(sender, args, 1, ConfigValues.messagesConnectionCommandSyntax);
        if (playerData == null) {
            return true;
        }
        int keepAlivePing = CollisionUtil.getPlayerPing(playerData.getPlayer());
        List lines = ConfigValues.commandsConnection;
        if (lines == null) {
            return true;
        }
        for (Object raw : lines) {
            this.sendFormattedMessage(sender, String.valueOf(raw)
                .replace("%player%", playerData.getUsername())
                .replace("%transaction-ping%", Integer.toString(keepAlivePing))
                .replace("%keepalive-ping%", Integer.toString(keepAlivePing))
                .replace("%last-replied-transaction%", Long.toString(System.currentTimeMillis() - playerData.getJoinTime()))
                .replace("%queued-transactions%", Integer.toString(playerData.getPacketHandler().getCheckSummaries().size()))
                .replace("%flying-delay%", Long.toString(playerData.getClickTracker().getLastFlyingPacketTime() == 0L
                    ? 0L
                    : System.currentTimeMillis() - playerData.getClickTracker().getLastFlyingPacketTime())));
        }
        return true;
    }

    private boolean handleFreeze(CommandSender sender, String[] args) {
        PlayerData playerData = this.resolvePlayerData(sender, args, 1, ConfigValues.messagesFreezeCommandSyntax);
        if (playerData == null) {
            return true;
        }
        boolean frozen = !playerData.getBlockBreakTracker().isFrozen();
        playerData.getBlockBreakTracker().setFrozen(frozen);
        String message = frozen ? ConfigValues.messagesFroze : ConfigValues.messagesUnfroze;
        this.sendFormattedMessage(sender, message.replace("%player%", playerData.getUsername()));
        String broadcast = frozen ? ConfigValues.messagesFrozeStaffBroadcast : ConfigValues.messagesUnfrozeStaffBroadcast;
        if (broadcast != null) {
            Bukkit.broadcastMessage(ColorUtil.translateColors(
                broadcast.replace("%player%", playerData.getUsername()).replace("%staff%", sender.getName())
            ));
        }
        return true;
    }

    private boolean handleRotate(CommandSender sender, String[] args) {
        PlayerData playerData = this.resolvePlayerData(sender, args, 1, ConfigValues.messagesRotateCommandSyntax);
        if (playerData == null) {
            return true;
        }
        CollisionUtil.applyRandomRotation(playerData);
        this.sendFormattedMessage(sender, ConfigValues.messagesRandomlyRotated.replace("%player%", playerData.getUsername()));
        return true;
    }

    private boolean handleShuffle(CommandSender sender, String[] args) {
        PlayerData playerData = this.resolvePlayerData(sender, args, 1, ConfigValues.messagesShuffleCommandSyntax);
        if (playerData == null) {
            return true;
        }
        CollisionUtil.shuffleHotbar(playerData.getPlayer());
        this.sendFormattedMessage(sender, ConfigValues.messagesShuffledHotbar.replace("%player%", playerData.getUsername()));
        return true;
    }

    private boolean handleTop(CommandSender sender) {
        List lines = ConfigValues.commandsTop;
        if (lines == null) {
            return true;
        }
        Collection all = VulcanBootstrap.INSTANCE.getHookManager().getAllPlayerData();
        List total = this.sortedCopy(all, Comparator.comparingInt(PlayerData::getTotalViolations).reversed());
        List combat = this.sortedCopy(all, Comparator.comparingInt(PlayerData::getCombatViolations).reversed());
        List movement = this.sortedCopy(all, Comparator.comparingInt(PlayerData::getMovementViolations).reversed());
        List player = this.sortedCopy(all, Comparator.comparingInt(PlayerData::getPlayerViolations).reversed());
        for (Object raw : lines) {
            String line = String.valueOf(raw);
            line = this.applyTopPlaceholders(line, "total", total, p -> ((PlayerData) p).getTotalViolations());
            line = this.applyTopPlaceholders(line, "combat", combat, p -> ((PlayerData) p).getCombatViolations());
            line = this.applyTopPlaceholders(line, "movement", movement, p -> ((PlayerData) p).getMovementViolations());
            line = this.applyTopPlaceholders(line, "player", player, p -> ((PlayerData) p).getPlayerViolations());
            this.sendFormattedMessage(sender, line);
        }
        return true;
    }

    private List sortedCopy(Collection all, Comparator comparator) {
        List copy = new ArrayList();
        if (all != null) {
            copy.addAll(all);
        }
        copy.sort(comparator);
        return copy;
    }

    private String applyTopPlaceholders(String line, String category, List ranked, java.util.function.ToIntFunction scorer) {
        for (int index = 1; index <= 5; ++index) {
            String name = "None";
            String violations = "0";
            if (index <= ranked.size()) {
                PlayerData playerData = (PlayerData) ranked.get(index - 1);
                name = playerData.getUsername();
                violations = Integer.toString(scorer.applyAsInt(playerData));
            }
            line = line.replace("%" + category + "-" + index + "-name%", name)
                .replace("%" + category + "-" + index + "-violations%", violations);
        }
        return line;
    }
}
