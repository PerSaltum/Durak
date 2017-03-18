package main;

public class Turn {
	private final TurnType turnType;
	private final Card[] deadCards;
	private final Card attacker; // Not null when turnType == Defence
	private final CardFight[] fights;
	private final Card[] yourCards;
	private final int opponentCardsNumber;
	private final int remainingDeckCardsNumber;
	private final Card deckBottomCard;

	public Turn(TurnType turnType, Card[] deadCards, Card attacker, CardFight[] fights, Card[] yourCards,
			int opponentCardsNumber, int remainingDeckCardsNumber, Card deckBottomCard, Suit trumpSuit) {
		this.turnType = turnType;
		this.deadCards = deadCards;
		this.attacker = attacker;
		this.fights = fights;
		this.yourCards = yourCards;
		this.opponentCardsNumber = opponentCardsNumber;
		this.remainingDeckCardsNumber = remainingDeckCardsNumber;
		this.deckBottomCard = deckBottomCard;
	}

	public TurnType getTurnType() {
		return turnType;
	}

	public Card[] getDeadCards() {
		return deadCards;
	}

	public Card getAttacker() {
		return attacker;
	}

	public CardFight[] getFights() {
		return fights;
	}

	public Card[] getYourCards() {
		return yourCards;
	}

	public int getOpponentCardsNumber() {
		return opponentCardsNumber;
	}

	public int getRemainingDeckCardsNumber() {
		return remainingDeckCardsNumber;
	}

	public Card getDeckBottomCard() {
		return deckBottomCard;
	}

	public Suit getTrumpSuit() {
		return deckBottomCard.getSuit();
	}
}
