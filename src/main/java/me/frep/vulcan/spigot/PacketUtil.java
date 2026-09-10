package me.frep.vulcan.spigot;

import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateAttributes;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateAttributes.PropertyModifier;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class PacketUtil {
    private static final UUID SPRINT_MODIFIER = UUID.fromString("662a6b8d-da3e-4c1c-8813-96ea6097278d");
    private static final UUID SPEED_EFFECT_MODIFIER = UUID.fromString("19156c30-b575-3f85-a869-c0a29ccaa5fa");

    private PacketUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static double computeFinalAttributeValue(WrapperPlayServerUpdateAttributes.Property property) {
        return PacketUtil.applyModifiers(property.getValue(), property.getModifiers(), false);
    }

    public static double computeWalkSpeedWithoutSprint(WrapperPlayServerUpdateAttributes.Property property) {
        return PacketUtil.applyModifiers(property.getValue(), property.getModifiers(), true);
    }

    private static double applyModifiers(double baseValue, List modifiers, boolean excludeMovementBoosts) {
        List<PropertyModifier> remaining = new ArrayList<PropertyModifier>(modifiers);
        if (excludeMovementBoosts) {
            remaining.removeIf(PacketUtil::isSprintOrSpeedModifier);
        }
        double afterAddition = baseValue;
        for (PropertyModifier modifier : remaining) {
            if (modifier.getOperation() == PropertyModifier.Operation.ADDITION) {
                afterAddition += modifier.getAmount();
            }
        }
        double result = afterAddition;
        for (PropertyModifier modifier : remaining) {
            if (modifier.getOperation() == PropertyModifier.Operation.MULTIPLY_BASE) {
                result += afterAddition * modifier.getAmount();
            }
        }
        for (PropertyModifier modifier : remaining) {
            if (modifier.getOperation() == PropertyModifier.Operation.MULTIPLY_TOTAL) {
                result *= 1.0 + modifier.getAmount();
            }
        }
        return result;
    }

    private static boolean isSprintOrSpeedModifier(PropertyModifier modifier) {
        String uuid = modifier.getUUID().toString();
        if (uuid.equals(SPRINT_MODIFIER.toString()) || uuid.equals(SPEED_EFFECT_MODIFIER.toString())) {
            return true;
        }
        return modifier.getName().toString().toLowerCase().contains("speed");
    }
}
