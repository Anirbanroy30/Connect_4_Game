package com.anirbanroy.javafxapplication;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {


    public Controller controller;
    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));
        GridPane rootGridPane=loader.load();

        MenuBar menuBar=createmenu();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        Pane menupane= (Pane) rootGridPane.getChildren().get(0);
        menupane.getChildren().add(menuBar);
        controller =loader.getController();
        controller.createplayground();

        Scene scene=new Scene(rootGridPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Connect 4");
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    public MenuBar createmenu(){
        //Menu Items
        Menu filemenu=new Menu("File");

        //file menu
        MenuItem Newgame=new MenuItem("New Game");
        Newgame.setOnAction(event -> { controller.resetgame(); });

        SeparatorMenuItem separatormenuItems1=new SeparatorMenuItem();

        MenuItem resetgame=new MenuItem("Reset Game");
        resetgame.setOnAction(event -> { controller.resetgame(); });

        MenuItem quitegame=new MenuItem("Exit Game");
        quitegame.setOnAction(event -> {
            Platform.exit();
            System.exit(0);
        });

        filemenu.getItems().addAll(Newgame,resetgame,separatormenuItems1,quitegame);

        //Help Menu
        Menu helpmenu=new Menu("Help");

        MenuItem aboutGame=new MenuItem("About Game");
        aboutGame.setOnAction(event ->{
            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("About Connect4 Game");
            alert.setHeaderText("How to play?");
            alert.setContentText("Connect Four is a two-player connection game in which the players first choose a color and then take turns dropping colored discs from the top into a seven-column, six-row vertically suspended grid. The pieces fall straight down, occupying the next available space within the column. The objective of the game is to be the first to form a horizontal, vertical, or diagonal line of four of one's own discs. Connect Four is a solved game. The first player can always win by playing the right moves.");
            alert.show();
        });
        MenuItem aboutme=new MenuItem("About Devloper");
        aboutme.setOnAction(event -> {
            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("About me");
            alert.setHeaderText("Anirban Roy");
            alert.setContentText("I love to play games around with code."+" I devlop this popular Mutiplayer game in free time with the help of javafx and java.");
            alert.show();
        });

        helpmenu.getItems().addAll(aboutGame,aboutme);

        MenuBar menuBar=new MenuBar();
        menuBar.getMenus().addAll(filemenu,helpmenu);
        return menuBar;
    }

    private void resetGame() {
    }



    public static void main(String[] args) {
        launch(args);
    }
}
