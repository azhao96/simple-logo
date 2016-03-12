package guipackage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import controller.Controller;
import javafx.scene.Node;
import javafx.scene.control.Label;

/**
 * Create ComboBox to hold interpretable languages. 
 * @author AnnieTang
 *
 */

public class GUIComboBoxLanguages extends GUIComboBox {

	public GUIComboBoxLanguages(GUICanvas canvas, ResourceBundle myResources, Controller myController,
			String promptText, GUICommandLine myCommandLine) {
		super(canvas, myResources, myController, promptText, myCommandLine);
	}

	@Override
	protected List<String> optionsList() {
		return new ArrayList<>(Arrays.asList(myResources.getString("Languages").split(" ")));
	}
	
	@Override
	protected void setButtonAction(){
		comboButton.setOnAction(event -> myController.setLanguage(comboBox.getValue()));
	}

	@Override
	protected Node getNodeForBox(String item) {
		return new Label(NO_NODE_FOR_BOX);
	}

}
