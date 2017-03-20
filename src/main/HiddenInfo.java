package main;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import main.common.Card;
import main.common.CommonInfo;
import main.common.Move;
import main.common.Turn;
import main.common.TurnType;

public class HiddenInfo {
	private final List<Card> deck = new LinkedList<>();// not in order
	private final Set<Card> firstHand = new LinkedHashSet<>();
	private final Set<Card> secondHand = new LinkedHashSet<>();
	private boolean isFirstPlayerAttack = true;
	static final Random random = new Random();

	public HiddenInfo() {
		List<Card> tmp = new LinkedList<>(Arrays.asList(Game.getAllCards()));
		while (!tmp.isEmpty()) {
			int num = HiddenInfo.random.nextInt(tmp.size());
			deck.add(tmp.remove(num));
		}

		for (int i = 0; i < 6; i++) {
			firstHand.add(this.getNextCard());
			secondHand.add(this.getNextCard());
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

	public Card getNextCard() {
		return deck.remove(deck.size() - 1);
	}

	public CommonInfo initCommonInfo() {
		return new CommonInfo(TurnType.Attack, null, null, null, deck.size(), deck.get(0));
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
			yourCards = Collections.unmodifiableSet(firstHand);
			opponentCardsNumber = secondHand.size();
		} else {
			yourCards = Collections.unmodifiableSet(secondHand);
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
		// TODO Auto-generated method stub
		return null;
	}

	private Move secondPlayerMove(Turn turn) {
		// TODO Auto-generated method stub
		return null;
	}

}
