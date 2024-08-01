package invitetoplay;

import auth.FXMLLoginController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import serverconnection.ServerConnect;
import models.User;
import pagemanager.Navigation;

public class FXMLInviteController {

    @FXML
    private TableView<User> tableView;

    @FXML
    private TableColumn<User, String> playerColumn;

    @FXML
    private TableColumn<User, Integer> scoreColumn;

    @FXML
    private TableColumn<User, String> statusColumn;

    private String userEmail = FXMLLoginController.userEmail;
    private String user2Email;
    private static ScheduledExecutorService scheduler;

    @FXML
    public void initialize() {
        playerColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        statusColumn.setCellValueFactory(cellData
                -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus() ? "Online" : "Offline"));

        tableView.setRowFactory(tv -> new TableRow<User>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else {
                    setStyle(item.getStatus() ? "-fx-background-color: #D0B8A8;" : "-fx-background-color: #E0E0E0;");
                }
            }
        });

        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> Platform.runLater(() -> {
            try {
                loadData();
            } catch (IOException e) {
                showAlert("Error", "Unable to load user data.");
            }
        }), 0, 10, TimeUnit.SECONDS);
    }

    private void loadData() throws IOException {
        try {
            ServerConnect.makeConnectionWithServer();
            ServerConnect.sendMessage("GETPLAYERSSTATUS:" + FXMLLoginController.userEmail);

            handelServerMessage();

        } catch (IOException e) {
            showAlert("Error", "Unable to load user data.");
        }
    }

    private void updateView(String[] resArr) {
        ArrayList<User> usersObj = new ArrayList<>();
        FXCollections.observableArrayList().clear();
        for (String u : resArr) {
            User user = new User(u);
            usersObj.add(user);
        }

        ObservableList<User> users = FXCollections.observableArrayList(usersObj);
        tableView.setItems(users);
    }

    @FXML
    private void handleInviteButton(ActionEvent event) throws IOException {
        User selectedUser = tableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null && selectedUser.getStatus()) {
            try {
                ServerConnect.makeConnectionWithServer();
                String message = "INVITE:" + selectedUser.getEmail() + ":" + userEmail;
                ServerConnect.sendMessage(message);
                String response = ServerConnect.receiveMessage();
                if ("INVITE_SENT".equals(response)) {
                    System.out.println("congratulation");
                } else {
                    showAlert("Invitation Failed", "Failed to send invitation.");
                }
            } catch (IOException ex) {
                Logger.getLogger(FXMLInviteController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            showAlert("No User Selected or User Offline", "Please select an online user from the table.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        ButtonType acceptButton = new ButtonType("Accept");
        ButtonType refuseButton = new ButtonType("Refuse");
        alert.getButtonTypes().setAll(acceptButton, refuseButton);
        alert.showAndWait().ifPresent(response -> {
            if (response == acceptButton) {
                String msg1 = "ACCEPT_INVITE:" + userEmail + ":" + user2Email;
                ServerConnect.sendMessage(msg1);
            } else if (response == refuseButton) {
                String msg2 = "DECLINE_INVITE:" + userEmail + ":" + user2Email;
                ServerConnect.sendMessage(msg2);

            }
            handelServerMessage();
        });

    }

    public static void shutdown() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }

    private void handelServerMessage() {
        try {
            String response = ServerConnect.receiveMessage();
            String[] responseArr = response.split(":");

            if (responseArr.length >= 4) { // solve error in this line
                updateView(responseArr);
            } else {
                switch (responseArr[0]) {
                    case "INVITE_REQUEST":
                        this.user2Email = responseArr[1];
                        showAlert(responseArr[0], responseArr[1] + " Want To Play With you");
                        break;
                    case "ACCEPT_INVITE":
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle(responseArr[1] + " Accept To Play With You");
                        alert.setHeaderText(null);
                        alert.setContentText("Let's Play");
                        alert.showAndWait();
                        // Navigation.nextPage(null, "/gamewindow/FXMLGameWindow.fxml");
                        break;
                    case "DECLINE_INVITE":
                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Decline Invitation from  " + responseArr[1]);
                        alert.setHeaderText(null);
                        alert.setContentText("I don't wan't play now");
                        alert.showAndWait();
                        break;
                }

            }

        } catch (IOException ex) {
            Logger.getLogger(FXMLInviteController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
