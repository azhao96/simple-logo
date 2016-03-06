package model;

import java.util.List;

/**
 * Product function.
 * Created by blakekaplan on 2/27/16.
 */
public class Product extends Node {

    private static final String PRODUCT = "product ";
    private static final int EXPR1 = 0;
	private static final int EXPR2 = 1;

	/**
     * Returns the product of expr1 and expr2.
     * @param commandDict
     * @param varDict
     */
    @Override
    public double interpret(CommandDictionary commandDict, VariableDictionary varDict) throws ClassNotFoundException, NullPointerException, IndexOutOfBoundsException {
        List<Node> children = getChildren();
        return children.get(EXPR1).interpret(commandDict, varDict) * children.get(EXPR2).interpret(commandDict, varDict);
    }

    /**
	 * Returns the required user input for this command. 
	 */
    @Override
    public String toString() {
        return PRODUCT + getChildren().get(EXPR1).toString() + " " + getChildren().get(EXPR2).toString();
    }
}