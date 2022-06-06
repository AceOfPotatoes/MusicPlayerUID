package it.unical.sadstudents.mediaplayeruid.model;

import it.unical.sadstudents.mediaplayeruid.ThreadManager;
import javafx.beans.property.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.util.Timer;
import java.util.TimerTask;

public class Player {
    //VARIABLES
    private int skipMilliseconds = 10000;
    private Double volume = 50.0 ;
    private Timer timer;
    private TimerTask task;
    private boolean runningTimer;
    private Media media;
    private MediaPlayer mediaPlayer;
    private MediaView mediaView ;
    //END VARIABLES

    //PROPERTIES
    private SimpleDoubleProperty currentMediaTime = new SimpleDoubleProperty(0);
    private SimpleDoubleProperty endMediaTime = new SimpleDoubleProperty(0);
    private SimpleStringProperty artistName = new SimpleStringProperty();
    private SimpleStringProperty mediaName = new SimpleStringProperty();
    private SimpleBooleanProperty mediaLoaded = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty isRunning = new SimpleBooleanProperty(false);
    //END PROPERTIES

    //SINGLETON
    private static Player instance = null;
    private Player() { }
    public static Player getInstance(){
        if (instance==null)
            instance = new Player();
        return instance;
    }
    //END SINGLETON


    //VARIABLES GETTERS AND SETTERS
    public boolean isMediaLoaded() {
        return mediaLoaded.get();
    }
    public void setMediaLoaded(boolean mediaLoaded) { this.mediaLoaded.set(mediaLoaded);}
    public SimpleBooleanProperty mediaLoadedProperty() {
        return mediaLoaded;
    }

    public void setMediaName(String mediaName) {
        this.mediaName.set(mediaName);
    }
    public String getMediaName() {
        return mediaName.get();
    }
    public SimpleStringProperty mediaNameProperty() {
        return mediaName;
    }

    public SimpleBooleanProperty isRunningProperty() {
        return isRunning;
    }
    public boolean getIsRunning() { return isRunning.get(); }

    public double getCurrentMediaTime() { return currentMediaTime.get(); }
    public void setCurrentMediaTime(double currentMediaTime) { this.currentMediaTime.set(currentMediaTime); }
    public SimpleDoubleProperty currentMediaTimeProperty() { return currentMediaTime; }

    public void setEndMediaTime(double endMediaTime) { this.endMediaTime.set(endMediaTime); }
    public double getEndMediaTime() { return endMediaTime.get(); }
    public SimpleDoubleProperty endMediaTimeProperty() { return endMediaTime; }

    public String getArtistName() { return artistName.get(); }
    public SimpleStringProperty artistNameProperty() { return artistName; }
    public void setArtistName(String artistName) { this.artistName.set(artistName); }
    //END VARIABLES GETTERS AND SETTERS

    //GETTERS AND SETTERS NEEDED TO SET VIDEO TAB
    public MediaView getMediaView() {
        return mediaView;
    }
    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
    public void setMediaView(MediaView mediaView) {
        this.mediaView = mediaView;
    }
    //END GETTERS AND SETTERS


    //FUNCTIONS: START POINT FOR MEDIA REPRODUCTION
    public void createMedia(Integer index){
        mediaName.set(PlayQueue.getInstance().getQueue().get(index).getTitle());
        media = new Media(PlayQueue.getInstance().getQueue().get(index).getPath());
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);
        mediaLoaded.set(true);
        playMedia();
        ThreadManager.getInstance().setNameAndArtistLabels(media);
        //TODO: REGEX per riproduzione *.mp4
    }

    public void playMedia(){
        if(media != null){
            mediaPlayer.setVolume(volume);
            mediaPlayer.play();
            isRunning.set(true);
            ThreadManager.getInstance().beginTimer();
        }
    }
    //END FUNCTION START POINT FOR MEDIA REPRODUCTION

    //FUNCTION: BASIC MEDIA CONTROLS
    public void pauseMedia(){
        if(media != null){
            mediaPlayer.pause();
            isRunning.set(false);
            ThreadManager.getInstance().cancelTimer();
        }
    }

    public void changePosition(double position){
        mediaPlayer.seek(Duration.seconds(position));
        ThreadManager.getInstance().beginTimer();
    }

    public void tenSecondBack() {
        mediaPlayer.seek(Duration.millis((mediaPlayer.getCurrentTime().toMillis()-skipMilliseconds)));

    }

    public void tenSecondForward() {
        mediaPlayer.seek(Duration.millis((mediaPlayer.getCurrentTime().toMillis()+skipMilliseconds)));
    }

    public void stop(){
        pauseMedia();
        setMediaLoaded(false);
        mediaNameProperty().set("");
        artistNameProperty().set("");
    }

    public void restart() {
        if(media != null)
            mediaPlayer.seek(Duration.seconds(0.0));
        mediaPlayer.play();
        ThreadManager.getInstance().beginTimer();
    }

    public void setVolume(double v) {
        volume= v;
        mediaPlayer.setVolume(v);
    }
    //END BASIC CONTROLS
}
