import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
public class client {
    private static int server_port = 1234;// Port sur lequel le serveur UDP écoute
    private static int group_port = 1235;// Port du groupe multicast
    private static String server_ip = "localhost";// Adresse IP du serveur
    //declaration des sockets 
    private static DatagramSocket socket;//Socket standard pour communiquer avec le serveur
    private static MulticastSocket multicastsocket;// Socket pour rejoindre et écouter le groupe multicast
    private static InetAddress groupaddress; // Adresse du groupe multicast
    //declaration des variables de controle
    private static String nickname;// Pseudo de l'utilisateur
    private static boolean joined = false;// Indique si l'utilisateur a rejoint le chat
    private static AtomicBoolean running = new AtomicBoolean(true);
    // Contrôle l'état d'exécution car lorsque l'utilisateur quitte le chat, il faut arrêter les threads d'écoute 
    //sinon ils continuent à écouter et à recevoir des messages et on peut avoir des exceptions de socket fermée
    public static void main(String[] args) throws IOException {
        socket = new DatagramSocket();// Création du socket UDP pour communication directe avec serveur
        multicastsocket = new MulticastSocket(group_port); // Socket pour rejoindre un groupe multicast
        groupaddress = InetAddress.getByName("230.0.0.1");// Adresse IP multicast (doit être dans la plage 224.0.0.0 - 239.255.255.255)
        NetworkInterface nif = NetworkInterface.getByName("lo");// Spécifie l'interface réseau à utiliser (ici 'lo' = loopback = localhost)
        multicastsocket.joinGroup(new InetSocketAddress(groupaddress, group_port), nif);// Rejoindre le groupe multicast 
        Scanner scanner = new Scanner(System.in);
        System.out.print("enter password :"); // On demande un mot de passe à l'utilisateur qui est "pass"
        // pour le moment, on ne fait pas de cryptage, on enverra le mot de passe en clair
        String password = scanner.nextLine();// // On demande un mot de passe à l'utilisateur
        sendtoserver("pass:" + password);// // On demande un mot de passe à l'utilisateur 
        // On crée deux threads pour écouter le serveur et le groupe multicast
        Thread serverListenerThread = new Thread(client::listentoserver);//listentoserver est la runnable qu ce thread va executer 
        Thread groupListenerThread = new Thread(client::listentogroup);//listentogroup est la runnable qu ce thread va executer
        //thread en mode daemon pour qu'il s'arrête quand le programme principal s'arrête
        // Un thread daemon est un thread qui s'exécute en arrière-plan et qui ne bloque pas l'arrêt de l'application
        serverListenerThread.setDaemon(true);
        groupListenerThread.setDaemon(true);
        // On démarre les threads d'écoute
        serverListenerThread.start();
        groupListenerThread.start();
        // On attend que l'utilisateur rejoigne le chat avant de lui permettre d'envoyer des messages
        while (!joined) {
            try {
                Thread.sleep(100);
                //Pause pour éviter la boucle active
                // On utilise Thread.sleep pour éviter une boucle active qui consommerait trop de ressources CPU
                /*puisque l'udp n'a pas le meme caractere bloquant
                que celui de tcp , on doit nous meme construire de code bloquant 
                pour ne pas encombrer les ressources*/ 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Interrupted while waiting to join.");
                return;
            }
        }
        // Une fois que l'utilisateur a rejoint le chat, on entre dans la boucle principale du chat et on lui permet d'envoyer des messages
        while (joined && running.get()) {
            if (scanner.hasNextLine()) {// Vérifie si l'utilisateur a entré quelque chose
                // On lit le message de l'utilisateur
                String msg = scanner.nextLine();
                if ("exit".equalsIgnoreCase(msg)) {
                    leavechat();// Envoie le message de départ au serveur
                    break;
                } else if (msg.length() <= 100) {
                    sendtoserver(nickname + ": " + msg);// Envoie le message au serveur
                } else {
                    System.out.println("Message too long. Maximum 100 characters allowed.");
                }
            }
        }
        // On attend un peu pour s'assurer que le message de départ est envoyé avant de quitter
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // Ignorer
        }
        
        System.exit(0);// Quitte le programme lorsque l'utilisateur tape "exit"
    }
    //fonction pour envoyer un message au serveur
    private static void sendtoserver(String message) throws IOException {
        if (!running.get() && !message.startsWith("LEAVE:")) return;
        
        byte[] buf = message.getBytes();
        InetAddress address = InetAddress.getByName(server_ip);
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, server_port);
        socket.send(packet);
    }
    // la fonction runnable passe au thread  qui écoute les réponses du serveur
    private static void listentoserver() {
        try {
            byte[] buf = new byte[256];// Buffer pour recevoir les messages du serveur
            // On écoute les messages du serveur
            while (running.get()) {
                try {
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);// reception d'un paquet UDP
                    String received = new String(packet.getData(), 0, packet.getLength());
                    //extraire le message du paquet UDP dans une chaine de caractere 
                    //on teste le msg entree par le serveur pour savoir donner acces au client de joindre ou pas
                    if ("PASSWORD_OK".equals(received)) {
                        System.out.print("Enter your nickname: ");
                        nickname = new Scanner(System.in).nextLine();
                        sendtoserver("NICK:" + nickname);
                    } else if ("ERROR:Incorrect Password".equals(received) || "ERROR:Nickname already taken".equals(received)) {
                        System.out.println(received);
                        running.set(false);
                        return;
                    } else if ("NICKNAME_OK".equals(received)) {
                        joined = true;
                        System.out.println("You have joined the chat. You can now send messages. Type 'exit' to leave the chat.");
                    }
                } catch (IOException e) {
                    if (running.get()) {
                        // Only print stack trace if we're still running
                        System.out.println("Server listener error: " + e.getMessage());
                    }
                    return;
                }
            }
        } catch (Exception e) {
            if (running.get()) {
                e.printStackTrace();
            }
        }
    }
    //la fonction runnable passe au thread  qui écoute les messages du groupe multicast
    private static void listentogroup() {
        try {
            byte[] buf = new byte[256];
            while (running.get()) {
                try {
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    multicastsocket.receive(packet);
                    String received = new String(packet.getData(), 0, packet.getLength());
                    if (joined) {
                        System.out.println(received);
                    }
                } catch (IOException e) {
                    if (running.get()) {
                        System.out.println("Group listener error: " + e.getMessage());
                    }
                    return;
                }
            }
        } catch (Exception e) {
            if (running.get()) {
                e.printStackTrace();
            }
        }
    }
    // Fonction pour quitter le chat
    // On envoie un message de départ au serveur et on ferme les sockets
    private static void leavechat() throws IOException {
        if (!joined) return;
        sendtoserver("LEAVE:" + nickname);
        // On attend un peu pour s'assurer que le message de départ est envoyé avant de quitter
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // On arrête les threads d'écoute
        running.set(false);
        joined = false;
        // Quitter le groupe multicast
        try {
            NetworkInterface nif = NetworkInterface.getByName("lo");
            multicastsocket.leaveGroup(new InetSocketAddress(groupaddress, group_port), nif);
        } catch (IOException e) {
            // Ignore exceptions during cleanup
        }
        // On ferme les sockets avec un delay pour s'assurer que les messages sont envoyés
        try {
            if (!socket.isClosed()) {
                socket.close();
            }
        } catch (Exception e) {
            // Ignore exceptions during cleanup
        }
        try {
            if (!multicastsocket.isClosed()) {
                multicastsocket.close();
            }
        } catch (Exception e) {
            // Ignore exceptions during cleanup
        }
        System.out.println("You left the chat.");
    }
}