
package localpage;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import pagemanager.Navigation;


public class FXMLLocalController implements Initializable {
    
     private static String difficulty = "easy";

      public static String getDifficulty() {
        return difficulty;
    }

@FXML
   private void handleEasyButton(ActionEvent event) {
        difficulty = "easy";
        
        Navigation.nextPage(event, "/gamewindow/FXMLGameWindow.fxml");
    }

    @FXML
    private void handleMediumModeButton(ActionEvent event) {
        difficulty = "medium";
        
        Navigation.nextPage(event, "/gamewindow/FXMLGameWindow.fxml");
    }

    @FXML
    private void handleHardModeButton(ActionEvent event) {
        difficulty = "hard";
        Navigation.nextPage(event, "/gamewindow/FXMLGameWindow.fxml");
    }

    


    @FXML
    private void handleBackLMButton(ActionEvent event) {

        Navigation.nextPage(event, "/localmodes/LocalModes.fxml");
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
