package me.Khagana.SmashMC.Listeners;

import me.Khagana.SmashMC.GameManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;

public class DashListener implements Listener {
    Map<String, Long> cooldowns = new HashMap<>();

    @EventHandler
    public void DashEvent(PlayerInteractEvent e){
        if(e.getItem().equals(GameManager.getInstance().getDash())){
            Player p = e.getPlayer();
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (cooldowns.containsKey(p.getDisplayName()) && cooldowns.get(p.getDisplayName())>System.currentTimeMillis()){
                        long timeLeft = (cooldowns.get(p.getName()) - System.currentTimeMillis()) / 1000;
                        p.sendMessage(ChatColor.GOLD + "This ability will be ready in " + timeLeft + " second(s)");
                        return;
                }
                cooldowns.put(p.getDisplayName(), System.currentTimeMillis()+7000);
                p.setVelocity(p.getLocation().getDirection().multiply(2).setY(1 ));
            }
        }
    }
}
