package mp3.uf3.tcp.tcpUnicClient.Tasca2;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Tasca2Client extends Thread {
    /* CLient TCP que ha endevinar un número pensat per SrvTcpAdivina_Obj.java */
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private boolean continueConnected;
    private Llista llista;

    private Tasca2Client(String hostname, int port,Llista llista) {
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
        this.llista=llista;
    }

    public void run() {
        while(continueConnected) {
            try {
                ObjectOutputStream outtt = new ObjectOutputStream(out);
                outtt.writeObject(llista);
                outtt.flush();
                this.llista=getRequest();
               break;
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
        close(socket);

    }
    private Llista getRequest() {
        try {
            ObjectInputStream ois = new ObjectInputStream(in);
            llista = (Llista) ois.readObject();
            System.out.println(llista.llistatoString());
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return llista;
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
            Logger.getLogger(Tasca2Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        String jugador, ipSrv;

        //Demanem la ip del servidor i nom del jugador
        System.out.println("Ip del servidor?");
        Scanner sip = new Scanner(System.in);
        ipSrv = sip.next();
        System.out.println("Nom jugador:");
        jugador = sip.next();
        List<Integer> ints = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
           ints.add(((int) (Math.random() * 25))+1);
        }
        Llista llista = new Llista(jugador,ints);

        Tasca2Client clientTcp = new Tasca2Client(ipSrv,5566,llista);
        clientTcp.llista.setNom(jugador);
        clientTcp.start();
    }
}
