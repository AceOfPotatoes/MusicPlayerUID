package it.unical.sadstudents.mediaplayeruid.view;

import it.unical.sadstudents.mediaplayeruid.model.MyMedia;
import it.unical.sadstudents.mediaplayeruid.model.VideoLibrary;
import it.unical.sadstudents.mediaplayeruid.utils.ImageCreator;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.scene.input.ContextMenuEvent;

import java.util.ArrayList;

public class VideoGalleryTilePaneHandler {
    private SimpleIntegerProperty readyInteger = new SimpleIntegerProperty(0);
    private ArrayList<MyMediaSingleBox> myMediaSingleBoxes = new ArrayList<>();
    private Thread thread;
    private int selected=-1;


    //SINGLETON
    private static VideoGalleryTilePaneHandler instance = null;
    private VideoGalleryTilePaneHandler(){    }
    public static VideoGalleryTilePaneHandler getInstance(){
        if (instance==null)
            instance = new VideoGalleryTilePaneHandler();
        return instance;
    }
    //END SINGLETON


    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public int getReadyInteger() {
        return readyInteger.get();
    }

    public SimpleIntegerProperty readyIntegerProperty() {
        return readyInteger;
    }

    public void setReadyInteger(int readyInteger) {
        this.readyInteger.set(readyInteger);
    }

    public ArrayList<MyMediaSingleBox> getMyMediaSingleBoxes() {
        return myMediaSingleBoxes;
    }

    public void setMyMediaSingleBoxes(ArrayList<MyMediaSingleBox> myMediaSingleBoxes) {
        this.myMediaSingleBoxes = myMediaSingleBoxes;
    }

    public void listCreator(){
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i< VideoLibrary.getInstance().getVideoLibrary().size(); i++){
                    MyMediaSingleBox myMediaSingleBox = new MyMediaSingleBox(VideoLibrary.getInstance().getVideoLibrary().get(i),"video");
                    myMediaSingleBox.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                        @Override
                        public void handle(ContextMenuEvent contextMenuEvent) {
                            myMediaSingleBox.contextMenu(myMediaSingleBox,contextMenuEvent.getScreenX(),contextMenuEvent.getScreenY());
                        }
                    });

                    myMediaSingleBoxes.add(myMediaSingleBox);
                    readyInteger.set(readyInteger.get()+1);
                }


                for (int i = 0; i< myMediaSingleBoxes.size(); ++i){
                    ImageCreator imageCreator = new ImageCreator();
                    imageCreator.setPane(myMediaSingleBoxes.get(i));
                    imageCreator.start();
                }

            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    public void removeWithIndex(MyMedia myMedia){
        for(int i=0;i<myMediaSingleBoxes.size();i++){
            if(myMedia.equals(myMediaSingleBoxes.get(i).getMyMedia())){
                myMediaSingleBoxes.remove(i);
                readyInteger.set(readyInteger.get()-1);
            }

        }


    }

    public void addSingleItem(){

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                MyMediaSingleBox myMediaSingleBox = new MyMediaSingleBox(VideoLibrary.getInstance().getVideoLibrary().get(VideoLibrary.getInstance().getVideoLibrary().size()-1),"video");
                myMediaSingleBox.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                    @Override
                    public void handle(ContextMenuEvent contextMenuEvent) {
                        myMediaSingleBox.contextMenu(myMediaSingleBox,contextMenuEvent.getScreenX(),contextMenuEvent.getScreenY());
                    }
                });

                myMediaSingleBoxes.add(myMediaSingleBox);
                readyInteger.set(readyInteger.get()+1);

                ImageCreator imageCreator = new ImageCreator();
                imageCreator.setPane(myMediaSingleBoxes.get(myMediaSingleBoxes.size()-1));
                imageCreator.start();



            }
        });

        thread.setDaemon(true);
        thread.start();
    }


}
