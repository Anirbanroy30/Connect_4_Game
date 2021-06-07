package com.anirbanroy.javafxapplication;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable {

    private static final int coloum=7;
    private static final int row=6;
    private static final int Circle_Diametre=80;
    private static  String name1;
    private static  String name2;
    private static final String disc1="#0000ff";
    private static final String disc2="#00ff00";

    private static boolean isplayerOne=true;
    private boolean isAllowedtoInsert=true;

    private Disc[][] insertedDiscArray=new Disc[row][coloum];

    @FXML
    public GridPane mygrid;
    @FXML
    public Pane filepane;
    @FXML
    public Pane Structurepane;
    @FXML
    public VBox playervbox;
    @FXML
    public Label label1;// player name lebel
    @FXML
    public Label label2;//turn lebel
    @FXML
    public Button setNamesButton;
    @FXML
    public TextField playerOneTextField;
    @FXML
    public TextField playerTwoTextField;

    public void createplayground() {

        Platform.runLater(()->setNamesButton.requestFocus());
        Shape RectangleWithHoles=gameStructuralMethod();
        mygrid.add(RectangleWithHoles, 0, 1);
        List<Rectangle> rectangleList=clickablecoloums();

        for (Rectangle rectangle:rectangleList)
        {
            mygrid.add(rectangle,0,1);
        }
        setNamesButton.setOnAction(event -> {
            name1=playerOneTextField.getText();
            name2=playerTwoTextField.getText();
            label1.setText((isplayerOne?name1:name2));
        });

    }
    private Shape gameStructuralMethod(){
        Shape RectangleWithHoles = new Rectangle((coloum+1) * Circle_Diametre, (row+1) * Circle_Diametre);

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < coloum; j++) {

                Circle circle = new Circle();
                circle.setRadius(Circle_Diametre / 2);
                circle.setCenterX(Circle_Diametre / 2);
                circle.setCenterY(Circle_Diametre / 2);
                circle.setSmooth(true);

                circle.setTranslateX(j * (Circle_Diametre+5)+Circle_Diametre/4);
                circle.setTranslateY(i * (Circle_Diametre+5)+Circle_Diametre/4);

                RectangleWithHoles = Shape.subtract(RectangleWithHoles, circle);
            }
        }
        RectangleWithHoles.setFill(Color.WHITE);
        return RectangleWithHoles;
    }
    private List<Rectangle> clickablecoloums() {
        List<Rectangle>rectangleList=new ArrayList<>();

        for (int i = 0; i < coloum; i++) {
            Rectangle rectangle = new Rectangle(Circle_Diametre, (row + 1) * Circle_Diametre);
            rectangle.setFill(Color.TRANSPARENT);
            rectangle.setTranslateX(i*(Circle_Diametre+5)+Circle_Diametre / 4);

            rectangle.setOnMouseEntered(event -> rectangle.setFill(Color.valueOf("#eeeeee26")));
            rectangle.setOnMouseExited(event -> rectangle.setFill(Color.TRANSPARENT));
            final int coloumVar=i;
            rectangle.setOnMouseClicked(event -> {
                if (isAllowedtoInsert) {   //for no multiple inserting disc
                    isAllowedtoInsert=false;
                    insertDisc(new Disc(isplayerOne), coloumVar);
                }
            });
            rectangleList.add(rectangle);
        }
        return rectangleList;
    }

    private void insertDisc(Disc disc, int coloumVar) {
        int discRow=row-1;
        while(discRow>=0) {
            if (getDiscisPresent(discRow,coloumVar)== null)
                break;

            discRow--;
        }
        if (discRow<0)
            return;


        insertedDiscArray[discRow][coloumVar]=disc;  // Structural Changes for Devlopers
        Structurepane.getChildren().add(disc);
        int currentRow=discRow;
        disc.setTranslateX(coloumVar*(Circle_Diametre+5)+Circle_Diametre/4);
        TranslateTransition translateTransition=new TranslateTransition(Duration.seconds(0.5),disc);


        translateTransition.setToY(discRow*(Circle_Diametre+5)+Circle_Diametre/4);
        translateTransition.setOnFinished(event -> {

            if (gameEnded(currentRow,coloumVar)) {
                gameover();

            }
                isAllowedtoInsert=true;//Allow the next player to insert
                isplayerOne=!isplayerOne;
                label1.setText(isplayerOne? playerOneTextField.getText():name2 );

        });
        translateTransition.play();

    }



    public void resetgame() {
        Structurepane.getChildren().clear();
        for (int i = 0; i <row ; i++) {
            for (int j = 0; j <coloum ; j++) {

                insertedDiscArray[i][j]=null;
            }
            isplayerOne=true;
            label1.setText(name1);
        }
    }

    private boolean gameEnded(int currentRow, int coloumVar) {

        List<Point2D>verticalCombinations= IntStream.rangeClosed(currentRow-3,currentRow+3) //For range of all rows....
                .mapToObj(r->new Point2D(r,coloumVar))
                .collect(Collectors.toList());  //in varticalpoints row is changing like (0,3),(1,3),(2,3)..............

        List<Point2D>horizentalCombinations= IntStream.rangeClosed(coloumVar-3,coloumVar+3) //For range of all coloums....
                .mapToObj(c->new Point2D(currentRow,c))
                .collect(Collectors.toList());  //in varticalpoints row is changing like (0,1),(0,2),(0,3)..............

        Point2D startpoint1=new Point2D(currentRow-3,coloumVar+3);
        List<Point2D>diagonal_1_Combinations= IntStream.rangeClosed(0,6) //For all diagonal combination....
                .mapToObj(i->startpoint1.add(i,-i))
                .collect(Collectors.toList());

        Point2D startpoint2=new Point2D(currentRow-3,coloumVar-3);
        List<Point2D>diagonal_2_Combinations= IntStream.rangeClosed(0,6) //For all diagonal combination....
                .mapToObj(i->startpoint2.add(i,i))
                .collect(Collectors.toList());
        boolean isEnded=checkCombinations(verticalCombinations)||checkCombinations(horizentalCombinations)||checkCombinations(diagonal_1_Combinations)||checkCombinations(diagonal_2_Combinations);

        return isEnded;
    }

    private boolean checkCombinations(List<Point2D> points) {
        int chain=0;
        for (Point2D point:points) {
            int rowIndex = (int) point.getX();
            int coloumIndex = (int) point.getY();

            Disc disc = getDiscisPresent(rowIndex, coloumIndex);

            if (disc != null && disc.isplayeronemove == isplayerOne) {
                chain++;
                if (chain == 4) {
                    return true;
                }
            }
            else {
            chain = 0;

            }

        }
            return false;

    }
    private Disc getDiscisPresent(int discRow,int coloumVar)
    {
        if(discRow>=row|| discRow<0 || coloumVar>=coloum || coloumVar<0) {

            return null;
        }

        return insertedDiscArray[discRow][coloumVar];
    }
    private void gameover() {
        String winner=isplayerOne ? name1: name2;
        System.out.println("winner is:-"+winner);

        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(" Connect4 ");
        alert.setHeaderText("Winner");
        alert.setContentText("winner is :-"+winner);
        ButtonType yesbtn=new ButtonType("Yes play again");
        ButtonType nobtn=new ButtonType("No Exit");
        alert.getButtonTypes().setAll(yesbtn,nobtn);
        Platform.runLater(()->{
            Optional<ButtonType> clickedbtn=alert.showAndWait();
            if(clickedbtn.isPresent() && clickedbtn.get()==yesbtn){
                resetgame();
            }
            else {
                Platform.exit();
                System.exit(0);
            }
        });

    }
    private class Disc extends Circle {
        private final boolean isplayeronemove;

        public Disc(boolean isplayeronemove) {
            this.isplayeronemove = isplayeronemove;
            setFill(isplayeronemove ? Color.valueOf(disc1) : Color.valueOf(disc2));
            setRadius(Circle_Diametre / 2);
            setCenterX(Circle_Diametre / 2);
            setCenterY(Circle_Diametre / 2);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
