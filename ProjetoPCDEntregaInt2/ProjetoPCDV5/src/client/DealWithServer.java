package client;

import java.io.*;
import java.net.*;
import game.Game;
import utils.Message;


public class DealWithServer extends Thread{
	
	private Game game;
	private ObjectInputStream in;
	private PrintWriter out;
	private Socket socket;	
	private GameGuiMain gui;
	
	public DealWithServer(Socket socket) {
		super();
		this.socket=socket;
	}
	
	
	public void run() {
		try {
			doConnections(socket);
			serve();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void serve() throws ClassNotFoundException, IOException {
				
		
		Message first=(Message)in.readObject();	
		game=new Game();		
		gui=new GameGuiMain(game);		
		gui.init();
		
		
		
		

		
		
		
		
		
	}
	
	
	
	
	private void doConnections(Socket socket) throws IOException {
		in = new ObjectInputStream ( socket.getInputStream ());
		out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
				socket.getOutputStream())), true);
	}
	
	public boolean isGameOver() {
		return false;
	}
}