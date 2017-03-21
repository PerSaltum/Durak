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
	private static final PrintStream err = System.err;

	private static final int consoleFlushIntervals = 10; // Sleep time (ms)
	
	@Override
	public Move move(Turn turn) {
		printTurn(turn);
		Move m = readMove(turn);
		return m;
	}

	private void printTurn(Turn turn) {
		println("============================================");

		printDeadCards(turn);
		CommonInfo commonInfo = turn.getCommonInfo();

		// Turn type
		print(commonInfo.getTurnType() + ", Deck: " + commonInfo.getRemainingDeckCardsNumber() + ", Trump: ");
		printCard(turn.getCommonInfo().getDeckBottomCard());
		println(", Enemy: " + turn.getOpponentCardsNumber());

		printMyHand(turn);
		// Card fights
		if (!commonInfo.getFights().isEmpty()) {
			print("Def: ");
			for (CardFight cardFight : commonInfo.getFights()) {
				printCard(cardFight.getDefender());
				print(" ");
			}
			println();
			print("Att: ");
			for (CardFight cardFight : commonInfo.getFights()) {
				printCard(cardFight.getAttacker());
				print(" ");
			}
			println();
		}

		// Attacker card
		if (commonInfo.getTurnType().equals(TurnType.Defence)) {
			print("Attacker: ");
			printCard(commonInfo.getAttacker());
			println();
		}

		// Possible moves
		print("Moves: ");

		Set<Move> possibleMoves = Utils.getPossibleMoves(turn);
		Move[] possibleMovesSorted = Utils.sortMoves(possibleMoves);
		for (Move move : possibleMovesSorted) {
			printMove(move);
			print(" ");
		}

		println();
	}

	private Move readMove(Turn turn) {
		while (true) {
			String input = in.nextLine().trim();

			Move move = parseInput(input);
			if (move == null) {
				print("Couldn't parse the move. Try again: ");
				continue;
			}

			Set<Move> possibleMoves = Utils.getPossibleMoves(turn);
			if (!possibleMoves.contains(move)) {
				print("You can't proceed with such move. Try again: ");
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
		print(name + ": ");
		for (Card card : cardSetSorted) {
			printCard(card);
			print(" ");
		}
		println();
	}
	
	private void printMove(Move move) {
		switch (move.getMoveType()) {
		case Finish:
			print("fin");
			break;
		case UseCard:
			printCard(move.getCard());
			break;
		}
		out.flush();
	}
	
	private void printCard(Card card) {
		if (card == null)
			return;
		
		switch (card.getSuit()) {
		case Clubs:
		case Spades:
			print(card);
			break;
		case Diamonds:
		case Hearts:
			printRed(card);
			break;
		}
	}
	
	private static void printRed(Object obj) {
		err.print(obj.toString());
		err.flush();
		sleep();
	}
	
	private static void print(Object obj) {
		out.print(obj.toString());
		out.flush();
		sleep();
	}
	
	private static void println(Object obj) {
		out.println(obj.toString());
		out.flush();
		sleep();
	}
	
	private static void println() {
		out.println();
		out.flush();
		sleep();
	}

	private static void sleep() {
		try {
			Thread.sleep(consoleFlushIntervals);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return null when can't parse
	 */
	private Move parseInput(String input) {
		input = input.toUpperCase();

		if (input.equals("FIN") || input.equals("F"))
			return Move.createFinishMove();

		input = input.replace('H', '♥').replace('C', '♣').replace('D', '♦').replace('S', '♠');

		Card[] allCards = Game.getAllCards();
		for (Card card : allCards)
			if (card.toString().equals(input))
				return Move.createUseCardMove(card);

		return null;
	}
}
