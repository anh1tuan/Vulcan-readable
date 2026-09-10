package me.frep.vulcan.spigot;

import java.util.List;
import java.util.function.Function;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;


public enum ExemptType {
    TPS(ExemptType::isTps),
    CHUNK(ExemptType::isChunk),
    JOINED(ExemptType::isJoined),
    CAKE(ExemptType::isCake),
    BAMBOO(ExemptType::isBamboo),
    HAPPY_GHAST(ExemptType::isHappyGhast),
    PACKET_EXPLOSION(ExemptType::isPacketExplosion),
    ENTITY_CRAM_FIX(ExemptType::isEntityCramFix),
    AROUND_SLIME(ExemptType::isAroundSlime),
    SIGN(ExemptType::isSign),
    PRESSURE_PLATE(ExemptType::isPressurePlate),
    SWIMMING(ExemptType::isSwimming),
    SWIMMING_JESUS(ExemptType::isSwimmingJesus),
    COLLIDING_HORIZONTALLY(ExemptType::isCollidingHorizontally),
    DRIPSTONE(ExemptType::isDripstone),
    HIGH_FLY_SPEED(ExemptType::isHighFlySpeed),
    GLITCHED_BLOCKS_ABOVE(ExemptType::isGlitchedBlocksAbove),
    PROJECTILE_DAMAGE(ExemptType::isProjectileDamage),
    CHEST(ExemptType::isChest),
    ATTACK_DAMAGE(ExemptType::isAttackDamage),
    SKULL(ExemptType::isSkull),
    EXPLOSION(ExemptType::isExplosion),
    STAIRS(ExemptType::isStairs),
    HIGH_LEVITATION(ExemptType::isHighLevitation),
    FENCE_GATE(ExemptType::isFenceGate),
    POWDER_SNOW(ExemptType::isPowderSnow),
    DRAGON_DAMAGE(ExemptType::isDragonDamage),
    JUMP_BOOST_RAN_OUT(ExemptType::isJumpBoostRanOut),
    LAGGED_NEAR_GROUND(ExemptType::isLaggedNearGround),
    SNOW(ExemptType::isSnow),
    NETHERITE_ARMOR(ExemptType::isNetheriteArmor),
    ZERO_DELTA(ExemptType::isZeroDelta),
    JOINED_CHUNK_LOAD(ExemptType::isJoinedChunkLoad),
    NEAR_SOLID(ExemptType::isNearSolid),
    CAMPFIRE(ExemptType::isCampfire),
    CANCELLED_MOVE(ExemptType::isCancelledMove),
    CARPET(ExemptType::isCarpet),
    FLOWER_POT(ExemptType::isFlowerPot),
    EMPTIED_BUCKET(ExemptType::isEmptiedBucket),
    END_ROD(ExemptType::isEndRod),
    HOPPER(ExemptType::isHopper),
    CHAIN(ExemptType::isChain),
    DOOR(ExemptType::isDoor),
    PICKED_UP_ITEM(ExemptType::isPickedUpItem),
    ANVIL(ExemptType::isAnvil),
    LAGGED_NEAR_GROUND_MODERN(ExemptType::isLaggedNearGroundModern),
    FULLY_SUBMERGED(ExemptType::isFullySubmerged),
    RESPAWN(ExemptType::isRespawn),
    ICE(ExemptType::isIce),
    BED(ExemptType::isBed),
    SLAB(ExemptType::isSlab),
    HIGH_JUMP_BOOST(ExemptType::isHighJumpBoost),
    LILY_PAD(ExemptType::isLilyPad),
    HIGH_SPEED(ExemptType::isHighSpeed),
    WALL(ExemptType::isWall),
    SWEET_BERRIES(ExemptType::isSweetBerries),
    CONDUIT(ExemptType::isConduit),
    PLACED_WEB(ExemptType::isPlacedWeb),
    PISTON(ExemptType::isPiston),
    CHORUS_FRUIT(ExemptType::isChorusFruit),
    ENDER_PEARL(ExemptType::isEnderPearl),
    NEAR_GROUND(ExemptType::isNearGround),
    SLEEPING(ExemptType::isSleeping),
    SOUL_SPEED(ExemptType::isSoulSpeed),
    TRAPDOOR(ExemptType::isTrapdoor),
    FISHING_ROD(ExemptType::isFishingRod),
    PLUGIN_LOAD(ExemptType::isPluginLoad),
    RIPTIDE(ExemptType::isRiptide),
    FIREWORK(ExemptType::isFirework),
    SPECTATOR(ExemptType::isSpectator),
    CAULDRON(ExemptType::isCauldron),
    ENTITY_COLLISION(ExemptType::isEntityCollision),
    DEATH(ExemptType::isDeath),
    DEPTH_STRIDER(ExemptType::isDepthStrider),
    LEVITATION(ExemptType::isLevitation),
    FROZEN(ExemptType::isFrozen),
    DIGGING(ExemptType::isDigging),
    BLOCK_BREAK(ExemptType::isBlockBreak),
    PLACING(ExemptType::isPlacing),
    SERVER_POSITION(ExemptType::isServerPosition),
    SERVER_POSITION_FAST(ExemptType::isServerPositionFast),
    CANCELLED_PLACE(ExemptType::isCancelledPlace),
    BLOCK_PLACE(ExemptType::isBlockPlace),
    BLOCK_PLACE_FAST(ExemptType::isBlockPlaceFast),
    SWIMMING_ON_OLD_VERSION(ExemptType::isSwimmingOnOldVersion),
    FULLY_STUCK(ExemptType::isFullyStuck),
    PLACED_CLIMBABLE(ExemptType::isPlacedClimbable),
    PARTIALLY_STUCK(ExemptType::isPartiallyStuck),
    NOT_MOVING(ExemptType::isNotMoving),
    PLACED_SLIME(ExemptType::isPlacedSlime),
    FIREBALL(ExemptType::isFireball),
    WORLD_CHANGE(ExemptType::isWorldChange),
    FALL_DAMAGE(ExemptType::isFallDamage),
    ILLEGAL_BLOCK(ExemptType::isIllegalBlock),
    HONEY(ExemptType::isHoney),
    SCAFFOLDING(ExemptType::isScaffolding),
    HALF_BLOCK(ExemptType::isHalfBlock),
    SHULKER(ExemptType::isShulker),
    GLIDING(ExemptType::isGliding),
    ELYTRA(ExemptType::isElytra),
    VELOCITY(ExemptType::isVelocity),
    DEAD(ExemptType::isDead),
    WEB(ExemptType::isWeb),
    SOUL_SAND(ExemptType::isSoulSand),
    CINEMATIC(ExemptType::isCinematic),
    FAST(ExemptType::isFast),
    LENIENT_SCAFFOLDING(ExemptType::isLenientScaffolding),
    WINDOW_CLICK(ExemptType::isWindowClick),
    DROPPED_ITEM(ExemptType::isDroppedItem),
    CANCELLED_BREAK(ExemptType::isCancelledBreak),
    MYTHIC_MOB(ExemptType::isMythicMob),
    CREATIVE(ExemptType::isCreative),
    AUTOCLICKER_NON_DIG(ExemptType::isAutoclickerNonDig),
    AUTOCLICKER(ExemptType::isAutoclicker),
    COLLIDING_VERTICALLY(ExemptType::isCollidingVertically),
    SLOW_FALLING(ExemptType::isSlowFalling),
    BUBBLE_COLUMN(ExemptType::isBubbleColumn),
    FENCE(ExemptType::isFence),
    JUMP_BOOST(ExemptType::isJumpBoost),
    FLIGHT(ExemptType::isFlight),
    COMBO_MODE(ExemptType::isComboMode),
    DOLPHINS_GRACE(ExemptType::isDolphinsGrace),
    TELEPORT(ExemptType::isTeleport),
    MODERN_SERVER(ExemptType::isModernServer),
    KELP(ExemptType::isKelp),
    CLIENT_1_9_OR_NEWER(ExemptType::isClient1_9OrNewer),
    SLIME(ExemptType::isSlime),
    BOAT(ExemptType::isBoat),
    SEA_PICKLE(ExemptType::isSeaPickle),
    SEAGRASS(ExemptType::isSeagrass),
    TURTLE_EGG(ExemptType::isTurtleEgg),
    SHULKER_BOX(ExemptType::isShulkerBox),
    WATERLOGGED(ExemptType::isWaterlogged),
    CLIMBABLE(ExemptType::isClimbable),
    LIQUID(ExemptType::isLiquid),
    GLASS_PANE(ExemptType::isGlassPane),
    FARMLAND(ExemptType::isFarmland),
    VEHICLE(ExemptType::isVehicle),
    VOID(ExemptType::isVoid);

