package main.strategy;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.util.Set;

import main.Game;
import main.Utils;
import main.common.Card;
import main.common.CardFight;
import main.common.CommonInfo;
import main.common.Move;
import main.common.Turn;
import main.common.TurnType;

/**
 * Human player interface via console.
 */
public class ConsoleStrategy implements Strategy {

	private static final Scanner in = new Scanner(System.in);
	private static final PrintStream out = System.out;
	
	private boolean isFirstMove = true;
	
	@Override
	public Move move(Turn turn) {
		printTurn(turn);
		Move m = readMove(turn);
		return m;
	}
	
	private void printTurn(Turn turn) {
		out.println("========");
		
		if (isFirstMove) {
			// Game begins. Show deck bottom card.
			printDeckBottomCard(turn);
			isFirstMove = false;
		}

		CommonInfo commonInfo = turn.getCommonInfo();
		
		// Turn type
		out.println(commonInfo.getTurnType());

		// Card fights
		if (!commonInfo.getFights().isEmpty()) {
			for (CardFight cardFight : commonInfo.getFights())
				out.print(cardFight.getDefender() + " ");
			out.println();
			for (CardFight cardFight : commonInfo.getFights())
				out.print(cardFight.getAttacker() + " ");
			out.println();
		}
		
		// Attacker card
		if (commonInfo.getTurnType().equals(TurnType.Defence))
			out.println("Attacker: " + commonInfo.getAttacker());
		
		// Possible moves
		out.print("Moves: ");

		Set<Move> possibleMoves = Utils.getPossibleMoves(turn);
		Move[] possibleMovesSorted = sortPossibleMoves(possibleMoves);
		for (Move move : possibleMovesSorted)
			out.print(move + " ");
		
		out.println();
	}
	
	private Move[] sortPossibleMoves(Set<Move> possibleMoves) {
		Move[] possibleMovesArray = new Move[possibleMoves.size()];
		{
			int i = 0;
			for (Move move : possibleMoves)
				possibleMovesArray[i++] = move;
			Arrays.sort(possibleMovesArray, new Comparator<Move>() {
				@Override
				public int compare(Move o1, Move o2) {
					return o1.hashCode() - o2.hashCode();
				}
			});
		}
		
		return possibleMovesArray;
	}

	private void printDeckBottomCard(Turn turn) {
		out.println(turn.getCommonInfo().getDeckBottomCard());
	}
	
	private Move readMove(Turn turn) {
		String input = in.nextLine().trim();

		Move move = parseInput(input);
		if (move == null)
			return null;
		
		Set<Move> possibleMoves = Utils.getPossibleMoves(turn);
		if (!possibleMoves.contains(move))
			return null;
		
		return move;
	}
	
	/**
	 * @return null when can't parse
	 */
	private Move parseInput(String input) {
		if (input.equals("finish"))
			return Move.createFinishMove();

		Card[] allCards = Game.getAllCards();
		for (Card card : allCards)
			if (card.toString().equals(input))
				return Move.createUseCardMove(card);
		
		return null;
	}
}
