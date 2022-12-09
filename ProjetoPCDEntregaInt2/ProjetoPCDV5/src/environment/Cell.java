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

	public void initialPut(Player player) throws InterruptedException { // É invocado com a célula que o jogador pretende para colocação
		lock.lock();

		try {
			if (isOccupied()) { // Célula ocupada -> sysout com informação
				Player occupantPlayer;
				occupantPlayer = getPlayer();
				System.out.println("2 - Sou o jogador " + player.getIdentification()
						+ " e não fiquei com as coordenadas " + getPosition().toString() + " por causa do jogador "
						+ occupantPlayer.getIdentification());
				player.setNotPlacedAtFirst(); // Jogador nao foi colocado à primeira tentativa. Variável serve para que não tenha de esperar 2 x 10 segundos
			}
			
			while (isOccupied() && getPlayer().isActive()) { // Encontrou jogador ativo na célula
				System.out.println("3 - Sou o jogador " + player.getIdentification() + " e o jogador ocupante está ativo");
				cellIsFree.await();
			}
	
			if (isOccupied() && !getPlayer().isActive()) { // Encontrou jogador não ativo
				System.out.println("4 - Sou o jogador " + player.getIdentification() + " e o jogador ocupante está inativo. Vou tentar outra célula");
				player.addPlayerToGame(); // Tenta outra posição inicial (recursivamente)
				return;
			}

			setPlayer(player);
			game.notifyChange();
			if (!player.getPlacedAtFirst())
				System.out.println("5 - Sou o jogador " + player.getIdentification() + " e fiquei nas coordenadas "
						+ getPosition().toString());
		} finally {
			lock.unlock();
		}
	}

	// Processa movimento do jogador
	// Método é invocado com a instancia nextCell
	public synchronized void movementPut(Player movingPlayer, Cell currentCell) throws InterruptedException { 
		if (!movingPlayer.isActive()) // Reconfirma jogador vivo
			return;
		if (isOccupied()) { // nextCell está ocupada por outro jogador
			if (this.getPlayer().isActive()) {
				movingPlayer.duel(this.getPlayer()); // getPlayer traz jogador que ocupa a célula que movingPlayer quer
			} else { // Célula ocupada por jogador não vivo
				if(!getPlayer().isHumanPlayer())
				game.goThreadTwoSeconds(movingPlayer);
				try {
					wait();
				} catch (InterruptedException e) {
					if (game.getEndOfGame()) { // Game is over
						Thread.currentThread().interrupt(); // Porque catch fez reset ao Interrupt status
					} else {
						return;
					}
				}
			}
		} else { // nextCell está livre
			currentCell.clear();
			currentCell.releaseAwaitingPlayers();
			this.setPlayer(movingPlayer); // Coloca o movingPlayer no destino (nextCell)
//			System.out.println("Sou o jogador " + player.getIdentification() + " movimentei para " + getPosition().toString() + " e tenho energia " + player.getCurrentStrength());
			if (movingPlayer.isHumanPlayer())
				movingPlayer.setMove(0);
		}

		game.notifyChange();
	}

	// Coloca player a null
	private synchronized void clear() throws InterruptedException {
		setPlayer(null);
	}
	
	// Sinaliza quem está bloqueado em espera por célula disponível (em initialPut())
	public void releaseAwaitingPlayers() throws InterruptedException {
		lock.lock();
		try {
			cellIsFree.signalAll();
		} finally {
			lock.unlock();
		}
	}

}
