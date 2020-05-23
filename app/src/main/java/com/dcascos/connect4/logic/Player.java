package com.dcascos.connect4.logic;

public class Player {

	private static final char PLAYER1 = '1';
	private static final char PLAYER2 = '2';

	private final char id;

	private Player(char id) {
		this.id = id;
	}

	public static Player player1() {
		return new Player(PLAYER1);
	}

	public static Player player2() {
		return new Player(PLAYER2);
	}

	public boolean isPlayer1() {
		return id == PLAYER1;
	}

	public char getId() {
		return id;
	}
}
