package vamixProject.mainSystem.buttonListenersAndImages;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Creates the mouse over effect on the Download button 
 * which swaps between the roll over icon and the text.
 * @author chester
 */
public class DownloadListener extends MouseAdapter{

	private URL _downloadURL =  getClass().getResource("buttonImages/Download.png");
	private JButton _downloadButton;

	// Creates the listener and references the button.
	public DownloadListener(JButton downloadButton) {
		_downloadButton = downloadButton;
	}

	// When the mouse is over the button show the icon.
	public void mouseEntered(MouseEvent evt) {
		ImageIcon icon = new ImageIcon(_downloadURL);
		_downloadButton.setText("");
		_downloadButton.setIcon(icon);
	}

	// When the mouse is not over the button show the text.
	public void mouseExited(MouseEvent evt) {
		ImageIcon icon = new ImageIcon("");
		_downloadButton.setText("<html><center><u>D</u>ownload<br>Media</center></html>");
		_downloadButton.setIcon(icon);
	}
}