package auth;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import pagemanager.Navigation;
import serverconnection.ServerConnect;

public class FXMLLoginController implements Initializable {

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;
    
    public static String userEmail;
    


    @FXML
    private void handleSignUpButton(ActionEvent event) {
        Navigation.nextPage(event, "/auth/FXMLSignUp.fxml");
    }

    @FXML
    private void handleBackHButton(ActionEvent event) {
        Navigation.nextPage(event, "/homepage/FXMLHome.fxml");
    }

    @FXML
    private void handleLoginButton(ActionEvent event) {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Input Error", "Please enter both email and password.");
            return;
        }

        try {
            ServerConnect.makeConnectionWithServer();
            ServerConnect.sendMessage("LOGIN:" + email + ":" + password);

            String response = ServerConnect.receiveMessage();
            if (response != null) {
                if (response.startsWith("LOGIN_SUCCESS")) {
                    //String username = response.split(":")[1];
                    userEmail = email;
                    Navigation.nextPage(event, "/invitetoplay/FXMLInvite.fxml");
                } else {
                    showAlert("Error", "Invalid email or password. Please try again.");
                }
            }
        } catch (IOException e) {
            showAlert("Error", "Could not connect to the server. Please try again later.");
            e.printStackTrace();
        } finally {
            try {
                ServerConnect.closeConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialization code here
    }
}