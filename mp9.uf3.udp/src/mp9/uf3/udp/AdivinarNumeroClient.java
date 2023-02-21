package mp9.uf3.udp;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Scanner;

public class AdivinarNumeroClient {
    Scanner sc = new Scanner(System.in);
    InetAddress serverIP;
    int serverPort;
    DatagramSocket socket;
    String m;
    int intentos;
    boolean r ;


    public void init(String host, int port) throws SocketException,
            UnknownHostException {
        serverIP = InetAddress.getByName(host);
        serverPort = port;
        socket = new DatagramSocket();
    }

    public void runClient() throws IOException {
        byte [] receivedData = new byte[4];
        byte [] sendingData;

//a l'inici
        sendingData = getFirstRequest();
//el servidor atén el port indefinidament
        while(!r){
            DatagramPacket packet = new DatagramPacket(sendingData,
                    sendingData.length,
                    serverIP,
                    serverPort);
//enviament de la resposta
            socket.send(packet);

//creació del paquet per rebre les dades
            packet = new DatagramPacket(receivedData, 4);
//espera de les dades
            socket.receive(packet);
//processament de les dades rebudes i obtenció de la resposta
            sendingData = getDataToRequest(packet.getData(), packet.getLength());
        }
    }

    private byte[] getDataToRequest(byte[] data, int length) {
        int n = ByteBuffer.wrap(data).getInt();
        mustContinue(n);
        if(r){socket.close();}
        else System.out.println("INTRODUCE UN NUEVO NUMERO: ");
        int numnuevo = sc.nextInt();sc.nextLine();
        return ByteBuffer.allocate(4).putInt(numnuevo).array();

    }

    private byte[] getFirstRequest() {
        System.out.println("Introduce el primer número:");
        intentos =0;
        m = sc.nextLine();
        return m.getBytes();
    }

    private void mustContinue(int t) {
         if(t==1){System.out.println("INTRODUCE UN NUMERO MAS PEQUEÑO");}
         else if (t==0) {System.out.println("HAS ACERTADO CON "+intentos+" FELICIDADES"); r= true;}
         else System.out.println("INTRODUCE UN NUMERO MAS GRANDE: ");
         ++intentos;
    }

    public static void main(String[] args) throws IOException {
        AdivinarNumeroClient adivinarNumeroClient = new AdivinarNumeroClient();
        adivinarNumeroClient.init("localhost",5566);
        adivinarNumeroClient.runClient();
    }
}
