package me.frep.vulcan.spigot;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import ac.vulcan.anticheat.SchedulerAdapter;
import ac.vulcan.anticheat.UniversalScheduler;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import me.frep.vulcan.api.VulcanAPI$Factory;
import me.frep.vulcan.spigot.api.VulcanSpigotAPI;
import me.frep.vulcan.spigot.bukkit.Metrics;
import me.frep.vulcan.spigot.check.manager.CheckManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;


public final class VulcanBootstrap {
    public static final VulcanBootstrap INSTANCE = new VulcanBootstrap();
    private VulcanPlugin plugin;
    private long startTime;
    private final String licenseSpigot = LicenseConfig.spigot();
    private final String nonce = LicenseConfig.nonce();
    private final String resource = LicenseConfig.resource();
    private final Logger logger = Bukkit.getLogger();
    private boolean viaVersionOrProtocolSupportPresent;
    private boolean floodgateV2Present;
    private boolean placeholderApiPresent;
    private boolean mcMmoPresent;
    private boolean geyserPresent;
    private boolean libsDisguisesPresent;
    private boolean mythicMobsPresent;
    private boolean gSitPresent;
    private boolean mythicMobsV5;
    private boolean shutdown;
    private final Map entityIdMap = new HashMap();
    private final KeepAliveRunnable keepAliveRunnable = new KeepAliveRunnable();
    private final TickRunnable tickRunnable = new TickRunnable();
    private final PluginManager pluginManager = Bukkit.getServer().getPluginManager();
    private final PunishmentManager punishmentManager = new PunishmentManager();
    private final OutgoingPacketHandler outgoingPacketHandler = new OutgoingPacketHandler();
    private final HookManager hookManager = new HookManager();
    private final AlertMessageBuilder alertMessageBuilder = new AlertMessageBuilder();
    private final AlertPlaceholders alertPlaceholders = new AlertPlaceholders();
    private final ExecutorService alertsExecutor = Executors.newSingleThreadExecutor();
    private final ExecutorService joinExecutor = Executors.newSingleThreadExecutor();
    private final ExecutorService violationExecutor = Executors.newSingleThreadExecutor();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static SchedulerAdapter schedulerAdapter;
    private final Map clientBrandByUuid = new HashMap();
    private int discordWebhookCooldownCount = 0;
    private VulcanBootstrap() {
    }

