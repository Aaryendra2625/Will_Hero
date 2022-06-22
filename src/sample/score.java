package sample;

import java.io.Serializable;

public class score implements Serializable {
    String playerName;
    int score;
    int coins;
    public score(String playerName, int score, int coins){
        this.score = score;
        this.playerName = playerName;
        this.coins = coins;
    }
}
