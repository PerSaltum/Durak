package main;

public class Game {
	private static final Card[] deck;
	
	static {
		deck = new Card[Value.values().length * Suit.values().length];
		int i = 0;
		for (Value value : Value.values())
			for (Suit suit : Suit.values())
				deck[i++] = new Card(suit, value);
	}
}
