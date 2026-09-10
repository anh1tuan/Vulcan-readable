package me.frep.vulcan.spigot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Color;


public final class ConfigValues {
    public static Map punishmentCommandsByCheck;
    public static Map enabledByCheck;
    public static Map maxViolationsByCheck;
    public static Map alertIntervalByCheck;
    public static Map minimumVlToNotifyByCheck;
    public static Map punishmentCommandListByCheck;
    public static Map punishableByCheck;
    public static Map punishmentCommandsBySeverity;
    public static Map maximumPingByCheck;
    public static Map minimumTpsByCheck;
    public static Map checkEnabledByName;
    public static Map maxBufferByCheck;
    public static Map bufferDecayByCheck;
    public static Map bufferMultipleByCheck;
    public static Map hotbarShuffleEnabledByCheck;
    public static Map hotbarShuffleMinimumVlByCheck;
    public static Map hotbarShuffleIntervalByCheck;
    public static Map randomRotationEnabledByCheck;
    public static Map randomRotationMinimumVlByCheck;
    public static Map randomRotationIntervalByCheck;
    public static Map discordAlertIntervalByCheck;
    public static Map maxVlBeforeSetbackByCheck;
    public static String resetCommandSyntax;
    public static String messagesNoPermission;
    public static String alertsToggledOnMessage;
    public static String alertsToggledOffMessage;
    public static String messagesCantExecuteFromConsole;
    public static String punishmentsMessage;
    public static String messagesBanCommandSyntax;
    public static String messagesReloadSuccess;
    public static String messagesKbCommandSyntax;
    public static String messagesInvalidTarget;
    public static String messagesKbTestSuccess;
    public static String messagesJdayCommandSyntax;
    public static String messagesJdayNoPendingBans;
    public static String messagesJdayAddedToList;
    public static String messagesResetCommand;
    public static String messagesProfileCommandSyntax;
    public static String clientBrandAlertMessage;
    public static String alertsFormat;
    public static String violationResetMessage;
    public static String alertsConsoleFormat;
    public static String logFileAlertMessage;
    public static String logFilePunishmentMessage;
    public static String alertsSeverityColors1;
    public static String alertsSeverityColors2;
    public static String alertsSeverityColors3;
    public static String alertsSeverityColors4;
    public static String alertsSeverityColors5;
    public static String commandsCps;
    public static String noNextPage;
    public static String messagesNoPreviousPage;
    public static String messagesEnabledAllChecks;
    public static String disabledAllChecks;
    public static String enabledAllPunishments;
    public static String disabledAllPunishments;
    public static String messagesDisabledCheck;
    public static String messagesDisabledCheck2;
    public static String enabledPunishment;
    public static String messagesDisabledPunishment;
    public static String enabledHotbarShuffle;
    public static String messagesDisabledHotbarShuffle;
    public static String enabledRandomRotation;
    public static String messagesDisabledRandomRotation;
    public static String messagesEnabledBroadcastPunishment;
    public static String disabledBroadcastPunishment;
    public static String mustBeAnInteger;
    public static String messagesMustBePositive;
    public static String setMaxBuffer;
    public static String messagesSetHotbarShuffleMinimumViolations;
    public static String setHotbarInterval;
    public static String setRandomRotationMinimumViolations;
    public static String setRandomRotationInterval;
    public static String setAlertInterval;
    public static String mustBeNumber;
    public static String setBufferMultiple;
    public static String messagesSetBufferDecay;
    public static String setMaxViolations;
    public static String setMinimumViolationsToAlert;
    public static String setMaximumPing;
    public static String setMinimumTps;
    public static String removedPunishmentCommand;
    public static String invalidIndexNumber;
    public static String stoppedEditingPunishmentCommands;
    public static String addedPunishmentCommand;
    public static String enterPunishmentCommand;
    public static String removePunishmentCommand;
    public static String messagesDisableCheckCommandSyntax;
    public static String messagesInvalidCheck;
    public static String messagesCheckRemoved;
    public static String messagesViolationsCommandSyntax;
    public static String messagesNoLogs;
    public static String messagesCpsCommandSyntax;
    public static String messagesConnectionCommandSyntax;
    public static String messagesLogsCommandSyntax;
    public static String messagesNoLogsFile;
    public static String messagesLogsCommandNoLogs;
    public static String messagesNoPage;
    public static String commandsLogsColor;
    public static String commandsLogsHeaderFooter;
    public static String messagesUpdateAvailable;
    public static String messagesLatestVersion;
    public static String settingsServerName;
    public static String messagesFrozen;
    public static String messagesLoggedOutWhileFrozen;
    public static String messagesFroze;
    public static String messagesUnfroze;
    public static String messagesFreezeCommandSyntax;
    public static String clientBrandKickMessage;
    public static String clientBrandAlertsUnallowedClientBrandKickMessage;
    public static String clientBrandAlertsConsoleMessage;
    public static String settingsIgnoreGeyserPrefix;
    public static String messagesShuffleCommandSyntax;
    public static String messagesShuffledHotbar;
    public static String messagesRandomlyRotated;
    public static String messagesRotateCommandSyntax;
    public static String ghostBlocksFixMessage;
    public static String ghostBlocksFixConsoleMessage;
    public static String ghostBlocksFixStaffMessage;
    public static String settingsTopCommandUnresolved;
    public static String alertsExperimentalSymbol;
    public static String messagesResetCommandSyntax;
    public static String messagesViolationsReset;
    public static String messagesUnknownCommand;
    public static String alertsSeverityColors6;
    public static String alertsSeverityColors7;
    public static String alertsSeverityColors8;
    public static String alertsSeverityColors9;
    public static String alertsSeverityColors10;
    public static String messagesFrozeStaffBroadcast;
    public static String messagesUnfrozeStaffBroadcast;
    public static String ghostBlocksFixConsoleMessage2;
    public static String commandsJdayListHeaderFooter;
    public static String commandsJdayListFormat;
    public static String messagesRemovedFromJday;
    public static String messagesJdayRemoveSyntax;
    public static String discordWebhookAlertsUrl;
    public static String discordWebhookAlertsAvatar;
    public static String discordWebhookAlertsUsername;
    public static String discordWebhookAlertsTitle;
    public static String discordWebhookAlertsDescription;
    public static String discordWebhookPunishmentsUrl;
    public static String discordWebhookPunishmentsAvatar;
    public static String discordWebhookPunishmentsUsername;
    public static String discordWebhookPunishmentsTitle;
    public static String discordWebhookPunishmentsDescription;
    public static String commandsPunishlogsHeaderFooter;
    public static String messagesPunishlogsSyntax;
    public static String connectionKeepaliveKickMessage;
    public static String connectionKeepaliveStaffAlertMessage;
    public static String connectionKeepaliveConsoleMessage;
    public static String connectionMaxPingKickMessage;
    public static String connectionMaxPingStaffAlertMessage;
    public static String connectionMaxPingConsoleMessage;
    public static String discordWebhookAlertsThumbnail;
    public static String discordWebhookPunishmentsThumbnail;
    public static String discordWebhookAlertsContent;
    public static String discordWebhookPunishmentsContent;
    public static String messagesSentTestAlert;
    public static String unloadedChunksMessage;
    public static String messagesDescriptionCommandSyntax;
    public static String description;
    public static String connectionInvalidKickMessage;
    public static String connectionInvalidStaffAlertMessage;
    public static String connectionInvalidConsoleAlertMessage;
    public static String connectionOutOfOrderKickMessage;
    public static String connectionOutOfOrderStaffAlertMessage;
    public static String connectionOutOfOrderConsoleAlertMessage;
    public static String connectionNoResponseKickMessage;
    public static String connectionNoResponseStaffAlertMessage;
    public static String connectionNoResponseConsoleAlertMessage;
    public static String connectionMaxSizeStaffAlertMessage;
    public static String connectionMaxSizeConsoleAlertMessage;
    public static String connectionMaxSizeKickMessage;
    public static long joinCheckWaitTime;
    public static long checksPlayerTimerDMaxBalance;
    public static long judgementDaysCooldown;
    public static long connectionKeepaliveMaxDelay;
    public static long cancelHitsDuration;
    public static boolean settingsToggleAlertsOnJoin;
    public static boolean violationResetMessageEnabled;
    public static boolean violationResetEnabled;
    public static boolean printToConsole;
    public static boolean logFileEnabled;
    public static boolean punishmentFileEnabled;
    public static boolean settingsPerCheckSeverities;
    public static boolean clientBrandAlertsConsole;
    public static boolean entityCollisionEnabled;
    public static boolean cinematicEnabled;
    public static boolean settingsDebug;
    public static boolean ghostBlocksFixEnabled;
    public static boolean ghostBlocksFixStaffMessageEnabled;
    public static boolean settingsAsyncAlerts;
    public static boolean apiExportEnabled;
    public static boolean settingsEnableApi;
    public static boolean settingsIgnoreGeyserUuids;
    public static boolean settingsIgnoreGeyserPrefixes;
    public static boolean ghostBlocksFixSetback;
    public static boolean settingsIgnoreFlagsGeyserClientBrand;
    public static boolean clientBrandAlertsResolve;
    public static boolean ghostBlocksFixMessageEnabled;
    public static boolean punishmentStatisticsBroadcastEnabled;
    public static boolean judgementDaysRunAtInterval;
    public static boolean settingsIgnoreIfInJudgementDay;
    public static boolean checksPlayerGhosthandACancel;
    public static boolean settingsIgnoreVivecraft;
    public static boolean checksMovementBoatflyAKickOut;
    public static boolean checksMovementBoatflyBKickOut;
    public static boolean checksMovementEntityflightAKickOut;
    public static boolean checksMovementEntityspeedAKickOut;
    public static boolean checksMovementNosaddleAKickOut;
    public static boolean settingsLenientScaffolding;
    public static boolean checksMovementFlightAIgnoreGhostBlocks;
    public static boolean checksMovementFlightCIgnoreGhostBlocks;
    public static boolean checksMovementFlightDIgnoreGhostBlocks;
    public static boolean ghostBlocksFixUpdateBlocks;
    public static boolean settingsIgnoreFloodgate;
    public static boolean bstatsEnabled;
    public static boolean ghostBlocksFixIgnore117;
    public static boolean checksMovementFlightEIgnoreGhostBlocks;
    public static boolean ghostBlocksFixPrintToConsole;
    public static boolean punishBroadcastEnabled;
    public static boolean hookPlaceholderApi;
    public static boolean discordWebhookAlertsEnabled;
    public static boolean discordWebhookAlertsServerNameField;
    public static boolean discordWebhookAlertsLocationField;
    public static boolean discordWebhookAlertsClientBrandField;
    public static boolean discordWebhookAlertsInformationField;
    public static boolean discordWebhookAlertsDescriptionField;
    public static boolean discordWebhookAlertsClientVersionField;
    public static boolean discordWebhookPunishmentsEnabled;
    public static boolean discordWebhookAlertsPingTpsField;
    public static boolean connectionKeepaliveKickEnabled;
    public static boolean ghostWaterFixEnabled;
    public static boolean connectionMaxPingKickEnabled;
    public static boolean hookProtocolLib;
    public static boolean discordWebhookPunishmentsDescriptionField;
    public static boolean discordWebhookPunishmentsServerNameField;
    public static boolean discordWebhookPunishmentsClientBrandField;
    public static boolean discordWebhookPunishmentsClientVersionField;
    public static boolean discordWebhookPunishmentsPingTpsField;
    public static boolean discordWebhookPunishmentsLocationField;
    public static boolean checksPlayerAirplaceACancel;
    public static boolean settingsIgnoreAlertsOverMaxViolations;
    public static boolean clientBrandAlertsWhitelistEnabled;
    public static boolean settingsIncompatabilityManager;
    public static boolean settingsPluginMessaging;
    public static boolean settingsHookBrewery;
    public static boolean settingsHookMcmmo;
    public static boolean settingsHookMythicmobs;
    public static boolean settingsHookGsit;
    public static boolean dontSetbackLowerPositions;
    public static boolean settingsVelocityWorldBorder;
    public static boolean unloadedChunksSetbackEnabled;
    public static boolean entityCramFixEnabled;
    public static boolean discordWebhookCooldown;
    public static boolean ghostBlocksFixIgnoreLegacyVia;
    public static boolean connectionDebugMessages;
    public static boolean otherAnticheatSupport;
    public static boolean connectionIgnoreIdsOverZero;
    public static boolean settingsDontRegisterGeyser;
    public static boolean settingsTestingStuff;
    public static boolean connectionNoResponseCodeOfConductFix;
    public static boolean cancelHitsEnabledForAllChecks;
    public static boolean cancelHitsCancelEvent;
    public static List alertsClickCommands;
    public static List clickAlertCommandCommands;
    public static List judgementDaysCommands;
    public static List judgementDaysBroadcast;
    public static List clientBrandJoinMessages;
    public static List punishmentsBroadcast;
    public static List commandsHelp;
    public static List commandsBanCommands;
    public static List alertsHoverMessage;
    public static List commandsBanBroadcast;
    public static List judgementDaysStartedBroadcast;
    public static List commandsProfile;
    public static List messagesCustomCommands;
    public static List commandsConnection;
    public static List clientBrandAlertsBlockedClientBrands;
    public static List messagesClickAlertCommandCommands;
    public static List commandsTop;
    public static List punishmentStatisticsBroadcastBroadcast;
    public static List freezeLoggedOutWhileFrozenCommands;
    public static List judgementDaysEndedBroadcast;
    public static List clientBrandAlertsIgnoreAlertsList;
    public static List clientBrandAlertsWhitelistedClientBrands;
    public static List cancelHitsChecks;
    public static int checksCombatAutoclickerAMaxCps;
    public static int checksPlayerFastplaceAMaxBlocks;
    public static int alertsSeverityViolations2;
    public static int punishmentStatisticsBroadcastInterval;
    public static int alertsSeverityViolations3;
    public static int alertsSeverityViolations4;
    public static int alertsSeverityViolations5;
    public static int checksPlayerPingspoofCMaxPing;
    public static int checksPlayerScaffoldKMaxBlocks;
    public static int settingsMaxLogsFileSize;
    public static int checksPlayerImprobableAMaxCombatViolations;
    public static int checksPlayerImprobableBMaxMovementViolations;
    public static int checksPlayerImprobableCMaxPlayerViolations;
    public static int checksPlayerImprobableDMaxAutoclickerViolations;
    public static int settingsPunishmentDelay;
    public static int checksPlayerTimerCMaxPackets;
    public static int settingsMaxAlertViolation;
    public static int settingsFlightCooldown;
    public static int checksPlayerFastbreakAMinDifference;
    public static int judgementDaysInterval;
    public static int settingsMinTicksExisted;
    public static int ghostBlocksFixTicks;
    public static int alertsSeverityViolations6;
    public static int alertsSeverityViolations7;
    public static int alertsSeverityViolations8;
    public static int alertsSeverityViolations9;
    public static int alertsSeverityViolations10;
    public static int checksPlayerImprobableEMaxTotalViolations;
    public static int checksPlayerImprobableFMaxScaffoldViolations;
    public static int unloadedChunksMaxTicks;
    public static int discordWebhookAlertsColorR;
    public static int discordWebhookAlertsColorG;
    public static int discordWebhookAlertsColorB;
    public static int discordWebhookPunishmentsColorR;
    public static int discordWebhookPunishmentsColorG;
    public static int discordWebhookPunishmentsColorB;
    public static int connectionMaxPingMaxPing;
    public static int connectionMaxPingMaxTicks;
    public static int settingsIgnoreAlertsOverMaxViolationsAmount;
    public static int checksPlayerFastplaceBMaxBlocks;
    public static int settingsEntityCramEntitiesAmount;
    public static int settingsEntityCramExemptTicks;
    public static int discordWebhookCooldownResetTicks;
    public static int discordWebhookCooldownMaxWebhooks;
    public static int connectionNoResponseDelaySeconds;
    public static int connectionMaxSizeAmount;
    public static double checksMovementBoatflyAMaxSpeed;
    public static double checksMovementElytraAMaxSpeed;
    public static double checksCombatReachAMaxReach;
    public static double checksCombatHitboxAMaxAngle;
    public static double checksCombatHitboxBMaxAngle;
    public static double checksMovementEntityspeedAMaxSpeed;
    public static double checksPlayerTimerAMaxSpeed;
    public static double checksCombatReachBMaxReach;
    public static double checksPlayerInteractAMaxDistance;
    public static double checksPlayerInteractBMaxDistance;
    public static double checksPlayerTimerBMinSpeed;
    public static double checksMovementSpeedBMinDifference;
    public static double checksMovementSpeedCMinDifference;
    public static double checksMovementSpeedDMinDifference;
    public static double checksMovementElytraLMinDifference;
    public static double checksMovementElytraMMinDifference;
    public static double ghostBlocksFixBufferMax;
    public static double ghostBlocksFixBufferDecay;
    public static double checksCombatAutoclickerTMinKurtosis;
    public static double ghostBlocksFixMinimumTps;
    public static double checksMovementEntityspeedAMinDifference;
    private static String disabledCheckMessage = "";
    private static YamlFileHelper configYaml;
    private static File configFile;
    public static void saveConfigYaml() {
        try {
            configYaml.save(configFile);
        } catch (Exception exception) {
            File file = new File(VulcanBootstrap.INSTANCE.getVulcanPlugin().getDataFolder(), "broken-" + new Date().getTime());
            VulcanBootstrap.INSTANCE.getVulcanPlugin().getLogger().log(Level.SEVERE, "config.yml");
            VulcanBootstrap.INSTANCE.getVulcanPlugin().getLogger().log(Level.SEVERE, "Corrupted config.yml, creating file and renaming the current file to: " + file.getName());
            VulcanBootstrap.INSTANCE.getVulcanPlugin().getLogger().log(Level.SEVERE, "Try fixing the file with a yaml parser online!");
            configFile.renameTo(file);
            ConfigValues.loadMainConfig();
        }
    }

