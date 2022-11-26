package game;

public class WinnersCount {
	private int wtgo;
	private Game game;
	private int winnerNumber = 0;

	public WinnersCount(Game game, int winnersToGameOver) {
		this.wtgo = winnersToGameOver;
		game.setWinnersCount(this);
	}

	public synchronized void await() throws InterruptedException {
		while (wtgo > 0) {
			wait();
		}
	}

	public synchronized void countDown(int id) {
		System.out.println("O jogador " + id + " foi o vencedor numero " + ++winnerNumber +"!");
		wtgo--;
		if (wtgo == 0)
			notifyAll();
	}
}
