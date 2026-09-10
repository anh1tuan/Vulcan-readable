package me.frep.vulcan.spigot;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Effect;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

/**
 * 3D ray from an origin along a direction (Bukkit {@link Vector}).
 */
public class Ray3d {
    public final Vector origin;
    public final Vector direction;

    public Ray3d(Vector origin, Vector direction) {
        this.origin = origin;
        this.direction = direction;
    }

    public static boolean vectorsEqual(Vector left, Vector right) {
        return left.getX() == right.getX()
            && left.getY() == right.getY()
            && left.getZ() == right.getZ();
    }

    public Vector getPointAt(double distance) {
        return this.origin.clone().add(this.direction.clone().multiply(distance));
    }

    public boolean isOnRay(Vector point) {
        if (this.direction.lengthSquared() == 0.0) {
            return vectorsEqual(this.origin, point);
        }
        double t = (point.getX() - this.origin.getX()) / (this.direction.getX() == 0.0 ? 1.0 : this.direction.getX());
        Vector expected = this.getPointAt(t);
        return expected.distanceSquared(point) < 1.0E-6;
    }

    public List getPoints(double maxDistance, double step) {
        List points = new ArrayList();
        if (step <= 0.0) {
            points.add(this.origin.clone());
            return points;
        }
        for (double distance = 0.0; distance <= maxDistance; distance += step) {
            points.add(this.getPointAt(distance));
        }
        return points;
    }

    public List getPointsInRange(double minDistance, double maxDistance, double step) {
        List points = new ArrayList();
        if (step <= 0.0) {
            return points;
        }
        for (double distance = minDistance; distance <= maxDistance; distance += step) {
            points.add(this.getPointAt(distance));
        }
        return points;
    }

    public List getBlocks(World world, double maxDistance, double step) {
        List blocks = new ArrayList();
        List points = this.getPoints(maxDistance, step);
        for (int i = 0; i < points.size(); ++i) {
            Vector point = (Vector) points.get(i);
            Block block = point.toLocation(world).getBlock();
            if (block.getType().isSolid()) {
                blocks.add(block);
            }
        }
        return blocks;
    }

    public Vector getIntercept(Vector min, Vector max, double maxDistance, double step) {
        List points = this.getPoints(maxDistance, step);
        for (int i = 0; i < points.size(); ++i) {
            Vector point = (Vector) points.get(i);
            if (isBetween(point, min, max)) {
                return point;
            }
        }
        return null;
    }

    public boolean intersectsBox(Vector min, Vector max, double maxDistance, double step) {
        return this.getIntercept(min, max, maxDistance, step) != null;
    }

    public void playRayEffects(World world, double maxDistance, double step) {
        List points = this.getPoints(maxDistance, step);
        for (int i = 0; i < points.size(); ++i) {
            Vector point = (Vector) points.get(i);
            world.playEffect(point.toLocation(world), Effect.ENDEREYE_LAUNCH, 5);
        }
    }

    public static boolean isBetween(Vector point, Vector min, Vector max) {
        double x = point.getX();
        double y = point.getY();
        double z = point.getZ();
        return x >= Math.min(min.getX(), max.getX()) && x <= Math.max(min.getX(), max.getX())
            && y >= Math.min(min.getY(), max.getY()) && y <= Math.max(min.getY(), max.getY())
            && z >= Math.min(min.getZ(), max.getZ()) && z <= Math.max(min.getZ(), max.getZ());
    }
}
