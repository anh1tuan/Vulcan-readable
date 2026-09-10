package me.frep.vulcan.spigot.check;

import com.github.retrooper.packetevents.event.PacketEvent;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import java.util.Objects;
import me.frep.vulcan.api.check.Check;
import me.frep.vulcan.api.event.VulcanSetbackEvent;
import me.frep.vulcan.spigot.ConfigValues;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ServerCompatibility;
import me.frep.vulcan.spigot.CollisionUtil;
import me.frep.vulcan.spigot.VulcanBootstrap;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;

public abstract class AbstractCheck implements Check {
    protected final PlayerData playerData;
    private int violationLevel;
    private double buffer;
    public final String checkName;
    public final double maxBufferConfig;
    public final double bufferDecayConfig;
    public final double bufferMultipleConfig;

    public AbstractCheck(PlayerData playerData) {
        this.playerData = playerData;
        this.checkName = this.getClass().getSimpleName();
        this.maxBufferConfig = ((Integer) ConfigValues.maxBufferByCheck.get(this.checkName)).intValue();
        this.bufferDecayConfig = (Double) ConfigValues.bufferDecayByCheck.get(this.checkName);
        this.bufferMultipleConfig = (Double) ConfigValues.bufferMultipleByCheck.get(this.checkName);
    }

    public abstract void handle(PacketEvent event, long timestamp);

    public void fail(Object detail) {
        if (this.playerData.getPlayer() == null) {
            return;
        }

        boolean enabled = (Boolean) ConfigValues.checkEnabledByName.get(this.getCheckName());
        if (enabled) {
            int current = this.getViolationLevel();
            int setbackLimit = (Integer) ConfigValues.maxVlBeforeSetbackByCheck.get(this.getCheckName());
            if (current > setbackLimit) {
                VulcanSetbackEvent setbackEvent =
                    new VulcanSetbackEvent(this.playerData.getPlayer(), this);
                Bukkit.getPluginManager().callEvent(setbackEvent);
                if (!setbackEvent.isCancelled()) {
                    this.playerData.getBlockBreakTracker().markGhostBlockFail();
                }
            }
        }

        this.maybeShuffleHotbar();
        this.maybeApplyRandomRotation();
        this.multiplyBuffer(this.bufferMultipleConfig);
        String information = Objects.toString(detail, "");
        VulcanBootstrap.INSTANCE.getJoinExecutor().execute(
            () -> VulcanBootstrap.INSTANCE.getAlertMessageBuilder().handleAlert(
                this, this.playerData, information
            )
        );
    }



    public void failSimple(Object detail) {
        this.fail(detail);
    }



    public void punish() {
        VulcanBootstrap.INSTANCE.getJoinExecutor().execute(
            () -> VulcanBootstrap.INSTANCE.getAlertPlaceholders().handlePunishment(this, this.playerData)
        );
    }

    protected boolean isExempt(ExemptType... types) {
        return this.playerData.getExemptionChecker().isExempt(types);
    }


    public int getCurrentTick() {
        return VulcanBootstrap.INSTANCE.getKeepAliveRunnable().getCurrentTick();
    }

    public boolean isFlyingPacket(PacketEvent event) {
        if (!(event instanceof PacketReceiveEvent)) {
            return false;
        }
        PacketTypeCommon type = ((PacketReceiveEvent) event).getPacketType();
        return type == PacketType.Play.Client.PLAYER_FLYING
            || type == PacketType.Play.Client.PLAYER_POSITION
            || type == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION
            || type == PacketType.Play.Client.PLAYER_ROTATION
            || type == PacketType.Play.Client.CLIENT_TICK_END;
    }


    public boolean isPositionPacket(PacketEvent event) {
        if (!(event instanceof PacketReceiveEvent)) {
            return false;
        }
        PacketTypeCommon type = ((PacketReceiveEvent) event).getPacketType();
        return type == PacketType.Play.Client.PLAYER_POSITION
            || type == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION;
    }

    public boolean isRotationPacket(PacketEvent event) {
        if (!(event instanceof PacketReceiveEvent)) {
            return false;
        }
        PacketTypeCommon type = ((PacketReceiveEvent) event).getPacketType();
        return type == PacketType.Play.Client.PLAYER_ROTATION
            || type == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION;
    }


    public double incrementBuffer() {
        this.buffer = Math.min(10000.0, this.buffer + 1.0);
        return this.buffer;
    }

    public double decreaseBuffer(double amount) {
        this.buffer = Math.max(0.0, this.buffer - amount);
        return this.buffer;
    }

    public double increaseBuffer(double amount) {
        this.buffer = Math.min(10000.0, this.buffer + amount);
        return this.buffer;
    }

