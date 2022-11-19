package game;

import environment.Direction;

public class BotPlayer extends Player {
	private long interval = Game.REFRESH_INTERVAL;


	public BotPlayer(Game game, int id, byte originalStrength) {
		super(game, id, originalStrength);

	}
	
	public void run() {
		game.addPlayerToGame(this);
		try {
			Thread.sleep(Game.INITIAL_WAITING_TIME);
		} catch (InterruptedException e) {}

		while (!isInterrupted() && this.getCurrentStrength() > 0 && this.getCurrentStrength() < 10) {
			try {
				sleep(interval * getDebuffMultiplier());
				rollDice();
			} catch (InterruptedException e) {
				return;
			}
		}
	}

	public void rollDice() {
		// Escolha da direcao para movimento
		double random=Math.random();
		Direction direction = null;
		
		if (random<0.25) direction=environment.Direction.UP;
		if (random<0.50 && random>=0.25) direction=environment.Direction.DOWN;
		if (random<0.75 && random>=0.5)	direction=environment.Direction.LEFT;
		if (random>=0.75) direction=environment.Direction.RIGHT;

		game.movePlayer(this,getCurrentCell(), direction);
	}
	
	public boolean isHumanPlayer() {
		return false;
	}
}