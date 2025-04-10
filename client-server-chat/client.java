
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
        PrintWriter o = new PrintWriter(os, true);
        BufferedReader in = new BufferedReader(new InputStreamReader(is,"UTF-8"),8192);
        String message2 = in.readLine();
        if(message2!= null && !message2.trim().isEmpty()){
        System.out.println("entrer votre message : ");
        System.out.flush();
            Scanner scanner = new Scanner(System.in);
            String message = scanner.nextLine();
            scanner.close();
            
            o.println(message);
            o.flush();
            System.out.println("Message envoyé au serveur : " + message);
            System.out.flush();
            String reponse = in.readLine();
            System.out.println("Réponse du serveur : " + reponse);
            System.out.flush();
            s.close();
            System.out.println("Connexion fermée.");
            System.out.flush();}

    }
    catch (UnknownHostException e)//c'est vous avez l'add ip mich hiya w ella serveur mabdach ma7alich socket
    {
        e.printStackTrace();
    }
    catch (IOException e){
        e.printStackTrace();
    }
    
}
}