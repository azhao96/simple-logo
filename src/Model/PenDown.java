package Model;

public class PenDown extends Node {

	@Override
	public double interpret() {
		Turtle turtle = getTurtle();
		turtle.putPenDown();
		return 1;
	}

	@Override
	public String toString() {
		return "PenDown";
	}

}
