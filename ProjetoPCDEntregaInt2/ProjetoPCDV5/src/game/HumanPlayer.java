package game;

import environment.Cell;
import environment.Coordinate;
import environment.Direction;

/**
 * Class to demonstrate a player being added to the game.
 * @author luismota
 *
 */
public class HumanPlayer extends Player {
	private Direction nextDirection;
	private int move;
	private long interval=Game.REFRESH_INTERVAL;
	public HumanPlayer(Game game, int id, byte originalStrength) {
		super(game, id, originalStrength);
	}
	
	public void run() {
		game.addPlayerToGame(this);
		try {
			Thread.sleep(game.INITIAL_WAITING_TIME);
		} catch (InterruptedException e1) {}
		while(!isInterrupted() && this.getCurrentStrength() > 0 && this.getCurrentStrength() < 10)
			try {
				sleep(interval*getDebuffMultiplier());
				
				if(move==1) {
					
					movePlayer(nextDirection, getCurrentCell());
				}
				else {
					
				}
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			
	}
	
	public void setNextDirection(Direction direction) {
		this.nextDirection=direction;
		setMove(1);
	}
	public void setMove(int move) {
		this.move = move;
	}
	public int getMove() {
		return move;
	}
	
	
	public boolean isHumanPlayer() {
		return true;
	}
}
