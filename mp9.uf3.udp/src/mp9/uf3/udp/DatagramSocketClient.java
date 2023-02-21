package mp9.uf3.udp;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class DatagramSocketClient {
    Scanner sc = new Scanner(System.in);
    InetAddress serverIP;
    int serverPort;
    DatagramSocket socket;
    String m;


    public void init(String host, int port) throws SocketException,
            UnknownHostException {
        serverIP = InetAddress.getByName(host);
        serverPort = port;
        socket = new DatagramSocket();
    }

    public void runClient() throws IOException {
        byte [] receivedData = new byte[1024];
        byte [] sendingData;

//a l'inici
        sendingData = getFirstRequest();
//el servidor atén el port indefinidament
        while(mustContinue(sendingData)){
            DatagramPacket packet = new DatagramPacket(sendingData,
                    sendingData.length,
                    serverIP,
                    serverPort);
//enviament de la resposta
            socket.send(packet);

//creació del paquet per rebre les dades
            packet = new DatagramPacket(receivedData, 1024);
//espera de les dades
            socket.receive(packet);
//processament de les dades rebudes i obtenció de la resposta
            sendingData = getDataToRequest(packet.getData(), packet.getLength());
        }
    }

    private byte[] getDataToRequest(byte[] data, int length) {
        String msg = new String(data,0,length);
        System.out.println(msg);
        System.out.println("Introdueix una altre frase:");
        m=sc.nextLine();
        return msg.toUpperCase().getBytes();

    }

    private byte[] getFirstRequest() {
        System.out.println("Introdueix el nom:");
        m = sc.nextLine();
       return m.getBytes();
    }

    private boolean mustContinue(byte[] sendingData) {
        String msg = new String(sendingData,0,m.length());

        return !(msg.equals("adéu") | msg.equals("Adeu") | msg.equals("Adéu") | msg.equals("ADEU") | msg.equals("BYE") | msg.equals("bye"));
    }
    public static void main(String[] args) {
        DatagramSocketClient client = new DatagramSocketClient();
        try {
            client.init("localhost",5566);
            client.runClient();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}