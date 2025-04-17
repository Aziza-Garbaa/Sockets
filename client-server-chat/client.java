
import java.net.Socket;
import java.net.UnknownHostException;

import java.io.*;
import java.util.Scanner;

public class client {
    public static void main(String[] args){
        try{
            System.out.println("Client en attente de connexion sur le port 12346...");
            System.out.flush();
        Socket s=new Socket("127.0.0.1",12346);
        InputStream is = s.getInputStream();
        OutputStream os = s.getOutputStream();
        BufferedWriter o = new BufferedWriter(new OutputStreamWriter(os));
        BufferedReader in = new BufferedReader(new InputStreamReader(is,"UTF-8"),8192);
        Scanner scanner=new Scanner(System.in);
        System.out.println("entrer votre nom d'utilisateur: ");//demander et envoyer le nom de l'utilisateur
        String username=scanner.nextLine();
        o.write(username);
        o.newLine();
        o.flush();
        Thread receiveThread=new Thread(() ->{//thread pour recevoir les messages de serveur
            try{
                String message;
                while((message=in.readLine())!= null){
                    System.out.println(message);
                }
            }catch (IOException e){
                System.out.println("connexion au serveur perdue. ");
                e.printStackTrace();;
            }
        });
        receiveThread.start();
        //boucle principale pour envoyer des messages
        System.out.println("Vous pouvez maintenant envoyer des messages (tapez 'exit' pour quitter):"); 
        while(true){
           // String message2 = in.readLine();
            String message2=scanner.nextLine();
            if(message2.equalsIgnoreCase("exit")){
                o.write(message2);
                o.newLine();
                o.flush();
                break;
            }
            o.write(message2);
            o.newLine();
            o.flush();
        }
        //fermeture des ressources
        if(in.readLine()!=null){
        o.close();
        in.close();
        s.close();
        scanner.close();
        System.out.println("Connexion fermée.");
        System.out.flush();}
    }
    catch (UnknownHostException e)
    {   System.out.println("Adresse IP du serveur invalide");
        e.printStackTrace();
    }
    catch (IOException e){
        System.out.println("Erreur de connexion ou de communication");
        e.printStackTrace();
    }
    
}}
