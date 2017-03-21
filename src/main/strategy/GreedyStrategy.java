package main.strategy;

import java.util.Set;

import main.Utils;
import main.common.Card;
import main.common.Move;
import main.common.MoveType;
import main.common.Suit;
import main.common.Turn;
import main.common.TurnType;

public class GreedyStrategy implements Strategy {

	private final int version;

	/**
	 * 
	 * @param version
	 *            - [0..1]
	 */
	public GreedyStrategy(int version) {
		this.version = version;
	}

	@Override
	public Move move(Turn turn) {
		Set<Move> possibleMoves = Utils.getPossibleMoves(turn);

		Move best = null;

		for (Move move : possibleMoves) {
			if (best == null) {
				best = move;
				continue;
			}

			if (isBetterThen(best, move, turn))
				best = move;

		}

		return best;
	}

	/**
	 * @return true, if "move" better then "best"
	 */
	private boolean isBetterThen(Move best, Move move, Turn turn) {
		Suit trumpSuit = turn.getCommonInfo().getTrumpSuit();
		if (version > 0) {
			if (turn.getCommonInfo().getTurnType().equals(TurnType.AfterAttack)) {
				if (move.getMoveType().equals(MoveType.UseCard) && move.getCard().getSuit().equals(trumpSuit))
					return false;
				if (best.getMoveType().equals(MoveType.UseCard) && best.getCard().getSuit().equals(trumpSuit))
					return true;
			}
		}
		if (move.getMoveType().equals(MoveType.Finish))
			return false;

		if (best.getMoveType().equals(MoveType.Finish))
			return true;

		Card bestCard = best.getCard();
		Card moveCard = move.getCard();

		boolean isBestTrump = bestCard.getSuit().equals(trumpSuit);
		boolean isMoveTrump = moveCard.getSuit().equals(trumpSuit);
		if (isBestTrump && !isMoveTrump)
			return true;
		if (isMoveTrump && !isBestTrump)
			return false;

		if (moveCard.getValue().ordinal() < bestCard.getValue().ordinal())
			return true;

		return false;
	}

	@Override
	public String getName() {
		return "Greedy";
	}

}
