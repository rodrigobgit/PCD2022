package game;

import environment.Cell;
import environment.Coordinate;

/**
 * Represents a player.
 * @author luismota
 *
 */
public abstract class Player extends Thread  {
	protected  Game game;
	private int id;
	private byte originalStrength;
	private byte currentStrength;
	private byte debuffMultiplier;
	private int move;

	public Player(Game game, int id, byte originalStrength) {
		this.game=game;
		this.id = id;
		this.originalStrength=originalStrength;
		this.currentStrength = originalStrength;
		this.debuffMultiplier = originalStrength;
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

	public void setCurrentStrength(byte newStrength) {
		if (newStrength > 10) newStrength = 10;
		currentStrength = newStrength;
		if (newStrength == 10) game.addWinner(id);
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

	public void setMove(int move) {
		this.move=move;
	}

	public int getMove() {
		return move;
	}

}
