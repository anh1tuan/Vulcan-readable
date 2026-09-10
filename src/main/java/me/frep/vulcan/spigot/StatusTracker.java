package me.frep.vulcan.spigot;

import java.util.LinkedList;
import java.util.List;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.attribute.Attribute;
import com.github.retrooper.packetevents.protocol.attribute.Attributes;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.potion.PotionType;
import com.github.retrooper.packetevents.protocol.potion.PotionTypes;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClientStatus;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChangeGameState;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityEffect;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityMetadata;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerAbilities;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerPositionAndLook;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerRemoveEntityEffect;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateAttributes;
import org.bukkit.GameMode;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;


public class StatusTracker {
    private final PlayerData playerData;
    private boolean clientSneaking;
    private boolean clientSprinting;
    private boolean berserkActive;
    private boolean onGround;
    private boolean hasSpeed;
    private boolean hasJumpBoost;
    private boolean levitating;
    private boolean hasSlowFalling;
    private boolean hasDolphinsGrace;
    private boolean hasConduitPower;
    private boolean hasSlowness;
    private boolean hasDepthStrider;
    private boolean wearingElytra;
    private boolean elytraOrGliding;
    private boolean jesusFlagged;
    private boolean hasSwiftSneak;
    private boolean attributesReady;
    private boolean hasHaste;
    private boolean collisionExemptFlag;
    private boolean hasAquaAffinity;
    private boolean elytraFlyingMetadata;
    private boolean swimmingMetadata;
    private boolean riptideSpinAttack;
    private boolean instaBreak = false;
    private double walkSpeed = 0.1;
    private double stepHeight = 0.6;
    private double jumpStrength = 0.42;
    private double gravity = 0.08;
    private double lastJumpStrength = 0.42;
    private double lastGravity = 0.08;
    private double entityInteractionRange = 3.0;
    final LinkedList pendingTeleports = new LinkedList();
    private int lastBucketEmptyTick;
    private int lastShortWindowTick;
    private int lastAttackTick;
    private int ticksSinceSneaking;
        private int lastWindowClickTick;
    private int heldItemAmount;
    private int previousHeldItemAmount;
    private int heldHotbarSlot;
    private int previousHeldHotbarSlot;
    private int ticksSinceServerTeleport = 100;
    private int lastBedEnterTick;
    private int lastFlyingTick;
    private int itemPickupTick;
    private int lastBukkitBlockBreakTick;
        private int lastDamageTick;
    private int ticksSinceQueuedTeleport = 100;
    private int sneakTicks;
    private int lastClientRespawnTick;
    private int lastProjectileLaunchTick;
    private int lastHookTick;
    private int ticksSinceFireDamage = 100;
    private int ticksSinceMeleeDamage = 100;
    private int ticksSinceFallDamage = 100;
    private int ticksSinceDeath = 100;
    private int ticksSincePearlTeleport = 100;
    private int ticksSinceFireball = 100;
    private int ticksSinceCancelledBreak = 100;
    private int lastDropItemTick;
    private int ticksSinceMagicDamage;
    private int ticksSincePoisonDamage;
    private int ticksSinceContactDamage;
    private int ticksSinceProjectileDamage;
        private int ticksSinceLavaDamage;
    private int ticksSinceVelocity = 100;
    private int ticksSinceClimbablePlace = 100;
    private int ticksSinceBlockPlace = 150;
    private int ticksSinceJoin = 0;
    private int ticksSinceWorldChange = 100;
    private int ticksSinceTrackedBreak;
    private int ticksSinceNonPearlTeleport = 100;
    private int ticksSinceBrandAlert = 100;
    private int ticksSinceNonFallDamage = 100;
    private int lastPlacedBlockX;
    private int lastPlacedBlockY;
    private int lastPlacedBlockZ;
    private int ticksSinceBlockBreak;
    private int speedAmplifier = 0;
    private int jumpBoostAmplifier = 0;
    private int levitationAmplifier = 0;
    private int slowFallingAmplifier = 0;
    private int dolphinsGraceAmplifier = 0;
    private int ticksSinceChorusFruit = 100;
    private int ticksSinceCobwebPlace = 100;
    private int serverPositionLookTicks = 100;
    private int ticksSinceTrackedDamage = 100;
    private int ticksSinceIcePlace = 100;
    private int ticksSinceLastServerPos = 100;
    private int ticksLived = 0;
    private int ticksSinceDig;
    private int ticksSinceAbilities = 0;
    private int ticksSinceTotem = 100;
    private int ticksSinceLongWindow = 500;
    private int ticksSinceCancelledMove = 1000;
    private int ticksSinceFireBreak = 100;
    private int lastSpeedAmplifier = 0;
    private int soulSpeedLevel = 0;
    private int ticksWithoutAttributes = 100;
    private int slownessAmplifier = 0;
    private int ticksSinceJumpStrengthChange;
    private int ticksSinceGravityChange;
    private int ticksSinceElytraGliding = 0;
    private int ticksSinceElytraFlying = 100;
    private double lastHeldChangeX;
    private double lastHeldChangeZ;
    private double attackOffsetFromHeldChange;
    private double lastServerX;
    private double lastServerY;
    private double lastServerZ;
    private double distanceFromLastServerPos;
    private double distanceFromLastPlacedBlock;
    private double glidingDeltaXZ;
    private long lastMillisWindowTime;
    private GameMode gameMode = GameMode.SURVIVAL;
    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;
    private ItemStack mainHand;
    private ItemStack offHand;
    public StatusTracker(PlayerData playerData) {
        this.playerData = playerData;
    }
    public void handleDeath() {
        this.clientSneaking = false;
        this.clientSprinting = false;
        this.playerData.setGliding(false);
        this.ticksSinceDeath = 0;
    }

    public void handleEntityAction(WrapperPlayClientEntityAction wrapper) {
        switch (wrapper.getAction()) {
            case START_SPRINTING:
                this.clientSprinting = true;
                break;
            case STOP_SPRINTING:
                this.clientSprinting = false;
                break;
            case START_SNEAKING:
                this.clientSneaking = true;
                break;
            case STOP_SNEAKING:
                this.clientSneaking = false;
                break;
            case START_FALL_FLYING:
                this.playerData.setGliding(true);
                break;
            default:
                break;
        }
    }

