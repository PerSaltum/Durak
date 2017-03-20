package main.common;

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

	@Override
	public int hashCode() {
		return card.hashCode() * 10 + moveType.ordinal();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof Move)) return false;
		Move that = (Move) obj;
		return this.card.equals(that.card) && this.moveType == that.moveType;
	}
	
	@Override
	public String toString() {
		switch (moveType) {
		case Finish:
			return "finish";
		case UseCard:
			return card.toString();
		}
		
		throw new UnsupportedOperationException("Unsupported move type");
	}
}
