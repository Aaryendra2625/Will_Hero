package sample;

import javafx.scene.image.ImageView;
import javafx.scene.shape.Shape;

import java.io.Serializable;

public class gameObjects implements Serializable {
    double last_time = 0;
    boolean moving = false;
    protected double x_speed, y_speed,x_acceleration, y_acceleration;
    protected ImageView object;
    private boolean moving_coin = false;
    public gameObjects(ImageView object){
        this.object = object;
        this.x_speed = 0;
        this.y_speed = 0;
        this.x_acceleration = 0;
        this.y_acceleration = 0;
    }
    public double getX_Speed(){
        return this.x_speed;
    }
    public void setX_speed(double speed){
        this.x_speed = speed;
    }
    public double getY_acceleration(){
        return this.y_acceleration;
    }
    public double getY_speed(){
        return this.y_speed;
    }
    public ImageView getObject(){
        return this.object;
    }
    public void setY_acceleration(double acceleration){
        this.y_acceleration = acceleration;
    }
    public void setY_speed(double speed){
        this.y_speed = speed;
    }
    public void setMoving(boolean moving){
        this.moving = moving;
    }
    public boolean getMoving(){
        return this.moving;
    }
    public void setLast_time(double last_time){
        this.last_time = last_time;
    }
    public double getLast_time(){
        return this.last_time;
    }
    public void setMoving_coin(){
        moving_coin = true;
    }

    public boolean isMoving_coin() {
        return moving_coin;
    }
}
