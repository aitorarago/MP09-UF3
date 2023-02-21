package mp9.uf3.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastServer {
    MulticastSocket socket;
    InetAddress multicastIP;
    int port;
    boolean continueRunning = true;
    String[] f ;


    public MulticastServer(int portValue, String strIp,String[] frases) throws IOException {
        socket = new MulticastSocket(portValue);
        multicastIP = InetAddress.getByName(strIp);
        f=frases;
        port = portValue;
    }

    public void runServer() throws IOException{
        DatagramPacket packet;
        byte [] sendingData = new byte[1024];

        while(continueRunning){
            sendingData=agafarFrase().getBytes();
            packet = new DatagramPacket(sendingData, sendingData.length,multicastIP, port);
            socket.send(packet);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.getMessage();
            }


        }
        socket.close();
    }
    public String agafarFrase(){
        return f[(int) (Math.random() * f.length)];
    }
    public static void main(String[] args) throws IOException {
        String[] frases = {"HOLA", "ADEU", "RAP", "SARDINA", "GALLINA", "MANDARINA", "POMA", "TOMA", "GOMA", "ROMA", "MAHOMA"};
        MulticastServer cvel = new MulticastServer(5557, "225.100.11.111",frases);
        cvel.runServer();
        System.out.println("Parat!");

    }


}
