package TCP.Act2;

import java.io.*;
import java.net.Socket;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ThreadList implements Runnable{
    private Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean continueConnected;

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
                        .distinct()
                        .collect(Collectors.toList());
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
