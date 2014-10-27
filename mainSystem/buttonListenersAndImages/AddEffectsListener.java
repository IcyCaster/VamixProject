package vamixProject.mainSystem.buttonListenersAndImages;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Creates the mouse over effect on the AddEffect button 
 * which swaps between the roll over icon and the text.
 * @author chester
 */
public class AddEffectsListener extends MouseAdapter {

	private URL _effectURL = getClass().getResource("buttonImages/Effects.png");
	private JButton _addEffectsButton;

	// Creates the listener and references the button.
	public AddEffectsListener(JButton addEffectsButton) {
		_addEffectsButton = addEffectsButton;
	}

	// When the mouse is over the button show the icon.
	public void mouseEntered(MouseEvent evt) {
		ImageIcon icon = new ImageIcon(_effectURL);
		_addEffectsButton.setText("");
		_addEffectsButton.setIcon(icon);
	}

	// When the mouse is not over the button show the text.
	public void mouseExited(MouseEvent evt) {
		ImageIcon icon = new ImageIcon("");
		_addEffectsButton.setText("<html><center>Add<br><u>E</u>ffects</center></html>");
		_addEffectsButton.setIcon(icon);
	}
}