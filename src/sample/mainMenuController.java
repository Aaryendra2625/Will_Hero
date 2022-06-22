package sample;
//1234
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.crypto.spec.PSource;

public class mainMenuController {
    private boolean on_off_other_scene;
    private Stage main_stage;
    private Scene other_scene;
    @FXML
    Group creatures;
    @FXML
    private ImageView sound_on,sound_off;

    @FXML
    private AnchorPane mainMenuAnchorpane;
    @FXML
    TextField name;
    private MediaPlayer mediaPlayer;
    private boolean currently_on = true;

    public void setMediaPlayer(MediaPlayer mediaPlayer){
        this.mediaPlayer = mediaPlayer;
    }
    public void sound_on_off(){

        if(currently_on){
            mediaPlayer.pause();
            sound_on.setOpacity(0);
            sound_off.setOpacity(1);
        }else{
            mediaPlayer.play();
            sound_on.setOpacity(1);
            sound_off.setOpacity(0);
        }

        currently_on = !currently_on;
    }
    public void getStage(Stage primary_stage, Scene scene){
        this.main_stage = primary_stage;
        this.other_scene = scene;

    }
    public AnchorPane getAnchorpane(){
        return this.mainMenuAnchorpane;
    }
    public void start(){
        this.main_stage.setScene(other_scene);
    }
    public void exit(){
        System.exit(0);
    }
    public void move_the_creatures(){
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
//                System.out.println(creatures.getLayoutX());
                if(creatures.getLayoutX()==1990 || creatures.getLayoutX()==0){
                    creatures.setRotate(creatures.getRotate() + 180);
                }
                if(creatures.getRotate()%360==0){
                    creatures.setLayoutX(creatures.getLayoutX()+5);
                }
                else{
                    creatures.setLayoutX(creatures.getLayoutX()-5);
                }
            }
        };
        animationTimer.start();


    }
}
