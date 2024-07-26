/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package localmodes;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import pagemanager.Navigation;

/**
 * FXML Controller class
 *
 * @author rawan
 */
public class LocalModesController implements Initializable {

     public static boolean isTwoPlayers = false;
    
    
    @FXML
    private void handleComputerModeButton(ActionEvent event) {
        isTwoPlayers = false;

        Navigation.nextPage(event, "/localpage/FXMLLocal.fxml");

    }
    
     @FXML
    private void handleTwoPlayersButton(ActionEvent event) {
 isTwoPlayers = true;
        Navigation.nextPage(event, "/gamewindow/FXMLGameWindow.fxml");

    }
    
     @FXML
    private void handleBackHButton(ActionEvent event) {

        Navigation.nextPage(event, "/homepage/FXMLHome.fxml");
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
