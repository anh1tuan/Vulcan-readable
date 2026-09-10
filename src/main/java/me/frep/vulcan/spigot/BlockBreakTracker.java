package me.frep.vulcan.spigot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.DiggingAction;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerDigging;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientVehicleMove;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerAbilities;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerPositionAndLook;
import me.frep.vulcan.api.event.VulcanGhostBlockEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Boat;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EnderDragonPart;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.NumberConversions;


public class BlockBreakTracker {
    private final PlayerData playerData;
    private boolean nearCobweb;
    private boolean nearIce;
    private boolean nearSweetBerry;
    private boolean nearSoulSandOrSoil;
    private boolean nearBubbleColumn;
    private boolean nearWall;
    private boolean nearLilyPad;
    private boolean nearTrapdoor;
    private boolean nearHoney;
    private boolean nearBed;
    private boolean nearShulker;
    private boolean nearCarpet;
    
    private boolean nearExactScaffolding;
    private boolean nearSlime;
    private boolean nearFlowerPot;
    private boolean nearPiston;
    private boolean nearCampfire;
    private boolean nearSweetBerryBush;
    private boolean onSeaPickle;
    private boolean nearSnow;
    private boolean nearAnvil;
    private boolean nearExactEndRod;
    private boolean nearEndRod;
    private boolean nearDoor;
    private boolean nearCauldron;
    private boolean nearHopper;
    private boolean nearFenceGate;
    private boolean onLilyPad;
    private boolean nearClimbableColumn;
    private boolean cobwebInColumn;
    private boolean bubbleColumnInColumn;
    private boolean liquidInColumn;
    private boolean headSamplesAllSolid;
    private boolean nearKelp;
    private boolean nearSeagrass;
    private boolean nearPackedIce;
    private boolean nearbySolid;
    private boolean nearbyHeadSolid;
    private boolean feetSampleSolid;
    private boolean clientOnGround;
    private boolean blockGridAlignedY;
    private boolean unmoved;
    private boolean nearBoat;
    private boolean nearBell;
    private boolean nearBedOrBedrock;
    private boolean nearSoulSand;
    private boolean nearLectern;
    private boolean nearSeaPickle;
    private boolean touchingSolid;
    private boolean nearConduit;
    private boolean nearCollidingHorizontally;
    private boolean nearCampfireBlock;
    private boolean nearSolid;
    private boolean nearChest;
    private boolean vehicleNearSolid;
    private boolean vehicleNearIce;
    private boolean vehicleNearSweetBerry;
    private boolean vehicleAllSolid;
    private boolean hasPositionDelta;
    private boolean nearFrostedIce;
    private boolean nearExactBubbleColumn;
    private boolean nearFarmland;
    private boolean lastClientOnGround;
    private boolean flightAllowed;
    private boolean nearHoneyBlock;
    private boolean nearSkull;
    private boolean vehicleNearEndRod;
    private boolean nearPressurePlate;
    private boolean vehicleNearBed;
    private boolean nearFence;
    private boolean nearPowderSnow;
    private boolean collidingLivingEntity;
    private boolean previousPreviousClientOnGround;
    private boolean nearbyLivingEntity;
    private boolean nearCake;
    private boolean nearSlab;
    private boolean nearWorldBorder;
    private boolean nearStair;
    private boolean jumpCollisionExempt;
    private boolean nearSign;
    private boolean nearClimbable;
    private boolean nearBamboo;
    private boolean breakingBlock;
    private boolean nearScaffolding;
    private boolean inPowderSnow;
    private boolean feetOnIce;
    private boolean blockBelowSolidOrIce;
    private boolean carpetInHeadSample;
    private boolean nearDripleaf;
    private boolean nearDripstone;
    private boolean nearChain;
    private boolean nearTurtleEgg;
    private boolean nearHeavyCore;
    private boolean positionChanged;
    private boolean lastPositionChanged;
    private boolean previousPreviousPositionChanged;
    private boolean nearHappyGhast;
    private boolean collidingDragon;
    private boolean nearDriedGhast;
    private boolean nearBlueIce;
    private Location lastLocation;
    private Location currentLocation;
    private final Map nearbyEntityScratch = new HashMap();
    public boolean frozen;
    public List nearbyEntities;
    private Material blockBelow;
    private Material blockTwoBelow;
    private Material blockThreeBelow;
    private List nearbyMaterials;
    private List nearbyFeetSamples;
    private List nearbyHeadSamples;
    private List nearbyHeadSurroundSamples;
    private List nearbyFeetSurroundSamples;
    public List nearbyColumnMaterials;
    public List vehicleNearbyMaterials;
    public List nearbyColumnExtra;
    public Material standingMaterial;
    public Material lastHeldMaterial;
    public Material alternateHeldMaterial;
    private double locationX;
    private double locationY;
    private double locationZ;
    private double previousX;
    private double previousY;
    private double previousZ;
    private double deltaX;
    private double deltaY;
    private double deltaZ;
    private double deltaXZ;
    private double hypotDelta;
    private double lastDeltaX;
    private double lastDeltaZ;
    private double lastDeltaY;
    private double lastDeltaXZ;
    private double previousDeltaY;
    private double vehicleX;
    private double vehicleY;
    private double vehicleZ;
    private double lastVehicleX;
    private double lastVehicleY;
    private double lastVehicleZ;
    private double lastVehicleDeltaX;
    private double lastVehicleDeltaY;
    private double lastVehicleDeltaZ;
    private double vehicleDeltaX;
    private double vehicleDeltaY;
    private double vehicleDeltaZ;
    private double vehicleDeltaXZ;
    private double lastServerX;
    private double lastServerY;
    private double lastServerZ;
    private double ghostBlockBuffer;
    private double lastPreviousX;
    private double lastPreviousY;
    private double lastPreviousZ;
    private double lastLocationX;
    private double lastLocationY;
    private double lastLocationZ;
    private double webSlowdown;
    private double recordedDeltaXZ;
    private int vehicleTicks = 100;
    private int ticksSinceInsideVehicle = 100;
    private int ticksSinceFlightAllowed = 100;
    private int ticksSinceSlime = 100;
    private int airTicks;
    private int groundTicks;
    private int ticksSinceOnGround = 100;
    private int riptideTicks = 200;
    private int ticksSinceRiptide = 200;
    private int ticksSinceCollisionExempt = 100;
    private int ticksSinceSolidFeet;
    private int ticksSinceHoldingRiptide = 100;
    private int levitationTicks = 100;
    private int bubbleColumnTicks = 100;
    private int ticksGliding;
    private int ticksSinceSprinting = 100;
    private int ghostBlockAirTicks;
    private int alignedYTicks;
    private int ticksSinceWall = 100;
    private int liquidTicks = 100;
    private int ticksSinceClimbableColumn;
    private int ticksAirborne = 100;
    private int packedIceTicks = 100;
    private int pressurePlateTicks = 100;
    private int soulSandTicks = 100;
    private int ticksSinceCobweb = 100;
    private int soulSpeedTicks = 100;
    private int ticksSinceBedOrBedrock = 100;
    private int lastDigStartTick;
    private int vehicleMoveTicks;
    private int ticksSinceBlockPlace = 200;
    private int ticksSinceTouchingSolid = 100;
    private int ticksSinceEndRod = 100;
    private int endRodTicks;
    private int sneakTicks = 100;
    private int ticksSinceFirework = 1000;
    private int ticksSinceBamboo = 100;
    private int ticksSinceNearbySweetBerry = 500;
    private int ticksSinceNearbyEndRod = 500;
    private int ticksSinceVehicleSolid = 500;
    private int ticksSinceVehicleIce = 150;
    private int ticksSinceVehicleSweetBerry = 150;
    private int seaPickleTicks = 150;
    private int blockX;
    private int blockY;
    private int blockZ;
    private int ticksSinceFrostedIce;
    private int ticksSinceExactScaffolding = 100;
    private int ticksSinceGhostBlock = 500;
    private int lastAbilitiesTick;
    private int ticksSinceVehicleAllSolid;
    private int ticksStationary = 100;
    private int elytraTicks = 100;
    private int ticksSinceSkull = 100;
    private int farmlandTicks = 100;
    private int ticksSinceShulker;
    private int ticksSinceCarpet;
    private int entityCollisionTicks = 100;
    private int ticksSinceVehicleEndRod = 100;
    private int ticksSinceDigStart = 1000;
    private int highWalkSpeedTicks = 100;
    private int highFlySpeedTicks = 200;
    private int ticksSinceNearbyCarpet = 100;
    private int ticksSinceVehicleBed = 1000;
    private int waterloggedTicks = 1000;
    private int flowerPotTicks = 1000;
    private int ticksSinceNearbyBubbleColumn = 1000;
    private int carpetTicks = 0;
    private int ticksSinceFail = 1000;
    private int ticksSinceHorizontalCollision = 100;
    private int ticksSinceNotSpectator = 100;
    private int ticksSinceFeetIce = 100;
    private int ticksSinceStandingOnSolidOrIce = 100;
    private int ticksSinceBoat = 100;
    private int ticksSinceMoving = 100;
    private int ticksSinceRiptideWeapon = 1000;
    private int ticksSinceHappyGhast = 100;
    private int seagrassTicks = 0;
    private World world;
    private Entity vehicleEntity;
    private Entity previousVehicleEntity;
    private float flySpeed = 0.2f;
    private float abilityFlySpeed = 0.2f;
    private List pendingRunnables = new ArrayList();
    private LinkedList jumpFlags = new LinkedList();
    private boolean flightExempt;
    private boolean creativeExempt;
    private boolean joinedExempt;
    private boolean liquidExempt;
    private boolean levitationExempt;
    private boolean slowFallingExempt;
    private boolean riptideExempt;
    private boolean vehicleExempt;
    private boolean lenientScaffoldingExempt;
    private boolean glidingExempt;
    private boolean elytraExempt;
    private boolean teleportExempt;
    private boolean enderPearlExempt;
    private boolean chunkExempt;
    private boolean comboModeExempt;
    private boolean mythicMobExempt;
    private boolean climbableExempt;
    public BlockBreakTracker(PlayerData playerData) {
        this.playerData = playerData;
    }

