package mp3.uf3.tcp.tcpUnicClient.jocObjMulticastTCP;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastServer {
    MulticastSocket socket;
    InetAddress multicastIP;
    int port;
    boolean continueRunning = true;
    Taulerr tauler;

    public MulticastServer(int portValue, String strIp,Taulerr ta) throws IOException {
        socket = new MulticastSocket(portValue);
        multicastIP = InetAddress.getByName(strIp);
        port = portValue;
        tauler = ta;
    }

    public void runServer() throws IOException{
        DatagramPacket packet;
        byte [] sendingData;

        System.out.println("MULTICAST FUNCIONANT");

        while(continueRunning){
            sendingData = tobytes();
            packet = new DatagramPacket(sendingData, sendingData.length, multicastIP, port);
            socket.send(packet);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                ex.getMessage();
            }


        }
        socket.close();
    }

    private byte[] tobytes() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(tauler);
       return baos.toByteArray();
    }
}
