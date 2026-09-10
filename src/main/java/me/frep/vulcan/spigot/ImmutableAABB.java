package me.frep.vulcan.spigot;

/**
 * Minecraft {@code AxisAlignedBB} recovered from the jar-reader license class
 * (Zelix merged AABB geometry into the same type as {@code %%__USER__%%} placeholders).
 */
public class ImmutableAABB {
    public final double minX;
    public final double minY;
    public final double minZ;
    public final double maxX;
    public final double maxY;
    public final double maxZ;

    public ImmutableAABB(double x1, double y1, double z1, double x2, double y2, double z2) {
        this.minX = Math.min(x1, x2);
        this.minY = Math.min(y1, y2);
        this.minZ = Math.min(z1, z2);
        this.maxX = Math.max(x1, x2);
        this.maxY = Math.max(y1, y2);
        this.maxZ = Math.max(z1, z2);
    }

    public ImmutableAABB(MutableBlockVector min, MutableBlockVector max) {
        this(min.getBlockX(), min.getBlockY(), min.getBlockZ(), max.getBlockX(), max.getBlockY(), max.getBlockZ());
    }

    public static ImmutableAABB fromUnordered(double x1, double y1, double z1, double x2, double y2, double z2) {
        return new ImmutableAABB(x1, y1, z1, x2, y2, z2);
    }

    public ImmutableAABB expand(double x, double y, double z) {
        double newMinX = this.minX;
        double newMinY = this.minY;
        double newMinZ = this.minZ;
        double newMaxX = this.maxX;
        double newMaxY = this.maxY;
        double newMaxZ = this.maxZ;
        if (x < 0.0) {
            newMinX += x;
        } else if (x > 0.0) {
            newMaxX += x;
        }
        if (y < 0.0) {
            newMinY += y;
        } else if (y > 0.0) {
            newMaxY += y;
        }
        if (z < 0.0) {
            newMinZ += z;
        } else if (z > 0.0) {
            newMaxZ += z;
        }
        return new ImmutableAABB(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ);
    }

    public ImmutableAABB grow(double x, double y, double z) {
        return new ImmutableAABB(
            this.minX - x,
            this.minY - y,
            this.minZ - z,
            this.maxX + x,
            this.maxY + y,
            this.maxZ + z
        );
    }

    public ImmutableAABB contract(double x, double y, double z) {
        return new ImmutableAABB(
            this.minX + x,
            this.minY + y,
            this.minZ + z,
            this.maxX - x,
            this.maxY - y,
            this.maxZ - z
        );
    }

    public ImmutableAABB offset(double x, double y, double z) {
        return new ImmutableAABB(
            this.minX + x,
            this.minY + y,
            this.minZ + z,
            this.maxX + x,
            this.maxY + y,
            this.maxZ + z
        );
    }

    public ImmutableAABB union(ImmutableAABB other) {
        return new ImmutableAABB(
            Math.min(this.minX, other.minX),
            Math.min(this.minY, other.minY),
            Math.min(this.minZ, other.minZ),
            Math.max(this.maxX, other.maxX),
            Math.max(this.maxY, other.maxY),
            Math.max(this.maxZ, other.maxZ)
        );
    }

    public double calculateXOffset(ImmutableAABB other, double offsetX) {
        if (other.maxY <= this.minY || other.minY >= this.maxY || other.maxZ <= this.minZ || other.minZ >= this.maxZ) {
            return offsetX;
        }
        if (offsetX > 0.0 && other.maxX <= this.minX) {
            double delta = this.minX - other.maxX;
            if (delta < offsetX) {
                offsetX = delta;
            }
        } else if (offsetX < 0.0 && other.minX >= this.maxX) {
            double delta = this.maxX - other.minX;
            if (delta > offsetX) {
                offsetX = delta;
            }
        }
        return offsetX;
    }

    public double calculateYOffset(ImmutableAABB other, double offsetY) {
        if (other.maxX <= this.minX || other.minX >= this.maxX || other.maxZ <= this.minZ || other.minZ >= this.maxZ) {
            return offsetY;
        }
        if (offsetY > 0.0 && other.maxY <= this.minY) {
            double delta = this.minY - other.maxY;
            if (delta < offsetY) {
                offsetY = delta;
            }
        } else if (offsetY < 0.0 && other.minY >= this.maxY) {
            double delta = this.maxY - other.minY;
            if (delta > offsetY) {
                offsetY = delta;
            }
        }
        return offsetY;
    }