    public void handleFlyingPacket(WrapperPlayClientPlayerFlying packet, long timestamp) {
        this.previousPreviousClientOnGround = this.lastClientOnGround;
        this.lastClientOnGround = this.clientOnGround;
        this.clientOnGround = packet.isOnGround();
        this.previousPreviousPositionChanged = this.lastPositionChanged;
        this.lastPositionChanged = this.positionChanged;
        this.positionChanged = packet.hasPositionChanged();

        if (this.jumpFlags.size() == 5) {
            this.jumpFlags.pollLast();
        }

        JumpFlag current = new JumpFlag(
            packet.getLocation().getX(),
            packet.getLocation().getY(),
            packet.getLocation().getZ()
        );
        JumpFlag previous = this.pollJumpFlag(0, timestamp);
        current.setOnGround(packet.isOnGround());
        current.setDeltaX(current.getPosX() - previous.getPosX());
        current.setDeltaY(current.getPosY() - previous.getPosY());
        current.setDeltaZ(current.getPosZ() - previous.getPosZ());
        current.setDistance(TrigUtil.hypot2(current.getDeltaX(), current.getDeltaZ()));
        this.jumpFlags.addFirst(current);

        if (timestamp <= 0L) {
            return;
        }
        if (packet.hasPositionChanged()) {
            this.breakingBlock = this.blockX != 0 || this.blockY != 0 || this.blockZ != 0;
            this.world = this.playerData.getPlayer().getWorld();
            this.lastPreviousX = this.previousX;
            this.lastPreviousY = this.previousY;
            this.lastPreviousZ = this.previousZ;
            this.previousX = this.locationX;
            this.previousY = this.locationY;
            this.previousZ = this.locationZ;
            this.locationX = packet.getLocation().getX();
            this.locationY = packet.getLocation().getY();
            this.locationZ = packet.getLocation().getZ();
            this.lastLocation = this.currentLocation == null
                ? new Location(this.world, this.locationX, this.locationY, this.locationZ)
                : this.currentLocation;
            this.currentLocation = new Location(this.world, this.locationX, this.locationY, this.locationZ);
            this.previousDeltaY = this.lastDeltaY;
            this.lastDeltaX = this.deltaX;
            this.lastDeltaY = this.deltaY;
            this.lastDeltaZ = this.deltaZ;
            this.lastDeltaXZ = this.deltaXZ;
            this.deltaX = this.locationX - this.previousX;
            this.deltaY = this.locationY - this.previousY;
            this.deltaZ = this.locationZ - this.previousZ;
            this.deltaXZ = TrigUtil.hypot2(this.deltaX, this.deltaZ);
            this.hypotDelta = TrigUtil.hypot3(this.deltaX, this.deltaY, this.deltaZ);
            this.hasPositionDelta = Math.abs(this.deltaX) > 0.0
                || Math.abs(this.deltaY) > 0.0
                || Math.abs(this.deltaZ) > 0.0;
            this.unmoved = !this.hasPositionDelta;
            this.blockGridAlignedY = this.locationY % 0.015625 == 0.0;
            if (this.playerData.getPlayer().getVehicle() == null) {
                this.vehicleMoveTicks = 0;
            }

            this.sampleCollision();
            this.updateGroundAndAirTicks();
            this.handleGhostBlockFix();
            this.handleUnloadedChunkSetback();
            this.updateCollisionTickCounters();

            if (this.playerData.getStatusTracker().getWalkSpeed() > 0.11) {
                this.highWalkSpeedTicks = 0;
            }

            if (ConfigValues.settingsVelocityWorldBorder && ServerCompatibility.isServer1_8OrNewer()) {
                org.bukkit.WorldBorder border = this.world.getWorldBorder();
                double centerX = border.getCenter().getX();
                double centerZ = border.getCenter().getZ();
                double halfSize = border.getSize() / 2.0;
                double[] edgeDistances = new double[] {
                    this.locationX - centerX - halfSize,
                    this.locationX - centerX + halfSize,
                    this.locationZ - centerZ - halfSize,
                    this.locationZ - centerZ + halfSize
                };
                for (int i = 0; i < edgeDistances.length; ++i) {
                    if (edgeDistances[i] < 0.0) {
                        edgeDistances[i] = -edgeDistances[i];
                    }
                }
                Arrays.sort(edgeDistances);
                this.nearWorldBorder = edgeDistances[0] < 1.0;
            }
        }

    }

    public JumpFlag pollJumpFlag(int index, long timestamp) {
        int current = 0;
        JumpFlag selected = new JumpFlag(0.0, 0.0, 0.0);
        for (Object raw : this.jumpFlags) {
            JumpFlag flag = (JumpFlag) raw;
            selected = flag;
            if (current == index) {
                break;
            }
            ++current;
        }
        return selected;
    }

    private void refreshFlightExemption() {
        ExemptionChecker exemptionChecker = this.playerData.getExemptionChecker();
        this.flightExempt = exemptionChecker.isExempt(ExemptType.FLIGHT);
        this.creativeExempt = exemptionChecker.isExempt(ExemptType.CREATIVE);
        this.joinedExempt = exemptionChecker.isExempt(ExemptType.JOINED);
        this.liquidExempt = exemptionChecker.isExempt(ExemptType.LIQUID);
        this.levitationExempt = exemptionChecker.isExempt(ExemptType.LEVITATION);
        this.slowFallingExempt = exemptionChecker.isExempt(ExemptType.SLOW_FALLING);
        this.riptideExempt = exemptionChecker.isExempt(ExemptType.RIPTIDE);
        this.vehicleExempt = exemptionChecker.isExempt(ExemptType.VEHICLE);
        this.lenientScaffoldingExempt = exemptionChecker.isExempt(ExemptType.LENIENT_SCAFFOLDING);
        this.glidingExempt = exemptionChecker.isExempt(ExemptType.GLIDING);
        this.elytraExempt = exemptionChecker.isExempt(ExemptType.ELYTRA);
        this.teleportExempt = exemptionChecker.isExempt(ExemptType.TELEPORT);
        this.enderPearlExempt = exemptionChecker.isExempt(ExemptType.ENDER_PEARL);
        this.chunkExempt = exemptionChecker.isExempt(ExemptType.CHUNK);
        this.comboModeExempt = exemptionChecker.isExempt(ExemptType.COMBO_MODE);
        this.mythicMobExempt = exemptionChecker.isExempt(ExemptType.MYTHIC_MOB);
        this.climbableExempt = exemptionChecker.isExempt(ExemptType.CLIMBABLE);
    }
    private void sampleCollision() {
        this.blockX = NumberConversions.floor((double) this.locationX);
        this.blockY = NumberConversions.floor((double) this.locationY);
        this.blockZ = NumberConversions.floor((double) this.locationZ);
        this.nearbyMaterials = BoundingBoxUtil.getNearbyMaterials(this.world, this.blockX, this.blockY, this.blockZ);
        this.nearbyColumnMaterials = this.nearbyMaterials;
        this.nearbyColumnExtra = this.nearbyMaterials;
        this.nearbyFeetSamples = CollisionUtil.sampleFeetColumnBlocks(this.playerData);
        this.nearbyHeadSamples = CollisionUtil.sampleHeadColumnBlocks(this.playerData);
        this.nearbyHeadSurroundSamples = CollisionUtil.sampleNearbyHeadBlocks(this.playerData);
        this.nearbyFeetSurroundSamples = CollisionUtil.sampleNearbyFeetBlocks(this.playerData);
        this.blockBelow = BoundingBoxUtil.getBlockType(
            this.world, this.blockX, NumberConversions.floor((double) (this.locationY - 1.0)), this.blockZ
        );
        this.blockTwoBelow = BoundingBoxUtil.getBlockType(
            this.world, this.blockX, NumberConversions.floor((double) (this.locationY - 2.0)), this.blockZ
        );
        this.blockThreeBelow = BoundingBoxUtil.getBlockType(
            this.world, this.blockX, NumberConversions.floor((double) (this.locationY - 3.0)), this.blockZ
        );
        this.headSamplesAllSolid = CollectionPredicates.matches(this.nearbyHeadSamples, m -> isSolidMaterial((Material) m));
        this.nearCollidingHorizontally = CollectionPredicates.exists(this.nearbyFeetSurroundSamples, m -> isSolidMaterial((Material) m))
            || CollectionPredicates.exists(this.nearbyHeadSurroundSamples, m -> isSolidMaterial((Material) m));
        this.nearSolid = CollectionPredicates.exists(this.nearbyMaterials, m -> isSolidMaterial((Material) m));
        this.nearbySolid = CollectionPredicates.matches(this.nearbyFeetSamples, m -> isSolidMaterial((Material) m));
        this.nearbyHeadSolid = CollectionPredicates.matches(this.nearbyHeadSamples, m -> isSolidMaterial((Material) m));
        this.updateNearbyCollisionFlags();
        this.updateHeadAndFeetFlags();
    }

    private void updateHeadAndFeetFlags() {
        this.carpetInHeadSample = false;
        if (this.nearbyHeadSamples != null) {
            for (Object raw : this.nearbyHeadSamples) {
                if (raw instanceof Material && BoundingBoxUtil.isCarpet((Material) raw)) {
                    this.carpetInHeadSample = true;
                    break;
                }
            }
        }
        this.feetSampleSolid = false;
        if (this.nearbyFeetSamples != null) {
            for (Object raw : this.nearbyFeetSamples) {
                if (raw instanceof Material && BoundingBoxUtil.isSolid((Material) raw)) {
                    this.feetSampleSolid = true;
                    break;
                }
            }
        }
        if (!this.unmoved
            && !this.nearSlime
            && !this.nearSoulSand
            && !this.nearCampfire
            && !this.nearHoney
            && !this.nearCampfireBlock
            && !this.nearTrapdoor
            && !this.nearFlowerPot
            && !this.nearLilyPad
            && !this.onLilyPad
            && !this.nearHappyGhast) {
            this.ghostBlockAirTicks = 0;
        }
    }


