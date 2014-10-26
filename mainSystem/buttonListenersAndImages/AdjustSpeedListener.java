package vamixProject.mainSystem.buttonListenersAndImages;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class AdjustSpeedListener extends MouseAdapter{
	
	private URL _speedURL = getClass().getResource("buttonImages/VideoEdit.png");
	private JButton _adjustSpeedButton;
	
	public AdjustSpeedListener(JButton adjustSpeedButton) {
		_adjustSpeedButton = adjustSpeedButton;
	}
	
	public void mouseEntered(MouseEvent evt)
	{
		ImageIcon icon = new ImageIcon(_speedURL);
		_adjustSpeedButton.setText("");
		_adjustSpeedButton.setIcon(icon);
	}
	public void mouseExited(MouseEvent evt)
	{
		ImageIcon icon = new ImageIcon("");
		_adjustSpeedButton.setText("<html><center>Ad<u>j</u>ust the<br>Speed</center></html>");
		_adjustSpeedButton.setIcon(icon);
	}

}
