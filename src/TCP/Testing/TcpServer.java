package TCP.Testing;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import TCP.Act2.Llista;

public class TcpServer {
    public static void main(String[] args) {
        int serverPort = 9090;

        try (
                ServerSocket serverSocket = new ServerSocket(serverPort);
                Socket clientSocket = serverSocket.accept();
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())
        ) {
            // Llegir l'objecte Llista enviat pel client
            Llista llista = (Llista) in.readObject();

            // Modificar la llista: ordenar-la i eliminar números repetits
            List<Integer> modifiedList = llista.getNumberList()
                    .stream()
                    .sorted()
                    .distinct()
                    .collect(Collectors.toList());
            Llista modifiedLlista = new Llista(llista.getNom(), modifiedList);

            // Enviar l'objecte Llista modificat al client
            out.writeObject(modifiedLlista);
            out.flush();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}

