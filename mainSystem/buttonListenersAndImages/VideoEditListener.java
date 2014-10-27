package vamixProject.mainSystem.buttonListenersAndImages;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Creates the mouse over effect on the VideoEdit button 
 * which swaps between the roll over icon and the text.
 * @author chester
 */
public class VideoEditListener extends MouseAdapter{

	private URL _videoURL = getClass().getResource("buttonImages/VideoEdit.png");
	private JButton _videoEditButton;

	// Creates the listener and references the button.
	public VideoEditListener(JButton videoEditButton) {
		_videoEditButton = videoEditButton;
	}

	// When the mouse is over the button show the icon.
	public void mouseEntered(MouseEvent evt) {
		ImageIcon icon = new ImageIcon(_videoURL);
		_videoEditButton.setText("");
		_videoEditButton.setIcon(icon);
	}

	// When the mouse is not over the button show the text.
	public void mouseExited(MouseEvent evt) {
		ImageIcon icon = new ImageIcon("");
		_videoEditButton.setText("<html><center>Edit the<br><u>V</u>ideo</center></html>");
		_videoEditButton.setIcon(icon);
	}
}