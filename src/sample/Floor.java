// random
package sample;

import javafx.scene.image.ImageView;

public class Floor {
    ImageView imageView;
    boolean unreachable = true;
    public Floor(ImageView imageView){
        this.imageView = imageView;
    }
    public double getLayoutX(){
        return this.imageView.getLayoutX();
    }
    public double getLayoutY(){
        return this.imageView.getLayoutY();
    }
    public double getWidth() {
        return this.imageView.getFitWidth();
    }
    public void setUnreachable(boolean unreachable){
        this.unreachable = unreachable;
    }
    public boolean getUnreachable(){
        return this.unreachable;
    }
    public double getLeftCoordinate(){
        return this.getLayoutX();
    }
    public double getRightCoordinate(){
        return this.getLayoutX() + this.getWidth();
    }
    public double getHeight(){
        return this.getLayoutY();
    }

}

