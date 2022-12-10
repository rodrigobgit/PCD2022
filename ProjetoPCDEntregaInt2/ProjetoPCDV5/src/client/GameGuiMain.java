package client;
//GameGuiMain new

import java.awt.event.KeyEvent; // New
import java.awt.event.KeyListener; // New
import java.util.Observable;
import java.util.Observer;


import javax.swing.JFrame;

@SuppressWarnings("deprecation")
public class GameGuiMain implements Observer { // GameGuiMain contém instâncias Observador
	private JFrame frame = new JFrame("pcd.io");
	private BoardJComponent boardGui;
	private Game game;

	public GameGuiMain(Game game) {
		super();
		this.game=game;
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
		
		
	}

	@Override
	public void update(Observable o, Object arg) {
		boardGui.repaint();
	}
	public BoardJComponent getBoardGui() {
		return boardGui;
	}

	

}
