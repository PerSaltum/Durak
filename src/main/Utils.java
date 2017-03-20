package main;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;

import main.common.Card;
import main.common.CardFight;
import main.common.Move;
import main.common.Suit;
import main.common.Turn;
import main.common.TurnType;

public class Utils {

	private static final Move finish = Move.createFinishMove();;

	public static Set<Move> getPossibleMoves(Turn turn) {
		Set<Move> result = new LinkedHashSet<>();

		if (turn.getCommonInfo().getTurnType().equals(TurnType.Attack)
				|| turn.getCommonInfo().getTurnType().equals(TurnType.AfterAttack)) {
			if (turn.getCommonInfo().getFights() == null || turn.getCommonInfo().getFights().size() == 0) {
				for (Card card : turn.getYourCards()) {
					result.add(Move.createUseCardMove(card));
				}
			} else {
				loop: for (Card card : turn.getYourCards()) {
					for (CardFight cardFight : turn.getCommonInfo().getFights()) {
						if (card.getValue().equals(cardFight.getAttacker().getValue())
								|| (cardFight.getDefender() != null
										&& card.getValue().equals(cardFight.getDefender().getValue()))) {
							result.add(Move.createUseCardMove(card));
							continue loop;
						}

					}
				}
			}
		} else {
			Card attacker = turn.getCommonInfo().getAttacker();
			Suit trumpSuit = turn.getCommonInfo().getTrumpSuit();
			for (Card card : turn.getYourCards()) {
				if (isDefenderBeatAttacker(attacker, card, trumpSuit))
					result.add(Move.createUseCardMove(card));
			}
		}
		if (!turn.getCommonInfo().getTurnType().equals(TurnType.Attack) || !turn.getCommonInfo().getFights().isEmpty())
			result.add(finish);
		return result;
	}

	public static boolean isDefenderBeatAttacker(Card attacker, Card defender, Suit trumpSuit) {
		if (!defender.getSuit().equals(attacker.getSuit()) && !defender.getSuit().equals(trumpSuit))
			return false;

		if (defender.getSuit().equals(trumpSuit) && !attacker.getSuit().equals(trumpSuit))
			return true;

		if (defender.getValue().ordinal() > attacker.getValue().ordinal())
			return true;

		return false;

	}

	public static Move[] sortMoves(Set<Move> possibleMoves) {
		Move[] possibleMovesArray = new Move[possibleMoves.size()];
		int i = 0;
		for (Move move : possibleMoves)
			possibleMovesArray[i++] = move;
		Arrays.sort(possibleMovesArray, new Comparator<Move>() {
			@Override
			public int compare(Move o1, Move o2) {
				return o1.hashCode() - o2.hashCode();
			}
		});

		return possibleMovesArray;
	}

	public static Card[] sortCards(Set<Card> cards) {
		Card[] cardsArray = new Card[cards.size()];
		int i = 0;
		for (Card card : cards)
			cardsArray[i++] = card;
		Arrays.sort(cardsArray, new Comparator<Card>() {
			@Override
			public int compare(Card o1, Card o2) {
				return o1.hashCode() - o2.hashCode();
			}
		});

		return cardsArray;
	}

}
