package TCP.TCPMulticat;

import TCP.ExerClasse.SecretNum;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class SrvTcpAdivina_Obj implements Runnable{
    /* Servidor TCP que genera un número perquè ClientTcpAdivina_Obj.java jugui a encertar-lo
     * i on la comunicació dels diferents jugadors la gestionaran els Threads : ThreadServidorAdivina_Obj.java
     * */

    private int port;
    private SecretNum ns;
    private Tauler t;
    ServerSocket serverSocket = null;

    private SrvTcpAdivina_Obj(int port ) {
        this.port = port;
        ns = new SecretNum(100);
        t = new Tauler();
    }

    private void listen() {

        Socket clientSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            while(true) { //esperar connexió del client i llançar thread
                clientSocket = serverSocket.accept();
                //Llançar Thread per establir la comunicació
                //sumem 1 al numero de jugadors
                t.addNUmPlayers();
                ThreadSevidorAdivina_Obj FilServidor = new ThreadSevidorAdivina_Obj(clientSocket, ns, t);
                Thread client = new Thread(FilServidor);
                client.start();

            }
        } catch (IOException ex) {
//            Logger.getLogger(SrvTcpAdivina_Obj.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Tancant servidors...");
        }
    }

    public static void main(String[] args) throws IOException {
        SrvTcpAdivina_Obj srv = new SrvTcpAdivina_Obj(5558);
        MulticastServer srvVel = new MulticastServer(5557, "224.0.11.111",srv.t);
        Thread thread = new Thread(srv);
        thread.start();
        srvVel.runServer();
        System.out.println("Stopped!");
        try {
            srv.serverSocket.close();
        }catch (SocketException e){
            System.out.println("Tancant el joc...");
        }

//        System.exit(0);
    }

    @Override
    public void run() {
        listen();
    }
}
