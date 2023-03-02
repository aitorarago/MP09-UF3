package mp3.uf3.tcp.tcpUnicClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.*;
import java.util.Scanner;
import java.util.logging.Logger;

public class TcpServer {
    static int PORT;
    private final InetAddress serverIP;
    private boolean end=false;
    Socket clientSocket;
    TcpServer(String host, int port) throws SocketException,UnknownHostException {
        serverIP = InetAddress.getByName(host);
        PORT = port;
        clientSocket = new Socket();
        listen();
    }
    public void listen(){
        ServerSocket serverSocket=null;
        try {
            serverSocket = new ServerSocket(PORT);

            while(!end){
                clientSocket = serverSocket.accept();
//processem la petició del client
                proccesClientRequest(clientSocket);
//tanquem el sòcol temporal per atendre el client
                closeClient(clientSocket);
            }
//tanquem el sòcol principal
            if(serverSocket!=null && !serverSocket.isClosed()){
                serverSocket.close();
            }

        } catch (IOException ex) {
            System.out.println(Logger.getLogger(ex.toString()));
        }
    }
    public void proccesClientRequest(Socket clientSocket){
        boolean farewellMessage=false;
        String clientMessage="";
        BufferedReader in=null;
        PrintStream out=null;
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out= new PrintStream(clientSocket.getOutputStream());
            do{
//processem el missatge del client i generem la resposta. Si
//clientMessage és buida generarem el missatge de benvinguda
                String dataToSend = processData(clientMessage);
                out.println(dataToSend);
                out.flush();
                clientMessage=in.readLine();
                farewellMessage = isFarewellMessage(clientMessage);
            }while((clientMessage)!=null && !farewellMessage);
        } catch (IOException ex) {
            System.out.println(Logger.getLogger(ex.toString()));

        }
    }

    private boolean isFarewellMessage(String clientMessage) {
        if(clientMessage.isEmpty())return true;
        else return false;
    }

    private String processData(String clientMessage) {
        System.out.println("Client: "+clientMessage);
        Scanner sc = new Scanner(System.in);
        System.out.println("INTRODUCE LO QUE QUIERAS CONTESTAR AL CLIENTE:");
        return sc.nextLine();
    }

    private void closeClient(Socket clientSocket){
//si falla el tancament no podem fer gaire cosa, només enregistrar
//el problema
        try {
//tancament de tots els recursos
            if(clientSocket!=null && !clientSocket.isClosed()){
                if(!clientSocket.isInputShutdown()){
                    clientSocket.shutdownInput();
                }
                if(!clientSocket.isOutputShutdown()){
                    clientSocket.shutdownOutput();
                }
                clientSocket.close();
            }
        } catch (IOException ex) {
            System.out.println(Logger.getLogger(ex.toString()));
        }
    }

    public static void main(String[] args) throws UnknownHostException,SocketException{
        TcpServer server = new TcpServer("localhost",5566);
    }
}

