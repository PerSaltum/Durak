package main;

public class Move {
	private MoveType moveType = null;
	private Card[] cards;
	
	public Move(int maximumCardsNumber) {
		this.cards = new Card[maximumCardsNumber];
	}
	
	public Card[] getCards() {
		return cards;
	}
	
	public MoveType getMoveType() {
		return moveType;
	}
	
	public void setMoveType(MoveType moveType) {
		this.moveType = moveType;
	}
}
