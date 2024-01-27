package me.Khagana.SmashMC.Listeners;

import me.Khagana.SmashMC.GameManager;
import me.Khagana.SmashMC.ItemBuilder;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.ThreadLocalRandom;

public class OnPlayerRespawnListener implements Listener {
    @EventHandler
    public void onPlayerRespawnEvent(PlayerRespawnEvent e){
        if (GameManager.getInstance().isGameStatus()) {
            Player p = e.getPlayer();
            if (GameManager.getInstance().getPlayerMap().get(p).isAlive()) {
                int respawnPointNumber = ThreadLocalRandom.current().nextInt(0, GameManager.getInstance().getRespawnPoints().size());
                e.setRespawnLocation(GameManager.getInstance().getRespawnPoints().get(respawnPointNumber));
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 60, 1));
                p.setMaxHealth(GameManager.getInstance().getPlayerMap().get(e.getPlayer()).getRemainingLives()*2);
                p.getInventory().clear();
                p.getInventory().setItem(0, GameManager.getInstance().getStikulator());
                p.getInventory().setItem(1, GameManager.getInstance().getDash());
                GameManager.getInstance().getPlayerMap().get(p).resetPercentage();
            } else {
                p.setGameMode(GameMode.SPECTATOR);
            }
        }
    }
}
