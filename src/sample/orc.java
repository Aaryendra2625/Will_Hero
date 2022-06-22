package sample;

import javafx.scene.image.ImageView;

public class orc extends gameObjects{
    private double jumping_height;
//    private boolean wont_die_from_one_shot = false;
    private int hits_can_take;
    public orc(ImageView imageView,int hits_can_take){
        super(imageView);
        this.y_acceleration = 20;
        this.y_speed = -205;
        this.hits_can_take = hits_can_take;
    }

    public int getHits_can_take() {
        return hits_can_take;
    }

    public void setHits_can_take(int hits_can_take) {
        this.hits_can_take = hits_can_take;
    }

    public void setJumping_height(double jumping_height){
        this.jumping_height = jumping_height;
    }
    public double getJumping_height(){
        return this.jumping_height;
    }

}
