package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import javax.net.ssl.SSLException;



public class Client {
	private BufferedReader in;
	private PrintWriter out;
	private Socket socket;
	private InetAddress ip;
	private int port;
	
	
	
	public Client(InetAddress ip, int port) {
		this.ip=ip;
		this.port=port;
		
	}
	
	public static void main(String[] args) throws IOException {
		InetAddress serverip=InetAddress.getByName(args[0]);
		int serverport=Integer.parseInt(args[1]);
		Client client=new Client(serverip,serverport);
		client.runClient();
		
	}

	public void runClient() throws IOException {
		
		try {
			connectToServer();
			DealWithServer dws=new DealWithServer(socket);
			dws.start();
			
			//para manter a ligação aberta por agora
			while(!dws.isGameOver()) {
				
			}
				
			
		} finally {				
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void connectToServer() throws IOException {		
		System.out.println("Endereço = " + ip);
		socket = new Socket(ip, port);
		System.out.println("Socket = " + socket);
		
		

	}



}
