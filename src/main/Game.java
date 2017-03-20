package main;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import main.common.Card;
import main.common.CardFight;
import main.common.CommonInfo;
import main.common.Move;
import main.common.MoveType;
import main.common.Suit;
import main.common.TurnType;
import main.common.Value;
import main.strategy.ConsoleStrategy;
import main.strategy.RandomStrategy;
import main.strategy.Strategy;

public class Game {
	private static final Card[] allCards;
	static {
		allCards = new Card[Value.values().length * Suit.values().length];
		int i = 0;
		for (Value value : Value.values())
			for (Suit suit : Suit.values())
				allCards[i++] = new Card(suit, value);
	}

	public static void main(String[] args) {
		Strategy firstPlayer = new RandomStrategy();
		Strategy secondPlayer = new ConsoleStrategy();
		
		HiddenInfo state = new HiddenInfo(firstPlayer, secondPlayer);
		CommonInfo commonInfo = state.initCommonInfo();
		while (isGameOver(state, commonInfo))
			commonInfo = makeMove(state, commonInfo);
		printResult(state, commonInfo);
	}

	public static Card[] getAllCards() {
		return allCards;
	}

	private static boolean isGameOver(HiddenInfo state, CommonInfo commonInfo) {
		if (state.getDeckSize() > 0)// commonInfo.getRemainingDeckCardsNumber()
			return false;

		if (!state.getAttackerCards().isEmpty() && !state.getDefenderCards().isEmpty())
			return false;

		if (commonInfo.getTurnType().equals(TurnType.Defence))
			return false;
		return true;
	}

	private static CommonInfo makeMove(HiddenInfo state, CommonInfo commonInfo) {
		switch (commonInfo.getTurnType()) {
		case Attack:
			return makeAttackMove(state, commonInfo);
		case AfterAttack:
			return makeAfterAttackMove(state, commonInfo);
		case Defence:
			return makeDefenceMove(state, commonInfo);
		}

		// unreachable
		throw new UnsupportedOperationException("Unsupported turn type");
	}

	private static CommonInfo makeAttackMove(HiddenInfo state, CommonInfo commonInfo) {
		Move move = state.callAttackerMove(commonInfo);
		if (move.getMoveType().equals(MoveType.Finish)) {
			return successfulDefense(state, commonInfo);

		} else {
			Set<Card> attackerCards = state.getAttackerCards();

			if (!attackerCards.remove(move.getCard()))
				throw new IllegalStateException("Attack card not in hand");

			return new CommonInfo(TurnType.Defence, commonInfo.getDeadCards(), move.getCard(), commonInfo.getFights(),
					commonInfo.getRemainingDeckCardsNumber(), commonInfo.getDeckBottomCard());
		}
	}

	private static CommonInfo successfulDefense(HiddenInfo state, CommonInfo commonInfo) {
		if (commonInfo.getFights() == null || commonInfo.getFights().isEmpty())
			throw new IllegalStateException("Finish move when no fights");

		// Fights to dead
		Set<Card> newDead = new LinkedHashSet<>(commonInfo.getDeadCards());
		for (CardFight cardFight : commonInfo.getFights()) {
			newDead.add(cardFight.getAttacker());
			newDead.add(cardFight.getDefender());
		}

		// give cards to player, defender last
		Set<Card> firstToGive = state.getAttackerCards();
		Set<Card> secondToGive = state.getDefenderCards();

		while (state.getDeckSize() > 0 && firstToGive.size() < 6) {
			firstToGive.add(state.pullCardFromDeck());
		}

		while (state.getDeckSize() > 0 && secondToGive.size() < 6) {
			secondToGive.add(state.pullCardFromDeck());
		}
		// prepare next move
		state.switchRoles();
		return new CommonInfo(TurnType.Attack, Collections.unmodifiableSet(newDead), null, null, state.getDeckSize(),
				commonInfo.getDeckBottomCard());
	}

