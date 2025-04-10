import java.net.Socket;
import java.util.Scanner;
import java.io.InputStream;
import java.io.OutputStream;

public class client {
    public static void main(String[] args) throws Exception {
        //coté serveur 3andou socket w cote client 3andou socket
        Socket s = new Socket("127.0.0.1", 12345);//lina localhost ya3ni 3andou serveur fi localhost w 1234 ya3ni l port mta3 serveur ama najam n7ot add ip met3 pc met3i w ela add ip met3 serveur kanou distant
        InputStream is = s.getInputStream();
        OutputStream os = s.getOutputStream();
        //lina je cree les flux entree w sortie meta3 communication entre le client et le serveur
        System.out.println("connexion etablie");
        System.out.println("entrer un entier");
         Scanner scanner = new Scanner(System.in);
            int message = scanner.nextInt();
            scanner.close();
        os.write(message);//lina output yani alli bach yaba3thou l serveur objet ismou os 7at fih alli bach yida5lou utilisateur
        int rep=is.read();//lina yistana reponse min 3and serveur
        System.out.println("reponse de serveur: "+rep);//affichage l resultat
        s.close();//lina n7abess l'ecoute mta3 socket w n7abess l'ecoute mta3 serveur
    }
    
}