    public static void loadConfigFile() {
        try {
            configYaml.load(configFile);
        } catch (Exception exception) {
            File file = new File(VulcanBootstrap.INSTANCE.getVulcanPlugin().getDataFolder(), "config.yml.broken." + new Date().getTime());
            VulcanBootstrap.INSTANCE.getVulcanPlugin().getLogger().log(Level.SEVERE, "config.yml");
            VulcanBootstrap.INSTANCE.getVulcanPlugin().getLogger().log(Level.SEVERE, "Corrupted config.yml, creating file and renaming the current file to: " + file.getName());
            VulcanBootstrap.INSTANCE.getVulcanPlugin().getLogger().log(Level.SEVERE, "Try fixing the file with a yaml parser online!");
            configFile.renameTo(file);
            ConfigValues.loadMainConfig();
        }
    }
    public static void loadMainConfig() {
        File dataFolder = VulcanBootstrap.INSTANCE.getVulcanPlugin().getDataFolder();
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        configFile = new File(dataFolder, "config.yml");
        copyStreamToFile(VulcanBootstrap.INSTANCE.getVulcanPlugin().getResource("config.yml"), configFile);
        configYaml = YamlFileHelper.loadFromFile(configFile);
        try {
            configYaml.saveSkippingRootKeys(configFile, "checks");
        } catch (Exception ignored) {
        }
    }

