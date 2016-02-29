package Controller;

import java.util.ArrayList;
import java.util.List;

import Model.Node;
import Model.Turtle;

/**
 * This class is the only external-facing back end class. It facilitates the interaction between the front end and back end
 * by processing the commands inputed by the user in the front end.
 *
 * @author amyzhao
 * @author blakekaplan
 */
public class Controller {

    private static final String SYNTAX_RESOURCE = "resources/languages/Syntax";
    private static final String DEFAULT_LANGUAGE_RESOURCE = "resources/languages/English";
    private static final String LANGUAGE_RESOURCE_LOCATION = "resources/languages/";
    private String myLanguageResource;
    private Parser myParser;
    private List<Turtle> myTurtles;
    private List<String> myCommandHistory;
    private int myCanvasWidth;
    private int myCanvasHeight;
    private Turtle myTurtle;

    /**
     * Initializes the controller.
     */
    public void init(int canvasHeight, int canvasWidth, Turtle t) {
        myCanvasWidth = canvasWidth;
        myCanvasHeight = canvasHeight;
        myParser = new Parser();
        myLanguageResource = DEFAULT_LANGUAGE_RESOURCE;
        myParser.addPatterns(myLanguageResource);
        myParser.addPatterns(SYNTAX_RESOURCE);
        myCommandHistory = new ArrayList<String>();
        myTurtles = new ArrayList<Turtle>();
        myTurtle = t;
    }

    /**
     * Sets the parser language.
     * @param lang: user-selected language.
     */
    public void setLanguage(String lang) {
    	myLanguageResource = LANGUAGE_RESOURCE_LOCATION + lang;
    	myParser.clearAllPatterns();
    	myParser.addPatterns(myLanguageResource);
    	myParser.addPatterns(SYNTAX_RESOURCE);
    }
    
    /**
     * Processes the command.
     *
     * @param command: String inputed by user to the command line.
     * @throws ClassNotFoundException
     */
    public void processCommand(String command){
    	try{
    		List<Node> commands = myParser.createCommandTree(command, myTurtle);
            executeCommandTree(commands);
    	}
    	catch(ClassNotFoundException e){
    		System.out.println("Could not process command.");
    	}
    }

    private void executeCommandTree(List<Node> headNodes) {
        for (int i = 0; i < headNodes.size(); i++) {
            Node head = headNodes.get(i);
            System.out.println(head.toString());
            //head.interpret();
            System.out.println("result: " + Double.toString(head.interpret()));
            System.out.println(myTurtle.printPosition());
            addCommandToHistory(head.toString());
        }
    }


    private void addCommandToHistory(String command) {
        myCommandHistory.add(command);
    }

    public List<String> getCommandHistory() {
        return myCommandHistory;
    }

}
