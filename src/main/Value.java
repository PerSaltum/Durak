package main;

public enum Value {
	Six { @Override public String toString() { return "6"; } },
	Seven { @Override public String toString() { return "7"; } },
	Eight { @Override public String toString() { return "8"; } },
	Nine { @Override public String toString() { return "9"; } },
	Ten { @Override public String toString() { return "10"; } },
	Jack { @Override public String toString() { return "J"; } },
	Queen { @Override public String toString() { return "Q"; } },
	King { @Override public String toString() { return "K"; } },
	Ace { @Override public String toString() { return "A"; } }
}
