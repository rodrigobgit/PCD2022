package environment;

import game.Game;
import game.Player;

public class Cell {
	private Coordinate position;
	private Game game;
	private Player player;
	public Cell(Game g, Coordinate position) {
		super();
		this.game=g;
		this.position = position;
	}

	public Coordinate getPosition() {
		return position;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public Cell getCell(Coordinate at) {
		return game.board[at.x][at.y];
	}

	public boolean isOcupied() {
		return player != null;
	}

	public synchronized void initialPut(Player player) { // Usar varáveis condicionais (pelo menos, aqui)
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
		game.notifyChange();
		notifyAll();
	}

	// Processa movimento do jogador, colocando-o na nova célula
	public synchronized void movementPut(Player player) {
		setPlayer(player);
	}
	
	// Limpa celula onde estava o jogador
	public synchronized void clear() {
		setPlayer(null);
		game.notifyChange();
		notifyAll();
	}
}
