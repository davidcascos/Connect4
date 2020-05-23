package com.dcascos.connect4.logic;

public class Position {

	private final int row;
	private final int column;

	public Position(int row, int column) {
		this.row = row;
		this.column = column;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public Position move(Direction direction) {
		return new Position(row + direction.getChangeInRow(), column + direction.getChangeInColumn());
	}

	private boolean isEqualTo(Position other) {
		return other != null && other.row == row && other.column == column;
	}

	public static int pathLength(Position pos1, Position pos2) {
		if (pos1.isEqualTo(pos2)) {
			return 1;
		} else if (pos1.row == pos2.row) {
			return (Math.abs(pos1.column - pos2.column)) + 1;
		} else if (pos1.column == pos2.column) {
			return (Math.abs(pos1.row - pos2.row)) + 1;
		} else {
			return Math.max(pos1.row, pos2.row) - Math.min(pos1.row, pos2.row) + 1;
		}
	}
}