    private static void copyStreamToFile(InputStream inputStream, File file) {
        if (inputStream == null || file == null || file.exists()) {
            return;
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, read);
            }
            outputStream.close();
            inputStream.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void reloadMainConfig() {
        try {
            resetCommandSyntax = ConfigValues.getConfigString("prefix");
            messagesNoPermission = ConfigValues.getConfigString("messages.no-permission");
            unloadedChunksSetbackEnabled = ConfigValues.getConfigBoolean("unloaded-chunks.setback-enabled");
            unloadedChunksMessage = ConfigValues.getConfigString("unloaded-chunks.message");
            unloadedChunksMaxTicks = ConfigValues.getConfigInt("unloaded-chunks.max-ticks");
            alertsToggledOnMessage = ConfigValues.getConfigString("alerts.toggled-on-message");
            alertsToggledOffMessage = ConfigValues.getConfigString("alerts.toggled-off-message");
            clickAlertCommandCommands = ConfigValues.getConfigStringList("alerts.click-alert-command-commands");
            alertsClickCommands = ConfigValues.getConfigStringList("alerts.click-commands");
            messagesCantExecuteFromConsole = ConfigValues.getConfigString("messages.cant-execute-from-console");
            judgementDaysCommands = ConfigValues.getConfigStringList("judgement-days.commands");
            judgementDaysBroadcast = ConfigValues.getConfigStringList("judgement-days.broadcast");
            judgementDaysEndedBroadcast = ConfigValues.getConfigStringList("judgement-days.ended-broadcast");
            clientBrandJoinMessages = ConfigValues.getConfigStringList("client-brand-alerts.message");
            settingsToggleAlertsOnJoin = ConfigValues.getConfigBoolean("settings.toggle-alerts-on-join");
            punishmentsMessage = ConfigValues.getConfigString("punishments.message");
            punishmentsBroadcast = ConfigValues.getConfigStringList("punishments.broadcast");
            commandsHelp = ConfigValues.getConfigStringList("commands.help");
            discordWebhookCooldown = ConfigValues.getConfigBoolean("discord-webhook.cooldown");
            discordWebhookCooldownResetTicks = ConfigValues.getConfigInt("discord-webhook.cooldown-reset-ticks");
            discordWebhookCooldownMaxWebhooks = ConfigValues.getConfigInt("discord-webhook.cooldown-max-webhooks");
            commandsJdayListHeaderFooter = ConfigValues.getConfigString("commands.jday.list-header-footer");
            commandsPunishlogsHeaderFooter = ConfigValues.getConfigString("commands.punishlogs.header-footer");
            commandsJdayListFormat = ConfigValues.getConfigString("commands.jday.list-format");
            ghostWaterFixEnabled = ConfigValues.getConfigBoolean("ghost-blocks-fix.ghost-water-fix");
            dontSetbackLowerPositions = ConfigValues.getConfigBoolean("settings.dont-setback-lower-positions");
            connectionKeepaliveKickMessage = ConfigValues.getConfigString("connection.keepalive.kick-message");
            connectionMaxPingKickMessage = ConfigValues.getConfigString("connection.max-ping.kick-message");
            connectionKeepaliveKickEnabled = ConfigValues.getConfigBoolean("connection.keepalive.kick-enabled");
            connectionMaxPingKickEnabled = ConfigValues.getConfigBoolean("connection.max-ping.kick-enabled");
            connectionMaxPingMaxPing = ConfigValues.getConfigInt("connection.max-ping.max-ping");
            settingsVelocityWorldBorder = ConfigValues.getConfigBoolean("settings.velocity-world-border");
            settingsIncompatabilityManager = ConfigValues.getConfigBoolean("settings.incompatability-manager");
            settingsPluginMessaging = ConfigValues.getConfigBoolean("settings.plugin-messaging");
            connectionMaxPingMaxTicks = ConfigValues.getConfigInt("connection.max-ping.max-ticks");
            connectionKeepaliveStaffAlertMessage = ConfigValues.getConfigString("connection.keepalive.staff-alert-message");
            connectionMaxPingStaffAlertMessage = ConfigValues.getConfigString("connection.max-ping.staff-alert-message");
            connectionMaxPingConsoleMessage = ConfigValues.getConfigString("connection.max-ping.console-message");
            connectionKeepaliveConsoleMessage = ConfigValues.getConfigString("connection.keepalive.console-message");
            connectionKeepaliveMaxDelay = ConfigValues.getConfigLong("connection.keepalive.max-delay");
            discordWebhookPunishmentsEnabled = ConfigValues.getConfigBoolean("discord-webhook.punishments.enabled");
            discordWebhookPunishmentsUrl = ConfigValues.getConfigString("discord-webhook.punishments.url");
            discordWebhookPunishmentsAvatar = ConfigValues.getConfigString("discord-webhook.punishments.avatar");
            discordWebhookPunishmentsThumbnail = ConfigValues.getConfigString("discord-webhook.punishments.thumbnail");
            discordWebhookPunishmentsColorR = ConfigValues.getConfigInt("discord-webhook.punishments.color-r");
            discordWebhookPunishmentsColorG = ConfigValues.getConfigInt("discord-webhook.punishments.color-g");
            discordWebhookPunishmentsColorB = ConfigValues.getConfigInt("discord-webhook.punishments.color-b");
            discordWebhookPunishmentsUsername = ConfigValues.getConfigString("discord-webhook.punishments.username");
            discordWebhookPunishmentsTitle = ConfigValues.getConfigString("discord-webhook.punishments.title");
            settingsIgnoreAlertsOverMaxViolations = ConfigValues.getConfigBoolean("settings.ignore-alerts-over-max-violations");
            settingsIgnoreAlertsOverMaxViolationsAmount = ConfigValues.getConfigInt("settings.ignore-alerts-over-max-violations-amount");
            discordWebhookPunishmentsDescription = ConfigValues.getConfigString("discord-webhook.punishments.description");
            messagesPunishlogsSyntax = ConfigValues.getConfigString("messages.punishlogs-syntax");
            discordWebhookAlertsEnabled = ConfigValues.getConfigBoolean("discord-webhook.alerts.enabled");
            discordWebhookAlertsUrl = ConfigValues.getConfigString("discord-webhook.alerts.url");
            discordWebhookAlertsAvatar = ConfigValues.getConfigString("discord-webhook.alerts.avatar");
            discordWebhookAlertsThumbnail = ConfigValues.getConfigString("discord-webhook.alerts.thumbnail");
            discordWebhookAlertsColorR = ConfigValues.getConfigInt("discord-webhook.alerts.color-r");
            discordWebhookAlertsColorG = ConfigValues.getConfigInt("discord-webhook.alerts.color-g");
            discordWebhookAlertsColorB = ConfigValues.getConfigInt("discord-webhook.alerts.color-b");
            discordWebhookAlertsUsername = ConfigValues.getConfigString("discord-webhook.alerts.username");
            discordWebhookAlertsTitle = ConfigValues.getConfigString("discord-webhook.alerts.title");
            discordWebhookAlertsDescription = ConfigValues.getConfigString("discord-webhook.alerts.description");
            discordWebhookAlertsServerNameField = ConfigValues.getConfigBoolean("discord-webhook.alerts.server-name-field");
            discordWebhookAlertsContent = ConfigValues.getConfigString("discord-webhook.alerts.content");
            discordWebhookPunishmentsContent = ConfigValues.getConfigString("discord-webhook.punishments.content");
            discordWebhookPunishmentsServerNameField = ConfigValues.getConfigBoolean("discord-webhook.punishments.server-name-field");
            discordWebhookAlertsLocationField = ConfigValues.getConfigBoolean("discord-webhook.alerts.location-field");
            discordWebhookPunishmentsLocationField = ConfigValues.getConfigBoolean("discord-webhook.punishments.location-field");
            discordWebhookAlertsClientBrandField = ConfigValues.getConfigBoolean("discord-webhook.alerts.client-brand-field");
            discordWebhookPunishmentsClientBrandField = ConfigValues.getConfigBoolean("discord-webhook.punishments.client-brand-field");
            discordWebhookAlertsInformationField = ConfigValues.getConfigBoolean("discord-webhook.alerts.information-field");
            discordWebhookAlertsDescriptionField = ConfigValues.getConfigBoolean("discord-webhook.alerts.description-field");
            discordWebhookPunishmentsDescriptionField = ConfigValues.getConfigBoolean("discord-webhook.punishments.description-field");
            discordWebhookAlertsClientVersionField = ConfigValues.getConfigBoolean("discord-webhook.alerts.client-version-field");
            discordWebhookPunishmentsClientVersionField = ConfigValues.getConfigBoolean("discord-webhook.punishments.client-version-field");
            discordWebhookAlertsPingTpsField = ConfigValues.getConfigBoolean("discord-webhook.alerts.ping-tps-field");
            discordWebhookPunishmentsPingTpsField = ConfigValues.getConfigBoolean("discord-webhook.punishments.ping-tps-field");
            messagesJdayRemoveSyntax = ConfigValues.getConfigString("messages.jday-remove-syntax");
            messagesBanCommandSyntax = ConfigValues.getConfigString("messages.ban-command-syntax");
            commandsBanCommands = ConfigValues.getConfigStringList("commands.ban.commands");
            commandsBanBroadcast = ConfigValues.getConfigStringList("commands.ban.broadcast");
            messagesReloadSuccess = ConfigValues.getConfigString("messages.reload-success");
            messagesKbCommandSyntax = ConfigValues.getConfigString("messages.kb-command-syntax");
            messagesInvalidTarget = ConfigValues.getConfigString("messages.invalid-target");
            messagesKbTestSuccess = ConfigValues.getConfigString("messages.kb-test-success");
            messagesJdayCommandSyntax = ConfigValues.getConfigString("messages.jday-command-syntax");
            messagesJdayNoPendingBans = ConfigValues.getConfigString("messages.jday-no-pending-bans");
            judgementDaysStartedBroadcast = ConfigValues.getConfigStringList("judgement-days.started-broadcast");
            messagesJdayAddedToList = ConfigValues.getConfigString("messages.jday-added-to-list");
            messagesResetCommand = ConfigValues.getConfigString("messages.reset-command");
            messagesProfileCommandSyntax = ConfigValues.getConfigString("messages.profile-command-syntax");
            commandsProfile = ConfigValues.getConfigStringList("commands.profile");
            clientBrandAlertMessage = ConfigValues.getConfigString("client-brand-alerts.message");
            clientBrandAlertsResolve = ConfigValues.getConfigBoolean("client-brand-alerts.resolve");
            messagesRemovedFromJday = ConfigValues.getConfigString("messages.removed-from-jday");
            alertsHoverMessage = ConfigValues.getConfigStringList("alerts.hover-message");
            alertsFormat = ConfigValues.getConfigString("alerts.format");
            violationResetEnabled = ConfigValues.getConfigBoolean("violation-reset.enabled");
            violationResetMessageEnabled = ConfigValues.getConfigBoolean("violation-reset.message-enabled");
            violationResetMessage = ConfigValues.getConfigString("violation-reset.message");
            printToConsole = ConfigValues.getConfigBoolean("alerts.print-to-console");
            alertsConsoleFormat = ConfigValues.getConfigString("alerts.console-format");
            logFileEnabled = ConfigValues.getConfigBoolean("log-file.enabled");
            logFileAlertMessage = ConfigValues.getConfigString("log-file.alert-message");
            logFilePunishmentMessage = ConfigValues.getConfigString("log-file.punishment-message");
            punishmentFileEnabled = ConfigValues.getConfigBoolean("punishment-file.enabled");
            checksCombatAutoclickerAMaxCps = ConfigValues.getConfigInt("checks.combat.autoclicker.a.max-cps");
            checksCombatAutoclickerTMinKurtosis = ConfigValues.getConfigInt("checks.combat.autoclicker.t.min-kurtosis");
            messagesClickAlertCommandCommands = ConfigValues.getConfigStringList("alerts.click-alert-command-commands");
            checksPlayerFastplaceAMaxBlocks = ConfigValues.getConfigInt("checks.player.fastplace.a.max-blocks");
            checksPlayerFastplaceBMaxBlocks = ConfigValues.getConfigInt("checks.player.fastplace.b.max-blocks");
            checksPlayerAirplaceACancel = ConfigValues.getConfigBoolean("checks.player.ghosthand.a.cancel");
            settingsIgnoreVivecraft = ConfigValues.getConfigBoolean("settings.ignore-vivecraft");
            checksMovementBoatflyAKickOut = ConfigValues.getConfigBoolean("checks.movement.boatfly.a.kick-out");
            checksMovementBoatflyBKickOut = ConfigValues.getConfigBoolean("checks.movement.boatfly.b.kick-out");
            checksMovementEntityspeedAKickOut = ConfigValues.getConfigBoolean("checks.movement.entityspeed.a.kick-out");
            checksMovementEntityflightAKickOut = ConfigValues.getConfigBoolean("checks.movement.entityflight.a.kick-out");
            checksMovementSpeedBMinDifference = ConfigValues.getConfigDouble("checks.movement.speed.b.min-difference");
            checksMovementSpeedCMinDifference = ConfigValues.getConfigDouble("checks.movement.speed.c.min-difference");
            checksMovementSpeedDMinDifference = ConfigValues.getConfigDouble("checks.movement.speed.d.min-difference");
            checksMovementFlightAIgnoreGhostBlocks = ConfigValues.getConfigBoolean("checks.movement.flight.a.ignore-ghost-blocks");
            checksMovementFlightCIgnoreGhostBlocks = ConfigValues.getConfigBoolean("checks.movement.flight.c.ignore-ghost-blocks");
            checksMovementFlightDIgnoreGhostBlocks = ConfigValues.getConfigBoolean("checks.movement.flight.d.ignore-ghost-blocks");
            checksMovementFlightEIgnoreGhostBlocks = ConfigValues.getConfigBoolean("checks.movement.flight.e.ignore-ghost-blocks");
            checksMovementElytraLMinDifference = ConfigValues.getConfigDouble("checks.movement.elytra.l.min-difference");
            checksMovementElytraMMinDifference = ConfigValues.getConfigDouble("checks.movement.elytra.m.min-difference");
            checksMovementNosaddleAKickOut = ConfigValues.getConfigBoolean("checks.movement.nosaddle.a.kick-out");
            checksMovementBoatflyAMaxSpeed = ConfigValues.getConfigDouble("checks.movement.boatfly.a.max-speed");
            checksMovementElytraAMaxSpeed = ConfigValues.getConfigDouble("checks.movement.elytra.a.max-speed");
            checksCombatReachAMaxReach = ConfigValues.getConfigDouble("checks.combat.reach.a.max-reach");
            checksCombatReachBMaxReach = ConfigValues.getConfigDouble("checks.combat.reach.b.max-reach");
            settingsIgnoreGeyserPrefixes = ConfigValues.getConfigBoolean("settings.ignore-geyser-prefixes");
            settingsIgnoreGeyserPrefix = ConfigValues.getConfigString("settings.ignore-geyser-prefix");
            checksPlayerScaffoldKMaxBlocks = ConfigValues.getConfigInt("checks.player.scaffold.k.max-blocks");
            checksCombatHitboxAMaxAngle = ConfigValues.getConfigDouble("checks.combat.hitbox.a.max-angle");
            checksCombatHitboxBMaxAngle = ConfigValues.getConfigDouble("checks.combat.hitbox.b.max-angle");
            checksPlayerTimerBMinSpeed = ConfigValues.getConfigDouble("checks.player.timer.b.min-speed");
            checksPlayerInteractAMaxDistance = ConfigValues.getConfigDouble("checks.player.interact.a.max-distance");
            checksPlayerInteractBMaxDistance = ConfigValues.getConfigDouble("checks.player.interact.b.max-distance");
            checksMovementEntityspeedAMaxSpeed = ConfigValues.getConfigDouble("checks.movement.entityspeed.a.max-speed");
            checksPlayerImprobableAMaxCombatViolations = ConfigValues.getConfigInt("checks.player.improbable.a.max-combat-violations");
            checksPlayerImprobableBMaxMovementViolations = ConfigValues.getConfigInt("checks.player.improbable.b.max-movement-violations");
            checksPlayerImprobableCMaxPlayerViolations = ConfigValues.getConfigInt("checks.player.improbable.c.max-player-violations");
            checksPlayerImprobableDMaxAutoclickerViolations = ConfigValues.getConfigInt("checks.player.improbable.d.max-autoclicker-violations");
            checksPlayerImprobableEMaxTotalViolations = ConfigValues.getConfigInt("checks.player.improbable.e.max-total-violations");
            checksPlayerImprobableFMaxScaffoldViolations = ConfigValues.getConfigInt("checks.player.improbable.f.max-scaffold-violations");
            checksPlayerGhosthandACancel = ConfigValues.getConfigBoolean("checks.player.ghosthand.a.cancel");
            checksPlayerFastbreakAMinDifference = ConfigValues.getConfigInt("checks.player.fastbreak.a.min-difference");
            messagesCustomCommands = ConfigValues.getConfigStringList("alerts.custom-commands");
            checksPlayerTimerAMaxSpeed = ConfigValues.getConfigDouble("checks.player.timer.a.max-speed");
            checksPlayerTimerCMaxPackets = ConfigValues.getConfigInt("checks.player.timer.c.max-packets");
            checksPlayerTimerDMaxBalance = ConfigValues.getConfigLong("checks.player.timer.d.max-balance");
            settingsIgnoreGeyserUuids = ConfigValues.getConfigBoolean("settings.ignore-geyser-uuids");
            ghostBlocksFixIgnore117 = ConfigValues.getConfigBoolean("ghost-blocks-fix.ignore-1-17");
            alertsSeverityViolations2 = ConfigValues.getConfigInt("alerts.severity.violations.2");
            alertsSeverityViolations3 = ConfigValues.getConfigInt("alerts.severity.violations.3");
            alertsSeverityViolations4 = ConfigValues.getConfigInt("alerts.severity.violations.4");
            alertsSeverityViolations5 = ConfigValues.getConfigInt("alerts.severity.violations.5");
            alertsSeverityViolations6 = ConfigValues.getConfigInt("alerts.severity.violations.6");
            alertsSeverityViolations7 = ConfigValues.getConfigInt("alerts.severity.violations.7");
            alertsSeverityViolations8 = ConfigValues.getConfigInt("alerts.severity.violations.8");
            alertsSeverityViolations9 = ConfigValues.getConfigInt("alerts.severity.violations.9");
            alertsSeverityViolations10 = ConfigValues.getConfigInt("alerts.severity.violations.10");
            alertsSeverityColors1 = ConfigValues.getConfigString("alerts.severity.colors.1");
            alertsSeverityColors2 = ConfigValues.getConfigString("alerts.severity.colors.2");
            alertsSeverityColors3 = ConfigValues.getConfigString("alerts.severity.colors.3");
            alertsSeverityColors4 = ConfigValues.getConfigString("alerts.severity.colors.4");
            alertsSeverityColors5 = ConfigValues.getConfigString("alerts.severity.colors.5");
            alertsSeverityColors6 = ConfigValues.getConfigString("alerts.severity.colors.6");
            alertsSeverityColors7 = ConfigValues.getConfigString("alerts.severity.colors.7");
            alertsSeverityColors8 = ConfigValues.getConfigString("alerts.severity.colors.8");
            alertsSeverityColors9 = ConfigValues.getConfigString("alerts.severity.colors.9");
            alertsSeverityColors10 = ConfigValues.getConfigString("alerts.severity.colors.10");
            settingsPerCheckSeverities = ConfigValues.getConfigBoolean("settings.per-check-severities");
            checksPlayerPingspoofCMaxPing = ConfigValues.getConfigInt("checks.player.pingspoof.c.max-ping");
            entityCollisionEnabled = ConfigValues.getConfigBoolean("settings.entity-collision");
            cinematicEnabled = ConfigValues.getConfigBoolean("settings.cinematic");
            commandsCps = ConfigValues.getConfigString("commands.cps");
            settingsDebug = ConfigValues.getConfigBoolean("settings.debug");
            messagesSentTestAlert = ConfigValues.getConfigString("messages.sent-test-alert");
            noNextPage = ConfigValues.getConfigString("messages.no-next-page");
            messagesNoPreviousPage = ConfigValues.getConfigString("messages.no-previous-page");
            messagesEnabledAllChecks = ConfigValues.getConfigString("messages.enabled-all-checks");
            disabledAllChecks = ConfigValues.getConfigString("messages.disabled-all-checks");
            enabledAllPunishments = ConfigValues.getConfigString("messages.enabled-all-punishments");
            disabledAllPunishments = ConfigValues.getConfigString("messages.disabled-all-punishments");
            messagesDisabledCheck = ConfigValues.getConfigString("messages.enabled-check");
            messagesDisabledCheck2 = ConfigValues.getConfigString("messages.disabled-check");
            enabledPunishment = ConfigValues.getConfigString("messages.enabled-punishment");
            messagesDisabledPunishment = ConfigValues.getConfigString("messages.disabled-punishment");
            enabledHotbarShuffle = ConfigValues.getConfigString("messages.enabled-hotbar-shuffle");
            messagesDisabledHotbarShuffle = ConfigValues.getConfigString("messages.disabled-hotbar-shuffle");
            enabledRandomRotation = ConfigValues.getConfigString("messages.enabled-random-rotation");
            messagesDisabledRandomRotation = ConfigValues.getConfigString("messages.disabled-random-rotation");
            messagesEnabledBroadcastPunishment = ConfigValues.getConfigString("messages.enabled-broadcast-punishment");
            disabledBroadcastPunishment = ConfigValues.getConfigString("messages.disabled-broadcast-punishment");
            mustBeAnInteger = ConfigValues.getConfigString("messages.must-be-an-integer");
            messagesMustBePositive = ConfigValues.getConfigString("messages.must-be-positive");
            setMaxBuffer = ConfigValues.getConfigString("messages.set-max-buffer");
            messagesSetHotbarShuffleMinimumViolations = ConfigValues.getConfigString("messages.set-hotbar-shuffle-minimum-violations");
            setHotbarInterval = ConfigValues.getConfigString("messages.set-hotbar-interval");
            setRandomRotationMinimumViolations = ConfigValues.getConfigString("messages.set-random-rotation-minimum-violations");
            setRandomRotationInterval = ConfigValues.getConfigString("messages.set-random-rotation-interval");
            setAlertInterval = ConfigValues.getConfigString("messages.set-alert-interval");
            mustBeNumber = ConfigValues.getConfigString("messages.must-be-number");
            setBufferMultiple = ConfigValues.getConfigString("messages.set-buffer-multiple");
            messagesSetBufferDecay = ConfigValues.getConfigString("messages.set-buffer-decay");
            setMaxViolations = ConfigValues.getConfigString("messages.set-max-violations");
            setMinimumViolationsToAlert = ConfigValues.getConfigString("messages.set-minimum-violations-to-alert");
            setMaximumPing = ConfigValues.getConfigString("messages.set-maximum-ping");
            setMinimumTps = ConfigValues.getConfigString("messages.set-minimum-tps");
            removedPunishmentCommand = ConfigValues.getConfigString("messages.removed-punishment-command");
            invalidIndexNumber = ConfigValues.getConfigString("messages.invalid-index-number");
            stoppedEditingPunishmentCommands = ConfigValues.getConfigString("messages.stopped-editing-punishment-commands");
            addedPunishmentCommand = ConfigValues.getConfigString("messages.added-punishment-command");
            enterPunishmentCommand = ConfigValues.getConfigString("messages.enter-punishment-command");
            removePunishmentCommand = ConfigValues.getConfigString("messages.remove-punishment-command");
            messagesDisableCheckCommandSyntax = ConfigValues.getConfigString("messages.disable-check-command-syntax");
            messagesInvalidCheck = ConfigValues.getConfigString("messages.invalid-check");
            messagesCheckRemoved = ConfigValues.getConfigString("messages.check-removed");
            messagesViolationsCommandSyntax = ConfigValues.getConfigString("messages.violations-command-syntax");
            messagesNoLogs = ConfigValues.getConfigString("messages.no-logs");
            messagesCpsCommandSyntax = ConfigValues.getConfigString("messages.cps-command-syntax");
            messagesConnectionCommandSyntax = ConfigValues.getConfigString("messages.connection-command-syntax");
            commandsConnection = ConfigValues.getConfigStringList("commands.connection");
            messagesLogsCommandSyntax = ConfigValues.getConfigString("messages.logs-command-syntax");
            messagesNoLogsFile = ConfigValues.getConfigString("messages.no-logs-file");
            messagesLogsCommandNoLogs = ConfigValues.getConfigString("messages.logs-command-no-logs");
            messagesNoPage = ConfigValues.getConfigString("messages.no-page");
            commandsLogsColor = ConfigValues.getConfigString("commands.logs.color");
            commandsLogsHeaderFooter = ConfigValues.getConfigString("commands.logs.header-footer");
            messagesUpdateAvailable = ConfigValues.getConfigString("messages.update-available");
            settingsIgnoreFloodgate = ConfigValues.getConfigBoolean("settings.ignore-floodgate");
            messagesLatestVersion = ConfigValues.getConfigString("messages.latest-version");
            bstatsEnabled = ConfigValues.getConfigBoolean("settings.bstats");
            clientBrandAlertsIgnoreAlertsList = ConfigValues.getConfigStringList("client-brand-alerts.ignore-alerts-list");
            settingsMaxLogsFileSize = ConfigValues.getConfigInt("settings.max-logs-file-size");
            settingsServerName = ConfigValues.getConfigString("settings.server-name");
            messagesFrozen = ConfigValues.getConfigString("messages.frozen");
            messagesLoggedOutWhileFrozen = ConfigValues.getConfigString("messages.logged-out-while-frozen");
            messagesFroze = ConfigValues.getConfigString("messages.froze");
            messagesUnfroze = ConfigValues.getConfigString("messages.unfroze");
            messagesFreezeCommandSyntax = ConfigValues.getConfigString("messages.freeze-command-syntax");
            clientBrandAlertsBlockedClientBrands = ConfigValues.getConfigStringList("client-brand-alerts.blocked-client-brands");
            clientBrandAlertsUnallowedClientBrandKickMessage = ConfigValues.getConfigString("client-brand-alerts.unallowed-client-brand-kick-message");
            clientBrandKickMessage = clientBrandAlertsUnallowedClientBrandKickMessage;
            settingsAsyncAlerts = ConfigValues.getConfigBoolean("settings.async-alerts");
            settingsEnableApi = ConfigValues.getConfigBoolean("settings.enable-api");
            apiExportEnabled = settingsEnableApi;
            settingsPunishmentDelay = ConfigValues.getConfigInt("settings.punishment-delay");
            settingsIgnoreFlagsGeyserClientBrand = ConfigValues.getConfigBoolean("settings.ignore-flags-geyser-client-brand");
            joinCheckWaitTime = ConfigValues.getConfigLong("settings.join-check-wait-time");
            settingsMinTicksExisted = ConfigValues.getConfigInt("settings.min-ticks-existed");
            settingsMaxAlertViolation = ConfigValues.getConfigInt("settings.max-alert-violation");
            messagesShuffleCommandSyntax = ConfigValues.getConfigString("messages.shuffle-command-syntax");
            messagesShuffledHotbar = ConfigValues.getConfigString("messages.shuffled-hotbar");
            messagesRotateCommandSyntax = ConfigValues.getConfigString("messages.rotate-command-syntax");
            messagesRandomlyRotated = ConfigValues.getConfigString("messages.randomly-rotated");
            commandsTop = ConfigValues.getConfigStringList("commands.top");
            ghostBlocksFixEnabled = ConfigValues.getConfigBoolean("ghost-blocks-fix.enabled");
            ghostBlocksFixMinimumTps = ConfigValues.getConfigDouble("ghost-blocks-fix.minimum-tps");
            ghostBlocksFixBufferMax = ConfigValues.getConfigDouble("ghost-blocks-fix.buffer.max");
            ghostBlocksFixBufferDecay = ConfigValues.getConfigDouble("ghost-blocks-fix.buffer.decay");
            ghostBlocksFixMessage = ConfigValues.getConfigString("ghost-blocks-fix.message");
            ghostBlocksFixTicks = ConfigValues.getConfigInt("ghost-blocks-fix.ticks");
            ghostBlocksFixUpdateBlocks = ConfigValues.getConfigBoolean("ghost-blocks-fix.update-blocks");
            ghostBlocksFixConsoleMessage = ConfigValues.getConfigString("ghost-blocks-fix.console-message");
            settingsFlightCooldown = ConfigValues.getConfigInt("settings.flight-cooldown");
            ghostBlocksFixMessageEnabled = ConfigValues.getConfigBoolean("ghost-blocks-fix.message-enabled");
            ghostBlocksFixStaffMessageEnabled = ConfigValues.getConfigBoolean("ghost-blocks-fix.staff-message-enabled");
            ghostBlocksFixStaffMessage = ConfigValues.getConfigString("ghost-blocks-fix.staff-message");
            ghostBlocksFixSetback = ConfigValues.getConfigBoolean("ghost-blocks-fix.setback");
            ghostBlocksFixPrintToConsole = ConfigValues.getConfigBoolean("ghost-blocks-fix.print-to-console");
            ghostBlocksFixIgnoreLegacyVia = ConfigValues.getConfigBoolean("ghost-blocks-fix.ignore-legacy-via");
            clientBrandAlertsConsole = ConfigValues.getConfigBoolean("client-brand-alerts.console");
            clientBrandAlertsConsoleMessage = ConfigValues.getConfigString("client-brand-alerts.console-message");
            settingsTopCommandUnresolved = ConfigValues.getConfigString("settings.top-command-unresolved");
            judgementDaysCooldown = ConfigValues.getConfigLong("judgement-days.cooldown");
            punishmentStatisticsBroadcastEnabled = ConfigValues.getConfigBoolean("punishment-statistics-broadcast.enabled");
            punishmentStatisticsBroadcastBroadcast = ConfigValues.getConfigStringList("punishment-statistics-broadcast.broadcast");
            punishmentStatisticsBroadcastInterval = ConfigValues.getConfigInt("punishment-statistics-broadcast.interval");
            judgementDaysRunAtInterval = ConfigValues.getConfigBoolean("judgement-days.run-at-interval");
            judgementDaysInterval = ConfigValues.getConfigInt("judgement-days.interval");
            freezeLoggedOutWhileFrozenCommands = ConfigValues.getConfigStringList("freeze.logged-out-while-frozen-commands");
            alertsExperimentalSymbol = ConfigValues.getConfigString("alerts.experimental-symbol");
            settingsIgnoreIfInJudgementDay = ConfigValues.getConfigBoolean("settings.ignore-if-in-judgement-day");
            messagesResetCommandSyntax = ConfigValues.getConfigString("messages.reset-command-syntax");
            messagesViolationsReset = ConfigValues.getConfigString("messages.violations-reset");
            messagesUnknownCommand = ConfigValues.getConfigString("messages.unknown-command");
            messagesFrozeStaffBroadcast = ConfigValues.getConfigString("messages.froze-staff-broadcast");
            messagesUnfrozeStaffBroadcast = ConfigValues.getConfigString("messages.unfroze-staff-broadcast");
            settingsLenientScaffolding = ConfigValues.getConfigBoolean("settings.lenient-scaffolding");
            ghostBlocksFixConsoleMessage2 = ConfigValues.getConfigString("ghost-blocks-fix.console-message");
            entityCollisionEnabled = ConfigValues.getConfigBoolean("settings.entity-collision");
            settingsHookMcmmo = ConfigValues.getConfigBoolean("settings.hook-mcmmo");
            settingsHookMythicmobs = ConfigValues.getConfigBoolean("settings.hook-mythicmobs");
            settingsHookGsit = ConfigValues.getConfigBoolean("settings.hook-gsit");
            settingsHookBrewery = ConfigValues.getConfigBoolean("settings.hook-brewery");
            clientBrandAlertsWhitelistedClientBrands = ConfigValues.getConfigStringList("client-brand-alerts.whitelisted-client-brands");
            clientBrandAlertsWhitelistEnabled = ConfigValues.getConfigBoolean("client-brand-alerts.whitelist-enabled");
            entityCramFixEnabled = ConfigValues.getConfigBoolean("settings.entity-cram-fix-enabled");
            settingsEntityCramEntitiesAmount = ConfigValues.getConfigInt("settings.entity-cram-entities-amount");
            settingsEntityCramExemptTicks = ConfigValues.getConfigInt("settings.entity-cram-exempt-ticks");
            checksMovementEntityspeedAMinDifference = ConfigValues.getConfigDouble("checks.movement.entityspeed.a.min-difference");
            description = ConfigValues.getConfigString("commands.description");
            messagesDescriptionCommandSyntax = ConfigValues.getConfigString("messages.description-command-syntax");
            connectionInvalidKickMessage = ConfigValues.getConfigString("connection.invalid.kick-message");
            connectionInvalidStaffAlertMessage = ConfigValues.getConfigString("connection.invalid.staff-alert-message");
            connectionInvalidConsoleAlertMessage = ConfigValues.getConfigString("connection.invalid.console-alert-message");
            connectionOutOfOrderKickMessage = ConfigValues.getConfigString("connection.out-of-order.kick-message");
            connectionOutOfOrderStaffAlertMessage = ConfigValues.getConfigString("connection.out-of-order.staff-alert-message");
            connectionOutOfOrderConsoleAlertMessage = ConfigValues.getConfigString("connection.out-of-order.console-alert-message");
            connectionNoResponseDelaySeconds = ConfigValues.getConfigInt("connection.no-response.delay-in-seconds");
            connectionNoResponseKickMessage = ConfigValues.getConfigString("connection.no-response.kick-message");
            connectionNoResponseStaffAlertMessage = ConfigValues.getConfigString("connection.no-response.staff-alert-message");
            connectionNoResponseConsoleAlertMessage = ConfigValues.getConfigString("connection.no-response.console-alert-message");
            connectionNoResponseCodeOfConductFix = ConfigValues.getConfigBoolean("connection.no-response.code-of-conduct-fix");
            connectionDebugMessages = ConfigValues.getConfigBoolean("connection.debug-messages");
            connectionMaxSizeAmount = ConfigValues.getConfigInt("connection.max-size.amount");
            connectionMaxSizeStaffAlertMessage = ConfigValues.getConfigString("connection.max-size.staff-alert-message");
            connectionMaxSizeConsoleAlertMessage = ConfigValues.getConfigString("connection.max-size.console-alert-message");
            connectionMaxSizeKickMessage = ConfigValues.getConfigString("connection.max-size.kick-message");
            otherAnticheatSupport = ConfigValues.getConfigBoolean("connection.other-anticheat-support");
            connectionIgnoreIdsOverZero = ConfigValues.getConfigBoolean("connection.ignore-ids-over-0");
            settingsDontRegisterGeyser = ConfigValues.getConfigBoolean("settings.dont-register-geyser");
            settingsTestingStuff = ConfigValues.getConfigBoolean("settings.testing-stuff");
            cancelHitsEnabledForAllChecks = ConfigValues.getConfigBoolean("cancel-hits.enabled-for-all-checks");
            cancelHitsDuration = ConfigValues.getConfigLong("cancel-hits.duration");
            cancelHitsCancelEvent = ConfigValues.getConfigBoolean("cancel-hits.cancel-event");
            cancelHitsChecks = (List<String>) (List<?>) ConfigValues.getConfigStringList("cancel-hits.checks").stream().map(s -> normalizeCancelHitsCheck((String) s)).collect(Collectors.toList());
            CheckRegistryLoader.loadCheckSettings();
        }
    catch (Exception exception) {
            Bukkit.getLogger().severe("Error while loading Vulcan's configuration file!");
            exception.printStackTrace();
        }
    }

    
    private static String normalizeCancelHitsCheck(String check) {
        if (check == null) {
            return "";
        }
        return check.toLowerCase().replace(" ", "").replace("-", "");
    }

