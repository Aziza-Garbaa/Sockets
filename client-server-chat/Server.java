import java.net.ServerSocket;
import java.net.Socket;

import java.io.*;
import java.util.Scanner;
public class Server {
    public static void main(String[] args) {
        try{
        ServerSocket ss = new ServerSocket(12346);//lina create serversocket tw najam na7ki 3ala concept serveur
        System.out.println("Serveur en attente de connexion sur le port 12346...");
        System.out.flush();
        new Thread(()->{
        while(true){//le serveur reste à l'ecoute indefiniement
        try{
        Socket s = ss.accept();//lina bloquit el serveur ila mayijih client yi7ib ya3mil connexion
        System.out.println("Nouveau client connecté!");
        ClientHandler clientHandler=new ClientHandler(s);//creer un nouveau gestionnaire de client et demarrer son thread
        Thread thread=new Thread(clientHandler);
        thread.start();}
   
        
    catch (IOException e)//ioexception d'entrée/sortie comme connexion rx
    {e.printStackTrace();}//printStackTrace affiche les details de l'erreur kima la nature de l'erreur w win bithabit sar
}}).start();
//interface pour permettre au serveur de repondre
Scanner scanner=new Scanner(System.in);
while (true){
    System.out.println("Liste des clients connectés: "+ClientHandler.clientHandlers.size());
    for(int i=0;i<ClientHandler.clientHandlers.size();i++){
        System.out.println(i+": "+ClientHandler.clientHandlers.get(i).getClientusername());

    }
    System.out.println("Entrer le numero du client auquel vous voulez répondre (-1 pour actualiser la Liste):");
    int clientnum=scanner.nextInt();
    scanner.nextLine();//consommer le retour à la ligne
    if(clientnum== -1) continue;
    if(clientnum>=0 && clientnum<ClientHandler.clientHandlers.size()){
        System.out.println("Entrez votre message pour "  + ClientHandler.clientHandlers.get(clientnum).getClientusername()+":");
        String message=scanner.nextLine();
        //envoyer le message au client choisi
        ClientHandler client=ClientHandler.clientHandlers.get(clientnum);
      /*   client.bufferedWriter.write("Serveur : "+message);
        client.bufferedWriter.newLine();
        client.bufferedWriter.flush();*/
        client.sendMessage("Serveur : " + message);

        System.out.println("Message envoyé!");
    }else{
        System.out.println("Numero de client invalide");
    }
    
    }}
    catch(IOException e){
        e.printStackTrace();
    }
}
}