    private void updateNearbyCollisionFlags() {
        this.nearDripstone = false;
        this.nearChain = false;
        this.nearTurtleEgg = false;
        this.nearDripleaf = false;
        this.nearHeavyCore = false;
        this.nearDriedGhast = false;
        this.nearBlueIce = false;
        this.collidingLivingEntity = false;
        this.nearHappyGhast = false;
        this.cobwebInColumn = false;
        this.bubbleColumnInColumn = false;
        this.liquidInColumn = false;
        this.inPowderSnow = false;
        this.nearFence = false;
        this.nearScaffolding = false;
        this.nearBamboo = false;
        this.nearClimbable = false;
        this.nearSign = false;
        this.nearPressurePlate = false;
        this.nearSkull = false;
        this.nearFarmland = false;
        this.nearBedOrBedrock = false;
        this.nearFlowerPot = false;
        this.nearChest = false;
        this.nearEndRod = false;
        this.nearLilyPad = false;
        this.nearDoor = false;
        this.nearHopper = false;
        this.nearCarpet = false;
        this.nearStair = false;
        this.nearBoat = false;
        this.nearCampfireBlock = false;
        this.nearAnvil = false;
        this.nearSnow = false;
        this.nearFenceGate = false;
        this.touchingSolid = false;
        this.nearShulker = false;
        this.nearCauldron = false;
        this.nearBed = false;
        this.nearHoney = false;
        this.nearTrapdoor = false;
        this.nearSlab = false;
        this.nearCake = false;
        this.nearPiston = false;
        this.nearSlime = false;
        this.onLilyPad = false;
        this.nearWall = false;
        this.nearBubbleColumn = false;
        this.nearSoulSandOrSoil = false;
        this.nearSoulSand = false;
        this.nearIce = false;
        this.nearCobweb = false;
        this.nearSweetBerry = false;
        this.nearExactScaffolding = false;
        this.nearSweetBerryBush = false;
        this.nearFrostedIce = false;
        this.nearExactBubbleColumn = false;
        this.nearBell = false;
        this.nearLectern = false;
        this.nearConduit = false;
        this.nearKelp = false;
        this.nearSeagrass = false;
        this.nearPackedIce = false;
        this.nearExactEndRod = false;
        this.nearHoneyBlock = false;

        if (this.nearbyMaterials != null) {
            for (Object raw : this.nearbyMaterials) {
                if (!(raw instanceof Material)) {
                    continue;
                }
                Material material = (Material) raw;
                this.nearSweetBerry |= BoundingBoxUtil.isSweetBerry(material);
                this.nearCobweb |= BoundingBoxUtil.isCobweb(material);
                this.nearIce |= BoundingBoxUtil.isIce(material);
                this.nearSoulSand |= BoundingBoxUtil.isSoulSand(material);
                this.nearSoulSandOrSoil |= BoundingBoxUtil.isSoulSand(material);
                this.nearBubbleColumn |= BoundingBoxUtil.isBubbleColumn(material);
                this.nearWall |= BoundingBoxUtil.isWall(material);
                this.onLilyPad |= BoundingBoxUtil.isLilyPad(material);
                this.nearSlime |= BoundingBoxUtil.isSlime(material);
                this.nearPiston |= BoundingBoxUtil.isPiston(material);
                this.nearTrapdoor |= BoundingBoxUtil.isTrapdoor(material);
                this.nearHoney |= BoundingBoxUtil.isHoney(material);
                this.nearBed |= BoundingBoxUtil.isBed(material);
                this.nearCauldron |= BoundingBoxUtil.isCauldron(material);
                this.nearShulker |= BoundingBoxUtil.isShulker(material);
                this.touchingSolid |= BoundingBoxUtil.isSolid(material);
                this.nearFenceGate |= BoundingBoxUtil.isFenceGate(material);
                this.nearSnow |= BoundingBoxUtil.isSnow(material);
                this.nearAnvil |= BoundingBoxUtil.isAnvil(material);
                this.nearCampfireBlock |= BoundingBoxUtil.isCampfire(material);
                this.nearCarpet |= BoundingBoxUtil.isCarpet(material);
                this.nearHopper |= BoundingBoxUtil.isHopper(material);
                this.nearDoor |= BoundingBoxUtil.isDoor(material);
                this.nearLilyPad |= BoundingBoxUtil.isLilyPad(material);
                this.nearEndRod |= BoundingBoxUtil.isEndRod(material);
                this.nearChest |= BoundingBoxUtil.isChest(material);
                this.nearFlowerPot |= BoundingBoxUtil.isFlowerPot(material);
                this.nearBedOrBedrock |= BoundingBoxUtil.isBedOrBedrock(material);
                this.nearFarmland |= BoundingBoxUtil.isFarmland(material);
                this.nearSkull |= BoundingBoxUtil.isSkull(material);
                this.nearPressurePlate |= BoundingBoxUtil.isPressurePlate(material);
                this.nearCake |= BoundingBoxUtil.isCake(material);
                this.nearSlab |= BoundingBoxUtil.isSlab(material);
                this.nearStair |= BoundingBoxUtil.isStair(material);
                this.nearSign |= BoundingBoxUtil.isSign(material);
                this.nearClimbable |= BoundingBoxUtil.isClimbable(material);
                this.nearBamboo |= BoundingBoxUtil.isBamboo(material);
                this.nearScaffolding |= BoundingBoxUtil.isScaffolding(material);
                this.nearFence |= BoundingBoxUtil.isFence(material);
                this.inPowderSnow |= BoundingBoxUtil.isPowderSnow(material);
                this.nearDripstone |= BoundingBoxUtil.isDripstone(material);
                this.nearChain |= BoundingBoxUtil.isChain(material);
                this.nearTurtleEgg |= BoundingBoxUtil.isTurtleEgg(material);
                this.nearDripleaf |= BoundingBoxUtil.isDripleaf(material);
                this.nearHeavyCore |= BoundingBoxUtil.isHeavyCore(material);
                this.nearDriedGhast |= BoundingBoxUtil.isDriedGhast(material);
                this.nearBlueIce |= BoundingBoxUtil.isBlueIce(material);
                this.nearExactScaffolding |= BoundingBoxUtil.isExactScaffolding(material);
                this.nearSweetBerryBush |= BoundingBoxUtil.isSweetBerryBush(material);
                this.nearFrostedIce |= BoundingBoxUtil.isFrostedIce(material);
                this.nearExactBubbleColumn |= BoundingBoxUtil.isBubbleColumn(material);
                this.nearBell |= BoundingBoxUtil.isBell(material);
                this.nearLectern |= BoundingBoxUtil.isLectern(material);
                this.nearConduit |= BoundingBoxUtil.isConduit(material);
                this.nearKelp |= BoundingBoxUtil.isKelp(material);
                this.nearSeagrass |= BoundingBoxUtil.isSeagrass(material);
                this.nearPackedIce |= BoundingBoxUtil.isPackedIce(material);
                this.nearExactEndRod |= BoundingBoxUtil.isEndRod(material);
                this.nearHoneyBlock |= BoundingBoxUtil.isHoneyBlock(material);
            }
        }

        this.collidingDragon = false;
        if (this.nearbyEntities != null) {
            for (Object raw : this.nearbyEntities) {
                if (!(raw instanceof Entity)) {
                    continue;
                }
                Entity entity = (Entity) raw;
                this.nearBoat |= entity instanceof Boat;
                this.nearHappyGhast |= "HAPPY_GHAST".equals(entity.getType().toString());
                if (entity instanceof LivingEntity) {
                    this.collidingLivingEntity = true;
                }
                if (entity instanceof EnderDragon || entity instanceof EnderDragonPart) {
                    this.collidingDragon = true;
                }
            }
        }

        this.nearClimbableColumn = CollisionUtil.isClimbableInColumn(this.playerData);
        this.cobwebInColumn = CollisionUtil.isCobwebInColumn(this.playerData);
        this.bubbleColumnInColumn = CollisionUtil.isInBubbleColumn(this.playerData);
        this.liquidInColumn = CollisionUtil.isLiquidInColumn(this.playerData);

        if (!this.unmoved
            && !this.nearSlime
            && !this.nearSoulSand
            && !this.nearHoney
            && !this.nearCampfireBlock
            && !this.nearTrapdoor
            && !this.nearFlowerPot
            && !this.nearLilyPad
            && !this.onLilyPad) {
            return;
        }
        this.ghostBlockAirTicks = 0;
    }

    public void handleVehicleMove(WrapperPlayClientVehicleMove packet, long timestamp) {
        ++this.vehicleMoveTicks;
        this.previousVehicleEntity = this.vehicleEntity;
        if (this.playerData.getPlayer() != null) {
            this.vehicleEntity = this.playerData.getPlayer().getVehicle();
        }
        if (this.previousVehicleEntity != this.vehicleEntity) {
            this.vehicleMoveTicks = 0;
        }
        this.vehicleX = packet.getPosition().getX();
        this.vehicleY = packet.getPosition().getY();
        this.vehicleZ = packet.getPosition().getZ();
        this.lastVehicleDeltaX = this.vehicleDeltaX;
        this.lastVehicleDeltaY = this.vehicleDeltaY;
        this.lastVehicleDeltaZ = this.vehicleDeltaZ;
        this.vehicleDeltaX = this.vehicleX - this.lastVehicleX;
        this.vehicleDeltaY = this.vehicleY - this.lastVehicleY;
        this.vehicleDeltaZ = this.vehicleZ - this.lastVehicleZ;
        this.vehicleDeltaXZ = TrigUtil.hypot2(this.vehicleDeltaX, this.vehicleDeltaZ);

        if (this.vehicleNearbyMaterials != null) {
            this.vehicleNearBed = false;
            this.onSeaPickle = false;
            this.vehicleNearSweetBerry = false;
            this.vehicleNearEndRod = false;
            this.vehicleNearIce = false;
            this.vehicleNearSolid = false;
            for (Object raw : this.vehicleNearbyMaterials) {
                if (!(raw instanceof Material)) {
                    continue;
                }
                Material material = (Material) raw;
                this.vehicleNearSolid |= BoundingBoxUtil.isSolid(material);
                this.vehicleNearIce |= BoundingBoxUtil.isIce(material);
                this.vehicleNearEndRod |= BoundingBoxUtil.isEndRod(material);
                this.vehicleNearSweetBerry |= BoundingBoxUtil.isSweetBerry(material);
                this.onSeaPickle |= BoundingBoxUtil.isSeaPickle(material);
                this.vehicleNearBed |= BoundingBoxUtil.isBedOrBedrock(material);
            }
            this.vehicleAllSolid = CollectionPredicates.matches(this.vehicleNearbyMaterials, m -> isSolidMaterial((Material) m));
        }
        this.nearbyLivingEntity = false;
        this.collidingDragon = false;
        if (!VulcanBootstrap.INSTANCE.isFolia() && this.nearbyEntities != null) {
            for (Object raw : this.nearbyEntities) {
                if (!(raw instanceof Entity)) {
                    continue;
                }
                Entity entity = (Entity) raw;
                if (entity instanceof Boat) {
                    this.nearBoat = true;
                }
                if (this.vehicleEntity != null
                    && (this.vehicleEntity.getPassengers().contains(entity)
                        || this.vehicleEntity.getEntityId() == entity.getEntityId())) {
                    continue;
                }
                if (entity instanceof LivingEntity) {
                    this.nearbyLivingEntity = true;
                }
                if (entity instanceof EnderDragon || entity instanceof EnderDragonPart) {
                    this.collidingDragon = true;
                }
            }
        }
        if (this.vehicleNearSolid) {
            this.ticksSinceVehicleSolid = 0;
        } else {
            ++this.ticksSinceVehicleSolid;
        }
        if (this.vehicleNearEndRod) {
            this.ticksSinceVehicleEndRod = 0;
        } else {
            ++this.ticksSinceVehicleEndRod;
        }
        if (this.vehicleNearBed) {
            this.ticksSinceVehicleBed = 0;
        } else {
            ++this.ticksSinceVehicleBed;
        }
        if (this.vehicleNearIce) {
            this.ticksSinceVehicleIce = 0;
        } else {
            ++this.ticksSinceVehicleIce;
        }
        if (this.vehicleNearSweetBerry) {
            this.ticksSinceVehicleSweetBerry = 0;
        } else {
            ++this.ticksSinceVehicleSweetBerry;
        }
        if (this.onSeaPickle) {
            this.seaPickleTicks = 0;
        } else {
            ++this.seaPickleTicks;
        }
        if (this.vehicleAllSolid) {
            this.ticksSinceVehicleAllSolid = 0;
        } else {
            ++this.ticksSinceVehicleAllSolid;
        }
        if (this.collidingDragon) {
            this.ticksSinceVehicleAllSolid = 0;
        }
        this.lastVehicleX = this.vehicleX;
        this.lastVehicleY = this.vehicleY;
        this.lastVehicleZ = this.vehicleZ;
    }

    public void updateGroundAndAirTicks() {
        ++this.ticksSinceGhostBlock;
        ++this.ticksSinceDigStart;
        ++this.highWalkSpeedTicks;
        ++this.ticksSinceFail;
        if (this.clientOnGround) {
            this.airTicks = 0;
            ++this.groundTicks;
        } else {
            this.groundTicks = 0;
            ++this.airTicks;
        }
    }

