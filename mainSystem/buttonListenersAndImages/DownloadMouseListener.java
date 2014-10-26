package vamixProject.mainSystem.buttonListenersAndImages;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class DownloadMouseListener extends MouseAdapter{
	
	private URL _downloadURL =  getClass().getResource("buttonImages/Download.png");
	private JButton _downloadButton;

	public DownloadMouseListener(JButton downloadButton) {
		_downloadButton = downloadButton;
	}
	public void mouseEntered(MouseEvent evt)
	{
		ImageIcon icon = new ImageIcon(_downloadURL);
		_downloadButton.setText("");
		_downloadButton.setIcon(icon);
	}
	public void mouseExited(MouseEvent evt)
	{
		ImageIcon icon = new ImageIcon("");
		_downloadButton.setText("<html><center><u>D</u>ownload<br>Media</center></html>");
		_downloadButton.setIcon(icon);
	}
	
}
