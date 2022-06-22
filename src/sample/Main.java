//abcd

package sample;

import javafx.animation.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.util.Duration;
import javafx.scene.media.MediaView;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Main extends Application implements Runnable {
    private gameObjects BossWeapon,BossOrc;
    private MediaPlayer mediaPlayer;
    private boolean already_revived_once = false,flag = false;
    private double circle1_opacity,circle2_opacity;
    private Player curr_player;
    private int weapon_quantity;
    private int weapon_index_for_throwing = 2;  // no actual use
    private Scene mainGameScene,mainMenuScreen;
    private boolean got_weapon = false;
    private gameObjects curr_weapon;
    private Timeline cloud_move;
    private Stage stage;
    private final Image off = new Image("sample\\sound_off.png");
    private final Image on = new Image("sample\\sound_on.png");
    private ImageView sound,coin_img, coin_img_2,blast_screen;
    private boolean will_dont_move = false;
    private Circle circle1, circle2;
    private int movements;
    private int num_of_coins;
    private Label movement_label,num_coins;
    private final boolean[] curr_on = new boolean[1];
    private ArrayList<AnimationTimer> all_animation_timers;
    private ArrayList<gameObjects> gameObjectsList;
    private ArrayList<gameObjects> clouds;
    private ArrayList<gameObjects> islands;
    private ArrayList<gameObjects> orcs;
    private ArrayList<gameObjects> trees;
    private ArrayList<gameObjects> coins;
    private ArrayList<gameObjects> chests;
    private ArrayList<gameObjects> weapons;
    private ArrayList<gameObjects> obstacles;
    private ArrayList<score> highscores;
    private gameObjects willHero;
    private AnchorPane anchorPane, pauseMenuAnchorPane,mainMenuAnchorPane, endGameAnchorPane;


    @Override
    public void start(Stage primaryStage) throws Exception{

//        System.out.println(Paths.get(".").toAbsolutePath().normalize().toString());
        FXMLLoader loader2 = new FXMLLoader(getClass().getResource("mainMenu.fxml"));
        Parent root2 = loader2.load();
        mainMenuScreen = new Scene(root2,1400,770);
        mainMenuController mainMenuController = loader2.getController();
        mainMenuController.move_the_creatures();
        stage = primaryStage;
//        mainMenuController.getStage(primaryStage,mainGameScene);

        FXMLLoader loader1 = new FXMLLoader(getClass().getResource("pauseMenu.fxml"));
        loader1.load();
        pauseMenuController pauseMenuController = loader1.getController();
//        pauseMenuController.getMainGameAnchorpane(anchorPane);
//        pauseMenuController.getListOfAnimationTimers(all_animation_timers);
        pauseMenuAnchorPane = pauseMenuController.getPauseMenu();
        Label resume = new Label("Resume");
        resume.setTextFill(Color.valueOf("#004463"));
        String myfont = Font.getFamilies().get(180);
        resume.setFont(Font.font(myfont,34));
        resume.setLayoutX(608);
        resume.setLayoutY(201);
        resume.setCursor(Cursor.HAND);
        resume.setOnMouseClicked(event-> resume());
        pauseMenuAnchorPane.getChildren().add(resume);

        Label restart = new Label("Restart");
        restart.setTextFill(Color.valueOf("#004463"));
        restart.setFont(Font.font(myfont,34));
        restart.setLayoutX(615);
        restart.setLayoutY(267);
        restart.setCursor(Cursor.HAND);
        restart.setOnMouseClicked(event-> {
            resume();
            movements = 0;
            start_new_game(primaryStage);
        });
        pauseMenuAnchorPane.getChildren().add(restart);

        Label saveGame = new Label("Save game");
        saveGame.setTextFill(Color.valueOf("#004463"));
        saveGame.setFont(Font.font(myfont,34));
        saveGame.setLayoutX(587);
        saveGame.setLayoutY(334);
        saveGame.setCursor(Cursor.HAND);
        saveGame.setOnMouseClicked(event-> {
            resume();
            try {
                saveGame();
                System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        pauseMenuAnchorPane.getChildren().add(saveGame);

        Label exit_game = new Label("Exit game");
        exit_game.setTextFill(Color.valueOf("#004463"));
        exit_game.setFont(Font.font(myfont,34));
        exit_game.setLayoutX(594);
        exit_game.setLayoutY(408);
        exit_game.setCursor(Cursor.HAND);
        exit_game.setOnMouseClicked(event-> System.exit(0));
        pauseMenuAnchorPane.getChildren().add(exit_game);


        mainMenuAnchorPane = mainMenuController.getAnchorpane();
        Label start = new Label("Start game");
        start.setTextFill(Color.valueOf("#004463"));
//        String myfont = Font.getFamilies().get(180);
        start.setFont(Font.font(myfont,34));
        start.setLayoutX(460);
        start.setLayoutY(580);
        start.setCursor(Cursor.HAND);
        mainMenuAnchorPane.getChildren().add(start);

        TextField nameEnter = new TextField();
        nameEnter.setPromptText("Enter your name:");
        nameEnter.setFont(Font.font(myfont,12));
        nameEnter.setLayoutX(610);
        nameEnter.setLayoutY(500);
        mainMenuAnchorPane.getChildren().add(nameEnter);

        Label load = new Label("Load game");
        load.setTextFill(Color.valueOf("#004463"));
        load.setFont(Font.font(myfont,34));
        load.setLayoutX(730);
        load.setLayoutY(580);
        load.setCursor(Cursor.HAND);
        mainMenuAnchorPane.getChildren().add(load);

        Label view_highscores = new Label("View highscores");
        view_highscores.setTextFill(Color.valueOf("#004463"));
        view_highscores.setFont(Font.font(myfont,34));
        view_highscores.setLayoutX(420);
        view_highscores.setLayoutY(661);
        view_highscores.setCursor(Cursor.HAND);
        mainMenuAnchorPane.getChildren().add(view_highscores);
        highscores = DeserialiseScores("C:\\Users\\Aaryendra\\IdeaProjects\\Will_hero_game\\all_scores.txt");
        if(highscores==null){
            highscores = new ArrayList<>();
        }
        view_highscores.setOnMouseClicked(event -> {
            if(highscores.size()>0){
                highscores.sort((n1, n2) -> n2.score - n1.score);
            }
            AnchorPane leaderboard = new AnchorPane();
            leaderboard.setPrefWidth(1400);
            leaderboard.setPrefHeight(770);

            Label back = new Label("Back");
            back.setLayoutX(5);
            back.setLayoutY(10);
            back.setFont(Font.font(myfont,34));
            back.setCursor(Cursor.HAND);
            back.setOnMouseClicked(event2->{
                mainMenuAnchorPane.getChildren().remove(leaderboard);
            });

            Label l1 = new Label("Rank     Player Name     Score");

            l1.setLayoutX(300);
            l1.setLayoutY(100);
            l1.setFont(Font.font(myfont,55));

            ImageView iv = new ImageView(new Image("sample\\backgroundPauseMenu.png"));
            iv.setFitWidth(1400);
            iv.setFitHeight(770);
            leaderboard.getChildren().addAll(iv,back,l1);

            for(int i = 0;i<Math.min(5,highscores.size());i++){
                Label score_i = new Label("   " + (i+1) +"            "+highscores.get(i).playerName+"             " + highscores.get(i).score);
                score_i.setLayoutX(300);
                score_i.setLayoutY(100 + (i+1)*100);
                score_i.setTextFill(Color.valueOf("#004463"));
                score_i.setFont(Font.font(myfont,50));
                leaderboard.getChildren().add(score_i);
            }
            mainMenuAnchorPane.getChildren().addAll(leaderboard);
        });

        Label exit = new Label("Exit game");
        exit.setTextFill(Color.valueOf("#004463"));
        exit.setFont(Font.font(myfont,34));
        exit.setLayoutX(738);
        exit.setLayoutY(661);
        exit.setCursor(Cursor.HAND);
        mainMenuAnchorPane.getChildren().add(exit);

        exit.setOnMouseClicked(event -> System.exit(0));
        String s = "C:\\Users\\Aaryendra\\OneDrive\\Desktop\\JAVAFX\\music.mp3";
        Media media = new Media(Paths.get(s).toUri().toString());
        mediaPlayer = new MediaPlayer(media);

        start.setOnMouseClicked(event -> {
            curr_player = new Player(nameEnter.getText());
            start_new_game(primaryStage);
        });

        load.setOnMouseClicked(event -> {
            try {
                curr_player = new Player(nameEnter.getText());
                load_the_game(primaryStage);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        });

//        mediaPlayer.play();
        mainMenuController.setMediaPlayer(mediaPlayer);


        primaryStage.setScene(mainMenuScreen);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public void start_new_game(Stage stage){
        movements = 0;
        num_of_coins = 0;
        String l = "C:\\Users\\Aaryendra\\Downloads\\loading_animation.mp4";
        Media loading = new Media(Paths.get(l).toUri().toString());
        MediaPlayer loading_video = new MediaPlayer(loading);
        loading_video.play();
        MediaView mediaView = new MediaView(loading_video);
        mediaView.setLayoutX(0);
        mediaView.setLayoutY(0);
        mediaView.setFitHeight(770);
        mediaView.setFitWidth(1400);
        mediaView.setPreserveRatio(false);
        mainMenuAnchorPane.getChildren().add(mediaView);

        anchorPane = new AnchorPane();
        anchorPane.setPrefHeight(770);
        anchorPane.setPrefWidth(1400);

        if(all_animation_timers!=null){
            for(AnimationTimer animationTimer : all_animation_timers){
                animationTimer.stop();
            }
        }

        all_animation_timers = new ArrayList<>();
        gameObjectsList = new ArrayList<>();
        islands = new ArrayList<>();
        clouds = new ArrayList<>();
        orcs = new ArrayList<>();
        trees = new ArrayList<>();
        coins = new ArrayList<>();
        chests = new ArrayList<>();
        weapons = new ArrayList<>();
        obstacles = new ArrayList<>();


        addBackground(0,0,1400,770,"sample\\img.png",anchorPane);
        addIslands();
        addClouds();

        addTrees();
        addCoins();
        addChests();
        addObstacles();
        addWillHero(114,407,39,50,"sample\\Helmet3.png");
        addOrcs();
        addBossOrc(islands.get(islands.size()-1).getObject().getLayoutX()+50,islands.get(islands.size()-1).getObject().getLayoutY()-150,150,150,"sample\\Orc2.png");
        addWeapons();

        coin_img_2 = new ImageView("sample\\Coin.png");
        coin_img_2.setFitWidth(30);
        coin_img_2.setFitHeight(30);
        coin_img_2.setLayoutX(-300);

        Lighting lighting = new Lighting();
        lighting.setDiffuseConstant(2);
        Light light = new Light.Distant();
        light.setColor(Color.valueOf("#ff2209"));
        lighting.setLight(light);

        blast_screen = new ImageView(new Image("sample\\whitescreen.png"));
        blast_screen.setEffect(lighting);
        blast_screen.setFitWidth(440);
        blast_screen.setFitHeight(380);
        blast_screen.setLayoutX(-1000);

        Iterator<gameObjects> it = clouds.iterator();
        while(it.hasNext()){
            gameObjects cloud = it.next();
            anchorPane.getChildren().add(cloud.getObject());
        }
        for(gameObjects tree : trees){
            anchorPane.getChildren().add(tree.getObject());
        }
        for(gameObjects object : gameObjectsList){
            anchorPane.getChildren().add(object.getObject());
        }
        for(gameObjects weapon : weapons){
            anchorPane.getChildren().add(weapon.getObject());
        }
        anchorPane.getChildren().addAll(coin_img_2,blast_screen);
        cloud_movement();

        heading_labels(anchorPane);
        set_sound_on_off(anchorPane);
        mainGameScene = new Scene(anchorPane,1400,770);
        loading_video.setOnEndOfMedia(()->{
            mainMenuAnchorPane.getChildren().remove(mediaView);
            stage.setScene(mainGameScene);
        });
        mainGameScene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.RIGHT) {
                move_will();
            }
        });
//        return mainGameScene;
    }

    public void load_the_game(Stage stage) throws IOException, ClassNotFoundException {

        String l = "C:\\Users\\Aaryendra\\Downloads\\loading_animation.mp4";
        Media loading = new Media(Paths.get(l).toUri().toString());
        MediaPlayer loading_video = new MediaPlayer(loading);
        loading_video.play();
        MediaView mediaView = new MediaView(loading_video);
        mediaView.setLayoutX(0);
        mediaView.setLayoutY(0);
        mediaView.setFitHeight(770);
        mediaView.setFitWidth(1400);
        mediaView.setPreserveRatio(false);
        mainMenuAnchorPane.getChildren().add(mediaView);

        all_animation_timers = new ArrayList<>();
        gameObjectsList = new ArrayList<>();
        islands = new ArrayList<>();
        clouds = new ArrayList<>();
        orcs = new ArrayList<>();
        trees = new ArrayList<>();
        coins = new ArrayList<>();
        chests = new ArrayList<>();
        weapons = new ArrayList<>();
        obstacles = new ArrayList<>();

        anchorPane = new AnchorPane();
        anchorPane.setPrefHeight(770);
        anchorPane.setPrefWidth(1400);
        addBackground(0,0,1400,770,"sample\\img.png",anchorPane);
        ArrayList<Coordinates> chestList = Deserialise("C:\\Users\\Aaryendra\\IdeaProjects\\Will_hero_game\\loaded_chestList.txt");
        for(Coordinates chest : chestList){
            addChest(chest.layout_x,chest.layout_y,chest.fitWidth,chest.fitHeight,chest.image);
        }
        ArrayList<Coordinates> coinList = Deserialise("C:\\Users\\Aaryendra\\IdeaProjects\\Will_hero_game\\loaded_coinList.txt");
        for(Coordinates coin : coinList){
            if(coin.moving){
                addCoin(coin.layout_x, coin.layout_y, coin.fitWidth, coin.fitHeight, coin.image,true);
            }
            else{
                addCoin(coin.layout_x, coin.layout_y, coin.fitWidth, coin.fitHeight, coin.image,false);
            }

        }

        ArrayList<Coordinates> islandList = Deserialise("C:\\Users\\Aaryendra\\IdeaProjects\\Will_hero_game\\loaded_islandList.txt");
        for(Coordinates island : islandList){
            addIsland(island.layout_x, island.layout_y, island.fitWidth, island.fitHeight, island.image);
        }
        ArrayList<Coordinates> obstacleList = Deserialise("C:\\Users\\Aaryendra\\IdeaProjects\\Will_hero_game\\loaded_obstacleList.txt");
        for(Coordinates obstacle : obstacleList){
            addObstacle(obstacle.layout_x, obstacle.layout_y, obstacle.fitWidth, obstacle.fitHeight, obstacle.image);
        }

        ArrayList<score> score;
        score = DeserialiseScores("C:\\Users\\Aaryendra\\IdeaProjects\\Will_hero_game\\score.txt");
        num_of_coins = score.get(0).coins;
        movements = score.get(0).score;


        ArrayList<Coordinates> will = Deserialise("C:\\Users\\Aaryendra\\IdeaProjects\\Will_hero_game\\willHero.txt");
        addWillHero(will.get(0).layout_x,will.get(0).layout_y,will.get(0).fitWidth,will.get(0).fitHeight,"sample\\Helmet3.png");

        ArrayList<Coordinates> orcList = Deserialise("C:\\Users\\Aaryendra\\IdeaProjects\\Will_hero_game\\loaded_OrcList.txt");
        for(Coordinates orc : orcList){
            if(orc.fitWidth==100){
                addUnMovingOrc(orc.layout_x, orc.layout_y, orc.fitWidth, orc.fitHeight, orc.image);
            }
            else{
                addOrc(orc.layout_x, orc.layout_y, orc.fitWidth, orc.fitHeight, orc.image);
            }

        }
        addBossOrc(islands.get(islands.size()-1).getObject().getLayoutX()+50,islands.get(islands.size()-1).getObject().getLayoutY()-150,150,150,"sample\\Orc2.png");
        coin_img_2 = new ImageView("sample\\Coin.png");
        coin_img_2.setFitWidth(30);
        coin_img_2.setFitHeight(30);
        coin_img_2.setLayoutX(-300);

        Lighting lighting = new Lighting();
        lighting.setDiffuseConstant(2);
        Light light = new Light.Distant();
        light.setColor(Color.valueOf("#ff2209"));
        lighting.setLight(light);

        blast_screen = new ImageView(new Image("sample\\whitescreen.png"));
        blast_screen.setEffect(lighting);
        blast_screen.setFitWidth(440);
        blast_screen.setFitHeight(380);
        blast_screen.setLayoutX(-1000);
        for(gameObjects cloud : clouds){
            anchorPane.getChildren().add(cloud.getObject());
        }
        for(gameObjects tree : trees){
            anchorPane.getChildren().add(tree.getObject());
        }
        for(gameObjects object : gameObjectsList){
            anchorPane.getChildren().add(object.getObject());
        }

        anchorPane.getChildren().addAll(coin_img_2,blast_screen);
        cloud_movement();
        heading_labels(anchorPane);
        set_sound_on_off(anchorPane);

        ArrayList<weapon_details> weaponList = DeserialiseWeapons("C:\\Users\\Aaryendra\\IdeaProjects\\Will_hero_game\\loaded_weaponsList.txt");
        for(weapon_details weapon : weaponList){
            got_weapon = got_weapon || weapon.got_weapon;
            if(weapon.got_weapon){
                if(weapon.weapon_name.equals("Sword")){
                    circle2.setOpacity(0.5);
                    addWeapon(weapon.weapon_name,weapon.x_speed,weapon.range,weapon.acceleration,weapon.image,
                            weapon.layoutX,weapon.layoutY,weapon.width,weapon.height,true);

                }
                else{
                    circle1.setOpacity(0.5);
                    addWeapon(weapon.weapon_name,weapon.x_speed,weapon.range,weapon.acceleration,weapon.image,
                            weapon.layoutX,weapon.layoutY,weapon.width,weapon.height,true);
                }
            }
            else{
                addWeapon(weapon.weapon_name,weapon.x_speed,weapon.range,weapon.acceleration,weapon.image,
                        weapon.layoutX,weapon.layoutY,weapon.width,weapon.height,false);
            }


        }

        for(gameObjects weapon : weapons){
            anchorPane.getChildren().add(weapon.getObject());
        }
        mainGameScene = new Scene(anchorPane,1400,770);
        mainGameScene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.RIGHT) {
                move_will();
            }
        });
        loading_video.setOnEndOfMedia(()->{
            mainMenuAnchorPane.getChildren().remove(mediaView);
            stage.setScene(mainGameScene);
        });

    }

    public void move_will_back_after_collision(){

        AnimationTimer move_back_after_collision = new AnimationTimer() {
            double distance = 10;
            double last = 0;

            @Override
            public void handle(long l) {
                if (last != 0) {
                    if (distance <= 0) {
                        will_dont_move = false;
                        willHero.setX_speed(0);
                    } else {
                        double dist = 1;
                        distance -= dist;
                        willHero.getObject().setLayoutX(willHero.getObject().getLayoutX() - dist);
                    }
                }
                last = l;
            }
        };
        move_back_after_collision.start();
        all_animation_timers.add(move_back_after_collision);

        for(gameObjects gameObject : gameObjectsList){
            AnimationTimer animationTimer2 = new AnimationTimer() {
                double distance = 10;
                double last = 0;
                @Override
                public void handle(long l) {
                    if(last!=0){
                        if(distance<=0){
                            willHero.setX_speed(0);
                        }
                        else{
                            double dist = 1;
                            distance -= dist;
                            gameObject.getObject().setLayoutX(gameObject.getObject().getLayoutX() + dist);
                        }
                    }
                    last = l;
                }
            };
            animationTimer2.start();
            all_animation_timers.add(animationTimer2);
        }

        for(gameObjects gameObject : trees){
            AnimationTimer animationTimer2 = new AnimationTimer() {
                double distance = 10;
                double last = 0;
                @Override
                public void handle(long l) {
                    if(last!=0){
                        if(distance<=0){
                            willHero.setX_speed(0);
                        }
                        else{
                            double dist = 1;
                            distance -= dist;
                            gameObject.getObject().setLayoutX(gameObject.getObject().getLayoutX() + dist);
                        }
                    }
                    last = l;
                }
            };
            animationTimer2.start();
            all_animation_timers.add(animationTimer2);
        }
    }

    public void move_obstacle(){
        for(gameObjects obstacle : obstacles){
            AnimationTimer move_tnt = new AnimationTimer() {
                double last = 0;
                @Override
                public void handle(long l) {
                    if (last != 0) {
                        if (obstacle.getX_Speed() <= 0 && obstacle.getX_Speed() >= -10) {
                            obstacle.setX_speed(0);
                        } else {
                            obstacle.getObject().setLayoutX(obstacle.getObject().getLayoutX() + obstacle.getX_Speed() * (l / Math.pow(10, 9) - last / Math.pow(10, 9)));
                            obstacle.setX_speed(obstacle.getX_Speed() - (obstacle.getX_Speed() / Math.abs(obstacle.getX_Speed())) * 10);
                        }
                    }
                    last = l;
                }
            };
            move_tnt.start();
        }
    }

    public void obstacle_vertical_movement(){
        for(gameObjects obstacle : obstacles){
            AnimationTimer obstacle_movement = new AnimationTimer() {
                @Override
                public void handle(long l) {
                    boolean collides = false;
                    for(gameObjects island : islands){
                        if(isColliding(obstacle.getObject(),island.getObject())){
                            if (obstacle.getObject().getLayoutY() + obstacle.getObject().getFitHeight() >island.getObject().getLayoutY() +30) {
//                                        System.out.println(orc.getObject().getLayoutY() + orc.getObject().getFitHeight());
//                                obstacle.setX_speed(-100);
                            }
                            else{
                                obstacle.setY_speed(0);
                                collides = true;
                            }

                            break;
                        }
                    }
                    if(!collides){
                        obstacle.setY_speed(3);
                    }
                    obstacle.getObject().setLayoutY(obstacle.getObject().getLayoutY()+obstacle.getY_speed());
                }
            };
            obstacle_movement.start();

        }

    }

    public void move_orc(){
        for(gameObjects orc : orcs){
            if(orc.getObject().getLayoutY() + orc.getObject().getFitHeight()>490){
                continue;
            }
            AnimationTimer move_orcs = new AnimationTimer() {
                double last = 0;

                @Override
                public void handle(long l) {
                    if (last != 0) {
                        if (orc.getX_Speed() <= 0 && orc.getX_Speed() >= -10) {
                            orc.setX_speed(0);
                        } else {
                            orc.getObject().setLayoutX(orc.getObject().getLayoutX() + orc.getX_Speed() * (l / Math.pow(10, 9) - last / Math.pow(10, 9)));
                            orc.setX_speed(orc.getX_Speed() - (orc.getX_Speed() / Math.abs(orc.getX_Speed())) * 10);
                        }
                    }
                    last = l;
                }
            };
            move_orcs.start();
            all_animation_timers.add(move_orcs);
        }

    }

    public void move_will() {
        if(willHero.getObject().getLayoutY() + willHero.getObject().getFitHeight()>490){
            return;
        }
        movements++;
        movement_label.setText(movements+"");
        if(got_weapon && ((weapon)curr_weapon).getWeapon_name().equals("Shuriken")){

            if(weapon_quantity==2){
                gameObjects shuriken_thrown = weapons.get(weapon_index_for_throwing);
                weapon_index_for_throwing++;

                shuriken_thrown.getObject().setLayoutX(curr_weapon.getObject().getLayoutX());
                shuriken_thrown.getObject().setLayoutY(curr_weapon.getObject().getLayoutY());
                ((weapon)shuriken_thrown).setRange(((weapon)curr_weapon).getRange());
                shuriken_thrown.setX_speed(curr_weapon.getX_Speed());
                shuriken_thrown.setY_speed(1);
                ((weapon) shuriken_thrown).setAcceleration(((weapon) curr_weapon).getAcceleration());

                AnimationTimer throw_weapon = new AnimationTimer() {
                    @Override
                    public void handle(long l) {
                        if(shuriken_thrown.getX_Speed()!=0){

                            if(((weapon) shuriken_thrown).getRange()<=0){
                                shuriken_thrown.setX_speed(0);
                                shuriken_thrown.setY_speed(0);
                                ((weapon) shuriken_thrown).setAcceleration(0);
                                shuriken_thrown.getObject().setLayoutX(-200);
                                anchorPane.getChildren().remove(shuriken_thrown.getObject());
                            }
                            else{
                                if(isColliding(BossOrc.getObject(),shuriken_thrown.getObject())){
                                    ((BossOrc)BossOrc).decrease_hits();
                                    if(((BossOrc)BossOrc).getHits_it_can_take()==0){

                                        anchorPane.getChildren().remove(BossOrc.getObject());
                                        anchorPane.getChildren().remove(shuriken_thrown.getObject());
                                        shuriken_thrown.setX_speed(0);
                                        shuriken_thrown.setY_speed(0);
                                        shuriken_thrown.getObject().setLayoutX(-200);
                                        ((weapon) shuriken_thrown).setAcceleration(0);
                                    }
                                    else{
                                        shuriken_thrown.getObject().setLayoutX(-1000);
                                        shuriken_thrown.setX_speed(0);
                                        shuriken_thrown.setY_speed(0);
                                        ((weapon) shuriken_thrown).setAcceleration(0);
                                        anchorPane.getChildren().remove(shuriken_thrown.getObject());
                                    }
                                }
                                for(gameObjects orc : orcs){
                                    if(isColliding(orc.getObject(),shuriken_thrown.getObject())){
                                        ((orc)orc).setHits_can_take(((orc)orc).getHits_can_take()-1);
                                        if(((orc)orc).getHits_can_take()==0){
                                            num_of_coins+=2;
                                            num_coins.setText(num_of_coins+"");
                                            orcs.remove(orc);
                                            anchorPane.getChildren().remove(orc.getObject());
                                            anchorPane.getChildren().remove(shuriken_thrown.getObject());
                                            shuriken_thrown.setX_speed(0);
                                            shuriken_thrown.setY_speed(0);
                                            shuriken_thrown.getObject().setLayoutX(-200);
                                            ((weapon) shuriken_thrown).setAcceleration(0);
                                        }
                                        else{
                                            shuriken_thrown.getObject().setLayoutX(-1000);
                                            shuriken_thrown.setX_speed(0);
                                            shuriken_thrown.setY_speed(0);
                                            ((weapon) shuriken_thrown).setAcceleration(0);
                                            anchorPane.getChildren().remove(shuriken_thrown.getObject());
                                        }

                                        break;
                                    }
                                }
                                shuriken_thrown.getObject().setLayoutX(shuriken_thrown.getObject().getLayoutX() + shuriken_thrown.getX_Speed());
                                shuriken_thrown.getObject().setLayoutY(shuriken_thrown.getObject().getLayoutY() - shuriken_thrown.getY_speed());
                                shuriken_thrown.getObject().setRotate(shuriken_thrown.getObject().getRotate() + 10);
                                ((weapon)shuriken_thrown).setRange(((weapon)shuriken_thrown).getRange()-shuriken_thrown.getX_Speed());
                                shuriken_thrown.setX_speed(shuriken_thrown.getX_Speed() + ((weapon) shuriken_thrown).getAcceleration());
//                            System.out.println(curr_weapon.getObject().getLayoutX() +" "+ curr_weapon.getObject().getLayoutX() +" "+curr_weapon.getObject().getLayoutX());
                            }
                        }


                    }
                };
                throw_weapon.start();
            }
            gameObjects shuriken_thrown = weapons.get(weapon_index_for_throwing);
            weapon_index_for_throwing++;

            shuriken_thrown.getObject().setLayoutX(curr_weapon.getObject().getLayoutX());
            shuriken_thrown.getObject().setLayoutY(curr_weapon.getObject().getLayoutY());
            ((weapon)shuriken_thrown).setRange(((weapon)curr_weapon).getRange());
            shuriken_thrown.setX_speed(curr_weapon.getX_Speed());
            ((weapon) shuriken_thrown).setAcceleration(((weapon) curr_weapon).getAcceleration());

            AnimationTimer throw_weapon = new AnimationTimer() {
                @Override
                public void handle(long l) {
                    if(shuriken_thrown.getX_Speed()!=0){

                        if(((weapon) shuriken_thrown).getRange()<=0){
                            shuriken_thrown.setX_speed(0);
                            ((weapon) shuriken_thrown).setAcceleration(0);
                            shuriken_thrown.getObject().setLayoutX(-200);
                            anchorPane.getChildren().remove(shuriken_thrown.getObject());
                        }
                        else{
                            if(isColliding(BossOrc.getObject(),shuriken_thrown.getObject())){
                                ((BossOrc)BossOrc).decrease_hits();
                                if(((BossOrc)BossOrc).getHits_it_can_take()==0){

                                    anchorPane.getChildren().remove(BossOrc.getObject());
                                    anchorPane.getChildren().remove(shuriken_thrown.getObject());
                                    shuriken_thrown.setX_speed(0);
                                    shuriken_thrown.setY_speed(0);
                                    shuriken_thrown.getObject().setLayoutX(-200);
                                    ((weapon) shuriken_thrown).setAcceleration(0);
                                }
                                else{
                                    shuriken_thrown.getObject().setLayoutX(-1000);
                                    shuriken_thrown.setX_speed(0);
                                    shuriken_thrown.setY_speed(0);
                                    ((weapon) shuriken_thrown).setAcceleration(0);
                                    anchorPane.getChildren().remove(shuriken_thrown.getObject());
                                }
                            }
                            for(gameObjects orc : orcs){
                                if(isColliding(orc.getObject(),shuriken_thrown.getObject())){
                                    ((orc)orc).setHits_can_take(((orc)orc).getHits_can_take()-1);
                                    if(((orc)orc).getHits_can_take()==0){
                                        num_of_coins+=2;
                                        num_coins.setText(num_of_coins+"");
                                        orcs.remove(orc);
                                        anchorPane.getChildren().remove(orc.getObject());
                                        anchorPane.getChildren().remove(shuriken_thrown.getObject());
                                        shuriken_thrown.setX_speed(0);
                                        shuriken_thrown.getObject().setLayoutX(-200);
                                        ((weapon) shuriken_thrown).setAcceleration(0);
                                    }
                                    else{
                                        shuriken_thrown.getObject().setLayoutX(-1000);
                                        shuriken_thrown.setX_speed(0);
                                        ((weapon) shuriken_thrown).setAcceleration(0);
                                        anchorPane.getChildren().remove(shuriken_thrown.getObject());
                                    }

                                    break;
                                }
                            }
                            shuriken_thrown.getObject().setLayoutX(shuriken_thrown.getObject().getLayoutX() + shuriken_thrown.getX_Speed());
                            shuriken_thrown.getObject().setRotate(shuriken_thrown.getObject().getRotate() + 10);
                            ((weapon)shuriken_thrown).setRange(((weapon)shuriken_thrown).getRange()-shuriken_thrown.getX_Speed());
                            shuriken_thrown.setX_speed(shuriken_thrown.getX_Speed() + ((weapon) shuriken_thrown).getAcceleration());
//                            System.out.println(curr_weapon.getObject().getLayoutX() +" "+ curr_weapon.getObject().getLayoutX() +" "+curr_weapon.getObject().getLayoutX());
                        }
                    }


                }
            };
            throw_weapon.start();

        }
        AnimationTimer move_will_hero = new AnimationTimer() {
            double distance = 120;
            double last = 0;

            @Override
            public void handle(long l) {
                if (!will_dont_move) {
                    if (last != 0) {
                        if (distance <= 0) {
                            willHero.setX_speed(0);
                        } else {
                            distance -= 40;
                            willHero.getObject().setLayoutX(willHero.getObject().getLayoutX() + 40);
                            if(got_weapon) curr_weapon.getObject().setLayoutX(willHero.getObject().getLayoutX()+13);
                        }
                    }
                    last = l;
                }

            }
        };
        move_will_hero.start();

        all_animation_timers.add(move_will_hero);
        for(gameObjects gameObject : gameObjectsList){
            AnimationTimer temp_timer1 = new AnimationTimer() {

                    double distance = 120;
                    double last = 0;

                    @Override
                    public void handle(long l) {

                        if (!will_dont_move) {
                            if (last != 0) {
                                if (distance <= 0) {
    //                            gameObject.setX_speed(0);
                                } else {
                                    double dist = 10;
                                    distance -= dist;
                                    gameObject.getObject().setLayoutX(gameObject.getObject().getLayoutX() - dist);
                                }
                            }
                            last = l;
                        }


                    }
                };
            temp_timer1.start();
            all_animation_timers.add(temp_timer1);
        }
        for(gameObjects gameObject : trees){
            AnimationTimer temp_timer2 = new AnimationTimer() {
                double distance = 120;
                double last = 0;

                @Override
                public void handle(long l) {
                    if (!will_dont_move) {
                        if (last != 0) {
                            if (distance <= 0) {
//                            willHero.setX_speed(0);
                            } else {
                                double dist = 10;
                                distance -= dist;
                                gameObject.getObject().setLayoutX(gameObject.getObject().getLayoutX() - dist);
                            }
                        }
                        last = l;
                    }

                }
            };
            temp_timer2.start();
            all_animation_timers.add(temp_timer2);

        }
    }

    public void orc_jump(){
        AnimationTimer orc_jump_timer = new AnimationTimer() {

            @Override
            public void handle(long l) {
                for (int j = 0;j<orcs.size();j++) {
                    gameObjects orc = orcs.get(j);
                    if(orc.getObject().getLayoutY()>770){
                        num_of_coins+=2;
                        num_coins.setText(num_of_coins+"");
                        orcs.remove(orc);
                        j--;
                    }
                    if (orc.getLast_time() != 0) {
                        if (orc.getX_Speed() != 0) {
                            boolean collides = false;
                            boolean on_top = false;
                            int idx = orcs.indexOf(orc);
                            for (int i = 0; i < orcs.size(); i++) {
                                gameObjects curr = orcs.get(i);
                                if (curr == orc) continue;
                                if (isColliding(orc.getObject(), curr.getObject())) {
                                    collides = true;

                                    if (orc.getObject().getLayoutY() + orc.getObject().getFitHeight() <= curr.getObject().getLayoutY()+10) {
                                        on_top = true;
                                        orc.setY_speed(curr.getY_speed());
                                    } else {

                                        if (orc.getObject().getLayoutX() > curr.getObject().getLayoutX()) {
                                            orc.setX_speed(400);
                                            curr.setX_speed(200);
                                        } else {
                                            orc.setX_speed(200);
                                            curr.setX_speed(400);
                                        }
                                    }

                                    break;
                                }
                            }
                            if(got_weapon && isColliding(orc.getObject(), curr_weapon.getObject()) && ((weapon) curr_weapon).getWeapon_name().equals("Sword")){
                                if(willHero.getObject().getLayoutY() <orc.getObject().getLayoutY() + orc.getObject().getFitHeight()-20){
                                    num_of_coins+=2;
                                    num_coins.setText(num_of_coins+"");
                                    orcs.remove(orc);
                                    anchorPane.getChildren().remove(orc.getObject());
                                    j--;


                                }

                            }
                            collides = false;

                            // orc
                            for (gameObjects island : islands) {
                                if (isColliding(orc.getObject(), island.getObject())) {
                                    collides = true;
                                    if (orc.getObject().getLayoutY() + orc.getObject().getFitHeight() >island.getObject().getLayoutY() +30) {
//                                        System.out.println(orc.getObject().getLayoutY() + orc.getObject().getFitHeight());
                                        orc.setX_speed(-100);
                                        collides = false;
                                    }

                                    break;
                                }
                            }
                            if (collides) {
                                orc.setY_speed(0);
                            } else {
                                orc.setY_speed(205);
                                orc.setY_acceleration(20);
                            }
                        }
//                    System.out.println(orc.getObject().getLayoutY());
                         if (orc.getObject().getLayoutY() <= ((orc)orc).getJumping_height()) {
                            if (!orc.getMoving()) {
                                orc.setY_speed(205);
                                orc.setY_acceleration(-20);
                            }
                            orc.setMoving(true);
                        } else {
                            orc.setMoving(false);
                            boolean collides = false;
                            boolean on_top = false;
                            int idx = orcs.indexOf(orc);
                            for (int i = 0; i < orcs.size(); i++) {
                                gameObjects curr = orcs.get(i);
                                if (curr == orc) continue;

                                if (isColliding(orc.getObject(), curr.getObject())) {
                                    collides = true;
                                    if(i>idx) continue;
                                    if (orc.getObject().getLayoutY() + orc.getObject().getFitHeight() <= curr.getObject().getLayoutY()+10) {
                                        on_top = true;
                                        orc.setY_speed(curr.getY_speed());
                                    } else {
                                        if (orc.getObject().getLayoutX() > curr.getObject().getLayoutX()) {
                                            orc.setX_speed(400);
                                            curr.setX_speed(200);
                                        } else {
                                            orc.setX_speed(200);
                                            curr.setX_speed(400);
                                        }
                                    }


                                    break;
                                }
                            }
//                            for(gameObjects chest : chests){
//                                if(isColliding(orc.getObject(), chest.getObject())) {
//                                    if(orc.getObject().getLayoutX() + orc.getObject().getFitWidth() > chest.getObject().getLayoutX()
//                                        && orc.getObject().getLayoutY() + orc.getObject().getFitHeight() - 15<= chest.getObject().getLayoutY()){
//                                        orc.setX_speed(500);
//                                    }
//                                    else {
//                                        orc.setX_speed(-200);
//                                    }
////                                    orc.getObject().setLayoutX(chest.getObject().getLayoutX()-80);
//                                    break;
//                                }
//                            }
                            collides = false;
                            for (gameObjects island : islands) {
                                if (isColliding(orc.getObject(), island.getObject())) {
                                    collides = true;
                                    if (orc.getObject().getLayoutY() + orc.getObject().getFitHeight() >= island.getObject().getLayoutY()+30) {
//                                        orc.setX_speed(-100);
                                        collides = false;
                                    }
                                    else{
                                        ((orc)orc).setJumping_height(island.getObject().getLayoutY()-170);
                                    }


                                    break;
                                }
                            }
                            if (collides) {
                                orc.setY_acceleration(20);
                                orc.setY_speed(-205);
                            }

//                            System.out.println(willHero.getObject().getLayoutY());

                        }
                        orc.getObject().setLayoutY(orc.getObject().getLayoutY() + orc.getY_speed() * (l / Math.pow(10, 9) - orc.getLast_time() / Math.pow(10, 9)));
                        orc.setY_speed(orc.getY_speed() + orc.getY_acceleration() * (l / Math.pow(10, 9) - orc.getLast_time() / Math.pow(10, 9)));
                    }
                    orc.setLast_time(l);
                }


            }
        };
        orc_jump_timer.start();
        all_animation_timers.add(orc_jump_timer);
    }

    public void boss_orc(){
        AnimationTimer animationTimer = new AnimationTimer() {
            double speed = 5;
            @Override
            public void handle(long l) {

                if(BossWeapon.getObject().getLayoutX()<1400){
                    if(anchorPane.getChildren().contains(BossOrc.getObject())){
                        if(got_weapon && isColliding(BossOrc.getObject(), curr_weapon.getObject()) && ((weapon) curr_weapon).getWeapon_name().equals("Sword")){
                            if(willHero.getObject().getLayoutY() <BossOrc.getObject().getLayoutY() + BossOrc.getObject().getFitHeight()-20){
                                anchorPane.getChildren().remove(BossOrc.getObject());
                                for(AnimationTimer at : all_animation_timers){
                                    at.stop();
                                }
                                endGameAnchorPane = new AnchorPane();
                                endGameAnchorPane.setPrefWidth(1400);
                                endGameAnchorPane.setPrefHeight(770);
                                add_items_to_won_game_anchorpane();
                                anchorPane.getChildren().add(endGameAnchorPane);

                            }

                        }
                        if(isColliding(BossWeapon.getObject(),willHero.getObject())){
                            highscores.add(new score(curr_player.name,movements,num_of_coins));
                            try {
                                SerialiseScores("C:\\Users\\Aaryendra\\IdeaProjects\\Will_hero_game\\all_scores.txt",highscores);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            willHero.getObject().setLayoutX(-1000);
                            willHero.setY_speed(0);
                            willHero.setY_acceleration(0);
                            anchorPane.getChildren().remove(willHero.getObject());
                            if(!already_revived_once &&  num_of_coins>=25){
                                endGameAnchorPane = new AnchorPane();
                                endGameAnchorPane.setPrefWidth(1400);
                                endGameAnchorPane.setPrefHeight(770);
                                anchorPane.getChildren().add(endGameAnchorPane);
                                add_items_to_end_game_anchorpane_with_revive(endGameAnchorPane,"killed by Boss orc.");

                            }
                            else{
                                endGameAnchorPane = new AnchorPane();
                                endGameAnchorPane.setPrefWidth(1400);
                                endGameAnchorPane.setPrefHeight(770);
                                anchorPane.getChildren().add(endGameAnchorPane);
                                add_items_to_end_game_anchorpane_without_revive(endGameAnchorPane,"killed by Boss orc.");

                            }
                        }
                        if(BossWeapon.getObject().getLayoutX()< -100){
                            BossWeapon.getObject().setLayoutX(BossOrc.getObject().getLayoutX()+100);
                            speed = 5;
                        }
                        else{
                            BossWeapon.getObject().setLayoutX(BossWeapon.getObject().getLayoutX()-speed);
                            speed+=0.1;
                        }
                    }
                    else{
                        anchorPane.getChildren().remove(BossWeapon.getObject());

                        for(AnimationTimer at : all_animation_timers){
                            at.stop();
                        }
                        endGameAnchorPane = new AnchorPane();
                        endGameAnchorPane.setPrefWidth(1400);
                        endGameAnchorPane.setPrefHeight(770);
                        add_items_to_won_game_anchorpane();
                        anchorPane.getChildren().add(endGameAnchorPane);
                    }
                }


            }
        };
        animationTimer.start();
    }

    public void will_jump(){

        AnimationTimer will_jump_timer = new AnimationTimer() {
            double max_height = 460;
            int chest_num = 0;
            @Override
            public void handle(long l) {
//                System.out.println(curr_weapon.getObject().getLayoutX() +" "+curr_weapon.getObject().getLayoutY());
                if (willHero.last_time != 0) {
                    if(blast_screen.getOpacity()==0){
                        blast_screen.setLayoutX(-1000);
                    }
//                    System.out.println(willHero.getObject().getLayoutY());
                    if(willHero.getObject().getLayoutY()>770){ // goes outside the screen
                        highscores.add(new score(curr_player.name,movements,num_of_coins));
                        try {
                            SerialiseScores("C:\\Users\\Aaryendra\\IdeaProjects\\Will_hero_game\\all_scores.txt",highscores);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if(curr_weapon!=null) {
                            curr_weapon.getObject().setLayoutX(curr_weapon.getObject().getLayoutX()-1000);
                            curr_weapon.getObject().setOpacity(0);
                        }
                        willHero.setY_speed(0);
                        willHero.setY_acceleration(0);
                        willHero.getObject().setLayoutY(-100);
                        anchorPane.getChildren().remove(willHero.getObject());
                        if(!already_revived_once && num_of_coins>=25){
                            endGameAnchorPane = new AnchorPane();
                            endGameAnchorPane.setPrefWidth(1400);
                            endGameAnchorPane.setPrefHeight(770);
//                            anchorPane.getChildren().add(endGameAnchorPane);
//                            flag = true;
                            add_items_to_end_game_anchorpane_with_revive(endGameAnchorPane," died by falling into the abyss.");
                            anchorPane.getChildren().add(endGameAnchorPane);
                        }
                        else{
                            endGameAnchorPane = new AnchorPane();
                            endGameAnchorPane.setPrefWidth(1400);
                            endGameAnchorPane.setPrefHeight(770);
//                            flag = true;
                            anchorPane.getChildren().add(endGameAnchorPane);
                            add_items_to_end_game_anchorpane_without_revive(endGameAnchorPane," died by falling into the abyss.");
//                            anchorPane.getChildren().add(endGameAnchorPane);
                        }
                    }
//                    System.out.println(willHero.getObject().getLayoutY());
                    if (willHero.getObject().getLayoutY() <= max_height - 190) {
                        if (!willHero.getMoving()) {
                            willHero.setY_speed(230);
                            willHero.setY_acceleration(100);
                        }
                        willHero.setMoving(true);
                    } else {

                        for(int i = 0;i<obstacles.size();i++){
                            gameObjects obstacle = obstacles.get(i);

                            if(obstacle.getObject().getOpacity()==0.999){
                                blast_screen.setOpacity(1);
                                blast_screen.setLayoutX(obstacle.getObject().getLayoutX()-200);
                                blast_screen.setLayoutY(obstacle.getObject().getLayoutY()-150);
                                obstacle.getObject().setLayoutX(-1000);
                                obstacles.remove(obstacle);
                                i--;
                                anchorPane.getChildren().remove(obstacle.getObject());

                                Timeline timeline = new Timeline();

                                timeline.getKeyFrames().add(new KeyFrame(
                                        Duration.seconds(0.3),new KeyValue(blast_screen.opacityProperty(),0, Interpolator.LINEAR)
                                ));

                                if(isColliding(willHero.object,blast_screen)){
                                    highscores.add(new score(curr_player.name,movements,num_of_coins));
                                    try {
                                        SerialiseScores("C:\\Users\\Aaryendra\\IdeaProjects\\Will_hero_game\\all_scores.txt",highscores);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    if(curr_weapon!=null) {
                                        curr_weapon.getObject().setLayoutX(curr_weapon.getObject().getLayoutX()-1000);
                                        curr_weapon.getObject().setOpacity(0);
                                    }
                                    willHero.setY_speed(0);
                                    willHero.setY_acceleration(0);
                                    willHero.getObject().setLayoutX(-10000);
                                    anchorPane.getChildren().remove(willHero.getObject());
                                    if(!already_revived_once && num_of_coins>=25){
                                        endGameAnchorPane = new AnchorPane();
                                        endGameAnchorPane.setPrefWidth(1400);
                                        endGameAnchorPane.setPrefHeight(770);
//                            anchorPane.getChildren().add(endGameAnchorPane);
                                        add_items_to_end_game_anchorpane_with_revive(endGameAnchorPane," died by TNT.");
                                        anchorPane.getChildren().add(endGameAnchorPane);
                                    }
                                    else {
                                        endGameAnchorPane = new AnchorPane();
                                        endGameAnchorPane.setPrefWidth(1400);
                                        endGameAnchorPane.setPrefHeight(770);
                                        anchorPane.getChildren().add(endGameAnchorPane);
                                        add_items_to_end_game_anchorpane_without_revive(endGameAnchorPane, " died by TNT.");
                                    }
                                }


                                for(int j = 0;j<orcs.size();j++){
                                    gameObjects orc = orcs.get(j);
                                    if(isColliding(orc.getObject(),blast_screen)){
                                        anchorPane.getChildren().remove(orc.getObject());
                                        orcs.remove(orc);
                                        j--;
                                    }
                                }

                                timeline.play();
                            }
                            if(isColliding(obstacle.getObject(),willHero.getObject())){

                                Timeline opacity_reducer = new Timeline();
                                opacity_reducer.getKeyFrames().add(new KeyFrame(
                                        Duration.seconds(0.9),new KeyValue(obstacle.getObject().opacityProperty(),0.999, Interpolator.LINEAR)
                                ));

                                opacity_reducer.play();

                                if(willHero.getObject().getLayoutX()+willHero.getObject().getFitWidth()<obstacle.getObject().getLayoutX()+40){
                                    if(willHero.getObject().getLayoutY()+willHero.getObject().getFitHeight()>obstacle.getObject().getLayoutY()+20){
                                        obstacle.setX_speed(700);
                                        willHero.setX_speed(-100);


                                    }
                                    else{
                                        max_height = obstacle.getObject().getLayoutY();
                                        willHero.setY_acceleration(40);
                                        willHero.setY_speed(-305);
                                    }

                                }
                                else if(willHero.getObject().getLayoutY()+willHero.getObject().getFitHeight()<obstacle.getObject().getLayoutY()+20){
                                    max_height = obstacle.getObject().getLayoutY();
                                    willHero.setY_acceleration(40);
                                    willHero.setY_speed(-305);
                                }

                                break;

                            }
                        }
                        // collision with coin
                        for (int i = 0; i < coins.size(); i++) {
                            gameObjects coin = coins.get(i);
                            if (isColliding(willHero.getObject(), coin.getObject())) {
                                coins.remove(coin);
                                anchorPane.getChildren().remove(coin.getObject());
                                i--;
                                num_of_coins++;
                                if(num_of_coins>=100){
                                    coin_img.setLayoutX(65);
                                }
                                else if(num_of_coins>=10){
                                    coin_img.setLayoutX(50);
                                }
                                num_coins.setText(num_of_coins + "");
                            }
                        }

                        //collision with chest
                        for (int i = chest_num; i < chests.size(); i++) {
                            gameObjects chest = chests.get(i);
                            if(!chest.getObject().getImage().getUrl().equals("sample\\ChestOpen.png")){
                                if (isColliding(willHero.getObject(), chest.getObject())) {
                                    chest.getObject().setImage(new Image("sample\\ChestOpen.png"));
                                    Random random = new Random();
//                                    int num = random.nextInt(2);
                                int num = 0;
                                    if(num==0){

                                        //weapon chest
                                        got_weapon = true;
//                                        int weapon_num = random.nextInt(2);
                                        int weapon_num = 1;
                                        if(weapon_num==0){

                                            circle2.setOpacity(0.5);
                                        }
                                        else{
                                            if(circle1.getOpacity()==0.5){
                                                weapon_quantity = 2;
                                            }
                                            circle1.setOpacity(0.5);
                                        }
                                        if(curr_weapon!=null) curr_weapon.getObject().setLayoutX(-200);
                                        curr_weapon = weapons.get(weapon_num);
                                    }
                                    else{
                                        // coin chest
                                        coin_img_2.setLayoutX(willHero.getObject().getLayoutX()-25);
                                        coin_img_2.setLayoutY(willHero.getObject().getLayoutY()-45);
                                        coin_img_2.setOpacity(1);
                                        coin_img_2.setRotate(0);
                                        Timeline coin_animation = new Timeline();
//                                    coin_animation.setCycleCount(Timeline.INDEFINITE);
                                        coin_animation.getKeyFrames().add(new KeyFrame(
                                                Duration.seconds(3),new KeyValue(coin_img_2.rotateProperty(),720, Interpolator.LINEAR),
                                                new KeyValue(coin_img_2.opacityProperty(),0, Interpolator.LINEAR)
                                        ));
                                        coin_animation.play();
                                        num_of_coins+=10;
                                        if(num_of_coins>=100){
                                            coin_img.setLayoutX(65);
                                        }
                                        else if(num_of_coins>=10){
                                            coin_img.setLayoutX(50);
                                        }
                                        num_coins.setText(num_of_coins + "");
                                    }
                                    chest_num++;
                                    break;
                                }
                            }

                        }

                        willHero.setMoving(false);
                        boolean collides = false;
                        boolean on_top = false;
                        for (gameObjects orc : orcs) {
                            if (isColliding(willHero.getObject(), orc.getObject())) {
                                if (willHero.getObject().getLayoutY() + willHero.getObject().getFitHeight() - 20 <= orc.getObject().getLayoutY()) {
                                    on_top = true;
                                    max_height = orc.getObject().getLayoutY();
                                } else {
                                    collides = true;

                                    if(willHero.getObject().getLayoutY() > orc.getObject().getLayoutY() + orc.getObject().getFitHeight()-20){
                                        highscores.add(new score(curr_player.name,movements,num_of_coins));
                                        try {
                                            SerialiseScores("C:\\Users\\Aaryendra\\IdeaProjects\\Will_hero_game\\all_scores.txt",highscores);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        willHero.getObject().setLayoutX(-10000);
                                        willHero.setY_speed(0);
                                        willHero.setY_acceleration(0);
                                        orc.getObject().setLayoutX(orc.getObject().getLayoutX()+50);
                                        if(curr_weapon!=null) {
                                            curr_weapon.getObject().setLayoutX(curr_weapon.getObject().getLayoutX()-1000);
                                            curr_weapon.getObject().setOpacity(0);
                                        }

                                        anchorPane.getChildren().remove(willHero.getObject());
                                        collides = false;
                                        orc.setX_speed(0);
                                        if(!already_revived_once && num_of_coins>=25){
                                            endGameAnchorPane = new AnchorPane();
                                            endGameAnchorPane.setPrefWidth(1400);
                                            endGameAnchorPane.setPrefHeight(770);
                                            anchorPane.getChildren().add(endGameAnchorPane);
                                            add_items_to_end_game_anchorpane_with_revive(endGameAnchorPane," was killed by an orc.");

                                        }
                                        else{
                                            endGameAnchorPane = new AnchorPane();
                                            endGameAnchorPane.setPrefWidth(1400);
                                            endGameAnchorPane.setPrefHeight(770);
                                            anchorPane.getChildren().add(endGameAnchorPane);
                                            add_items_to_end_game_anchorpane_without_revive(endGameAnchorPane," was killed by an orc.");
//                                            anchorPane.getChildren().add(endGameAnchorPane);
                                        }

                                    }
                                    else{
                                        orc.setX_speed(550);
                                    }
                                    if(curr_weapon!=null && ((weapon)curr_weapon).getWeapon_name().equals("Sword")){
                                        num_of_coins+=2;
                                        num_coins.setText(num_of_coins+"");
                                        orcs.remove(orc);
                                        anchorPane.getChildren().remove(orc.getObject());


                                    }

                                }

                                break;
                            }
                        }
                        if (on_top) {
                            willHero.setY_acceleration(40);
                            willHero.setY_speed(-305);
                        } else if (collides) {

                            will_dont_move = true;
                            move_will_back_after_collision();
//                            move_will_hero.stop();
                            willHero.setY_acceleration(100);
                            willHero.setY_speed(230);


                        }

                        collides = false;
                        for (gameObjects island : islands) {
                            if (isColliding(willHero.getObject(), island.getObject()) && (willHero.getObject().getLayoutY() + willHero.getObject().getFitHeight() -15<=island.getObject().getLayoutY())) {
                                max_height = island.getObject().getLayoutY();
                                collides = true;
                                break;
                            }
                        }
                        if (collides) {
                            willHero.setY_acceleration(40);
                            willHero.setY_speed(-305);
                        }

//                            System.out.println(willHero.getObject().getLayoutY());

                    }
                    willHero.getObject().setLayoutY(willHero.getObject().getLayoutY() + willHero.getY_speed() * (l / Math.pow(10, 9) - willHero.last_time / Math.pow(10, 9)));
                    willHero.setY_speed(willHero.getY_speed() + willHero.getY_acceleration() * (l / Math.pow(10, 9) - willHero.last_time / Math.pow(10, 9)));
                    if(got_weapon){
                        curr_weapon.getObject().setLayoutY(willHero.getObject().getLayoutY() + 20);
                        curr_weapon.getObject().setLayoutX(willHero.getObject().getLayoutX()+13);
                    }
                }

                willHero.setLast_time(l);
            }
        };
        will_jump_timer.start();
        all_animation_timers.add(will_jump_timer);
    }

    public void addBackground(double layoutX, double layoutY, double width,double height,String image,AnchorPane anchorPane){
        Image background_img = new Image(image);
        ImageView imageView = new ImageView(background_img);
        imageView.setLayoutX(layoutX);
        imageView.setLayoutY(layoutY);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        Glow glow = new Glow(0.6);
        imageView.setEffect(glow);
        anchorPane.getChildren().add(imageView);
    }

    public void addWillHero(double layoutX, double layoutY, double width,double height,String image){
        Image will_img = new Image(image);
        ImageView imageView = new ImageView(will_img);
        imageView.setLayoutX(layoutX);
        imageView.setLayoutY(layoutY);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
//        imageView.setPreserveRatio(true);
        gameObjects will = new Helmet(imageView);
        willHero = will;
        curr_player.setHelmet(will);
        gameObjectsList.add(will);
    }

    public void addOrcs(){
        addOrc(570,410,60,60,"sample\\Orc3.png");
        addOrc(1100,415,60,60,"sample\\Orc3.png");
        addOrc(1950,400,60,60,"sample\\Orc3.png");
        addOrc(2015,400,60,60,"sample\\Orc3.png");
        addOrc(2700,395,60,60,"sample\\Orc3.png");
        addOrc(3020,400,60,60,"sample\\Orc3.png");
        addOrc(3090,400,60,60,"sample\\Orc3.png");
        addOrc(3440,357,60,60,"sample\\Orc3.png");
        addOrc(3840,307,60,60,"sample\\Orc3.png");
        addOrc(4240,257,60,60,"sample\\Orc3.png");
        addOrc(5410,257,60,60,"sample\\Orc3.png");
        addOrc(5800,307,60,60,"sample\\Orc3.png");
        addUnMovingOrc(6120,325,100,100,"sample\\Orc3.png");
        addUnMovingOrc(7050,216,100,100,"sample\\RedOrc1.png");
        addOrc(7600,205,60,60,"sample\\RedOrc1.png");
        addOrc(8210,397,60,60,"sample\\RedOrc1.png");
        addOrc(8420,397,60,60,"sample\\RedOrc1.png");
        addUnMovingOrc(8800,297,100,100,"sample\\RedOrc1.png");
        addOrc(9250,400,60,60,"sample\\RedOrc1.png");
        addOrc(9600,340,60,60,"sample\\RedOrc1.png");
        addOrc(10320,260,60,60,"sample\\RedOrc1.png");
        addOrc(10500,260,60,60,"sample\\RedOrc1.png");
        addUnMovingOrc(10820,357,100,100,"sample\\RedOrc1.png");
        addOrc(11600,260,60,60,"sample\\RedOrc1.png");
        addOrc(12350,260,60,60,"sample\\RedOrc1.png");
        addOrc(12430,260,60,60,"sample\\RedOrc1.png");
        addOrc(12720,260,60,60,"sample\\RedOrc1.png");

    }

    public void addTrees(){
        addTree(750,375,70,100,"sample\\Tree1.png");
    }

    public void addClouds(){
        addCloud(44,28,180,150,0.95,"sample\\cloud.png");
        addCloud(156,122,170,150,0.77,"sample\\cloud.png");
        addCloud(385,61,175,150,0.95,"sample\\cloud.png");
        addCloud(732,74,175,150,0.62,"sample\\cloud.png");
        addCloud(955,14,175,150,0.95,"sample\\cloud.png");
        addCloud(1181,49,175,150,0.68,"sample\\cloud.png");
    }

    public void addCoins(){
        addCoin(400,350,30,30,"sample\\Coin.png",false);
        addCoin(350,350,30,30,"sample\\Coin.png",false);
        addCoin(300,350,30,30,"sample\\Coin.png",false);
        addCoin(1050,90,30,30,"sample\\Coin.png",false);
        addCoin(1100,90,30,30,"sample\\Coin.png",false);
        addCoin(1000,140,30,30,"sample\\Coin.png",false);
        addCoin(1050,140,30,30,"sample\\Coin.png",false);
        addCoin(1100,140,30,30,"sample\\Coin.png",false);
        addCoin(1150,140,30,30,"sample\\Coin.png",false);
        addCoin(1050,190,30,30,"sample\\Coin.png",false);
        addCoin(1100,190,30,30,"sample\\Coin.png",false);
        addCoin(1720,350,30,30,"sample\\Coin.png",false);
        addCoin(1720,300,30,30,"sample\\Coin.png",false);
        addCoin(1720,400,30,30,"sample\\Coin.png",false);
        addCoin(3015,220,30,30,"sample\\Coin.png",false);
        addCoin(3065,220,30,30,"sample\\Coin.png",false);
        addCoin(3115,220,30,30,"sample\\Coin.png",false);
        addCoin(3620,315,30,30,"sample\\Coin.png",true);
        addCoin(3670,315,30,30,"sample\\Coin.png",true);
        addCoin(4020,265,30,30,"sample\\Coin.png",true);
        addCoin(4070,265,30,30,"sample\\Coin.png",true);
        addCoin(4560,215,30,30,"sample\\Coin.png",false);
        addCoin(4605,165,30,30,"sample\\Coin.png",false);
        addCoin(4615,215,30,30,"sample\\Coin.png",false);
        addCoin(4645,115,30,30,"sample\\Coin.png",false);
        addCoin(4685,165,30,30,"sample\\Coin.png",false);
        addCoin(4675,215,30,30,"sample\\Coin.png",false);
        addCoin(4730,215,30,30,"sample\\Coin.png",false);
        addCoin(5620,315,30,30,"sample\\Coin.png",true);
        addCoin(5670,315,30,30,"sample\\Coin.png",true);
        addCoin(7810,200,30,30,"sample\\Coin.png",false);
        addCoin(8320,210,30,30,"sample\\Coin.png",false);
        addCoin(8320,260,30,30,"sample\\Coin.png",false);
        addCoin(8320,310,30,30,"sample\\Coin.png",false);
        addCoin(9840,320,30,30,"sample\\Coin.png",false);
        addCoin(10000,280,30,30,"sample\\Coin.png",false);
        addCoin(10050,280,30,30,"sample\\Coin.png",false);
        addCoin(10100,280,30,30,"sample\\Coin.png",false);
        addCoin(10650,320,30,30,"sample\\Coin.png",false);
    }

    public void addChests(){
        addChest(1425,165,70,60,"sample\\ChestClosed.png");
        addChest(2430,290,70,60,"sample\\ChestClosed.png");
        addChest(5030,270,70,60,"sample\\ChestClosed.png");
        addChest(6650,310,70,60,"sample\\ChestClosed.png");
        addChest(11320,340,70,60,"sample\\ChestClosed.png");
    }

    public void addObstacles(){
        addObstacle(930,190,50,50,"sample\\TNT.png");
        addObstacle(8075,170,50,50,"sample\\TNT.png");
        addObstacle(11920,220,50,50,"sample\\TNT.png");
    }

    public void addIslands(){
        addIsland(74,460,264,148,"sample\\ground1.png");
        addIsland(425,470,197,150,"sample\\ground2.png");
        addIsland(630,470,210,160,"sample\\ground3.png");
        addIsland(850,240,152,148,"sample\\ground1.png");
        addIsland(1000,480,359,160,"sample\\ground3.png");
        addIsland(1200,225,150,140,"sample\\ground2.png");
        addIsland(1355,225,170,140,"sample\\ground2.png");
        addIsland(1450,470,250,150,"sample\\ground2.png");
        addIsland(1800,470,400,150,"sample\\ground2.png");
        addIsland(2260,350,300,150,"sample\\ground3.png");
        addIsland(2630,465,300,150,"sample\\ground3.png");
        addIsland(2945,465,300,150,"sample\\ground4.png");
        addIsland(3340,430,260,150,"sample\\ground2.png");
        addIsland(3740,380,260,150,"sample\\ground2.png");
        addIsland(4140,330,260,150,"sample\\ground2.png");
        addIsland(4540,280,260,150,"sample\\ground2.png");
        addIsland(4940,330,260,150,"sample\\ground2.png");
        addIsland(5340,380,260,150,"sample\\ground2.png");
        addIsland(5740,430,260,150,"sample\\ground2.png");
        addIsland(6010,430,300,150,"sample\\ground3.png");
        addIsland(6520,370,300,150,"sample\\ground3.png");
        addIsland(6980,320,300,150,"sample\\ground3.png");
        addIsland(7470,265,300,150,"sample\\ground3.png");
        addIsland(7900,220,300,150,"sample\\ground3.png");
        addIsland(8180,460,420,150,"sample\\ground3.png");
        addIsland(8680,400,300,150,"sample\\ground3.png");
        addIsland(9050,460,300,150,"sample\\ground3.png");
        addIsland(9490,400,300,150,"sample\\ground3.png");
        addIsland(9920,320,300,150,"sample\\ground3.png");
        addIsland(10300,320,300,150,"sample\\ground3.png");
        addIsland(10750,460,300,150,"sample\\ground3.png");
        addIsland(11200,400,300,150,"sample\\ground3.png");
        addIsland(11510,400,260,150,"sample\\ground2.png");
        addIsland(11850,270,300,150,"sample\\ground3.png");
        addIsland(12250,440,300,150,"sample\\ground3.png");
        addIsland(12620,440,300,150,"sample\\ground3.png");
        addIsland(13000,440,300,150,"sample\\ground3.png");
        addIsland(13310,440,300,150,"sample\\ground3.png");
        addIsland(13620,440,300,150,"sample\\ground3.png");
        addIsland(13930,440,300,150,"sample\\ground3.png");

    }

    public void addWeapons(){
        addWeapon("Sword",0,0,0,"sample\\WeaponSword.png",-200,-200,55,45,false);
        for(int i =0;i<500;i++){
            addWeapon("Shuriken",6,1300,0.6,"sample\\WeaponShuriken.png",-200,-200,55,45,false);
        }
    }

    public void addCoin(double layoutX, double layoutY, double width, double height, String image,boolean moving){
        Image coin_img = new Image(image);
        ImageView imageView = new ImageView(coin_img);
        imageView.setLayoutX(layoutX);
        imageView.setLayoutY(layoutY);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        gameObjects coin = new gameObjects(imageView);

        if(moving){
            coin.setMoving_coin();
            Timeline moving_coin = new Timeline();
            moving_coin.setAutoReverse(true);
            moving_coin.setCycleCount(Timeline.INDEFINITE);
            moving_coin.getKeyFrames().add(new KeyFrame(
                    Duration.seconds(3),new KeyValue(imageView.layoutYProperty(),500, Interpolator.LINEAR)
            ));
            moving_coin.play();
        }
        coins.add(coin);
        gameObjectsList.add(coin);
    }

    public void addChest(double layoutX, double layoutY, double width, double height, String image){
        Image chest_img = new Image(image);
        ImageView imageView = new ImageView(chest_img);
        imageView.setLayoutX(layoutX);
        imageView.setLayoutY(layoutY);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        gameObjects chest = new chest(imageView);
        chests.add(chest);
        gameObjectsList.add(chest);
    }

    public void addWeapon(String weapon_name, double x_speed, double range, double acceleration, String image,
                          double layoutX, double layoutY, double width, double height,boolean curr_weapon_or_not){
        ImageView imageView = new ImageView(new Image(image));
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setLayoutX(layoutX);
        imageView.setLayoutY(layoutY);
        gameObjects weapon = new weapon(weapon_name,x_speed,range, acceleration,imageView);
        weapons.add(weapon);
        if(curr_weapon_or_not){
            curr_weapon = weapon;
        }
//        anchorPane.getChildren().add(imageView);
    }

    public void addTree(double layoutX, double layoutY, double width, double height, String image){
        Image tree_img = new Image(image);
        ImageView imageView = new ImageView(tree_img);
        imageView.setLayoutX(layoutX);
        imageView.setLayoutY(layoutY);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
//        imageView.setPreserveRatio(true);
        gameObjects tree = new gameObjects(imageView);
        trees.add(tree);

    }

    public void addOrc(double layoutX, double layoutY, double width, double height, String image){
        Image orc_img = new Image(image);
        ImageView imageView = new ImageView(orc_img);
        imageView.setLayoutX(layoutX);
        imageView.setLayoutY(layoutY);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
//        imageView.setPreserveRatio(true);
        gameObjects orc = new orc(imageView,1);
        ((orc)orc).setJumping_height(layoutY-120);
        orcs.add(orc);
        gameObjectsList.add(orc);
    }

    public void addUnMovingOrc(double layoutX, double layoutY, double width, double height, String image){
        Image orc_img = new Image(image);
        ImageView imageView = new ImageView(orc_img);
        imageView.setLayoutX(layoutX);
        imageView.setLayoutY(layoutY);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
//        imageView.setPreserveRatio(true);
        gameObjects orc = new orc(imageView,2);
        orc.setY_acceleration(0);
        orc.setY_speed(0);
        orcs.add(orc);
        gameObjectsList.add(orc);
    }

    public void addCloud(double layoutX, double layoutY, double width, double height, double opacity, String image){
        Image cloud_img = new Image(image);
        ImageView imageView = new ImageView(cloud_img);
        imageView.setLayoutX(layoutX);
        imageView.setLayoutY(layoutY);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setOpacity(opacity);
        imageView.setPreserveRatio(true);
        gameObjects cloud = new gameObjects(imageView);
        clouds.add(cloud);
    }

    public void addIsland(double layoutX, double layoutY, double width, double height, String image){
        Image island_img = new Image(image);
        ImageView imageView = new ImageView(island_img);
        imageView.setLayoutX(layoutX);
        imageView.setLayoutY(layoutY);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        gameObjects island = new island(imageView);

        islands.add(island);
        gameObjectsList.add(island);
    }

    public void addObstacle(double layoutX, double layoutY, double width, double height, String image){
        Image obstacle_img = new Image(image);
        ImageView imageView = new ImageView(obstacle_img);
        imageView.setLayoutX(layoutX);
        imageView.setLayoutY(layoutY);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        gameObjects obstacle = new island(imageView);

        obstacles.add(obstacle);
        gameObjectsList.add(obstacle);
    }

    public void addBossOrc(double layoutX, double layoutY, double width, double height, String image){
        ImageView weapon = new ImageView(new Image("sample\\WeaponKnife.png"));
        weapon.setLayoutX(layoutX+100);
        weapon.setLayoutY(layoutY);
        weapon.setFitWidth(55);
        weapon.setFitHeight(20);
        BossWeapon = new weapon("Axe",20,800,50,weapon);

        ImageView imageView = new ImageView(new Image(image));
        imageView.setLayoutX(layoutX);
        imageView.setLayoutY(layoutY);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        BossOrc = new BossOrc(imageView,BossWeapon);
        gameObjectsList.add(BossOrc);
        gameObjectsList.add(BossWeapon);
    }

    public void cloud_movement(){
        for(gameObjects cloud : clouds){
            cloud_move = new Timeline();
            cloud_move.setAutoReverse(true);
            cloud_move.setCycleCount(Timeline.INDEFINITE);
            cloud_move.getKeyFrames().add(new KeyFrame(
                    Duration.seconds(15),new KeyValue(cloud.getObject().layoutXProperty(),cloud.getObject().getLayoutX()-100, Interpolator.LINEAR)
            ));

            cloud_move.play();
        }
    }

    public void set_sound_on_off(AnchorPane anchorPane){
        sound = new ImageView(on);
        sound.setPreserveRatio(true);
        sound.setLayoutX(1345);
        sound.setLayoutY(5);
        sound.setFitWidth(45);
        sound.setFitHeight(0);
        sound.setCursor(Cursor.HAND);
        sound.setEffect(new InnerShadow());
        curr_on[0] = true; // making sound on(in the beginning))
        sound.setOnMousePressed(event->{
            if(curr_on[0]){
                mediaPlayer.pause();
                sound.setImage(off);

            }
            else{
                mediaPlayer.play();
                sound.setImage(on);
            }
            sound.setPreserveRatio(true);
            sound.setLayoutX(1345);
            sound.setLayoutY(5);
            sound.setFitWidth(45);
            sound.setFitHeight(0);
            sound.setEffect(new InnerShadow());
            curr_on[0] = !curr_on[0];
        });
        anchorPane.getChildren().add(sound);
    }

    public boolean isColliding(ImageView image1, ImageView image2){
        return image1.getBoundsInParent().intersects(image2.getBoundsInParent());
    }
    public ArrayList<score> DeserialiseScores(String file_path) throws IOException {
        ObjectInputStream in = null;
        ArrayList<score> list = null;
        try{
            in = new ObjectInputStream(new FileInputStream(file_path));
            list = (ArrayList<score>) in.readObject();
        }
        catch(Exception e){
            System.out.println("game not found");
        }
        finally{
            if(in!=null){
                in.close();
            }
        }
        return list;
    }

    public ArrayList<weapon_details> DeserialiseWeapons(String file_path) throws IOException {
        ObjectInputStream in = null;
        ArrayList<weapon_details> list = null;
        try{
            in = new ObjectInputStream(new FileInputStream(file_path));
            list = (ArrayList<weapon_details>) in.readObject();
        }
        catch(Exception e){
            System.out.println("game not found");
        }
        finally{
            if(in!=null){
                in.close();
            }
        }
        return list;
    }

    public void SerialiseWeapon(String file_path,ArrayList<weapon_details> list) throws IOException {
        ObjectOutputStream out = null;

        try{
            out = new ObjectOutputStream(new FileOutputStream(file_path));
            out.writeObject(list);
        }
        catch (Exception e){
            System.out.println(e);
        }
        finally{
            if(out!=null){
                out.close();
            }
        }
    }

    public void SerialiseScores(String file_path,ArrayList<score> list) throws IOException{
        ObjectOutputStream out = null;

        try{
            out = new ObjectOutputStream(new FileOutputStream(file_path));
            out.writeObject(list);
        }
        catch (Exception e){
            System.out.println(e);
        }
        finally{
            if(out!=null){
                out.close();
            }
        }
    }

    public void Serialise(String file_path,ArrayList<Coordinates> list) throws IOException {
        ObjectOutputStream out = null;

        try{
            out = new ObjectOutputStream(new FileOutputStream(file_path));
            out.writeObject(list);
        }
        catch (Exception e){
            System.out.println(e);
        }
        finally{
            if(out!=null){
                out.close();
            }
        }
    }

    public ArrayList<Coordinates> Deserialise(String file_path) throws IOException{
        ObjectInputStream in = null;
        ArrayList<Coordinates> list = null;
        try{
            in = new ObjectInputStream(new FileInputStream(file_path));
            list = (ArrayList<Coordinates>) in.readObject();
        }
        catch(Exception e){
            System.out.println("game not found");
        }
        finally{
            if(in!=null){
                in.close();
            }
        }
        return list;
    }

    public void heading_labels(AnchorPane anchorPane){
        DropShadow dropShadow = new DropShadow();
        dropShadow.setSpread(1);
        dropShadow.setColor(Color.BLACK);
        Label heading_label1 = new Label("WILL");
        String myfont = Font.getFamilies().get(180);
        heading_label1.setFont(Font.font(myfont,96));
        heading_label1.setTextFill(Color.WHITE);
        heading_label1.setLayoutX(540);
        heading_label1.setLayoutY(70);
        heading_label1.setEffect(dropShadow);

        Label heading_label2 = new Label("HERO");
        heading_label2.setLayoutX(557);
        heading_label2.setLayoutY(165);
        heading_label2.setFont(Font.font(myfont,73));
        heading_label2.setTextFill(Color.WHITE);
        heading_label2.setEffect(dropShadow);

        ImageView pause = new ImageView("sample\\pauseButton.png");
        pause.setLayoutX(1300);
        pause.setLayoutY(3);
        pause.setFitWidth(40);
        pause.setFitHeight(40);
        pause.setEffect(new InnerShadow());
        pause.setOnMouseClicked(mouseEvent -> {
            pause();
        });
        pause.setCursor(Cursor.HAND);

        num_coins = new Label(num_of_coins+"");
        num_coins.setLayoutX(10);
        num_coins.setLayoutY(5);
        num_coins.setFont(Font.font(myfont,30));
        num_coins.setTextFill(Color.YELLOW);
        num_coins.setEffect(new DropShadow());

        coin_img = new ImageView("sample\\Coin.png");
        coin_img.setLayoutX(35);
        coin_img.setLayoutY(10);
        coin_img.setFitWidth(40);
        coin_img.setFitHeight(40);
        anchorPane.getChildren().addAll(heading_label1,heading_label2,pause,num_coins,coin_img);
        add_start_button(heading_label1,heading_label2,myfont,anchorPane);
    }

    public void add_start_button(Label heading1, Label heading2,String font, AnchorPane anchorPane){
        Button start = new Button("Start");
        circle1 = new Circle();
        circle1.setLayoutX(68);
        circle1.setLayoutY(700);
        circle1.setRadius(45);
        circle1.setOpacity(0.2);
        circle1.setFill(Color.DODGERBLUE);
        circle1.setCursor(Cursor.HAND);
        circle1.setOnMouseClicked(mouseEvent -> {
            if(circle1.getOpacity()==0.5 && curr_weapon!=weapons.get(1)){
//                System.out.println("yes");
                if(curr_weapon!=null) curr_weapon.getObject().setLayoutX(-1000);
                got_weapon = true;
                curr_weapon = weapons.get(1);
            }
            else if(circle1.getOpacity()==0.5){
//                System.out.println("no");
                curr_weapon.getObject().setLayoutX(-1000);
                curr_weapon = null;
                got_weapon = false;
            }
        });
        circle2 = new Circle();
        circle2.setLayoutX(170);
        circle2.setLayoutY(700);
        circle2.setRadius(45);
        circle2.setOpacity(0.2);
        circle2.setFill(Color.DODGERBLUE);
        circle2.setCursor(Cursor.HAND);
        circle2.setOnMouseClicked(mouseEvent -> {
            if(circle2.getOpacity()==0.5 && curr_weapon!=weapons.get(0)){
                if(curr_weapon!=null) curr_weapon.getObject().setLayoutX(-1000);
                got_weapon = true;
                curr_weapon = weapons.get(0);
            }
            else if(circle2.getOpacity()==0.5){
                curr_weapon.getObject().setLayoutX(-1000);
                curr_weapon = null;
                got_weapon = false;
            }
        });
        ImageView sword = new ImageView(new Image("sample\\WeaponSword.png"));
        sword.setLayoutX(130);
        sword.setLayoutY(685);
        sword.setFitWidth(79);
        sword.setFitHeight(30);
        sword.setCursor(Cursor.HAND);
        sword.setOnMouseClicked(mouseEvent -> {
            if(circle2.getOpacity()==0.5 && curr_weapon!=weapons.get(0)){
                if(curr_weapon!=null) curr_weapon.getObject().setLayoutX(-1000);
                got_weapon = true;
                curr_weapon = weapons.get(0);
            }
            else if(circle2.getOpacity()==0.5){
                curr_weapon.getObject().setLayoutX(-1000);
                curr_weapon = null;
                got_weapon = false;
            }
        });
        ImageView shuriken = new ImageView(new Image("sample\\WeaponShuriken.png"));
        shuriken.setLayoutX(36);
        shuriken.setLayoutY(668);
        shuriken.setFitWidth(64);
        shuriken.setFitHeight(65);
        shuriken.setCursor(Cursor.HAND);
        shuriken.setOnMouseClicked(mouseEvent -> {
            if(circle1.getOpacity()==0.5 && curr_weapon!=weapons.get(1)){
                if(curr_weapon!=null) curr_weapon.getObject().setLayoutX(-1000);
                got_weapon = true;
                curr_weapon = weapons.get(1);
            }
            else if(circle1.getOpacity()==0.5){
                curr_weapon.getObject().setLayoutX(-1000);
                curr_weapon = null;
                got_weapon = false;
            }
        });
        anchorPane.getChildren().addAll(circle1,circle2,sword,shuriken);
        start.setOnMouseClicked(Event ->{
            will_jump();
            orc_jump();
            move_orc();
            boss_orc();
            obstacle_vertical_movement();
            move_obstacle();
            // adding the movement label
            movement_label = new Label(movements+"");
            String myfont = Font.getFamilies().get(180);
            movement_label.setFont(Font.font(myfont,53));
            movement_label.setTextFill(Color.WHITE);
            movement_label.setLayoutX(650);
            movement_label.setLayoutY(0);
            DropShadow dropShadow = new DropShadow();
            dropShadow.setSpread(0.6);
            movement_label.setEffect(dropShadow);


            anchorPane.getChildren().add(movement_label);
            anchorPane.getChildren().remove(start);
            anchorPane.getChildren().remove(heading1);
            anchorPane.getChildren().remove(heading2);
        });
        start.setEffect(new DropShadow());
        start.setTextFill(Color.BLACK);
        start.setLayoutX(616);
        start.setLayoutY(636);
        start.setPrefWidth(138);
        start.setPrefHeight(52);
        start.setFont(Font.font(font,30));
        start.setCursor(Cursor.HAND);
        anchorPane.getChildren().add(start);

    }

    public void resume(){
        anchorPane.getChildren().remove(pauseMenuAnchorPane);
//        for(AnimationTimer animationTimer : all_animation_timers){
//            animationTimer.start();
//        }

    }

    public void saveGame() throws IOException {

        ArrayList<Coordinates> willhero = new ArrayList<>();
        willhero.add(new Coordinates(willHero.getObject().getLayoutX(),willHero.getObject().getLayoutY(),willHero.getObject().getFitWidth(),willHero.getObject().getFitHeight(),willHero.getObject().getImage().getUrl()));
        Serialise("C:\\Users\\Aaryendra\\IdeaProjects\\Will_hero_game\\willHero.txt",willhero);

        ArrayList<Coordinates> chestList = new ArrayList<>();
        for(gameObjects chest : chests){
            Coordinates new_chest = new Coordinates(chest.getObject().getLayoutX(),chest.getObject().getLayoutY(),chest.getObject().getFitWidth(),chest.getObject().getFitHeight(),chest.getObject().getImage().getUrl());
            chestList.add(new_chest);
        }
        Serialise("C:\\Users\\Aaryendra\\IdeaProjects\\Will_hero_game\\loaded_chestList.txt",chestList);

        ArrayList<Coordinates> coinList = new ArrayList<>();
        for(gameObjects coin : coins){
            Coordinates new_coin= new Coordinates(coin.getObject().getLayoutX(), coin.getObject().getLayoutY(), coin.getObject().getFitWidth(), coin.getObject().getFitHeight(), coin.getObject().getImage().getUrl());
            if(coin.isMoving_coin()){
                new_coin.setMoving(true);
            }
            coinList.add(new_coin);
        }
        Serialise("C:\\Users\\Aaryendra\\IdeaProjects\\Will_hero_game\\loaded_coinList.txt",coinList);

        ArrayList<Coordinates> orcList = new ArrayList<>();
        for(gameObjects orc : orcs){
            Coordinates new_orc= new Coordinates(orc.getObject().getLayoutX(), orc.getObject().getLayoutY(), orc.getObject().getFitWidth(), orc.getObject().getFitHeight(), orc.getObject().getImage().getUrl());
            orcList.add(new_orc);
        }
        Serialise("C:\\Users\\Aaryendra\\IdeaProjects\\Will_hero_game\\loaded_OrcList.txt",orcList);

        ArrayList<Coordinates> islandList = new ArrayList<>();
        for(gameObjects island : islands){
            Coordinates new_island= new Coordinates(island.getObject().getLayoutX(), island.getObject().getLayoutY(), island.getObject().getFitWidth(), island.getObject().getFitHeight(), island.getObject().getImage().getUrl());
            islandList.add(new_island);
        }
        Serialise("C:\\Users\\Aaryendra\\IdeaProjects\\Will_hero_game\\loaded_islandList.txt",islandList);

        ArrayList<score> score = new ArrayList<>();
        score.add(new score(curr_player.name,movements,num_of_coins));
        SerialiseScores("C:\\Users\\Aaryendra\\IdeaProjects\\Will_hero_game\\score.txt",score);

        ArrayList<Coordinates> obstacleList = new ArrayList<>();
        for(gameObjects obstacle : obstacles){
            Coordinates new_obstacle = new Coordinates(obstacle.getObject().getLayoutX(), obstacle.getObject().getLayoutY(), obstacle.getObject().getFitWidth(), obstacle.getObject().getFitHeight(), obstacle.getObject().getImage().getUrl());
            obstacleList.add(new_obstacle);
        }
        Serialise("C:\\Users\\Aaryendra\\IdeaProjects\\Will_hero_game\\loaded_obstacleList.txt",obstacleList);

        ArrayList<weapon_details> weaponList  = new ArrayList<>();
        if(got_weapon){

            for(gameObjects weapon : weapons){
                if(circle1.getOpacity()==0.5 && ((weapon)weapon).getWeapon_name().equals("Shuriken")){
                    weapon_details temp = new weapon_details(((weapon)weapon).getWeapon_name(),weapon.x_speed,((weapon) weapon).getRange(),
                            ((weapon) weapon).getAcceleration(),weapon.getObject().getImage().getUrl(),weapon.getObject().getLayoutX(),
                            weapon.getObject().getLayoutY(),weapon.getObject().getFitWidth(),weapon.getObject().getFitHeight(),true);
                    weaponList.add(temp);
                }
                else if(circle2.getOpacity()==0.5 && ((weapon)weapon).getWeapon_name().equals("Sword")){
                    weapon_details temp = new weapon_details(((weapon)weapon).getWeapon_name(),weapon.x_speed,((weapon) weapon).getRange(),
                            ((weapon) weapon).getAcceleration(),weapon.getObject().getImage().getUrl(),weapon.getObject().getLayoutX(),
                            weapon.getObject().getLayoutY(),weapon.getObject().getFitWidth(),weapon.getObject().getFitHeight(),true);
                    weaponList.add(temp);
                }
                else{
                    weapon_details temp = new weapon_details(((weapon)weapon).getWeapon_name(),weapon.x_speed,((weapon) weapon).getRange(),
                            ((weapon) weapon).getAcceleration(),weapon.getObject().getImage().getUrl(),weapon.getObject().getLayoutX(),
                            weapon.getObject().getLayoutY(),weapon.getObject().getFitWidth(),weapon.getObject().getFitHeight(),false);
                    weaponList.add(temp);
                }

            }
        }
        else{
            for(gameObjects weapon : weapons){
                weapon_details temp = new weapon_details(((weapon)weapon).getWeapon_name(),weapon.x_speed,((weapon) weapon).getRange(),
                        ((weapon) weapon).getAcceleration(),weapon.getObject().getImage().getUrl(),weapon.getObject().getLayoutX(),
                        weapon.getObject().getLayoutY(),weapon.getObject().getFitWidth(),weapon.getObject().getFitHeight(),false);
                weaponList.add(temp);
            }
        }

        SerialiseWeapon("C:\\Users\\Aaryendra\\IdeaProjects\\Will_hero_game\\loaded_weaponsList.txt",weaponList);
    }

    public void pause() {

        anchorPane.getChildren().add(pauseMenuAnchorPane);
    }
    public void add_items_to_won_game_anchorpane(){
        String myfont = Font.getFamilies().get(180);

        ImageView background = new ImageView(new Image("sample\\backgroundPauseMenu.png"));
        InnerShadow effect = new InnerShadow();
        effect.setChoke(0.22);
        effect.setWidth(255);
        effect.setHeight(255);
        effect.setRadius(127);
        background.setEffect(effect);
        background.setLayoutX(470);
        background.setLayoutY(119);
        background.setFitWidth(481);
        background.setFitHeight(558);

        Label you_won = new Label("You won!");
        you_won.setFont(Font.font(myfont,30));
        you_won.setTextFill(Color.valueOf("#004463"));
        you_won.setLayoutX(630);
        you_won.setLayoutY(200);

        ImageView trophy = new ImageView(new Image("sample\\trophy_image.png"));
        trophy.setLayoutX(650);
        trophy.setLayoutY(250);
        trophy.setFitWidth(80);
        trophy.setFitHeight(80);

        Label score_msg = new Label("Score: "+movements);
        score_msg.setFont(Font.font(myfont,23));
        score_msg.setTextFill(Color.valueOf("#004463"));
        score_msg.setLayoutX(654);
        score_msg.setLayoutY(348);

        ImageView coin2 = new ImageView(new Image("sample\\Coin.png"));
        coin2.setFitHeight(25);
        coin2.setFitWidth(25);
        coin2.setLayoutY(135);
        coin2.setLayoutX(863);

        Label coins = new Label(num_of_coins+"");
        coins.setLayoutX(898);
        coins.setLayoutY(130);
        coins.setTextFill(Color.YELLOW);
        coins.setFont(Font.font(myfont,23));
        coins.setEffect(new DropShadow());

        Label restart_msg = new Label("Restart");
        restart_msg.setFont(Font.font(myfont,26));
        restart_msg.setTextFill(Color.valueOf("#004463"));
        restart_msg.setLayoutX(663);
        restart_msg.setLayoutY(468);
        restart_msg.setCursor(Cursor.HAND);
        restart_msg.setOnMouseClicked(event->{
            anchorPane.getChildren().remove(endGameAnchorPane);
            movements = 0;
            num_of_coins = 0;
            already_revived_once = false;
            curr_weapon = null;
            got_weapon = false;
            start_new_game(stage);
        });
        Label exit = new Label("Exit");
        exit.setFont(Font.font(myfont,26));
        exit.setTextFill(Color.valueOf("#004463"));
        exit.setLayoutX(683);
        exit.setLayoutY(537);
        exit.setCursor(Cursor.HAND);
        exit.setOnMouseClicked(event->System.exit(0));
        endGameAnchorPane.getChildren().addAll(background,you_won,score_msg,restart_msg,exit,coin2,coins,trophy);
    }
    public void add_items_to_end_game_anchorpane_without_revive(AnchorPane endGameAnchorPane, String str){
        String myfont = Font.getFamilies().get(180);

        ImageView background = new ImageView(new Image("sample\\backgroundPauseMenu.png"));
        InnerShadow effect = new InnerShadow();
        effect.setChoke(0.22);
        effect.setWidth(255);
        effect.setHeight(255);
        effect.setRadius(127);
        background.setEffect(effect);
        background.setLayoutX(470);
        background.setLayoutY(119);
        background.setFitWidth(481);
        background.setFitHeight(558);

        Label you_died = new Label("You died!");
        you_died.setFont(Font.font(myfont,30));
        you_died.setTextFill(Color.valueOf("#004463"));
        you_died.setLayoutX(630);
        you_died.setLayoutY(200);
        Label die_msg;

        die_msg = new Label(curr_player.name+" was killed by an orc.");
        die_msg = new Label(curr_player.name + str);
        die_msg.setFont(Font.font(myfont,18));
        die_msg.setAlignment(Pos.CENTER);
        die_msg.setTextFill(Color.valueOf("#004463"));
        die_msg.setLayoutX(574);
        die_msg.setLayoutY(296);



        Label score_msg = new Label("Score: "+movements);
        score_msg.setFont(Font.font(myfont,23));
        score_msg.setTextFill(Color.valueOf("#004463"));
        score_msg.setLayoutX(654);
        score_msg.setLayoutY(348);

        ImageView coin2 = new ImageView(new Image("sample\\Coin.png"));
        coin2.setFitHeight(25);
        coin2.setFitWidth(25);
        coin2.setLayoutY(135);
        coin2.setLayoutX(863);

        Label coins = new Label(num_of_coins+"");
        coins.setLayoutX(898);
        coins.setLayoutY(130);
        coins.setTextFill(Color.YELLOW);
        coins.setFont(Font.font(myfont,23));
        coins.setEffect(new DropShadow());

        Label restart_msg = new Label("Restart");
        restart_msg.setFont(Font.font(myfont,26));
        restart_msg.setTextFill(Color.valueOf("#004463"));
        restart_msg.setLayoutX(663);
        restart_msg.setLayoutY(468);
        restart_msg.setCursor(Cursor.HAND);
        restart_msg.setOnMouseClicked(event->{
            anchorPane.getChildren().remove(endGameAnchorPane);
            movements = 0;
            num_of_coins = 0;
            already_revived_once = false;
            curr_weapon = null;
            got_weapon = false;
            start_new_game(stage);
        });
        Label exit = new Label("Exit");
        exit.setFont(Font.font(myfont,26));
        exit.setTextFill(Color.valueOf("#004463"));
        exit.setLayoutX(683);
        exit.setLayoutY(537);
        exit.setCursor(Cursor.HAND);
        exit.setOnMouseClicked(event->System.exit(0));
        endGameAnchorPane.getChildren().addAll(background,you_died,die_msg,score_msg,restart_msg,exit,coin2,coins);
    }

    public void add_items_to_end_game_anchorpane_with_revive(AnchorPane endGameAnchorPane,String str){
        String myfont = Font.getFamilies().get(180);

        ImageView background = new ImageView(new Image("sample\\backgroundPauseMenu.png"));
        InnerShadow effect = new InnerShadow();
        effect.setChoke(0.22);
        effect.setWidth(255);
        effect.setHeight(255);
        effect.setRadius(127);
        background.setEffect(effect);
        background.setLayoutX(470);
        background.setLayoutY(119);
        background.setFitWidth(481);
        background.setFitHeight(558);

        Label you_died = new Label("You died!");
        you_died.setFont(Font.font(myfont,30));
        you_died.setTextFill(Color.valueOf("#004463"));
        you_died.setLayoutX(630);
        you_died.setLayoutY(200);
        Label die_msg;

        die_msg = new Label(curr_player.name+str);
        die_msg.setFont(Font.font(myfont,18));
        die_msg.setAlignment(Pos.CENTER);
        die_msg.setTextFill(Color.valueOf("#004463"));
        die_msg.setLayoutX(574);
        die_msg.setLayoutY(296);




        Label score_msg = new Label("Score: "+movements);
        score_msg.setFont(Font.font(myfont,23));
        score_msg.setTextFill(Color.valueOf("#004463"));
        score_msg.setLayoutX(654);
        score_msg.setLayoutY(348);

        Label revive_msg = new Label("Revive game      x25");
        revive_msg.setFont(Font.font(myfont,23));
        revive_msg.setTextFill(Color.valueOf("#004463"));
        revive_msg.setLayoutX(596);
        revive_msg.setLayoutY(454);
        revive_msg.setCursor(Cursor.HAND);
        revive_msg.setOnMouseClicked(event ->{
            anchorPane.getChildren().remove(endGameAnchorPane);
            already_revived_once = true;
            num_of_coins-=25;
            num_coins.setText(num_of_coins+"");
            willHero.getObject().setLayoutX(65);
            willHero.getObject().setLayoutY(100);
            willHero.setY_speed(205);
            willHero.setY_acceleration(30);
            if(curr_weapon!=null){
                curr_weapon.getObject().setOpacity(1);
                curr_weapon.getObject().setLayoutX(65);
//                anchorPane.getChildren().addAll(willHero.getObject(),curr_weapon.getObject());
            }
//            System.out.println(anchorPane.getChildren());
            if(anchorPane.getChildren().contains(willHero.getObject())){
                anchorPane.getChildren().remove(endGameAnchorPane);
            }
            else{
                anchorPane.getChildren().add(willHero.getObject());
            }


        });

        ImageView coin = new ImageView(new Image("sample\\Coin.png"));
        coin.setFitHeight(25);
        coin.setFitWidth(25);
        coin.setLayoutY(459);
        coin.setLayoutX(749);

        ImageView coin2 = new ImageView(new Image("sample\\Coin.png"));
        coin2.setFitHeight(25);
        coin2.setFitWidth(25);
        coin2.setLayoutY(135);
        coin2.setLayoutX(863);

        Label coins = new Label(num_of_coins+"");
        coins.setLayoutX(898);
        coins.setLayoutY(130);
        coins.setTextFill(Color.YELLOW);
        coins.setFont(Font.font(myfont,23));
        coins.setEffect(new DropShadow());

        Label restart_msg = new Label("Restart");
        restart_msg.setFont(Font.font(myfont,23));
        restart_msg.setTextFill(Color.valueOf("#004463"));
        restart_msg.setLayoutX(663);
        restart_msg.setLayoutY(508);
        restart_msg.setCursor(Cursor.HAND);
        restart_msg.setOnMouseClicked(event->{
            anchorPane.getChildren().remove(endGameAnchorPane);
            movements = 0;
            num_of_coins = 0;
            already_revived_once = false;
            curr_weapon = null;
            got_weapon = false;
            start_new_game(stage);
        });
        Label exit = new Label("Exit");
        exit.setFont(Font.font(myfont,23));
        exit.setTextFill(Color.valueOf("#004463"));
        exit.setLayoutX(683);
        exit.setLayoutY(558);
        exit.setCursor(Cursor.HAND);
        exit.setOnMouseClicked(event->System.exit(0));
        endGameAnchorPane.getChildren().addAll(background,you_died,die_msg,score_msg,revive_msg,coin,restart_msg,exit,coin2,coins);
    }

    public static void main(String[] args) {

        launch(args);
    }

    @Override
    public void run() {
        String s = "C:\\Users\\Aaryendra\\OneDrive\\Desktop\\JAVAFX\\music.mp3";
        Media media = new Media(Paths.get(s).toUri().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);

    }
}
