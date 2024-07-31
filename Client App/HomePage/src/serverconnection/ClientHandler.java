package serverconnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private Consumer<String> onMessageReceived;

    public ClientHandler(Socket socket, Consumer<String> onMessageReceived) {
        this.clientSocket = socket;
        this.onMessageReceived = onMessageReceived;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String message;
            while ((message = receiveMessage()) != null) {
                handleClientMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    private void handleClientMessage(String message) {
        if (message.startsWith("INVITE_REQUEST:")) {
            String inviterName = message.substring("INVITE_REQUEST:".length());
            Platform.runLater(() -> showInvitationDialog(inviterName));
        } else if (message.startsWith("INVITE_SENT")) {
            Platform.runLater(() -> showAlert("Invitation Sent", "Invitation was successfully sent."));
        } else if (message.startsWith("INVITE_FAIL")) {
            Platform.runLater(() -> showAlert("Invitation Failed", "Failed to send invitation."));
        } else if (onMessageReceived != null) {
            onMessageReceived.accept(message);
        }
    }

    private void showInvitationDialog(String inviterName) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Game Invitation");
        alert.setHeaderText(inviterName + " has invited you to play Tic-Tac-Toe.");
        alert.setContentText("Do you accept the invitation?");

        ButtonType acceptButton = new ButtonType("Accept");
        ButtonType declineButton = new ButtonType("Decline");
        alert.getButtonTypes().setAll(acceptButton, declineButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == acceptButton) {
                sendMessage("ACCEPT_INVITE:" + inviterName);
            } else if (response == declineButton) {
                sendMessage("DECLINE_INVITE:" + inviterName);
            }
        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    private String receiveMessage() throws IOException {
        return in.readLine();
    }

    private void closeResources() {
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (clientSocket != null) {
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
