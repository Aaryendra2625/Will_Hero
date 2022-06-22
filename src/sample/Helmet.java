package sample;

import javafx.scene.image.ImageView;

public class Helmet  extends gameObjects{
    private int movements;
    private gameObjects sword,shuriken;
    public Helmet(ImageView will){
        super(will);
        this.movements = 0;
        this.y_acceleration = 40;
        this.y_speed = -305;
    }
    public void setWeapons(gameObjects sword, gameObjects shuriken){
        this.sword = sword;
        this.shuriken = shuriken;
    }
    public void incrementMovement(){
        this.movements++;
    }

}
