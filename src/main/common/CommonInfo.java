package main.common;

public class CommonInfo {

	private final TurnType turnType;
	private final Card[] deadCards;
	private final Card attacker; // Not null when turnType == Defence
	private final CardFight[] fights;
	private final int remainingDeckCardsNumber;
	private final Card deckBottomCard;

	public CommonInfo(TurnType turnType, Card[] deadCards, Card attacker, CardFight[] fights,
			int remainingDeckCardsNumber, Card deckBottomCard) {
		super();
		this.turnType = turnType;
		this.deadCards = deadCards;
		this.attacker = attacker;
		this.fights = fights;
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
