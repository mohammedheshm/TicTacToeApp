package auth;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import pagemanager.Navigation;

import java.net.URL;
import java.util.ResourceBundle;
import serverconnection.ServerConnect;


public class FXMLSignUpController implements Initializable {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailtxtField;

    @FXML
    private PasswordField passwordtxtField;

    @FXML
    private PasswordField rePasswordField;
    
    @FXML
    private void handleBackLoginButton(ActionEvent event) {
        Navigation.nextPage(event, "/auth/FXMLLogin.fxml");
    }

    @FXML
    private void handleSignUpButton(ActionEvent event) {
        String username = usernameField.getText();
        String email = emailtxtField.getText();
        String password = passwordtxtField.getText();
        String rePassword = rePasswordField.getText();

        // Validate inputs
        if (username == null || username.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            rePassword == null || rePassword.trim().isEmpty()) {
            showAlert("Input Error", "Please fill in all fields.");
            return;
        }

        if (!password.equals(rePassword)) {
            showAlert("Password Error", "Passwords do not match.");
            return;
        }

        // Check if email already exists
        try {
            ServerConnect.makeConnectionWithServer();
            ServerConnect.sendMessage("ISEXIST:" + email + ":" + password);

            String response = ServerConnect.receiveMessage();
            if (response != null) {
                if (response.startsWith("USER_EXIST")) {
                    showAlert("Error", "An account with this email already exists.");
                }
                else {
                    ServerConnect.sendMessage("REGISTER:" + username + ":"  +  password + ":"  +email );
                   
                    if (response.startsWith("USER_NOTEXIST"))
                   {
                       showAlert("Success", "Account created successfully!");
                   }
                    
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
        

        // Navigate to login page
        Navigation.nextPage(event, "/auth/FXMLLogin.fxml");
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
        // Initialize if needed
    }
}