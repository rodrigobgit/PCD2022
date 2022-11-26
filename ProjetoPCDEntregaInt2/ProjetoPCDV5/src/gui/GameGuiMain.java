package gui;
//GameGuiMain new

import java.awt.event.KeyEvent; // New
import java.awt.event.KeyListener; // New
import java.util.Observable;
import java.util.Observer;
import game.Game;
import game.HumanPlayer;
import javax.swing.JFrame;

@SuppressWarnings("deprecation")
public class GameGuiMain implements Observer { // GameGuiMain contém instâncias Observador
	private JFrame frame = new JFrame("pcd.io");
	private BoardJComponent boardGui;
	private Game game;
//	private ArrayList<BotPlayer> arrayBotThreads; // ArrayList para as Threads Bot

	public GameGuiMain() {
		super();
		game = new Game();
		game.addObserver(this);
		buildGui();
	}

	private void buildGui() {
		boardGui = new BoardJComponent(game);
		frame.add(boardGui);
		frame.setSize(800,800);
		frame.setLocation(0, 0);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void init() {
		frame.setVisible(true);
		game.go();
		
		//humano de teste
		HumanPlayer humanPlayer = new HumanPlayer(game, 90, (byte) 3);		
		humanPlayer.start();		
		
		// Fica a dar scan às teclas do jogador humano
		while (true) {
			KeyEvent e = new KeyEvent(boardGui, 1, 20, 1, 10, 'a');
			boardGui.keyPressed(e);
			// se houve uma tecla clicada,o humano move-se
			if (boardGui.getLastPressedDirection() != null) {
				humanPlayer.setNextDirection(boardGui.getLastPressedDirection());
				boardGui.clearLastPressedDirection();
			}
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		boardGui.repaint();
	}

/*	public static void gameOver(Game game) {
		// Interrompe todas as Threads
		for (Player pl : game.getArrayPlayerThreads()) {
			pl.interrupt();
			System.out.println("Thread " + pl.getId() + " foi interrompida e é do jogador " + pl.getIdentification());
		}
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
						
		for (Player pl : game.getArrayPlayerThreads()) {
			try {
				pl.join();
				System.out.println("Thread " + pl.getId() + " terminou");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				System.out.println("Thread " + pl.getId() + " teve problemas no join");
				e.printStackTrace();
			}
		}
		System.out.println("Game Over!");
	} */
	
	public static void main(String[] args) {
		GameGuiMain game = new GameGuiMain();
		game.init();
	}

}
