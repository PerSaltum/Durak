package main.common;

public enum Suit {
	Hearts { @Override public String toString() { return "♥"; } },
	Spades { @Override public String toString() { return "♠"; } },
	Diamonds { @Override public String toString() { return "♦"; } },
	Clubs { @Override public String toString() { return "♣"; } }
}
