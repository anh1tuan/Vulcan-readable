package me.frep.vulcan.spigot;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.event.UserConnectEvent;
import com.github.retrooper.packetevents.event.UserDisconnectEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.configuration.client.WrapperConfigClientPluginMessage;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPluginMessage;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.geysermc.floodgate.api.FloodgateApi;

public class PacketListenerImpl implements PacketListener {
    private static final String MODERN_BRAND_CHANNEL = "minecraft:brand";
    private static final String LEGACY_BRAND_CHANNEL = "MC|Brand";
    private static final String GEYSER_JAVA_UUID_PREFIX = "00000000-0000-0000-0009";

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        PacketTypeCommon packetType = event.getPacketType();
        if (packetType == PacketType.Configuration.Client.PLUGIN_MESSAGE) {
            WrapperConfigClientPluginMessage wrapper = new WrapperConfigClientPluginMessage(event);
            this.handleBrandPluginMessage(wrapper.getChannelName(), wrapper.getData(), event.getUser().getUUID());
        }
        if (!(packetType instanceof PacketType.Play.Client)) {
            return;
        }
        User user = event.getUser();
        if (this.shouldIgnoreUser(user)) {
            return;
        }
        if (packetType == PacketType.Play.Client.PLUGIN_MESSAGE) {
            WrapperPlayClientPluginMessage wrapper = new WrapperPlayClientPluginMessage(event);
            this.handleBrandPluginMessage(wrapper.getChannelName(), wrapper.getData(), user.getUUID());
        }
        PlayerData playerData = VulcanBootstrap.INSTANCE.getHookManager().getPlayerDataByUser(user);
        if (playerData == null) {
            user.closeConnection();
            return;
        }
        Player player = playerData.getPlayer();
        if (player == null) {
            player = (Player) event.getPlayer();
        }
        if (player != null) {
            playerData.setPlayer(player);
        }
        VulcanBootstrap.INSTANCE.getPunishmentManager().handlePacket(playerData, event.getTimestamp(), event);
    }

    @Override
    public void onPacketSend(PacketSendEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.SYSTEM_CHAT_MESSAGE) {
            return;
        }
        User user = event.getUser();
        if (this.shouldIgnoreUser(user)) {
            return;
        }
        if (event.getPacketType() == PacketType.Login.Server.LOGIN_SUCCESS) {
            event.getTasksAfterSend().add(() -> VulcanBootstrap.INSTANCE.getHookManager().registerUser(user));
        }
        if (!(event.getPacketType() instanceof PacketType.Play.Server)) {
            return;
        }
        PlayerData playerData = VulcanBootstrap.INSTANCE.getHookManager().getPlayerDataByUser(user);
        if (playerData == null) {
            user.closeConnection();
            return;
        }
        Player player = playerData.getPlayer();
        if (player == null) {
            player = (Player) event.getPlayer();
        }
        if (player != null) {
            playerData.setPlayer(player);
        }
        VulcanBootstrap.INSTANCE.getOutgoingPacketHandler().handlePacketSend(playerData, event);
    }

    @Override
    public void onUserDisconnect(UserDisconnectEvent event) {
        User user = event.getUser();
        if (user == null || user.getUUID() == null) {
            return;
        }
        VulcanBootstrap.INSTANCE.getHookManager().unregisterPlayer(user.getUUID());
    }

    @Override
    public void onUserConnect(UserConnectEvent event) {
        // PlayerData is created after LOGIN_SUCCESS; connect itself is not cancelled.
    }

    private boolean shouldIgnoreUser(User user) {
        if (user == null || user.getUUID() == null) {
            return true;
        }
        if (ConfigValues.settingsIgnoreFloodgate
                && VulcanBootstrap.INSTANCE.isFloodgateV2Present()
                && FloodgateApi.getInstance().isFloodgatePlayer(user.getUUID())) {
            return true;
        }
        if (ConfigValues.settingsIgnoreGeyserUuids
                && user.getUUID().toString().startsWith(GEYSER_JAVA_UUID_PREFIX)) {
            return true;
        }
        return ConfigValues.settingsIgnoreGeyserPrefixes
            && user.getName() != null
            && user.getName().startsWith(ConfigValues.settingsIgnoreGeyserPrefix);
    }

    private void handleBrandPluginMessage(String channel, byte[] data, UUID uuid) {
        if (channel == null || data == null || uuid == null) {
            return;
        }
        if (!channel.equalsIgnoreCase(MODERN_BRAND_CHANNEL) && !channel.equals(LEGACY_BRAND_CHANNEL)) {
            return;
        }
        try {
            String brand = new String(data, StandardCharsets.UTF_8);
            if (!brand.isEmpty() && brand.charAt(0) < 32) {
                brand = brand.substring(1);
            }
            brand = brand.replace("\0", "").trim();
            VulcanBootstrap.INSTANCE.getClientBrandByUuid().put(uuid, brand);
        } catch (Exception ignored) {
        }
    }
}
