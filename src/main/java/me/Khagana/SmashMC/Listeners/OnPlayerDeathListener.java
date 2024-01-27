package me.Khagana.SmashMC.Listeners;

import me.Khagana.SmashMC.GameManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class OnPlayerDeathListener implements Listener {
    @EventHandler
    public void OnPlayerDeathEvent(PlayerDeathEvent e){
        if (GameManager.getInstance().isGameStatus()) {
            Player player = (Player) e.getEntity();
            GameManager.getInstance().getPlayerMap().get(player).onDeath();
        }
    }
}
