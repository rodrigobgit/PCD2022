package server;

import java.io.*;
import java.net.Socket;

import game.Game;
import game.HumanPlayer;
import utils.Message;

public class DealWithClient extends Thread {

	private Socket socket;
	private BufferedReader  in;
	private ObjectOutputStream out;
	private Server server;
	

	public DealWithClient(Socket socket,Server server) {
		super();
		this.socket = socket;
		this.server=server;
	}

	@Override
	public void run() {
		try {
			doConnections(socket);
			serve();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void doConnections(Socket socket) throws IOException {
		out = new ObjectOutputStream (socket.getOutputStream());
		in = new BufferedReader (new InputStreamReader ( socket.getInputStream ()));
	}

	private void serve() throws IOException, InterruptedException {
		
		Game game=server.getGame();
		
		//ver esta situacao do id, que id devemos meter etc
		HumanPlayer player=new HumanPlayer(game, 90, (byte) 5);
		player.start();
		
				
		//manda o estado no jogo no momento que o cliente liga		
		out.writeObject(new Message(game.getBoard()));
		
		//manda atualizacao do estado do jogo ciclicamente
		while (true) {
			sleep(Game.REFRESH_INTERVAL);
			//fazer o out.write
			
			
		}
	}

}