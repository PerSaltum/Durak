package main;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
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
import main.strategy.GreedyStrategy;
import main.strategy.Strategy;

public class Game {

	public static boolean useUnmodifiable = true;

	public static void main(String[] args) {
		long timeStart = System.currentTimeMillis();
		Strategy firstPlayer = new GreedyStrategy(1);
		Strategy secondPlayer = new ConsoleStrategy();

		double totalScore = 0;
		int win = 0;
		int draw = 0;
		int loose = 0;
		for (int i = 0; i < 1000000; i++) {
			GameResult gameResult = playGame(firstPlayer, secondPlayer, i);
			double result = gameResult.getFirstPlayerScore();
			totalScore += result;
			if (gameResult.getWinner() == 1) {
				win++;
			} else if (gameResult.getWinner() == 0) {
				draw++;
			} else {
				assert gameResult.getWinner() == -1;
				loose++;
			}
		}
		long timeStop = System.currentTimeMillis();
		System.out.println("Score: " + totalScore);
		System.out.println("Win/draw/loose: " + win + "/" + draw + "/" + loose);
		System.out.println("Time, ms: " + (timeStop - timeStart));
	}
	
	public static GameResult playGame(Strategy firstPlayer, Strategy secondPlayer, long randomSeed) {
		HiddenInfo state = new HiddenInfo(firstPlayer, secondPlayer, randomSeed);
		CommonInfo commonInfo = state.initCommonInfo();
		while (!isGameOver(state, commonInfo))
			commonInfo = makeMove(state, commonInfo);
		
		final double result = getResult(state, commonInfo, false);
		
		return new GameResult() {
			@Override
			public int getWinner() {
				if (result > 0.5)
					return 1;
				if (result > -0.5)
					return 0;
				return -1;
			}
	
			@Override
			public double getFirstPlayerScore() {
				return result;
			}
	
			/**
			 * Return -1 for better score
			 */
			@Override
			public int compareTo(GameResult arg0) {
				if (this.getFirstPlayerScore() > arg0.getFirstPlayerScore())
					return -1;
				return 1;
			}
		};
	}

	public interface GameResult extends Comparable<GameResult> {
		/**
		 * first player: 1
		 * draw: 0
		 * second player: -1
		 */
		int getWinner();
		double getFirstPlayerScore();
	}

	private static final Card[] allCards;
	static {
		allCards = new Card[Value.values().length * Suit.values().length];
		int i = 0;
		for (Value value : Value.values())
			for (Suit suit : Suit.values())
				allCards[i++] = new Card(suit, value);
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
		Set<Card> deadSet;
		if (useUnmodifiable)
			deadSet = Collections.unmodifiableSet(newDead);
		else
			deadSet = newDead;
		return new CommonInfo(TurnType.Attack, deadSet, null, new LinkedList<CardFight>(), state.getDeckSize(),
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
		return new CommonInfo(TurnType.Attack, commonInfo.getDeadCards(), null, new LinkedList<CardFight>(),
				state.getDeckSize(), commonInfo.getDeckBottomCard());
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
			int notDefendedFights = 0;
			for (CardFight cardFight : fights) {
				if (cardFight.getDefender() == null)
					notDefendedFights++;
			}
			int defenderCardsNumber = state.getDefenderCards().size();
			assert notDefendedFights <= defenderCardsNumber;
			if (fightsNumber >= 6 || notDefendedFights >= defenderCardsNumber) {
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

	private static double getResult(HiddenInfo state, CommonInfo commonInfo, boolean verbose) {
		return state.getResults(commonInfo, verbose);
	}

}
