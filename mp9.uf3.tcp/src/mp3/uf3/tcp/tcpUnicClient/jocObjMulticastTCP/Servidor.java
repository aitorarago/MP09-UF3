package mp3.uf3.tcp.tcpUnicClient.jocObjMulticastTCP;

import mp9.uf3.udp.SecretNum;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {
    /* Servidor TCP que genera un número perquè ClientTcpAdivina_Obj.java jugui a encertar-lo
     * i on la comunicació dels diferents jugadors la gestionaran els Threads : ThreadServidorAdivina_Obj.java
     * */

    private final int port;
    private final SecretNum ns;
    private Taulerr t;

    public Servidor(int port,Taulerr ta) {
        this.port = port;
        ns = new SecretNum(100);
        t=ta;
    }


    public void listen() {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            while(true) { //esperar connexió del client i llançar thread
                clientSocket = serverSocket.accept();
                //Llançar Thread per establir la comunicació
                //sumem 1 al numero de jugadors
                t.addNUmPlayers();
                Thread_adiv FilServidor = new Thread_adiv(clientSocket, ns, t);
                Thread client = new Thread(FilServidor);
                client.start();

            }
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
