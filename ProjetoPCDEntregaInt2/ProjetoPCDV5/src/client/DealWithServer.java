package client;

import java.io.*;
import java.net.*;
import utils.Message;
import utils.Data;

public class DealWithServer extends Thread{
	
	private Game game;
	private ObjectInputStream in;
	private PrintWriter out;
	private Socket socket;	
	private GameGuiMain gui;
	private boolean isReceiving=false;
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
				
		game=new Game();		
		gui=new GameGuiMain(game);		
		gui.init();
//		game.notifyChange();
//		isReceiving=true;
		while(!isGameOver()){
			Message msg=(Message)in.readObject();
			game.setMsg(msg);
			game.notifyChange();
			
		}
		
		
		
		
		
		
		

		
		
		
		
		
	}
	
	public boolean isReceiving() {
		return isReceiving;
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