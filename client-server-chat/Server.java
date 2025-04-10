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
        Socket s = ss.accept();//lina bloquit el serveur ila mayijih client yi7ib ya3mil connexion
       
        InputStream is = s.getInputStream();
        OutputStream os = s.getOutputStream();
        System.out.flush();
        //create canal de communication entre le serveur et le client
        BufferedReader in =new BufferedReader(new InputStreamReader(is,"UTF-8"),8192);//9ibili sta3malna fonction read alli a7na 9olna ta9ra octet barka w ykoun entier lina ana ni7ib msg donc fama barcha optionnet tw no7thomlik fil fichier doc minhom hathi alli hiya bach na9ra text envoyé par le client
        //BufferedReader in = new BufferedReader(new InputStreamReader(is));lina inputstreamreader yi7awil les octets met3 inputstream en texte
        System.out.flush();
        PrintWriter o = new PrintWriter(os, true);//hathi ser à envoyer le test vers le client w true hiya active auto-flush ya3ni msg envoyé immediatement
        //printwriter hiya reelement te7athar fil flux ya3ni ana 7athart flux os 9olt hatha flux de sortie twa printwriter 9alit rahou hatha flux de sortie bach ykoun texte w awal maya7thar lazem yitib3ath toul
        
        
        
        System.out.flush();
        System.out.println("En attente de message du client...");
        o.println("en attente de message du client...");
        o.flush();
        System.out.flush();
        
        String message = in.readLine();
        //bufferedreader y9ra el message kamil tous les lignes bi fathil fonction .readLines();
        if(message!= null && !message.trim().isEmpty()){
        System.out.println("Message reçu du client : " + message);
        System.out.flush();}
        else{
            System.out.println("Aucun message reçu.");
            System.out.flush();
        }


      
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("Entrez votre réponse : ");
        System.out.flush();
        String reponse = scanner.nextLine();
        scanner.close();
        o.println(reponse);//donc lina b3atht msg
        System.out.println("Réponse envoyée au client : " + reponse);
        System.out.flush();
        s.close();
        ss.close();
        System.out.println("Connexion fermée.");
        System.out.flush();
        

    }
    catch (IOException e)//ioexception d'entrée/sortie comme connexion rx
    {e.printStackTrace();}//printStackTrace affiche les details de l'erreur kima la nature de l'erreur w win bithabit sar
    
}
}
