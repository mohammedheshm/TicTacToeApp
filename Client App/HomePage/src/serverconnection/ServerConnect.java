package serverconnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerConnect {

    private static String SERVER_ADDRESS = "localhost"; // IP 
    private static final int SERVER_PORT = 1529;
    private static Socket socket;
    private static PrintWriter out;
    private static BufferedReader in;
    private static Thread listenerThread;
    private static volatile boolean running = false;

    public static void makeConnectionWithServer() throws IOException {
        if (socket == null || socket.isClosed()) {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            running = true;
            new Thread() {
                public void run() {
                    while (running) {
                        try {
                            String msg = in.readLine();
                            System.out.println(msg);
                        } catch (IOException ex) {
                            Logger.getLogger(ServerConnect.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            };
        }
    }

    public static void setSERVER_ADDRESS(String SERVER_ADDRESS) {
        ServerConnect.SERVER_ADDRESS = SERVER_ADDRESS;
    }

    public static void sendMessage(String message) {
        out.println(message);
    }

    public static String receiveMessage() throws IOException {
        return in.readLine();
    }

    public static void closeConnection() throws IOException {
        running = false;
        if (in != null) {
            in.close();
        }
        if (out != null) {
            out.close();
        }
        if (socket != null) {
            socket.close();
        }
        if (listenerThread != null) {
            listenerThread.interrupt();
        }
    }

    public static boolean isConnection() throws IOException {
        //makeConnectionWithServer();
        return running;
    }
}
