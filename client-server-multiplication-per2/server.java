import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
///awil serveur ncodih fi 7eyeti i'm sooo excited and happy :]

public class server {
	public static void main(String[] args) throws Exception {
		ServerSocket ss = new ServerSocket(12345);//lina na5la9 fi serveur ya3mil fil connexion 3al port 1234 ServerSocket hiya class fi java
		Socket s = ss.accept();//lina 3amalt socket l'instruction ss.accept() instruction bloqante titsama 3alach ? 5atirha tbloqui programme ya3ni yo93od yistana 7atta yijih client yi7ib ya3mil connexion
		InputStream is = s.getInputStream();//hathi t5alini na9ra client cheb3ath
		OutputStream os = s.getOutputStream();//hathi t5alini n3adi l client des donnees
		//lina ne7athar fi un canal meta3 communication entre le client et le serveur
		int nb=is.read();//hathi instruction bloqante t5alini na9ra l'input mta3 client w n7otou fi nv w bloquante 5atir to93od tistana client yab3ath une données
		//tres important avec inputstream on attend juste un octet
		int rep=nb*2;
		os.write(rep);
		s.close();
		ss.close();//hathi t5alini n7abess l'ecoute mta3 serveur w n7abess l'ecoute mta3 socketa
		
		 
		
	}
}

