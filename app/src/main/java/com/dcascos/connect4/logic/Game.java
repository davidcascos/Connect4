package com.dcascos.connect4.logic;

import java.io.Serializable;

public class Game implements Serializable {
	private static final int TOWIN = 4;

	private final Board board;
	private Status status;

	public Game(int size) {
		this.board = new Board(size);
		this.status = Status.PLAYER1_PLAYS;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Position playOpponent() {
		int size = board.getSize();
		Position position = drop((int) (Math.random() * size));
		while (position == null) {
			position = drop((int) (Math.random() * size));
		}
		return position;
	}

	private void toggleTurn() {
		Player actualPlayer = (status.equals(Status.PLAYER1_PLAYS)) ? Player.player1() : Player.player2();
		status = (actualPlayer.isPlayer1()) ? Status.PLAYER2_PLAYS : Status.PLAYER1_PLAYS;
	}

	public boolean checkForFinish() {
		return status.equals(Status.PLAYER1_WINS) || status.equals(Status.PLAYER2_WINS) || status.equals(Status.DRAW) || status.equals(Status.ALL_PLAYER_LOSE);
	}

	public Position drop(int column) {
		if (board.firstEmptyRow(column) == -1) {
			return null;
		} else {
			Player actualPlayer = (status.equals(Status.PLAYER1_PLAYS)) ? Player.player1() : Player.player2();
			Position actualPosition = board.occupyCell(column, actualPlayer);

			if (!board.hasValidMoves()) {
				status = Status.DRAW;
			} else if (board.maxConnected(actualPosition) >= TOWIN) {
				status = (actualPlayer.isPlayer1()) ? Status.PLAYER1_WINS : Status.PLAYER2_WINS;
			} else {
				toggleTurn();
			}
			return actualPosition;
		}
	}
}