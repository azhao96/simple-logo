package model;

/**
 * Tangent function.
 * Created by blakekaplan on 2/27/16.
 */
public class Tangent extends Node {

    private static final String TANGENT = "tan ";
    private static final int DEGREES = 0;
    private static final int DEGREES_PER_PI = 180;

    /**
     * Returns the tangent of the expression, where the expression is given in degrees.
     *
     * @param commandDict
     * @param varDict
     */
    @Override
    public double interpret(CommandDictionary commandDict, VariableDictionary varDict) throws ClassNotFoundException, NullPointerException, IndexOutOfBoundsException {
        double degrees = getChildren().get(DEGREES).interpret(commandDict, varDict);
        double radians = degrees * (Math.PI / DEGREES_PER_PI);
        return Math.tan(radians);
    }

    /**
     * Returns the required user input for this command.
     */
    @Override
    public String toString() {
        return TANGENT + getChildren().get(DEGREES).toString();
    }
}