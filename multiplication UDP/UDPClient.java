// Importation des classes nécessaires pour la communication UDP 
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;//InetAddress est utilisé pour représenter une adresse IP
import java.util.Scanner;
public class UDPClient {
    public static void main(String[] args) throws Exception {
        DatagramSocket ds = new DatagramSocket(); 
        // On crée une socket UDP pour le client (le port sera attribué automatiquement si on ne l'indique pas)
        Scanner scanner = new Scanner(System.in); // On crée un scanner pour lire l'entrée de l'utilisateur
        System.out.println("Entrez un entier :");
        int nb = scanner.nextInt(); // On lit un entier entré par l'utilisateur
        scanner.close(); 
        // On ferme le scanner (on libère les ressources utilisées par le scanner)
        byte[] data = {(byte) nb}; 
        // On convertit l'entier en tableau de bytes pour l'envoyer (les donnees sont toujours sous forme de "raw data" dans les paquets UDP)
        InetAddress serverAddress = InetAddress.getByName("127.0.0.1");
        //la fonction getByName() permet de convertir une chaîne de caractères représentant en une adresse IP (le type est InestAddress) "
        // On définit l'adresse IP du serveur (localhost ici; adresse loopback puisque on va faire tourner le client et le serveur sur la même machine)
        DatagramPacket sendPacket = new DatagramPacket(data, data.length, serverAddress, 12345); 
        // On crée un paquet contenant les données à envoyer au serveur
        //( le datagrampacket a pour parametres le tableau de bytes(data), la taille du tableau(data.length), l'adresse IP du serveur et le port du serveur)
        ds.send(sendPacket);
        // On envoie le paquet au serveur
        byte[] buffer = new byte[1024]; 
        // On prépare un buffer pour recevoir la réponse du serveur (raw data reçue)
        // On crée un tableau de bytes pour stocker la réponse du serveur (1024 bytes ici, mais on peut choisir une taille différente)
        DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length); 
        // On crée un paquet pour recevoir la réponse du serveur (le buffer est le tableau de bytes qui va contenir la réponse)
        ds.receive(receivePacket); 
        // On attend de recevoir la réponse du serveur (instruction bloquante)
        int result = buffer[0]; 
        // On récupère le résultat (la réponse du serveur) en lisant le premier byte du buffer (on suppose que la réponse est un entier)
        // On peut aussi utiliser receivePacket.getData() pour récupérer les données du paquet reçu
        //(mais ici on a déjà le buffer et on sait que c'est un entier dans dautres exemple on va utiliser cette methode )
        // On lit le résultat (la réponse du serveur)
        System.out.println("Réponse du serveur : " + result); 
        // On affiche la réponse du serveur (le résultat)
        ds.close(); 
        // On libère les ressources utilisées par la socket (on ferme la socket pour éviter les fuites de mémoire et libérer les ressources)   
    }
} 