    private final Function<PlayerData, Boolean> predicate;

    ExemptType(Function<PlayerData, Boolean> predicate) {
        this.predicate = predicate;
    }

    public boolean test(PlayerData playerData) {
        return Boolean.TRUE.equals(this.predicate.apply(playerData));
    }

    private static Boolean isVoid(PlayerData playerData) {
        double y = playerData.getBlockBreakTracker().getLocationY();
        if (ServerCompatibility.isServer1_18OrNewer()) {
            return y < -70.0;
        }
        return y < 0.0;
    }

    private static Boolean isVehicle(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().getVehicleTicks() < 25);
    }

    private static Boolean isFarmland(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().getFarmlandTicks() < 20);
    }

    private static Boolean isGlassPane(PlayerData playerData) {
        java.util.List materials = playerData.getBlockBreakTracker().getNearbyMaterials();
        if (materials == null) {
            return Boolean.FALSE;
        }
        for (Object raw : materials) {
            if (raw instanceof org.bukkit.Material && BoundingBoxUtil.isGlassPane((org.bukkit.Material) raw)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    private static Boolean isLiquid(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().getLiquidTicks() < 25);
    }

    private static Boolean isClimbable(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().isNearClimbable()
            || playerData.getBlockBreakTracker().isNearClimbableColumn());
    }

    private static Boolean isWaterlogged(PlayerData playerData) {
        return Boolean.valueOf(ServerCompatibility.isModernProtocol() && playerData.getBlockBreakTracker().getWaterloggedTicks() < 30);
    }

    private static Boolean isShulkerBox(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().isNearShulker()
            || playerData.getBlockBreakTracker().getTicksSinceShulker() < 60);
    }

    private static Boolean isTurtleEgg(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().isNearTurtleEgg());
    }

    private static Boolean isSeagrass(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().getSeagrassTicks() < 20);
    }

    private static Boolean isSeaPickle(PlayerData playerData) {
        return playerData.getBlockBreakTracker().isNearSeaPickle();
    }

    private static Boolean isBoat(PlayerData playerData) {
        return playerData.getBlockBreakTracker().isNearBoat();
    }

    private static Boolean isSlime(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().isNearSlime()
            || playerData.getBlockBreakTracker().getTicksSinceSlime() < 65);
    }

    private static Boolean isClient1_9OrNewer(PlayerData playerData) {
        return playerData.getUser().getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_9);
    }

    private static Boolean isKelp(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().isNearKelp());
    }

    private static Boolean isModernServer(PlayerData playerData) {
        return Boolean.valueOf(ServerCompatibility.isModernProtocol());
    }

    private static Boolean isTeleport(PlayerData playerData) {
        return Boolean.valueOf(playerData.getStatusTracker().getTicksSinceNonPearlTeleport() < 10
            || playerData.getStatusTracker().getTicksSincePearlTeleport() < 10);
    }

    private static Boolean isDolphinsGrace(PlayerData playerData) {
        return Boolean.valueOf(CollisionUtil.hasDolphinsGrace(playerData));
    }

    private static Boolean isComboMode(PlayerData playerData) {
        Player player = playerData.getPlayer();
        return Boolean.valueOf(player != null
            && playerData.getStatusTracker().getTicksSinceMeleeDamage() < 25
            && player.getMaximumNoDamageTicks() < 15);
    }
    private static Boolean isFlight(PlayerData data) {
        if (data.getPlayer() != null && data.getPlayer().getAllowFlight()) {
            return Boolean.TRUE;
        }
        return Boolean.valueOf(data.getBlockBreakTracker().getTicksSinceFlightAllowed() < ConfigValues.settingsFlightCooldown);
    }

    private static Boolean isJumpBoost(PlayerData playerData) {
        return Boolean.valueOf(playerData.getStatusTracker().getJumpBoostAmplifier() > 0);
    }

    private static Boolean isFence(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().isNearFence());
    }

    private static Boolean isBubbleColumn(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().getBubbleColumnTicks() < 40);
    }

    private static Boolean isSlowFalling(PlayerData playerData) {
        return Boolean.valueOf(playerData.getStatusTracker().hasSlowFalling()
            || playerData.getStatusTracker().getSlowFallingAmplifier() > 0);
    }

    private static Boolean isCollidingVertically(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().isFeetSampleSolid()
            || playerData.getBlockBreakTracker().isHeadSamplesAllSolid());
    }

    private static Boolean isAutoclicker(PlayerData playerData) {
        if (playerData.getExemptionChecker().isExempt(PLACING, DIGGING, BLOCK_BREAK, FAST, DROPPED_ITEM, WINDOW_CLICK, CREATIVE, CANCELLED_BREAK)) {
            return Boolean.TRUE;
        }
        int now = VulcanBootstrap.INSTANCE.getKeepAliveRunnable().getCurrentTick();
        long millis = System.currentTimeMillis();
        return Boolean.valueOf(
            playerData.getBlockBreakTracker().getTicksSinceBlockPlace() < 20
                || millis - playerData.getClickTracker().getLastClickTimestamp() < 3000L
                || millis - playerData.getClickTracker().getLastFlyingPacketTime() < 250L
                || millis - playerData.getStatusTracker().getLastMillisWindowTime() < 110L
                || now - playerData.getStatusTracker().getLastHookTick() < 20
                || now - playerData.getStatusTracker().getLastProjectileLaunchTick() < 20
                || playerData.getStatusTracker().getTicksSinceJoin() < 7
        );
    }

    private static Boolean isAutoclickerNonDig(PlayerData playerData) {
        if (playerData.getExemptionChecker().isExempt(PLACING, DIGGING, BLOCK_BREAK, FAST, DROPPED_ITEM, WINDOW_CLICK, CANCELLED_BREAK, CREATIVE)) {
            return Boolean.TRUE;
        }
        int now = VulcanBootstrap.INSTANCE.getKeepAliveRunnable().getCurrentTick();
        return Boolean.valueOf(
            now - playerData.getStatusTracker().getLastProjectileLaunchTick() < 20
                || playerData.getStatusTracker().getTicksSinceJoin() < 15
        );
    }
    private static Boolean isCreative(PlayerData data) {
        if (data.getPlayer() != null && data.getPlayer().getGameMode() == GameMode.CREATIVE) {
            return Boolean.TRUE;
        }
        return Boolean.valueOf(data.getStatusTracker().getGameMode() == GameMode.CREATIVE);
    }

    private static Boolean isMythicMob(PlayerData playerData) {
        return Boolean.valueOf(VulcanBootstrap.INSTANCE.isMythicMobsPresent()
            && ConfigValues.settingsHookMythicmobs
            && playerData.getStatusTracker().getTicksSinceMeleeDamage() < 50);
    }

    private static Boolean isCancelledBreak(PlayerData playerData) {
        return Boolean.valueOf(playerData.getStatusTracker().getTicksSinceCancelledBreak() < 40);
    }

    private static Boolean isDroppedItem(PlayerData playerData) {
        int now = VulcanBootstrap.INSTANCE.getKeepAliveRunnable().getCurrentTick();
        return Boolean.valueOf(now - playerData.getStatusTracker().getLastDropItemTick() < 15);
    }

    private static Boolean isWindowClick(PlayerData playerData) {
        return Boolean.valueOf(VulcanBootstrap.INSTANCE.getKeepAliveRunnable().getCurrentTick() - playerData.getStatusTracker().getLastWindowClickTick() < 15);
    }

    private static Boolean isLenientScaffolding(PlayerData playerData) {
        return Boolean.valueOf(ConfigValues.settingsLenientScaffolding
            && playerData.getBlockBreakTracker().getTicksSinceExactScaffolding() < 3);
    }

    private static Boolean isFast(PlayerData playerData) {
        return playerData.getCheckState().isKeepAliveDelayed();
    }

    private static Boolean isCinematic(PlayerData playerData) {
        return Boolean.valueOf(ConfigValues.cinematicEnabled && playerData.getActionTracker().isRecentRotation());
    }

    private static Boolean isSoulSand(PlayerData playerData) {
        return playerData.getBlockBreakTracker().isNearSoulSandOrSoil();
    }

    private static Boolean isWeb(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().isNearCobweb()
            || playerData.getBlockBreakTracker().getTicksSinceCobweb() < 25);
    }

    private static Boolean isDead(PlayerData playerData) {
        Player player = playerData.getPlayer();
        return Boolean.valueOf(player != null && player.isDead());
    }

    private static Boolean isVelocity(PlayerData playerData) {
        return Boolean.valueOf(playerData.getTickTracker().getTicksSinceVelocity() < 40);
    }

    private static Boolean isElytra(PlayerData playerData) {
        Player player = playerData.getPlayer();
        return Boolean.valueOf(
            (player != null && player.isGliding())
                || playerData.getBlockBreakTracker().getElytraTicks() < 100
                || playerData.getStatusTracker().getTicksSinceElytraFlying() < 100);
    }

    private static Boolean isGliding(PlayerData playerData) {
        return Boolean.valueOf(playerData.isGliding()
            || playerData.getStatusTracker().getTicksSinceElytraFlying() < 10);
    }

    private static Boolean isShulker(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().isNearShulker()
            || playerData.getBlockBreakTracker().getTicksSinceShulker() < 40);
    }

    private static Boolean isHalfBlock(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().isNearSlab());
    }

    private static Boolean isScaffolding(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().isNearExactScaffolding()
            || playerData.getBlockBreakTracker().isNearScaffolding()
            || playerData.getBlockBreakTracker().getTicksSinceExactScaffolding() < 30);
    }

    private static Boolean isHoney(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().isNearHoney()
            || playerData.getBlockBreakTracker().isNearHoneyBlock());
    }

    private static Boolean isIllegalBlock(PlayerData playerData) {
        BlockBreakTracker blocks = playerData.getBlockBreakTracker();
        return Boolean.valueOf(
            blocks.isNearTrapdoor()
                || blocks.isNearHoney()
                || blocks.isNearCarpet()
                || blocks.isNearSkull()
                || blocks.isNearExactScaffolding()
                || blocks.isNearCampfire()
        );
    }

    private static Boolean isFallDamage(PlayerData playerData) {
        return Boolean.valueOf(playerData.getStatusTracker().getTicksSinceFallDamage() < 40);
    }

    private static Boolean isWorldChange(PlayerData playerData) {
        return Boolean.valueOf(playerData.getStatusTracker().getTicksSinceWorldChange() < 40);
    }

    private static Boolean isFireball(PlayerData playerData) {
        return Boolean.valueOf(playerData.getStatusTracker().getTicksSinceFireball() < 25);
    }

    private static Boolean isPlacedSlime(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().isNearSlime()
            || playerData.getStatusTracker().getTicksSinceBlockPlace() < 120);
    }

    private static Boolean isNotMoving(PlayerData playerData) {
        return Boolean.valueOf(!playerData.getBlockBreakTracker().hasPositionDelta());
    }

    private static Boolean isPartiallyStuck(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().isNearbyHeadSolid()
            && !playerData.getBlockBreakTracker().isNearbySolid());
    }

    private static Boolean isPlacedClimbable(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().isNearClimbable()
            || playerData.getStatusTracker().getTicksSinceClimbablePlace() < 30);
    }

    private static Boolean isFullyStuck(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().isNearbySolid()
            && playerData.getBlockBreakTracker().isHeadSamplesAllSolid());
    }

    private static Boolean isSwimmingOnOldVersion(PlayerData playerData) {
        if (playerData.getBlockBreakTracker().getLiquidTicks() >= 60) {
            return Boolean.FALSE;
        }
        return Boolean.valueOf(
            playerData.getUser().getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_13)
                && ServerCompatibility.isServerOlderThan1_13()
        );
    }

    private static Boolean isBlockPlaceFast(PlayerData playerData) {
        return Boolean.valueOf(playerData.getStatusTracker().getTicksSinceBlockPlace() < 15);
    }

    private static Boolean isBlockPlace(PlayerData playerData) {
        return Boolean.valueOf(playerData.getStatusTracker().getTicksSinceBlockPlace() < 30);
    }

    private static Boolean isCancelledPlace(PlayerData playerData) {
        return Boolean.valueOf(playerData.getStatusTracker().getTicksSinceCancelledMove() < 40);
    }

    private static Boolean isServerPositionFast(PlayerData playerData) {
        return Boolean.valueOf(playerData.getStatusTracker().getTicksSinceLastServerPos() < 5);
    }

    private static Boolean isServerPosition(PlayerData playerData) {
        return Boolean.valueOf(playerData.getStatusTracker().getTicksSinceLastServerPos() < 8);
    }

    private static Boolean isPlacing(PlayerData playerData) {
        return Boolean.valueOf(playerData.getStatusTracker().getTicksSinceBlockPlace() < 10);
    }

    private static Boolean isBlockBreak(PlayerData playerData) {
        return Boolean.valueOf(playerData.getStatusTracker().getTicksSinceBlockBreak() < 25);
    }

    private static Boolean isDigging(PlayerData playerData) {
        return Boolean.valueOf(playerData.getStatusTracker().getTicksSinceDig() < 20);
    }

    private static Boolean isFrozen(PlayerData playerData) {
        return playerData.getBlockBreakTracker().isFrozen();
    }

    private static Boolean isLevitation(PlayerData playerData) {
        return Boolean.valueOf(playerData.getStatusTracker().getLevitationAmplifier() > 0
            || playerData.getBlockBreakTracker().getLevitationTicks() < 75);
    }

    private static Boolean isDepthStrider(PlayerData playerData) {
        return Boolean.valueOf(playerData.getStatusTracker().hasDepthStrider());
    }

    private static Boolean isDeath(PlayerData playerData) {
        return Boolean.valueOf(playerData.getStatusTracker().getTicksSinceDeath() < 25);
    }

    private static Boolean isEntityCollision(PlayerData playerData) {
        return Boolean.valueOf(ConfigValues.entityCollisionEnabled && playerData.getBlockBreakTracker().getEntityCollisionTicks() < 8);
    }

    private static Boolean isCauldron(PlayerData playerData) {
        return playerData.getBlockBreakTracker().isNearCauldron();
    }
    private static Boolean isSpectator(PlayerData data) {
        if (!ServerCompatibility.isServer1_8OrNewer() || data.getPlayer() == null) {
            return Boolean.FALSE;
        }
        return Boolean.valueOf(data.getPlayer().getGameMode() == GameMode.SPECTATOR);
    }

    private static Boolean isFirework(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().getTicksSinceFirework() < 150);
    }

    private static Boolean isRiptide(PlayerData playerData) {
        return Boolean.valueOf(ServerCompatibility.isModernProtocol() && playerData.getBlockBreakTracker().getRiptideTicks() < 80);
    }

    private static Boolean isPluginLoad(PlayerData playerData) {
        return Boolean.valueOf(System.currentTimeMillis() - VulcanBootstrap.INSTANCE.getStartTime() < 10000L);
    }

    private static Boolean isFishingRod(PlayerData playerData) {
        int now = VulcanBootstrap.INSTANCE.getKeepAliveRunnable().getCurrentTick();
        return Boolean.valueOf(now - playerData.getStatusTracker().getLastHookTick() < 100);
    }

    private static Boolean isTrapdoor(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().isNearTrapdoor());
    }

    private static Boolean isSoulSpeed(PlayerData playerData) {
        return Boolean.valueOf(ServerCompatibility.isServer1_16OrNewer() && playerData.getBlockBreakTracker().getSoulSpeedTicks() < 60);
    }
    private static Boolean isSleeping(PlayerData data) {
        Player player = data.getPlayer();
        if (player != null && player.isSleeping()) {
            return Boolean.TRUE;
        }
        int now = VulcanBootstrap.INSTANCE.getKeepAliveRunnable().getCurrentTick();
        return Boolean.valueOf(now - data.getStatusTracker().getLastBedEnterTick() < 40);
    }

    private static Boolean isNearGround(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().isNearbySolid()
            || playerData.getBlockBreakTracker().isNearbyHeadSolid());
    }

    private static Boolean isEnderPearl(PlayerData playerData) {
        return Boolean.valueOf(playerData.getStatusTracker().getTicksSincePearlTeleport() < 5);
    }

    private static Boolean isChorusFruit(PlayerData playerData) {
        return Boolean.valueOf(playerData.getStatusTracker().getTicksSinceChorusFruit() < 25);
    }

    private static Boolean isPiston(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().isNearPiston());
    }

    private static Boolean isPlacedWeb(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().isNearCobweb()
            || playerData.getStatusTracker().getTicksSinceCobwebPlace() < 25);
    }

    private static Boolean isConduit(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().isNearConduit());
    }

    private static Boolean isSweetBerries(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().isNearSweetBerry()
            || playerData.getBlockBreakTracker().isNearSweetBerryBush());
    }

    private static Boolean isWall(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().isNearWall()
            || playerData.getBlockBreakTracker().getTicksSinceWall() < 20);
    }

    private static Boolean isHighSpeed(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().getHighWalkSpeedTicks() < 40);
    }

    private static Boolean isLilyPad(PlayerData playerData) {
        return playerData.getBlockBreakTracker().isNearLilyPad();
    }

    private static Boolean isHighJumpBoost(PlayerData playerData) {
        return Boolean.valueOf(playerData.getStatusTracker().getJumpBoostAmplifier() > 2);
    }

    private static Boolean isSlab(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().isNearSlab());
    }

    private static Boolean isBed(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().isNearBed()
            || playerData.getBlockBreakTracker().getTicksSinceBedOrBedrock() < 30);
    }

    private static Boolean isIce(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().isNearIce()
            || playerData.getBlockBreakTracker().getTicksSinceFrostedIce() < 20);
    }

    private static Boolean isRespawn(PlayerData playerData) {
        return Boolean.valueOf(VulcanBootstrap.INSTANCE.getKeepAliveRunnable().getCurrentTick() - playerData.getStatusTracker().getLastClientRespawnTick() < 20);
    }

    private static Boolean isFullySubmerged(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().getLiquidTicks() < 20);
    }

    private static Boolean isLaggedNearGroundModern(PlayerData playerData) {
        return Boolean.valueOf(ServerCompatibility.isModernProtocol()
            && playerData.getCheckState().isKeepAliveDelayed()
            && (playerData.getBlockBreakTracker().isNearbySolid()
                || playerData.getBlockBreakTracker().isClientOnGround()));
    }

    private static Boolean isAnvil(PlayerData playerData) {
        return playerData.getBlockBreakTracker().isNearAnvil();
    }

    private static Boolean isPickedUpItem(PlayerData playerData) {
        int now = VulcanBootstrap.INSTANCE.getKeepAliveRunnable().getCurrentTick();
        return Boolean.valueOf(now - playerData.getStatusTracker().getItemPickupTick() < 30);
    }

    private static Boolean isDoor(PlayerData playerData) {
        return playerData.getBlockBreakTracker().isNearDoor();
    }

    private static Boolean isChain(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().isNearChain());
    }

    private static Boolean isHopper(PlayerData playerData) {
        return playerData.getBlockBreakTracker().isNearHopper();
    }

    private static Boolean isEndRod(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().isNearEndRod()
            || playerData.getBlockBreakTracker().getTicksSinceEndRod() < 20);
    }

    private static Boolean isEmptiedBucket(PlayerData playerData) {
        int now = VulcanBootstrap.INSTANCE.getKeepAliveRunnable().getCurrentTick();
        return Boolean.valueOf(now - playerData.getStatusTracker().getLastBucketEmptyTick() < 15);
    }

    private static Boolean isFlowerPot(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().isNearFlowerPot()
            || playerData.getBlockBreakTracker().getFlowerPotTicks() < 20);
    }

    private static Boolean isCarpet(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().isNearCarpet()
            || playerData.getBlockBreakTracker().getTicksSinceCarpet() < 20
            || playerData.getBlockBreakTracker().getTicksSinceNearbyCarpet() < 20);
    }

    private static Boolean isCancelledMove(PlayerData playerData) {
        return Boolean.valueOf(playerData.getStatusTracker().getTicksSinceCancelledMove() < 5);
    }

    private static Boolean isCampfire(PlayerData playerData) {
        return playerData.getBlockBreakTracker().isNearCampfire();
    }

    private static Boolean isNearSolid(PlayerData playerData) {
        return playerData.getBlockBreakTracker().isNearSolid();
    }

    private static Boolean isJoinedChunkLoad(PlayerData playerData) {
        return Boolean.valueOf(System.currentTimeMillis() - playerData.getJoinTime() < 30000L);
    }

    private static Boolean isZeroDelta(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().isUnmoved());
    }

    private static Boolean isNetheriteArmor(PlayerData playerData) {
        StatusTracker status = playerData.getStatusTracker();
        org.bukkit.inventory.ItemStack[] pieces = new org.bukkit.inventory.ItemStack[] {
            status.getHelmet(), status.getChestplate(), status.getLeggings(), status.getBoots()
        };
        for (int i = 0; i < pieces.length; ++i) {
            if (pieces[i] != null && pieces[i].getType().toString().contains("NETHERITE")) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    private static Boolean isSnow(PlayerData playerData) {
        return playerData.getBlockBreakTracker().isNearSnow();
    }

    private static Boolean isLaggedNearGround(PlayerData playerData) {
        return Boolean.valueOf(playerData.getCheckState().isKeepAliveDelayed()
            && (playerData.getBlockBreakTracker().isNearbySolid()
                || playerData.getBlockBreakTracker().isClientOnGround()));
    }

    private static Boolean isJumpBoostRanOut(PlayerData playerData) {
        return Boolean.valueOf(playerData.getStatusTracker().getTicksSinceJumpStrengthChange() > 0
            && playerData.getStatusTracker().getTicksSinceJumpStrengthChange() < 30);
    }

    private static Boolean isDragonDamage(PlayerData playerData) {
        return Boolean.valueOf(playerData.getStatusTracker().getTicksSinceNonFallDamage() < 60);
    }

    private static Boolean isPowderSnow(PlayerData playerData) {
        return playerData.getBlockBreakTracker().isNearPowderSnow();
    }

    private static Boolean isFenceGate(PlayerData playerData) {
        return playerData.getBlockBreakTracker().isNearFenceGate();
    }

    private static Boolean isHighLevitation(PlayerData playerData) {
        return Boolean.valueOf(playerData.getStatusTracker().getLevitationAmplifier() > 10);
    }

    private static Boolean isStairs(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().isNearStair());
    }

    private static Boolean isExplosion(PlayerData playerData) {
        return Boolean.valueOf(playerData.getTickTracker().getTicksSinceExplosion() < 50);
    }

    private static Boolean isSkull(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().getTicksSinceSkull() < 20);
    }

    private static Boolean isAttackDamage(PlayerData playerData) {
        return Boolean.valueOf(playerData.getStatusTracker().getTicksSinceMeleeDamage() < 80);
    }

    private static Boolean isChest(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().isNearChest());
    }

    private static Boolean isProjectileDamage(PlayerData playerData) {
        return Boolean.valueOf(playerData.getStatusTracker().getTicksSinceProjectileDamage() < 20);
    }

    private static Boolean isGlitchedBlocksAbove(PlayerData playerData) {
        java.util.List materials = playerData.getBlockBreakTracker().getNearbyHeadSamples();
        if (materials == null || materials.isEmpty()) {
            return Boolean.FALSE;
        }
        for (Object raw : materials) {
            if (!(raw instanceof org.bukkit.Material)) {
                continue;
            }
            org.bukkit.Material material = (org.bukkit.Material) raw;
            if (BoundingBoxUtil.isSlab(material)
                || BoundingBoxUtil.isStair(material)
                || BoundingBoxUtil.isTrapdoor(material)
                || BoundingBoxUtil.isFence(material)
                || BoundingBoxUtil.isFenceGate(material)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    private static Boolean isHighFlySpeed(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().getHighFlySpeedTicks() < 100);
    }

    private static Boolean isDripstone(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().isNearDripstone());
    }

    private static Boolean isCollidingHorizontally(PlayerData playerData) {
        return playerData.getBlockBreakTracker().isNearCollidingHorizontally();
    }

    private static Boolean isSwimmingJesus(PlayerData playerData) {
        return Boolean.valueOf(playerData.getStatusTracker().isSwimmingMetadata());
    }

    private static Boolean isSwimming(PlayerData playerData) {
        return Boolean.valueOf(playerData.getPlayer() != null && playerData.getPlayer().isSwimming());
    }

    private static Boolean isPressurePlate(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().getPressurePlateTicks() < 20);
    }

    private static Boolean isSign(PlayerData playerData) {
        return playerData.getBlockBreakTracker().isNearSign();
    }

    private static Boolean isAroundSlime(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().isNearSlime()
            || playerData.getBlockBreakTracker().getTicksSinceSlime() < 20);
    }

    private static Boolean isEntityCramFix(PlayerData playerData) {
        return Boolean.valueOf(ConfigValues.entityCramFixEnabled
            && playerData.getStatusTracker().getTicksSinceJoin() < ConfigValues.settingsEntityCramExemptTicks);
    }

    private static Boolean isPacketExplosion(PlayerData playerData) {
        return Boolean.valueOf(playerData.getTickTracker().getTicksSinceExplosion() < 16);
    }

    private static Boolean isHappyGhast(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().isNearHappyGhast()
            || playerData.getBlockBreakTracker().getTicksSinceHappyGhast() < 25);
    }

    private static Boolean isBamboo(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().isNearBamboo()
            || playerData.getBlockBreakTracker().getTicksSinceBamboo() < 20);
    }

    private static Boolean isCake(PlayerData playerData) {
        return Boolean.valueOf(playerData.getBlockBreakTracker().isNearCake());
    }

    private static Boolean isJoined(PlayerData playerData) {
        if (System.currentTimeMillis() - playerData.getJoinTime() < ConfigValues.joinCheckWaitTime) {
            return Boolean.TRUE;
        }
        return Boolean.valueOf(playerData.getStatusTracker().getTicksLived() < ConfigValues.settingsMinTicksExisted);
    }

    private static Boolean isChunk(PlayerData playerData) {
        Player player = playerData.getPlayer();
        if (player == null || player.getWorld() == null) {
            return Boolean.TRUE;
        }
        int chunkX = NumberConversions.floor(playerData.getBlockBreakTracker().getLocationX()) >> 4;
        int chunkZ = NumberConversions.floor(playerData.getBlockBreakTracker().getLocationZ()) >> 4;
        return Boolean.valueOf(!player.getWorld().isChunkLoaded(chunkX, chunkZ));
    }

    private static Boolean isTps(PlayerData playerData) {
        return Boolean.valueOf(ServerCompatibility.getTicksPerSecond() < 19.25);
    }
}
