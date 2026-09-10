package me.frep.vulcan.spigot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientHeldItemChange;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerBlockPlacement;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import me.frep.vulcan.api.data.IPlayerData;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.manager.CheckManager;
import org.bukkit.entity.Player;


public class PlayerData
implements IPlayerData {
    private final User user;
    @Nullable
    private Player player;
    private String clientBrand;
    private int entityId;
    private UUID uuid;
    private ClientVersion clientVersion;
    private String username;
    private boolean joined;
    private boolean inventoryOpen;
    private boolean blocking;
    private boolean gliding;
    private int totalViolations;
    private int combatViolations;
    private int movementViolations;
    private int playerViolations;
    private int autoClickerViolations;
    private int scaffoldViolations;
    private int timerViolations;
    private final long joinTime;
    private final int joinTicks;
    private long lastClientBrandAlert;
    private long lastVelocityTime;
    private WrapperPlayClientPlayerFlying lastFlyingPacket;
    private WrapperPlayClientInteractEntity lastInteractPacket;
    private WrapperPlayClientPlayerDigging lastDigPacket;
    private WrapperPlayClientEntityAction lastEntityActionPacket;
    private WrapperPlayClientHeldItemChange lastHeldItemChangePacket;
    private WrapperPlayClientPlayerBlockPlacement lastBlockPlacePacket;
    private List recentFlags;
    private List checks;
    private HashSet exemptPlayers;
    private HashSet ignoredPlayers;
    private final ExemptionChecker exemptionChecker;
    private final ReachEntityHelper reachEntityHelper;
    private final StatusTracker statusTracker;
    private final ClickTracker clickTracker;
    private final BlockBreakTracker blockBreakTracker;
    private final ActionTracker actionTracker;
    private final TickTracker tickTracker;
    private final CheckState checkState;
    private final PacketHandler packetHandler;
    private final SampleDeque recentSamples;
    private final SampleDeque olderSamples;
    private long lastFlagTime;
    private long lastPunishmentTime;
    private static final String[] clientBrandPlaceholders;

    public PlayerData(User user) {
        this.clientBrand = "vanilla";
        this.entityId = -42069;
        this.uuid = null;
        this.clientVersion = ClientVersion.UNKNOWN;
        this.username = "Unknown";
        this.joined = false;
        this.inventoryOpen = false;
        this.blocking = false;
        this.gliding = false;
        this.joinTime = System.currentTimeMillis();
        this.joinTicks = VulcanBootstrap.INSTANCE.getKeepAliveRunnable().getCurrentTick();
        this.recentFlags = new ArrayList();
        this.exemptPlayers = new HashSet();
        this.ignoredPlayers = new HashSet();
        this.exemptionChecker = new ExemptionChecker(this);
        this.reachEntityHelper = new ReachEntityHelper(this);
        this.statusTracker = new StatusTracker(this);
        this.clickTracker = new ClickTracker(this);
        this.blockBreakTracker = new BlockBreakTracker(this);
        this.actionTracker = new ActionTracker(this);
        this.tickTracker = new TickTracker(this);
        this.checkState = new CheckState(this);
        this.packetHandler = new PacketHandler(this);
        this.recentSamples = new SampleDeque(100);
        this.olderSamples = new SampleDeque(100);
        this.lastFlagTime = -1L;
        this.user = user;
        this.checks = CheckManager.createChecksForPlayer(this);
    }

    public boolean isClientAtLeast1_17() {
        return CollisionUtil.isClientAtLeast1_17(this);
    }

    public boolean isClientAtLeast1_13() {
        return CollisionUtil.isClientAtLeast1_13(this);
    }

    public boolean isClientNewerThan1_9() {
        return CollisionUtil.isClientNewerThan1_9(this);
    }

    public boolean isClientPre1_9() {
        return this.clientVersion.isOlderThan(ClientVersion.V_1_9);
    }

    public boolean isClientAtLeast1_21_2() {
        return this.clientVersion.isNewerThanOrEquals(ClientVersion.V_1_21_2);
    }

    public List getActiveExemptTypes() {
        List types = new ArrayList();
        ExemptType[] exemptTypes = ExemptType.values();
        for (int i = 0; i < exemptTypes.length; ++i) {
            if (this.exemptionChecker.isExempt(exemptTypes[i])) {
                types.add(exemptTypes[i].toString());
            }
        }
        return types;
    }

    public boolean isClientAtMost1_8() {
        return this.clientVersion.isOlderThanOrEquals(ClientVersion.V_1_8);
    }
    public void refreshExemptTypes() {
        // ExemptType predicates are evaluated live in AbstractCheck.isExempt.
    }

    public User getUser() {
        return this.user;
    }

    public Player getPlayer() {
        return this.player;
    }

    @Nullable

    @Override
    public String getClientBrand() {
        return this.clientBrand;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public ClientVersion getClientVersion() {
        return this.clientVersion;
    }

    public String getUsername() {
        return this.username;
    }

    public boolean isJoined() {
        return this.joined;
    }

    public boolean isInventoryOpen() {
        return this.inventoryOpen;
    }

    public boolean isBlocking() {
        return this.blocking;
    }

    public boolean isGliding() {
        return this.gliding;
    }

    public void setGliding(boolean gliding) {
        this.gliding = gliding;
    }

    @Override
    public int getTotalViolations() {
        return this.totalViolations;
    }

    @Override
    public int getCombatViolations() {
        return this.combatViolations;
    }

    @Override
    public int getMovementViolations() {
        return this.movementViolations;
    }

    @Override
    public int getPlayerViolations() {
        return this.playerViolations;
    }

    @Override
    public int getAutoClickerViolations() {
        return this.autoClickerViolations;
    }

    @Override
    public int getScaffoldViolations() {
        return this.scaffoldViolations;
    }

    @Override
    public int getTimerViolations() {
        return this.timerViolations;
    }

    @Override
    public long getJoinTime() {
        return this.joinTime;
    }

    @Override
    public int getJoinTicks() {
        return this.joinTicks;
    }

    @Override
    public long getLastClientBrandAlert() {
        return this.lastClientBrandAlert;
    }

    public long getLastVelocityTime() {
        return this.lastVelocityTime;
    }

    public WrapperPlayClientPlayerFlying getLastFlyingPacket() {
        return this.lastFlyingPacket;
    }

    public WrapperPlayClientInteractEntity getLastInteractPacket() {
        return this.lastInteractPacket;
    }

    public WrapperPlayClientPlayerDigging getLastDigPacket() {
        return this.lastDigPacket;
    }

    public WrapperPlayClientEntityAction getLastEntityActionPacket() {
        return this.lastEntityActionPacket;
    }

    public WrapperPlayClientHeldItemChange getLastHeldItemChangePacket() {
        return this.lastHeldItemChangePacket;
    }

    public WrapperPlayClientPlayerBlockPlacement getLastBlockPlacePacket() {
        return this.lastBlockPlacePacket;
    }

    public List getRecentFlags() {
        return this.recentFlags;
    }

    public List getChecks() {
        return this.checks;
    }

    public void setChecks(List value) {
        this.checks = value;
    }

    public HashSet getExemptPlayers() {
        return this.exemptPlayers;
    }

    public HashSet getIgnoredPlayers() {
        return this.ignoredPlayers;
    }

    public ExemptionChecker getExemptionChecker() {
        return this.exemptionChecker;
    }

    public ReachEntityHelper getReachEntityHelper() {
        return this.reachEntityHelper;
    }

    public StatusTracker getStatusTracker() {
        return this.statusTracker;
    }

    public ClickTracker getClickTracker() {
        return this.clickTracker;
    }

    public BlockBreakTracker getBlockBreakTracker() {
        return this.blockBreakTracker;
    }

    public ActionTracker getActionTracker() {
        return this.actionTracker;
    }

    public TickTracker getTickTracker() {
        return this.tickTracker;
    }

    public CheckState getCheckState() {
        return this.checkState;
    }

    public PacketHandler getPacketHandler() {
        return this.packetHandler;
    }

    public SampleDeque getRecentSamples() {
        return this.recentSamples;
    }

    public SampleDeque getOlderSamples() {
        return this.olderSamples;
    }

    public long getLastFlagTime() {
        return this.lastFlagTime;
    }

    public void setPlayer(@Nullable Player player) {
        this.player = player;
    }

    public void setClientBrand(String value) {
        this.clientBrand = value;
    }

    public void setEntityId(int value) {
        this.entityId = value;
    }

    public void setUuid(UUID value) {
        this.uuid = value;
    }

    public void setClientVersion(ClientVersion value) {
        this.clientVersion = value;
    }

    public void setUsername(String value) {
        this.username = value;
    }

    public void setJoined(boolean value) {
        this.joined = value;
    }

    public void setInventoryOpen(boolean value) {
        this.inventoryOpen = value;
    }

    public void setBlocking(boolean value) {
        this.blocking = value;
    }

    public void setTotalViolations(int value) {
        this.totalViolations = value;
    }

    public void setCombatViolations(int value) {
        this.combatViolations = value;
    }

    public void setMovementViolations(int value) {
        this.movementViolations = value;
    }

    public void setPlayerViolations(int value) {
        this.playerViolations = value;
    }

    public void setAutoClickerViolations(int value) {
        this.autoClickerViolations = value;
    }

    public void setScaffoldViolations(int value) {
        this.scaffoldViolations = value;
    }

    public void setTimerViolations(int value) {
        this.timerViolations = value;
    }

    public void setLastClientBrandAlert(long value) {
        this.lastClientBrandAlert = value;
    }

    public void setLastVelocityTime(long value) {
        this.lastVelocityTime = value;
    }

    public void setLastFlyingPacket(WrapperPlayClientPlayerFlying value) {
        this.lastFlyingPacket = value;
    }

    public void setLastInteractPacket(WrapperPlayClientInteractEntity value) {
        this.lastInteractPacket = value;
    }

    public void setLastDigPacket(WrapperPlayClientPlayerDigging value) {
        this.lastDigPacket = value;
    }

    public void setLastEntityActionPacket(WrapperPlayClientEntityAction value) {
        this.lastEntityActionPacket = value;
    }

    public void setLastHeldItemChangePacket(WrapperPlayClientHeldItemChange value) {
        this.lastHeldItemChangePacket = value;
    }

    public void setLastBlockPlacePacket(WrapperPlayClientPlayerBlockPlacement value) {
        this.lastBlockPlacePacket = value;
    }

    public void setRecentFlags(List value) {
        this.recentFlags = value;
    }

    public void setExemptPlayers(HashSet value) {
        this.exemptPlayers = value;
    }

    public void setIgnoredPlayers(HashSet value) {
        this.ignoredPlayers = value;
    }

    public long getLastPunishmentTime() {
        return this.lastPunishmentTime;
    }

    public void setLastPunishmentTime(long value) {
        this.lastPunishmentTime = value;
    }

    public void setLastFlagTime(long value) {
        this.lastFlagTime = value;
    }

    private void lambda$handleClientBrand$5() {
        VulcanBootstrap.INSTANCE.getAlertPlaceholders().sendAlertMessage(ConfigValues.clientBrandAlertMessage.replaceAll("%player%", this.player.getName()).replaceAll("%client-version%", this.user.getClientVersion().getReleaseName()).replaceAll("%client-brand%", this.clientBrand));
    }

    private static void lambda$handleClientBrand$4(String string) {
        VulcanBootstrap.INSTANCE.getAlertPlaceholders().sendAlertMessage(string);
    }

    private void lambda$handleClientBrand$3() {
        this.player.sendMessage(ColorUtil.translateColors(ConfigValues.clientBrandKickMessage.replaceAll("%client-brand%", this.clientBrand)));
        this.player.kickPlayer(ColorUtil.translateColors(ConfigValues.clientBrandKickMessage.replaceAll("%client-brand%", this.clientBrand)));
    }

    private void lambda$handleClientBrand$2() {
        this.player.kickPlayer(ColorUtil.translateColors(ConfigValues.clientBrandKickMessage.replaceAll("%client-brand%", this.clientBrand)));
    }

    private void lambda$handleClientBrand$1() {
        this.player.kickPlayer(ColorUtil.translateColors(ConfigValues.clientBrandAlertsUnallowedClientBrandKickMessage.replace("%client-brand%", this.clientBrand)));
    }

    private void lambda$handleClientBrand$0() {
        this.player.kickPlayer(ColorUtil.translateColors(ConfigValues.clientBrandAlertsUnallowedClientBrandKickMessage.replace("%client-brand%", this.clientBrand)));
    }

    static {
        clientBrandPlaceholders = new String[] {"%client-version%", "Invalid client brand.", "%player%", "%client-brand%", "Ignored client brand.", "unallowed", "vulcan.bypass.client-brand.whitelist", "%version%", "vulcan.bypass.client-brand.blacklist", "cancelled", "%ping%", "vulcan.bypass.client-brand-alerts", "%client-brand%"};
    }

}
