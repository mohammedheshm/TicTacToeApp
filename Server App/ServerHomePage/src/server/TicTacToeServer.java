package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import models.TicTacToeGame;

public class TicTacToeServer {

    private ServerSocket serverSocket;
    private List<ClientHandler> clients;
    private ConcurrentHashMap<Integer, TicTacToeGame> games;
    private ConcurrentHashMap<String, ClientHandler> clientMap;
    private int gameIdCounter = 0;
    private static final int PORT = 1529;
    private volatile boolean running = false;

    public TicTacToeServer() {
        this.clients = new ArrayList<>();
        this.games = new ConcurrentHashMap<>();
        this.clientMap = new ConcurrentHashMap<>();
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(PORT);
        running = true;
        System.out.println("Server started on port: " + PORT);

        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            } catch (IOException e) {
                if (running) {
                    System.err.println("Error accepting client connection: " + e.getMessage());
                }
                break;
            }
        }
    }

    public void stop() throws IOException {
        running = false;
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
        for (ClientHandler client : clients) {
            client.stop();
        }
        System.out.println("Server stopped.");
    }

    public List<ClientHandler> getClients() {
        return clients;
    }

    public int createNewGame(String player1, String player2) {
        int gameId = ++gameIdCounter;
        TicTacToeGame game = new TicTacToeGame(gameId, player1, player2);
        games.put(gameId, game);
        return gameId;
    }

    public TicTacToeGame getGameById(int gameId) {
        return games.get(gameId);
    }

    public void broadcast(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    public void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        clientMap.remove(clientHandler.getUseremail());
    }

    public void handleInvite(String inviter, String invitee) {
        ClientHandler inviteeHandler = clientMap.get(invitee);
        if (inviteeHandler != null) {
            inviteeHandler.sendMessage("INVITATION:" + inviter);
            ClientHandler inviterHandler = clientMap.get(inviter);
            if (inviterHandler != null) {
                inviterHandler.sendMessage("INVITATION_SENT");
            }
        } else {
            ClientHandler inviterHandler = clientMap.get(inviter);
            if (inviterHandler != null) {
                inviterHandler.sendMessage("INVITATION_FAIL");
            }
        }
    }

    public void handleGetAllOnline(String requestor) {
        StringBuilder userList = new StringBuilder();
        for (String user : clientMap.keySet()) {
            userList.append(user).append("&Online:");
        }
        ClientHandler requestorHandler = clientMap.get(requestor);
        if (requestorHandler != null) {
            requestorHandler.sendMessage(userList.toString());
        }
    }

    public void registerClient(String username, ClientHandler clientHandler) {
        clientMap.put(username, clientHandler);
    }
}
