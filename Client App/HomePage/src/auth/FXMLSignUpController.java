
package auth;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import pagemanager.Navigation;

public class FXMLSignUpController implements Initializable {

   //#handleBackLoginButton
    
    
         @FXML
    private void handleBackLoginButton(ActionEvent event) {
              
        Navigation.nextPage(event, "/auth/FXMLLogin.fxml");

    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
