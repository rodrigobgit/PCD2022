package environment;

import javax.swing.event.DocumentListener;

import game.Game;
import game.Player;

public class Cell {
	private Coordinate position;
	private Game game;
	private Player player;
	
	public Cell(Game g, Coordinate position) {
		super();
		this.position = position;
		this.game=g;
	}

	public Coordinate getPosition() {
		return position;
	}

	public boolean isOcupied() {
		return player != null;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	//coloca jogador
	public synchronized void initialPut(Player player) {
		while(isOcupied()) {
			Player occupantPlayer;
			occupantPlayer = getPlayer();
			System.out.println("Sou o jogador " + player.getIdentification() + " e fiquei parado nas coordenadas "
			+ getPosition().toString() + " por causa do jogador " + occupantPlayer.getIdentification());
			try {
				wait();
				
			} catch(InterruptedException e) {
				
			}
		}	
		
		setPlayer(player);
	}
	//o que fazer aqui? o metodo clear tem de passar a estar no metodo movementPut. O movementPut vai fazer clear da cell atual e set na proxima cell.
	
	
	//move jogador para esta celula
	public synchronized void movementPut(Player player) {
		if (isOcupied()) {
			Player occupantPlayer;
			occupantPlayer = getPlayer();
		} else {
			setPlayer(player);
		}
	}
	//limpa c�lula
	public synchronized void clear() {
		setPlayer(null);
		notifyAll();
	}
	

}
