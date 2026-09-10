package me.frep.vulcan.spigot.check.impl.movement.antilevitation;

import com.github.retrooper.packetevents.event.PacketEvent;
import me.frep.vulcan.spigot.PlayerData;
import me.frep.vulcan.spigot.ExemptType;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(name = "Anti Levitation", type = 'A', complexType = "Ignore", description = "Ignored Levitation effect.")
public class AntiLevitationA extends AbstractCheck {
    public AntiLevitationA(PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void handle(PacketEvent event, long timestamp) {
        if (!this.isFlyingPacket(event)) {
            return;
        }
        int amplifier = this.playerData.getStatusTracker().getLevitationAmplifier();
        if (amplifier < 0) {
            return;
        }
        if (this.isExempt(
                ExemptType.SCAFFOLDING, ExemptType.VOID, ExemptType.COLLIDING_VERTICALLY, ExemptType.TRAPDOOR,
                ExemptType.BED, ExemptType.SLIME, ExemptType.HONEY, ExemptType.VELOCITY, ExemptType.RIPTIDE,
                ExemptType.JOINED, ExemptType.SWIMMING, ExemptType.CHUNK, ExemptType.JOINED_CHUNK_LOAD, ExemptType.WEB,
                ExemptType.SLEEPING, ExemptType.DEATH, ExemptType.FROZEN, ExemptType.BLOCK_PLACE, ExemptType.BLOCK_BREAK,
                ExemptType.FULLY_STUCK, ExemptType.PARTIALLY_STUCK, ExemptType.WORLD_CHANGE, ExemptType.VEHICLE,
                ExemptType.BOAT, ExemptType.GLIDING, ExemptType.SLOW_FALLING, ExemptType.BUBBLE_COLUMN,
                ExemptType.SPECTATOR, ExemptType.DOLPHINS_GRACE, ExemptType.SHULKER_BOX, ExemptType.FENCE,
                ExemptType.FENCE_GATE, ExemptType.STAIRS)) {
            return;
        }
        double deltaY = this.playerData.getBlockBreakTracker().getDeltaY();
        boolean clientOnGround = this.playerData.getBlockBreakTracker().isClientOnGround();
        this.fail("deltaY=" + deltaY + " onGround=" + clientOnGround + " amplifier=" + amplifier);
        this.applyBufferDecay();
    }

    @Override
    public void handleEvent(Event event, long timestamp) {
    }
}
