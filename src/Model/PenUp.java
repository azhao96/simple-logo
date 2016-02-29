package Model;

public class PenUp extends Node {

	private static final String PENUP = "penup";
	
	@Override
	public double interpret() {
		Turtle turtle = getTurtle();
		turtle.lifePenUp();
		return 0;
	}

	@Override
	public String toString() {
		return PENUP;
	}

}