package game;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
	private Cell currentCell;
	private int id;
	private byte originalStrength;
	private byte currentStrength;
	private byte debuffMultiplier;
	private Lock lock = new ReentrantLock();
	private int move;

	public Player(Game game, int id, byte originalStrength) {
		this.game=game;
		this.id = id;
		this.originalStrength=originalStrength;
		this.currentStrength = originalStrength;
		this.debuffMultiplier = originalStrength;
//		this.currentCell = null;
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

	public void setCurrentStrength(Player player, byte newStrength) {
		if (newStrength > 10) newStrength = 10;
		player.currentStrength = newStrength;
		if (newStrength == 10) game.addWinner(id);
	}

	public int getIdentification() {
		return id;
	}
	
	/* Método anterior (em que currentCell era atributo de Player)
	public Cell getCurrentCell() {
		return currentCell;
	}*/
	
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

	public void setCurrentCell(Cell cell) {
		this.currentCell = cell;
	}

	public int getDebuffMultiplier() {
		return debuffMultiplier;
	}

	public void movePlayer(Direction dir, Cell cell) {
		// Calcula coordenada da proxima cell
		Coordinate atualCoord = cell.getPosition();
		Coordinate nextCoord = atualCoord.translate(dir.getVector());
		// Verifica se está dentro do board
		if (nextCoord.getX() >= 0 && nextCoord.getY() >= 0 && nextCoord.getX() < Game.DIMX
				&& nextCoord.getY() < Game.DIMY) {
			
			Cell nextCell = game.getCell(nextCoord);

			// todo o codigo ate ao else rollDice deverá estar no metodo movementPut da
			// classe Cell
			// nextCell.movementPut(this);

			if (nextCell.isOcupied()) { //
				if (nextCell.getPlayer().getCurrentStrength() > 0 && nextCell.getPlayer().getCurrentStrength() < 10) {
					duel(this, nextCell.getPlayer());
					game.notifyChange();
				}
				// fazer a imobilizacao de 2 seg aqui

			} else {
				lock.lock();	// este lock é para assegurar que as duas operações seguintes são para a mesma Thread
				cell.clear();
				nextCell.movementPut(this);
				if (isHumanPlayer()) setMove(0);
				lock.unlock();
			}

		// rollDice não pode ser invocado porque é o método que invoca este (causa um loop) - era um dos erros	
		} /* else {
			if (!isHumanPlayer())
				rollDice();

		} */
	}
	
	public void duel(Player movingPlayer, Player occupantPlayer) {
		int winner;
		if (movingPlayer.getCurrentStrength() > occupantPlayer.getCurrentStrength()) { // movingPlayer winns
			winner = 1;
		} else {
			if (movingPlayer.getCurrentStrength() < occupantPlayer.getCurrentStrength()) { // movingPlayer looses
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
		
		byte newStrength = (byte) (movingPlayer.getCurrentStrength() + occupantPlayer.getCurrentStrength());

		switch (winner) {
		case 1:
			setCurrentStrength(movingPlayer, newStrength);
			setCurrentStrength(occupantPlayer, (byte) 0);
//			System.out.println("Moving player ganhou conflito");
			break;
		case 2:
			setCurrentStrength(occupantPlayer, newStrength);
			setCurrentStrength(movingPlayer, (byte) 0);
//			System.out.println("Moving player perdeu conflito");
			break;
		}
	}
	public void rollDice() {
		
	}
	public void setMove(int move) {
		this.move=move;
	}
	public int getMove() {
		return move;
	}
	
	
}
