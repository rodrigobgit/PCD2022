package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public static final int PORTO = 8080;

	public void startServing() throws IOException {
		System.out.println("Server started");
		ServerSocket ss = new ServerSocket(PORTO);
		try {
			while (true) { // para ter varios clientes, vários de cada vez
				Socket socket = ss.accept(); // Fica em espera até alguem se conetar
				DealWithClient dwc = new DealWithClient(socket); // cria a thread, passando a socket
				dwc.start(); // faz a thread correr e volta ao início do while
			}
		} finally {
			ss.close();
		}
	}

	public static void main(String[] args) {
		try {
			new Server().startServing();
		} catch (IOException e) {
			
		}
	}
}
