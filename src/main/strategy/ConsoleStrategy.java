package main.strategy;

import java.io.PrintStream;
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

	@Override
	public Move move(Turn turn) {
		printTurn(turn);
		Move m = readMove(turn);
		return m;
	}

	private void printTurn(Turn turn) {
		out.println("============================================");

		printDeadCards(turn);
		CommonInfo commonInfo = turn.getCommonInfo();

		// Turn type
		out.println(commonInfo.getTurnType() + ", Deck: " + commonInfo.getRemainingDeckCardsNumber() + ", Trump: "
				+ turn.getCommonInfo().getDeckBottomCard() + ", Enemy: " + turn.getOpponentCardsNumber());

		printMyHand(turn);
		// Card fights
		if (commonInfo.getFights() != null) {
			out.print("Def: ");
			for (CardFight cardFight : commonInfo.getFights())
				out.print(cardFight.getDefender() + " ");
			out.println();
			out.print("Att: ");
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
		Move[] possibleMovesSorted = Utils.sortMoves(possibleMoves);
		for (Move move : possibleMovesSorted)
			out.print(move + " ");

		out.println();
	}

	private Move readMove(Turn turn) {
		while (true) {
			String input = in.nextLine().trim();

			Move move = parseInput(input);
			if (move == null) {
				out.print("Couldn't parse the move. Try again: ");
				continue;
			}

			Set<Move> possibleMoves = Utils.getPossibleMoves(turn);
			if (!possibleMoves.contains(move)) {
				out.print("You can't proceed with this move. Try again: ");
				continue;
			}

			return move;
		}

		// unreachable
	}

	private void printMyHand(Turn turn) {
		Set<Card> hand = turn.getYourCards();
		printCardSet(hand, "Hand");
	}

	private void printDeadCards(Turn turn) {
		Set<Card> dead = turn.getCommonInfo().getDeadCards();
		printCardSet(dead, "Dead cards");
	}

	private void printCardSet(Set<Card> cardSet, String name) {
		Card[] cardSetSorted = Utils.sortCards(cardSet);
		out.print(name + ": ");
		for (Card card : cardSetSorted)
			out.print(card + " ");
		out.println();
	}

	/**
	 * @return null when can't parse
	 */
	private Move parseInput(String input) {
		input = input.toUpperCase();

		if (input.equals("FINISH") || input.equals("F"))
			return Move.createFinishMove();

		input = input.replace('H', '♥').replace('C', '♣').replace('D', '♦').replace('S', '♠');

		Card[] allCards = Game.getAllCards();
		for (Card card : allCards)
			if (card.toString().equals(input))
				return Move.createUseCardMove(card);

		return null;
	}
}