    public void handleUpdateAttributes(WrapperPlayServerUpdateAttributes packet) {
        this.playerData.getClickTracker().runAfterConfirmation(() -> this.handleUpdateAttributesPacket(packet));
    }

    public void handleInteractEntityAttack(WrapperPlayClientInteractEntity packet) {
        if (packet.getAction() != WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
            return;
        }
        this.attackOffsetFromHeldChange = 1000.0;
    }

    public void handleBukkitBlockBreak() {
        this.ticksSinceBlockBreak = 0;
    }

    public void handleBukkitBlockBreak(boolean instaBreak) {
        this.ticksSinceBlockBreak = 0;
        this.instaBreak = instaBreak;
    }

    public void markItemPickupTick() {
        this.itemPickupTick = VulcanBootstrap.INSTANCE.getKeepAliveRunnable().getCurrentTick();
    }

    public void markBucketEmptyTick() {
        this.lastBucketEmptyTick = VulcanBootstrap.INSTANCE.getKeepAliveRunnable().getCurrentTick();
    }
    public void handlePlayerDigging(WrapperPlayClientPlayerDigging wrapper) {
        switch (wrapper.getAction()) {
            case START_DIGGING:
                this.ticksSinceDig = 0;
                break;
            case CANCELLED_DIGGING:
            case FINISHED_DIGGING:
                this.ticksSinceDig = 0;
                break;
            case DROP_ITEM:
            case DROP_ITEM_STACK:
                this.lastDropItemTick = VulcanBootstrap.INSTANCE.getKeepAliveRunnable().getCurrentTick();
                break;
            case RELEASE_USE_ITEM:
            case SWAP_ITEM_WITH_OFFHAND:
            default:
                break;
        }
    }
    public void onEntitySpawn() {
    }

    public void markBukkitBlockBreakTick() {
        this.lastBukkitBlockBreakTick = VulcanBootstrap.INSTANCE.getKeepAliveRunnable().getCurrentTick();
    }

    public void handleClientStatus(WrapperPlayClientClientStatus wrapperPlayClientClientStatus) {
        if (wrapperPlayClientClientStatus.getAction() == WrapperPlayClientClientStatus.Action.PERFORM_RESPAWN) {
            this.lastClientRespawnTick = VulcanBootstrap.INSTANCE.getKeepAliveRunnable().getCurrentTick();
        }
    }

    public void handleWindowClick() {
        this.lastWindowClickTick = VulcanBootstrap.INSTANCE.getKeepAliveRunnable().getCurrentTick();
        this.lastMillisWindowTime = System.currentTimeMillis();
        this.ticksSinceLongWindow = 0;
    }

    public void updateAttackState() {
        if (this.berserkActive) {
            this.lastAttackTick = VulcanBootstrap.INSTANCE.getKeepAliveRunnable().getCurrentTick();
        }
        if (this.lastHeldChangeX == 0.0) {
            return;
        }
        double x = this.playerData.getBlockBreakTracker().getLocationX();
        double z = this.playerData.getBlockBreakTracker().getLocationZ();
        this.attackOffsetFromHeldChange = TrigUtil.hypot2(
            Math.abs(x - this.lastHeldChangeX),
            Math.abs(z - this.lastHeldChangeZ)
        );
        if (this.attackOffsetFromHeldChange <= 12.0) {
            return;
        }
        this.attackOffsetFromHeldChange = 0.0;
        this.lastHeldChangeZ = 0.0;
        this.lastHeldChangeX = 0.0;
    }

    public void resetTicksSinceVelocity() {
        this.ticksSinceVelocity = 0;
    }

    public void refreshHeldItemCounts() {
        Player player = this.playerData.getPlayer();
        if (player == null) {
            this.heldItemAmount = 0;
            this.heldHotbarSlot = 0;
        } else {
            this.heldItemAmount = player.getItemInHand().getAmount();
            this.heldHotbarSlot = player.getInventory().getHeldItemSlot();
        }
        this.previousHeldHotbarSlot = this.heldHotbarSlot;
        this.previousHeldItemAmount = this.heldItemAmount;
    }

    public void recordServerPosition(double x, double y, double z) {
        if (this.berserkActive) {
            this.berserkActive = false;
        }
        this.ticksSinceLastServerPos = 0;
        this.lastServerX = x;
        this.lastServerY = y;
        this.lastServerZ = z;
    }

    public void resetTicksSinceNonPearlTeleport() {
        this.ticksSinceNonPearlTeleport = 0;
    }

    public void resetTicksSincePearlTeleport() {
        this.ticksSincePearlTeleport = 0;
    }

    public void markCancelledBreak() {
        this.ticksSinceCancelledBreak = 0;
    }

    public void handleServerPosition(WrapperPlayServerPlayerPositionAndLook packet, PacketSendEvent event) {
        this.playerData.getClickTracker().runAfterConfirmation(() -> this.handleServerPositionPacket(packet));
        this.playerData.getClickTracker().runAfterConfirmation(this.pendingTeleports::poll);
        event.getTasksAfterSend().add(this::lambda$handleServerPosition$2);
    }

    public void handleEntityEffect(WrapperPlayServerEntityEffect packet) {
        this.playerData.getClickTracker().runAfterConfirmation(() -> this.lambda$handleEntityEffect$3(packet));
    }

    public void handleRemoveEntityEffect(WrapperPlayServerRemoveEntityEffect packet) {
        this.playerData.getClickTracker().runAfterConfirmation(() -> this.lambda$handleRemoveEntityEffect$4(packet));
    }

    public void handleGameStateChange(WrapperPlayServerChangeGameState packet) {
        this.playerData.getClickTracker().runAfterConfirmation(() -> this.lambda$handleGameStateChange$5(packet));
    }

    public void resetTicksSinceWorldChange() {
        this.ticksSinceWorldChange = 0;
        this.playerData.getBlockBreakTracker().setGhostBlockAirTicks(0);
    }

    public void resetTicksSinceBrandAlert() {
        this.ticksSinceBrandAlert = 0;
    }

