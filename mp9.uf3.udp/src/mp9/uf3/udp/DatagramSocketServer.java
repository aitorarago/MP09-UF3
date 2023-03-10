package mp9.uf3.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Locale;

public class DatagramSocketServer{
    DatagramSocket socket;

    public void init(int port) throws SocketException {
        socket = new DatagramSocket(port);
    }

    public void runServer() throws IOException {
        byte [] receivingData = new byte[1024];
        byte [] sendingData;
        InetAddress clientIP;
        int clientPort;

//el servidor atén el port indefinidament
        while(true){
//creació del paquet per rebre les dades
            DatagramPacket packet = new DatagramPacket(receivingData, 1024);
//espera de les dades
            socket.receive(packet);
            //obtenció de l'adreça del client
            clientIP = packet.getAddress();
            //obtenció del port del client
            clientPort = packet.getPort();
//processament de les dades rebudes i obtenció de la resposta
            sendingData = processData(packet.getData(), packet.getLength());


//creació del paquet per enviar la resposta
            packet = new DatagramPacket(sendingData, sendingData.length,
                    clientIP, clientPort);
//enviament de la resposta
            socket.send(packet);
        }
    }

    private byte[] processData(byte[] data, int length) {
        String msg = new String(data,0,length);
        System.out.println(msg.toUpperCase(Locale.ROOT));
        return msg.toUpperCase().getBytes();
    }
    public static void main(String[] args) {
        DatagramSocketServer server = new DatagramSocketServer();
        try {
            server.init(5566);
            server.runServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}