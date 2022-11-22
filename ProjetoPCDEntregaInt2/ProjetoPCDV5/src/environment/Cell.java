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

	public boolean isOccupied() {
		return player != null;
	}

	public void initialPut(Player player) throws InterruptedException { // Usar varáveis condicionais
		lock.lock();
//		System.out.println("1 - Célula " + getPosition() + " tentada pelo jogador " + player.getIdentification());
		try {
			while (isOccupied()) {
				Player occupantPlayer;
				occupantPlayer = getPlayer();
//				System.out.println("2 - Sou o jogador " + player.getIdentification() + " e não fiquei com as coordenadas "
//						+ getPosition().toString() + " por causa do jogador " + occupantPlayer.getIdentification());
				cellIsFree.await();
//				System.out.println("3 - Sou o jogador " + player.getIdentification() + " penso ficar nas coordenadas "
//						+ getPosition().toString());

			}
			setPlayer(player);
//			System.out.println("4 - Sou o jogador " + player.getIdentification() + " e fiquei nas coordenadas "
//					+ getPosition().toString());
			
			game.notifyChange();
		} finally {
			lock.unlock();
		}
	}

	// Processa movimento do jogador
	public void movementPut(Player movingPlayer, Cell currentCell) { // Método instanciado pela nextCell (this)
		lock.lock();
		if (isOccupied()) { // nextCell está ocupada por outro jogador
			if (this.getPlayer().getCurrentStrength() > 0 && this.getPlayer().getCurrentStrength() < 10) {
				movingPlayer.duel(this.getPlayer()); // getPlayer traz jogador que ocupa a célula que movingPlayer pretende
			}
			
// fazer a imobilizacao de 2 seg aqui
			
		} else { // nextCell está livre
			currentCell.setPlayer(null); // Coloca o movingPlayer a null, na currentCell
			cellIsFree.signalAll();
			this.setPlayer(movingPlayer); // Coloca o movingPlayer no destino (nextCell)
			if (movingPlayer.isHumanPlayer()) movingPlayer.setMove(0);
		}
		game.notifyChange();
		lock.unlock();
	}

}
