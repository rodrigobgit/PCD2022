package game;

import java.util.ArrayList;
import java.util.Observable;
import environment.Cell;
import environment.Coordinate;
/* import gui.GameGuiMain; */
import environment.TwoSecondsWait;

public class Game extends Observable { // Game é o objecto Observado
	public static final int DIMY = 30;
	public static final int DIMX = 30;
	private static final int NUM_BOT_PLAYERS = 50;
	public static final int NUM_FINISHED_PLAYERS_TO_END_GAME=3;
	public static final long REFRESH_INTERVAL = 400;
	public static final double MAX_INITIAL_STRENGTH = 3;
	public static final long MAX_WAITING_TIME_FOR_MOVE = 2000;
	public static final long INITIAL_WAITING_TIME = 10000;
	private LaunchWinnersCount lwc; // LaunchWinnersCount é a thread que lanca um CounterDownLatch
	private WinnersCount wc; // WinnersCount é o CounterDownLatch
	private boolean endOfGame = false;

	public Cell[][] board;
	private ArrayList<Player> arrayPlayerThreads; // ArrayList para as Threads de Jogador
	private ArrayList<TwoSecondsWait> arrayTwoSecondsThreads; // ArrayList para as Threads de espera
	
	public Game() {
		board = new Cell[Game.DIMX][Game.DIMY]; // Atributo board mantém, exclusivamente, a localização dos jogadores
		for (int x = 0; x < Game.DIMX; x++) 
			for (int y = 0; y < Game.DIMY; y++) 
				board[x][y] = new Cell(this, new Coordinate(x, y));
		this.wc = null;
		lwc = new LaunchWinnersCount (this, NUM_FINISHED_PLAYERS_TO_END_GAME);
	}
		
	public void go() {
		arrayPlayerThreads = new ArrayList<>();
		arrayTwoSecondsThreads = new ArrayList<>();
		lwc.start();
			
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
//			System.out.println("Jogador " + botPlayer.getIdentification() + " ficou com a Thread " + botPlayer.getId());
			botPlayer.start();
		}

	}
	
	public Cell getRandomCell() {
		Cell newCell=getCell(new Coordinate((int)(Math.random()*Game.DIMX),(int)(Math.random()*Game.DIMY)));
		return newCell; 
	}
	public Cell[][] getBoard() {
		return board;
	}
	public void setBoard(Cell[][] board) {
		this.board = board;
	}
	public Cell getCell(Coordinate at) {
		return board[at.x][at.y];
	}

	public WinnersCount getWinnersCount() {
		return wc;
	}
	
	public void setWinnersCount(WinnersCount wc) {
		this.wc = wc;
	}

	public boolean getEndOfGame(){
		return endOfGame;
	}
	
	public void notifyChange() {
		setChanged();
		notifyObservers();
	}

	public void gameOver() {
		endOfGame = true;

		for (TwoSecondsWait tsw : arrayTwoSecondsThreads) {
			tsw.interrupt();
//			System.out.println("Thread " + tsw.getId() + " foi interrompida");
		}
		// Retirar no fim (ciclo join() não é necessário)
		for (TwoSecondsWait tsw : arrayTwoSecondsThreads) {
			if (tsw.isAlive()) {
				try {
					tsw.join();
//					System.out.println("Thread " + tsw.getId() + " terminou");

				} catch (InterruptedException e) {
					System.out.println("Thread " + tsw.getId() + " teve problemas no join");
				}
			}
		}
		
		for (Player pl : arrayPlayerThreads) {
			pl.interrupt();
//			System.out.println("Thread " + pl.getId() + " foi interrompida e é do jogador " + pl.getIdentification());
		}
		// Retirar no fim (ciclo join() não é necessário)
		for (Player pl : arrayPlayerThreads) {
			if (pl.isAlive()) {
				try {
					pl.join();
//					System.out.println("Thread " + pl.getId() + " terminou");
				} catch (InterruptedException e1) {
					System.out.println("Thread " + pl.getId() + " teve problemas no join");
				}
			}
		}

		
/*		for (Player pl : arrayPlayerThreads) {
			System.out.println("Thread " + pl.getId() + " está alive? " + pl.isAlive());
		}*/
		
		System.out.println("Game Over!");
	}

	public void goThreadTwoSeconds(Player player) throws InterruptedException {
		TwoSecondsWait tsw = new TwoSecondsWait(player); // Thread para provocar espera de dois segundos
		arrayTwoSecondsThreads.add(tsw);
		tsw.start();
	}
	
}
