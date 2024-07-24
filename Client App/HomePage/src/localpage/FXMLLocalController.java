/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package localpage;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import pagemanager.Navigation;

/**
 * FXML Controller class
 *
 * @author user
 */
public class FXMLLocalController implements Initializable {

    
     @FXML
    private void handleEasyModeButton(ActionEvent event) {
              
        Navigation.nextPage(event, "/gamewindow/FXMLGameWindow.fxml");

    }
    
        @FXML
    private void handleMediumModeButton(ActionEvent event) {
             
        Navigation.nextPage(event, "/gamewindow/FXMLGameWindow.fxml");

    }
        @FXML
    private void handleHardModeButton(ActionEvent event) {
              
        Navigation.nextPage(event, "/gamewindow/FXMLGameWindow.fxml");
    }
    
            @FXML
    private void handleTwoPlayersButton(ActionEvent event) {
              
        Navigation.nextPage(event, "/gamewindow/FXMLGameWindow.fxml");
    }
    
    
    
             @FXML
    private void handleBackHButton(ActionEvent event) {
              
        Navigation.nextPage(event, "/homepage/FXMLHome.fxml");
    }
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
