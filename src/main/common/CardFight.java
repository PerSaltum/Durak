package main.common;

public class CardFight {
	private final Card attacker;
	private final Card defender;
	
	public CardFight(Card attacker, Card defender) {
		this.attacker = attacker;
		this.defender = defender;
	}

	public Card getAttacker() {
		return attacker;
	}

	public Card getDefender() {
		return defender;
	}
}
