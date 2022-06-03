package it.unical.sadstudents.mediaplayeruid.view;

import it.unical.sadstudents.mediaplayeruid.MainApplication;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class SceneHandler {
    private Scene scene;
    private Stage stage;
    private SimpleDoubleProperty screenSizeHeigth = new SimpleDoubleProperty(1200);
    private SimpleDoubleProperty screenSizeWidth = new SimpleDoubleProperty(800);

    public Stage getStage() {
        return stage;
    }

    private String theme = "dark";
    private Alert alert = new Alert(Alert.AlertType.INFORMATION);

    private static SceneHandler instance = null;
    private SimpleStringProperty currentMidPane = new SimpleStringProperty("home-view.fxml"); //aggiunto per switchautomatico
    private Pane subScene;

    public static SceneHandler getInstance(){
        if (instance==null)
            instance = new SceneHandler();
        return instance;
    }

    private SceneHandler(){    }

    public void init(Stage mainStage) throws Exception {
        stage = mainStage;
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
        scene = new Scene(loader.load(), screenSizeHeigth.get(), screenSizeWidth.get());
        stage.setTitle("MediaPlayer UID");
        stage.setMinHeight(600);
        stage.setMinWidth(750);
        /*for (String font : List.of("fonts/Roboto/Roboto-Regular.ttf", "fonts/Roboto/Roboto-Bold.ttf")) {
            Font.loadFont(Objects.requireNonNull(MainApplication.class.getResource(font)).toExternalForm(), 10);
            //Font.loadFont((MainApplication.class.getResource(font)).toExternalForm(), 10);

        }*/
        for (String style : List.of("css/"+theme+".css", /*"css/fonts.css",*/ "css/style.css")) {
            String resource = Objects.requireNonNull(MainApplication.class.getResource(style)).toExternalForm();
            scene.getStylesheets().add(resource);
        }
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                Platform.exit();
                System.exit(0);
            }
        });

    }

    public String getCurrentMidPane() {
        return currentMidPane.get();
    }

    public SimpleStringProperty currentMidPaneProperty() {
        return currentMidPane;
    }

    public void setCurrentMidPane(String currentMidPane) {
        this.currentMidPane.set(currentMidPane);
    }

    public Pane switchPane(){
        try{
            subScene = new FXMLLoader().load(MainApplication.class.getResource(currentMidPane.get()));
        }catch(IOException e){}

        return subScene;

    }

    public double getScreenSizeHeigth() {
        return screenSizeHeigth.get();
    }

    public SimpleDoubleProperty screenSizeHeigthProperty() {
        return screenSizeHeigth;
    }

    public void setScreenSizeHeigth(double screenSizeHeigth) {
        this.screenSizeHeigth.set(screenSizeHeigth);
    }

    public double getScreenSizeWidth() {
        return screenSizeWidth.get();
    }

    public SimpleDoubleProperty screenSizeWidthProperty() {
        return screenSizeWidth;
    }

    public void setScreenSizeWidth(double screenSizeWidth) {
        this.screenSizeWidth.set(screenSizeWidth);
    }
}