package mp3.uf3.tcp.tcpUnicClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Logger;

public class AdivinarNumClient {
    static int PORT;
    private final InetAddress serverIP;
    private final Scanner sc = new Scanner(System.in);
    AdivinarNumClient(String host,int port) throws UnknownHostException {
        PORT=port;
        serverIP = InetAddress.getByName(host);
        connect(serverIP.getHostAddress(), port);
    }



    public void connect(String address, int port) {
        String serverData;
        String request;
        boolean continueConnected=false;
        Socket socket;
        BufferedReader in;
        PrintStream out;
        try {
            socket = new Socket(InetAddress.getByName(address), port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintStream(socket.getOutputStream());
            System.out.println("conectado");
//el client atén el port fins que decideix finalitzar
            while(!continueConnected){
                serverData = in.readLine();
                if(mustFinish(serverData))break;
//processament de les dades rebudes i obtenció d'una nova petició
                request = getRequest(serverData);
//enviament de la petició
                out.println(request);//assegurem que acaba amb un final de línia
                out.flush(); //assegurem que s'envia
//comprovem si la petició és un petició de finalització i en cas
//que ho sigui ens preparem per sortir del bucle
                continueConnected = mustFinish(serverData);
            }

            close(socket);
        } catch (IOException ex) {
            System.out.println(Logger.getLogger(ex.toString()));
        }
    }

    private boolean mustFinish(String request) {
        return (request.equals("Correcte"));
    }

    private String getRequest(String serverData) {
        System.out.println("Servidor: "+serverData);
        System.out.println("INTRODUCE EL NUEVO NUMERO:");
        return sc.nextLine();
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
            System.out.println(Logger.getLogger(ex.toString()));
        }
    }

    public static void main(String[] args) throws UnknownHostException{
        AdivinarNumClient client = new AdivinarNumClient("localhost",5566);
    }
}
