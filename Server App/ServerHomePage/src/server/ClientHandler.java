package server;

import Dao.GameManager;
import Dao.UserManager;
import models.TicTacToeGame;
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
    private String useremail;

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
            if (clientSocket.isClosed()) {
                System.out.println("Connection closed: " + e.getMessage());
            } else {
                System.err.println("IOException occurred: " + e.getMessage());
            }
        } finally {
            stop();
        }
    }

    private void handleClientMessage(String message) {
        System.out.println("Received message: " + message);
        String[] parts = message.split(":");

        switch (parts[0]) {
            case "MOVE":
                handleMove(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
                break;
            case "RESULT":
                handleResult(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
                break;
            case "LOGIN":
                useremail = parts[1];
                handleLogin(useremail, parts[2]);
                break;
            case "REGISTER":
                handleRegister(parts[1], parts[2], parts[3]);
                break;
            case "ISEXIST":
                handleISEXIST(parts[1]);
                break;
            case "GETALLONLINE":
                handleGetAll(parts[1]);
                break;
            case "INVITE":
                handleInvite(parts[1], parts[2]);
                break;
            case "INVITE_ACCEPT":
                handleInviteResponse(parts[1], true);
                break;
            case "INVITE_REJECT":
                handleInviteResponse(parts[1], false);
                break;
            default:
                System.out.println("Unknown message type: " + parts[0]);
        }
    }

    private void handleMove(int gameId, int row, int col) {
        TicTacToeGame game = server.getGameById(gameId);
        if (game != null && game.makeMove(row, col)) {
            int winner = game.checkWinner();
            if (winner != 0) {
                server.broadcast("GAME_OVER:" + gameId + ":" + winner);
                GameManager.updateGame(gameId, winner, "finished");
            } else {
                server.broadcast("MOVE:" + gameId + ":" + row + ":" + col);
            }
        }
    }

    private void handleResult(int gameId, int winnerId) {
        GameManager.updateGame(gameId, winnerId, "finished");
    }

    private void handleLogin(String useremail, String password) {
        try {
            if (UserManager.validateUser(useremail, password)) {
                User user = UserManager.getUserByUseremail(useremail);
                if (user != null) {
                    user.setStatus(true);
                    UserManager.updateUserStatus(user.getUseremail(), true);
                    this.useremail = user.getUseremail(); // Store username upon successful login
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

    private void handleInvite(String fromUser, String toUser) {
        // Send invitation to the specified user
        // You might need to keep track of the invitations in a data structure or send to specific users
        System.out.println("Invitation from " + fromUser + " to " + toUser);
        // Check if the 'toUser' is online
        if (server.isUserOnline(toUser)) {
            // Inform the 'toUser' about the invitation
            server.sendMessageToUser(toUser, "INVITE:" + fromUser);
            sendMessage("INVITE_SENT");
        } else {
            sendMessage("USER_OFFLINE");
        }
    }

    private void handleInviteResponse(String fromUser, boolean accepted) {
        // Respond to the sender based on the invite response
        if (accepted) {
            sendMessage("INVITE_ACCEPTED:" + fromUser);
        } else {
            sendMessage("INVITE_REJECTED:" + fromUser);
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public String getUseremail() {
        return useremail;
    }

    public void stop() {
        try {
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing socket: " + e.getMessage());
        }
    }
}
