package me.Khagana.SmashMC.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class OnDamageListener implements Listener {
    @EventHandler
    public void OnDamageEvent(EntityDamageEvent e){
        if (!(e.getCause()==EntityDamageEvent.DamageCause.VOID)) {
            if (e.getEntity() instanceof Player) {
                e.setDamage(0);
            }
        }
    }
}
