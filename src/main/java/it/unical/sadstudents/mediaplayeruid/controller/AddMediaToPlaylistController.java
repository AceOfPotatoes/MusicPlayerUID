package it.unical.sadstudents.mediaplayeruid.controller;

import it.unical.sadstudents.mediaplayeruid.model.*;
import it.unical.sadstudents.mediaplayeruid.utils.SearchForFile;
import it.unical.sadstudents.mediaplayeruid.view.SceneHandler;
import it.unical.sadstudents.mediaplayeruid.view.SubStageHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddMediaToPlaylistController implements Initializable {

    private ArrayList<Integer> PosizioniSelezionate;
    private ObservableList<MyMedia> temp = FXCollections.observableArrayList();
    private String tabSelezionata="MusicLibrary";


    @FXML
    private TableView<MyMedia> tableViewSelection;


    @FXML
    private TableColumn<MyMedia,String> title, artist, album, genre, year, length;

    @FXML
    private Button ButtonAddToPlayList;

    @FXML
    private Button btnAddToPlaylist;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnMoveDown;

    @FXML
    private Button btnMoveUp;

    @FXML
    private ImageView imageMedia;

    @FXML
    private Label labelTitle;

    @FXML
    private Button buttonMusicLibrary;


    @FXML
    private Button buttonVideoLibrary;

    @FXML
    private TableView<MyMedia> tableViewPlaylist;

    @FXML
    private TableColumn<MyMedia, String> album1,artist1,genre1,length1,title1,year1;

    @FXML
    private TextField textField;




    @FXML
    void onAddToPlayListNewAction(ActionEvent event) {
        int index = tableViewSelection.getSelectionModel().getSelectedIndex();
        if(index>=0){
            temp.add(tableViewSelection.getItems().get(index));
            SubStageHandler.getInstance().setUpdated(true);
        }
    }



    @FXML
    void onDownOrderAction(ActionEvent event) {
        int index=tableViewPlaylist.getSelectionModel().getSelectedIndex();
        if (index<temp.size()-1){
            temp.add(index,temp.get(index+1));
            temp.remove(index+2);
            SubStageHandler.getInstance().setUpdated(true);

        }

    }


    @FXML
    void onRemoveAction(ActionEvent event) {
        int index = tableViewPlaylist.getSelectionModel().getSelectedIndex();
        if(0<=index && index<temp.size()){
            temp.remove(index);
            tableViewPlaylist.getSelectionModel().select(index);
            SubStageHandler.getInstance().setUpdated(true);

        }


    }

    @FXML
    void onUpOrderAction(ActionEvent event) {
        int index=tableViewPlaylist.getSelectionModel().getSelectedIndex();
        if (index>0){
            temp.add(index-1,temp.get(index));
            temp.remove(index+1);
            tableViewPlaylist.getSelectionModel().select(index-1);
            SubStageHandler.getInstance().setUpdated(true);
        }

    }

    @FXML
    void onMusicLibrary(ActionEvent event) {
        //reset();
        tabSelezionata="MusicLibrary";
        tableViewSelection.setItems(MusicLibrary.getInstance().getMusicLibrary());
    }


    @FXML
    void onVideoLibrary(ActionEvent event) {
        //reset();
        tabSelezionata="VideoLibrary";
        tableViewSelection.setItems(VideoLibrary.getInstance().getVideoLibrary());
    }

    @FXML
    void onSaveAction(ActionEvent event) {
        int index = PlaylistCollection.getInstance().getPlaylistWidthName(SubStageHandler.getInstance().getPlaylistName());
        PlaylistCollection.getInstance().getPlayListsCollections().get(index).clearMyList();
        if(temp.size()>0)
            PlaylistCollection.getInstance().getPlayListsCollections().get(index).clearSongs();

        for(int i=0; i< temp.size();i++){
            PlaylistCollection.getInstance().getPlayListsCollections().get(index).addMedia(temp.get(i));
        }
        PlaylistCollection.getInstance().setUpdatePlaylist(true);
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
    private int indexPlaylist;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startToolTip();
        setButtonAdd(false);
        indexPlaylist = PlaylistCollection.getInstance().returnPlaylist(SubStageHandler.getInstance().getPlaylistName());
        String image = PlaylistCollection.getInstance().getPlayListsCollections().get(indexPlaylist).getImage();
        if(image!=null&&image!="")
            imageMedia.setImage(new Image(image));
        labelTitle.setText(SubStageHandler.getInstance().getPlaylistName());

        for(MyMedia myMedia: PlaylistCollection.getInstance().getPlayListsCollections().get(indexPlaylist).getMyList())
            temp.add(myMedia);



        tableViewPlaylist.setItems(temp);


        title1.setCellValueFactory(new PropertyValueFactory<MyMedia,String>("title"));
        artist1.setCellValueFactory(new PropertyValueFactory<MyMedia,String>("artist"));
        album1.setCellValueFactory(new PropertyValueFactory<MyMedia,String>("album"));
        genre1.setCellValueFactory(new PropertyValueFactory<MyMedia,String>("genre"));
        year1.setCellValueFactory(new PropertyValueFactory<MyMedia,String>("year"));
        length1.setCellValueFactory(new PropertyValueFactory<MyMedia,String>("length"));



        tableViewSelection.setItems(MusicLibrary.getInstance().getMusicLibrary());

        title.setCellValueFactory(new PropertyValueFactory<MyMedia,String>("title"));
        artist.setCellValueFactory(new PropertyValueFactory<MyMedia,String>("artist"));
        album.setCellValueFactory(new PropertyValueFactory<MyMedia,String>("album"));
        genre.setCellValueFactory(new PropertyValueFactory<MyMedia,String>("genre"));
        year.setCellValueFactory(new PropertyValueFactory<MyMedia,String>("year"));
        length.setCellValueFactory(new PropertyValueFactory<MyMedia,String>("length"));



        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(tabSelezionata.equals("MusicLibrary"))
                    tableViewSelection.setItems(SearchForFile.getInstance().getSearch(newValue, MusicLibrary.getInstance().getMusicLibrary()));
                else
                    tableViewSelection.setItems(SearchForFile.getInstance().getSearch(newValue, VideoLibrary.getInstance().getVideoLibrary()));
            }
        });


        SceneHandler.getInstance().scaleTransition(btnAddToPlaylist);
        SceneHandler.getInstance().scaleTransition(btnDelete);
        SceneHandler.getInstance().scaleTransition(btnMoveDown);
        SceneHandler.getInstance().scaleTransition(btnMoveUp);
        SceneHandler.getInstance().scaleTransition(ButtonAddToPlayList);
    }
    public void setButtonAdd(boolean enable){
        ButtonAddToPlayList.setDisable(enable);
    }

    public int presente(int index){
        for(int i=0;i<PosizioniSelezionate.size();i++) {
            if (PosizioniSelezionate.get(i) == index)
                return i;
        }
        return -1;
    }

    private void startToolTip() {
        btnAddToPlaylist.setTooltip(new Tooltip("Add to playlist"));
        btnDelete.setTooltip(new Tooltip("Remove song"));
        btnMoveDown.setTooltip(new Tooltip("Move down"));
        btnMoveUp.setTooltip(new Tooltip("Move up"));
        ButtonAddToPlayList.setTooltip(new Tooltip("Save your changes"));
        textField.setTooltip(new Tooltip("Search for the song"));
        // TODO: 11/07/2022 aggiungi 
        buttonMusicLibrary.setTooltip(new Tooltip(""));
        buttonVideoLibrary.setTooltip(new Tooltip(""));
    }
    public void reset(){
        // TODO: 02/07/2022  non funziona il reset 
        for(int i=0;i<PosizioniSelezionate.size();i++){
            TableRow<MyMedia> row= tableViewSelection.getRowFactory().call(tableViewSelection);
            row.setStyle("-fx-background-color: white;");
        }
    }

}

