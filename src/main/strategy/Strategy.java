package main.strategy;

import main.common.Move;
import main.common.Turn;

public interface Strategy {
	public Move move(Turn turn);
}
