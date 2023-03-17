package TCP.TCPMulticat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;

public class MulticastServer {
    MulticastSocket socket;
    InetAddress multicastIP;
    int port;
    boolean continueRunning = true;
    private ArrayList<String> phrases = new ArrayList<>();
    private Tauler t;

    public MulticastServer(int portValue, String strIp, Tauler t) throws IOException {
        socket = new MulticastSocket(portValue);
        multicastIP = InetAddress.getByName(strIp);
        port = portValue;
        this.t = t;
    }

    public void runServer() throws IOException{
        DatagramPacket packet;
        byte [] sendingData;
        while(continueRunning){
            String phrase = t.toString();
            sendingData = phrase.getBytes();
            packet = new DatagramPacket(sendingData, sendingData.length,multicastIP, port);
            socket.send(packet);
            if (t.acabats == t.getNumPlayers() && t.getNumPlayers() != 0) {
                continueRunning = false;
            }

            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                ex.getMessage();
            }
        }
        socket.close();
    }

}