    public void updateCollisionTickCounters() {
        ++this.ticksSinceBlockPlace;
        if (this.nearbySolid) {
            this.ghostBlockAirTicks = 0;
        } else {
            ++this.ghostBlockAirTicks;
        }
        if (this.blockGridAlignedY) {
            this.alignedYTicks = 0;
        } else {
            ++this.alignedYTicks;
        }
        Material below = this.standingMaterial != null ? this.standingMaterial : this.blockBelow;
        this.blockBelowSolidOrIce = below != null && (below.isSolid() || BoundingBoxUtil.isIce(below));
        if (this.blockBelowSolidOrIce) {
            this.ticksSinceStandingOnSolidOrIce = 0;
        } else {
            ++this.ticksSinceStandingOnSolidOrIce;
        }
        if (this.abilityFlySpeed > 0.11f) {
            this.highFlySpeedTicks = 0;
        }
        ++this.highFlySpeedTicks;
        if (this.touchingSolid) {
            this.ticksSinceTouchingSolid = 0;
        } else {
            ++this.ticksSinceTouchingSolid;
        }
        if (this.nearPackedIce) {
            this.packedIceTicks = 0;
        } else {
            ++this.packedIceTicks;
        }
        ++this.ticksSinceFirework;
        if (this.nearBamboo) {
            this.ticksSinceBamboo = 0;
        } else {
            ++this.ticksSinceBamboo;
        }
        if (this.nearSkull) {
            this.ticksSinceSkull = 0;
        } else {
            ++this.ticksSinceSkull;
        }
        if (this.nearBoat) {
            this.ticksSinceBoat = 0;
        } else {
            ++this.ticksSinceBoat;
        }
        if (this.unmoved) {
            this.ticksSinceMoving = 0;
        } else {
            ++this.ticksSinceMoving;
        }
        if (this.playerData.getStatusTracker().isSneaking()) {
            ++this.sneakTicks;
        } else {
            this.sneakTicks = 0;
        }
        if (!ServerCompatibility.isServer1_8OrNewer()) {
            return;
        }
        if (this.playerData.getPlayer() != null && this.playerData.getPlayer().getGameMode() == GameMode.SPECTATOR) {
            this.ticksSinceNotSpectator = 0;
        } else {
            ++this.ticksSinceNotSpectator;
        }
        if (this.nearBedOrBedrock) {
            this.ticksSinceBedOrBedrock = 0;
        } else {
            ++this.ticksSinceBedOrBedrock;
        }
        if (this.nearFarmland) {
            this.farmlandTicks = 0;
        } else {
            ++this.farmlandTicks;
        }
        if (this.nearCarpet) {
            this.ticksSinceCarpet = 0;
        } else {
            ++this.ticksSinceCarpet;
        }
        if (this.nearHappyGhast) {
            this.ticksSinceHappyGhast = 0;
        } else {
            ++this.ticksSinceHappyGhast;
        }
        if (this.nearEndRod) {
            this.ticksSinceEndRod = 0;
        } else {
            ++this.ticksSinceEndRod;
        }
        if (this.collidingLivingEntity) {
            this.entityCollisionTicks = 0;
        } else {
            ++this.entityCollisionTicks;
        }
        if (this.playerData.getStatusTracker().isOnGround()) {
            this.ticksAirborne = 0;
        } else {
            ++this.ticksAirborne;
        }
        if (this.nearSoulSand) {
            this.soulSandTicks = 0;
        } else {
            ++this.soulSandTicks;
        }
        if (this.nearFlowerPot) {
            this.flowerPotTicks = 0;
        } else {
            ++this.flowerPotTicks;
        }
        if (this.nearCarpet) {
            this.carpetTicks = 0;
        } else {
            ++this.carpetTicks;
        }
        if (this.nearSeagrass) {
            this.seagrassTicks = 0;
        } else {
            ++this.seagrassTicks;
        }
        if (this.nearCollidingHorizontally) {
            this.ticksSinceHorizontalCollision = 0;
        } else {
            ++this.ticksSinceHorizontalCollision;
        }
        if (this.nearFrostedIce) {
            this.ticksSinceFrostedIce = 0;
        } else {
            ++this.ticksSinceFrostedIce;
        }
        if (this.nearCobweb) {
            this.ticksSinceCobweb = 0;
        } else {
            ++this.ticksSinceCobweb;
        }
        if (this.feetSampleSolid) {
            this.ticksSinceSolidFeet = 0;
        } else {
            ++this.ticksSinceSolidFeet;
        }
        if (this.nearSlime) {
            this.ticksSinceSlime = 0;
        } else {
            ++this.ticksSinceSlime;
        }
        if (this.nearWall) {
            this.ticksSinceWall = 0;
        } else {
            ++this.ticksSinceWall;
        }
        if (this.nearBubbleColumn) {
            this.ticksSinceNearbyBubbleColumn = 0;
        } else {
            ++this.ticksSinceNearbyBubbleColumn;
        }
        if (!this.unmoved) {
            this.ticksStationary = 0;
        } else {
            ++this.ticksStationary;
        }
        if (this.nearExactScaffolding) {
            this.ticksSinceExactScaffolding = 0;
        } else {
            ++this.ticksSinceExactScaffolding;
        }
        if (!this.liquidInColumn && !this.nearIce && !this.bubbleColumnInColumn) {
            this.liquidTicks = 0;
        } else {
            ++this.liquidTicks;
        }
        if (this.nearClimbableColumn) {
            this.ticksSinceClimbableColumn = 0;
        } else {
            ++this.ticksSinceClimbableColumn;
        }
        if (this.nearShulker) {
            this.ticksSinceShulker = 0;
        } else {
            ++this.ticksSinceShulker;
        }
        if (this.nearbyHeadSurroundSamples != null
            && CollectionPredicates.exists(this.nearbyHeadSurroundSamples, raw -> raw instanceof Material && BoundingBoxUtil.isSweetBerry((Material) raw))) {
            this.ticksSinceNearbySweetBerry = 0;
        } else {
            ++this.ticksSinceNearbySweetBerry;
        }
        if (this.nearbyHeadSurroundSamples != null
            && CollectionPredicates.exists(this.nearbyHeadSurroundSamples, raw -> raw instanceof Material && BoundingBoxUtil.isEndRod((Material) raw))) {
            this.ticksSinceNearbyEndRod = 0;
        } else {
            ++this.ticksSinceNearbyEndRod;
        }
        if (this.nearbyHeadSurroundSamples != null
            && CollectionPredicates.exists(this.nearbyHeadSurroundSamples, raw -> raw instanceof Material && BoundingBoxUtil.isCarpet((Material) raw))) {
            this.ticksSinceNearbyCarpet = 0;
        } else {
            ++this.ticksSinceNearbyCarpet;
        }
        this.feetOnIce = this.nearbyFeetSamples != null
            && CollectionPredicates.matches(this.nearbyFeetSamples, raw -> raw instanceof Material && BoundingBoxUtil.isIce((Material) raw));
        if (this.feetOnIce) {
            this.ticksSinceFeetIce = 0;
        } else {
            ++this.ticksSinceFeetIce;
        }
        Player player = this.playerData.getPlayer();
        if (player != null && player.isInsideVehicle()) {
            this.ticksSinceInsideVehicle = 0;
        } else {
            ++this.ticksSinceInsideVehicle;
        }
        ++this.vehicleTicks;
        if (player != null && player.getAllowFlight()) {
            this.ticksSinceFlightAllowed = 0;
        } else {
            ++this.ticksSinceFlightAllowed;
        }
        if (this.playerData.getStatusTracker().isSprinting()) {
            this.ticksSinceSprinting = 0;
        } else {
            ++this.ticksSinceSprinting;
        }
        if (this.playerData.getStatusTracker().isOnGround()) {
            this.ticksSinceOnGround = 0;
        } else {
            ++this.ticksSinceOnGround;
        }
        if (this.playerData.getStatusTracker().isCollisionExemptFlag()) {
            this.ticksSinceCollisionExempt = 0;
        } else {
            ++this.ticksSinceCollisionExempt;
        }
        this.recordedDeltaXZ = this.deltaXZ;
        StatusTracker statusTracker = this.playerData.getStatusTracker();
        if (this.nearSoulSandOrSoil
            && statusTracker.getBoots() != null
            && statusTracker.getBoots().getEnchantmentLevel(Enchantment.SOUL_SPEED) > 0) {
            this.soulSpeedTicks = 0;
        } else {
            ++this.soulSpeedTicks;
        }
        if (statusTracker.isElytraOrGliding()) {
            this.elytraTicks = 0;
        } else {
            ++this.elytraTicks;
        }
        if (CollisionUtil.isRiptiding(this.playerData) || CollisionUtil.isHoldingRiptide(this.playerData)) {
            this.ticksSinceRiptide = 0;
        } else {
            ++this.ticksSinceRiptide;
        }
        if (this.nearPressurePlate) {
            this.pressurePlateTicks = 0;
        } else {
            ++this.pressurePlateTicks;
        }
        if (statusTracker.isLevitating()) {
            this.levitationTicks = 0;
        } else {
            ++this.levitationTicks;
        }
        if (CollisionUtil.isHoldingRiptide(this.playerData)) {
            this.ticksSinceHoldingRiptide = 0;
        } else {
            ++this.ticksSinceHoldingRiptide;
        }
        if (this.nearBubbleColumn) {
            this.bubbleColumnTicks = 0;
        } else {
            ++this.bubbleColumnTicks;
        }
        if (CollisionUtil.isHoldingRiptide(this.playerData)) {
            this.ticksSinceRiptideWeapon = 0;
        } else {
            ++this.ticksSinceRiptideWeapon;
        }
        if (ServerCompatibility.isModernProtocol() && player != null && player.isGliding()) {
            ++this.ticksGliding;
        } else {
            this.ticksGliding = 0;
        }
    }

    public void handleReleaseUseItem(WrapperPlayClientPlayerDigging packet) {
        if (this.playerData.getPlayer() == null) {
            return;
        }
        if (packet.getAction() != DiggingAction.RELEASE_USE_ITEM) {
            return;
        }
        boolean recentlyInLiquid = this.liquidTicks < 30 || this.waterloggedTicks < 30;
        boolean raining = this.playerData.getPlayer().getPlayerWeather() == WeatherType.DOWNFALL
            || this.playerData.getPlayer().getWorld().hasStorm();
        boolean recentlyInWater = this.liquidTicks < 20;
        if (!recentlyInLiquid && !raining && !recentlyInWater) {
            return;
        }
        if (this.ticksSinceRiptideWeapon >= 20) {
            return;
        }
        this.ticksSinceRiptide = 0;
    }

    public boolean isStandingOnScaffold() {
        if (ServerCompatibility.isModernProtocol()) {
            return BoundingBoxUtil.isScaffoldMaterial(this.standingMaterial)
                || BoundingBoxUtil.isScaffoldMaterial(this.blockBelow);
        }
        return BoundingBoxUtil.isScaffoldMaterial(this.blockBelow)
            || BoundingBoxUtil.isScaffoldMaterial(this.blockTwoBelow);
    }

    public boolean isColliding() {
        if (CollectionPredicates.matches(this.nearbyFeetSamples, this::isSolidMaterial)) {
            return true;
        }
        if (ServerCompatibility.isModernProtocol()) {
            return CollectionPredicates.matches(this.nearbyColumnExtra, this::isSolidMaterial);
        }
        return false;
    }

    public double getAbsDeltaXZChange() {
        return Math.abs(this.deltaXZ - this.lastDeltaXZ);
    }

    public void markDigStart() {
        this.lastDigStartTick = VulcanBootstrap.INSTANCE.getKeepAliveRunnable().getCurrentTick();
        this.ticksSinceDigStart = 0;
    }

    public void resetWaterloggedTicks() {
        this.waterloggedTicks = 0;
    }

    private void handleGhostBlockFix() {
        if (this.playerData.getPlayer() == null || !ConfigValues.ghostBlocksFixEnabled) {
            return;
        }
        if (ServerCompatibility.getTicksPerSecond() < ConfigValues.ghostBlocksFixMinimumTps) {
            return;
        }
        if (this.playerData.getPlayer().hasPermission("vulcan.bypass.ghostblock")) {
            return;
        }
        if (ConfigValues.ghostBlocksFixIgnoreLegacyVia
            && this.playerData.getUser().getClientVersion().isOlderThan(ClientVersion.V_1_17)
            && this.locationY < 5.0) {
            return;
        }
        if (this.playerData.getExemptionChecker().isExempt(
            ExemptType.LAGGED_NEAR_GROUND,
            ExemptType.BLOCK_PLACE_FAST,
            ExemptType.NOT_MOVING,
            ExemptType.LAGGED_NEAR_GROUND_MODERN,
            ExemptType.FLIGHT,
            ExemptType.TELEPORT,
            ExemptType.SWIMMING,
            ExemptType.DEATH
        )) {
            return;
        }
        String brand = this.playerData.getClientBrand();
        if (brand == null) {
            brand = "";
        }
        if (ConfigValues.settingsIgnoreGeyserPrefixes
            && (brand.toLowerCase().contains("geyser")
                || this.playerData.getUsername().startsWith(ConfigValues.settingsIgnoreGeyserPrefix))) {
            return;
        }
        boolean inAirLongEnough = this.ghostBlockAirTicks > ConfigValues.ghostBlocksFixTicks
            || this.alignedYTicks >= 1;
        if (!inAirLongEnough) {
            if (this.ghostBlockBuffer > 0.0) {
                this.ghostBlockBuffer -= ConfigValues.ghostBlocksFixBufferDecay;
            }
            return;
        }
        this.ghostBlockBuffer += 1.0;
        if (this.ghostBlockBuffer <= ConfigValues.ghostBlocksFixBufferMax) {
            return;
        }
        VulcanGhostBlockEvent event = new VulcanGhostBlockEvent(this.playerData.getPlayer());
        Bukkit.getPluginManager().callEvent((Event) event);
        if (event.isCancelled()) {
            return;
        }
        this.ticksSinceGhostBlock = 0;
        if (ConfigValues.ghostBlocksFixSetback) {
            Location setback = new Location(
                this.world,
                this.lastServerX,
                this.lastServerY,
                this.lastServerZ,
                this.playerData.getActionTracker().getYaw(),
                this.playerData.getActionTracker().getPitch()
            );
            WrapperPlayServerPlayerPositionAndLook look = new WrapperPlayServerPlayerPositionAndLook(
                setback.getX(),
                setback.getY(),
                setback.getZ(),
                setback.getYaw(),
                setback.getPitch(),
                0,
                69,
                false
            );
            this.playerData.getUser().sendPacket((PacketWrapper) look);
            AsyncScheduler.runSync(() -> this.lambda$handleUnloadedChunk$1(setback));
        }
        if (ConfigValues.ghostBlocksFixMessageEnabled && ConfigValues.ghostBlocksFixMessage != null) {
            this.playerData.getPlayer().sendMessage(ColorUtil.translateColors(ConfigValues.ghostBlocksFixMessage));
        }
        if (ConfigValues.ghostBlocksFixStaffMessageEnabled && ConfigValues.ghostBlocksFixStaffMessage != null) {
            VulcanBootstrap.INSTANCE.getAlertMessageBuilder().sendAlertMessage(
                ConfigValues.ghostBlocksFixStaffMessage
                    .replaceAll("%x%", Integer.toString(this.blockX))
                    .replaceAll("%y%", Integer.toString(this.blockY))
                    .replaceAll("%z%", Integer.toString(this.blockZ))
                    .replaceAll("%ticks%", Integer.toString(this.ghostBlockAirTicks))
                    .replaceAll("%world%", this.playerData.getPlayer().getWorld().getName())
                    .replaceAll("%player%", this.playerData.getUsername())
            );
        }
        if (ConfigValues.ghostBlocksFixPrintToConsole && ConfigValues.ghostBlocksFixConsoleMessage != null) {
            VulcanBootstrap.INSTANCE.getVulcanPlugin().getLogger().log(
                Level.INFO,
                ColorUtil.translateColors(
                    ConfigValues.ghostBlocksFixConsoleMessage
                        .replaceAll("%ticks%", Integer.toString(this.ghostBlockAirTicks))
                        .replaceAll("%player%", this.playerData.getUsername())
                        .replaceAll("%world%", this.playerData.getPlayer().getWorld().getName())
                        .replaceAll("%z%", Integer.toString(this.blockZ))
                        .replaceAll("%y%", Integer.toString(this.blockY))
                        .replaceAll("%x%", Integer.toString(this.blockX))
                )
            );
        }
        this.ghostBlockAirTicks = 0;
        this.ghostBlockBuffer = 0.0;
    }

