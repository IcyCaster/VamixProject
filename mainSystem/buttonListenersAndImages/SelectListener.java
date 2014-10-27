package vamixProject.mainSystem.buttonListenersAndImages;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Creates the mouse over effect on the Select button 
 * which swaps between the roll over icon and the text.
 * @author chester
 */
public class SelectListener extends MouseAdapter{

	private URL _selectURL = getClass().getResource("buttonImages/Select.png");
	private JButton _selectionButton;

	// Creates the listener and references the button.
	public SelectListener(JButton selectionButton) {
		_selectionButton = selectionButton;
	}

	// When the mouse is over the button show the icon.
	public void mouseEntered(MouseEvent evt) {
		ImageIcon icon = new ImageIcon(_selectURL);
		_selectionButton.setText("");
		_selectionButton.setIcon(icon);
	}

	// When the mouse is not over the button show the text.
	public void mouseExited(MouseEvent evt) {
		ImageIcon icon = new ImageIcon("");
		_selectionButton.setText("<html><center><u>S</u>elect<br>Media</center></html>");
		_selectionButton.setIcon(icon);
	}
}