    public void onEntityDamage(EntityDamageEvent.DamageCause cause) {
        if (cause == null) {
            return;
        }
        this.lastDamageTick = VulcanBootstrap.INSTANCE.getKeepAliveRunnable().getCurrentTick();
        this.ticksSinceTrackedDamage = 0;
        if (cause != EntityDamageEvent.DamageCause.FALL && cause != EntityDamageEvent.DamageCause.SUFFOCATION) {
            this.ticksSinceNonFallDamage = 0;
        }
        switch (cause) {
            case ENTITY_ATTACK:
                this.ticksSinceMeleeDamage = 0;
                break;
            case FIRE:
            case FIRE_TICK:
                this.ticksSinceFireDamage = 0;
                break;
            case FALL:
                this.ticksSinceFallDamage = 0;
                break;
            case MAGIC:
                this.ticksSinceMagicDamage = 0;
                break;
            case POISON:
                this.ticksSincePoisonDamage = 0;
                break;
            case CONTACT:
                this.ticksSinceContactDamage = 0;
                break;
            case PROJECTILE:
                this.ticksSinceProjectileDamage = 0;
                break;
            case LAVA:
                this.ticksSinceLavaDamage = 0;
                break;
            default:
                break;
        }
    }

    public void recordDamagerType(EntityType type) {
        if (type == null) {
            return;
        }
        String name = type.name();
        if (name.contains("ARROW") || name.contains("TRIDENT") || name.contains("SNOWBALL")
            || name.contains("EGG") || name.contains("FIREWORK") || name.contains("SHULKER_BULLET")
            || name.contains("LLAMA_SPIT")) {
            this.ticksSinceProjectileDamage = 0;
            return;
        }
        if (name.contains("FIREBALL") || name.contains("SMALL_FIREBALL") || "BLAZE".equals(name)) {
            this.ticksSinceFireball = 0;
            return;
        }
        this.ticksSinceMeleeDamage = 0;
    }

    public void resetServerPositionLookTicks() {
        this.serverPositionLookTicks = 0;
    }

    public void markBedEnterTick() {
        this.lastBedEnterTick = VulcanBootstrap.INSTANCE.getKeepAliveRunnable().getCurrentTick();
    }

    public void markFlyingTick() {
        this.lastFlyingTick = VulcanBootstrap.INSTANCE.getKeepAliveRunnable().getCurrentTick();
    }
    public void handleFlyingPacket(WrapperPlayClientPlayerFlying packet, long timestamp) {
        this.onGround = false;
        if (packet.hasPositionChanged() && this.pendingTeleports != null) {
            double x = packet.getLocation().getX();
            double y = packet.getLocation().getY();
            double z = packet.getLocation().getZ();
            for (Object raw : this.pendingTeleports) {
                if (!(raw instanceof Vector)) {
                    continue;
                }
                Vector sample = (Vector) raw;
                if (Math.abs(sample.getX() - x) < 1.0E-8
                    && Math.abs(sample.getY() - y) < 1.0E-8
                    && Math.abs(sample.getZ() - z) < 1.0E-8) {
                    this.onGround = true;
                    break;
                }
            }
        }
        if (this.onGround) {
            this.ticksSinceServerTeleport = 0;
        }
        ++this.ticksSinceServerTeleport;
        ++this.ticksSinceGravityChange;
        ++this.ticksSinceJumpStrengthChange;
        ++this.ticksLived;
        ++this.ticksSinceFireDamage;
        ++this.ticksSinceMagicDamage;
        ++this.ticksSinceFallDamage;
        ++this.ticksSinceDeath;
        ++this.ticksSincePearlTeleport;
        ++this.ticksSinceFireball;
        ++this.ticksSinceCancelledBreak;
        ++this.ticksSinceMeleeDamage;
        ++this.ticksSinceNonFallDamage;
        ++this.ticksSinceContactDamage;
        ++this.ticksSincePoisonDamage;
                ++this.ticksSinceProjectileDamage;
        ++this.ticksSinceLavaDamage;
        ++this.ticksSinceQueuedTeleport;
        ++this.ticksSinceDig;
        ++this.ticksSinceLongWindow;
        ++this.ticksSinceVelocity;
        ++this.ticksSinceClimbablePlace;
        ++this.ticksSinceBlockPlace;
        ++this.ticksSinceWorldChange;
        ++this.ticksSinceTrackedBreak;
        ++this.ticksSinceNonPearlTeleport;
        ++this.ticksSinceBrandAlert;
        ++this.ticksSinceBlockBreak;
        ++this.ticksSinceCobwebPlace;
        ++this.ticksSinceChorusFruit;
        ++this.serverPositionLookTicks;
        ++this.ticksSinceJoin;
        ++this.ticksSinceAbilities;
        ++this.ticksSinceTrackedDamage;
        ++this.ticksSinceIcePlace;
        ++this.ticksSinceLastServerPos;
        ++this.ticksSinceTotem;
        ++this.ticksSinceFireBreak;
        ++this.ticksSinceCancelledMove;
        if (!this.attributesReady) {
            ++this.ticksWithoutAttributes;
        } else {
            this.ticksWithoutAttributes = 0;
        }
        if (!packet.hasPositionChanged()) {
            return;
        }
        Player player = this.playerData.getPlayer();
        if (player != null) {
            if (this.clientSneaking) {
                ++this.sneakTicks;
                this.ticksSinceSneaking = 0;
            } else {
                this.sneakTicks = 0;
                ++this.ticksSinceSneaking;
            }
            this.helmet = player.getInventory().getHelmet();
            this.chestplate = player.getInventory().getChestplate();
            this.leggings = player.getInventory().getLeggings();
            this.boots = player.getInventory().getBoots();
            if (ServerCompatibility.isOffHandSupported()) {
                this.offHand = player.getInventory().getItemInOffHand();
            }
        }
        this.hasDepthStrider = this.boots != null
            && ServerCompatibility.isServer1_8OrNewer()
            && this.boots.containsEnchantment(Enchantment.DEPTH_STRIDER);
        this.hasAquaAffinity = this.helmet != null
            && this.helmet.containsEnchantment(Enchantment.WATER_WORKER);
        this.hasSwiftSneak = this.leggings != null
            && ServerCompatibility.isServer1_9OrNewer()
            && this.leggings.containsEnchantment(Enchantment.SWIFT_SNEAK);
        this.wearingElytra = this.chestplate != null
            && ServerCompatibility.isOffHandSupported()
            && "ELYTRA".equals(this.chestplate.getType().toString());
        this.elytraOrGliding = this.wearingElytra || player != null && player.isGliding();
        if (this.elytraFlyingMetadata) {
            this.ticksSinceElytraFlying = 0;
        }
        ++this.ticksSinceElytraFlying;
        if (this.elytraOrGliding) {
            this.glidingDeltaXZ = this.playerData.getBlockBreakTracker().getLastDeltaXZ();
            this.ticksSinceElytraGliding = 0;
        }
        ++this.ticksSinceElytraGliding;
        double x = packet.getLocation().getX();
        double y = packet.getLocation().getY();
        double z = packet.getLocation().getZ();
        if (this.lastServerX != 0.0 && this.lastServerZ != 0.0) {
            this.distanceFromLastServerPos = TrigUtil.hypot3(
                Math.abs(x - this.lastServerX),
                Math.abs(y - this.lastServerY),
                Math.abs(z - this.lastServerZ)
            );
            if (this.distanceFromLastServerPos > 8.0) {
                this.distanceFromLastServerPos = 0.0;
                this.lastServerX = 0.0;
                this.lastServerZ = 0.0;
            }
        }
        if (this.lastPlacedBlockX == 0 || this.lastPlacedBlockY == 0) {
            return;
        }
        this.distanceFromLastPlacedBlock = TrigUtil.hypot3(
            Math.abs(x - (double) this.lastPlacedBlockX),
            Math.abs(y - (double) this.lastPlacedBlockY),
            Math.abs(z - (double) this.lastPlacedBlockZ)
        );
        if (this.distanceFromLastPlacedBlock <= 12.0) {
            return;
        }
        this.lastPlacedBlockZ = 0;
        this.lastPlacedBlockY = 0;
        this.lastPlacedBlockX = 0;
    }

