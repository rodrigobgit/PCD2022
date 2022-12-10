package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import game.Game;

public class Server {
	public static final int PORTO = 8080;
	private Game game;
	private int newPlayerID=Game.NUM_BOT_PLAYERS-1;
	
	public void startServing() throws IOException {
		System.out.println("Server started");
		ServerSocket ss = new ServerSocket(PORTO);
		//inicia o jogo
		game=new Game();
		game.go();
		try {
			while (!game.isEndOfGame()) { // para ter varios clientes, vários de cada vez
				Socket socket = ss.accept(); // Fica em espera até alguem se conetar
				DealWithClient dwc = new DealWithClient(socket,this); // cria a thread, passando a socket				
				dwc.start(); // faz a thread correr e volta ao início do while
			}
		} finally {
			ss.close();
			
		}
	}

	public Game getGame() {
		return game;
	}

	public synchronized int getNewPlayerID() {
		newPlayerID++;
		return newPlayerID;
	}

	public static void main(String[] args) {
		try {
			new Server().startServing();
		} catch (IOException e) {
		}
	}
}