    public static boolean getConfigBoolean(String key) {
        return configYaml.getBoolean(key);
    }

    public static String getConfigString(String key) {
        return configYaml.getString(key);
    }

    public static int getConfigInt(String key) {
        return configYaml.getInt(key);
    }

    public static double getConfigDouble(String key) {
        return configYaml.getDouble(key);
    }

    public static long getConfigLong(String key) {
        return configYaml.getLong(key);
    }

    public static List getConfigStringList(String key) {
        return configYaml.getStringList(key);
    }

    public static boolean isConfigStringKey(String key) {
        return configYaml.isString(key);
    }

    public static Color getConfigColor(String key) {
        return configYaml.getColor(key);
    }

    public static boolean isConfigColorKey(String key) {
        return configYaml.isColor(key);
    }

    public static void setBukkitConfigValue(String key, Object value) {
        VulcanBootstrap.INSTANCE.getVulcanPlugin().getConfig().set(key, value);
        VulcanBootstrap.INSTANCE.getVulcanPlugin().saveConfig();
        VulcanBootstrap.INSTANCE.getVulcanPlugin().reloadConfig();
    }

    private ConfigValues() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    private static String lambda$updateConfig$0(String string) {
        return string.toLowerCase().replace(" ","");
    }
    static {
        ConfigValues.punishmentCommandsByCheck = new HashMap();
        ConfigValues.enabledByCheck = new HashMap();
        ConfigValues.maxViolationsByCheck = new HashMap();
        ConfigValues.alertIntervalByCheck = new HashMap();
        ConfigValues.minimumVlToNotifyByCheck = new HashMap();
        ConfigValues.punishmentCommandListByCheck = new HashMap();
        ConfigValues.punishableByCheck = new HashMap();
        ConfigValues.punishmentCommandsBySeverity = new HashMap();
        ConfigValues.maximumPingByCheck = new HashMap();
        ConfigValues.minimumTpsByCheck = new HashMap();
        ConfigValues.checkEnabledByName = new HashMap();
        ConfigValues.maxBufferByCheck = new HashMap();
        ConfigValues.bufferDecayByCheck = new HashMap();
        ConfigValues.bufferMultipleByCheck = new HashMap();
        ConfigValues.hotbarShuffleEnabledByCheck = new HashMap();
        ConfigValues.hotbarShuffleMinimumVlByCheck = new HashMap();
        ConfigValues.hotbarShuffleIntervalByCheck = new HashMap();
        ConfigValues.randomRotationEnabledByCheck = new HashMap();
        ConfigValues.randomRotationMinimumVlByCheck = new HashMap();
        ConfigValues.randomRotationIntervalByCheck = new HashMap();
        ConfigValues.discordAlertIntervalByCheck = new HashMap();
        ConfigValues.maxVlBeforeSetbackByCheck = new HashMap();
    }
}
