package sample;
/////
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;


import java.io.IOException;
import java.util.ArrayList;

public class pauseMenuController {
    private AnchorPane main_screen_anchorpane;
    private ArrayList<AnimationTimer> list_of_animation_timers;
    @FXML
    AnchorPane pauseMenu;

    @FXML
    Group start_new_game;

    @FXML
    Label Resume, save_game, exit_game;

    public AnchorPane getPauseMenu(){
        return this.pauseMenu;
    }
    public void resume(){
        main_screen_anchorpane.getChildren().remove(pauseMenu);
//        for(AnimationTimer animationTimer : list_of_animation_timers){
//            animationTimer.start();
//        }
    }
    public void exit(){
        System.exit(0);
    }
    public void getMainGameAnchorpane(AnchorPane anchorPane){
        this.main_screen_anchorpane = anchorPane;
    }
    public void getListOfAnimationTimers(ArrayList<AnimationTimer> list_of_animation_timers){
        this.list_of_animation_timers = list_of_animation_timers;
    }

}
