package model;

import java.util.List;

public class Ask extends TurtleNode {

	private static final String ASK = "Ask ";
	private static final int TURTLE_IDS = 0;
	private static final int COMMANDS = 1;
	@Override
	public double interpret(CommandDictionary commandDict, VariableDictionary varDict)
			throws ClassNotFoundException, NullPointerException, IndexOutOfBoundsException {
		List<Turtle> origActiveList = getActiveTurtles();
		List<Double> turtleIDs = createListFromCommandList((CommandList) getChildren().get(TURTLE_IDS), commandDict, varDict);

		inactivateAllTurtles();
		// only activates the ones listed in turtleIDs
		for (int i = 0; i < turtleIDs.size(); i++) {
			Turtle turtle = getTurtleByID(turtleIDs.get(i));
			if (turtle != null) {
				turtle.changeCurrentTurtleStatus(true);
				turtle.setActive(true);
				turtle.changeCurrentTurtleStatus(false);
			} else {
				createTurtle(turtleIDs.get(i));
			}
		}
		
		double ret = getChildren().get(COMMANDS).interpret(commandDict, varDict);
		
		inactivateAllTurtles();
		for (int i = 0; i < origActiveList.size(); i++) {
			Turtle turtle = getTurtleByID(origActiveList.get(i).getID());
			if (turtle != null) {
				turtle.changeCurrentTurtleStatus(true);
				turtle.setActive(true);
				turtle.changeCurrentTurtleStatus(false);
			}
		}
		
		return ret;
	}

	@Override
	protected double applyToIndividualTurtle(Turtle turtle, CommandDictionary commandDict, VariableDictionary varDict)
			throws ClassNotFoundException, NullPointerException, IndexOutOfBoundsException {
		return 0;
	}
	
	@Override
	public String toString() {
		return ASK + childrenToString();
	}
}
