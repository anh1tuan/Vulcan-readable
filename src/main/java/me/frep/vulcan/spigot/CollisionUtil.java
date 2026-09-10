package me.frep.vulcan.spigot;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerHeldItemChange;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPing;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerPositionAndLook;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWindowConfirmation;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffectType;


public final class CollisionUtil {
    public static void shuffleHotbar(Player player) {
        int slot = ThreadLocalRandom.current().nextInt(8);
        CollisionUtil.sendPacketToPlayer(
            VulcanBootstrap.INSTANCE.getHookManager().getPlayerData(player),
            new WrapperPlayServerHeldItemChange(slot)
        );
    }

    public static void applyRandomRotation(PlayerData playerData) {
        Player player = playerData.getPlayer();
        if (player == null || !VulcanBootstrap.INSTANCE.isFolia()) {
            return;
        }
        Location location = player.getLocation();
        float yaw = (float) ThreadLocalRandom.current().nextDouble(360.0) - 180.0f;
        float pitch = (float) ThreadLocalRandom.current().nextDouble(180.0) - 90.0f;
        WrapperPlayServerPlayerPositionAndLook packet = new WrapperPlayServerPlayerPositionAndLook(
            location.getX(), location.getY(), location.getZ(), yaw, pitch, 0, 69, false);
        playerData.getUser().sendPacket(packet);
    }


    public static void sendPacketToPlayer(PlayerData playerData, PacketWrapper packetWrapper) {
        PacketEvents.getAPI().getProtocolManager().sendPacket(playerData.getUser().getChannel(), packetWrapper);
    }

    public static double collisionDouble(Player p1, Player p2) { return 0.0; }

    public static int getPlayerPing(Player player) {
        try {
            return PacketEvents.getAPI().getPlayerManager().getPing((Object) player);
        } catch (Exception ignored) {
            return 0;
        }
    }

