/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pagemanager;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Navigation {
    
    public static void nextPage(ActionEvent event, String fxmlPath) {
        try {
            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
            // Load the new scene from FXML
            Parent newSceneRoot = FXMLLoader.load(Navigation.class.getResource(fxmlPath));
            
            // Set the new scene
            Scene newScene = new Scene(newSceneRoot);
            stage.setScene(newScene);
            
            // Show the new scene
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error dialog)
        }
    }
}
