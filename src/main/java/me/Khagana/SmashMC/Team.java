package me.Khagana.SmashMC;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Team {
    @Getter private List<Player> players;
    private List<Player> remainingPlayers;

    @Getter private String name;

    public Team(String name){
        this.name=name;
        this.players = new ArrayList<>();
        this.remainingPlayers = new ArrayList<>();
    }

    public boolean isEliminated(){
        return remainingPlayers.isEmpty();
    }

    public void eliminatePlayer(SmashPlayer p){
        remainingPlayers.remove(p.getPlayer());
        if (isEliminated()){
            GameManager.getInstance().eliminateTeam(this);
        }
    }

    public void addMember(Player p){
        players.add(p);
        remainingPlayers.add(p);
    }

    public void removeMember(Player p){
        players.remove(p);
        remainingPlayers.remove(p);
        if (players.isEmpty()){
            GameManager.getInstance().getTeams().remove(this);
            GameManager.getInstance().getRemainingTeams().remove(this);
        }
    }
}
