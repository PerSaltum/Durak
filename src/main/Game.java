package main;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import main.common.Card;
import main.common.CardFight;
import main.common.CommonInfo;
import main.common.Move;
import main.common.MoveType;
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
			commonInfo = makeMove(state, commonInfo);
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

	private static CommonInfo makeMove(HiddenInfo state, CommonInfo commonInfo) {
		switch (commonInfo.getTurnType()) {
		case Attack:
			return makeAttackMove(state, commonInfo);
		case AfterAttack:
			return makeAfterAttackMove(state, commonInfo);
		case Defence:
			return makeDefenceMove(state, commonInfo);
		}
		return null;
	}

	private static CommonInfo makeAttackMove(HiddenInfo state, CommonInfo commonInfo) {
		Move move = callAttackerMove(state, commonInfo);
		if (move.getMoveType().equals(MoveType.Finish)) {
			if (commonInfo.getFights() == null || commonInfo.getFights().isEmpty())
				throw new IllegalStateException("Finish move when no fights");

			// Fights to dead
			Set<Card> newDead = new LinkedHashSet<>(commonInfo.getDeadCards());
			for (CardFight cardFight : commonInfo.getFights()) {
				newDead.add(cardFight.getAttacker());
				newDead.add(cardFight.getDefender());
			}

			// give cards to player, defender last
			Set<Card> firstToGive;
			Set<Card> secondToGive;
			if (state.isFirstPlayerAttack) {
				firstToGive = state.firstHand;
				secondToGive = state.secondHand;
			} else {
				firstToGive = state.secondHand;
				secondToGive = state.firstHand;
			}

			while (!state.deck.isEmpty() && firstToGive.size() < 6) {
				firstToGive.add(getNextCard(state.deck));
			}

			while (!state.deck.isEmpty() && secondToGive.size() < 6) {
				secondToGive.add(getNextCard(state.deck));
			}
			// prepare next move
			state.isFirstPlayerAttack = !state.isFirstPlayerAttack;
			return new CommonInfo(TurnType.Attack, Collections.unmodifiableSet(newDead), null, null, state.deck.size(),
					commonInfo.getDeckBottomCard());

		} else {
			Set<Card> attackerCards;
			if (state.isFirstPlayerAttack)
				attackerCards = state.firstHand;
			else
				attackerCards = state.secondHand;

			if (!attackerCards.remove(move.getCard()))
				throw new IllegalStateException("Attack card not in hand");

			return new CommonInfo(TurnType.Defence, commonInfo.getDeadCards(), move.getCard(), commonInfo.getFights(),
					commonInfo.getRemainingDeckCardsNumber(), commonInfo.getDeckBottomCard());
		}
	}

	private static Move callAttackerMove(HiddenInfo state, CommonInfo commonInfo) {
		boolean isFirstPlayerMove = state.isFirstPlayerAttack;
		return callMove(state, commonInfo, isFirstPlayerMove);
	}

	private static Move callDefenderMove(HiddenInfo state, CommonInfo commonInfo) {
		boolean isFirstPlayerMove = !state.isFirstPlayerAttack;
		return callMove(state, commonInfo, isFirstPlayerMove);
	}

	private static Move callMove(HiddenInfo state, CommonInfo commonInfo, boolean isFirstPlayerMove) {
		int opponentCardsNumber;
		Set<Card> yourCards;
		if (isFirstPlayerMove) {
			yourCards = Collections.unmodifiableSet(state.firstHand);
			opponentCardsNumber = state.secondHand.size();
		} else {
			yourCards = Collections.unmodifiableSet(state.secondHand);
			opponentCardsNumber = state.firstHand.size();
		}
		Turn turn = new Turn(commonInfo, yourCards, opponentCardsNumber);
		Move move;
		if (isFirstPlayerMove) {
			move = firstPlayerMove(turn);
		} else {
			move = secondPlayerMove(turn);
		}
		return move;
	}

	private static CommonInfo makeAfterAttackMove(HiddenInfo state, CommonInfo commonInfo) {
		Move move = callAttackerMove(state, commonInfo);
		if (move.getMoveType().equals(MoveType.Finish)) {
			// Give all cards from fights to defender
			// Give cards to attacker
			// next attack
		} else {
			// Make new fight
			// if max cards were given - do like "finish" move
			// else - next afterAttack

		}
		// TODO Auto-generated method stub
		return null;

	}

	private static CommonInfo makeDefenceMove(HiddenInfo state, CommonInfo commonInfo) {
		Move move = callDefenderMove(state, commonInfo);
		if (move.getMoveType().equals(MoveType.Finish)) {
			// Move attacker to fights
			// if max cards were given - do like "finish" in afterAttack move
			// else - call AfterAttack
		} else {
			// check if move correct
			// make new fight
			// if max cards were given - do like "finish" in attack
			// else - call Attack

		}
		// TODO Auto-generated method stub
		return null;

	}

	private static Move firstPlayerMove(Turn turn) {
		// TODO Auto-generated method stub
		return null;
	}

	private static Move secondPlayerMove(Turn turn) {
		// TODO Auto-generated method stub
		return null;
	}

	private static void printResult(HiddenInfo state, CommonInfo commonInfo) {
		// TODO Auto-generated method stub

	}

}
