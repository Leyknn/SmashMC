package me.Khagana.SmashMC;


import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SmashPlayer {

    @Getter @Setter private int remainingLives;
    private Team team;

    private boolean isAlive;

    private float percentage;

    @Getter private final Player player;

    public SmashPlayer(Team team, Player p){
        this.team = team;
        team.addMember(p);
        this.isAlive = true;
        this.percentage = (float) 0.5;
        this.player=p;
    }

    public void onDeath(){
        remainingLives -=1;
        if (remainingLives == 0){
            isAlive=false;
            team.eliminatePlayer(this);
        }
    }

    public float kbMultiplayer(){
        return this.percentage;
    }

    public void getHit(){
        percentage *=  GameManager.getInstance().getConfig().getPercentagePerHitMul();
        displayPercentage();
    }

    public void resetPercentage(){percentage=0;}

    public void updatePercentage(){
        percentage *= GameManager.getInstance().getConfig().getPercentagePerSecondMul();
        displayPercentage();
    }

    private void displayPercentage(){player.setLevel((int) ((percentage-0.50)*100));}

    public boolean isAlive(){
        return isAlive;
    }

    public void setIsAlive(){isAlive = true;}

    public boolean canSwitchTeam(Team newTeam){
        if (GameManager.getInstance().isGameStatus()){
            player.sendMessage(ChatColor.RED + "You can't change team while game is in progress");
            return false;
        }
        return true;
    }

    public void changeTeam(Team newTeam){
        if (canSwitchTeam(newTeam)){
            team.removeMember(player);
            newTeam.addMember(player);
            team=newTeam;
        }
    }

}
