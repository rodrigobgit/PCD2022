package environment;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import game.Game;
import game.Player;

public class Cell {
	private Coordinate position;
	private Game game;
	private Player player;
	private Lock lock = new ReentrantLock();
	private Condition cellIsOccupied = lock.newCondition(); // Não apagar (servirá para variáveis condicionais)
	private Condition cellIsFree = lock.newCondition(); // Não apagar  (servirá para variáveis condicionais)
	
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

	public synchronized void setPlayer(Player player) {
		this.player = player;
	}
	
	public Cell getCell(Coordinate at) {
		return game.board[at.x][at.y];
	}

	public boolean isOcupied() {
		return player != null;
	}

	/* Não apagar as linhas comentadas que se seguem (servirá para implementar variáveis condicionais)
	public synchronized void initialPut(Player player) throws InterruptedException { // Usar varáveis condicionais
		lock.lock();
		while (isOcupied()) {
			Player occupantPlayer;
			occupantPlayer = getPlayer();
			System.out.println("Sou o jogador " + player.getIdentification() + " e fiquei parado nas coordenadas "
					+ getPosition().toString() + " por causa do jogador " + occupantPlayer.getIdentification());
			try {
				cellIsOccupied.await();
				setPlayer(player);
				game.notifyChange();
				cellIsFree.signalAll();
			} finally {
				lock.unlock();
			}
		}
	} */

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
	public synchronized void movementPut(Player movingPlayer, Cell currentCell) { // Método instanciado pela nextCell (this)
		if (isOcupied()) { // nextCell está ocupada por outro jogador
			if (this.getPlayer().getCurrentStrength() > 0 && this.getPlayer().getCurrentStrength() < 10) {
				movingPlayer.duel(this.getPlayer()); // getPlayer traz jogador que ocupa a célula que movingPlayer pretende
			}

// fazer a imobilizacao de 2 seg aqui

		} else {
			currentCell.setPlayer(null); // Coloca o movingPlayer a null, na currentCell
			this.setPlayer(movingPlayer); // Coloca o movingPlayer, na nextCell
			if (movingPlayer.isHumanPlayer())
				movingPlayer.setMove(0);
		}
		game.notifyChange();
		notifyAll();
	}

/*	// Limpa celula onde estava o jogador
	public synchronized void clear() {
		setPlayer(null);
		game.notifyChange();
		notifyAll();
	}*/
}
