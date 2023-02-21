package mp9.uf3.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class AdivinarNumeroServer {
    DatagramSocket serversocket ;
    SecretNum secretNum;

    public void init(int port) throws SocketException {
        serversocket = new DatagramSocket(port);
        secretNum= new SecretNum(50);
        System.out.println(secretNum.getNum());
    }

    public void runServer() throws IOException {
        byte [] receivingData = new byte[4];
        byte [] sendingData;
        InetAddress clientIP;
        int clientPort;

//el servidor atén el port indefinidament
        while(true){
//creació del paquet per rebre les dades
            DatagramPacket packet = new DatagramPacket(receivingData, 4);
//espera de les dades
            serversocket.receive(packet);
            //obtenció de l'adreça del client
            clientIP = packet.getAddress();
            //obtenció del port del client
            clientPort = packet.getPort();


//processament de les dades rebudes i obtenció de la resposta
            sendingData = processData(packet.getData());


//creació del paquet per enviar la resposta
            packet = new DatagramPacket(sendingData, sendingData.length,
                    clientIP, clientPort);
//enviament de la resposta
            serversocket.send(packet);
        }
    }
    private byte[] processData(byte[] data) {
        int n = ByteBuffer.wrap(data).getInt();
        return ByteBuffer.allocate(4).putInt(secretNum.comprova(n)).array();
    }



    public static void main(String[] args) throws IOException {
        AdivinarNumeroServer adivinarNumeroServer = new AdivinarNumeroServer();
        adivinarNumeroServer.init(5566);
        adivinarNumeroServer.runServer();
    }
}
