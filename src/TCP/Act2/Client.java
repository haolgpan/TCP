package TCP.Act2;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client extends Thread{
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private final Scanner scanner = new Scanner(System.in);
    private boolean continueConnected;

    private Client(String hostname, int port){
        try {
            socket = new Socket(InetAddress.getByName(hostname), port);
            in = socket.getInputStream();
            out = socket.getOutputStream();
        } catch (UnknownHostException ex) {
            System.out.println("Error de connexió. No existeix el host: " + ex.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        continueConnected = true;
    }
    public void run(){
        ObjectOutputStream out;
        ObjectInputStream in;
        String nom, answer;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while (continueConnected){
            try {
                System.out.println("Introdueix el nom de la llista:");
                nom = scanner.nextLine();
                Llista llista = new Llista(nom, generarLlistaNumeros());
                out.writeObject(llista);
                out.flush();
                // Rebre la llista ordenada i sense repetits
                Llista llistaOrdenada = (Llista) in.readObject();
                System.out.println("Llista ordenada i sense repetits: " + llistaOrdenada.getNom() + " " + llistaOrdenada.getNumberList());
                System.out.println("Vols tornar a enviar una llista? (si/no)");
                answer = scanner.nextLine();
                if(answer.equals("no")) {
                    continueConnected = false;
                    in.close();
                    out.close();
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        close(socket);
    }
    private void close(Socket socket){
        //si falla el tancament no podem fer gaire cosa, només enregistrar
        //el problema
        try {
            //tancament de tots els recursos
            if(socket!=null && !socket.isClosed()){
                if(!socket.isInputShutdown()){
                    socket.shutdownInput();
                }
                if(!socket.isOutputShutdown()){
                    socket.shutdownOutput();
                }
                socket.close();
            }
        } catch (IOException ex) {
            //enregistrem l'error amb un objecte Logger
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private List<Integer> generarLlistaNumeros() {
        List<Integer> llista = new ArrayList<>();
        System.out.println("Quants numeros vols introduïr?");
        int num = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < num; i++) {
            System.out.println("Introdueix un número enter.");
            llista.add(scanner.nextInt());
            scanner.nextLine();
        }
        return llista;
    }
    public static void main(String[] args) {
        Client client = new Client("localhost",8080);
        client.start();
    }

}
