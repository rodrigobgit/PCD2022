package utils;

import java.io.Serializable;

import environment.Cell;


public class Message implements Serializable {		
	public final transient Cell[][]board;
	public final int isOver;
	
	public Message(Cell[][] board,int isOver) {
		this.board=board;
		this.isOver=isOver;
	
	}
	public Cell[][] getBoard() {
		return board;
	}
	
	
}