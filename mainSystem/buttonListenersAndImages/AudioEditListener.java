package vamixProject.mainSystem.buttonListenersAndImages;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Creates the mouse over effect on the AudioEdit button 
 * which swaps between the roll over icon and the text.
 * @author chester
 */
public class AudioEditListener extends MouseAdapter {

	private URL _audioURL = getClass().getResource("buttonImages/AudioEdit.png");
	private JButton _audioEditButton;

	// Creates the listener and references the button.
	public AudioEditListener(JButton audioEditButton) {
		_audioEditButton = audioEditButton;
	}

	// When the mouse is over the button show the icon.	
	public void mouseEntered(MouseEvent evt) {
		ImageIcon icon = new ImageIcon(_audioURL);
		_audioEditButton.setText("");
		_audioEditButton.setIcon(icon);
	}

	// When the mouse is not over the button show the text.
	public void mouseExited(MouseEvent evt) {
		ImageIcon icon = new ImageIcon("");
		_audioEditButton.setText("<html><center>Edit the<br><u>A</u>udio</center></html>");
		_audioEditButton.setIcon(icon);
	}
}