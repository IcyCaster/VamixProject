package vamixProject.mainSystem.buttonListenersAndImages;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Creates the mouse over effect on the AdjustSpeed button 
 * which swaps between the roll over icon and the text.
 * @author chester
 */
public class AdjustSpeedListener extends MouseAdapter{

	private URL _speedURL = getClass().getResource("buttonImages/VideoEdit.png");
	private JButton _adjustSpeedButton;

	// Creates the listener and references the button.
	public AdjustSpeedListener(JButton adjustSpeedButton) {
		_adjustSpeedButton = adjustSpeedButton;
	}

	// When the mouse is over the button show the icon.	
	public void mouseEntered(MouseEvent evt) {
		ImageIcon icon = new ImageIcon(_speedURL);
		_adjustSpeedButton.setText("");
		_adjustSpeedButton.setIcon(icon);
	}

	// When the mouse is not over the button show the text.
	public void mouseExited(MouseEvent evt) {
		ImageIcon icon = new ImageIcon("");
		_adjustSpeedButton.setText("<html><center>Ad<u>j</u>ust the<br>Speed</center></html>");
		_adjustSpeedButton.setIcon(icon);
	}
}