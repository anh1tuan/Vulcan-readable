package me.frep.vulcan.spigot;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChangeGameState;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityEffect;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityRelativeMove;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityRelativeMoveAndRotation;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityVelocity;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerExplosion;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPing;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerAbilities;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerPositionAndLook;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerRemoveEntityEffect;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateAttributes;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWindowConfirmation;
import java.util.concurrent.ThreadLocalRandom;

public class OutgoingPacketHandler {
    public void handlePacketSend(PlayerData playerData, PacketSendEvent event) {
        if (playerData == null || event == null) {
            return;
        }
        PacketTypeCommon packetType = event.getPacketType();
        long timestamp = event.getTimestamp();
        if (packetType == PacketType.Play.Server.JOIN_GAME) {
            this.sendJoinPing(playerData);
            return;
        }
        if (packetType == PacketType.Play.Server.ENTITY_VELOCITY) {
            playerData.getTickTracker().handleVelocityPacket(
                new WrapperPlayServerEntityVelocity(event),
                timestamp,
                event
            );
            return;
        }
        if (packetType == PacketType.Play.Server.EXPLOSION) {
            playerData.getTickTracker().handleExplosionPacket(
                timestamp,
                new WrapperPlayServerExplosion(event),
                event
            );
            return;
        }
        if (packetType == PacketType.Play.Server.PLAYER_POSITION_AND_LOOK) {
            playerData.getStatusTracker().handleServerPosition(
                new WrapperPlayServerPlayerPositionAndLook(event),
                event
            );
            return;
        }
        if (packetType == PacketType.Play.Server.ENTITY_METADATA) {
            playerData.getStatusTracker().handleEntityMetadata(new WrapperPlayServerEntityMetadata(event));
            return;
        }
        if (packetType == PacketType.Play.Server.UPDATE_ATTRIBUTES) {
            playerData.getStatusTracker().handleUpdateAttributes(new WrapperPlayServerUpdateAttributes(event));
            return;
        }
        if (packetType == PacketType.Play.Server.PLAYER_ABILITIES) {
            playerData.getBlockBreakTracker().handleAbilities(
                new WrapperPlayServerPlayerAbilities(event), timestamp);
            return;
        }
        if (packetType == PacketType.Play.Server.ENTITY_EFFECT) {
            playerData.getStatusTracker().handleEntityEffect(new WrapperPlayServerEntityEffect(event));
            return;
        }
        if (packetType == PacketType.Play.Server.REMOVE_ENTITY_EFFECT) {
            playerData.getStatusTracker().handleRemoveEntityEffect(new WrapperPlayServerRemoveEntityEffect(event));
            return;
        }
        if (packetType == PacketType.Play.Server.CHANGE_GAME_STATE) {
            playerData.getStatusTracker().handleGameStateChange(new WrapperPlayServerChangeGameState(event));
            return;
        }
        if (packetType == PacketType.Play.Server.ENTITY_RELATIVE_MOVE) {
            playerData.getReachEntityHelper().handleRelativeMove(
                new WrapperPlayServerEntityRelativeMove(event)
            );
            return;
        }
        if (packetType == PacketType.Play.Server.ENTITY_RELATIVE_MOVE_AND_ROTATION) {
            playerData.getReachEntityHelper().handleRelativeMoveAndRotation(
                new WrapperPlayServerEntityRelativeMoveAndRotation(event)
            );
        }
    }

    private void sendJoinPing(PlayerData playerData) {
        int pingId = ThreadLocalRandom.current().nextInt(-11500, -11000);
        PacketWrapper pingPacket;
        if (ServerCompatibility.isServer1_17OrNewer()) {
            pingPacket = new WrapperPlayServerPing(pingId);
        } else {
            pingPacket = new WrapperPlayServerWindowConfirmation(0, (short) pingId, false);
        }
        if (ConfigValues.connectionDebugMessages) {
            ServerCompatibility.logTranslatedInfo(
                "Join game PING sent with ID=" + pingId + " on " + playerData.getUsername()
            );
        }
        playerData.getClickTracker().setLastTransactionId(pingId);
        playerData.getUser().sendPacket(pingPacket);
    }
}
