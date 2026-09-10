package me.frep.vulcan.spigot.check.impl.combat.fastbow;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ServerCompatibility;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.event.Event;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;

@CheckInfo(name = "Fast Bow", type = 'A', complexType = "Limit", description = "Shooting a bow too quickly")
public class FastBowA extends AbstractCheck {
    private long lastShotTime;

    public FastBowA(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
        if (!(event instanceof ProjectileLaunchEvent)) {
            return;
        }
        if (this.playerData.getPlayer() == null) {
            return;
        }
        ProjectileLaunchEvent launch = (ProjectileLaunchEvent) event;
        if (!(launch.getEntity() instanceof Arrow)) {
            return;
        }
        Arrow arrow = (Arrow) launch.getEntity();
        ItemStack main = this.playerData.getPlayer().getItemInHand();
        if (main == null || main.getType() != Material.BOW && (!ServerCompatibility.isServer1_9OrNewer()
                || main.getType() != Material.CROSSBOW)) {
            return;
        }
        if (ServerCompatibility.isServer1_9OrNewer()) {
            int multiShot = this.playerData.getPlayer().getInventory().getItemInMainHand()
                    .getEnchantmentLevel(Enchantment.MULTISHOT);
            if (multiShot == 0) {
                multiShot = this.playerData.getPlayer().getInventory().getItemInOffHand()
                        .getEnchantmentLevel(Enchantment.MULTISHOT);
            }
            if (multiShot > 0 && arrow.getVelocity().length() <= 2.0) {
                return;
            }
        }
        long delay = System.currentTimeMillis() - this.lastShotTime;
        this.lastShotTime = System.currentTimeMillis();
        if (delay >= 750L) {
            return;
        }
        if (this.increaseBuffer(1.0) <= this.maxBufferConfig) {
            return;
        }
        this.fail("length=" + arrow.getVelocity().length() + "delay=" + delay);
        this.applyBufferDecay();
    }
}