    public void handleAbilities(WrapperPlayServerPlayerAbilities packet, long timestamp) {
        this.playerData.getPacketHandler().runOrQueueOnSummary(() -> this.lambda$handleAbilities$0(packet));
    }

    public boolean isStandingOnSolidColumn() {
        return this.standingOnSolidColumnBlock();
    }

    public boolean isStandingOnScaffoldColumn() {
        return this.standingOnScaffoldColumnBlock();
    }

    private List currentColumnMaterials() {
        if (ServerCompatibility.isModernProtocol()) {
            return this.nearbyColumnMaterials;
        }
        return this.nearbyMaterials;
    }

    private boolean standingOnSolidColumnBlock() {
        List column = this.currentColumnMaterials();
        if (column == null || column.isEmpty() || column.size() <= 16) {
            return true;
        }
        return BoundingBoxUtil.isSolid((Material) column.get(16));
    }

    private boolean standingOnScaffoldColumnBlock() {
        List column = this.currentColumnMaterials();
        if (column == null || column.isEmpty() || column.size() <= 16) {
            return true;
        }
        return BoundingBoxUtil.isScaffoldMaterial((Material) column.get(16));
    }

    public void markGhostBlockFail() {
        if (this.locationY < this.lastServerY && ConfigValues.dontSetbackLowerPositions) {
            return;
        }
        this.ticksSinceFail = 0;
        this.playerData.setInventoryOpen(true);
    }
    public void handleUnloadedChunkSetback() {
        if (!ConfigValues.unloadedChunksSetbackEnabled || this.playerData.getPlayer() == null) {
            return;
        }
        if (this.playerData.getExemptionChecker().isExempt(
            ExemptType.JOINED,
            ExemptType.TELEPORT,
            ExemptType.WORLD_CHANGE,
            ExemptType.LIQUID,
            ExemptType.CHUNK,
            ExemptType.FLIGHT,
            ExemptType.ELYTRA,
            ExemptType.GLIDING,
            ExemptType.SLOW_FALLING
        )) {
            return;
        }
        if (this.clientOnGround || this.deltaY >= -0.09000000357627869) {
            return;
        }
        double gravityError = Math.abs(Math.abs(this.deltaY) - 0.09800000190734881);
        if (gravityError >= 0.005) {
            this.webSlowdown = Math.max(0.0, this.webSlowdown - 0.025);
            return;
        }
        this.webSlowdown += 1.0;
        if (this.webSlowdown <= 0.0) {
            return;
        }
        Location setback = new Location(
            this.world,
            this.lastServerX,
            this.lastServerY,
            this.lastServerZ,
            this.playerData.getActionTracker().getYaw(),
            this.playerData.getActionTracker().getPitch()
        );
        WrapperPlayServerPlayerPositionAndLook look = new WrapperPlayServerPlayerPositionAndLook(
            setback.getX(),
            setback.getY(),
            setback.getZ(),
            setback.getYaw(),
            setback.getPitch(),
            0,
            69,
            false
        );
        this.playerData.getUser().sendPacket((PacketWrapper) look);
        AsyncScheduler.runSync(() -> this.lambda$handleUnloadedChunk$1(setback));
        if (ConfigValues.unloadedChunksMessage != null && !ConfigValues.unloadedChunksMessage.isEmpty()) {
            VulcanBootstrap.INSTANCE.getAlertMessageBuilder().sendAlertMessage(
                ConfigValues.unloadedChunksMessage
                    .replaceAll("%x%", Integer.toString(this.blockX))
                    .replaceAll("%y%", Integer.toString(this.blockY))
                    .replaceAll("%z%", Integer.toString(this.blockZ))
                    .replaceAll("%ticks%", Integer.toString(this.ghostBlockAirTicks))
                    .replaceAll("%world%", this.playerData.getPlayer().getWorld().getName())
                    .replaceAll("%player%", this.playerData.getUsername())
            );
        }
        this.webSlowdown = 0.0;
    }

    public void enqueueRunnable(Runnable runnable) {
        this.pendingRunnables.add(runnable);
    }

    public void runPendingRunnables() {
        this.pendingRunnables.forEach(r -> ((Runnable) r).run());
        this.pendingRunnables.clear();
    }

    public PlayerData getPlayerData() {
        return this.playerData;
    }

    public boolean isNearSweetBerry() {
        return this.nearSweetBerry;
    }

    public boolean isNearSoulSandOrSoil() {
        return this.nearSoulSandOrSoil;
    }

    public boolean isNearBubbleColumn() {
        return this.nearBubbleColumn;
    }

    public boolean isNearWall() {
        return this.nearWall;
    }

    public boolean isNearLilyPad() {
        return this.nearLilyPad;
    }

    public boolean isNearTrapdoor() {
        return this.nearTrapdoor;
    }

    public boolean isNearHoney() {
        return this.nearHoney;
    }

    public boolean isNearBed() {
        return this.nearBed;
    }

    public boolean isNearShulker() {
        return this.nearShulker;
    }

    public boolean isNearCarpet() {
        return this.nearCarpet;
    }


    public boolean isNearExactScaffolding() {
        return this.nearExactScaffolding;
    }

    public boolean isNearSlime() {
        return this.nearSlime;
    }

    public boolean isNearFlowerPot() {
        return this.nearFlowerPot;
    }

    public boolean isNearPiston() {
        return this.nearPiston;
    }

    public boolean isNearCampfire() {
        return this.nearCampfire;
    }

    public boolean isNearSweetBerryBush() {
        return this.nearSweetBerryBush;
    }


    public boolean isOnSeaPickle() {
        return this.onSeaPickle;
    }

    public boolean isNearSnow() {
        return this.nearSnow;
    }

    public boolean isNearAnvil() {
        return this.nearAnvil;
    }

    public boolean isNearExactEndRod() {
        return this.nearExactEndRod;
    }

    public boolean isNearEndRod() {
        return this.nearEndRod;
    }

    public boolean isNearDoor() {
        return this.nearDoor;
    }

    public boolean isNearCauldron() {
        return this.nearCauldron;
    }

    public boolean isNearHopper() {
        return this.nearHopper;
    }

    public boolean isNearFenceGate() {
        return this.nearFenceGate;
    }

    public boolean isOnLilyPad() {
        return this.onLilyPad;
    }

    public boolean isNearClimbableColumn() {
        return this.nearClimbableColumn;
    }

    public boolean isCobwebInColumn() {
        return this.cobwebInColumn;
    }

    public boolean isBubbleColumnInColumn() {
        return this.bubbleColumnInColumn;
    }

    public boolean isLiquidInColumn() {
        return this.liquidInColumn;
    }

    public boolean isHeadSamplesAllSolid() {
        return this.headSamplesAllSolid;
    }

    public boolean isNearKelp() {
        return this.nearKelp;
    }

    public boolean isNearbySolid() {
        return this.nearbySolid;
    }

    public boolean isNearbyHeadSolid() {
        return this.nearbyHeadSolid;
    }

    public boolean isFeetSampleSolid() {
        return this.feetSampleSolid;
    }

    public boolean isClientOnGround() {
        return this.clientOnGround;
    }


    public boolean isBlockGridAlignedY() {
        return this.blockGridAlignedY;
    }

    public boolean isFeetOnIce() {
        return this.feetOnIce;
    }

    public boolean isUnmoved() {
        return this.unmoved;
    }

    public boolean isNearBoat() {
        return this.nearBoat;
    }

    public boolean isNearBell() {
        return this.nearBell;
    }

    public boolean isNearBedOrBedrock() {
        return this.nearBedOrBedrock;
    }

    public boolean isNearSoulSand() {
        return this.nearSoulSand;
    }

    public boolean isNearLectern() {
        return this.nearLectern;
    }


    public boolean isNearSeaPickle() {
        return this.nearSeaPickle;
    }

    public boolean isTouchingSolid() {
        return this.touchingSolid;
    }

    public boolean isNearConduit() {
        return this.nearConduit;
    }

    public boolean isNearCollidingHorizontally() {
        return this.nearCollidingHorizontally;
    }

    public boolean isNearCampfireBlock() {
        return this.nearCampfireBlock;
    }

    public boolean isNearSolid() {
        return this.nearSolid;
    }

    public boolean isNearChest() {
        return this.nearChest;
    }

    public boolean isVehicleNearSolid() {
        return this.vehicleNearSolid;
    }

    public boolean isVehicleNearIce() {
        return this.vehicleNearIce;
    }

    public boolean isVehicleNearSweetBerry() {
        return this.vehicleNearSweetBerry;
    }

    public boolean isVehicleAllSolid() {
        return this.vehicleAllSolid;
    }

    public boolean hasPositionDelta() {
        return this.hasPositionDelta;
    }

    public boolean isNearFrostedIce() {
        return this.nearFrostedIce;
    }

    public boolean isNearExactBubbleColumn() {
        return this.nearExactBubbleColumn;
    }

    public boolean isNearFarmland() {
        return this.nearFarmland;
    }

    public boolean isLastClientOnGround() {
        return this.lastClientOnGround;
    }

    public boolean isFlightAllowed() {
        return this.flightAllowed;
    }

    public boolean isNearHoneyBlock() {
        return this.nearHoneyBlock;
    }

    public boolean isNearSkull() {
        return this.nearSkull;
    }

    public boolean isSolidMaterial(Object raw) {
        return raw instanceof Material && BoundingBoxUtil.isSolid((Material) raw);
    }

    public boolean isVehicleNearEndRod() {
        return this.vehicleNearEndRod;
    }

    public boolean isNearPressurePlate() {
        return this.nearPressurePlate;
    }

    public boolean isVehicleNearBed() {
        return this.vehicleNearBed;
    }

    public boolean isNearFence() {
        return this.nearFence;
    }

    public boolean isNearPowderSnow() {
        return this.nearPowderSnow;
    }

    public boolean isCollidingLivingEntity() {
        return this.collidingLivingEntity;
    }

    public boolean isPreviousPreviousClientOnGround() {
        return this.previousPreviousClientOnGround;
    }

