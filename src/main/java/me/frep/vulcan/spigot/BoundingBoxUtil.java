package me.frep.vulcan.spigot;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;


public final class BoundingBoxUtil {
    public static List getNearbyBlocks(PlayerData playerData, int radius) {
        LinkedList<Block> blocks = new LinkedList<Block>();
        if (playerData == null || playerData.getPlayer() == null) {
            return blocks;
        }
        org.bukkit.Location location = playerData.getPlayer().getLocation();
        World world = location.getWorld();
        if (world == null) {
            return blocks;
        }
        int originX = location.getBlockX();
        int originY = location.getBlockY();
        int originZ = location.getBlockZ();
        for (int x = originX - radius; x <= originX + radius; ++x) {
            for (int y = originY - radius; y <= originY + radius; ++y) {
                for (int z = originZ - radius; z <= originZ + radius; ++z) {
                    blocks.add(world.getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }

    public static List getNearbyBlocks(World world, int x, int y, int z) {
        LinkedList<Block> blocks = new LinkedList<Block>();
        if (world == null) {
            return blocks;
        }
        for (int dx = -1; dx <= 1; ++dx) {
            for (int dy = -2; dy <= 1; ++dy) {
                for (int dz = -1; dz <= 1; ++dz) {
                    blocks.add(world.getBlockAt(x + dx, y + dy, z + dz));
                }
            }
        }
        return blocks;
    }

    public static List sampleBlocksTwoBelow(World world, int x, int y, int z) {
        LinkedList<Material> sampled = new LinkedList<Material>();
        if (world == null) {
            return sampled;
        }
        int below = y - 2;
        sampled.add(world.getBlockAt(x + 1, below, z).getType());
        sampled.add(world.getBlockAt(x - 1, below, z).getType());
        sampled.add(world.getBlockAt(x, below, z + 1).getType());
        sampled.add(world.getBlockAt(x, below, z - 1).getType());
        sampled.add(world.getBlockAt(x + 1, below, z + 1).getType());
        sampled.add(world.getBlockAt(x - 1, below, z + 1).getType());
        sampled.add(world.getBlockAt(x - 1, below, z - 1).getType());
        sampled.add(world.getBlockAt(x, below, z).getType());
        sampled.add(world.getBlockAt(x + 1, below, z - 1).getType());
        return sampled;
    }

    public static Material getBlockType(World world, int x, int y, int z) {
        if (world == null || !world.isChunkLoaded(x >> 4, z >> 4)) {
            return Material.AIR;
        }
        return world.getBlockAt(x, y, z).getType();
    }

    public static List getNearbyMaterials(World world, int x, int y, int z) {
        LinkedList<Material> materials = new LinkedList<Material>();
        if (world == null) {
            return materials;
        }
        for (int dx = -1; dx <= 1; ++dx) {
            for (int dy = -2; dy <= 1; ++dy) {
                for (int dz = -1; dz <= 1; ++dz) {
                    materials.add(BoundingBoxUtil.getBlockType(world, x + dx, y + dy, z + dz));
                }
            }
        }
        return materials;
    }

    public static boolean isScaffoldMaterial(Material material) {
        if (material == null) {
            return false;
        }
        String name = material.toString();
        return name.contains("SLAB")
                || name.contains("STAIR")
                || name.contains("FENCE")
                || name.contains("WALL")
                || name.contains("SCAFFOLD");
    }

    public static boolean isSolid(Material material) {
        return material.isSolid();
    }

    public static boolean nameContains(Material material, String token) {
        return material != null && material.toString().contains(token);
    }

    public static boolean nameEquals(Material material, String token) {
        return material != null && material.toString().equals(token);
    }

    public static boolean isClimbableMaterial(Material material) {
        return BoundingBoxUtil.nameEquals(material, "LADDER")
            || BoundingBoxUtil.nameContains(material, "VINE")
            || BoundingBoxUtil.nameEquals(material, "SCAFFOLDING");
    }

    public static boolean isLiquid(Material material) {
        return BoundingBoxUtil.nameContains(material, "LAVA")
            || BoundingBoxUtil.nameContains(material, "WATER")
            || BoundingBoxUtil.nameContains(material, "BUBBLE");
    }

    public static boolean isBedOrBedrock(Material material) {
        return BoundingBoxUtil.nameContains(material, "BED")
            || BoundingBoxUtil.nameEquals(material, "BEDROCK");
    }

    public static boolean isPath(Material material) {
        return BoundingBoxUtil.nameContains(material, "PATH");
    }

    public static boolean isDripleaf(Material material) {
        return BoundingBoxUtil.nameContains(material, "DRIP")
            && BoundingBoxUtil.nameContains(material, "LEAF");
    }

    public static boolean isHeavyCore(Material material) {
        return BoundingBoxUtil.nameEquals(material, "HEAVY_CORE");
    }

    public static boolean isDriedGhast(Material material) {
        return BoundingBoxUtil.nameEquals(material, "DRIED_GHAST");
    }

    public static boolean isCactus(Material material) {
        return BoundingBoxUtil.nameEquals(material, "CACTUS");
    }

    public static boolean isBlueIce(Material material) {
        return BoundingBoxUtil.nameEquals(material, "BLUE_ICE");
    }

    public static boolean isFrostedIce(Material material) {
        return BoundingBoxUtil.nameEquals(material, "FROSTED_ICE");
    }

    public static boolean isConduit(Material material) {
        return BoundingBoxUtil.nameEquals(material, "CONDUIT");
    }

    public static boolean isKelp(Material material) {
        return BoundingBoxUtil.nameEquals(material, "KELP")
            || BoundingBoxUtil.nameEquals(material, "KELP_PLANT")
            || BoundingBoxUtil.nameContains(material, "KELP");
    }

    public static boolean isSeagrass(Material material) {
        return BoundingBoxUtil.nameContains(material, "SEAGRASS")
            || BoundingBoxUtil.nameContains(material, "SEA_GRASS");
    }

    public static boolean isPackedIce(Material material) {
        return BoundingBoxUtil.nameEquals(material, "PACKED_ICE")
            || BoundingBoxUtil.nameEquals(material, "BLUE_ICE");
    }

    public static boolean isBell(Material material) {
        return BoundingBoxUtil.nameEquals(material, "BELL");
    }

    public static boolean isLectern(Material material) {
        return BoundingBoxUtil.nameEquals(material, "LECTERN");
    }

    public static boolean isHoneyBlock(Material material) {
        return BoundingBoxUtil.nameEquals(material, "HONEY_BLOCK");
    }

    public static boolean isSweetBerryBush(Material material) {
        return BoundingBoxUtil.nameEquals(material, "SWEET_BERRY_BUSH");
    }

    public static boolean isIceVariant(Material material) {
        return BoundingBoxUtil.nameEquals(material, "ICE")
            || BoundingBoxUtil.nameEquals(material, "PACKED_ICE")
            || BoundingBoxUtil.nameEquals(material, "BLUE_ICE")
            || BoundingBoxUtil.nameEquals(material, "FROSTED_ICE");
    }

    public static boolean isCobweb(Material material) {
        return BoundingBoxUtil.nameContains(material, "WEB") || BoundingBoxUtil.nameEquals(material, "COBWEB");
    }

    public static boolean isIce(Material material) {
        return BoundingBoxUtil.nameContains(material, "ICE");
    }

    public static boolean isSoulSand(Material material) {
        return BoundingBoxUtil.nameEquals(material, "SOUL_SAND") || BoundingBoxUtil.nameEquals(material, "SOUL_SOIL");
    }

    public static boolean isSlime(Material material) {
        return BoundingBoxUtil.nameContains(material, "SLIME");
    }

    public static boolean isHoney(Material material) {
        return BoundingBoxUtil.nameContains(material, "HONEY");
    }

    public static boolean isWall(Material material) {
        return BoundingBoxUtil.nameContains(material, "WALL")
            && !BoundingBoxUtil.nameContains(material, "SIGN")
            && !BoundingBoxUtil.nameContains(material, "BANNER")
            && !BoundingBoxUtil.nameContains(material, "TORCHFLOWER")
            && !BoundingBoxUtil.nameContains(material, "HEAD")
            && !BoundingBoxUtil.nameContains(material, "SKULL")
            && !BoundingBoxUtil.nameContains(material, "FAN");
    }

    public static boolean isGlassPane(Material material) {
        return BoundingBoxUtil.nameContains(material, "PANE")
            || BoundingBoxUtil.nameEquals(material, "THIN_GLASS")
            || BoundingBoxUtil.nameEquals(material, "IRON_BARS");
    }

    public static boolean isLilyPad(Material material) {
        return BoundingBoxUtil.nameContains(material, "LILY") || BoundingBoxUtil.nameEquals(material, "WATER_LILY");
    }

    public static boolean isCauldron(Material material) {
        return BoundingBoxUtil.nameContains(material, "CAULDRON");
    }

    public static boolean isFenceGate(Material material) {
        return BoundingBoxUtil.nameContains(material, "FENCE_GATE") || BoundingBoxUtil.nameContains(material, "GATE");
    }

    public static boolean isSnow(Material material) {
        return BoundingBoxUtil.nameContains(material, "SNOW") && !BoundingBoxUtil.nameContains(material, "SNOWMAN");
    }

    public static boolean isAnvil(Material material) {
        return BoundingBoxUtil.nameContains(material, "ANVIL");
    }

    public static boolean isHopper(Material material) {
        return BoundingBoxUtil.nameContains(material, "HOPPER");
    }

    public static boolean isDoor(Material material) {
        return BoundingBoxUtil.nameContains(material, "DOOR") && !BoundingBoxUtil.nameContains(material, "TRAP");
    }

    public static boolean isTrapdoor(Material material) {
        return BoundingBoxUtil.nameContains(material, "TRAPDOOR") || BoundingBoxUtil.nameContains(material, "TRAP_DOOR");
    }

    public static boolean isSlab(Material material) {
        return BoundingBoxUtil.nameContains(material, "SLAB") || BoundingBoxUtil.nameContains(material, "STEP");
    }

    public static boolean isStair(Material material) {
        return BoundingBoxUtil.nameContains(material, "STAIR");
    }

    public static boolean isSign(Material material) {
        return BoundingBoxUtil.nameContains(material, "SIGN");
    }

    public static boolean isFence(Material material) {
        return BoundingBoxUtil.nameContains(material, "FENCE") && !BoundingBoxUtil.nameContains(material, "GATE");
    }

    public static boolean isSeaPickle(Material material) {
        return BoundingBoxUtil.nameContains(material, "SEA_PICKLE") || BoundingBoxUtil.nameContains(material, "PICKLE");
    }

    public static boolean isClimbable(Material material) {
        return BoundingBoxUtil.nameContains(material, "LADDER")
            || BoundingBoxUtil.nameContains(material, "VINE")
            || BoundingBoxUtil.nameContains(material, "SCAFFOLD")
            || BoundingBoxUtil.nameContains(material, "WEEPING_VINES")
            || BoundingBoxUtil.nameContains(material, "TWISTING_VINES")
            || BoundingBoxUtil.nameContains(material, "CAVE_VINES");
    }

    public static boolean isCarpet(Material material) {
        return BoundingBoxUtil.nameContains(material, "CARPET") || BoundingBoxUtil.nameContains(material, "MOSS_CARPET");
    }

    public static boolean isChest(Material material) {
        return BoundingBoxUtil.nameContains(material, "CHEST") || BoundingBoxUtil.nameContains(material, "BARREL");
    }

    public static boolean isBed(Material material) {
        return BoundingBoxUtil.nameContains(material, "BED");
    }

    public static boolean isPiston(Material material) {
        return BoundingBoxUtil.nameContains(material, "PISTON");
    }

    public static boolean isShulker(Material material) {
        return BoundingBoxUtil.nameContains(material, "SHULKER");
    }

    public static boolean isCampfire(Material material) {
        return BoundingBoxUtil.nameContains(material, "CAMPFIRE");
    }

    public static boolean isBubbleColumn(Material material) {
        return BoundingBoxUtil.nameEquals(material, "BUBBLE_COLUMN");
    }

    public static boolean isSweetBerry(Material material) {
        return BoundingBoxUtil.nameContains(material, "BERRY") || BoundingBoxUtil.nameContains(material, "SWEET_BERRY_BUSH");
    }

    public static boolean isEndRod(Material material) {
        return BoundingBoxUtil.nameContains(material, "END_ROD");
    }

    public static boolean isFlowerPot(Material material) {
        return BoundingBoxUtil.nameContains(material, "FLOWER_POT") || BoundingBoxUtil.nameContains(material, "POTTED");
    }

    public static boolean isSkull(Material material) {
        return BoundingBoxUtil.nameContains(material, "SKULL") || BoundingBoxUtil.nameContains(material, "HEAD");
    }

    public static boolean isPressurePlate(Material material) {
        return BoundingBoxUtil.nameContains(material, "PRESSURE_PLATE") || BoundingBoxUtil.nameContains(material, "PRESSURE_PLATE");
    }

    public static boolean isFarmland(Material material) {
        return BoundingBoxUtil.nameContains(material, "FARMLAND");
    }

    public static boolean isCake(Material material) {
        return BoundingBoxUtil.nameContains(material, "CAKE");
    }

    public static boolean isBamboo(Material material) {
        return BoundingBoxUtil.nameContains(material, "BAMBOO");
    }

    public static boolean isScaffolding(Material material) {
        return BoundingBoxUtil.nameContains(material, "SCAFFOLD");
    }

    public static boolean isExactScaffolding(Material material) {
        return BoundingBoxUtil.nameEquals(material, "SCAFFOLDING");
    }

    public static boolean isPowderSnow(Material material) {
        return BoundingBoxUtil.nameContains(material, "POWDER_SNOW");
    }

    public static boolean isDripstone(Material material) {
        return BoundingBoxUtil.nameContains(material, "DRIPSTONE");
    }

    public static boolean isChain(Material material) {
        String name = material == null ? "" : material.toString();
        return name.contains("CHAIN") && !name.contains("CHAINMAIL");
    }

    public static boolean isTurtleEgg(Material material) {
        return BoundingBoxUtil.nameContains(material, "TURTLE_EGG");
    }

    private BoundingBoxUtil() {
        throw new UnsupportedOperationException("BoundingBoxUtil is a utility class and cannot be instantiated");
    }
}
