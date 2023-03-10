package TCP.Act2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    private final int port;

    private Server(int port){
        this.port = port;
    }
    private void listen() {
        ServerSocket serverSocket;
        Socket clientSocket;
        try {
            serverSocket = new ServerSocket(port);
            while(true) { //esperar connexió del client i llançar thread
                clientSocket = serverSocket.accept();
                System.out.println("Connexió establerta amb el client " + clientSocket.getInetAddress());
                //Llançar Thread per establir la comunicació
                ThreadList FilServidor = new ThreadList(clientSocket);
                Thread client = new Thread(FilServidor);
                client.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        Server server = new Server(8080);
        server.listen();
    }
}