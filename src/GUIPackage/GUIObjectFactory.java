package GUIPackage;

import java.util.ResourceBundle;

import Controller.Controller;

/**
 * Instantiate GUIObjects based on String passed in.
 *
 * @author AnnieTang
 */
public class GUIObjectFactory {
	private ResourceBundle myResources;
	private GUICanvas canvas;
	private Controller myController;
	private GUICommandLine myCommandLine;
	
	public GUIObjectFactory(ResourceBundle myResources, Controller myController, GUICanvas canvas, GUICommandLine cLine){
		this.myResources = myResources;
		this.canvas = canvas; 
		this.myController = myController;
		this.myCommandLine = cLine;
	}
	
	/**
	 * Creates new GUIObject based on nodeType passed in. 
	 * @param nodeTypeKey
	 * @return Specified IGUIObject
	 */
	protected IGUIObject createNewGUIObject(String nodeTypeKey){
		String nodeType = myResources.getString(nodeTypeKey);
		switch(nodeType){
			case("CommandLineVBox"):{
				return null;
			}
			case("ImageVBox"):{
				return new GUIObjectImageVBox(myResources, myController, canvas, nodeType);
			}
			case("LanguageComboBox"):{
				return new GUIObjectComboBoxLanguages(myResources, myController, 
						myResources.getString(nodeTypeKey+"PromptText"), myCommandLine);
			}
			case("VariablesTableView"):{
				return new GUIObjectTableView(myResources);
			}
			case("UserCommandsComboBox"):{
				return new GUIObjectComboBoxUserHist(myResources, myController,
						myResources.getString(nodeTypeKey+"PromptText"), myCommandLine);
			}
			case("PreviousCommandsComboBox"):{
				return new GUIObjectComboBoxCommandHist(myResources, myController, 
						myResources.getString(nodeTypeKey+"PromptText"), myCommandLine);
			}
			case("HelpTabPane"):{
				return null;
			}
			case("ColorPickerBackground"):{
				return new GUIObjectColorPickerBackground(canvas,myResources.getString(nodeTypeKey+"Label"));
			}
			case("ColorPickerPen"):{
				return new GUIObjectColorPickerPen(canvas,myResources.getString(nodeTypeKey+"Label"));
			}
			case("TurtleState"):{
				return new GUIObjectTurtleState(myResources,
						new GUIObjectLabeled(myResources, myResources.getString("TurtleLocation")),
						new GUIObjectLabeled(myResources, myResources.getString("TurtleHeading")), 
						new GUIObjectLabeled(myResources, myResources.getString("TurtlePen")),
						canvas);
			}
		}
		return null;
	}
}