    public void resetBuffer() {
        this.buffer = 0.0;
    }

    public void multiplyBuffer(double amount) {
        this.buffer *= amount;
    }

    public void applyBufferDecay() {
        this.decreaseBuffer(this.bufferDecayConfig);
    }

    public int getTicksSinceAttack() {
        return this.playerData.getReachEntityHelper().getTicksSinceAttack();
    }

    public boolean isTicksSinceAttackBelow(int threshold) {
        if (this.playerData.getReachEntityHelper().getTicksSinceAttack() >= threshold) {
            return false;
        }
        return this.playerData.getReachEntityHelper().getAttackTick() < threshold * 6;
    }


    public CheckInfo getCheckInfo() {
        if (this.getClass().isAnnotationPresent(CheckInfo.class)) {
            return this.getClass().getAnnotation(CheckInfo.class);
        }
        ServerCompatibility.logInfo(
            "CheckInfo annotation hasn't been added to the class " + this.checkName + "."
        );
        return null;
    }


    @Override
    public String getCategory() {
        String pkg = this.getClass().getPackage().getName();
        if (pkg.contains(".combat.")) {
            return "combat";
        }
        if (pkg.contains(".movement.")) {
            return "movement";
        }
        if (pkg.contains(".player.")) {
            return "player";
        }
        return "misc";
    }

    public boolean isOnGround() {
        return this.playerData.getStatusTracker().isOnGround();
    }

    public boolean isBreakingBlock() {
        return this.playerData.getBlockBreakTracker().isBreakingBlock();
    }

    @Override
    public String getName() {
        CheckInfo info = this.getCheckInfo();
        if (info == null) {
            return this.checkName.toLowerCase();
        }
        return info.name().toLowerCase().replace(" ", "");
    }

