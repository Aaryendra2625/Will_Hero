package sample;

import java.io.Serializable;

public class Coordinates implements Serializable {
    double layout_x, layout_y, fitWidth, fitHeight;
    String image;
    boolean moving = false;
    public Coordinates(double layout_x, double layout_y, double fitWidth, double fitHeight,String image){
        this.layout_x = layout_x;
        this.layout_y = layout_y;
        this.fitWidth = fitWidth;
        this.fitHeight = fitHeight;
        this.image = image;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }
}
