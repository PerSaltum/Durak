package main.common;

import java.util.Set;

public class Turn {
	private final CommonInfo commonInfo;
	private final Set<Card> yourCards;
	private final int opponentCardsNumber;

	public Turn(CommonInfo commonInfo, Set<Card> yourCards, int opponentCardsNumber) {
		super();
		this.commonInfo = commonInfo;
		this.yourCards = yourCards;
		this.opponentCardsNumber = opponentCardsNumber;
	}

	public CommonInfo getCommonInfo() {
		return commonInfo;
	}

	public Set<Card> getYourCards() {
		return yourCards;
	}

	public int getOpponentCardsNumber() {
		return opponentCardsNumber;
	}

}
