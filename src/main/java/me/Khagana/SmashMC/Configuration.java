package me.Khagana.SmashMC;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Configuration {
    private float percentagePerHitMul;
    private float percentagePerSecondMul;
    private int LivesNum;


    public Configuration(){
        percentagePerHitMul = (float) 1.1;
        percentagePerSecondMul = (float) 1.005;
        LivesNum = 3;
    }
}
