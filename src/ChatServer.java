import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChatServer {
    private static final int PORT = 12345;
    private ServerSocket serverSocket;
    private Map<Integer, Socket> clients = new HashMap();
    private int clientCounter = 1;

    public ChatServer() {
        try {
            this.serverSocket = new ServerSocket(12345);
            System.out.println("Server is loading on port 12345");

            while(true) {
                Socket clientSocket = this.serverSocket.accept();
                System.out.println("New User is connected");
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                out.println(this.clientCounter);
                this.clients.put(this.clientCounter, clientSocket);
                ClientThread clientThread = new ClientThread(this.clientCounter, clientSocket, this);
                Thread thread = new Thread(clientThread);
                thread.setDaemon(true);
                thread.start();
                ++this.clientCounter;
            }
        } catch (IOException var5) {
            var5.printStackTrace();
        }
    }

    public synchronized void sendMessageForAllClients(int sender, String message) {
        Iterator var3 = this.clients.entrySet().iterator();

        while(var3.hasNext()) {
            Map.Entry<Integer, Socket> entry = (Map.Entry)var3.next();
            int clientNumber = (Integer)entry.getKey();
            Socket clientSocket = (Socket)entry.getValue();
            if (clientNumber != sender) {
                try {
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    out.println("User " + sender + ": " + message);
                } catch (IOException var8) {
                    var8.printStackTrace();
                }
            }
        }

    }

    public synchronized void removeClient(int clientNumber) {
        this.clients.remove(clientNumber);
        System.out.println("User " + clientNumber + " left the chat.");
    }

    public static void main(String[] args) {
        (new ChatServer()).run();
    }

    public void run() {
    }
}