    @Override
    public char getType() {
        CheckInfo info = this.getCheckInfo();
        if (info == null) {
            return '?';
        }
        return Character.toLowerCase(info.type());
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public int getViolationLevel() {
        return this.violationLevel;
    }

    @Override
    public int getMaxViolationLevel() {
        return (Integer) ConfigValues.maxViolationsByCheck.get(this.checkName);
    }

    @Override
    public int getMinimumViolationLevelToNotify() {
        return (Integer) ConfigValues.minimumVlToNotifyByCheck.get(this.checkName);
    }

    @Override
    public double getMaxBuffer() {
        return this.maxBufferConfig;
    }

    @Override
    public String getDescription() {
        CheckInfo info = this.getCheckInfo();
        return info == null ? "" : info.description();
    }

    @Override
    public String getComplexType() {
        CheckInfo info = this.getCheckInfo();
        return info == null ? "" : info.complexType();
    }

    @Override
    public double getBufferDecay() {
        return this.bufferDecayConfig;
    }

    @Override
    public int getAlertInterval() {
        return (Integer) ConfigValues.alertIntervalByCheck.get(this.checkName);
    }

    @Override
    public double getBufferMultiple() {
        return this.bufferMultipleConfig;
    }

    @Override
    public boolean isExperimental() {
        CheckInfo info = this.getCheckInfo();
        return info != null && info.experimental();
    }

    @Override
    public boolean isPunishable() {
        return (Boolean) ConfigValues.punishableByCheck.get(this.checkName);
    }

    @Override
    public String getDisplayName() {
        CheckInfo info = this.getCheckInfo();
        return info == null ? this.checkName : info.name();
    }

    @Override
    public char getDisplayType() {
        CheckInfo info = this.getCheckInfo();
        return info == null ? '?' : info.type();
    }

    public void maybeShuffleHotbar() {
        if (this.playerData.getPlayer() == null) {
            return;
        }
        Boolean enabled = (Boolean) ConfigValues.hotbarShuffleEnabledByCheck.get(this.checkName);
        if (enabled == null || !enabled.booleanValue()) {
            return;
        }
        Integer minimum = (Integer) ConfigValues.hotbarShuffleMinimumVlByCheck.get(this.checkName);
        Integer interval = (Integer) ConfigValues.hotbarShuffleIntervalByCheck.get(this.checkName);
        if (minimum != null && this.violationLevel < minimum.intValue()) {
            return;
        }
        if (interval != null && interval.intValue() > 0 && this.violationLevel % interval.intValue() != 0) {
            return;
        }
        CollisionUtil.shuffleHotbar(this.playerData.getPlayer());
    }

    public void maybeApplyRandomRotation() {
        if (this.playerData.getPlayer() == null) {
            return;
        }
        Boolean enabled = (Boolean) ConfigValues.randomRotationEnabledByCheck.get(this.checkName);
        if (enabled == null || !enabled.booleanValue()) {
            return;
        }
        Integer minimum = (Integer) ConfigValues.randomRotationMinimumVlByCheck.get(this.checkName);
        Integer interval = (Integer) ConfigValues.randomRotationIntervalByCheck.get(this.checkName);
        if (minimum != null && this.violationLevel < minimum.intValue()) {
            return;
        }
        if (interval != null && interval.intValue() > 0 && this.violationLevel % interval.intValue() != 0) {
            return;
        }
        CollisionUtil.applyRandomRotation(this.playerData);
    }

    private boolean isClientPacket(PacketEvent event, PacketTypeCommon expected) {
        if (!(event instanceof PacketReceiveEvent)) {
            return false;
        }
        return ((PacketReceiveEvent) event).getPacketType() == expected;
    }

    public boolean isPositionRotationPacket(PacketEvent event) {
        return this.isClientPacket(event, PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION);
    }


    public boolean isInteractEntityPacket(PacketEvent event) {
        return this.isClientPacket(event, PacketType.Play.Client.INTERACT_ENTITY);
    }


    public boolean isBlockPlacePacket(PacketEvent event) {
        return this.isClientPacket(event, PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT);
    }


    public boolean isUseItemPacket(PacketEvent event) {
        return this.isClientPacket(event, PacketType.Play.Client.USE_ITEM);
    }


    public boolean isAnimationPacket(PacketEvent event) {
        return this.isClientPacket(event, PacketType.Play.Client.ANIMATION);
    }


    public boolean isVehicleMovePacket(PacketEvent event) {
        return this.isClientPacket(event, PacketType.Play.Client.VEHICLE_MOVE);
    }


    public boolean isEntityActionPacket(PacketEvent event) {
        return this.isClientPacket(event, PacketType.Play.Client.ENTITY_ACTION);
    }


    public boolean isDiggingPacket(PacketEvent event) {
        return this.isClientPacket(event, PacketType.Play.Client.PLAYER_DIGGING);
    }


    public boolean isAbilitiesPacket(PacketEvent event) {
        return this.isClientPacket(event, PacketType.Play.Client.PLAYER_ABILITIES);
    }


    public boolean isSteerVehiclePacket(PacketEvent event) {
        return this.isClientPacket(event, PacketType.Play.Client.STEER_VEHICLE);
    }


    public boolean isClientStatusPacket(PacketEvent event) {
        return this.isClientPacket(event, PacketType.Play.Client.CLIENT_STATUS);
    }


    public boolean isHeldItemChangePacket(PacketEvent event) {
        return this.isClientPacket(event, PacketType.Play.Client.HELD_ITEM_CHANGE);
    }


    public boolean isKeepAlivePacket(PacketEvent event) {
        return this.isClientPacket(event, PacketType.Play.Client.KEEP_ALIVE);
    }


    public boolean isClickWindowPacket(PacketEvent event) {
        return this.isClientPacket(event, PacketType.Play.Client.CLICK_WINDOW);
    }


    public boolean isSpectatePacket(PacketEvent event) {
        return this.isClientPacket(event, PacketType.Play.Client.PLAYER_FLYING);
    }


    public boolean isClientTickEndPacket(PacketEvent event) {
        return this.isClientPacket(event, PacketType.Play.Client.CLIENT_TICK_END);
    }


    public boolean isServerPositionLookPacket(PacketEvent event) {
        if (!(event instanceof PacketSendEvent)) {
            return false;
        }
        PacketTypeCommon type = ((PacketSendEvent) event).getPacketType();
        return type == PacketType.Play.Server.PLAYER_POSITION_AND_LOOK;
    }


    public abstract void handleEvent(Event event, long timestamp);

    public void handleEvent(Event event) {
        this.handleEvent(event, System.currentTimeMillis());
    }

    public PlayerData getPlayerData() {
        return this.playerData;
    }

    @Override
    public double getBuffer() {
        return this.buffer;
    }

    public String getCheckName() {
        return this.checkName;
    }

    public double getMaxBufferConfig() {
        return this.maxBufferConfig;
    }

    public double getBufferDecayConfig() {
        return this.bufferDecayConfig;
    }

    public double getBufferMultipleConfig() {
        return this.bufferMultipleConfig;
    }

    @Override
    public void setViolationLevel(int violationLevel) {
        this.violationLevel = violationLevel;
    }

    @Override
    public void setBuffer(double buffer) {
        this.buffer = buffer;
    }

    public static void setEmptyCheckArray(AbstractCheck[] ignored) {
    }

    public static AbstractCheck[] getEmptyCheckArray() {
        return null;
    }
}
