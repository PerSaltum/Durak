package main.common;

public class Turn {
	private final CommonInfo commonInfo;
	private final Card[] yourCards;
	private final int opponentCardsNumber;

	public Turn(CommonInfo commonInfo, Card[] yourCards, int opponentCardsNumber) {
		super();
		this.commonInfo = commonInfo;
		this.yourCards = yourCards;
		this.opponentCardsNumber = opponentCardsNumber;
	}

	public CommonInfo getCommonInfo() {
		return commonInfo;
	}

	public Card[] getYourCards() {
		return yourCards;
	}

	public int getOpponentCardsNumber() {
		return opponentCardsNumber;
	}

}
