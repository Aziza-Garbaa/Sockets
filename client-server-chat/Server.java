import java.net.ServerSocket;
import java.net.Socket;

import java.io.*;
public class Server {
    public static void main(String[] args) {
        try{
        ServerSocket ss = new ServerSocket(12346);
        System.out.println("Serveur en attente de connexion sur le port 12346...");
        System.out.flush();
        new Thread(
            ()->{//main thread
        while(true){//le serveur reste à l'ecoute indefiniement
        try{
        Socket s = ss.accept();//instruction bloquante ,le serveur attend jusqu'à le client tente de faire une connexion
        System.out.println("Nouveau client connecté!");
        ClientHandler clientHandler=new ClientHandler(s);//creer un nouveau gestionnaire de client
        Thread thread=new Thread(clientHandler);//creation d'un nouveau thread pour ce client
        thread.start();}//start fait appel à la methode run de l'interface runnable
   
        
    catch (IOException e)
    {e.printStackTrace();}
}}
).start();//execution de la methode run sur le main thread
 
}
    catch(IOException e){
        e.printStackTrace();
    }
}
}

