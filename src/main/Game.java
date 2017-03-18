package main;

import java.util.Arrays;
import java.util.List;

import main.common.Card;
import main.common.CommonInfo;
import main.common.Suit;
import main.common.TurnType;
import main.common.Value;

public class Game {
	private static final Card[] allCards;

	static {
		allCards = new Card[Value.values().length * Suit.values().length];
		int i = 0;
		for (Value value : Value.values())
			for (Suit suit : Suit.values())
				allCards[i++] = new Card(suit, value);
	}

	public static void main(String[] args) {
		HiddenInfo state = new HiddenInfo();
		CommonInfo start = init(state);
		// TODO
	}

	private static CommonInfo init(HiddenInfo state) {

		state.deck.addAll(Arrays.asList(allCards));
		Card deckBottomCard = getNextCard(state.deck);
		// TODO
		CommonInfo result = new CommonInfo(TurnType.Attack, new Card[0], null, null, 24, deckBottomCard);
		return result;
	}

	private static Card getNextCard(List<Card> deck) {
		// TODO Auto-generated method stub
		return null;
	}
}
