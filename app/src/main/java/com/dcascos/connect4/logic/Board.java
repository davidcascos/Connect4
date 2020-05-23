package com.dcascos.connect4.logic;

public class Board {

	private final int size;
	private final Cell[][] cells;

	Board(int size) {
		this.size = size;
		this.cells = new Cell[size][size];
	}

	public int getSize() {
		return size;
	}

	Position occupyCell(int column, Player player) {
		if (canPlayColumn(column)) {
			int row = firstEmptyRow(column);
			cells[row][column] = new Cell(player);
			return new Position(row, column);
		}
		return null;
	}

	boolean hasValidMoves() {
		for (int i = 0; i < size; i++) {
			if (canPlayColumn(i)) {
				return true;
			}
		}
		return false;
	}

	public int firstEmptyRow(int column) {
		if (canPlayColumn(column)) {
			for (int i = cells.length - 1; i >= 0; i--) {
				if (cells[i][column] == null) {
					return i;
				}
			}
		}
		return -1;
	}

	private boolean canPlayColumn(int column) {
		return column >= 0 && column < size && cells[0][column] == null;
	}


	public int maxConnected(Position position) {
		int maxCoincidence = 0;
		int totalCoincidence;
		Cell currentCell = cells[position.getRow()][position.getColumn()];

		for (int i = 0; i < Direction.ALL.length; i++) {
			totalCoincidence = countCoincidence(position, currentCell, Direction.ALL[i]) + countCoincidence(position, currentCell, Direction.ALL[i].invert()) - 1;

			if (totalCoincidence > maxCoincidence) {
				maxCoincidence = totalCoincidence;
			}
		}
		return maxCoincidence;
	}

	private int countCoincidence(Position mainPosition, Cell cell, Direction newDirection) {
		Position newPosition = mainPosition;
		while (newPosition.getRow() >= 0
				&& newPosition.getRow() < cells.length
				&& newPosition.getColumn() >= 0
				&& newPosition.getColumn() < cells[0].length
				&& cell.isEqualTo(cells[newPosition.getRow()][newPosition.getColumn()])) {
			newPosition = newPosition.move(newDirection);
		}
		return Position.pathLength(mainPosition, newPosition.move(newDirection.invert()));
	}
}