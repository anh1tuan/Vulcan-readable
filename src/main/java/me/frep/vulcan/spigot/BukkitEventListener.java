package me.frep.vulcan.spigot;

import io.github.retrooper.packetevents.util.folia.FoliaScheduler;
import me.frep.vulcan.spigot.check.AbstractCheck;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.NumberConversions;


public class BukkitEventListener
implements Listener {
    @EventHandler(priority=EventPriority.MONITOR)
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (!(event.getEntity() instanceof Firework)) {
            return;
        }
        Firework firework = (Firework) event.getEntity();
        if (!(firework.getShooter() instanceof Player)) {
            return;
        }
        PlayerData playerData = VulcanBootstrap.INSTANCE.getHookManager().getPlayerData((Player) firework.getShooter());
        if (playerData == null) {
            return;
        }
        playerData.getBlockBreakTracker().setTicksSinceFirework(0);
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event) {
        PlayerData playerData = VulcanBootstrap.INSTANCE.getHookManager().getPlayerData(event.getPlayer());
        if (playerData == null) {
            return;
        }
        StatusTracker statusTracker = playerData.getStatusTracker();
        statusTracker.resetTicksSinceBlockPlace();
        playerData.getBlockBreakTracker().setTicksSinceBlockPlace(0);
        if (event.isCancelled()) {
            statusTracker.resetTicksSinceCancelledMove();
        }
        Material type = event.getBlock().getType();
        if (BoundingBoxUtil.isIceVariant(type)) {
            statusTracker.resetTicksSinceIcePlace();
        }
        if (BoundingBoxUtil.isClimbableMaterial(type)) {
            statusTracker.resetTicksSinceClimbablePlace();
        }
        if (BoundingBoxUtil.isCobweb(type)) {
            statusTracker.resetTicksSinceCobwebPlace();
        }
        if (playerData.getBlockBreakTracker().isFrozen()) {
            event.getPlayer().leaveVehicle();
            event.setCancelled(true);
        }
        if (BoundingBoxUtil.isScaffolding(type)) {
            statusTracker.onBlockPlace(event);
        }
        if (playerData.getChecks() != null) {
            for (Object raw : playerData.getChecks()) {
                ((AbstractCheck) raw).handleEvent(event);
            }
        }
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent event) {
        PlayerData playerData = VulcanBootstrap.INSTANCE.getHookManager().getPlayerData(event.getPlayer());
        if (playerData == null) {
            return;
        }
        StatusTracker statusTracker = playerData.getStatusTracker();
        statusTracker.markBukkitBlockBreakTick();
        if (playerData.getBlockBreakTracker().isFrozen()) {
            event.setCancelled(true);
        }
        if (event.isCancelled()) {
            statusTracker.markCancelledBreak();
        }
        if ("FIRE".equals(event.getBlock().getType().toString())) {
            statusTracker.setTicksSinceFireBreak(0);
        }
        statusTracker.handleBukkitBlockBreak();
        if (playerData.getChecks() != null) {
            for (Object raw : playerData.getChecks()) {
                ((AbstractCheck) raw).handleEvent(event);
            }
        }
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        PlayerData playerData = VulcanBootstrap.INSTANCE.getHookManager().getPlayerData(event.getPlayer());
        if (playerData == null) {
            return;
        }
        playerData.getStatusTracker().markItemPickupTick();
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        PlayerData playerData = VulcanBootstrap.INSTANCE.getHookManager().getPlayerData(event.getPlayer());
        if (playerData == null) {
            return;
        }
        if (event.getItem() != null && event.getItem().getType().toString().contains("CHORUS")) {
            playerData.getStatusTracker().resetTicksSinceChorusFruit();
        }
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        PlayerData playerData = VulcanBootstrap.INSTANCE.getHookManager().getPlayerData(event.getPlayer());
        if (playerData == null) {
            return;
        }
        playerData.getStatusTracker().markBedEnterTick();
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerBedLeave(PlayerBedLeaveEvent event) {
        PlayerData playerData = VulcanBootstrap.INSTANCE.getHookManager().getPlayerData(event.getPlayer());
        if (playerData == null) {
            return;
        }
        playerData.getStatusTracker().markFlyingTick();
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent event) {
        PlayerData playerData = VulcanBootstrap.INSTANCE.getHookManager().getPlayerData(event.getPlayer());
        if (playerData == null) {
            return;
        }
        Action action = event.getAction();
        if (action == Action.LEFT_CLICK_BLOCK && playerData.getChecks() != null) {
            for (Object raw : playerData.getChecks()) {
                ((AbstractCheck) raw).handleEvent(event);
            }
        }
        ItemStack item = event.getItem();
        if ((action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)
            && item != null
            && "GLASS_BOTTLE".equals(item.getType().toString())) {
            playerData.getStatusTracker().resetTicksSinceChorusFruit();
        }
        Block clicked = event.getClickedBlock();
        if (action == Action.RIGHT_CLICK_BLOCK
            && clicked != null
            && "NOTE_BLOCK".equals(clicked.getType().toString())) {
            playerData.getClickTracker().setLastNpcOrNoteBlockInteractTime(System.currentTimeMillis());
        }
        if (action == Action.RIGHT_CLICK_BLOCK && item != null && item.getType().toString().contains("BUCKET")) {
            playerData.getStatusTracker().resetTicksSinceChorusFruit();
        }
        if (action == Action.RIGHT_CLICK_BLOCK
            && item != null
            && item.getType().toString().contains("_BUCKET")
            && clicked != null
            && !BoundingBoxUtil.isScaffolding(clicked.getType())) {
            Player player = event.getPlayer();
            double distance = TrigUtil.hypot3(
                Math.abs((double) clicked.getX() - player.getLocation().getX()),
                Math.abs((double) clicked.getY() - player.getLocation().getY()),
                Math.abs((double) clicked.getZ() - player.getLocation().getZ())
            );
            if (distance < 2.25) {
                playerData.getStatusTracker().resetTicksSinceChorusFruit();
            }
            playerData.getStatusTracker().markBucketEmptyTick();
        }
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerDeath(PlayerDeathEvent event) {
        PlayerData playerData = VulcanBootstrap.INSTANCE.getHookManager().getPlayerData(event.getEntity());
        if (playerData == null) {
            return;
        }
        playerData.getStatusTracker().handleDeath();
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        if (event.isCancelled()) {
            return;
        }
        Player player = (Player) event.getEntity();
        PlayerData playerData = VulcanBootstrap.INSTANCE.getHookManager().getPlayerData(player);
        if (playerData == null) {
            return;
        }
        if (playerData.getBlockBreakTracker().isFrozen()) {
            event.setCancelled(true);
        }
        playerData.getStatusTracker().onEntityDamage(event.getCause());
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent playerInteractEntityEvent) {
        Player player = playerInteractEntityEvent.getPlayer();
        PlayerData playerData = VulcanBootstrap.INSTANCE.getHookManager().getPlayerData(player);
        if (playerData == null) {
            return;
        }

        switch (playerInteractEntityEvent.getRightClicked().getType()) {
            case VILLAGER:
            case COW:
            case CHICKEN:
            case WOLF:
                playerData.getClickTracker().setLastNpcOrNoteBlockInteractTime(System.currentTimeMillis());
                return;
            case ITEM_FRAME:
                playerData.getClickTracker().setLastItemFrameInteractTime(System.currentTimeMillis());
                return;
            default:
                return;
        }
    }
    @EventHandler(priority=EventPriority.MONITOR)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (event.getEntity() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getEntity();
            if (!(arrow.getShooter() instanceof Player)) {
                return;
            }
            PlayerData playerData = VulcanBootstrap.INSTANCE.getHookManager().getPlayerData((Player) arrow.getShooter());
            if (playerData == null || playerData.getPlayer() == null || playerData.getChecks() == null) {
                return;
            }
            for (Object raw : playerData.getChecks()) {
                ((AbstractCheck) raw).handleEvent(event);
            }
            return;
        }
        if (event.getEntity() instanceof Snowball
            || event.getEntity() instanceof Egg
            || event.getEntity() instanceof ThrownExpBottle) {
            ProjectileSource shooter = ((Projectile) event.getEntity()).getShooter();
            if (!(shooter instanceof Player)) {
                return;
            }
            PlayerData playerData = VulcanBootstrap.INSTANCE.getHookManager().getPlayerData((Player) shooter);
            if (playerData == null) {
                return;
            }
            playerData.getStatusTracker().markProjectileLaunchTick();
        }
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        PlayerData playerData = VulcanBootstrap.INSTANCE.getHookManager().getPlayerData(event.getPlayer());
        if (playerData == null || event.getTo() == null) {
            return;
        }
        PlayerTeleportEvent.TeleportCause cause = event.getCause();
        if (cause == PlayerTeleportEvent.TeleportCause.ENDER_PEARL
            && playerData.getBlockBreakTracker().isFrozen()) {
            event.setCancelled(true);
        }
        if (cause == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            playerData.getStatusTracker().resetTicksSincePearlTeleport();
        } else if (cause != PlayerTeleportEvent.TeleportCause.UNKNOWN) {
            playerData.getStatusTracker().resetTicksSinceNonPearlTeleport();
        }
        if (!ServerCompatibility.isModernProtocol()) {
            return;
        }
        Location to = event.getTo();
        World world = to.getWorld();
        if (world == null) {
            return;
        }
        int x = NumberConversions.floor(to.getX());
        int y = NumberConversions.floor(to.getY());
        int z = NumberConversions.floor(to.getZ());
        if (!world.isChunkLoaded(x >> 4, z >> 4)) {
            return;
        }
        playerData.getBlockBreakTracker().setNearbyMaterials(BoundingBoxUtil.getNearbyMaterials(world, x, y, z));
        playerData.getBlockBreakTracker().setGhostBlockAirTicks(0);
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        PlayerData playerData = VulcanBootstrap.INSTANCE.getHookManager().getPlayerData(event.getPlayer());
        if (playerData == null) {
            return;
        }
        playerData.getStatusTracker().resetTicksSinceWorldChange();
    }
    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.getCaught() instanceof Boat) {
            Boat boat = (Boat) event.getCaught();
            VulcanBootstrap.INSTANCE.getEntityIdMap().put(
                Integer.valueOf(boat.getEntityId()),
                Integer.valueOf(VulcanBootstrap.INSTANCE.getKeepAliveRunnable().getCurrentTick())
            );
        }
        PlayerData playerData = VulcanBootstrap.INSTANCE.getHookManager().getPlayerData(event.getPlayer());
        if (playerData == null) {
            return;
        }
        playerData.getStatusTracker().markHookTick();
    }

    @EventHandler
    public void onBlockDamage(BlockDamageEvent blockDamageEvent) {
        Player player = blockDamageEvent.getPlayer();
        PlayerData playerData = VulcanBootstrap.INSTANCE.getHookManager().getPlayerData(player);

        if (playerData == null) {
            return;
        }

        if (blockDamageEvent.getInstaBreak()) {
            playerData.getStatusTracker().handleBukkitBlockBreak(true);
        }
    }
    @EventHandler(priority=EventPriority.MONITOR)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player attacker = (Player) event.getDamager();
            PlayerData attackerData = VulcanBootstrap.INSTANCE.getHookManager().getPlayerData(attacker);
            if (attackerData != null
                && System.currentTimeMillis() - attackerData.getJoinTime() - ConfigValues.joinCheckWaitTime < 0L) {
                event.setDamage(0.0);
                if (ConfigValues.cancelHitsCancelEvent) {
                    event.setCancelled(true);
                }
            }
        }
        if (event.getEntity() instanceof Player) {
            Player victim = (Player) event.getEntity();
            if (victim.hasMetadata("NPC") || victim.hasMetadata("npc")) {
                return;
            }
            PlayerData victimData = VulcanBootstrap.INSTANCE.getHookManager().getPlayerData(victim);
            if (victimData == null) {
                return;
            }
            victimData.getStatusTracker().recordDamagerType(event.getDamager().getType());
        }
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player) || event.isCancelled()) {
            return;
        }
        Player attacker = (Player) event.getDamager();
        Player victim = (Player) event.getEntity();
        PlayerData attackerData = VulcanBootstrap.INSTANCE.getHookManager().getPlayerData(attacker);
        PlayerData victimData = VulcanBootstrap.INSTANCE.getHookManager().getPlayerData(victim);
        if (attackerData == null || victimData == null) {
            return;
        }
        if (attackerData.getBlockBreakTracker().isFrozen() || victimData.getBlockBreakTracker().isFrozen()) {
            event.setCancelled(true);
        }
        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            attackerData.getReachEntityHelper().resetReachState();
            victimData.getReachEntityHelper().markAttackStartTick();
        }
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();
        if (to == null
            || from.getX() == to.getX() && from.getY() == to.getY() && from.getZ() == to.getZ()) {
            return;
        }
        Player player = event.getPlayer();
        PlayerData playerData = VulcanBootstrap.INSTANCE.getHookManager().getPlayerData(player);
        if (playerData == null) {
            return;
        }
        if (event.isCancelled()) {
            playerData.getStatusTracker().markCancelledMove();
        }
        BlockBreakTracker tracker = playerData.getBlockBreakTracker();
        if (tracker.isFrozen()) {
            if (ConfigValues.ghostBlocksFixMessageEnabled && ConfigValues.ghostBlocksFixMessage != null) {
                player.sendMessage(ColorUtil.translateColors(ConfigValues.ghostBlocksFixMessage));
            }
            event.setTo(new Location(
                player.getWorld(),
                tracker.getLastServerX(),
                tracker.getLastServerY(),
                tracker.getLastServerZ(),
                playerData.getActionTracker().getYaw(),
                playerData.getActionTracker().getPitch()
            ));
        }
        if (ServerCompatibility.isModernProtocol() && to.getWorld() != null) {
            int x = NumberConversions.floor(to.getX());
            int y = NumberConversions.floor(to.getY());
            int z = NumberConversions.floor(to.getZ());
            World world = to.getWorld();
            tracker.setNearbyMaterials(BoundingBoxUtil.getNearbyMaterials(world, x, y, z));
            tracker.setNearbyFeetSamples(BoundingBoxUtil.sampleBlocksTwoBelow(world, x, y, z));
            Block atFeet = to.getBlock();
            Block oneBelow = to.clone().subtract(0.0, 1.0, 0.0).getBlock();
            Block twoBelow = to.clone().subtract(0.0, 2.0, 0.0).getBlock();
            Block threeBelow = to.clone().subtract(0.0, 3.0, 0.0).getBlock();
            Block oneAbove = to.clone().add(0.0, 1.0, 0.0).getBlock();
            Block twoAbove = to.clone().add(0.0, 2.0, 0.0).getBlock();
            if (BukkitEventListener.isWaterlogged(oneBelow)
                || BukkitEventListener.isWaterlogged(atFeet)
                || BukkitEventListener.isWaterlogged(oneAbove)
                || BukkitEventListener.isWaterlogged(twoAbove)) {
                tracker.resetWaterloggedTicks();
            }
            tracker.setBlockBelow(oneBelow.getType());
            tracker.setBlockTwoBelow(twoBelow.getType());
            tracker.setBlockThreeBelow(threeBelow.getType());
        }
        if (VulcanBootstrap.INSTANCE.isFolia()) {
            int chunkX = NumberConversions.floor(to.getX()) >> 4;
            int chunkZ = NumberConversions.floor(to.getZ()) >> 4;
            FoliaScheduler.getRegionScheduler().execute(
                (Plugin) VulcanBootstrap.INSTANCE.getVulcanPlugin(),
                player.getWorld(),
                chunkX,
                chunkZ,
                () -> tracker.setNearbyEntities(player.getNearbyEntities(1.5, 2.0, 1.5))
            );
        } else {
            tracker.setNearbyEntities(player.getNearbyEntities(1.5, 2.0, 1.5));
        }
        if (VulcanBootstrap.INSTANCE.isFolia() && playerData.getChecks() != null) {
            for (Object raw : playerData.getChecks()) {
                ((AbstractCheck) raw).handleEvent(event);
            }
        }
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        PlayerData playerData = VulcanBootstrap.INSTANCE.getHookManager().getPlayerData(event.getPlayer());
        if (playerData == null) {
            return;
        }
        if (playerData.getBlockBreakTracker().isFrozen()
            && event.getReason() != null
            && event.getReason().toLowerCase().contains("flying")) {
            event.setCancelled(true);
        }
    }

    private static boolean isWaterlogged(Block block) {
        if (block == null) {
            return false;
        }
        BlockData data = block.getBlockData();
        return data instanceof Waterlogged && ((Waterlogged) data).isWaterlogged();
    }

}
