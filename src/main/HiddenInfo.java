package main;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import main.common.Card;

public class HiddenInfo {
	final List<Card> deck = new LinkedList<>();// not in order
	final Set<Card> firstHand = new LinkedHashSet<>();
	final Set<Card> secondHand = new LinkedHashSet<>();
	boolean isFirstPlayerAttack = true;
}
