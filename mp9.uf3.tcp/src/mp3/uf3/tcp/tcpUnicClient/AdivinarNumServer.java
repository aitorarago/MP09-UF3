package mp3.uf3.tcp.tcpUnicClient;

import mp9.uf3.udp.SecretNum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.*;
import java.util.logging.Logger;

public class AdivinarNumServer {
        static int PORT;
    private final SecretNum secretNum ;
        Socket clientSocket;
        AdivinarNumServer(String host, int port) throws UnknownHostException {
            secretNum= new SecretNum(100);
            InetAddress serverIP = InetAddress.getByName(host);
            PORT = port;
            clientSocket = new Socket();
            listen();

        }
        public void listen(){
            ServerSocket serverSocket;
            try {
                serverSocket = new ServerSocket(PORT);

                boolean end = false;
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
            boolean farewellMessage;
            String clientMessage="";
            BufferedReader in;
            PrintStream out;
            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out= new PrintStream(clientSocket.getOutputStream());
                System.out.println("Conectado: numero"+secretNum.getNum());
                do{
//processem el missatge del client i generem la resposta. Si
//clientMessage és buida generarem el missatge de benvinguda
                    String e = processData(clientMessage);
                    out.println(e);
                    out.flush();
                    clientMessage=in.readLine();
                    farewellMessage = isFarewellMessage(clientMessage);
                }while((clientMessage)!=null && !farewellMessage);
            } catch (IOException ex) {
                System.out.println(Logger.getLogger(ex.toString()));

            }
        }

        private boolean isFarewellMessage(String msg) {
            System.out.println(msg);
            return false;
        }

        private String processData(String clientMessage) {
          if(clientMessage.equals("")){return "Introduce un numero entre 0 y 100";}
          else return secretNum.comprova(clientMessage);
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

        public static void main(String[] args) throws UnknownHostException {
            AdivinarNumServer server = new AdivinarNumServer("localhost",5566);
        }
}

