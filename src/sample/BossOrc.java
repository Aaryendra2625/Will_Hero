package sample;

import javafx.scene.image.ImageView;

public class BossOrc extends gameObjects{
    gameObjects weapon;
    int hits_it_can_take = 5;
    public BossOrc(ImageView object,gameObjects weapon) {
        super(object);
        this.weapon = weapon;
    }
    public void decrease_hits(){
        hits_it_can_take--;
    }
    public int getHits_it_can_take(){
        return hits_it_can_take;
    }
    public gameObjects getWeapon() {
        return weapon;
    }
}