    public static boolean isClientAtLeast1_17(PlayerData playerData) {
        return playerData.getUser().getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_17);
    }

    public static boolean isClientNewerThan1_9(PlayerData playerData) {
        return playerData.getUser().getClientVersion().isNewerThan(ClientVersion.V_1_9);
    }

    public static boolean isClientAtLeast1_13(PlayerData playerData) {
        return playerData.getUser().getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_13);
    }

    public static void sendWindowConfirmation(PlayerData playerData, short windowId) {
        CollisionUtil.sendPacketToPlayer(playerData, new WrapperPlayServerWindowConfirmation(0, windowId, false));
    }

    public static void sendPingPacket(PlayerData playerData, int pingId) {
        CollisionUtil.sendPacketToPlayer(playerData, new WrapperPlayServerPing(pingId));
    }

    public static boolean listMatches(List column, Predicate<Material> predicate, int... indexes) {
        if (column == null || column.isEmpty() || predicate == null) {
            return false;
        }
        for (int index : indexes) {
            if (index < 0 || index >= column.size()) {
                continue;
            }
            Object raw = column.get(index);
            if (raw instanceof Material && predicate.test((Material) raw)) {
                return true;
            }
        }
        return false;
    }

    public static boolean modernColumnMatches(PlayerData playerData, Predicate<Material> predicate, int... indexes) {
        return CollisionUtil.listMatches(playerData.getBlockBreakTracker().getNearbyColumnMaterials(), predicate, indexes);
    }

    public static boolean legacyColumnMatches(PlayerData playerData, Predicate<Material> predicate, int... indexes) {
        return CollisionUtil.listMatches(playerData.getBlockBreakTracker().getNearbyMaterials(), predicate, indexes);
    }

    public static boolean isSolidMaterial(Material material) {
        return material != null && material.isSolid();
    }

    public static boolean isSwimming(PlayerData playerData) {
        return playerData.getPlayer() != null && playerData.getPlayer().isSwimming();
    }

    public static boolean hasModernSolidColumn(PlayerData playerData) {
        return CollisionUtil.modernColumnMatches(playerData, CollisionUtil::isSolidMaterial, 16, 19);
    }

    public static boolean hasLegacySolidColumn(PlayerData playerData) {
        return CollisionUtil.legacyColumnMatches(playerData, CollisionUtil::isSolidMaterial, 16, 19);
    }

    public static boolean hasDolphinsGrace(PlayerData playerData) {
        return playerData.getPlayer() != null
            && playerData.getPlayer().hasPotionEffect(PotionEffectType.DOLPHINS_GRACE);
    }



    public static boolean isInBubbleColumn(PlayerData playerData) {
        if (ServerCompatibility.isModernProtocol()) {
            return CollisionUtil.modernColumnMatches(playerData, BoundingBoxUtil::isBubbleColumn, 16, 19);
        }
        return CollisionUtil.legacyColumnMatches(playerData, BoundingBoxUtil::isBubbleColumn, 16, 19);
    }

    public static boolean hasModernBubbleColumn(PlayerData playerData) {
        return CollisionUtil.modernColumnMatches(playerData, BoundingBoxUtil::isBubbleColumn, 16, 19);
    }

    public static boolean hasLegacyBubbleColumn(PlayerData playerData) {
        return CollisionUtil.legacyColumnMatches(playerData, BoundingBoxUtil::isBubbleColumn, 16, 19);
    }

    private static List sampleBlocks(List source, int... indexes) {
        java.util.LinkedList sampled = new java.util.LinkedList();
        if (source == null || source.isEmpty()) {
            return sampled;
        }
        for (int index : indexes) {
            if (index >= 0 && index < source.size()) {
                sampled.add(source.get(index));
            }
        }
        return sampled;
    }

    public static List sampleFeetColumnBlocks(PlayerData playerData) {
        return CollisionUtil.sampleBlocks(playerData.getBlockBreakTracker().getNearbyColumnMaterials(), 0, 1, 2, 12, 13, 14, 24, 25, 26);
    }

    public static List sampleHeadColumnBlocks(PlayerData playerData) {
        return CollisionUtil.sampleBlocks(playerData.getBlockBreakTracker().getNearbyColumnMaterials(), 6, 7, 8, 18, 19, 20, 30, 31, 32);
    }

    public static List sampleNearbyHeadBlocks(PlayerData playerData) {
        return CollisionUtil.sampleBlocks(playerData.getBlockBreakTracker().getNearbyMaterials(), 6, 7, 8, 18, 19, 20, 30, 31, 32);
    }

    public static List sampleNearbyFeetBlocks(PlayerData playerData) {
        return CollisionUtil.sampleBlocks(playerData.getBlockBreakTracker().getNearbyMaterials(), 0, 1, 2, 12, 13, 14, 24, 25, 26);
    }

    public static List sampleMiddleColumnBlocks(PlayerData playerData) {
        return CollisionUtil.sampleBlocks(
            playerData.getBlockBreakTracker().getNearbyColumnMaterials(),
            3, 4, 5, 6, 7, 8, 15, 16, 17, 18, 19, 20, 27, 28, 29, 30, 31, 32
        );
    }

    public static List sampleMiddleNearbyBlocks(PlayerData playerData) {
        return CollisionUtil.sampleBlocks(
            playerData.getBlockBreakTracker().getNearbyMaterials(),
            3, 4, 5, 6, 7, 8, 15, 16, 17, 18, 19, 20, 27, 28, 29, 30, 31, 32
        );
    }

    public static List sampleBelowNearbyBlocks(PlayerData playerData) {
        return CollisionUtil.sampleBlocks(playerData.getBlockBreakTracker().getNearbyMaterials(), 9, 10, 11, 21, 22, 23, 33, 34, 35);
    }

    public static List sampleBelowColumnBlocks(PlayerData playerData) {
        return CollisionUtil.sampleBlocks(playerData.getBlockBreakTracker().getNearbyColumnMaterials(), 9, 10, 11, 21, 22, 23, 33, 34, 35);
    }

    public static boolean isRiptiding(PlayerData playerData) {
        return playerData.getPlayer() != null && playerData.getPlayer().isRiptiding();
    }



    public static boolean isGliding(PlayerData playerData) {
        return playerData.getPlayer() != null
            && ServerCompatibility.isServer1_9OrNewer()
            && playerData.getPlayer().isGliding();
    }



    public static boolean hasModernIceColumn(PlayerData playerData) {
        return CollisionUtil.modernColumnMatches(
            playerData,
            BoundingBoxUtil::isIceVariant,
            13, 16, 19
        );
    }

    public static boolean hasLegacyIceColumn(PlayerData playerData) {
        return CollisionUtil.legacyColumnMatches(
            playerData,
            BoundingBoxUtil::isIceVariant,
            13, 16, 19
        );
    }

    public static boolean hasModernCobwebColumn(PlayerData playerData) {
        return CollisionUtil.modernColumnMatches(
            playerData,
            BoundingBoxUtil::isCobweb,
            16, 19
        );
    }

    public static boolean hasLegacyCobwebColumn(PlayerData playerData) {
        return CollisionUtil.legacyColumnMatches(
            playerData,
            BoundingBoxUtil::isCobweb,
            16, 19
        );
    }

    public static boolean hasModernLiquidColumn(PlayerData playerData) {
        return CollisionUtil.modernColumnMatches(playerData, BoundingBoxUtil::isLiquid, 16, 19);
    }

    public static boolean hasLegacyLiquidColumn(PlayerData playerData) {
        return CollisionUtil.legacyColumnMatches(playerData, BoundingBoxUtil::isLiquid, 16, 19);
    }

    public static boolean hasModernClimbableColumn(PlayerData playerData) {
        return CollisionUtil.modernColumnMatches(
            playerData,
            BoundingBoxUtil::isClimbableMaterial,
            16, 19
        );
    }

    public static boolean hasLegacyClimbableColumn(PlayerData playerData) {
        return CollisionUtil.legacyColumnMatches(
            playerData,
            BoundingBoxUtil::isClimbableMaterial,
            16, 19
        );
    }

    public static boolean isIceInColumn(PlayerData playerData) {
        return ServerCompatibility.isModernProtocol()
            ? CollisionUtil.hasModernIceColumn(playerData)
            : CollisionUtil.hasLegacyIceColumn(playerData);
    }

    public static boolean isCobwebInColumn(PlayerData playerData) {
        return ServerCompatibility.isModernProtocol()
            ? CollisionUtil.hasModernCobwebColumn(playerData)
            : CollisionUtil.hasLegacyCobwebColumn(playerData);
    }

    public static boolean isLiquidInColumn(PlayerData playerData) {
        return ServerCompatibility.isModernProtocol()
            ? CollisionUtil.hasModernLiquidColumn(playerData)
            : CollisionUtil.hasLegacyLiquidColumn(playerData);
    }

    public static boolean isClimbableInColumn(PlayerData playerData) {
        return ServerCompatibility.isModernProtocol()
            ? CollisionUtil.hasModernClimbableColumn(playerData)
            : CollisionUtil.hasLegacyClimbableColumn(playerData);
    }

    public static boolean isSolidPathPlace(BlockPlaceEvent event) {
        Material type = event.getBlock().getType();
        return type.isSolid() && BoundingBoxUtil.isPath(type);
    }





    public static boolean isHoldingRiptide(PlayerData playerData) {
        if (playerData.getPlayer() == null || !ServerCompatibility.isModernProtocol()) {
            return false;
        }
        Player player = playerData.getPlayer();
        return player.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.RIPTIDE) > 0
            || player.getInventory().getItemInOffHand().getEnchantmentLevel(Enchantment.RIPTIDE) > 0;
    }





    public static float computeReach(PlayerData playerData, float baseReach) {
        float extra = (float) playerData.getStatusTracker().getEntityInteractionRange() * 0.0675f
            + (playerData.getBlockBreakTracker().getFlySpeed() - 0.2f) * 3.5f;
        float reach = baseReach + extra;
        if (reach < baseReach) {
            return baseReach + (float) playerData.getStatusTracker().getEntityInteractionRange() * 0.0675f;
        }
        return reach;
    }

    public static void handleCollision(Player player) {
        PlayerData playerData = VulcanBootstrap.INSTANCE.getHookManager().getPlayerData(player);
        if (playerData == null || !VulcanBootstrap.INSTANCE.isFolia()) {
            return;
        }
        Location location = player.getLocation();
        float yaw = (float) ThreadLocalRandom.current().nextDouble(360.0) - 180.0f;
        float pitch = (float) ThreadLocalRandom.current().nextDouble(180.0) - 90.0f;
        WrapperPlayServerPlayerPositionAndLook packet = new WrapperPlayServerPlayerPositionAndLook(
            location.getX(), location.getY(), location.getZ(), yaw, pitch, 0, 69, false);
        playerData.getUser().sendPacket(packet);
    }

    private CollisionUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    private static void lambda$rotateRandomly$1(Player player, Location location) {
        player.teleport(location, PlayerTeleportEvent.TeleportCause.UNKNOWN);
    }

    private static void lambda$rotateRandomly$0(PlayerData playerData, Location location) {
        playerData.getPlayer().teleport(location, PlayerTeleportEvent.TeleportCause.UNKNOWN);
    }
}