    public void resetTicksSinceBlockPlace() {
        this.ticksSinceBlockPlace = 0;
    }

    public void resetTicksSinceJoin() {
        this.ticksSinceJoin = 0;
    }

    public void resetTicksSinceCancelledMove() {
        this.ticksSinceCancelledMove = 0;
    }

    public void resetTicksSinceIcePlace() {
        this.ticksSinceIcePlace = 0;
    }

    public void resetTicksSinceClimbablePlace() {
        this.ticksSinceClimbablePlace = 0;
    }

    public void resetTicksSinceCobwebPlace() {
        this.ticksSinceCobwebPlace = 0;
    }

    public void markProjectileLaunchTick() {
        this.lastProjectileLaunchTick = VulcanBootstrap.INSTANCE.getKeepAliveRunnable().getCurrentTick();
    }

    public void markHookTick() {
        this.lastHookTick = VulcanBootstrap.INSTANCE.getKeepAliveRunnable().getCurrentTick();
    }

    public void onPlayerAbilities(WrapperPlayServerPlayerAbilities wrapperPlayServerPlayerAbilities) {
        this.ticksSinceAbilities = 0;
    }

    public void resetTicksSinceTrackedBreak() {
        this.ticksSinceTrackedBreak = 0;
    }

    public void onBlockPlace(BlockPlaceEvent blockPlaceEvent) {
        this.lastPlacedBlockX = blockPlaceEvent.getBlockPlaced().getX();
        this.lastPlacedBlockY = blockPlaceEvent.getBlockPlaced().getY();
        this.lastPlacedBlockZ = blockPlaceEvent.getBlockPlaced().getZ();
    }

    public void resetTicksSinceChorusFruit() {
        this.ticksSinceChorusFruit = 0;
    }

    public void handleEntityMetadata(WrapperPlayServerEntityMetadata packet) {
        this.playerData.getClickTracker().runAfterConfirmation(() -> this.lambda$handleMetaData$6(packet));
    }


    public static EntityData findEntityData(List list, int index) {
        if (list == null) {
            return null;
        }
        for (Object raw : list) {
            if (!(raw instanceof EntityData)) {
                continue;
            }
            EntityData data = (EntityData) raw;
            if (data.getIndex() == index) {
                return data;
            }
        }
        return null;
    }

    public PlayerData getPlayerData() {
        return this.playerData;
    }

    public boolean isClientSneaking() {
        return this.clientSneaking;
    }

    public boolean isClientSprinting() {
        return this.clientSprinting;
    }



    public boolean isBerserkActive() {
        return this.berserkActive;
    }



    public boolean isOnGround() {
        return this.onGround;
    }

    public boolean isSneaking() {
        return this.playerData.getPlayer() != null && this.playerData.getPlayer().isSneaking();
    }

    public boolean isSprinting() {
        return this.playerData.getPlayer() != null && this.playerData.getPlayer().isSprinting();
    }

    public boolean hasSpeed() {
        return this.hasSpeed;
    }

    public boolean hasJumpBoost() {
        return this.hasJumpBoost;
    }

    public boolean isLevitating() {
        return this.levitating;
    }

    public boolean hasSlowFalling() {
        return this.hasSlowFalling;
    }

    public boolean hasDolphinsGrace() {
        return this.hasDolphinsGrace;
    }


    public boolean hasConduitPower() {
        return this.hasConduitPower;
    }


    public boolean hasSlowness() {
        return this.hasSlowness;
    }

    public boolean hasDepthStrider() {
        return this.hasDepthStrider;
    }


    public boolean isWearingElytra() {
        return this.wearingElytra;
    }


    public boolean isElytraOrGliding() {
        return this.elytraOrGliding;
    }



    public boolean isJesusFlagged() {
        return this.jesusFlagged;
    }

    public boolean hasSwiftSneak() {
        return this.hasSwiftSneak;
    }


    public boolean isAttributesReady() {
        return this.attributesReady;
    }


    public boolean hasHaste() {
        return this.hasHaste;
    }



    public boolean isCollisionExemptFlag() {
        return this.collisionExemptFlag;
    }



    public boolean hasAquaAffinity() {
        return this.hasAquaAffinity;
    }


    public boolean isElytraFlyingMetadata() {
        return this.elytraFlyingMetadata;
    }

    public boolean isSwimmingMetadata() {
        return this.swimmingMetadata;
    }

    public boolean isRiptideSpinAttack() {
        return this.riptideSpinAttack;
    }

    public boolean isInstaBreak() {
        return this.instaBreak;
    }


    public double getWalkSpeed() {
        return this.walkSpeed;
    }

    public double getStepHeight() {
        return this.stepHeight;
    }

    public double getJumpStrength() {
        return this.jumpStrength;
    }

    public double getGravity() {
        return this.gravity;
    }

