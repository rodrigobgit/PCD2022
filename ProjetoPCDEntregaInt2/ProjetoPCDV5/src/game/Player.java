package game;

import environment.Cell;
import environment.Coordinate;
import environment.Direction;

/**
 * Represents a player.
 * @author luismota
 *
 */
public abstract class Player extends Thread  {
	protected  Game game;
	private int id;
	private byte originalStrength;
	protected boolean placedAtFirst;
	private byte currentStrength;
	private byte debuffMultiplier;
	private int move;

	public Player(Game game, int id, byte originalStrength) {
		this.game=game;
		this.id = id;
		this.originalStrength=originalStrength;
		this.placedAtFirst = true; // Por defeito, assume-se que jogador será colocado a primeira tentativa
		currentStrength = originalStrength;
		debuffMultiplier = originalStrength;
	}

	public abstract boolean isHumanPlayer();
	
	@Override
	public String toString() {
		return "Player [id=" + id + ", currentStrength=" + currentStrength + ", getCurrentCell()=" + getCurrentCell()
		+ "]";
	}

/*	@Override // Veio do código base. Para que serve?
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}*/ 

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public byte getCurrentStrength() {
		return currentStrength;
	}

	public void setCurrentStrength(byte newStrength) throws InterruptedException {
		if (newStrength > 10) newStrength = 10;
		currentStrength = newStrength;
		game.notifyChange();
		if (newStrength == 10)
			addWinner(id);
	}

	public int getIdentification() {
		return id;
	}
	
	public Cell getCurrentCell() {
		Coordinate coord;
		for(int x = 0; x < Game.DIMX; x++) {
			for(int y = 0; y < Game.DIMY; y++) {
				coord = new Coordinate(x, y);
				Cell cell = game.getCell(coord);
				if(cell.getPlayer() == this) {
					return cell;
				}
			}
		}
		System.out.println("O jogador " + this.getIdentification() + " vai ter célula null!!!");
		return null;
	}

	public int getDebuffMultiplier() {
		return debuffMultiplier;
	}

	public boolean getPlacedAtFirst() {
		return placedAtFirst;
	}
	
	public void setNotPlacedAtFirst() throws InterruptedException {
		placedAtFirst = false;
	}
	
	public void setMove(int move) throws InterruptedException {
		this.move=move;
	}

	public int getMove() {
		return move;
	}

	public void addPlayerToGame() throws InterruptedException {
		// Random determination of a cell
		Cell initialPos = game.getCell(new Coordinate((int)(Math.random()*Game.DIMX),(int)(Math.random()*Game.DIMY)));
		// Cell assignmnet to player
		initialPos.initialPut(this);
	}
	
	public void movePlayer(Player player, Direction dir) throws InterruptedException {
		// Calcula celula actual do Player
		Cell currentCell = getCurrentCell();

		// Calcula coordenada da proxima cell
		Coordinate currentCoord = currentCell.getPosition();
		Coordinate nextCoord = currentCoord.translate(dir.getVector());
		
		// Verifica se proxima cell está dentro do board
		if (nextCoord.getX() >= 0 && nextCoord.getY() >= 0 && nextCoord.getX() < Game.DIMX
				&& nextCoord.getY() < Game.DIMY) {
			Cell nextCell = game.getCell(nextCoord);
			nextCell.movementPut(player, currentCell);
		}
	}
	
	public  void duel(Player occupantPlayer) throws InterruptedException { // Metodo é instanciado pelo movingPlayer (this)
		int winner;
		if (this.getCurrentStrength() > occupantPlayer.getCurrentStrength()) { // movingPlayer winns
			winner = 1;
		} else {
			if (this.getCurrentStrength() < occupantPlayer.getCurrentStrength()) { // movingPlayer looses
				winner = 2;
			} else { // Same Strength
				int random = (int) Math.random();
				if (random == 1) {
					winner = 1; // movingPlayer winns
				} else { // random = 0
					winner = 2; // movingPlayer looses
				}
			}
		}

		byte newStrength = (byte) (this.getCurrentStrength() + occupantPlayer.getCurrentStrength());

		switch (winner) {
		case 1:
			this.setCurrentStrength(newStrength);
			occupantPlayer.setCurrentStrength((byte) 0);
			break;
		case 2:
			occupantPlayer.setCurrentStrength(newStrength);
			this.setCurrentStrength((byte) 0);
			break;
		}
	}
	
		public void addWinner(int id) {
			int numWinners = game.getNumWinnwers();
			game.setNumWinnwers(++numWinners);
			System.out.println("O jogador " + id + " foi o vencedor numero " + numWinners +"!");
			if (numWinners == Game.NUM_FINISHED_PLAYERS_TO_END_GAME)
				game.gameOver();
		}

}
