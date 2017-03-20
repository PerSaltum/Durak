package main.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

/**
 * Data structure with operations:
 * 1) Add element
 * 2) Remove random element
 */
public class Randomizer<T> {
	private final Random random;
	private final ArrayList<T> elems;
	
	private int size = 0;
	
	public Randomizer(int seed) {
		this.random = new Random(seed);
		this.elems = new ArrayList<T>();
	}
	
	public static <T> Randomizer<T> create(int seed) {
		return new Randomizer<T>(seed);
	}
	
	/**
	 * Add element.
	 */
	public void add(T elem) {
		elems.add(elem);
		++size;
	}
	
	/**
	 * Add all elements from the collection.
	 */
	public void addAll(Collection<T> elems) {
		for (T elem : elems)
			add(elem);
	}
	
	/**
	 * Remove element from the structure.
	 * @return removed element.
	 */
	public T remove() {
		if (size == 0)
			throw new IllegalStateException("Can't remove from an empty randomizer");
		int k = random.nextInt(size);
		T result = elems.get(k);
		elems.set(k, elems.get(size - 1));
		--size;
		return result;
	}
	
	public int getSize() {
		return size;
	}
	
	public boolean isEmpty() {
		return size == 0;
	}
	
	@Override
	public String toString() {
		return elems.toString();
	}
}
