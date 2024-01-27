package me.Khagana.SmashMC;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SmashMCCommands implements TabExecutor {

    private List<String> subCmd = new ArrayList<>(Arrays.asList("team", "game", "config", "set"));
    private List<String> subCmdTeam = new ArrayList<>(Arrays.asList("create", "add"));
    private List<String> subCmdGame = new ArrayList<>(Arrays.asList("start"));
    private List<String> subCmdConfig = new ArrayList<>(Arrays.asList("maxLifes", "teamSize", "percentagePerHitMul", "percentagePerSecondMul"));
    private List<String> subCmdSet = new ArrayList<>(Arrays.asList("respawn"));

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("sm")){
            if (args.length == 0){
                help(sender);
                return true;
            } else {
                switch (args[0].toLowerCase()){
                    case "team" :
                        team(sender, args);
                        return true;
                    case "game" :
                        game(sender, args);
                        return true;
                    case "config" :
                        config(sender, args);
                        return true;
                    case "set" :
                        set(sender, args);
                        return true;
                    default :
                        help(sender);
                        return true;
                }
            }
        }
        return false;
    }

    public void help(CommandSender sender){
        sender.sendMessage(ChatColor.GREEN + "usage: \n" +
                "    /sm team: manage team\n" +
                "    /sm game: manage game\n" +
                "    /sm set: manage respawn point\n" +
                "    /sm help: display this");
    }

    public void team(CommandSender sender, String[] args){
        if (args.length==1){
            helpTeam(sender);
        } else switch (args[1].toLowerCase()){ // example : /sm team create ___
            case "create":
                if (args.length==3){
                    Team team = GameManager.getInstance().getTeam(args[2]);
                    if (team==null){
                        team = new Team(args[2]);
                        GameManager.getInstance().addTeam(team);
                        sender.sendMessage(ChatColor.GREEN + "Team "+args[2] + " created");
                    }
                    else {
                        sender.sendMessage(ChatColor.RED + "The team "+args[2] + " already exists");
                    }
                } else {
                    helpTeam(sender);
                }
                break;
            case "add":
                if (args.length==4){
                    Player player = GameManager.getInstance().getPlayer(args[2]);
                    Team team = GameManager.getInstance().getTeam(args[3]);
                    if (player==null){
                        helpTeam(sender);
                        sender.sendMessage(ChatColor.RED + "No player called " + args[2]);
                    } else if (team==null){
                        helpTeam(sender);
                        sender.sendMessage(ChatColor.RED + "No team called " + args[3]);
                    } else {
                        if(GameManager.getInstance().getPlayerMap().containsKey(player)){
                            GameManager.getInstance().getPlayerMap().get(player).changeTeam(team);
                            sender.sendMessage(ChatColor.GREEN +args[2]+" added to the team "+args[3]);
                        } else{
                            SmashPlayer smashPlayer = new SmashPlayer(team, player);
                            GameManager.getInstance().addPlayer(player, smashPlayer);
                            sender.sendMessage(ChatColor.GREEN + "Player "+args[2]+" added to the team "+args[3]);
                        }
                    }
                }else {
                    helpTeam(sender);
                }
                break;
            default:
                helpTeam(sender);
        }
    }

    public void helpTeam(CommandSender sender){
        sender.sendMessage(ChatColor.GREEN + "usage: \n" +
                "    /sm team create <name>: create team\n" +
                "    /sm team add <player> <team>: add a player to a team\n" +
                "    /sm team: display this");

    }

    public void game(CommandSender sender, String[] args){
        if (args.length==2 && args[1].equalsIgnoreCase("start")){
            GameManager.getInstance().startGame(sender);
        } else {
            helpGame(sender);
        }
    }

    public void helpGame(CommandSender sender){
        sender.sendMessage(ChatColor.GREEN + "usage: \n" +
                "    /sm game start: start the game\n" +
                "    /sm game: display this");
    }

    public void config(CommandSender sender, String[] args){
        if (args.length==3){
            switch (args[1].toLowerCase()){
                case "percentageperhit":
                    if (isInteger(args[2])){
                        GameManager.getInstance().getConfig().setPercentagePerHitMul(Integer.parseInt(args[2]));
                        sender.sendMessage(ChatColor.GREEN + "Percentage per hit change to "+args[2]);
                    } else {
                        helpConfig(sender);
                    }
                    break;
                case "percentagepersecond":
                    if (isInteger(args[2])){
                        GameManager.getInstance().getConfig().setPercentagePerSecondMul(Integer.parseInt(args[2]));
                        sender.sendMessage(ChatColor.GREEN + "Percentage per second change to "+args[2]);
                    } else {
                        helpConfig(sender);
                    }
                    break;
                case "livesnum":
                    if (isInteger(args[2])){
                        GameManager.getInstance().getConfig().setLivesNum(Integer.parseInt(args[2]));
                        sender.sendMessage(ChatColor.GREEN + "Number of lives change to "+args[2]);
                    } else {
                        helpConfig(sender);
                    }
                    break;
                default:
                    helpConfig(sender);
            }
        }
        helpConfig(sender);
    }

    public void helpConfig(CommandSender sender){
        sender.sendMessage(ChatColor.GREEN + "usage: \n" +
                "    /sm config maxLifes <value>: modify number of lifes\n" +
                "    /sm config teamSize <value>: modify teams' size" +
                "    /sm config percentagePerHitMul <value>: modify the percentage added each hit" +
                "    /sm config percentagePerSecondMul <value>: modify the percentage added each second");
    }

    public void set(CommandSender sender, String[] args){
        if (sender instanceof Player && args.length==2 && args[1].equalsIgnoreCase("respawn")){
            GameManager.getInstance().addRespawnPoint(((Player) sender).getLocation());
            sender.sendMessage(ChatColor.GREEN + "Set respawn location");
        } else {
            helpSet(sender);
        }
    }

    public void helpSet(CommandSender sender){
        sender.sendMessage(ChatColor.GREEN + "usage: \n" +
                "    /sm set respawn: set the current position as a respawn point\n");
    }

    public boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        switch (args.length){
            case 1:
                return StringUtil.copyPartialMatches(args[0].toLowerCase(), subCmd, new ArrayList<>());
            case 2:
                switch (args[0].toLowerCase()){
                    case "team":
                        return StringUtil.copyPartialMatches(args[1], subCmdTeam, new ArrayList<>());
                    case "game":
                        return StringUtil.copyPartialMatches(args[1], subCmdGame, new ArrayList<>());
                    case "config":
                        return StringUtil.copyPartialMatches(args[1], subCmdConfig, new ArrayList<>());
                    case "set":
                        return StringUtil.copyPartialMatches(args[1], subCmdSet, new ArrayList<>());
                }
            case 3:
                switch (args[0].toLowerCase()){
                    case "team":
                        switch (args[1].toLowerCase()){
                            case "add":
                                return StringUtil.copyPartialMatches(args[2], Bukkit.getOnlinePlayers().stream().map(Player::getDisplayName).collect(Collectors.toList()), new ArrayList<>());

                        }
                }
            case 4:
                switch (args[0].toLowerCase()){
                    case "team":
                        switch (args[1].toLowerCase()){
                            case "add":
                                return StringUtil.copyPartialMatches(args[2], GameManager.getInstance().getTeams().stream().map(Team::getName).collect(Collectors.toList()), new ArrayList<>());

                        }
                }
        }
        return Collections.emptyList();
    }
}
