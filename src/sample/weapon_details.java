package sample;

import java.io.Serializable;

public class weapon_details implements Serializable {
    String weapon_name;
    double x_speed, range, acceleration;
    String image;
    double layoutX, layoutY, width, height;
    boolean got_weapon = false;
    public weapon_details(String weapon_name, double x_speed, double range, double acceleration, String image,
                          double layoutX, double layoutY, double width, double height,boolean got_weapon){
        this.weapon_name = weapon_name;
        this.x_speed = x_speed;
        this.range = range;
        this.acceleration = acceleration;
        this.image = image;
        this.layoutX = layoutX;
        this.layoutY = layoutY;
        this.width = width;
        this.height = height;
        this.got_weapon = got_weapon;
    }
}
