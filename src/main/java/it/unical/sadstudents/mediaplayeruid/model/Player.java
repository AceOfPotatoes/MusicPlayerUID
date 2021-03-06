package it.unical.sadstudents.mediaplayeruid.model;

import it.unical.sadstudents.mediaplayeruid.utils.MyNotification;
import it.unical.sadstudents.mediaplayeruid.utils.ThreadManager;
import it.unical.sadstudents.mediaplayeruid.utils.UpdateMetadata;
import it.unical.sadstudents.mediaplayeruid.view.SceneHandler;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import java.util.Timer;
import java.util.TimerTask;

public class Player {
    //VARIABLES
    private int skipMilliseconds = 10000;
    private Double volume = 100.0 ;
    private Timer timer;
    private TimerTask task;
    private boolean runningTimer;
    private Media media;
    private MediaPlayer mediaPlayer;
    private MediaView mediaView ;
    private Integer index;
    private boolean loopMode;
    private double unmuteVolumeValue=100;
    private double rate=1;

    //END VARIABLES

    //PROPERTIES
    private SimpleDoubleProperty currentMediaTime = new SimpleDoubleProperty(0);
    private SimpleDoubleProperty endMediaTime = new SimpleDoubleProperty(0);
    private SimpleStringProperty artistName = new SimpleStringProperty();
    private SimpleStringProperty mediaName = new SimpleStringProperty();
    private SimpleBooleanProperty mediaLoaded = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty isRunning = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty isAVideo = new SimpleBooleanProperty(false);
    public boolean getIsAVideo() { return isAVideo.get(); }
    public SimpleBooleanProperty isAVideoProperty() { return isAVideo; }
    public void setIsAVideo(boolean isAVideo) { this.isAVideo.set(isAVideo); }

    //END PROPERTIES

    //SINGLETON
    private static Player instance = null;
    private Player() {    }
    public static Player getInstance(){
        if (instance==null)
            instance = new Player();
        return instance;
    }
    //END SINGLETON


    //VARIABLES GETTERS AND SETTERS
    public void setRate(double rate) {
        this.rate = rate;
    }
    public double getUnmuteVolumeValue() {
        return unmuteVolumeValue;
    }
    public void setUnmuteVolumeValue(double unmuteVolumeValue) {
        this.unmuteVolumeValue = unmuteVolumeValue;
    }

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

    public boolean isLoopMode() { return loopMode; }
    public void setLoopMode(boolean loopMode) { this.loopMode = loopMode; }

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
            if(index>=0 && index<PlayQueue.getInstance().getQueue().size()) {
                this.index = index;

                try {
                    media = new Media(PlayQueue.getInstance().getQueue().get(index).getPath());

                    if (media.getSource().toLowerCase().endsWith(".mp4")) {
                        isAVideo.set(true);
                    } else {
                        isAVideo.set(false);
                        SceneHandler.getInstance().setRequestedVideoView(false);
                    }

                    mediaPlayer = new MediaPlayer(media);
                    mediaView.setMediaPlayer(mediaPlayer);

                    Platform.runLater(() -> {
                        mediaName.set(PlayQueue.getInstance().getQueue().get(index).getTitle());
                        artistName.set(PlayQueue.getInstance().getQueue().get(index).getArtist());
                    });
                    playMedia();
                } catch (MediaException mediaException) {
                    MyMedia temp = PlayQueue.getInstance().getQueue().get(index);
                    VideoLibrary.getInstance().removeSafe(temp);
                    MusicLibrary.getInstance().deleteStandard(temp);
                    if(PlayQueue.getInstance().getQueue().size()>0 && PlayQueue.getInstance().getCurrentMedia()<PlayQueue.getInstance().getQueue().size())
                        PlayQueue.getInstance().startMedia();
                    MyNotification.notifyError("ERROR",temp.getPath()+System.lineSeparator()+"not supported or not found",5);
                }
            }
    }

    public void playMedia(){
        if(media != null){
            mediaLoaded.set(true);
            mediaPlayer.setVolume(volume);
            mediaPlayer.setRate(rate);
            mediaPlayer.play();
            isRunning.set(true);
            for(int i = 0; i < mediaPlayer.getAudioEqualizer().getBands().size(); ++i)
                mediaPlayer.getAudioEqualizer().getBands().get(i).setGain(AudioEqualizer.getInstance().getPresetsValues().get(AudioEqualizer.getInstance().getCurrentPresetIndex())[i]);
            if(rate!=1){
                mediaPlayer.setRate(rate);
            }
            ThreadManager.getInstance().beginTimer();
            Home.getInstance().addToRecentMedia(PlayQueue.getInstance().getQueue().get(index));
            UpdateMetadata.getInstance().updateMetadata(PlayQueue.getInstance().getQueue().get(index),media);
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
    }

    public void tenSecondBack() {
        mediaPlayer.seek(Duration.millis((mediaPlayer.getCurrentTime().toMillis()-skipMilliseconds)));
    }

    public void tenSecondForward() {
        mediaPlayer.seek(Duration.millis((mediaPlayer.getCurrentTime().toMillis()+skipMilliseconds)));
    }

    public void stop(){
        //mediaPlayer.stop();
        mediaPlayer.dispose();
        mediaLoaded.set(false);
        currentMediaTime.set(0);
        endMediaTime.set(0);
        isRunning.set(false);
        isAVideo.set(false);
        mediaName.set("");
        artistName.set("");
        ThreadManager.getInstance().cancelTimer();
    }

    public void repeat() {
        if(!loopMode)
            loopMode = true;
        else
            loopMode = false;
    }

    public void setVolume(double v) {
        volume=v;
        mediaPlayer.setVolume(v);
    }
    //END BASIC CONTROLS

}
