package main.common;

import java.util.List;
import java.util.Set;

public class CommonInfo {

	private final TurnType turnType;
	private final Set<Card> deadCards;
	private final Card attacker; // Not null when turnType == Defence
	private final List<CardFight> fights;
	private final int remainingDeckCardsNumber;
	private final Card deckBottomCard;

	public CommonInfo(TurnType turnType, Set<Card> deadCards, Card attacker, List<CardFight> fights,
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

	public Set<Card> getDeadCards() {
		return deadCards;
	}

	public Card getAttacker() {
		return attacker;
	}

	public List<CardFight> getFights() {
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
