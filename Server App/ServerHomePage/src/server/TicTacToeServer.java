package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TicTacToeServer {

    private ServerSocket serverSocket;
    private List<ClientHandler> clients;
    private static final int PORT = 1529;
    private volatile boolean running = false;

    public TicTacToeServer() {
        this.clients = new ArrayList<>();
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

    public void broadcast(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    public void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
    }

}