    public void enable(VulcanPlugin vulcanPlugin) {
        if (vulcanPlugin == null) {
            throw new AssertionError("plugin is null while starting Vulcan!");
        }
        this.plugin = vulcanPlugin;
        schedulerAdapter = UniversalScheduler.createSchedulerAdapter(vulcanPlugin);
        vulcanPlugin.saveDefaultConfig();
        ConfigValues.loadMainConfig();
        ConfigValues.reloadMainConfig();
        StatsManager.loadStatsFile();
        CheckManager.registerChecks();
        this.keepAliveRunnable.start();
        this.tickRunnable.start();
        this.logInfo("Enabling Vulcan... Server Version: "
            + ServerCompatibility.getServerVersion().getReleaseName() + " detected!");
        PacketEvents.getAPI().getEventManager().registerListener(
            (PacketListener) new PacketListenerImpl(),
            PacketListenerPriority.HIGHEST
        );

        this.viaVersionOrProtocolSupportPresent = this.hookPresent("ViaVersion") || this.hookPresent("ProtocolSupport");
        if (this.hookPresent("floodgate")) {
            this.floodgateV2Present = true;
            this.logInfo("Floodgate 2.0 found. Enabling hook!");
        }
        if (this.hookPresent("Geyser-Spigot") || this.hookPresent("Geyser-Bukkit")) {
            this.geyserPresent = true;
        }
        if (this.hookPresent("mcMMO")) {
            this.mcMmoPresent = true;
            this.logInfo("mcMMO found. Enabling hook!");
        }
        if (this.hookPresent("LibsDisguises")) {
            this.libsDisguisesPresent = true;
            this.logInfo("LibsDisguises found. Enabling hook!");
        }
        Plugin mythicMobs = this.pluginManager.getPlugin("MythicMobs");
        if (mythicMobs != null) {
            this.mythicMobsPresent = true;
            this.mythicMobsV5 = mythicMobs.getDescription().getVersion().startsWith("5");
            this.logInfo("MythicMobs found. Enabling hook!");
        }
        if (this.hookPresent("GSit")) {
            this.gSitPresent = true;
        }
        if (this.hookPresent("CrackShot")) {
            this.logInfo("CrackShot found. Enabling hook!");
        }
        if (this.hookPresent("DiscordBotAPI")) {
            this.logInfo("DiscordBotAPI found. Enabling hook!");
        }

        VulcanAPI$Factory.setApi(new VulcanSpigotAPI());
        this.bindCommand("vulcan", new MainCommand(), new me.frep.vulcan.spigot.TabCompleter());
        this.bindCommand("logs", new LogsCommand(), null);
        this.bindCommand("alerts", new AlertsCommand(), null);
        this.bindCommand("jday", new MainCommand(), null);
        this.bindCommand("punishlogs", new LogsCommand(), null);
        this.bindCommand("checks", new ChecksCommand(), null);
        this.bindCommand("profile", new ProfileCommand(), null);
        vulcanPlugin.getServer().getMessenger().registerOutgoingPluginChannel((org.bukkit.plugin.Plugin) vulcanPlugin, "BungeeCord");

        if (ConfigValues.bstatsEnabled && !ServerCompatibility.isFolia()) {
            new Metrics((Plugin) vulcanPlugin, 10297);
            this.logInfo("bStats enabled!");
        }
        this.startTime = System.currentTimeMillis();
        this.pluginManager.registerEvents((Listener) new BukkitBridgeListener(), vulcanPlugin);
        this.pluginManager.registerEvents((Listener) new BukkitEventListener(), vulcanPlugin);
        if (this.mcMmoPresent && ConfigValues.settingsHookMcmmo) {
            this.pluginManager.registerEvents((Listener) new McMMOListener(), vulcanPlugin);
            this.logInfo("Enabled mcMMO listener!");
        }
        if (this.mythicMobsPresent && ConfigValues.settingsHookMythicmobs) {
            this.pluginManager.registerEvents((Listener) new EntityDamageListener(), vulcanPlugin);
            this.logInfo("Enabled MythicMobs listener!");
        }
        if (ServerCompatibility.isServer1_11OrNewer()) {
            this.pluginManager.registerEvents((Listener) new TotemResurrectListener(), vulcanPlugin);
        }
        if (!ServerCompatibility.isServerOlderThan1_13()) {
            this.pluginManager.registerEvents((Listener) new RiptideListener(), vulcanPlugin);
        }
        System.setProperty("packetevents.Version.handlePingsAsInvAcknowledgements", "true");
        if (this.hookPresent("PlaceholderAPI")) {
            this.placeholderApiPresent = true;
            new PlaceholderHook().register();
            this.logInfo("PlaceholderAPI found. Enabling hook!");
        }
    }

    public void disable(VulcanPlugin vulcanPlugin) {
        if (vulcanPlugin == null) {
            throw new AssertionError("plugin is null while shutting down Vulcan!");
        }
        this.plugin = vulcanPlugin;
        this.keepAliveRunnable.stop();
        this.tickRunnable.stop();
        this.alertsExecutor.shutdownNow();
        this.violationExecutor.shutdownNow();
        this.joinExecutor.shutdownNow();
        if (schedulerAdapter != null) {
            schedulerAdapter.cancelTasks(vulcanPlugin);
        }
    }

    public void reload() {
        ConfigValues.reloadMainConfig();
        CheckManager.registerChecks();
        CheckRegistryLoader.clearCheckSettings();
        CheckRegistryLoader.loadCheckSettings();
        this.plugin.reloadConfig();
    }

