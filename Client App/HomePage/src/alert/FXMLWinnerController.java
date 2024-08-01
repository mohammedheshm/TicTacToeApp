/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alert;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;


public class FXMLWinnerController implements Initializable {

    @FXML
    private MediaView loservideo;

    private MediaPlayer mediaPlayer;

    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         // Convert the path to URI format
    String videoPath = new File("D:\\newviedo\\awed.mp4").toURI().toString();

    // Create a Media object
    Media media = new Media(videoPath);

    // Create a MediaPlayer object
    mediaPlayer = new MediaPlayer(media);

    // Set the MediaPlayer to the MediaView
    loservideo.setMediaPlayer(mediaPlayer);
    // Set the size of the MediaView
    loservideo.setFitWidth(200);  // Set desired width
    loservideo.setFitHeight(200); // Set desired height
    loservideo.setPreserveRatio(true); // Preserve aspect ratio

    // Optionally, you can play the video automatically
    mediaPlayer.play();

    }    
    
}