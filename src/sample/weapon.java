package sample;

import javafx.scene.image.ImageView;

public class weapon extends gameObjects implements Cloneable{
    private String weapon_name;
    private double range, acceleration;
    private int quantity;
    public weapon(String weapon_name, double x_speed, double range, double acceleration, ImageView imageView) {
        super(imageView);
        this.x_speed = x_speed;
        this.range = range;
        this.acceleration = acceleration;
        this.weapon_name = weapon_name;
        this.quantity = 1;
    }

    public String getWeapon_name(){
        return this.weapon_name;
    }
    public void setRange(double range){
        this.range = range;
    }
    public void setAcceleration(double acceleration){
        this.acceleration = acceleration;
    }
    public double getRange(){
        return this.range;
    }
    public double getAcceleration(){
        return this.acceleration;
    }
}
