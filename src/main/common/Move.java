package main.common;

public class Move {
	private final MoveType moveType;
	private final Card card; // maybe null

	private Move(MoveType moveType, Card card) {
		super();
		this.moveType = moveType;
		this.card = card;
	}
	
	private static final Move finishMove = new Move(MoveType.Finish, null);
	
	public static Move createFinishMove() {
		return finishMove;
	}
	
	public static Move createUseCardMove(Card card) {
		return new Move(MoveType.UseCard, card);
	}

	public MoveType getMoveType() {
		return moveType;
	}

	public Card getCard() {
		return card;
	}

	@Override
	public int hashCode() {
		if (card == null)
			return 0;
		return card.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof Move)) return false;
		Move that = (Move) obj;
		
		if ((this.card == null) ^ (that.card == null))
			return false;
		
		if (this.moveType != that.moveType)
			return false;
		
		if (this.card != null) {
			assert(that.card != null);
			return this.card.equals(that.card);
		}
		
		return true;
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