    public void logInfo(String message) {
        Bukkit.getLogger().log(Level.INFO, "[Vulcan] " + message);
    }

    private boolean hookPresent(String pluginName) {
        return this.pluginManager.getPlugin(pluginName) != null;
    }

    private void bindCommand(String name, CommandExecutor executor, org.bukkit.command.TabCompleter tabCompleter) {
        org.bukkit.command.PluginCommand command = this.plugin.getCommand(name);
        if (command == null) {
            return;
        }
        command.setExecutor(executor);
        if (tabCompleter != null) {
            command.setTabCompleter(tabCompleter);
        }
    }

    public long getStartTime() {
        return this.startTime;
    }

    public String getLicenseSpigot() {
        return this.licenseSpigot;
    }

    public String getNonce() {
        return this.nonce;
    }

    public String getResource() {
        return this.resource;
    }

    public Logger getLogger() {
        return this.logger;
    }

    public boolean isFolia() {
        return ServerCompatibility.isFolia();
    }

    public boolean isFloodgateV1Present() {
        return this.hookPresent("Floodgate");
    }

    public boolean isFloodgateV2Present() {
        return this.floodgateV2Present;
    }

    public boolean isPlaceholderApiPresent() {
        return this.placeholderApiPresent;
    }

    public boolean isViaVersionOrProtocolSupportPresent() {
        return this.viaVersionOrProtocolSupportPresent;
    }

    public boolean isMcMmoPresent() {
        return this.mcMmoPresent;
    }

    public boolean isGeyserPresent() {
        return this.geyserPresent;
    }

    public boolean isLibsDisguisesPresent() {
        return this.libsDisguisesPresent;
    }

    public boolean isMythicMobsPresent() {
        return this.mythicMobsPresent;
    }

    public boolean isMythicMobsV5() {
        return this.mythicMobsV5;
    }

    public boolean isGSitPresent() {
        return this.gSitPresent;
    }

    public boolean isShutdown() {
        return this.shutdown;
    }

    public Map getEntityIdMap() {
        return this.entityIdMap;
    }

    public KeepAliveRunnable getKeepAliveRunnable() {
        return this.keepAliveRunnable;
    }

    public TickRunnable getTickRunnable() {
        return this.tickRunnable;
    }

    public PluginManager getPluginManager() {
        return this.pluginManager;
    }

    public PunishmentManager getPunishmentManager() {
        return this.punishmentManager;
    }

    public OutgoingPacketHandler getOutgoingPacketHandler() {
        return this.outgoingPacketHandler;
    }

    public HookManager getHookManager() {
        return this.hookManager;
    }

    public AlertMessageBuilder getAlertMessageBuilder() {
        return this.alertMessageBuilder;
    }

    public AlertPlaceholders getAlertPlaceholders() {
        return this.alertPlaceholders;
    }

    public ExecutorService getAlertsExecutor() {
        return this.alertsExecutor;
    }

    public ExecutorService getJoinExecutor() {
        return this.joinExecutor;
    }

    public ExecutorService getViolationExecutor() {
        return this.violationExecutor;
    }

    public ExecutorService getExecutorService() {
        return this.executorService;
    }

    public Map getClientBrandByUuid() {
        return this.clientBrandByUuid;
    }

    public VulcanPlugin getVulcanPlugin() {
        return this.plugin;
    }

    public void setShutdown(boolean shutdown) {
        this.shutdown = shutdown;
    }

    public void setDiscordWebhookCooldownCount(int discordWebhookCooldownCount) {
        this.discordWebhookCooldownCount = discordWebhookCooldownCount;
    }

    public static SchedulerAdapter getSchedulerAdapter() {
        return schedulerAdapter;
    }

    public int getDiscordWebhookCooldownCount() {
        return this.discordWebhookCooldownCount;
    }

}
