package mp3.uf3.tcp.tcpUnicClient.Tasca2;

import java.io.*;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Tasca2Thread implements Runnable {
    /* Thread que gestiona la comunicació de SrvTcPAdivina_Obj.java i un cllient ClientTcpAdivina_Obj.java */

    private Socket clientSocket = null;
    private InputStream in = null;
    private OutputStream out = null;
    private boolean acabat;
    private Llista llista;

    public Tasca2Thread(Socket clientSocket)throws IOException {
        this.clientSocket = clientSocket;
        acabat = false;
        //Enllacem els canals de comunicació
        in = clientSocket.getInputStream();
        out = clientSocket.getOutputStream();
        System.out.println("canals i/o creats amb un nou jugador");
    }

    @Override
    public void run() {
        try {
            while(!acabat) {
                //Llegim la jugada
                ObjectInputStream ois = new ObjectInputStream(in);
                try {
                    llista = (Llista) ois.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                System.out.println("jugada: " + llista.getNom() + "->" + llista.getNumberList());
                Set<Integer> set = new HashSet<>();
                set.addAll(llista.getNumberList());
                llista.setNumberList(set.stream().toList());
                acabat=true;
            }
        }catch(IOException e){
            System.out.println(e.getLocalizedMessage());
        }
        //Enviem últim estat del tauler abans de acabar amb la comunicació i acabem
        try {
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(llista);
            oos.flush();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}