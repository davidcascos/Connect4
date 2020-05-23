package com.dcascos.connect4.logic;

public class Cell {

	private final char state;

	public Cell(Player player) {
		this.state = player.getId();
	}

	public boolean isEqualTo(Cell other) {
		return other != null && other.state == state;
	}
}