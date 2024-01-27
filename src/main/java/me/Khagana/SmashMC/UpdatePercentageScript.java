package me.Khagana.SmashMC;

import org.bukkit.scheduler.BukkitRunnable;

public class UpdatePercentageScript extends BukkitRunnable {
    int i =0;
    @Override
    public void run() {
        for (SmashPlayer p : GameManager.getInstance().getPlayerMap().values()){
            if (p.isAlive()) {
                p.updatePercentage();
            }
        }
    }
}
