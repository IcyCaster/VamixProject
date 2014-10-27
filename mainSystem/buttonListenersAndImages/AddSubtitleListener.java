package vamixProject.mainSystem.buttonListenersAndImages;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Creates the mouse over effect on the AddSubtitle button 
 * which swaps between the roll over icon and the text.
 * @author chester
 */
public class AddSubtitleListener extends MouseAdapter {

	private URL _subURL = getClass().getResource("buttonImages/Subtitles.png");
	private JButton _addSubtitlesButton;

	// Creates the listener and references the button.
	public AddSubtitleListener(JButton addSubtitlesButton) {
		_addSubtitlesButton = addSubtitlesButton;
	}
	
	// When the mouse is over the button show the icon.
	public void mouseEntered(MouseEvent evt) {
		ImageIcon icon = new ImageIcon(_subURL);
		_addSubtitlesButton.setText("");
		_addSubtitlesButton.setIcon(icon);
	}

	// When the mouse is not over the button show the text.
	public void mouseExited(MouseEvent evt) {
		ImageIcon icon = new ImageIcon("");
		_addSubtitlesButton.setText("<html><center>Add<br>S<u>u</u>btitles</center></html>");
		_addSubtitlesButton.setIcon(icon);
	}
}