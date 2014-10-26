package vamixProject.mainSystem.buttonListenersAndImages;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class AddSubtitleListener extends MouseAdapter{
	private URL _subURL = getClass().getResource("buttonImages/Subtitles.png");
	private JButton _addSubtitlesButton;
	
	public AddSubtitleListener(JButton addSubtitlesButton) {
		_addSubtitlesButton = addSubtitlesButton;
	}
	
	public void mouseEntered(MouseEvent evt)
	{
		ImageIcon icon = new ImageIcon(_subURL);
		_addSubtitlesButton.setText("");
		_addSubtitlesButton.setIcon(icon);
	}
	public void mouseExited(MouseEvent evt)
	{
		ImageIcon icon = new ImageIcon("");
		_addSubtitlesButton.setText("<html><center>Add<br>S<u>u</u>btitles</center></html>");
		_addSubtitlesButton.setIcon(icon);
	}

}
