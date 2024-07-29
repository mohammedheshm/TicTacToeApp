
package auth;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import pagemanager.Navigation;


public class FXMLLoginController implements Initializable {

        
    
   
      @FXML
    private void handleSignUpButton(ActionEvent event) {
              
        Navigation.nextPage(event, "/auth/FXMLSignUp.fxml");
        
        
    }
   
    
       @FXML
    private void handleBackHButton(ActionEvent event) {
              
        Navigation.nextPage(event, "/homepage/FXMLHome.fxml");

    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
    }    
    
}