	private static CommonInfo makeAfterAttackMove(HiddenInfo state, CommonInfo commonInfo) {
		Move move = state.callAttackerMove(commonInfo);
		if (move.getMoveType().equals(MoveType.Finish)) {
			return pickUpCards(state, commonInfo);
		} else {
			// remove card from attacker
			Set<Card> attackerCards = state.getAttackerCards();
			if (!attackerCards.remove(move.getCard()))
				throw new IllegalStateException("AfterAttack card not in hand");

			// Make new fight
			CardFight newFight = new CardFight(move.getCard(), null);
			List<CardFight> fights = commonInfo.getFights();
			fights.add(newFight);// FIXME
			assert fights.size() <= 6;
			int notBeatenCardsNumber = 0;
			for (CardFight fight : fights)
				if (fight.getDefender() == null)
					notBeatenCardsNumber++;
			assert notBeatenCardsNumber <= state.getDefenderCards().size();
			if (fights.size() >= 6 || notBeatenCardsNumber >= state.getDefenderCards().size()) {
				// if max cards were given - do like "finish" move
				return pickUpCards(state, commonInfo);
			}
			// else - next afterAttack
			return new CommonInfo(TurnType.AfterAttack, commonInfo.getDeadCards(), null, fights,
					commonInfo.getRemainingDeckCardsNumber(), commonInfo.getDeckBottomCard());
		}
	}

	private static CommonInfo pickUpCards(HiddenInfo state, CommonInfo commonInfo) {
		// Give all cards from fights to defender
		Set<Card> defenderCards = state.getDefenderCards();
		for (CardFight fight : commonInfo.getFights()) {
			defenderCards.add(fight.getAttacker());
			Card defender = fight.getDefender();
			if (defender != null)
				defenderCards.add(defender);
		}
		// Give cards to attacker
		Set<Card> attackerCards = state.getAttackerCards();
		while (attackerCards.size() < 6 && state.getDeckSize() > 0) {
			attackerCards.add(state.pullCardFromDeck());
		}
		// next attack
		return new CommonInfo(TurnType.Attack, commonInfo.getDeadCards(), null, null, state.getDeckSize(),
				commonInfo.getDeckBottomCard());
	}

	private static CommonInfo makeDefenceMove(HiddenInfo state, CommonInfo commonInfo) {
		Move move = state.callDefenderMove(commonInfo);
		if (move.getMoveType().equals(MoveType.Finish)) {
			// Move attacker to fights
			CardFight newFight = new CardFight(commonInfo.getAttacker(), null);
			List<CardFight> fights = commonInfo.getFights();
			fights.add(newFight);// FIXME
			// if max cards were given - do like "finish" in afterAttack move
			int fightsNumber = fights.size();
			assert fightsNumber <= 6;
			int defenderCardsNumber = state.getDefenderCards().size();
			assert fightsNumber <= defenderCardsNumber;
			if (fightsNumber >= 6 || fightsNumber >= defenderCardsNumber) {
				return pickUpCards(state, commonInfo);
			}
			// else - call AfterAttack
			return new CommonInfo(TurnType.AfterAttack, commonInfo.getDeadCards(), null, fights,
					commonInfo.getRemainingDeckCardsNumber(), commonInfo.getDeckBottomCard());
		} else {
			// check if move correct
			Set<Card> defenderCards = state.getDefenderCards();
			Card defendCard = move.getCard();
			if (!defenderCards.remove(defendCard))
				throw new IllegalStateException("Defend card not in hand");

			if (!Utils.isDefenderBeatAttacker(commonInfo.getAttacker(), defendCard, commonInfo.getTrumpSuit()))
				throw new IllegalStateException("Defend card not beat attacker");

			// make new fight
			CardFight newFight = new CardFight(commonInfo.getAttacker(), defendCard);
			List<CardFight> fights = commonInfo.getFights();
			fights.add(newFight);// FIXME

			// if max cards were given - do like "finish" in attack
			assert fights.size() <= 6;
			if (fights.size() >= 6 || defenderCards.isEmpty()) {
				return successfulDefense(state, commonInfo);
			}
			// else - call Attack
			return new CommonInfo(TurnType.Attack, commonInfo.getDeadCards(), null, fights,
					commonInfo.getRemainingDeckCardsNumber(), commonInfo.getDeckBottomCard());
		}

	}

	private static void printResult(HiddenInfo state, CommonInfo commonInfo) {
		// TODO Auto-generated method stub

	}

}
