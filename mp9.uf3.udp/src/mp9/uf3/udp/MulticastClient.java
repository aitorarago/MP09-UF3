package mp9.uf3.udp;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class MulticastClient {
    private final MulticastSocket socket;
    private final InetAddress multicastIP;
    private final int port;
    NetworkInterface netIf;
    InetSocketAddress group;
    Map<String, Integer> frases;


    public MulticastClient(int portValue, String strIp) throws IOException {
        multicastIP = InetAddress.getByName(strIp);
        port = portValue;
        socket = new MulticastSocket(port);
        //netIf = NetworkInterface.getByName("enp1s0");
        netIf = socket.getNetworkInterface();
        group = new InetSocketAddress(strIp,portValue);
        frases= new HashMap<>();
    }

    public void runClient() throws IOException{
        DatagramPacket packet;
        byte [] receivedData = new byte[1024];

        socket.joinGroup(group,netIf);
        System.out.printf("Connectat a %s:%d%n",group.getAddress(),group.getPort());

        while(true){
            packet = new DatagramPacket(receivedData, 1024);
            socket.setSoTimeout(5000);
            try{
                socket.receive(packet);
                getData(packet.getData(),packet.getLength());
            }catch(SocketTimeoutException e){
                System.out.println("S'ha perdut la connexió amb el servidor.");
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        socket.leaveGroup(group,netIf);
        socket.close();
    }

    protected  void getData(byte[] data,int length) {
        String msg = new String(data,0,length);
        if(frases.containsKey(msg)){
            frases.put(msg,frases.get(msg)+1);
        }
        else frases.put(msg,1);
        System.out.println(msg+" -> "+frases.get(msg));
    }

    public static void main(String[] args) throws IOException {
        MulticastClient cvel = new MulticastClient(5557, "225.100.11.111");
        cvel.runClient();
        System.out.println("Parat!");

    }
}
