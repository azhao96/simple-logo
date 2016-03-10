package model;

import java.util.List;

/**
 * Object representing a list of commands.
 *
 * @author amyzhao
 */
public class CommandList extends Node {

    /**
     * Interprets a list of commands, executing each command within the list.
     *  @param commandDict
     * @param varDict*/
    @Override
    public double interpret(CommandDictionary commandDict, VariableDictionary varDict) throws ClassNotFoundException {
        List<IFunctions> children = getChildren();
        double ret = 0;

        for (int i = 0; i < children.size(); i++) {
            ret = children.get(i).interpret(commandDict, varDict);
        }

        return ret;
    }

    /**
     * Returns the required user input for this command.
     */
    @Override
    public String toString() {
        return "[ " + childrenToString() + "]";
    }

}
