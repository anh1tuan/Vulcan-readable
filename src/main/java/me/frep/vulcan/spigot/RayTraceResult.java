package me.frep.vulcan.spigot;

import org.bukkit.entity.Entity;

public class RayTraceResult {
    private final MutableBlockVector blockPos;
    public final HitResultType hitType;
    public final BlockFace facing;
    public final Vec3d hitVec;
    public final Entity entity;

    public RayTraceResult(Vec3d hitVec, BlockFace facing, MutableBlockVector blockPos) {
        this(HitResultType.BLOCK, hitVec, facing, blockPos);
    }

    public RayTraceResult(Vec3d hitVec, BlockFace facing) {
        this(HitResultType.BLOCK, hitVec, facing, MutableBlockVector.ORIGIN);
    }

    public RayTraceResult(Entity entity) {
        this(entity, new Vec3d(entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ()));
    }

    public RayTraceResult(HitResultType hitType, Vec3d hitVec, BlockFace facing, MutableBlockVector blockPos) {
        this.hitType = hitType;
        this.blockPos = blockPos;
        this.facing = facing;
        this.hitVec = hitVec == null ? null : new Vec3d(hitVec);
        this.entity = null;
    }

    public RayTraceResult(Entity entity, Vec3d hitVec) {
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
