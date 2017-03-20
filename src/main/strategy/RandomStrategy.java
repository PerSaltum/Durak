package main.strategy;

import java.util.Set;

import main.Utils;
import main.common.Move;
import main.common.Turn;
import main.utils.Randomizer;

/**
 * Does random moves.
 */
public class RandomStrategy implements Strategy {

	private final Randomizer<Move> randomizer = Randomizer.create(0);
	
	@Override
	public Move move(Turn turn) {
		Set<Move> possibleMoves = Utils.getPossibleMoves(turn);
		assert(!possibleMoves.isEmpty()) : "No possible moves?";
		
		randomizer.addAll(possibleMoves);
		return randomizer.remove();
	}

}
