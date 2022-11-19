package game;

import environment.Direction;

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
			Thread.sleep(Game.INITIAL_WAITING_TIME);
		} catch (InterruptedException e1) {
		}
		while (!isInterrupted() && this.getCurrentStrength() > 0 && this.getCurrentStrength() < 10)
			try {
				sleep(interval * getDebuffMultiplier());

				if (move == 1) {
					movePlayer(this, nextDirection);
				} else {

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
