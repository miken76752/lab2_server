import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientThread implements Runnable {
    private int clientNumber;
    private Socket clientSocket;
    private ChatServer chatServer;

    public ClientThread(int clientNumber, Socket clientSocket, ChatServer chatServer) {
        this.clientNumber = clientNumber;
        this.clientSocket = clientSocket;
        this.chatServer = chatServer;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));

            String message;
            while((message = in.readLine()) != null) {
                System.out.println("User " + this.clientNumber + ": " + message);
                this.chatServer.sendMessageForAllClients(this.clientNumber, message);
            }

            this.chatServer.removeClient(this.clientNumber);
            this.clientSocket.close();
        } catch (IOException var3) {
            var3.printStackTrace();
        }

    }
}