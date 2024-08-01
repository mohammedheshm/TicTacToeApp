package server;

import Dao.UserManager;
import models.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable {

    private Socket clientSocket;
    private TicTacToeServer server;
    private PrintWriter out;
    private BufferedReader in;
    String useremail;

    public ClientHandler(Socket socket, TicTacToeServer server) {
        this.clientSocket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String message;
            while ((message = in.readLine()) != null) {
                handleClientMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClientMessage(String message) {
        String[] parts = message.split(":");

        switch (parts[0]) {
            case "LOGIN":
                handleLogin(parts[1], parts[2]);
                break;
            case "REGISTER":
                handleRegister(parts[1], parts[2], parts[3]);
                break;
            case "ISEXIST":
                handleISEXIST(parts[1]);
                break;
            case "GETPLAYERSSTATUS":
                handleGetAll(parts[1]);
                break;
            case "INVITE":
                handleInvite(parts[1], parts[2]);
                break;
            case "ACCEPT_INVITE":
                System.out.println("Accepted Done !" + parts[1] + parts[2]);
                handleAcceptInvite(parts[2] , parts[1]);
                break;

            case "DECLINE_INVITE":
                handleDeclineInvite(parts[2], parts[1]);
                break;
            default:
                System.out.println("Unknown message type: " + parts[0]);
        }
    }

    private void handleInvite(String invitedUserEmail, String userEmail) {
        boolean inviteSent = false;
        System.out.println("Invite from :" + userEmail + " To " + invitedUserEmail);
        for (ClientHandler client : server.getClients()) {
            if (client.getUseremail() != null && client.getUseremail().equals(invitedUserEmail)) {
                client.sendMessage("INVITE_REQUEST:" + userEmail);
                inviteSent = true;
                //break;
            }
        }
        if (inviteSent) {
            sendMessage("INVITE_SENT");
        } else {
            sendMessage("INVITE_FAIL");
        }
    }

    private void handleDeclineInvite(String invitingUser, String invitedUser) {
        for (ClientHandler client : server.getClients()) {
            if (client.getUseremail().equals(invitingUser)) {
                client.sendMessage("DECLINE_INVITE:" + invitedUser);
                return;
            }
        }
        sendMessage("INVITE_NOT_FOUND");
    }

    private void handleLogin(String useremail, String password) {
        try {
            if (UserManager.validateUser(useremail, password)) {
                User user = UserManager.getUserByUseremail(useremail);
                if (user != null) {
                    user.setStatus(true);
                    UserManager.updateUserStatus(user.getUseremail(), true);
                    this.useremail = user.getUseremail();
                    System.out.println("Login successful for: " + useremail);
                    sendMessage("LOGIN_SUCCESS:" + useremail);
                } else {
                    System.out.println("Login failed for: " + useremail + " - user not found");
                    sendMessage("LOGIN_FAIL");
                }
            } else {
                System.out.println("Login failed for: " + useremail + " - invalid credentials");
                sendMessage("LOGIN_FAIL");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendMessage("LOGIN_ERROR");
        }
    }

    private void handleRegister(String username, String password, String email) {
        boolean success = UserManager.registerUser(username, password, email);
        if (success) {
            sendMessage("REGISTER_SUCCESS");
        } else {
            sendMessage("REGISTER_FAIL");
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public String getUseremail() {
        return useremail;
    }

    public void stop() throws IOException {
        if (clientSocket != null && !clientSocket.isClosed()) {
            clientSocket.close();
        }
    }

    private void handleISEXIST(String email) {
        System.out.println("Checking existence for email: " + email);
        boolean isExist = UserManager.isExist(email);
        if (isExist) {
            sendMessage("USER_EXIST");
        } else {
            sendMessage("USER_NOTEXIST");
        }
    }

    private void handleGetAll(String email) {
        List<User> users = UserManager.getAllUsers();
        StringBuilder msg = new StringBuilder();
        for (User u : users) {
            if (!u.getUseremail().equals(email)) {
                msg.append(u.getUsername()).append("&")
                        .append(u.getScore()).append("&")
                        .append(u.getStatus()).append("&")
                        .append(u.getUseremail()).append(":");
            }
        }
        if (msg.length() > 0) {
            sendMessage(msg.toString());
        } else {
            sendMessage("NO_USERS_TO_SHOW");
        }
    }

    private void handleAcceptInvite(String invitedEmail , String invitingEmail) {

        boolean inviteResponse = false;
        for (ClientHandler client : server.getClients()) {
            if (client.getUseremail() != null && client.getUseremail().equals(invitedEmail)) {
                client.sendMessage("ACCEPT_INVITE:" + invitingEmail);
                inviteResponse = true;
                //break;
            }
        }
        if (inviteResponse) {
            sendMessage("INVITE_RESPONSE_SENT");
        } else {
            sendMessage("INVITE_RESPONSE_FAIL");
        }
    }
}
