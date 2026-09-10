package me.frep.vulcan.spigot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.block.Block;

public final class MovementConstants {
    public static final double VANILLA_JUMP_MOTION = 0.41999998688697815;
    public static final List<Double> knownJumpMotions = new ArrayList<Double>();
    public static final Map<Integer, Double> expectedJumpMotionByAirTick = new HashMap<Integer, Double>();
    public static final Map<Integer, Double> sensitivityByPercent = new HashMap<Integer, Double>();
    public static final Map<Material, Float> blockHardnessByMaterial = new HashMap<Material, Float>();

    private MovementConstants() {
        throw new UnsupportedOperationException("utility class");
    }

    public static double sensitivityForPercent(int percent) {
        Double mapped = sensitivityByPercent.get(Integer.valueOf(percent));
        if (mapped != null) {
            return mapped.doubleValue();
        }
        return (double) percent / 200.0;
    }

    public static float hardnessOf(Block block) {
        if (block == null) {
            return -1.0f;
        }
        Material type = block.getType();
        Float mapped = blockHardnessByMaterial.get(type);
        if (mapped != null) {
            return mapped.floatValue();
        }
        try {
            return type.getHardness();
        } catch (Throwable ignored) {
            return -1.0f;
        }
    }

    static {
        double motion = VANILLA_JUMP_MOTION;
        for (int airTick = 1; airTick <= 20; ++airTick) {
            expectedJumpMotionByAirTick.put(Integer.valueOf(airTick), Double.valueOf(motion));
            knownJumpMotions.add(Double.valueOf(motion));
            motion = (motion - 0.08) * 0.9800000190734863;
        }
        knownJumpMotions.add(Double.valueOf(0.33319999363422365));
        knownJumpMotions.add(Double.valueOf(0.24813599859094587));
        knownJumpMotions.add(Double.valueOf(0.16477328182606651));
        knownJumpMotions.add(Double.valueOf(0.08307781780646721));
        knownJumpMotions.add(Double.valueOf(-0.0030162615090425808));
        for (int percent = 0; percent <= 200; ++percent) {
            sensitivityByPercent.put(Integer.valueOf(percent), Double.valueOf((double) percent / 200.0));
        }
    }
}
