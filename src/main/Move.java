package main;

public class Move {
	private final MoveType moveType;
	private final Card card;

	public Move(MoveType moveType, Card card) {
		super();
		this.moveType = moveType;
		this.card = card;
	}

	public MoveType getMoveType() {
		return moveType;
	}

	public Card getCard() {
		return card;
	}

}
