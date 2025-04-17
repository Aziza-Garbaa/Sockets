import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{
public static ArrayList <ClientHandler> clientHandlers = new ArrayList<>();
private Socket socket;
private BufferedReader bufferedReader;
private BufferedWriter bufferedWriter;
private String clientusername;
public ClientHandler(Socket socket){
    try{
        this.socket=socket;
       this.bufferedWriter= new BufferedWriter( new OutputStreamWriter(socket.getOutputStream()));
       //creation de flux sortant
       this.bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
       //creation de flux entrant
       this.clientusername=bufferedReader.readLine();
       //lecture du nom d'utilisateur
       clientHandlers.add(this);
       //enregistrement de l'utilisateur dans l'arraylist statique qui peut être accédée par toutes les instances de ClientHandler
        sendMessage("SERVEuR: "+clientusername + " a entré dans le chat !");
        sendMessage("Utilisez le format @nom_utilisateur message pour discuter en privé.");
    }
    catch (IOException e) {
        closeEverything(socket,bufferedReader,bufferedWriter);
    }
    }
@Override
public void run(){
    String messageFromClient;
    while(socket.isConnected()){//verification s'il y a toujours une connexion
        try{
            messageFromClient=bufferedReader.readLine();
            if(messageFromClient.equalsIgnoreCase("exit")||messageFromClient==null)
           { closeEverything(socket,bufferedReader,bufferedWriter);break;}
            if(messageFromClient.startsWith("@")){
                int index_space=messageFromClient.indexOf(" ");
                if(index_space!=-1)
                {String client2=messageFromClient.substring(1,index_space);
                String messageToSend =messageFromClient.substring(index_space+1);
                sendPrivateMessage(client2,messageToSend);}
                else{sendMessage("Format incorrect il faut respecter cette forme :@nom_utilisateur votre_message");}
            }
            else{sendMessage("veuillez utiliser@nom pour envoyer un message privé");}
            
        }
        catch(IOException e){
            closeEverything(socket,bufferedReader,bufferedWriter);
            break;
        }
    }
}
//methode pour envoyer un message au client courant
public void sendMessage(String messageToSend){
    try{
        bufferedWriter.write(messageToSend);
        bufferedWriter.newLine();
        bufferedWriter.flush();
        
    }
     catch (IOException e) {
        closeEverything(socket, bufferedReader, bufferedWriter);
    }

}
//methode pour supprimer un client de l'arraylist
public void removeClientHandler(){
    clientHandlers.remove(this);
    sendMessage("SERVEUR: "+clientusername+" a quitté le chat !");
}
//methode pour supprimer un utilisateur et fermer sa session
public void closeEverything(Socket socket,BufferedReader bufferedReader,BufferedWriter bufferedWriter){
    removeClientHandler();
    try{
        if(bufferedReader!=null){
            bufferedReader.close();
        }
        if(bufferedWriter!=null){
            bufferedWriter.close();
        }
        if(socket !=null){
            socket.close();
        }

    }
    catch(IOException e){
        e.printStackTrace();
    }
}
public String getClientusername(){
    return this.clientusername;
}
//methode pour envoyer un message d'un client à un autre
public void sendPrivateMessage(String client2 ,String message){
    for(ClientHandler clientHandler : clientHandlers){
        if(clientHandler.clientusername.equals(client2)){
            clientHandler.sendMessage(this.clientusername+"(privé): "+message);
            this.sendMessage("message envoyé");
            return;
        }
    }
    sendMessage("Utilisateur "+client2+" non trouvé");
}
}

