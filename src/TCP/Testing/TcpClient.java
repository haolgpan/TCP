package TCP.Testing;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import TCP.Act2.Llista;

public class TcpClient {
    public static void main(String[] args) {
        String serverAddress = "localhost";
        int serverPort = 9090;

        // Crear la llista a enviar
        List<Integer> numberList = new ArrayList<>();
        numberList.add(3);
        numberList.add(1);
        numberList.add(4);
        numberList.add(1);
        numberList.add(5);
        Llista llista = new Llista("Llista1", numberList);

        try (
                Socket socket = new Socket(serverAddress, serverPort);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
        ) {
            // Enviar l'objecte Llista al servidor
            out.writeObject(llista);
            out.flush();

            // Esperar la resposta del servidor
            Llista modifiedLlista = (Llista) in.readObject();

            // Mostrar la llista modificada
            System.out.println("Llista modificada: " + modifiedLlista.getNumberList());
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}

