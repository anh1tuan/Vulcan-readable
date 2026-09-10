package me.frep.vulcan.spigot;

import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class BlockDigTracker {
    private BlockDigTracker() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static long getDigDurationMillis(Block block, Player player) {
        if (block == null || player == null) {
            return 0L;
        }
        float hardness = MovementConstants.hardnessOf(block);
        if (hardness < 0.0f) {
            return 0L;
        }
        ItemStack tool = player.getItemInHand();
        if (tool != null) {
            String toolName = tool.getType().toString();
            if (toolName.equalsIgnoreCase("SHEARS") || toolName.contains("SWORD")) {
                return 0L;
            }
        }
        boolean canHarvest = tool != null && !block.getDrops(tool).isEmpty();
        double ticks = hardness * (canHarvest ? 1.5 : 5.0);
        double speed = 1.0;
        if (tool != null && tool.containsEnchantment(Enchantment.DIG_SPEED)) {
            int level = tool.getEnchantmentLevel(Enchantment.DIG_SPEED);
            speed += (double) (level * level + 1);
        }
        int haste = potionAmplifier(player, PotionEffectType.FAST_DIGGING);
        if (haste > 0) {
            speed *= 1.0 + 0.2 * (double) haste;
        }
        int fatigue = potionAmplifier(player, PotionEffectType.SLOW_DIGGING);
        if (fatigue > 0) {
            speed /= Math.pow(3.0, fatigue);
        }
        if (speed <= 0.0) {
            return 0L;
        }
        return (long) ((ticks / speed) * 1000.0);
    }

    private static int potionAmplifier(Player player, PotionEffectType type) {
        if (player == null || type == null || !player.hasPotionEffect(type)) {
            return 0;
        }
        PotionEffect effect = player.getPotionEffect(type);
        return effect == null ? 0 : effect.getAmplifier() + 1;
    }
}
