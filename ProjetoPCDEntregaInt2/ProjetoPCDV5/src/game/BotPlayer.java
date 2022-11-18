package game;

import java.util.function.ToDoubleBiFunction;

import environment.Cell;
import environment.Coordinate;
import environment.Direction;

public class BotPlayer extends Player {
	private long interval = Game.REFRESH_INTERVAL;


	public BotPlayer(Game game, int id, byte originalStrength) {
		super(game, id, originalStrength);

	}
	
	public void run() {
		game.addPlayerToGame(this);
		try {
			Thread.sleep(game.INITIAL_WAITING_TIME);
		} catch (InterruptedException e1) {}

		while (!isInterrupted() && this.getCurrentStrength() > 0 && this.getCurrentStrength() < 10) {
			try {
				sleep(interval * getDebuffMultiplier());
				rollDice();
			} catch (InterruptedException e) {
				return;
			}
		}
		
	}
	@Override
	public void rollDice() {
		// Escolha da direcao para movimento
		double random=Math.random();
		Direction direction = null;
		
		if (random<0.25) direction=environment.Direction.UP;
		if (random<0.50 && random>=0.25) direction=environment.Direction.DOWN;
		if (random<0.75 && random>=0.5)	direction=environment.Direction.LEFT;
		if (random>=0.75) direction=environment.Direction.RIGHT;
		
//		System.out.println(getCurrentCell());
		
		movePlayer(direction, getCurrentCell());
	}
	
	
	public boolean isHumanPlayer() {
		return false;
	}
}