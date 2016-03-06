package guipackage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import controller.Controller;

/**
 * Create ComboBox to hold interpretable languages. 
 * @author AnnieTang
 *
 */

public class GUIObjectComboBoxLanguages extends GUIObjectComboBox {

	public GUIObjectComboBoxLanguages(GUICanvas canvas, ResourceBundle myResources, Controller myController,
			String promptText, GUICommandLine cLine) {
		super(canvas, myResources, myController, promptText, cLine);
	}

	@Override
	protected List<String> optionsList() {
		return new ArrayList<String>(Arrays.asList(myResources.getString("Languages").split(" ")));
	}
	
	@Override
	protected void setButtonAction(){
		comboButton.setOnAction(event -> myController.setLanguage(comboBox.getValue()));
	}

	@Override
	protected void setCellFactory() {
	}

}