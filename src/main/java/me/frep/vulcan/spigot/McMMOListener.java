package me.frep.vulcan.spigot;

import com.gmail.nossr50.datatypes.skills.SuperAbilityType;
import com.gmail.nossr50.events.skills.abilities.McMMOPlayerAbilityActivateEvent;
import com.gmail.nossr50.events.skills.abilities.McMMOPlayerAbilityDeactivateEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class McMMOListener implements Listener {
    @EventHandler
    public void onBerserkActivate(McMMOPlayerAbilityActivateEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = VulcanBootstrap.INSTANCE.getHookManager().getPlayerData(player);
        if (playerData != null && event.getAbility() == SuperAbilityType.BERSERK) {
            playerData.getStatusTracker().setBerserkActive(true);
        }
    }

    @EventHandler
    public void onBerserkDeactivate(McMMOPlayerAbilityDeactivateEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = VulcanBootstrap.INSTANCE.getHookManager().getPlayerData(player);
        if (playerData != null && event.getAbility() == SuperAbilityType.BERSERK) {
            playerData.getStatusTracker().setBerserkActive(false);
        }
    }
}
