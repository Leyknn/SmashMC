package me.Khagana.SmashMC;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class GameManager {
    private static GameManager instance;
    private GameManager(){
        playerMap = new HashMap<>();
        teams = new ArrayList<>();
        remainingTeams = new ArrayList<>();
        winners = null;
        config = new Configuration();
        respawnPoints = new ArrayList<>();
        gameStatus = false;
        stikulator = new ItemBuilder(Material.STICK).displayname(ChatColor.BOLD + "" + ChatColor.AQUA + "Stickulator").glow().build();
        dash = new ItemBuilder(Material.FEATHER).displayname(ChatColor.BOLD + "" + ChatColor.AQUA + "Dash").glow().build();
        scripts = new ArrayList<>();
    }

    public static GameManager getInstance(){
        if (instance == null){
            instance = new GameManager();
        }
        return instance;
    }

    @Getter private Map<Player, SmashPlayer> playerMap;
    @Getter private List<Team> teams;
    @Getter private List<Team> remainingTeams;
    @Getter private Team winners;
    @Getter private Configuration config;
    @Getter private List<Location> respawnPoints;
    @Getter private boolean gameStatus; // false = not started, true  = started
    @Getter private ItemStack stikulator;
    @Getter private ItemStack dash;
    @Setter private Plugin plugin;
    private List<BukkitRunnable> scripts;

    public void eliminateTeam(Team team){
        remainingTeams.remove(team);
        if (remainingTeams.size()==1){
            setWinners(remainingTeams.get(0));
        }
    }

    public void setWinners(Team winners) {
        this.winners = winners;
        for (Player p : playerMap.keySet()){
            p.setGameMode(GameMode.SPECTATOR);
            p.sendTitle(getWinTitle(), getWinSubTitle());
            gameStatus=false;
        }
        for (BukkitRunnable s : scripts){
            if (s instanceof UpdatePercentageScript){
                s.cancel();
                scripts.remove(s);
                break;
            }
        }
    }

    public String getWinTitle(){
        return ChatColor.RED + winners.getName() + " wins the game !";
    }

    public String getWinSubTitle(){
        List<Player> winnersList = winners.getPlayers();
        String playersList = "";
        for (int i=0; i<winnersList.size(); i++){
            if (i!= winnersList.size()-1){
                playersList+=winnersList.get(i).getName() + ", ";
            } else {
                playersList+=winnersList.get(i).getName() + " ";
            }
        }
        return ChatColor.RED + "" + ChatColor.ITALIC + "Congrats to " + playersList + "!";
    }

    public void startGame(CommandSender sender){
        for (Player p : Bukkit.getOnlinePlayers()){
            if (!playerMap.containsKey(p)){
                if (sender instanceof Player) {
                    ((Player) sender).sendTitle("",ChatColor.RED + "" + ChatColor.BOLD + "All players must be in a team");
                } else {
                    sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "All players must be in a team");
                }
                return;
            }
        }
        if (respawnPoints.isEmpty()){
            if (sender instanceof Player) {
                ((Player) sender).sendTitle("",ChatColor.RED + "" + ChatColor.BOLD + "Must have at least one respawn point");
            } else {
                sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Must have at least one respawn point");
            }
            return;
        }
        gameStatus = true;
        for (Player p : Bukkit.getOnlinePlayers()){
            int respawnPointNumber = ThreadLocalRandom.current().nextInt(0, GameManager.getInstance().getRespawnPoints().size());
            p.setGameMode(GameMode.ADVENTURE);
            p.teleport(GameManager.getInstance().getRespawnPoints().get(respawnPointNumber));
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 60, 1));
            p.setMaxHealth(config.getLivesNum()*2);
            playerMap.get(p).setIsAlive();
            playerMap.get(p).setRemainingLives(config.getLivesNum());
            p.getInventory().clear();
            p.getInventory().setItem(0, stikulator);
            p.getInventory().setItem(1, dash);
        }
        BukkitRunnable script = new UpdatePercentageScript();
        script.runTaskTimer(plugin, 0, 20);
        scripts.add(script);
    }

    public Team getTeam(String name){
        for (Team t : teams){
            if (t.getName().equalsIgnoreCase(name)){
                return t;
            }
        }
        return null;
    }

    public void addTeam(Team team){
        teams.add(team);
        remainingTeams.add(team);
    }

    public Player getPlayer(String name){
        for (Player p : Bukkit.getOnlinePlayers()){
            if (p.getName().equalsIgnoreCase(name)){
                return p;
            }
        }
        return null;
    }

    public void addPlayer(Player player, SmashPlayer smashPlayer){
        this.playerMap.put(player, smashPlayer);
    }

    public void addRespawnPoint(Location loc){
        respawnPoints.add(loc);
    }
}
