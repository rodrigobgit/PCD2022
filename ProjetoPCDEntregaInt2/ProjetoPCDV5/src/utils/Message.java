package utils;

import java.io.Serializable;

import environment.Cell;


public class Message implements Serializable {		
	public final transient Cell[][]board;
	
	
	public Message(Cell[][] board) {
		this.board=board;
	
	}
	public Cell[][] getBoard() {
		return board;
	}
	
	
}