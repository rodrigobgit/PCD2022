package game;

import environment.Direction;

public class BotPlayer extends Player {
	private long interval = Game.REFRESH_INTERVAL;

	public BotPlayer(Game game, int id, byte originalStrength) {
		super(game, id, originalStrength);
	}
	
	public void run() {
		try {
			addPlayerToGame();
		} catch (InterruptedException e1) {
			System.out.println("O jogador " + this.getIdentification() + " nao chegou a entrar em jogo");
			return;
		}
		if (placedAtFirst) { // Jogador foi colocado no board à primeira tentativa
			try {
				Thread.sleep(Game.INITIAL_WAITING_TIME);
			} catch (InterruptedException e2) {
//				System.out.println("e2 para o jogador " + this.getIdentification() + " na Thread " + Thread.currentThread().getId()) ;
				return;
			}
		}

		while (!isInterrupted() && this.getCurrentStrength() > 0 && this.getCurrentStrength() < 10) {
			try {
				sleep(interval * getDebuffMultiplier());
				rollDice();
			} catch (InterruptedException e3) {
//				System.out.println("e3 para o jogador " + this.getIdentification() + " na Thread " + Thread.currentThread().getId());
				return;
			}
		}
	}

	public void rollDice() throws InterruptedException {
		// Escolha da direcao para movimento
		double random=Math.random();
		Direction nextDirection = null;
		
		if (random<0.25) nextDirection=environment.Direction.UP;
		if (random<0.50 && random>=0.25) nextDirection=environment.Direction.DOWN;
		if (random<0.75 && random>=0.5)	nextDirection=environment.Direction.LEFT;
		if (random>=0.75) nextDirection=environment.Direction.RIGHT;
		movePlayer(this, nextDirection);	
	}
	
	public boolean isHumanPlayer() {
		return false;
	}
}