    public double getLastJumpStrength() {
        return this.lastJumpStrength;
    }

    public double getLastGravity() {
        return this.lastGravity;
    }

    public double getEntityInteractionRange() {
        return this.entityInteractionRange;
    }

    public LinkedList getPendingTeleports() {
        return this.pendingTeleports;
    }


    public int getLastBucketEmptyTick() {
        return this.lastBucketEmptyTick;
    }

    public int getLastShortWindowTick() {
        return this.lastShortWindowTick;
    }

    public int getLastAttackTick() {
        return this.lastAttackTick;
    }

    public int getTicksSinceSneaking() {
        return this.ticksSinceSneaking;
    }


    public int getLastWindowClickTick() {
        return this.lastWindowClickTick;
    }

    public int getHeldItemAmount() {
        return this.heldItemAmount;
    }

    public int getPreviousHeldItemAmount() {
        return this.previousHeldItemAmount;
    }

    public int getHeldHotbarSlot() {
        return this.heldHotbarSlot;
    }

    public int getPreviousHeldHotbarSlot() {
        return this.previousHeldHotbarSlot;
    }

    public int getTicksSinceServerTeleport() {
        return this.ticksSinceServerTeleport;
    }

    public int getLastBedEnterTick() {
        return this.lastBedEnterTick;
    }

    public int getLastFlyingTick() {
        return this.lastFlyingTick;
    }

    public int getItemPickupTick() {
        return this.itemPickupTick;
    }

    public int getLastBukkitBlockBreakTick() {
        return this.lastBukkitBlockBreakTick;
    }


    public int getLastDamageTick() {
        return this.lastDamageTick;
    }

    public int getTicksSinceQueuedTeleport() {
        return this.ticksSinceQueuedTeleport;
    }

    public int getSneakTicks() {
        return this.sneakTicks;
    }

    public int getLastClientRespawnTick() {
        return this.lastClientRespawnTick;
    }


    public int getLastProjectileLaunchTick() {
        return this.lastProjectileLaunchTick;
    }

    public int getLastHookTick() {
        return this.lastHookTick;
    }

    public int getTicksSinceFireDamage() {
        return this.ticksSinceFireDamage;
    }

    public int getTicksSinceMeleeDamage() {
        return this.ticksSinceMeleeDamage;
    }


    public int getTicksSinceDeath() {
        return this.ticksSinceDeath;
    }

    public int getTicksSincePearlTeleport() {
        return this.ticksSincePearlTeleport;
    }

    public int getTicksSinceFireball() {
        return this.ticksSinceFireball;
    }

    public int getTicksSinceCancelledBreak() {
        return this.ticksSinceCancelledBreak;
    }

    public int getLastDropItemTick() {
        return this.lastDropItemTick;
    }
    public int getTicksSinceFallDamage() {
        return this.ticksSinceFallDamage;
    }

    public int getTicksSinceMagicDamage() {
        return this.ticksSinceMagicDamage;
    }

    public int getTicksSincePoisonDamage() {
        return this.ticksSincePoisonDamage;
    }

    public int getTicksSinceContactDamage() {
        return this.ticksSinceContactDamage;
    }

    public int getTicksSinceProjectileDamage() {
        return this.ticksSinceProjectileDamage;
    }


    public int getTicksSinceLavaDamage() {
        return this.ticksSinceLavaDamage;
    }


    public int getTicksSinceVelocity() {
        return this.ticksSinceVelocity;
    }

    public int getTicksSinceClimbablePlace() {
        return this.ticksSinceClimbablePlace;
    }

    public int getTicksSinceBlockPlace() {
        return this.ticksSinceBlockPlace;
    }

    public int getTicksSinceJoin() {
        return this.ticksSinceJoin;
    }

    public int getTicksSinceWorldChange() {
        return this.ticksSinceWorldChange;
    }

    public int getTicksSinceTrackedBreak() {
        return this.ticksSinceTrackedBreak;
    }

    public int getTicksSinceNonPearlTeleport() {
        return this.ticksSinceNonPearlTeleport;
    }

    public int getTicksSinceBrandAlert() {
        return this.ticksSinceBrandAlert;
    }

    public int getTicksSinceNonFallDamage() {
        return this.ticksSinceNonFallDamage;
    }


    public int getLastPlacedBlockX() {
        return this.lastPlacedBlockX;
    }

    public int getLastPlacedBlockY() {
        return this.lastPlacedBlockY;
    }

    public int getLastPlacedBlockZ() {
        return this.lastPlacedBlockZ;
    }

    public int getTicksSinceBlockBreak() {
        return this.ticksSinceBlockBreak;
    }




    public int getSpeedAmplifier() {
        return this.speedAmplifier;
    }

    public int getJumpBoostAmplifier() {
        return this.jumpBoostAmplifier;
    }

    public int getLevitationAmplifier() {
        return this.levitationAmplifier;
    }

    public int getSlowFallingAmplifier() {
        return this.slowFallingAmplifier;
    }

    public int getDolphinsGraceAmplifier() {
        return this.dolphinsGraceAmplifier;
    }

    public int getTicksSinceChorusFruit() {
        return this.ticksSinceChorusFruit;
    }

    public int getTicksSinceCobwebPlace() {
        return this.ticksSinceCobwebPlace;
    }

    public int getServerPositionLookTicks() {
        return this.serverPositionLookTicks;
    }


    public int getTicksSinceTrackedDamage() {
        return this.ticksSinceTrackedDamage;
    }

    public int getTicksSinceIcePlace() {
        return this.ticksSinceIcePlace;
    }

    public int getTicksSinceLastServerPos() {
        return this.ticksSinceLastServerPos;
    }

    public int getTicksLived() {
        return this.ticksLived;
    }

    public int getTicksSinceDig() {
        return this.ticksSinceDig;
    }

    public int getTicksSinceAbilities() {
        return this.ticksSinceAbilities;
    }

    public int getTicksSinceTotem() {
        return this.ticksSinceTotem;
    }

    public int getTicksSinceLongWindow() {
        return this.ticksSinceLongWindow;
    }

    public int getTicksSinceCancelledMove() {
        return this.ticksSinceCancelledMove;
    }






    public int getTicksSinceFireBreak() {
        return this.ticksSinceFireBreak;
    }



    public int getLastSpeedAmplifier() {
        return this.lastSpeedAmplifier;
    }

