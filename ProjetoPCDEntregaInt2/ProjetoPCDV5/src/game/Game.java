package game;

import java.util.ArrayList;
import java.util.Observable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import environment.Cell;
import environment.Coordinate;
import environment.Direction;

public class Game extends Observable { // Game é o objecto Observado
	public static final int DIMY = 30;
	public static final int DIMX = 30;
	private static final int NUM_BOT_PLAYERS = 90;
	private static final int NUM_FINISHED_PLAYERS_TO_END_GAME=3;
	public static final long REFRESH_INTERVAL = 100;
	public static final double MAX_INITIAL_STRENGTH = 3;
	public static final long MAX_WAITING_TIME_FOR_MOVE = 2000;
	public static final long INITIAL_WAITING_TIME = 2000;

	private int numWinners;
	public Cell[][] board;
	private ArrayList<Player> arrayPlayerThreads; // ArrayList para as Threads de Jogador
	private Lock lock = new ReentrantLock();

	
	
	public Game() {
		board = new Cell[Game.DIMX][Game.DIMY]; // Atributo board mantém, exclusivamente, a localização dos jogadores
		for (int x = 0; x < Game.DIMX; x++) 
			for (int y = 0; y < Game.DIMY; y++) 
				board[x][y] = new Cell(this, new Coordinate(x, y));
		this.numWinners = 0;
	}
		
	public void go() {
		arrayPlayerThreads = new ArrayList<>();
	
		// Meter bots em movimento
		for (int i = 0; i < NUM_BOT_PLAYERS; i++) {
			Byte originalStrength = 0;
			int random = (int) (Math.random() * 3);
			switch (random) {
			case 0:
				originalStrength = (byte) 1;
				break;
			case 1:
				originalStrength = (byte) 2;
				break;
			case 2:
				originalStrength = (byte) 3;
				break;
			}

			BotPlayer botPlayer = new BotPlayer(this, i, originalStrength);
			arrayPlayerThreads.add(botPlayer);
			botPlayer.start();
		}
	}
	
	public void addPlayerToGame(Player player) {
		// Random determination of a cell
		Cell initialPos = getCell(new Coordinate((int)(Math.random()*Game.DIMX),(int)(Math.random()*Game.DIMY)));
		// Cell assignmnet to player
		initialPos.initialPut(player);
	}

	public void movePlayer(Player player, Cell actualCell, Direction dir) {
		// Calcula coordenada da proxima cell
		Coordinate atualCoord = actualCell.getPosition();
		Coordinate nextCoord = atualCoord.translate(dir.getVector());

		// Verifica se proxima cell está dentro do board
		if (nextCoord.getX() >= 0 && nextCoord.getY() >= 0 && nextCoord.getX() < Game.DIMX
				&& nextCoord.getY() < Game.DIMY) {
			Cell nextCell = getCell(nextCoord);
			
			lock.lock();
			try {
				if (nextCell.isOcupied()) { //
					if (nextCell.getPlayer().getCurrentStrength() > 0
							&& nextCell.getPlayer().getCurrentStrength() < 10) {
						duel(player, nextCell.getPlayer());
						notifyChange();
					}
					// fazer a imobilizacao de 2 seg aqui

				} else {
					actualCell.clear();
					nextCell.movementPut(player);
					if (player.isHumanPlayer())
						player.setMove(0);

				}
			} finally {
				lock.unlock();
			}
		}
	}

	public void duel(Player movingPlayer, Player occupantPlayer) {
		int winner;
		if (movingPlayer.getCurrentStrength() > occupantPlayer.getCurrentStrength()) { // movingPlayer winns
			winner = 1;
		} else {
			if (movingPlayer.getCurrentStrength() < occupantPlayer.getCurrentStrength()) { // movingPlayer looses
				winner = 2;
			} else { // Same Strength
				int random = (int) Math.random();
				if (random == 1) {
					winner = 1; // movingPlayer winns
				} else { // random = 0
					winner = 2; // movingPlayer looses
				}
			}
		}

		byte newStrength = (byte) (movingPlayer.getCurrentStrength() + occupantPlayer.getCurrentStrength());

		switch (winner) {
		case 1:
			movingPlayer.setCurrentStrength(newStrength);
			occupantPlayer.setCurrentStrength((byte) 0);
			break;
		case 2:
			occupantPlayer.setCurrentStrength(newStrength);
			movingPlayer.setCurrentStrength((byte) 0);
			break;
		}
	}
	
	public Cell getRandomCell() {
		Cell newCell=getCell(new Coordinate((int)(Math.random()*Game.DIMX),(int)(Math.random()*Game.DIMY)));
		return newCell; 
	}
	
	public Cell getCell(Coordinate at) {
		return board[at.x][at.y];
	}
	
	public void notifyChange() {
		setChanged();
		notifyObservers();
	}

	public synchronized void addWinner(int id) {
		numWinners++;
		System.out.println("O jogador " + id + " venceu o jogo!");
		if (numWinners == NUM_FINISHED_PLAYERS_TO_END_GAME) {
			System.out.println("Temos " + numWinners + " vencedores");		
			gameOver(); 
		}
	}

	private void gameOver() {
		// Interrompe todas as Threads
		for (Player pl : arrayPlayerThreads) {
			pl.interrupt();
		}
		
		// Espera que terminem todas as Threads
//		for (Player pl : arrayPlayerThreads) {
//			try {
//				pl.join();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
		System.out.println("Game Over!");
	}
}
