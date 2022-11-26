package game;

import java.util.ArrayList;
import java.util.Observable;
import environment.Cell;
import environment.Coordinate;
/* import gui.GameGuiMain; */

public class Game extends Observable { // Game é o objecto Observado
	public static final int DIMY = 30;
	public static final int DIMX = 30;
	private static final int NUM_BOT_PLAYERS = 90;
	public static final int NUM_FINISHED_PLAYERS_TO_END_GAME=3;
	public static final long REFRESH_INTERVAL = 100;
	public static final double MAX_INITIAL_STRENGTH = 3;
	public static final long MAX_WAITING_TIME_FOR_MOVE = 2000;
	public static final long INITIAL_WAITING_TIME = 10000;

	private int numWinners;
	public Cell[][] board;
	private ArrayList<Player> arrayPlayerThreads; // ArrayList para as Threads de Jogador
	
	public Game() {
		board = new Cell[Game.DIMX][Game.DIMY]; // Atributo board mantém, exclusivamente, a localização dos jogadores
		for (int x = 0; x < Game.DIMX; x++) 
			for (int y = 0; y < Game.DIMY; y++) 
				board[x][y] = new Cell(this, new Coordinate(x, y));
		this.numWinners = 0;
/*		arrayPlayerThreads = new ArrayList<>(); */
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
//			System.out.println("Jogador " + botPlayer.getIdentification() + " ficou com a Thread " + botPlayer.getId());
			botPlayer.start();
		}
	}
	
	public Cell getRandomCell() {
		Cell newCell=getCell(new Coordinate((int)(Math.random()*Game.DIMX),(int)(Math.random()*Game.DIMY)));
		return newCell; 
	}
	
	public Cell getCell(Coordinate at) {
		return board[at.x][at.y];
	}

	public int getNumWinnwers() {
		return numWinners;
	}

	public void setNumWinnwers(int numWinners) {
		this.numWinners = numWinners;
	}
	
	
	public void notifyChange() {
		setChanged();
		notifyObservers();
	}

/*	public synchronized void addWinner(int id) throws InterruptedException { */
/*	public synchronized void addWinner(int id) {
		numWinners++;
		System.out.println("O jogador " + id + " venceu o jogo!");
		if (numWinners == NUM_FINISHED_PLAYERS_TO_END_GAME) {
			System.out.println("Já há " + numWinners + " vencedores");		
			gameOver();
		}
	}*/

	public void gameOver() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		for (Player pl : arrayPlayerThreads) {
			pl.interrupt();
			System.out.println("Thread " + pl.getId() + " foi interrompida e é do jogador " + pl.getIdentification());
		}

		for (Player pl : arrayPlayerThreads) {
			if (!pl.isAlive()) {
				try {
					pl.join();
					System.out.println("Thread " + pl.getId() + " terminou");
				} catch (InterruptedException e) {
					System.out.println("Thread " + pl.getId() + " teve problemas no join");
					/*
					 * if (!pl.isInterrupted()) { System.out.println("Thread " + pl.getId() +
					 * " afinal não estava interrompida"); } if (pl.isInterrupted()) {
					 * System.out.println("Thread " + pl.getId() + " estava interrompida"); }
					 * 
					 * e.printStackTrace();
					 */
				}
			}
		}

		for (Player pl : arrayPlayerThreads) {
			System.out.println("Thread " + pl.getId() + " está alive? " + pl.isAlive());
		}
		System.out.println("Game Over!");
	}
	
/*	public ArrayList<Player> getArrayPlayerThreads() {
		return arrayPlayerThreads;
	} */
}