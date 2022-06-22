//// random text
//
//package sample;
//
//import javafx.animation.*;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Group;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//
//import java.io.IOException;
//import java.util.*;
//
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.AnchorPane;
//import javafx.scene.media.MediaPlayer;
//import javafx.util.Duration;
//import javafx.scene.Node;
//
//public class Controller {
//    private pauseMenuController pauseMenuController;
//    private MediaPlayer mediaPlayer;
//    private ArrayList<Floor> floors;
//    private AnchorPane pauseMenuAnchorPane;
//    private Will_Hero willHero;
//    private long last = 0;
//    private double min_height_for_not_falling = 0;
//    private Timeline timeline1,timeline2,timeline3,timeline4;
//    int curr_floor_no = 0;
//    private int steps = 0;
//    private boolean has_started = false;
//    private boolean currently_on = true;
//    @FXML
//    private AnchorPane anchorPane;
//    @FXML
//    private Label will_hero_label1,will_hero_label2,tap,twice,movements;
//    @FXML
//    private Group floor_group,clouds_group,orc_group,everything;
//    @FXML
//    private Button Start_button;
//    @FXML
//    private ImageView willHeropng,sound_on,sound_off,background;
//
//    // get all nodes of a group in a list
////    List<Node> list = floor_group.getChildren().stream().toList();
////        for(int i = 0;i<list.size();i++){
////        System.out.println(list.get(i).getLayoutX()+" "+list.get(i).getLayoutY());
////    }
//    public void setPauseMenuController(pauseMenuController pauseMenuController){
//        this.pauseMenuController = pauseMenuController;
//    }
//    public void jump(){
//        timeline1 = new Timeline();
//        double curr_pos = willHeropng.getLayoutY();
////        AnimationTimer player_jump = new AnimationTimer() {
////            @Override
////            public void handle(long l) {
////                if(willHero.getLayoutY()==255 || willHero.getLayoutY()==385){
////                    willHero.setYSpeed(-1* willHero.getYSpeed());
////                }
////
////                willHero.setLayoutY(willHero.getLayoutY() + willHero.getYSpeed());
//////                System.out.println(willHero.getLayoutY()+" "+willHero.getYSpeed());
////
////                last = l;
////            }
////        };
////        player_jump.start();
//
//        timeline1.setAutoReverse(true);
//        timeline1.setCycleCount(Timeline.INDEFINITE);
//        timeline1.getKeyFrames().add(new KeyFrame(
//                Duration.seconds(0.7),new KeyValue(willHero.getLayoutYProperty(),curr_pos-130, Interpolator.LINEAR)
//        ));
//        timeline1.play();
//
//        timeline2 = new Timeline();
//        timeline2.getKeyFrames().add(new KeyFrame(
//                Duration.seconds(5),new KeyValue(willHero.getLayoutYProperty(),1000, Interpolator.LINEAR)
//        ));
//    }
//    public void sound_on_off(){
//
//        if(currently_on){
//            mediaPlayer.pause();
//            sound_on.setOpacity(0);
//            sound_off.setOpacity(1);
//        }else{
//            mediaPlayer.play();
//            sound_on.setOpacity(1);
//            sound_off.setOpacity(0);
//        }
//
//        currently_on = !currently_on;
//    }
//
//    public void jump_orc(){
//        timeline3 = new Timeline();
//        timeline3.setAutoReverse(true);
//        timeline3.setCycleCount(Timeline.INDEFINITE);
//        timeline3.getKeyFrames().add(new KeyFrame(
//                Duration.seconds(0.7),new KeyValue(orc_group.layoutYProperty(),-130, Interpolator.LINEAR)
//        ));
//        timeline3.play();
//        resume();
//    }
//
//    public void cloud_movement(){
//        Timeline timeline = new Timeline();
//        timeline.setAutoReverse(true);
//        timeline.setCycleCount(Timeline.INDEFINITE);
//        timeline.getKeyFrames().add(new KeyFrame(
//                Duration.seconds(15),new KeyValue(clouds_group.layoutXProperty(),-100, Interpolator.LINEAR)
//        ));
//
//        timeline.play();
//    }
//    public void click_start(ActionEvent e){
//        has_started = true;
//        anchorPane.getChildren().removeAll(Start_button,will_hero_label1,will_hero_label2);
//        movements.setOpacity(1);
//        tap.setOpacity(1);
//        twice.setOpacity(1);
//    }
//    public boolean getHasStarted(){
//        return this.has_started;
//    }
//    public void right(){
//        if(!has_started) return;
//        // take care of the collision
//        willHero.incrementMovement();
//        if(willHero.getLayoutY()>385) return;
//        movements.setText(willHero.getMovements()+"");
//        steps++;
//        double curr_position = willHeropng.getLayoutX()+140;
////        willHeropng.setLayoutX(curr_position);
//        Timeline timeline5 = new Timeline();
//        timeline5.getKeyFrames().add(new KeyFrame(
//                Duration.seconds(0.08),new KeyValue(willHero.getLayoutXProperty(),curr_position, Interpolator.LINEAR)
//        ));
//        timeline5.play();
//
//        for(int i = curr_floor_no;i<floors.size();i++){
//            Floor curr_floor = floors.get(i);
//            if(curr_floor.getUnreachable()){
//                if(curr_position<curr_floor.getRightCoordinate() && curr_position>curr_floor.getLeftCoordinate()
//                        && willHeropng.getLayoutY()<min_height_for_not_falling){
//                    curr_floor_no = i;
//
//                    if(timeline1.getCurrentRate()==0){
//                        willHeropng.setLayoutY(385);
//                        double curr_pos = willHeropng.getLayoutY();
//
//                        timeline2.stop();
//                        timeline1.setAutoReverse(true);
//                        timeline1.setCycleCount(Timeline.INDEFINITE);
//                        timeline1.getKeyFrames().add(new KeyFrame(
//                                Duration.seconds(0.7),new KeyValue(willHeropng.layoutYProperty(),curr_pos-130, Interpolator.LINEAR)
//                        ));
//                        timeline1.play();
//
//                    }
//                    break;
//                }
//                else if(((curr_position+willHeropng.getFitWidth())<curr_floor.getRightCoordinate())
//                        && (curr_position+willHeropng.getFitWidth())>curr_floor.getLeftCoordinate()
//                        && (willHeropng.getLayoutY())<min_height_for_not_falling) {
//                    curr_floor_no = i;
//                    if(timeline1.getCurrentRate()==0){
//                        willHeropng.setLayoutY(385);
//                        double curr_pos = willHeropng.getLayoutY();
//                        timeline2.stop();
//                        timeline1.setAutoReverse(true);
//                        timeline1.setCycleCount(Timeline.INDEFINITE);
//                        timeline1.getKeyFrames().add(new KeyFrame(
//                                Duration.seconds(0.7),new KeyValue(willHeropng.layoutYProperty(),curr_pos-130, Interpolator.LINEAR)
//                        ));
//                        timeline1.play();
//                    }
//                    break;
//                }
//                else if(curr_position<curr_floor.getWidth()+curr_floor.getLayoutX()){
//                    if(timeline2.getCurrentRate()==0){
//                        timeline1.stop();
//                        timeline2 = new Timeline();
//                        timeline2.getKeyFrames().add(new KeyFrame(
//                                Duration.seconds(4),new KeyValue(willHero.getLayoutYProperty(),1000, Interpolator.LINEAR)
//                        ));
//                        timeline2.play();
//                    }
//                    return;
//                }
//            }
//        }
//
//        timeline4 = new Timeline();
//        timeline4.setCycleCount(1);
//        timeline4.getKeyFrames().add(new KeyFrame(
//                Duration.seconds(0.8),new KeyValue(everything.layoutXProperty(),steps*-140, Interpolator.LINEAR)
//        ));
//        timeline4.play();
//
//    }
//    //    public void left(){
////        double curr_position = willHeropng.getLayoutX();
////        willHeropng.setLayoutX(curr_position - 300);
////    }
//    public void setMediaPlayer(MediaPlayer mediaPlayer){
//        this.mediaPlayer = mediaPlayer;
//    }
//    public ImageView getWillHero(Will_Hero will_hero){
//        this.willHero = will_hero;
//        return this.willHeropng;
//
//    }
//    public ArrayList<Floor> getFloors(){
//        List<Node> list = floor_group.getChildren().stream().toList();
//        floors = new ArrayList<>();
//        for(int i = 0;i<list.size();i++){
//            ImageView curr = (ImageView) list.get(i);
//            Floor curr_floor = new Floor(curr);
//            if(curr.getId().contains("_")){
//                curr_floor.setUnreachable(false);
//            }
//            floors.add(curr_floor);
////            System.out.println(list.get(i).getLayoutX()+" "+list.get(i).getLayoutY());
//        }
//        min_height_for_not_falling = list.get(0).getLayoutY();
//        return floors;
//    }
//
//    public void pause() throws IOException {
////        FXMLLoader loader = new FXMLLoader(getClass().getResource("pauseMenu.fxml"));
////        loader.load();
////        pauseMenuController pauseMenuController = loader.getController();
////        pauseMenuAnchorPane = pauseMenuController.getPauseMenu();
//        pauseMenuAnchorPane = pauseMenuController.getPauseMenu();
////        int curr_size = anchorPane.getChildren().size();
//        timeline1.pause();
////        timeline4.pause();
//        timeline3.pause();
//        if(anchorPane.getChildren().contains(pauseMenuAnchorPane)) return;
//        anchorPane.getChildren().add(pauseMenuAnchorPane);
//
////        System.out.println(anchorPane.getChildren());
////        temp();
//    }
//    public AnchorPane getAnchorpane(){
//        return this.anchorPane;
//    }
//    public void resume(){
//
//        anchorPane.getChildren().remove(pauseMenuAnchorPane);
////        jump();
////        jump_orc();
//        timeline1.play();
////        timeline4.play();
//        timeline3.play();
//    }
//}
//
