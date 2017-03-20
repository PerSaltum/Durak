package main.strategy;

import java.util.Random;
import java.util.Set;

import main.Utils;
import main.common.Move;
import main.common.Turn;

/**
 * Does random moves.
 */
public class RandomStrategy implements Strategy {

	private final Random random = new Random(0);
	
	@Override
	public Move move(Turn turn) {
		Set<Move> possibleMoves = Utils.getPossibleMoves(turn);
		assert(!possibleMoves.isEmpty()) : "No possible moves?";
		
		// Random element index
		int k = random.nextInt(possibleMoves.size());
		
		// Iterate to the k-th element
		for (Move move : possibleMoves)
			if (k-- == 0)
				return move;

		// unreachable
		throw new IllegalStateException();
	}

}
