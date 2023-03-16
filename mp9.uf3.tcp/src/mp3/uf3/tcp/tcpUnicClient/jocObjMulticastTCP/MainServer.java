package mp3.uf3.tcp.tcpUnicClient.jocObjMulticastTCP;
import java.io.IOException;

public class MainServer {
    public static void main(String[] args) throws IOException {
        Taulerr tauler = new Taulerr();
        Servidor srv = new Servidor(5558,tauler);
        Thread thread = new Thread(srv::listen);
        thread.start();
        MulticastServer srvmulticast = new MulticastServer(5557,"224.0.11.115",tauler);
        srvmulticast.runServer();


    }
}
