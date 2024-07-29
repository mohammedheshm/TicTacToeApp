
package auth;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class SignUpPage extends Application {
    
    @Override
    public void start(Stage stage) throws IOException {
        
       

        Parent root = FXMLLoader.load(getClass().getResource("FXMLSignUp.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        
    
    }

   
    public static void main(String[] args) {
        launch(args);
    }
    
}
