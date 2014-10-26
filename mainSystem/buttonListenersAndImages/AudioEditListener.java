package vamixProject.mainSystem.buttonListenersAndImages;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class AudioEditListener extends MouseAdapter {
	
	private URL _audioURL = getClass().getResource("buttonImages/AudioEdit.png");
	private JButton _audioEditButton;
	
	public AudioEditListener(JButton audioEditButton) {
		_audioEditButton = audioEditButton;
	}
	
	public void mouseEntered(MouseEvent evt)
	{
		ImageIcon icon = new ImageIcon(_audioURL);
		_audioEditButton.setText("");
		_audioEditButton.setIcon(icon);
	}
	public void mouseExited(MouseEvent evt)
	{
		ImageIcon icon = new ImageIcon("");
		_audioEditButton.setText("<html><center>Edit the<br><u>A</u>udio</center></html>");
		_audioEditButton.setIcon(icon);
	}

}
