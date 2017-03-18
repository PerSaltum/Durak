package main;

import java.util.Set;

public class GreedyStrategy implements Strategy {

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
		if (move.getMoveType().equals(MoveType.Finish))
			return false;

		if (best.getMoveType().equals(MoveType.Finish))
			return true;

		Suit trumpSuit = turn.getTrumpSuit();
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

}
