package TCP.Act2;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ThreadList implements Runnable{
    private final Socket clientSocket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private final boolean continueConnected;

    public ThreadList(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        in = new ObjectInputStream(clientSocket.getInputStream());
        out = new ObjectOutputStream(clientSocket.getOutputStream());
        continueConnected = true;
    }

    @Override
    public void run() {
        while (continueConnected) {
            try {
                Llista llista = (Llista) in.readObject();
                List<Integer> modifiedList = llista.getNumberList()
                        .stream()
                        .sorted()
                        .distinct().toList();
                llista.getNumberList().clear();
                llista.getNumberList().addAll(modifiedList);
                out.writeObject(llista);
                out.flush();
            } catch (EOFException e) {
                // S'ha arribat al final del flux, tanca la connexió
                close(clientSocket);
                break;
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
        close(clientSocket);
    }

    private void close(Socket socket) {
        try {
            if (socket != null && !socket.isClosed()) {
                if (!socket.isInputShutdown()) {
                    socket.shutdownInput();
                }
                if (!socket.isOutputShutdown()) {
                    socket.shutdownOutput();
                }
                socket.close();
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
