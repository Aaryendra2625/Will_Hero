package sample;

public class Player {
    gameObjects willHero, curr_weapon;  // helmet
    int coins_collected, curr_score;
    String name;

    public Player(String name){
        this.name = name;
        this.coins_collected = 0;
        this.curr_score = 0;
    }
    public void setHelmet(gameObjects willHero){
        this.willHero = willHero;
    }
}
