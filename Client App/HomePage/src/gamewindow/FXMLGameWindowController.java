
package gamewindow;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import pagemanager.Navigation;


public class FXMLGameWindowController implements Initializable {

  

    
    
       @FXML
    public void handleBackHButton(ActionEvent  event) {
            
        
       Navigation.nextPage(event, "/homepage/FXMLHome.fxml");
       
       
    }
    
        @FXML
    public void handleScreenShootButton(ActionEvent  event) {
            
        
       
       
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
     
    }    
    
}
