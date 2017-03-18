package main.common;

public class Card {
	private final Suit suit;
	private final Value value;
	
	public Card(Suit suit, Value value) {
		this.suit = suit;
		this.value = value;
	}

	public Suit getSuit() {
		return suit;
	}

	public Value getValue() {
		return value;
	}

	@Override
	public String toString() {
		return value.toString() + suit.toString();
	}

	@Override
	public int hashCode() {
		return value.ordinal() * Suit.values().length + suit.ordinal();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof Card)) return false;
		Card that = (Card) obj;
		return this.suit == that.suit && this.value == that.value;
	}
}
