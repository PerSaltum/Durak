package main;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import main.common.Card;
import main.common.CardFight;
import main.common.CommonInfo;
import main.common.Move;
import main.common.Turn;
import main.common.TurnType;
import main.strategy.Strategy;

public class HiddenInfo {
	private final List<Card> deck = new LinkedList<>();// not in order
	private final Set<Card> firstHand = new LinkedHashSet<>();
	private final Set<Card> secondHand = new LinkedHashSet<>();
	private boolean isFirstPlayerAttack = true;
	static final Random random = new Random();

	private final Strategy firstPlayer;
	private final Strategy secondPlayer;

	public HiddenInfo(Strategy firstPlayer, Strategy secondPlayer) {
		this.firstPlayer = firstPlayer;
		this.secondPlayer = secondPlayer;

		List<Card> tmp = new LinkedList<>(Arrays.asList(Game.getAllCards()));
		while (!tmp.isEmpty()) {
			int num = HiddenInfo.random.nextInt(tmp.size());
			deck.add(tmp.remove(num));
		}

		for (int i = 0; i < 6; i++) {
			firstHand.add(this.pullCardFromDeck());
			secondHand.add(this.pullCardFromDeck());
		}

	}

	public void switchRoles() {
		isFirstPlayerAttack = !isFirstPlayerAttack;
	}

	public Set<Card> getAttackerCards() {
		if (isFirstPlayerAttack)
			return firstHand;
		else
			return secondHand;
	}

	public Set<Card> getDefenderCards() {
		if (isFirstPlayerAttack)
			return secondHand;
		else
			return firstHand;
	}

	public Card pullCardFromDeck() {
		return deck.remove(deck.size() - 1);
	}

	public CommonInfo initCommonInfo() {
		return new CommonInfo(TurnType.Attack, new LinkedHashSet<>(), null, new LinkedList<CardFight>(), deck.size(),
				deck.get(0));
	}

	public int getDeckSize() {
		return deck.size();
	}

	public Move callAttackerMove(CommonInfo commonInfo) {
		return callMove(commonInfo, isFirstPlayerAttack);
	}

	public Move callDefenderMove(CommonInfo commonInfo) {
		return callMove(commonInfo, !isFirstPlayerAttack);
	}

	private Move callMove(CommonInfo commonInfo, boolean isFirstPlayerMove) {
		int opponentCardsNumber;
		Set<Card> yourCards;
		if (isFirstPlayerMove) {
			if (Game.useUnmodifiable)
				yourCards = Collections.unmodifiableSet(firstHand);
			else
				yourCards = firstHand;
			opponentCardsNumber = secondHand.size();
		} else {
			if (Game.useUnmodifiable)
				yourCards = Collections.unmodifiableSet(secondHand);
			else
				yourCards = secondHand;
			opponentCardsNumber = firstHand.size();
		}
		Turn turn = new Turn(commonInfo, yourCards, opponentCardsNumber);
		Move move;
		if (isFirstPlayerMove) {
			move = firstPlayerMove(turn);
		} else {
			move = secondPlayerMove(turn);
		}
		return move;
	}

	private Move firstPlayerMove(Turn turn) {
		return firstPlayer.move(turn);
	}

	private Move secondPlayerMove(Turn turn) {
		return secondPlayer.move(turn);
	}

	public double getResults(CommonInfo commonInfo, boolean vebose) {
		if (!firstHand.isEmpty() && !secondHand.isEmpty())
			throw new IllegalStateException("Game isn't over");

		if (firstHand.isEmpty() && secondHand.isEmpty()) {
			if (vebose)
				System.out.println("Game draw");
			return 0;
		}
		if (firstHand.isEmpty()) {
			double result = 1.0 + ((double) secondHand.size()) / 100.0;
			if (vebose) {
				System.out.println("Player " + firstPlayer.getName() + " win");
				System.out.println("Score: " + result);
			}
			return result;
		} else {
			double result = -(1.0 + ((double) firstHand.size()) / 100.0);
			if (vebose) {
				System.out.println("Player " + secondPlayer.getName() + " win");
				System.out.println("Score: " + result);
			}
			return result;

		}
	}

}
