import java.io.*;
import java.net.*;
import java.util.HashSet;
import java.util.HashMap;
public class server {
    private static int PORT = 1234;// Port sur lequel le serveur UDP écoute
    private static String PASSWORD = "pass";// Mot de passe pour le serveur
    private static HashSet<String> nicknames = new HashSet<>();
    // Ensemble pour stocker les pseudos des utilisateurs connectés
    private static HashMap<String, ClientInfo> clients = new HashMap<>();
    // Map pour stocker les informations des clients (adresse IP et port) associés à leur pseudo
    private static DatagramSocket socket;// Socket standard pour communiquer avec le serveur
    private static InetAddress groupaddress;// Adresse du groupe multicast
    private static int group_port = 1235;// Port du groupe multicast
    //Classe interne représentant un client avec son adresse IP et son port.
    // Nécessaire pour pouvoir lui répondre ou le retirer proprement du chat.
    private static class ClientInfo {
        InetAddress address;
        int port;
        ClientInfo(InetAddress address, int port) {
            this.address = address;
            this.port = port;
        }
    }
    //Fonction principale du serveur qui écoute les messages des clients et gère les connexions
    // et déconnexions. Elle utilise un socket UDP pour recevoir les messages et les traiter.
    public static void main(String[] args) throws IOException {
        socket = new DatagramSocket(PORT);// Création du socket UDP pour communication directe avec le serveur
        groupaddress = InetAddress.getByName("230.0.0.1");// Adresse IP multicast (doit être dans la plage 224.0.0.0 -239.255.255.255)
        System.out.println("Server is running on port " + PORT + "...");
        System.out.println("Multicast group: " + groupaddress + ":" + group_port);
        //Messages de démarrage pour debug/affichage.
        byte[] buf = new byte[256];// // Buffer pour recevoir les messages
        // Boucle principale du serveur qui écoute les messages des clients et les traite.
        //boucle infinie poue etre toujours a l'ecoute des messages
        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                // Création d'un paquet pour recevoir les messages des clients
                socket.receive(packet);
                // Réception du paquet
                // On traite le message reçu
                String received = new String(packet.getData(), 0, packet.getLength());
                // On convertit le message reçu en chaîne de caractères
                // On affiche le message reçu et l'adresse IP et le port du client qui l'a envoyé
                System.out.println("Received: " + received + " from " + packet.getAddress() + ":" + packet.getPort());
                //si le message commence par "pass:", on traite le mot de passe
                //si le message commence par "NICK:", on traite le pseudo
                //si le message commence par "LEAVE:", on traite la demande de déconnexion
                //
                if (received.startsWith("pass:")) {
                    handlePassword(received.substring(5), packet.getAddress(), packet.getPort());
                } else if (received.startsWith("NICK:")) {
                    handleNickname(received.substring(5), packet.getAddress(), packet.getPort());
                } else if (received.startsWith("LEAVE:")) {
                    String nickname = received.substring(6).trim();
                    System.out.println("Processing leave request for: '" + nickname + "'");
                    handleLeave(nickname);
                } else {
                    broadcastMessage(received);
                }
            } catch (IOException e) {
                System.err.println("Error processing packet: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    // Fonction pour traiter le mot de passe envoyé par le client
    private static void handlePassword(String password, InetAddress address, int port) throws IOException {
        // On enlève les espaces au début et à la fin du mot de passe
        if (PASSWORD.equals(password)) {
            sendToClient("PASSWORD_OK", address, port);
            // On envoie un message au client pour lui indiquer que le mot de passe est correct
            System.out.println("Password accepted for client: " + address + ":" + port);
        } else {
            sendToClient("ERROR:Incorrect Password", address, port);
            System.out.println("Password rejected for client: " + address + ":" + port);
        }
    }
    // Fonction pour traiter le pseudo envoyé par le client
    // Elle vérifie si le pseudo est valide et s'il n'est pas déjà pris
    private static void handleNickname(String nickname, InetAddress address, int port) throws IOException {
        nickname = nickname.trim();// Enlève les espaces au début et à la fin du pseudo
        // Vérifie si le pseudo est vide ou null
        if (nickname.isEmpty()) {
            sendToClient("ERROR:Invalid nickname", address, port);
            return;
        }
        // Vérifie si le pseudo est déjà pris
        if (nicknames.add(nickname)) {
            clients.put(nickname, new ClientInfo(address, port));
            sendToClient("NICKNAME_OK", address, port);
            System.out.println("New user joined: " + nickname + " from " + address + ":" + port);
            broadcastMessage(nickname + " joined the chat");
        } else// Si le pseudo est déjà pris, on envoie un message d'erreur au client
        {
            sendToClient("ERROR:Nickname already taken", address, port);
            System.out.println("Nickname already taken: " + nickname);
        }
    }
    // Fonction pour traiter la demande de déconnexion du client
    // Elle retire le pseudo de l'ensemble des pseudos et du map des clients
    private static void handleLeave(String nickname) throws IOException {
        System.out.println("User " + nickname + " is leaving the chat");
        // On enlève le pseudo de l'ensemble des pseudos et du map des clients
        // On envoie un message de départ au client
        if (nicknames.remove(nickname)) {
            clients.remove(nickname);
            // Make the broadcast message clear
            String leaveMessage = nickname + " left the chat";
            broadcastMessage(leaveMessage);
            System.out.println("Broadcast leave message: " + leaveMessage);
        } else {
            System.out.println("Warning: Received leave request for unknown nickname: " + nickname);
        }
    }
    // Fonction pour envoyer un message à un client spécifique
    // Elle utilise un DatagramPacket pour envoyer le message au client
    private static void sendToClient(String message, InetAddress address, int port) throws IOException {
        byte[] buf = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);
        System.out.println("Sent to client (" + address + ":" + port + "): " + message);
    }
    // Fonction pour envoyer un message à tous les clients connectés
    // Elle utilise un DatagramPacket pour envoyer le message au groupe multicast
    // Elle utilise l'adresse IP multicast et le port du groupe multicast
    private static void broadcastMessage(String message) throws IOException {
        byte[] buf = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, groupaddress, group_port);
        socket.send(packet);
        System.out.println("Broadcast: " + message);
    }
}