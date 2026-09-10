package me.frep.vulcan.spigot;

import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityRelativeMove;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityRelativeMoveAndRotation;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ReachEntityHelper {
    private final PlayerData playerData;
    private int ticksSinceAttack = 100;
    private int lastAttackConfirmTick;
    private int attackStartTick;
    private int lastItemFrameAttackTick;
    private Entity targetEntity;
    private Player attackingPlayer;
    private Player previousAttackingPlayer;
    private double attackDistance;
    private double closestReachDistance;
    private final SampleDeque targets = new SampleDeque(20);

    public ReachEntityHelper(PlayerData playerData) {
        this.playerData = playerData;
    }

    public void handleInteractEntity(WrapperPlayClientInteractEntity packet, long timestamp) {
        Player player = this.playerData.getPlayer();
        if (player == null) {
            return;
        }
        Entity entity = SpigotReflectionUtil.getEntityById(player.getWorld(), packet.getEntityId());
        if (entity instanceof ItemFrame) {
            this.lastItemFrameAttackTick = VulcanBootstrap.INSTANCE.getKeepAliveRunnable().getCurrentTick();
        }
        if (packet.getAction() != WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
            return;
        }
        this.targetEntity = entity;
        if (entity != null) {
            this.attackDistance = player.getLocation().toVector().setY(0).distance(entity.getLocation().toVector().setY(0)) - 0.42;
        }
        if (entity instanceof Player) {
            this.previousAttackingPlayer = this.attackingPlayer == null ? (Player) entity : this.attackingPlayer;
            this.attackingPlayer = (Player) entity;
        }
        this.ticksSinceAttack = 0;
    }

    public void handleRelativeMove(WrapperPlayServerEntityRelativeMove packet) {
        this.recordTargetMove(packet.getEntityId(), packet.getDeltaX(), packet.getDeltaY(), packet.getDeltaZ());
    }

    public void handleRelativeMoveAndRotation(WrapperPlayServerEntityRelativeMoveAndRotation packet) {
        this.recordTargetMove(packet.getEntityId(), packet.getDeltaX(), packet.getDeltaY(), packet.getDeltaZ());
    }

    public void clearTargets() {
        this.targets.clear();
    }

    public void resetReachState() {
        this.lastAttackConfirmTick = VulcanBootstrap.INSTANCE.getKeepAliveRunnable().getCurrentTick();
    }

    public void markAttackStartTick() {
        this.attackStartTick = VulcanBootstrap.INSTANCE.getKeepAliveRunnable().getCurrentTick();
    }

    public int getAttackTick() {
        return VulcanBootstrap.INSTANCE.getKeepAliveRunnable().getCurrentTick() - this.lastAttackConfirmTick;
    }

    public int getTicksSinceAttackStart() {
        return VulcanBootstrap.INSTANCE.getKeepAliveRunnable().getCurrentTick() - this.attackStartTick;
    }

    public void handleFlying() {
        ++this.ticksSinceAttack;
        if (this.targets.isEmpty() || this.attackingPlayer == null) {
            return;
        }
        int now = VulcanBootstrap.INSTANCE.getKeepAliveRunnable().getCurrentTick();
        int confirmAge = this.getAttackTick();
        double x = this.playerData.getBlockBreakTracker().getLocationX();
        double z = this.playerData.getBlockBreakTracker().getLocationZ();
        Vector origin = new Vector(x, 0.0, z);
        this.closestReachDistance = this.targets.stream()
            .filter(sample -> sample instanceof ReachTarget && Math.abs(now - ((ReachTarget) sample).getTick() - confirmAge) < 3)
            .mapToDouble(sample -> origin.distance(((ReachTarget) sample).getLocation().toVector().setY(0)) - 0.52)
            .min()
            .orElse(-1.0);
    }

    public PlayerData getPlayerData() {
        return this.playerData;
    }

    public int getTicksSinceAttack() {
        return this.ticksSinceAttack;
    }

    public int getLastAttackConfirmTick() {
        return this.lastAttackConfirmTick;
    }

    public int getAttackStartTick() {
        return this.attackStartTick;
    }

    public int getLastItemFrameAttackTick() {
        return this.lastItemFrameAttackTick;
    }

    public Entity getTargetEntity() {
        return this.targetEntity;
    }

    public Player getAttackingPlayer() {
        return this.attackingPlayer;
    }

    public Player getPreviousAttackingPlayer() {
        return this.previousAttackingPlayer;
    }

    public double getAttackDistance() {
        return this.attackDistance;
    }

    public double getClosestReachDistance() {
        return this.closestReachDistance;
    }

    public SampleDeque getTargets() {
        return this.targets;
    }

    private void recordTargetMove(int entityId, double deltaX, double deltaY, double deltaZ) {
        if (this.attackingPlayer == null || this.attackingPlayer.getEntityId() != entityId) {
            return;
        }
        PlayerData targetData = VulcanBootstrap.INSTANCE.getHookManager().getPlayerData(this.attackingPlayer);
        if (targetData == null || targetData.getPlayer() == null) {
            return;
        }
        int tick = VulcanBootstrap.INSTANCE.getKeepAliveRunnable().getCurrentTick();
        World world = targetData.getPlayer().getWorld();
        Location location = new Location(
            world,
            targetData.getBlockBreakTracker().getLocationX() - deltaX,
            targetData.getBlockBreakTracker().getLocationY() - deltaY,
            targetData.getBlockBreakTracker().getLocationZ() - deltaZ
        );
        this.targets.add(new ReachTarget(location, tick));
    }
}
