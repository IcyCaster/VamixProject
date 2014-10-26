package vamixProject.mainSystem.buttonListenersAndImages;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class VideoEditListener extends MouseAdapter{
	
	private URL _videoURL = getClass().getResource("buttonImages/VideoEdit.png");
	private JButton _videoEditButton;
	
	public VideoEditListener(JButton videoEditButton) {
		_videoEditButton = videoEditButton;
	}
	
	public void mouseEntered(MouseEvent evt)
	{
		ImageIcon icon = new ImageIcon(_videoURL);
		_videoEditButton.setText("");
		_videoEditButton.setIcon(icon);
	}
	public void mouseExited(MouseEvent evt)
	{
		ImageIcon icon = new ImageIcon("");
		_videoEditButton.setText("<html><center>Edit the<br><u>V</u>ideo</center></html>");
		_videoEditButton.setIcon(icon);
	}

}
