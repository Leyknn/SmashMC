package me.Khagana.SmashMC.Listeners;

import me.Khagana.SmashMC.GameManager;
import me.Khagana.SmashMC.SmashPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class OnHitListener implements Listener {

    @EventHandler
    public void onHitEvent(EntityDamageByEntityEvent e){
        if(GameManager.getInstance().isGameStatus()) {
            if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
                Player player = (Player) e.getEntity();
                if (!player.hasPotionEffect(PotionEffectType.SLOW_DIGGING)) {
                    SmashPlayer smashPlayer = GameManager.getInstance().getPlayerMap().get(player);
                    player.setVelocity(e.getDamager().getLocation().getDirection().multiply(smashPlayer.kbMultiplayer()).setY(10));
                    smashPlayer.getHit();
                } else {
                    e.setCancelled(true);
                }
            }
        }
    }
}
