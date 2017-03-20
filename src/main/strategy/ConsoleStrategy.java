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
		if (isFirstMove) {
			// Game begins. Show deck bottom card.
			printDeckBottomCard(turn);
			isFirstMove = false;
		}
		
		out.println("========");

		CommonInfo commonInfo = turn.getCommonInfo();
		
		// Turn type
		out.println(commonInfo.getTurnType() + ", Deck remainder: " + commonInfo.getRemainingDeckCardsNumber());

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
		out.print("Moves: " + myHand + " " + deadCards + " ");
		
		Set<Move> possibleMoves = Utils.getPossibleMoves(turn);
		Move[] possibleMovesSorted = sortPossibleMoves(possibleMoves);
		for (Move move : possibleMovesSorted)
			out.print(move + " ");
		
		out.println();
	}
	
	private static final String myHand = "my";
	private static final String deadCards = "dead";
	
	// TODO: move to Utils
	private static Move[] sortPossibleMoves(Set<Move> possibleMoves) {
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
		out.println("Deck bottom: " + turn.getCommonInfo().getDeckBottomCard());
	}
	
	private Move readMove(Turn turn) {
		while (true) {
			String input = in.nextLine().trim();
			
			if (input.equals(myHand)) {
				printMyHand(turn);
				continue;
			}
			
			if (input.equals(deadCards)) {
				printDeadCards(turn);
				continue;
			}
			
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
		Card[] cardSetSorted = sortCards(cardSet);
		out.print(name + ": ");
		for (Card card : cardSetSorted)
			out.print(card + " ");
		out.println();
	}
	
	// TODO: move to Utils
	private static Card[] sortCards(Set<Card> cards) {
		Card[] cardsArray = new Card[cards.size()];
		{
			int i = 0;
			for (Card card : cards)
				cardsArray[i++] = card;
			Arrays.sort(cardsArray, new Comparator<Card>() {
				@Override
				public int compare(Card o1, Card o2) {
					return o1.hashCode() - o2.hashCode();
				}
			});
		}
		
		return cardsArray;
	}
	
	/**
	 * @return null when can't parse
	 */
	private Move parseInput(String input) {
		if (input.equals("finish"))
			return Move.createFinishMove();

		input = input.replace('h', '♥').replace('c', '♣').replace('d', '♦').replace('s', '♠')
		             .replace('H', '♥').replace('C', '♣').replace('D', '♦').replace('S', '♠');
		
		Card[] allCards = Game.getAllCards();
		for (Card card : allCards)
			if (card.toString().equals(input))
				return Move.createUseCardMove(card);
		
		return null;
	}
}