    public double calculateZOffset(ImmutableAABB other, double offsetZ) {
        if (other.maxX <= this.minX || other.minX >= this.maxX || other.maxY <= this.minY || other.minY >= this.maxY) {
            return offsetZ;
        }
        if (offsetZ > 0.0 && other.maxZ <= this.minZ) {
            double delta = this.minZ - other.maxZ;
            if (delta < offsetZ) {
                offsetZ = delta;
            }
        } else if (offsetZ < 0.0 && other.minZ >= this.maxZ) {
            double delta = this.maxZ - other.minZ;
            if (delta > offsetZ) {
                offsetZ = delta;
            }
        }
        return offsetZ;
    }

    public boolean intersectsWith(ImmutableAABB other) {
        return other.maxX > this.minX && other.minX < this.maxX
            && other.maxY > this.minY && other.minY < this.maxY
            && other.maxZ > this.minZ && other.minZ < this.maxZ;
    }

    public boolean isVecInside(Vec3 vec) {
        return vec.x > this.minX && vec.x < this.maxX
            && vec.y > this.minY && vec.y < this.maxY
            && vec.z > this.minZ && vec.z < this.maxZ;
    }

    public double getAverageEdgeLength() {
        return (this.maxX - this.minX + (this.maxY - this.minY) + (this.maxZ - this.minZ)) / 3.0;
    }

    public ClientRayTraceResult calculateIntercept(Vec3 start, Vec3 end) {
        Vec3 hitMinX = start.getIntermediateWithXValue(end, this.minX);
        Vec3 hitMaxX = start.getIntermediateWithXValue(end, this.maxX);
        Vec3 hitMinY = start.getIntermediateWithYValue(end, this.minY);
        Vec3 hitMaxY = start.getIntermediateWithYValue(end, this.maxY);
        Vec3 hitMinZ = start.getIntermediateWithZValue(end, this.minZ);
        Vec3 hitMaxZ = start.getIntermediateWithZValue(end, this.maxZ);
        if (!isVecInYZ(hitMinX)) {
            hitMinX = null;
        }
        if (!isVecInYZ(hitMaxX)) {
            hitMaxX = null;
        }
        if (!isVecInXZ(hitMinY)) {
            hitMinY = null;
        }
        if (!isVecInXZ(hitMaxY)) {
            hitMaxY = null;
        }
        if (!isVecInXY(hitMinZ)) {
            hitMinZ = null;
        }
        if (!isVecInXY(hitMaxZ)) {
            hitMaxZ = null;
        }
        Vec3 closest = closestHit(start, hitMinX, hitMaxX, hitMinY, hitMaxY, hitMinZ, hitMaxZ);
        if (closest == null) {
            return null;
        }
        BlockFace face;
        if (closest == hitMinX) {
            face = BlockFace.WEST;
        } else if (closest == hitMaxX) {
            face = BlockFace.EAST;
        } else if (closest == hitMinY) {
            face = BlockFace.DOWN;
        } else if (closest == hitMaxY) {
            face = BlockFace.UP;
        } else if (closest == hitMinZ) {
            face = BlockFace.NORTH;
        } else {
            face = BlockFace.SOUTH;
        }
        return new ClientRayTraceResult(closest, face);
    }

    public boolean hasNaN() {
        return Double.isNaN(this.minX)
            || Double.isNaN(this.minY)
            || Double.isNaN(this.minZ)
            || Double.isNaN(this.maxX)
            || Double.isNaN(this.maxY)
            || Double.isNaN(this.maxZ);
    }

    @Override
    public String toString() {
        return "AABB[" + this.minX + ", " + this.minY + ", " + this.minZ + " -> "
            + this.maxX + ", " + this.maxY + ", " + this.maxZ + "]";
    }

    private boolean isVecInYZ(Vec3 vec) {
        return vec != null
            && vec.y >= this.minY && vec.y <= this.maxY
            && vec.z >= this.minZ && vec.z <= this.maxZ;
    }

    private boolean isVecInXZ(Vec3 vec) {
        return vec != null
            && vec.x >= this.minX && vec.x <= this.maxX
            && vec.z >= this.minZ && vec.z <= this.maxZ;
    }

    private boolean isVecInXY(Vec3 vec) {
        return vec != null
            && vec.x >= this.minX && vec.x <= this.maxX
            && vec.y >= this.minY && vec.y <= this.maxY;
    }

    private static Vec3 closestHit(Vec3 origin, Vec3... hits) {
        Vec3 closest = null;
        for (Vec3 hit : hits) {
            if (hit == null) {
                continue;
            }
            if (closest == null || origin.distanceSquared(hit) < origin.distanceSquared(closest)) {
                closest = hit;
            }
        }
        return closest;
    }
}
