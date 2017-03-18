package main;

import java.util.LinkedHashSet;
import java.util.Set;

public class Utils {

	private static final Move finish = new Move(MoveType.Finish, null);

	public static Set<Move> getPossibleMoves(Turn turn) {
		Set<Move> result = new LinkedHashSet<>();

		if (turn.getTurnType().equals(TurnType.Attack) || turn.getTurnType().equals(TurnType.AfterAttack)) {
			if (turn.getFights() == null || turn.getFights().length == 0) {
				for (Card card : turn.getYourCards()) {
					result.add(new Move(MoveType.SendCards, card));
				}
			} else {
				loop: for (Card card : turn.getYourCards()) {
					for (CardFight cardFight : turn.getFights()) {
						if (card.getValue().equals(cardFight.getAttacker().getValue())
								|| card.getValue().equals(cardFight.getDefender().getValue())) {
							result.add(new Move(MoveType.SendCards, card));
							continue loop;
						}

					}
				}
			}
		} else {
			Card attacker = turn.getAttacker();
			Suit trumpSuit = turn.getTrumpSuit();
			for (Card card : turn.getYourCards()) {
				if (!card.getSuit().equals(attacker.getSuit()) && !card.getSuit().equals(trumpSuit))
					continue;

				if (card.getSuit().equals(trumpSuit) && !attacker.getSuit().equals(trumpSuit)) {
					result.add(new Move(MoveType.SendCards, card));
					continue;
				}

				if (card.getValue().ordinal() > attacker.getValue().ordinal()) {
					result.add(new Move(MoveType.SendCards, card));
					continue;
				}
			}
		}

		result.add(finish);
		return null;
	}

}