    public int getSoulSpeedLevel() {
        return this.soulSpeedLevel;
    }


    public int getTicksWithoutAttributes() {
        return this.ticksWithoutAttributes;
    }

    public int getSlownessAmplifier() {
        return this.slownessAmplifier;
    }

    public int getTicksSinceJumpStrengthChange() {
        return this.ticksSinceJumpStrengthChange;
    }

    public int getTicksSinceGravityChange() {
        return this.ticksSinceGravityChange;
    }

    public int getTicksSinceElytraGliding() {
        return this.ticksSinceElytraGliding;
    }

    public int getTicksSinceElytraFlying() {
        return this.ticksSinceElytraFlying;
    }


    public double getLastHeldChangeX() {
        return this.lastHeldChangeX;
    }

    public double getLastHeldChangeZ() {
        return this.lastHeldChangeZ;
    }

    public double getAttackOffsetFromHeldChange() {
        return this.attackOffsetFromHeldChange;
    }

    public double getLastServerX() {
        return this.lastServerX;
    }

    public double getLastServerY() {
        return this.lastServerY;
    }

    public double getLastServerZ() {
        return this.lastServerZ;
    }

    public double getDistanceFromLastServerPos() {
        return this.distanceFromLastServerPos;
    }


    public double getDistanceFromLastPlacedBlock() {
        return this.distanceFromLastPlacedBlock;
    }







    public long getLastMillisWindowTime() {
        return this.lastMillisWindowTime;
    }

    public GameMode getGameMode() {
        return this.gameMode;
    }


    public ItemStack getHelmet() {
        return this.helmet;
    }

    public ItemStack getChestplate() {
        return this.chestplate;
    }

    public ItemStack getLeggings() {
        return this.leggings;
    }

    public ItemStack getBoots() {
        return this.boots;
    }

    public ItemStack getMainHand() {
        return this.mainHand;
    }

    public ItemStack getOffHand() {
        return this.offHand;
    }




    public void setClientSneaking(boolean value) {
        this.clientSneaking = value;
    }

    public void setClientSprinting(boolean value) {
        this.clientSprinting = value;
    }



    public void setBerserkActive(boolean active) {
        this.berserkActive = active;
    }



    public void setOnGround(boolean value) {
        this.onGround = value;
    }

    public void setHasSpeed(boolean value) {
        this.hasSpeed = value;
    }

    public void setHasJumpBoost(boolean value) {
        this.hasJumpBoost = value;
    }

    public void setLevitating(boolean value) {
        this.levitating = value;
    }

    public void setHasSlowFalling(boolean value) {
        this.hasSlowFalling = value;
    }

    public void setHasDolphinsGrace(boolean value) {
        this.hasDolphinsGrace = value;
    }


    public void setHasConduitPower(boolean value) {
        this.hasConduitPower = value;
    }


    public void setHasSlowness(boolean value) {
        this.hasSlowness = value;
    }

    public void setHasDepthStrider(boolean value) {
        this.hasDepthStrider = value;
    }

    public void setWearingElytra(boolean value) {
        this.wearingElytra = value;
    }

    public void setElytraOrGliding(boolean value) {
        this.elytraOrGliding = value;
    }


    public void setJesusFlagged(boolean jesusFlagged) {
        this.jesusFlagged = jesusFlagged;
    }

    public void setHasSwiftSneak(boolean value) {
        this.hasSwiftSneak = value;
    }

    public void setAttributesReady(boolean value) {
        this.attributesReady = value;
    }


    public void setHasHaste(boolean value) {
        this.hasHaste = value;
    }



    public void setCollisionExemptFlag(boolean value) {
        this.collisionExemptFlag = value;
    }



    public void setHasAquaAffinity(boolean value) {
        this.hasAquaAffinity = value;
    }

    public void setElytraFlyingMetadata(boolean value) {
        this.elytraFlyingMetadata = value;
    }

    public void setSwimmingMetadata(boolean value) {
        this.swimmingMetadata = value;
    }

    public void setRiptideSpinAttack(boolean value) {
        this.riptideSpinAttack = value;
    }

    public void setInstaBreak(boolean value) {
        this.instaBreak = value;
    }

    public void setWalkSpeed(double value) {
        this.walkSpeed = value;
    }

    public void setStepHeight(double value) {
        this.stepHeight = value;
    }

    public void setJumpStrength(double value) {
        this.jumpStrength = value;
    }

    public void setGravity(double value) {
        this.gravity = value;
    }

    public void setLastJumpStrength(double value) {
        this.lastJumpStrength = value;
    }

    public void setLastGravity(double value) {
        this.lastGravity = value;
    }

    public void setEntityInteractionRange(double value) {
        this.entityInteractionRange = value;
    }

    public void setLastBucketEmptyTick(int lastBucketEmptyTick) {
        this.lastBucketEmptyTick = lastBucketEmptyTick;
    }

    public void setLastShortWindowTick(int value) {
        this.lastShortWindowTick = value;
    }

    public void setLastAttackTick(int value) {
        this.lastAttackTick = value;
    }

    public void setTicksSinceSneaking(int value) {
        this.ticksSinceSneaking = value;
    }


    public void setLastWindowClickTick(int value) {
        this.lastWindowClickTick = value;
    }

    public void setHeldItemAmount(int value) {
        this.heldItemAmount = value;
    }

    public void setPreviousHeldItemAmount(int value) {
        this.previousHeldItemAmount = value;
    }

    public void setHeldHotbarSlot(int value) {
        this.heldHotbarSlot = value;
    }

    public void setPreviousHeldHotbarSlot(int value) {
        this.previousHeldHotbarSlot = value;
    }

    public void setTicksSinceServerTeleport(int value) {
        this.ticksSinceServerTeleport = value;
    }

    public void setLastBedEnterTick(int value) {
        this.lastBedEnterTick = value;
    }

    public void setLastFlyingTick(int value) {
        this.lastFlyingTick = value;
    }

    public void setItemPickupTick(int value) {
        this.itemPickupTick = value;
    }

    public void setLastBukkitBlockBreakTick(int value) {
        this.lastBukkitBlockBreakTick = value;
    }


    public void setLastDamageTick(int value) {
        this.lastDamageTick = value;
    }

    public void setTicksSinceQueuedTeleport(int value) {
        this.ticksSinceQueuedTeleport = value;
    }

