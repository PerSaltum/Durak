package main;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import main.common.Card;
import main.common.CommonInfo;
import main.common.Suit;
import main.common.Turn;
import main.common.TurnType;
import main.common.Value;

public class Game {
	private static final Card[] allCards;
	private static final Random random = new Random();

	static {
		allCards = new Card[Value.values().length * Suit.values().length];
		int i = 0;
		for (Value value : Value.values())
			for (Suit suit : Suit.values())
				allCards[i++] = new Card(suit, value);
	}

	public static void main(String[] args) {
		HiddenInfo state = new HiddenInfo();
		CommonInfo commonInfo = init(state);
		while (isGameOver(state, commonInfo))
			makeMove(state, commonInfo);
		printResult(state, commonInfo);
	}

	private static CommonInfo init(HiddenInfo state) {
		initDeck(state.deck);
		Card deckBottomCard = state.deck.get(0);
		for (int i = 0; i < 6; i++) {
			state.firstHand.add(getNextCard(state.deck));
			state.secondHand.add(getNextCard(state.deck));
		}
		CommonInfo result = new CommonInfo(TurnType.Attack, null, null, null, 24, deckBottomCard);
		return result;
	}

	private static void initDeck(List<Card> deck) {
		List<Card> tmp = new LinkedList<>(Arrays.asList(allCards));
		while (!tmp.isEmpty()) {
			int num = random.nextInt(tmp.size());
			deck.add(tmp.remove(num));
		}
	}

	private static Card getNextCard(List<Card> deck) {
		return deck.remove(deck.size() - 1);
	}

	private static boolean isGameOver(HiddenInfo state, CommonInfo commonInfo) {
		if (state.deck.size() > 0)
			return false;

		if (state.firstHand.size() > 0 && state.secondHand.size() > 0)
			return false;

		if (commonInfo.getTurnType().equals(TurnType.Defence))
			return false;
		return true;
	}

	private static void makeMove(HiddenInfo state, CommonInfo commonInfo) {
		switch (commonInfo.getTurnType()) {
		case Attack:
			makeAttackMove(state, commonInfo);
			break;
		case AfterAttack:
			makeAfterAttackMove(state, commonInfo);
			break;
		case Defence:
			makeDefenceMove(state, commonInfo);
			break;
		default:
			assert false;
			break;
		}
	}

	private static void makeAttackMove(HiddenInfo state, CommonInfo commonInfo) {
		int opponentCardsNumber;
		Set<Card> yourCards;
		if (state.isFirstAttack) {
			yourCards = Collections.unmodifiableSet(state.firstHand);
			opponentCardsNumber = state.secondHand.size();
		} else {
			yourCards = Collections.unmodifiableSet(state.secondHand);
			opponentCardsNumber = state.firstHand.size();
		}
		Turn turn = new Turn(commonInfo, yourCards, opponentCardsNumber);
		// TODO Auto-generated method stub

	}

	private static void makeAfterAttackMove(HiddenInfo state, CommonInfo commonInfo) {
		// TODO Auto-generated method stub

	}

	private static void makeDefenceMove(HiddenInfo state, CommonInfo commonInfo) {
		// TODO Auto-generated method stub

	}

	private static void printResult(HiddenInfo state, CommonInfo commonInfo) {
		// TODO Auto-generated method stub

	}

}
