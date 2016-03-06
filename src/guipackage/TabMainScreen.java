package guipackage;
import java.util.ResourceBundle;

import controller.Controller;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import model.Turtle;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
/**
 * Create tab for main screen (canvas, command line, options, etc.) 
 * @author AnnieTang
 *
 */

public class TabMainScreen {
	private static final String GUI_RESOURCE = "GUI";
	private static final int PANEL_PADDING = 10;
	private Tab myRootTab;
	private BorderPane myMainScreen;
	private ResourceBundle myResources;
	private GUIObjectFactory myFactory;
	private GUICanvas canvas;
	private GUICommandLine commandLine;
	private Controller myController;
	private GUIObjectLabeled myOutput;
	
	//GUIObject instance variables
	private IGUIObject userCommands;
	private IGUIObject previousCommands;
	private IGUIObject variables;
	private IGUIObject languageSelector;
	private IGUIObject imageInput;
	private IGUIObject colorPickerBackground;
	private IGUIObject colorPickerPen;
	private IGUIObject turtleState;
	
	/**
	 * Initializes Tab with all necessary components.
	 */
	private void initializeTab() {
		//create Turtle and Observer
		this.myResources = ResourceBundle.getBundle(GUI_RESOURCE);
		canvas = new GUICanvas();
		canvas.init();
		myController = new Controller(canvas);
		commandLine = new GUICommandLine(myController, myResources, this);
	}
	
	/**
	 * Sets up all elements on Tab and returns the Tab
	 * @return
	 */
	protected Tab getTab() {
		initializeTab();
		myRootTab = new Tab();
		myMainScreen = new BorderPane();
		myFactory = new GUIObjectFactory(myResources, myController, canvas, commandLine); 
		
		setCenterPane();
		setBottomPane();
		setLeftPane();
		setRightPane();
		setTopPane();
		
		myRootTab.setContent(myMainScreen);
		return myRootTab;
	}
	
	/**
	 * Next 5 methods all place GUIObjects on the Pane.
	 */
	private void setCenterPane() {
		HBox hbox = new HBox();
		Node canvasNode = canvas.createNode();
		turtleState = myFactory.createNewGUIObject("TurtleState");
		Node turtleStateVBox = turtleState.createNode();
		hbox.getChildren().addAll(canvasNode, turtleStateVBox);
		myMainScreen.setCenter(hbox);
	}

	private void setLeftPane() {
		VBox leftPanel = new VBox(PANEL_PADDING);
		colorPickerBackground = myFactory.createNewGUIObject("ColorPickerBackground");
		colorPickerPen = myFactory.createNewGUIObject("ColorPickerPen");
		userCommands = myFactory.createNewGUIObject("UserCommands");
		previousCommands = myFactory.createNewGUIObject("PreviousCommands");
		languageSelector = myFactory.createNewGUIObject("LanguageSelector");
//		imageInput = myFactory.createNewGUIObject("ImageVBox");
		imageInput = myFactory.createNewGUIObject("ImageComboBox");
		leftPanel.getChildren().addAll(colorPickerBackground.createNode(),
				colorPickerPen.createNode(),userCommands.createNode(), 
				previousCommands.createNode(), languageSelector.createNode(), 
				imageInput.createNode());
		myMainScreen.setLeft(leftPanel);
	}
	
	private void setRightPane() {
		HBox rightPanel = new HBox(PANEL_PADDING);
		variables = myFactory.createNewGUIObject("Variables");
		rightPanel.getChildren().add(variables.createNode());
		myMainScreen.setRight(rightPanel);
	}
	
	private void setBottomPane(){
		myMainScreen.setBottom(commandLine.createNode());
	}

	private void setTopPane() {
		myOutput = myController.getGUIOutput();
		myMainScreen.setTop(myOutput.createNode());
	}
	
	/**
	 * Updates the necessary GUI elements when called.
	 */
	protected void updateGUI() {
		userCommands.updateNode();
		previousCommands.updateNode();
		variables.updateNode();
		turtleState.updateNode();
	}
}