    public void setSneakTicks(int value) {
        this.sneakTicks = value;
    }

    public void setLastClientRespawnTick(int value) {
        this.lastClientRespawnTick = value;
    }


    public void setLastProjectileLaunchTick(int value) {
        this.lastProjectileLaunchTick = value;
    }

    public void setLastHookTick(int value) {
        this.lastHookTick = value;
    }

    public void setTicksSinceFireDamage(int value) {
        this.ticksSinceFireDamage = value;
    }

    public void setTicksSinceMeleeDamage(int value) {
        this.ticksSinceMeleeDamage = value;
    }

    public void setTicksSinceFallDamage(int value) {
        this.ticksSinceFallDamage = value;
    }

    public void setTicksSinceMagicDamage(int value) {
        this.ticksSinceMagicDamage = value;
    }

    public void setTicksSincePoisonDamage(int value) {
        this.ticksSincePoisonDamage = value;
    }

    public void setTicksSinceContactDamage(int value) {
        this.ticksSinceContactDamage = value;
    }

    public void setTicksSinceProjectileDamage(int value) {
        this.ticksSinceProjectileDamage = value;
    }


    public void setTicksSinceLavaDamage(int value) {
        this.ticksSinceLavaDamage = value;
    }


    public void setTicksSinceVelocity(int value) {
        this.ticksSinceVelocity = value;
    }

    public void setTicksSinceClimbablePlace(int value) {
        this.ticksSinceClimbablePlace = value;
    }

    public void setTicksSinceBlockPlace(int value) {
        this.ticksSinceBlockPlace = value;
    }

    public void setTicksSinceJoin(int value) {
        this.ticksSinceJoin = value;
    }

    public void setTicksSinceWorldChange(int value) {
        this.ticksSinceWorldChange = value;
    }

    public void setTicksSinceTrackedBreak(int value) {
        this.ticksSinceTrackedBreak = value;
    }

    public void setTicksSinceNonPearlTeleport(int value) {
        this.ticksSinceNonPearlTeleport = value;
    }

    public void setTicksSinceBrandAlert(int value) {
        this.ticksSinceBrandAlert = value;
    }

    public void setTicksSinceNonFallDamage(int value) {
        this.ticksSinceNonFallDamage = value;
    }


    public void setLastPlacedBlockX(int value) {
        this.lastPlacedBlockX = value;
    }

    public void setLastPlacedBlockY(int value) {
        this.lastPlacedBlockY = value;
    }

    public void setLastPlacedBlockZ(int value) {
        this.lastPlacedBlockZ = value;
    }

    public void setTicksSinceBlockBreak(int value) {
        this.ticksSinceBlockBreak = value;
    }




    public void setSpeedAmplifier(int value) {
        this.speedAmplifier = value;
    }

    public void setJumpBoostAmplifier(int value) {
        this.jumpBoostAmplifier = value;
    }

    public void setLevitationAmplifier(int value) {
        this.levitationAmplifier = value;
    }

    public void setSlowFallingAmplifier(int value) {
        this.slowFallingAmplifier = value;
    }

    public void setDolphinsGraceAmplifier(int value) {
        this.dolphinsGraceAmplifier = value;
    }

    public void setTicksSinceChorusFruit(int value) {
        this.ticksSinceChorusFruit = value;
    }

    public void setTicksSinceCobwebPlace(int value) {
        this.ticksSinceCobwebPlace = value;
    }

    public void setServerPositionLookTicks(int value) {
        this.serverPositionLookTicks = value;
    }


    public void setTicksSinceTrackedDamage(int value) {
        this.ticksSinceTrackedDamage = value;
    }

    public void setTicksSinceIcePlace(int value) {
        this.ticksSinceIcePlace = value;
    }

    public void setTicksSinceLastServerPos(int value) {
        this.ticksSinceLastServerPos = value;
    }

    public void setTicksLived(int value) {
        this.ticksLived = value;
    }

    public void setTicksSinceDig(int value) {
        this.ticksSinceDig = value;
    }

    public void setTicksSinceAbilities(int value) {
        this.ticksSinceAbilities = value;
    }

    public void setTicksSinceTotem(int value) {
        this.ticksSinceTotem = value;
    }

    public void setTicksSinceLongWindow(int value) {
        this.ticksSinceLongWindow = value;
    }

    public void markCancelledMove() {
        this.ticksSinceCancelledMove = 0;
    }

    public void setTicksSinceCancelledMove(int value) {
        this.ticksSinceCancelledMove = value;
    }






    public void setTicksSinceFireBreak(int ticks) {
        this.ticksSinceFireBreak = ticks;
    }



    public void setLastSpeedAmplifier(int value) {
        this.lastSpeedAmplifier = value;
    }

    public void setSoulSpeedLevel(int value) {
        this.soulSpeedLevel = value;
    }


    public void setTicksWithoutAttributes(int value) {
        this.ticksWithoutAttributes = value;
    }

    public void setSlownessAmplifier(int value) {
        this.slownessAmplifier = value;
    }

    public void setTicksSinceJumpStrengthChange(int value) {
        this.ticksSinceJumpStrengthChange = value;
    }

    public void setTicksSinceGravityChange(int value) {
        this.ticksSinceGravityChange = value;
    }

    public void setTicksSinceElytraGliding(int value) {
        this.ticksSinceElytraGliding = value;
    }

    public void setTicksSinceElytraFlying(int value) {
        this.ticksSinceElytraFlying = value;
    }


    public void setLastHeldChangeX(double value) {
        this.lastHeldChangeX = value;
    }

    public void setLastHeldChangeZ(double value) {
        this.lastHeldChangeZ = value;
    }

    public void setAttackOffsetFromHeldChange(double value) {
        this.attackOffsetFromHeldChange = value;
    }

    public void setLastServerX(double value) {
        this.lastServerX = value;
    }

    public void setLastServerY(double value) {
        this.lastServerY = value;
    }

    public void setLastServerZ(double value) {
        this.lastServerZ = value;
    }

    public void setDistanceFromLastServerPos(double value) {
        this.distanceFromLastServerPos = value;
    }


    public void setDistanceFromLastPlacedBlock(double value) {
        this.distanceFromLastPlacedBlock = value;
    }





    public void setGlidingDeltaXZ(double value) {
        this.glidingDeltaXZ = value;
    }



