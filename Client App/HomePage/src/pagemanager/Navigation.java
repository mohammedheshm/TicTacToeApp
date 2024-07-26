/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pagemanager;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Navigation {
    
    public static void nextPage(ActionEvent event, String fxmlPath) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
            Parent newSceneRoot = FXMLLoader.load(Navigation.class.getResource(fxmlPath));
            
            Scene newScene = new Scene(newSceneRoot);
            stage.setScene(newScene);
            
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            
        }
    }
    
public static void showAlert(ActionEvent event, String fxmlPath, String message, String title, Image image) {
    if (fxmlPath == null || fxmlPath.isEmpty()) {
        System.err.println("FXML path is null or empty.");
        return;
    }

    if (message == null) {
        System.err.println("Message is null.");
        return;
    }

    try {
        FXMLLoader loader = new FXMLLoader(Navigation.class.getResource(fxmlPath));
        Parent dialogRoot = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setScene(new Scene(dialogRoot));
        dialogStage.initStyle(StageStyle.UTILITY);

        if (event != null) {
            dialogStage.initOwner(((Node) event.getSource()).getScene().getWindow());
        } else {
            System.err.println("Event is null.");
        }

        Object controller = loader.getController();
        if (controller != null) {
            try {
                controller.getClass().getMethod("setDialogStage", Stage.class).invoke(controller, dialogStage);
                controller.getClass().getMethod("setMessage", String.class).invoke(controller, message);
                controller.getClass().getMethod("setTitle", String.class).invoke(controller, title);
                controller.getClass().getMethod("setImage", Image.class).invoke(controller, image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Controller is null.");
        }

        dialogStage.showAndWait();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}