    public boolean isNearbyLivingEntity() {
        return this.nearbyLivingEntity;
    }

    public boolean isNearCake() {
        return this.nearCake;
    }

    public boolean isNearSlab() {
        return this.nearSlab;
    }

    public boolean isNearWorldBorder() {
        return this.nearWorldBorder;
    }

    public boolean isNearStair() {
        return this.nearStair;
    }

    public boolean isJumpCollisionExempt() {
        return this.jumpCollisionExempt;
    }

    public boolean isNearSign() {
        return this.nearSign;
    }

    public boolean isNearClimbable() {
        return this.nearClimbable;
    }

    public boolean isNearBamboo() {
        return this.nearBamboo;
    }

    public boolean isBreakingBlock() {
        return this.breakingBlock;
    }

    public boolean isNearScaffolding() {
        return this.nearScaffolding;
    }

    public boolean isInPowderSnow() {
        return this.inPowderSnow;
    }

    public boolean isBlockBelowSolidOrIce() {
        return this.blockBelowSolidOrIce;
    }

    public boolean isCarpetInHeadSample() {
        return this.carpetInHeadSample;
    }

    public boolean isNearDripleaf() {
        return this.nearDripleaf;
    }

    public boolean isNearDripstone() {
        return this.nearDripstone;
    }

    public boolean isNearChain() {
        return this.nearChain;
    }

    public boolean isNearTurtleEgg() {
        return this.nearTurtleEgg;
    }

    public boolean isNearHeavyCore() {
        return this.nearHeavyCore;
    }

    public boolean isPreviousPreviousPositionChanged() {
        return this.previousPreviousPositionChanged;
    }

    public boolean isNearHappyGhast() {
        return this.nearHappyGhast;
    }

    public boolean isCollidingDragon() {
        return this.collidingDragon;
    }

    public boolean isNearDriedGhast() {
        return this.nearDriedGhast;
    }

    public boolean isNearBlueIce() {
        return this.nearBlueIce;
    }

    public Location getLastLocation() {
        return this.lastLocation;
    }

    public Location getCurrentLocation() {
        return this.currentLocation;
    }

    public Map getNearbyEntityScratch() {
        return this.nearbyEntityScratch;
    }

    public List getNearbyEntities() {
        return this.nearbyEntities;
    }

    public Material getBlockTwoBelow() {
        return this.blockTwoBelow;
    }

    public Material getBlockThreeBelow() {
        return this.blockThreeBelow;
    }

    public List getNearbyFeetSamples() {
        return this.nearbyFeetSamples;
    }

    public List getNearbyHeadSamples() {
        return this.nearbyHeadSamples;
    }

    public List getNearbyHeadSurroundSamples() {
        return this.nearbyHeadSurroundSamples;
    }

    public List getNearbyFeetSurroundSamples() {
        return this.nearbyFeetSurroundSamples;
    }

    public List getNearbyColumnMaterials() {
        return this.nearbyColumnMaterials;
    }

    public List getVehicleNearbyMaterials() {
        return this.vehicleNearbyMaterials;
    }

    public List getNearbyColumnExtra() {
        return this.nearbyColumnExtra;
    }

    public Material getStandingMaterial() {
        return this.standingMaterial;
    }

    public Material getLastHeldMaterial() {
        return this.lastHeldMaterial;
    }

    public Material getAlternateHeldMaterial() {
        return this.alternateHeldMaterial;
    }

    public double getLocationX() {
        return this.locationX;
    }

    public double getLocationY() {
        return this.locationY;
    }

    public double getLocationZ() {
        return this.locationZ;
    }

    public double getDeltaY() {
        return this.deltaY;
    }

    public double getDeltaXZ() {
        return this.deltaXZ;
    }

    public double getHypotDelta() {
        return this.hypotDelta;
    }

    public double getLastDeltaX() {
        return this.lastDeltaX;
    }

    public double getLastDeltaZ() {
        return this.lastDeltaZ;
    }

    public double getLastDeltaY() {
        return this.lastDeltaY;
    }

    public double getLastDeltaXZ() {
        return this.lastDeltaXZ;
    }

    public double getVehicleX() {
        return this.vehicleX;
    }

    public double getVehicleY() {
        return this.vehicleY;
    }

    public double getVehicleZ() {
        return this.vehicleZ;
    }


    public double getLastVehicleX() {
        return this.lastVehicleX;
    }


    public double getLastVehicleY() {
        return this.lastVehicleY;
    }


    public double getLastVehicleZ() {
        return this.lastVehicleZ;
    }


    public double getLastVehicleDeltaX() {
        return this.lastVehicleDeltaX;
    }


    public double getLastVehicleDeltaY() {
        return this.lastVehicleDeltaY;
    }


    public double getLastVehicleDeltaZ() {
        return this.lastVehicleDeltaZ;
    }


    public double getVehicleDeltaX() {
        return this.vehicleDeltaX;
    }


    public double getVehicleDeltaY() {
        return this.vehicleDeltaY;
    }

    public double getVehicleDeltaZ() {
        return this.vehicleDeltaZ;
    }


    public double getVehicleDeltaXZ() {
        return this.vehicleDeltaXZ;
    }

