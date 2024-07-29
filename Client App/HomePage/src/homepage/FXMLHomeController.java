
package homepage;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import pagemanager.Navigation;


public class FXMLHomeController implements Initializable {

    @FXML
    private Label label;

    @FXML
    private void handleLocalModeButton(ActionEvent event) {

        Navigation.nextPage(event, "/localmodes/LocalModes.fxml");

    }

    @FXML
    private void handleLeaderBoardButton(ActionEvent event) {

        Navigation.nextPage(event, "/leaderboardpage/FXMLLeaderBoard.fxml");

    }

    @FXML
    private void handleOnlineModeButton(ActionEvent event) {

        Navigation.nextPage(event, "/auth/FXMLLogin.fxml");

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}