    public void setLastMillisWindowTime(long value) {
        this.lastMillisWindowTime = value;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public void setHelmet(ItemStack value) {
        this.helmet = value;
    }

    public void setChestplate(ItemStack value) {
        this.chestplate = value;
    }

    public void setLeggings(ItemStack value) {
        this.leggings = value;
    }

    public void setBoots(ItemStack value) {
        this.boots = value;
    }

    public void setMainHand(ItemStack value) {
        this.mainHand = value;
    }

    public void setOffHand(ItemStack value) {
        this.offHand = value;
    }




    private void lambda$handleMetaData$6(WrapperPlayServerEntityMetadata packet) {
        EntityData sharedFlags = StatusTracker.findEntityData(packet.getEntityMetadata(), 0);
        if (sharedFlags != null && sharedFlags.getValue() instanceof Byte) {
            byte flags = ((Byte) sharedFlags.getValue()).byteValue();
            this.elytraFlyingMetadata = (flags & 0x80) != 0
                && this.playerData.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9);
            this.swimmingMetadata = (flags & 0x10) != 0;
        }
        if (ServerCompatibility.isServer1_9OrNewer() && this.playerData.getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9)) {
            int index = 8;
            EntityData handFlags = StatusTracker.findEntityData(packet.getEntityMetadata(), index);
            if (handFlags != null && handFlags.getValue() instanceof Byte) {
                byte flags = ((Byte) handFlags.getValue()).byteValue();
                this.riptideSpinAttack = (flags & 4) != 0;
            }
        }
    }
    private void lambda$handleGameStateChange$5(WrapperPlayServerChangeGameState packet) {
        float value = packet.getValue();
        if (value >= 0.0f && value <= 3.0f) {
            GameMode[] modes = new GameMode[] {
                GameMode.SURVIVAL, GameMode.CREATIVE, GameMode.ADVENTURE, GameMode.SPECTATOR
            };
            int index = (int) value;
            if (index >= 0 && index < modes.length) {
                this.gameMode = modes[index];
            }
        }
    }

    private void lambda$handleRemoveEntityEffect$4(WrapperPlayServerRemoveEntityEffect packet) {
        PotionType type = packet.getPotionType();
        if (type == PotionTypes.SPEED) {
            this.speedAmplifier = 0;
            this.hasSpeed = false;
            this.playerData.getBlockBreakTracker().setSneakTicks(0);
        } else if (type == PotionTypes.SLOWNESS) {
            this.hasSlowness = false;
            this.slownessAmplifier = 0;
        } else if (type == PotionTypes.HASTE) {
            this.hasHaste = false;
        } else if (type == PotionTypes.JUMP_BOOST) {
            this.jumpBoostAmplifier = 0;
            this.hasJumpBoost = false;
        } else if (type == PotionTypes.LEVITATION) {
            this.levitationAmplifier = 0;
            this.levitating = false;
        } else if (type == PotionTypes.SLOW_FALLING) {
            this.hasSlowFalling = false;
            this.slowFallingAmplifier = 0;
        } else if (type == PotionTypes.CONDUIT_POWER) {
            this.hasConduitPower = false;
        } else if (type == PotionTypes.DOLPHINS_GRACE) {
            this.dolphinsGraceAmplifier = 0;
            this.hasDolphinsGrace = false;
        }
    }

    private void lambda$handleEntityEffect$3(WrapperPlayServerEntityEffect packet) {
        int amplifier = packet.getEffectAmplifier() + 1;
        PotionType type = packet.getPotionType();
        if (type == PotionTypes.SPEED) {
            this.hasSpeed = true;
            this.lastSpeedAmplifier = this.speedAmplifier = amplifier;
        } else if (type == PotionTypes.HASTE) {
            this.hasHaste = true;
        } else if (type == PotionTypes.SLOWNESS) {
            this.hasSlowness = true;
            this.slownessAmplifier = amplifier;
        } else if (type == PotionTypes.JUMP_BOOST) {
            this.hasJumpBoost = true;
            this.jumpBoostAmplifier = amplifier;
        } else if (type == PotionTypes.LEVITATION) {
            this.levitating = true;
            this.levitationAmplifier = amplifier;
        } else if (type == PotionTypes.SLOW_FALLING) {
            this.hasSlowFalling = true;
            this.slowFallingAmplifier = amplifier;
        } else if (type == PotionTypes.CONDUIT_POWER) {
            this.hasConduitPower = true;
        } else if (type == PotionTypes.DOLPHINS_GRACE) {
            this.hasDolphinsGrace = true;
            this.dolphinsGraceAmplifier = amplifier;
        }
    }

    private void lambda$handleServerPosition$2() {
        this.playerData.getClickTracker().handleConfirmation();
    }

    private void handleServerPositionPacket(WrapperPlayServerPlayerPositionAndLook packet) {
        Vector vector = new Vector(packet.getX(), packet.getY(), packet.getZ());
        this.pendingTeleports.add(vector);
        this.ticksSinceQueuedTeleport = 0;
    }

    private void handleUpdateAttributesPacket(WrapperPlayServerUpdateAttributes packet) {
        for (Object raw : packet.getProperties()) {
            WrapperPlayServerUpdateAttributes.Property property = (WrapperPlayServerUpdateAttributes.Property) raw;
            Attribute attribute = property.getAttribute();
            if (attribute == Attributes.STEP_HEIGHT) {
                this.stepHeight = PacketUtil.computeFinalAttributeValue(property);
            } else if (attribute == Attributes.JUMP_STRENGTH) {
                this.lastJumpStrength = this.jumpStrength;
                this.jumpStrength = PacketUtil.computeFinalAttributeValue(property);
                if (this.jumpStrength != this.lastJumpStrength) {
                    this.ticksSinceJumpStrengthChange = 0;
                }
            } else if (attribute == Attributes.GRAVITY) {
                this.lastGravity = this.gravity;
                this.gravity = PacketUtil.computeFinalAttributeValue(property);
                if (this.gravity != this.lastGravity) {
                    this.ticksSinceGravityChange = 0;
                }
            } else if (attribute == Attributes.MOVEMENT_SPEED) {
                this.walkSpeed = PacketUtil.computeWalkSpeedWithoutSprint(property);
            } else if (attribute == Attributes.ENTITY_INTERACTION_RANGE) {
                this.entityInteractionRange = PacketUtil.computeFinalAttributeValue(property);
            }
        }
    }


}
