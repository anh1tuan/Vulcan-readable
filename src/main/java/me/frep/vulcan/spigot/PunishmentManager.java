package me.frep.vulcan.spigot;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClientStatus;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerBlockPlacement;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientVehicleMove;
import java.util.List;
import me.frep.vulcan.spigot.check.AbstractCheck;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PunishmentManager {
    public void handlePacket(PlayerData playerData, long timestamp, PacketReceiveEvent event) {
        if (playerData == null || event == null) {
            return;
        }
        this.updateTrackers(playerData, timestamp, event);
        List checks = playerData.getChecks();
        if (checks == null) {
            return;
        }
        for (Object checkObject : checks) {
            if (!(checkObject instanceof AbstractCheck)) {
                continue;
            }
            AbstractCheck check = (AbstractCheck) checkObject;
            Object enabled = ConfigValues.checkEnabledByName.get(check.getCheckName());
            if (enabled instanceof Boolean && !((Boolean) enabled).booleanValue()) {
                continue;
            }
            check.handle(event, timestamp);
        }
    }

    private void updateTrackers(PlayerData playerData, long timestamp, PacketReceiveEvent event) {
        PacketTypeCommon packetType = event.getPacketType();
        if (isFlying(packetType)) {
            WrapperPlayClientPlayerFlying flying = new WrapperPlayClientPlayerFlying(event);
            playerData.getBlockBreakTracker().handleFlyingPacket(flying, timestamp);
            playerData.getStatusTracker().handleFlyingPacket(flying, timestamp);
            playerData.getTickTracker().handleFlyingPacket();
            playerData.getActionTracker().handleRotation(timestamp, flying.getYaw(), flying.getPitch());
            playerData.getReachEntityHelper().handleFlying();
            playerData.getPacketHandler().handleFlying();
            return;
        }
        if (packetType == PacketType.Play.Client.CLIENT_TICK_END) {
            playerData.getTickTracker().handleFlyingPacket();
            return;
        }
        if (packetType == PacketType.Play.Client.ENTITY_ACTION) {
            WrapperPlayClientEntityAction wrapper = new WrapperPlayClientEntityAction(event);
            playerData.getStatusTracker().handleEntityAction(wrapper);
            return;
        }
        if (packetType == PacketType.Play.Client.CLIENT_STATUS) {
            playerData.getStatusTracker().handleClientStatus(new WrapperPlayClientClientStatus(event));
            return;
        }
        if (packetType == PacketType.Play.Client.PLAYER_DIGGING) {
            WrapperPlayClientPlayerDigging wrapper = new WrapperPlayClientPlayerDigging(event);
            playerData.setLastDigPacket(wrapper);
            playerData.getStatusTracker().handlePlayerDigging(wrapper);
            playerData.getBlockBreakTracker().handleReleaseUseItem(wrapper);
            return;
        }
        if (packetType == PacketType.Play.Client.VEHICLE_MOVE) {
            WrapperPlayClientVehicleMove wrapper = new WrapperPlayClientVehicleMove(event);
            playerData.getBlockBreakTracker().handleVehicleMove(wrapper, timestamp);
            return;
        }
        if (packetType == PacketType.Play.Client.INTERACT_ENTITY || packetType == PacketType.Play.Client.ATTACK) {
            WrapperPlayClientInteractEntity wrapper = new WrapperPlayClientInteractEntity(event);
            playerData.setLastInteractPacket(wrapper);
            playerData.getReachEntityHelper().handleInteractEntity(wrapper, timestamp);
            playerData.getStatusTracker().handleInteractEntityAttack(wrapper);
            return;
        }
        if (packetType == PacketType.Play.Client.PLAYER_BLOCK_PLACEMENT) {
            playerData.setLastBlockPlacePacket(new WrapperPlayClientPlayerBlockPlacement(event));
            return;
        }
        if (packetType == PacketType.Play.Client.ANIMATION) {
            playerData.getClickTracker().handleClick();
            return;
        }
        if (packetType == PacketType.Play.Client.PONG) {
            playerData.getPacketHandler().handlePong(new com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPong(event).getId());
            playerData.getClickTracker().handleConfirmation();
            return;
        }
        if (packetType == PacketType.Play.Client.CLICK_WINDOW) {
            playerData.getStatusTracker().handleWindowClick();
        }
        if (packetType == PacketType.Play.Client.WINDOW_CONFIRMATION) {
            playerData.getPacketHandler().handlePong(new com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientWindowConfirmation(event).getActionId());
            playerData.getClickTracker().handleConfirmation();
        }
    }

    private static boolean isFlying(PacketTypeCommon packetType) {
        return packetType == PacketType.Play.Client.PLAYER_FLYING
            || packetType == PacketType.Play.Client.PLAYER_POSITION
            || packetType == PacketType.Play.Client.PLAYER_POSITION_AND_ROTATION
            || packetType == PacketType.Play.Client.PLAYER_ROTATION;
    }

    private static void lambda$handle$0(PlayerData playerRef, Location location) {
        if (playerRef.getPlayer() == null) {
            return;
        }
        playerRef.getPlayer().teleport(location, PlayerTeleportEvent.TeleportCause.UNKNOWN);
    }
}
