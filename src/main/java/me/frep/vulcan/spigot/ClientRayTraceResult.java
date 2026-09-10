package me.frep.vulcan.spigot;

import org.bukkit.entity.Entity;

public class ClientRayTraceResult {
    private final MutableBlockVector blockPos;
    public final HitResultType hitType;
    public final BlockFace facing;
    public final Vec3 hitVec;
    public final Entity entity;

    public ClientRayTraceResult(Vec3 hitVec, BlockFace facing, MutableBlockVector blockPos) {
        this(HitResultType.BLOCK, hitVec, facing, blockPos);
    }

    public ClientRayTraceResult(Vec3 hitVec, BlockFace facing) {
        this(HitResultType.BLOCK, hitVec, facing, MutableBlockVector.ORIGIN);
    }

    public ClientRayTraceResult(Entity entity) {
        this(entity, new Vec3(entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ()));
    }

    public ClientRayTraceResult(HitResultType hitType, Vec3 hitVec, BlockFace facing, MutableBlockVector blockPos) {
        this.hitType = hitType;
        this.blockPos = blockPos;
        this.facing = facing;
        this.hitVec = hitVec == null ? null : new Vec3(hitVec.x, hitVec.y, hitVec.z);
        this.entity = null;
    }

    public ClientRayTraceResult(Entity entity, Vec3 hitVec) {
        this.hitType = HitResultType.ENTITY;
        this.entity = entity;
        this.hitVec = hitVec;
        this.facing = null;
        this.blockPos = null;
    }

    public MutableBlockVector getBlockPos() {
        return this.blockPos;
    }

    @Override
    public String toString() {
        return "HitResult{type=" + this.hitType
            + ", blockpos=" + this.blockPos
            + ", f=" + this.facing
            + ", pos=" + this.hitVec
            + ", entity=" + this.entity
            + '}';
    }
}
