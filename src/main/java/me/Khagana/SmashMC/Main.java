package me.Khagana.SmashMC;

import me.Khagana.SmashMC.Listeners.*;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        List<World> worlds = getServer().getWorlds();
        for (World w : worlds){
            w.setGameRuleValue("doDaylightCycle", "false");
            w.setGameRuleValue("doMobSpawning", "false");
        }
        GameManager.getInstance().setPlugin(this);
        this.getCommand("sm").setExecutor(new SmashMCCommands());
        this.getServer().getPluginManager().registerEvents(new DashListener(), this);
        this.getServer().getPluginManager().registerEvents(new OnHitListener(), this);
        this.getServer().getPluginManager().registerEvents(new OnDamageListener(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerDeathListener(), this);
        this.getServer().getPluginManager().registerEvents(new OnPlayerRespawnListener(), this);
    }

    @Override
    public void onDisable() {

    }
}
