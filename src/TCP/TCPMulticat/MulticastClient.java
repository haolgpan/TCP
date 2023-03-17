package TCP.TCPMulticat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;

public class MulticastClient {
    private static final String MULTICAST_IP = "224.0.11.111";
    private static final int PORT = 5557;

    private MulticastSocket socket;
    private InetAddress multicastIP;

    Tauler t;

    public Tauler getT() {
        return t;
    }

    public void setT(Tauler t) {
        this.t = t;
    }

    private int port;

    public void init() throws IOException {
        socket = new MulticastSocket(PORT);
        multicastIP = InetAddress.getByName(MULTICAST_IP);
        port = PORT;
        socket.joinGroup(multicastIP);
    }

    public void runClient() throws IOException {
        DatagramPacket packet;
        byte[] receivedData = new byte[1024];

        while (true) {
            packet = new DatagramPacket(receivedData, 1024);
            try {
                socket.receive(packet);
                socket.setSoTimeout(5000);
                String phrase = new String(packet.getData(), 0, packet.getLength());
                System.out.println(phrase);
            }catch (SocketTimeoutException e){
                System.out.println("Servidor tancat");
                socket.close();
                break;
            }
        }
    }

    public void close() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.leaveGroup(multicastIP);
            socket.close();
        }
    }
}