    public double getPreviousDeltaY() {
        return this.previousDeltaY;
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

    public double getGhostBlockBuffer() {
        return this.ghostBlockBuffer;
    }




    public double getLastPreviousX() {
        return this.lastPreviousX;
    }

    public double getLastPreviousY() {
        return this.lastPreviousY;
    }

    public double getLastPreviousZ() {
        return this.lastPreviousZ;
    }

    public double getLastLocationX() {
        return this.lastLocationX;
    }

    public double getLastLocationY() {
        return this.lastLocationY;
    }

    public double getLastLocationZ() {
        return this.lastLocationZ;
    }

    public double getWebSlowdown() {
        return this.webSlowdown;
    }

    public double getRecordedDeltaXZ() {
        return this.recordedDeltaXZ;
    }

    public int getVehicleTicks() {
        return this.vehicleTicks;
    }

    public int getTicksSinceFlightAllowed() {
        return this.ticksSinceFlightAllowed;
    }

    public int getTicksSinceSlime() {
        return this.ticksSinceSlime;
    }

    public int getAirTicks() {
        return this.airTicks;
    }

    public int getGroundTicks() {
        return this.groundTicks;
    }


    public int getTicksSinceOnGround() {
        return this.ticksSinceOnGround;
    }

    public int getRiptideTicks() {
        return this.riptideTicks;
    }

    public int getTicksSinceCollisionExempt() {
        return this.ticksSinceCollisionExempt;
    }

    public int getTicksSinceSolidFeet() {
        return this.ticksSinceSolidFeet;
    }

    public int getTicksSinceHoldingRiptide() {
        return this.ticksSinceHoldingRiptide;
    }

    public int getLevitationTicks() {
        return this.levitationTicks;
    }

    public int getBubbleColumnTicks() {
        return this.bubbleColumnTicks;
    }

    public int getTicksGliding() {
        return this.ticksGliding;
    }

    public int getTicksSinceSprinting() {
        return this.ticksSinceSprinting;
    }

    public int getGhostBlockAirTicks() {
        return this.ghostBlockAirTicks;
    }

    public int getAlignedYTicks() {
        return this.alignedYTicks;
    }

    public int getTicksSinceWall() {
        return this.ticksSinceWall;
    }

    public int getLiquidTicks() {
        return this.liquidTicks;
    }

    public int getTicksSinceClimbableColumn() {
        return this.ticksSinceClimbableColumn;
    }

    public int getTicksAirborne() {
        return this.ticksAirborne;
    }


    public int getPackedIceTicks() {
        return this.packedIceTicks;
    }

    public int getPressurePlateTicks() {
        return this.pressurePlateTicks;
    }

    public int getSoulSandTicks() {
        return this.soulSandTicks;
    }

    public int getTicksSinceCobweb() {
        return this.ticksSinceCobweb;
    }

    public int getSoulSpeedTicks() {
        return this.soulSpeedTicks;
    }

    public int getTicksSinceBedOrBedrock() {
        return this.ticksSinceBedOrBedrock;
    }

    public int getLastDigStartTick() {
        return this.lastDigStartTick;
    }

    public int getVehicleMoveTicks() {
        return this.vehicleMoveTicks;
    }

    public int getTicksSinceBlockPlace() {
        return this.ticksSinceBlockPlace;
    }

    public int getTicksSinceTouchingSolid() {
        return this.ticksSinceTouchingSolid;
    }

    public int getTicksSinceEndRod() {
        return this.ticksSinceEndRod;
    }

    public int getEndRodTicks() {
        return this.endRodTicks;
    }

    public int getSneakTicks() {
        return this.sneakTicks;
    }

    public int getTicksSinceFirework() {
        return this.ticksSinceFirework;
    }

    public int getTicksSinceBamboo() {
        return this.ticksSinceBamboo;
    }

    public int getTicksSinceNearbySweetBerry() {
        return this.ticksSinceNearbySweetBerry;
    }

    public int getTicksSinceNearbyEndRod() {
        return this.ticksSinceNearbyEndRod;
    }

    public int getTicksSinceVehicleSolid() {
        return this.ticksSinceVehicleSolid;
    }

    public int getTicksSinceVehicleIce() {
        return this.ticksSinceVehicleIce;
    }

    public int getTicksSinceVehicleSweetBerry() {
        return this.ticksSinceVehicleSweetBerry;
    }

    public int getSeaPickleTicks() {
        return this.seaPickleTicks;
    }


    public int getBlockX() {
        return this.blockX;
    }

    public int getBlockY() {
        return this.blockY;
    }

    public int getBlockZ() {
        return this.blockZ;
    }

    public int getTicksSinceFrostedIce() {
        return this.ticksSinceFrostedIce;
    }

    public int getTicksSinceExactScaffolding() {
        return this.ticksSinceExactScaffolding;
    }

    public int getTicksSinceGhostBlock() {
        return this.ticksSinceGhostBlock;
    }

    public int getLastAbilitiesTick() {
        return this.lastAbilitiesTick;
    }

    public int getTicksSinceVehicleAllSolid() {
        return this.ticksSinceVehicleAllSolid;
    }

    public int getTicksStationary() {
        return this.ticksStationary;
    }

    public int getElytraTicks() {
        return this.elytraTicks;
    }

    public int getTicksSinceSkull() {
        return this.ticksSinceSkull;
    }

    public int getFarmlandTicks() {
        return this.farmlandTicks;
    }

    public int getTicksSinceShulker() {
        return this.ticksSinceShulker;
    }

    public int getTicksSinceCarpet() {
        return this.ticksSinceCarpet;
    }

    public int getEntityCollisionTicks() {
        return this.entityCollisionTicks;
    }

    public int getTicksSinceVehicleEndRod() {
        return this.ticksSinceVehicleEndRod;
    }

    public int getTicksSinceDigStart() {
        return this.ticksSinceDigStart;
    }

    public int getHighWalkSpeedTicks() {
        return this.highWalkSpeedTicks;
    }

    public int getHighFlySpeedTicks() {
        return this.highFlySpeedTicks;
    }

    public int getTicksSinceNearbyCarpet() {
        return this.ticksSinceNearbyCarpet;
    }

    public int getTicksSinceVehicleBed() {
        return this.ticksSinceVehicleBed;
    }

    public int getWaterloggedTicks() {
        return this.waterloggedTicks;
    }

    public int getFlowerPotTicks() {
        return this.flowerPotTicks;
    }

    public int getTicksSinceNearbyBubbleColumn() {
        return this.ticksSinceNearbyBubbleColumn;
    }

    public int getCarpetTicks() {
        return this.carpetTicks;
    }

    public int getTicksSinceFail() {
        return this.ticksSinceFail;
    }

    public int getTicksSinceHorizontalCollision() {
        return this.ticksSinceHorizontalCollision;
    }

    public int getTicksSinceNotSpectator() {
        return this.ticksSinceNotSpectator;
    }

    public int getTicksSinceFeetIce() {
        return this.ticksSinceFeetIce;
    }

    public int getTicksSinceStandingOnSolidOrIce() {
        return this.ticksSinceStandingOnSolidOrIce;
    }

    public int getTicksSinceBoat() {
        return this.ticksSinceBoat;
    }

    public int getTicksSinceMoving() {
        return this.ticksSinceMoving;
    }

    public int getTicksSinceRiptideWeapon() {
        return this.ticksSinceRiptideWeapon;
    }

    public int getTicksSinceHappyGhast() {
        return this.ticksSinceHappyGhast;
    }

    public int getSeagrassTicks() {
        return this.seagrassTicks;
    }

    public World getWorld() {
        return this.world;
    }

    public Entity getVehicleEntity() {
        return this.vehicleEntity;
    }

    public Entity getPreviousVehicleEntity() {
        return this.previousVehicleEntity;
    }

    public float getFlySpeed() {
        return this.flySpeed;
    }

    public float getAbilityFlySpeed() {
        return this.abilityFlySpeed;
    }

    public List getPendingRunnables() {
        return this.pendingRunnables;
    }

    public LinkedList getJumpFlags() {
        return this.jumpFlags;
    }

    public boolean isFlightExempt() {
        return this.flightExempt;
    }

    public boolean isCreativeExempt() {
        return this.creativeExempt;
    }

    public boolean isJoinedExempt() {
        return this.joinedExempt;
    }

    public boolean isLiquidExempt() {
        return this.liquidExempt;
    }

    public boolean isLevitationExempt() {
        return this.levitationExempt;
    }

    public boolean isSlowFallingExempt() {
        return this.slowFallingExempt;
    }

    public boolean isRiptideExempt() {
        return this.riptideExempt;
    }

    public boolean isVehicleExempt() {
        return this.vehicleExempt;
    }

    public boolean isLenientScaffoldingExempt() {
        return this.lenientScaffoldingExempt;
    }

    public boolean isGlidingExempt() {
        return this.glidingExempt;
    }

    public boolean isElytraExempt() {
        return this.elytraExempt;
    }

    public boolean isTeleportExempt() {
        return this.teleportExempt;
    }

    public boolean isEnderPearlExempt() {
        return this.enderPearlExempt;
    }

    public boolean isComboModeExempt() {
        return this.comboModeExempt;
    }

    public boolean isMythicMobExempt() {
        return this.mythicMobExempt;
    }

    public boolean isClimbableExempt() {
        return this.climbableExempt;
    }

    public void setNearCobweb(boolean value) {
        this.nearCobweb = value;
    }

    public void setNearIce(boolean value) {
        this.nearIce = value;
    }

    public void setNearSweetBerry(boolean value) {
        this.nearSweetBerry = value;
    }

    public void setNearSoulSandOrSoil(boolean value) {
        this.nearSoulSandOrSoil = value;
    }

    public void setNearBubbleColumn(boolean value) {
        this.nearBubbleColumn = value;
    }

    public void setNearWall(boolean value) {
        this.nearWall = value;
    }

    public void setNearLilyPad(boolean value) {
        this.nearLilyPad = value;
    }

    public void setNearTrapdoor(boolean value) {
        this.nearTrapdoor = value;
    }

    public void setNearHoney(boolean value) {
        this.nearHoney = value;
    }

    public void setNearBed(boolean value) {
        this.nearBed = value;
    }

    public void setNearShulker(boolean value) {
        this.nearShulker = value;
    }

    public void setNearCarpet(boolean value) {
        this.nearCarpet = value;
    }


    public void setNearExactScaffolding(boolean value) {
        this.nearExactScaffolding = value;
    }

    public void setNearSlime(boolean value) {
        this.nearSlime = value;
    }

    public void setNearFlowerPot(boolean value) {
        this.nearFlowerPot = value;
    }

    public void setNearPiston(boolean value) {
        this.nearPiston = value;
    }

    public void setNearCampfire(boolean value) {
        this.nearCampfire = value;
    }

    public void setNearSweetBerryBush(boolean value) {
        this.nearSweetBerryBush = value;
    }


    public void setOnSeaPickle(boolean value) {
        this.onSeaPickle = value;
    }

    public void setNearSnow(boolean value) {
        this.nearSnow = value;
    }

    public void setNearAnvil(boolean value) {
        this.nearAnvil = value;
    }

    public void setNearExactEndRod(boolean value) {
        this.nearExactEndRod = value;
    }

    public void setNearEndRod(boolean value) {
        this.nearEndRod = value;
    }

    public void setNearDoor(boolean value) {
        this.nearDoor = value;
    }

    public void setNearCauldron(boolean value) {
        this.nearCauldron = value;
    }

    public void setNearHopper(boolean value) {
        this.nearHopper = value;
    }

    public void setNearFenceGate(boolean value) {
        this.nearFenceGate = value;
    }

    public void setOnLilyPad(boolean value) {
        this.onLilyPad = value;
    }

    public void setNearClimbableColumn(boolean value) {
        this.nearClimbableColumn = value;
    }

    public void setCobwebInColumn(boolean value) {
        this.cobwebInColumn = value;
    }

    public void setBubbleColumnInColumn(boolean value) {
        this.bubbleColumnInColumn = value;
    }

    public void setLiquidInColumn(boolean value) {
        this.liquidInColumn = value;
    }

    public void setHeadSamplesAllSolid(boolean value) {
        this.headSamplesAllSolid = value;
    }

    public void setNearKelp(boolean value) {
        this.nearKelp = value;
    }

    public void setNearbySolid(boolean value) {
        this.nearbySolid = value;
    }

    public void setNearbyHeadSolid(boolean value) {
        this.nearbyHeadSolid = value;
    }

    public void setFeetSampleSolid(boolean value) {
        this.feetSampleSolid = value;
    }

    public void setClientOnGround(boolean value) {
        this.clientOnGround = value;
    }

    public void setBlockGridAlignedY(boolean value) {
        this.blockGridAlignedY = value;
    }

    public void setUnmoved(boolean value) {
        this.unmoved = value;
    }

    public void setNearBoat(boolean value) {
        this.nearBoat = value;
    }

    public void setNearBell(boolean value) {
        this.nearBell = value;
    }

    public void setNearBedOrBedrock(boolean value) {
        this.nearBedOrBedrock = value;
    }

    public void setNearSoulSand(boolean value) {
        this.nearSoulSand = value;
    }

    public void setNearLectern(boolean value) {
        this.nearLectern = value;
    }


    public void setNearSeaPickle(boolean value) {
        this.nearSeaPickle = value;
    }

    public void setTouchingSolid(boolean value) {
        this.touchingSolid = value;
    }

    public void setNearConduit(boolean value) {
        this.nearConduit = value;
    }

    public void setNearCollidingHorizontally(boolean value) {
        this.nearCollidingHorizontally = value;
    }

    public void setNearCampfireBlock(boolean value) {
        this.nearCampfireBlock = value;
    }

    public void setNearSolid(boolean value) {
        this.nearSolid = value;
    }

    public void setNearChest(boolean value) {
        this.nearChest = value;
    }

    public void setVehicleNearSolid(boolean value) {
        this.vehicleNearSolid = value;
    }

    public void setVehicleNearIce(boolean value) {
        this.vehicleNearIce = value;
    }

    public void setVehicleNearSweetBerry(boolean value) {
        this.vehicleNearSweetBerry = value;
    }

    public void setVehicleAllSolid(boolean value) {
        this.vehicleAllSolid = value;
    }

    public void setNearNotMoving(boolean value) {
        this.hasPositionDelta = value;
    }

    public void setNearFrostedIce(boolean value) {
        this.nearFrostedIce = value;
    }

    public void setNearExactBubbleColumn(boolean value) {
        this.nearExactBubbleColumn = value;
    }

    public void setNearFarmland(boolean value) {
        this.nearFarmland = value;
    }

    public void setLastClientOnGround(boolean value) {
        this.lastClientOnGround = value;
    }

    public void setFlightAllowed(boolean value) {
        this.flightAllowed = value;
    }

    public void setNearHoneyBlock(boolean value) {
        this.nearHoneyBlock = value;
    }

    public void setNearSkull(boolean value) {
        this.nearSkull = value;
    }

    public void setVehicleNearEndRod(boolean value) {
        this.vehicleNearEndRod = value;
    }

    public void setNearPressurePlate(boolean value) {
        this.nearPressurePlate = value;
    }

    public void setVehicleNearBed(boolean value) {
        this.vehicleNearBed = value;
    }

    public void setNearFence(boolean value) {
        this.nearFence = value;
    }

    public void setNearPowderSnow(boolean value) {
        this.nearPowderSnow = value;
    }

    public void setCollidingLivingEntity(boolean value) {
        this.collidingLivingEntity = value;
    }

    public void setPreviousPreviousClientOnGround(boolean value) {
        this.previousPreviousClientOnGround = value;
    }

    public void setNearbyLivingEntity(boolean value) {
        this.nearbyLivingEntity = value;
    }

    public void setNearCake(boolean value) {
        this.nearCake = value;
    }

    public void setNearSlab(boolean value) {
        this.nearSlab = value;
    }

    public void setNearWorldBorder(boolean value) {
        this.nearWorldBorder = value;
    }

    public void setNearStair(boolean value) {
        this.nearStair = value;
    }

    public void setJumpCollisionExempt(boolean value) {
        this.jumpCollisionExempt = value;
    }

    public void setNearSign(boolean value) {
        this.nearSign = value;
    }

    public void setNearClimbable(boolean value) {
        this.nearClimbable = value;
    }

    public void setNearBamboo(boolean value) {
        this.nearBamboo = value;
    }

    public void setBreakingBlock(boolean value) {
        this.breakingBlock = value;
    }

    public void setNearScaffolding(boolean value) {
        this.nearScaffolding = value;
    }

    public void setInPowderSnow(boolean value) {
        this.inPowderSnow = value;
    }

    public void setFeetOnIce(boolean value) {
        this.feetOnIce = value;
    }

    public void setBlockBelowSolidOrIce(boolean value) {
        this.blockBelowSolidOrIce = value;
    }

    public void setCarpetInHeadSample(boolean value) {
        this.carpetInHeadSample = value;
    }

    public void setNearDripleaf(boolean value) {
        this.nearDripleaf = value;
    }

    public void setNearDripstone(boolean value) {
        this.nearDripstone = value;
    }

    public void setNearHeavyCore(boolean value) {
        this.nearHeavyCore = value;
    }

    public void setPositionChanged(boolean value) {
        this.positionChanged = value;
    }

    public void setLastPositionChanged(boolean value) {
        this.lastPositionChanged = value;
    }

    public void setPreviousPreviousPositionChanged(boolean value) {
        this.previousPreviousPositionChanged = value;
    }

    public void setNearHappyGhast(boolean value) {
        this.nearHappyGhast = value;
    }

    public void setCollidingDragon(boolean value) {
        this.collidingDragon = value;
    }

    public void setNearDriedGhast(boolean value) {
        this.nearDriedGhast = value;
    }

    public void setNearBlueIce(boolean value) {
        this.nearBlueIce = value;
    }

    public void setLastLocation(Location location) {
        this.lastLocation = location;
    }

    public void setCurrentLocation(Location value) {
        this.currentLocation = value;
    }

    public void setBlockBelow(Material value) {
        this.blockBelow = value;
    }

    public void setBlockTwoBelow(Material value) {
        this.blockTwoBelow = value;
    }

    public void setBlockThreeBelow(Material value) {
        this.blockThreeBelow = value;
    }

    public void setNearbyMaterials(List value) {
        this.nearbyMaterials = value;
    }

    public void setNearbyFeetSamples(List value) {
        this.nearbyFeetSamples = value;
    }

    public void setNearbyHeadSamples(List value) {
        this.nearbyHeadSamples = value;
    }

    public void setNearbyHeadSurroundSamples(List value) {
        this.nearbyHeadSurroundSamples = value;
    }

    public void setNearbyFeetSurroundSamples(List value) {
        this.nearbyFeetSurroundSamples = value;
    }

    public void setPreviousY(double value) {
        this.previousY = value;
    }

    public void setDeltaZ(double value) {
        this.deltaZ = value;
    }


    public void setLastLocationZ(double lastLocationZ) {
        this.lastLocationZ = lastLocationZ;
    }

    public void setVehicleTicks(int value) {
        this.vehicleTicks = value;
    }

    public void setTicksSinceFlightAllowed(int value) {
        this.ticksSinceFlightAllowed = value;
    }

    public void setTicksSinceSlime(int value) {
        this.ticksSinceSlime = value;
    }

    public void setAirTicks(int value) {
        this.airTicks = value;
    }

    public void setGroundTicks(int value) {
        this.groundTicks = value;
    }


    public void setTicksSinceOnGround(int value) {
        this.ticksSinceOnGround = value;
    }

    public void setRiptideTicks(int value) {
        this.riptideTicks = value;
    }

    public void setTicksSinceCollisionExempt(int value) {
        this.ticksSinceCollisionExempt = value;
    }

    public void setTicksSinceSolidFeet(int value) {
        this.ticksSinceSolidFeet = value;
    }

    public void setTicksSinceHoldingRiptide(int value) {
        this.ticksSinceHoldingRiptide = value;
    }

    public void setLevitationTicks(int value) {
        this.levitationTicks = value;
    }

    public void setBubbleColumnTicks(int value) {
        this.bubbleColumnTicks = value;
    }

    public void setTicksGliding(int value) {
        this.ticksGliding = value;
    }

    public void setTicksSinceSprinting(int value) {
        this.ticksSinceSprinting = value;
    }

    public void setGhostBlockAirTicks(int ticks) {
        this.ghostBlockAirTicks = ticks;
    }

    public void setAlignedYTicks(int value) {
        this.alignedYTicks = value;
    }

    public void setTicksSinceWall(int value) {
        this.ticksSinceWall = value;
    }

    public void setLiquidTicks(int value) {
        this.liquidTicks = value;
    }

    public void setTicksSinceClimbableColumn(int value) {
        this.ticksSinceClimbableColumn = value;
    }

    public void setTicksAirborne(int value) {
        this.ticksAirborne = value;
    }

    public void setPackedIceTicks(int value) {
        this.packedIceTicks = value;
    }

    public void setPressurePlateTicks(int value) {
        this.pressurePlateTicks = value;
    }

    public void setSoulSandTicks(int value) {
        this.soulSandTicks = value;
    }

    public void setTicksSinceCobweb(int value) {
        this.ticksSinceCobweb = value;
    }

    public void setSoulSpeedTicks(int value) {
        this.soulSpeedTicks = value;
    }

    public void setTicksSinceBedOrBedrock(int value) {
        this.ticksSinceBedOrBedrock = value;
    }

    public void setLastDigStartTick(int value) {
        this.lastDigStartTick = value;
    }

    public void setVehicleMoveTicks(int value) {
        this.vehicleMoveTicks = value;
    }

    public void setTicksSinceBlockPlace(int value) {
        this.ticksSinceBlockPlace = value;
    }

    public void setTicksSinceTouchingSolid(int value) {
        this.ticksSinceTouchingSolid = value;
    }

    public void setTicksSinceEndRod(int value) {
        this.ticksSinceEndRod = value;
    }

    public void setEndRodTicks(int value) {
        this.endRodTicks = value;
    }

    public void setSneakTicks(int value) {
        this.sneakTicks = value;
    }

    public void setTicksSinceFirework(int value) {
        this.ticksSinceFirework = value;
    }

    public void setTicksSinceBamboo(int value) {
        this.ticksSinceBamboo = value;
    }

    public void setTicksSinceNearbySweetBerry(int value) {
        this.ticksSinceNearbySweetBerry = value;
    }

    public void setTicksSinceNearbyEndRod(int value) {
        this.ticksSinceNearbyEndRod = value;
    }

    public void setTicksSinceVehicleSolid(int value) {
        this.ticksSinceVehicleSolid = value;
    }

    public void setTicksSinceVehicleIce(int value) {
        this.ticksSinceVehicleIce = value;
    }

    public void setTicksSinceVehicleSweetBerry(int value) {
        this.ticksSinceVehicleSweetBerry = value;
    }

    public void setSeaPickleTicks(int value) {
        this.seaPickleTicks = value;
    }

    public void setBlockX(int value) {
        this.blockX = value;
    }

    public void setBlockY(int value) {
        this.blockY = value;
    }

    public void setBlockZ(int value) {
        this.blockZ = value;
    }

    public void setTicksSinceFrostedIce(int value) {
        this.ticksSinceFrostedIce = value;
    }

    public void setTicksSinceExactScaffolding(int value) {
        this.ticksSinceExactScaffolding = value;
    }

    public void setTicksSinceGhostBlock(int value) {
        this.ticksSinceGhostBlock = value;
    }

    public void setLastAbilitiesTick(int value) {
        this.lastAbilitiesTick = value;
    }

    public void setTicksSinceVehicleAllSolid(int value) {
        this.ticksSinceVehicleAllSolid = value;
    }

    public void setTicksStationary(int value) {
        this.ticksStationary = value;
    }

    public void setElytraTicks(int value) {
        this.elytraTicks = value;
    }

    public void setTicksSinceSkull(int value) {
        this.ticksSinceSkull = value;
    }

    public void setFarmlandTicks(int value) {
        this.farmlandTicks = value;
    }

    public void setTicksSinceShulker(int value) {
        this.ticksSinceShulker = value;
    }

    public void setTicksSinceCarpet(int value) {
        this.ticksSinceCarpet = value;
    }

    public void setEntityCollisionTicks(int value) {
        this.entityCollisionTicks = value;
    }

    public void setTicksSinceVehicleEndRod(int value) {
        this.ticksSinceVehicleEndRod = value;
    }

    public void setTicksSinceDigStart(int value) {
        this.ticksSinceDigStart = value;
    }

    public void setHighWalkSpeedTicks(int value) {
        this.highWalkSpeedTicks = value;
    }

    public void setHighFlySpeedTicks(int value) {
        this.highFlySpeedTicks = value;
    }

    public void setTicksSinceNearbyCarpet(int value) {
        this.ticksSinceNearbyCarpet = value;
    }

    public void setTicksSinceVehicleBed(int value) {
        this.ticksSinceVehicleBed = value;
    }

    public void setWaterloggedTicks(int value) {
        this.waterloggedTicks = value;
    }

    public void setFlowerPotTicks(int value) {
        this.flowerPotTicks = value;
    }

    public void setTicksSinceNearbyBubbleColumn(int value) {
        this.ticksSinceNearbyBubbleColumn = value;
    }

    public void setCarpetTicks(int value) {
        this.carpetTicks = value;
    }

    public void setTicksSinceFail(int value) {
        this.ticksSinceFail = value;
    }

    public void setTicksSinceHorizontalCollision(int value) {
        this.ticksSinceHorizontalCollision = value;
    }

    public void setTicksSinceNotSpectator(int value) {
        this.ticksSinceNotSpectator = value;
    }

    public void setTicksSinceFeetIce(int value) {
        this.ticksSinceFeetIce = value;
    }

    public void setTicksSinceStandingOnSolidOrIce(int value) {
        this.ticksSinceStandingOnSolidOrIce = value;
    }

    public void setTicksSinceBoat(int value) {
        this.ticksSinceBoat = value;
    }

    public void setMovementViolations(int value) {
        this.ticksSinceMoving = value;
    }

    public void setTicksSinceRiptideWeapon(int value) {
        this.ticksSinceRiptideWeapon = value;
    }

    public void setTicksSinceHappyGhast(int value) {
        this.ticksSinceHappyGhast = value;
    }

    public void setSeagrassTicks(int value) {
        this.seagrassTicks = value;
    }

    public void setWorld(World value) {
        this.world = value;
    }

    public void setVehicleEntity(Entity entity) {
        this.vehicleEntity = entity;
    }

    public void setPreviousVehicleEntity(Entity value) {
        this.previousVehicleEntity = value;
    }

    public void setFlySpeed(float flySpeed) {
        this.flySpeed = flySpeed;
    }

    public void setScaledAbilitiesSpeed(float speed) {
        this.abilityFlySpeed = speed;
    }

    public void setPendingRunnables(List value) {
        this.pendingRunnables = value;
    }

    public void setJumpFlags(LinkedList value) {
        this.jumpFlags = value;
    }

    public void setFlightExempt(boolean value) {
        this.flightExempt = value;
    }

    public void setCreativeExempt(boolean value) {
        this.creativeExempt = value;
    }

    public void setJoinedExempt(boolean value) {
        this.joinedExempt = value;
    }

    public void setLiquidExempt(boolean value) {
        this.liquidExempt = value;
    }

    public void setLevitationExempt(boolean value) {
        this.levitationExempt = value;
    }

    public void setSlowFallingExempt(boolean value) {
        this.slowFallingExempt = value;
    }

    public void setRiptideExempt(boolean value) {
        this.riptideExempt = value;
    }

    public void setVehicleExempt(boolean value) {
        this.vehicleExempt = value;
    }

    public void setLenientScaffoldingExempt(boolean value) {
        this.lenientScaffoldingExempt = value;
    }

    public void setGlidingExempt(boolean value) {
        this.glidingExempt = value;
    }

    public void setElytraExempt(boolean value) {
        this.elytraExempt = value;
    }

    public void setTeleportExempt(boolean value) {
        this.teleportExempt = value;
    }

    public void setEnderPearlExempt(boolean value) {
        this.enderPearlExempt = value;
    }

    public void setChunkExempt(boolean value) {
        this.chunkExempt = value;
    }

    public void setComboModeExempt(boolean value) {
        this.comboModeExempt = value;
    }

    public void setMythicMobExempt(boolean value) {
        this.mythicMobExempt = value;
    }

    public void setClimbableExempt(boolean value) {
        this.climbableExempt = value;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    public void setNearbyEntities(List value) {
        this.nearbyEntities = value;
    }

    public void setNearbyColumnMaterials(List value) {
        this.nearbyColumnMaterials = value;
    }

    public void setVehicleNearbyMaterials(List value) {
        this.vehicleNearbyMaterials = value;
    }

    public void setNearbyColumnExtra(List value) {
        this.nearbyColumnExtra = value;
    }

    public void setStandingMaterial(Material value) {
        this.standingMaterial = value;
    }

    public void setLastHeldMaterial(Material value) {
        this.lastHeldMaterial = value;
    }

    public void setAlternateHeldMaterial(Material value) {
        this.alternateHeldMaterial = value;
    }

    private void lambda$handleUnloadedChunk$1(Location location) {
        this.playerData.getPlayer().teleport(location, PlayerTeleportEvent.TeleportCause.UNKNOWN);
    }

    private void lambda$handleAbilities$0(WrapperPlayServerPlayerAbilities wrapperPlayServerPlayerAbilities) {
        this.ticksSinceFlightAllowed = 0;
        this.lastAbilitiesTick = VulcanBootstrap.INSTANCE.getKeepAliveRunnable().getCurrentTick();
        this.flightAllowed = wrapperPlayServerPlayerAbilities.isFlightAllowed();
        this.abilityFlySpeed = wrapperPlayServerPlayerAbilities.getFlySpeed() * 2.0f;
    }

    public boolean isPositionChanged() {
        return this.positionChanged;
    }

    public boolean isLastPositionChanged() {
        return this.lastPositionChanged;
    }

    public double getDeltaX() {
        return this.deltaX;
    }

    public double getDeltaZ() {
        return this.deltaZ;
    }

    public double getPreviousX() {
        return this.previousX;
    }

    public double getPreviousY() {
        return this.previousY;
    }

    public double getPreviousZ() {
        return this.previousZ;
    }

    public boolean isFrozen() {
        return this.frozen;
    }

    public boolean isNearCobweb() {
        return this.nearCobweb;
    }

    public boolean isNearIce() {
        return this.nearIce;
    }

    public Material getBlockBelow() {
        return this.blockBelow;
    }

    public List getNearbyMaterials() {
        return this.nearbyMaterials;
    }

}
