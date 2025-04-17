import java.net.DatagramPacket;
import java.net.DatagramSocket;
public class UDPServer {
        public static void main(String[] args) throws Exception {
        DatagramSocket ds = new DatagramSocket(12345); 
        // On crée une socket UDP qui écoute sur le port 12345
        // On peut choisir n'importe quel port libre (au-dessus de 1024) pour le serveur
        // On peut aussi ne pas spécifier de port, et  dans ce cas le système choisira un port libre automatiquement
        byte[] buffer = new byte[1024]; 
        // On crée un tableau de bytes pour stocker la réponse du serveur (1024 bytes ici, mais on peut choisir une taille différente)
        DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
        // On crée un paquet pour recevoir les données depuis le client
        // Le paquet contient le buffer (le tableau de bytes) et sa taille (buffer.length)
        // Le paquet va être utilisé pour recevoir les données envoyées par le client
        System.out.println("Serveur UDP en attente d'un message...");
        ds.receive(receivePacket); 
        // On attend de recevoir un message du client (instruction bloquante)
        // Quand un message est reçu, il est stocké dans le paquet receivePacket
        // On peut aussi utiliser receivePacket.getData() pour récupérer les données du paquet reçu
        int nb = buffer[0]; 
        // On lit le premier octet du message (le nombre envoyé par le client)
        // On suppose que le client a envoyé un entier (un byte ici, mais on peut aussi envoyer des entiers plus grands)
        // On peut aussi utiliser receivePacket.getData() pour récupérer les données du paquet reçu
        int result = nb * 2; 
        // On effectue la multiplication par 2 ( le but de notre programme)
        byte[] response = {(byte) result}; 
        // On prépare la réponse (le résultat) à envoyer au client, sous forme de byte
        // On convertit le résultat en tableau de bytes pour l'envoyer (les donnees sont toujours sous forme de "raw data" dans les paquets UDP)
        DatagramPacket sendPacket = new DatagramPacket(response, response.length, receivePacket.getAddress(),  receivePacket.getPort());
        // On crée un paquet à envoyer au client, en utilisant son adresse IP et port
        // On utilise receivePacket.getAddress() et receivePacket.getPort() pour récupérer l'adresse IP et le port du client
        ds.send(sendPacket); 
        // On envoie la réponse au client
        ds.close(); 
        // On ferme la socket du serveur
    }
}
