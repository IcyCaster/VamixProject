package vamixProject.mainSystem.buttonListenersAndImages;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Creates the mouse over effect on the TextEdit button 
 * which swaps between the roll over icon and the text.
 * @author chester
 */
public class TextEditListener extends MouseAdapter{

	private URL _textURL = getClass().getResource("buttonImages/EditText.png");
	private JButton _textEditButton;

	// Creates the listener and references the button.
	public TextEditListener(JButton textEditButton) {
		_textEditButton = textEditButton;
	}

	// When the mouse is over the button show the icon.
	public void mouseEntered(MouseEvent evt) {
		ImageIcon icon = new ImageIcon(_textURL);
		_textEditButton.setText("");
		_textEditButton.setIcon(icon);
	}

	// When the mouse is not over the button show the text.
	public void mouseExited(MouseEvent evt) {
		ImageIcon icon = new ImageIcon("");
		_textEditButton.setText("<html><center>Edit the<br><u>T</u>ext</center></html>");
		_textEditButton.setIcon(icon);
	}
}