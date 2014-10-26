package vamixProject.mainSystem.buttonListenersAndImages;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class SelectListener extends MouseAdapter{

	private URL _selectURL = getClass().getResource("buttonImages/Select.png");
	private JButton _selectionButton;
	
	public SelectListener(JButton selectionButton) {
		_selectionButton = selectionButton;
	}
	public void mouseEntered(MouseEvent evt)
	{
		ImageIcon icon = new ImageIcon(_selectURL);
		_selectionButton.setText("");
		_selectionButton.setIcon(icon);
	}
	public void mouseExited(MouseEvent evt)
	{
		ImageIcon icon = new ImageIcon("");
		_selectionButton.setText("<html><center><u>S</u>elect<br>Media</center></html>");
		_selectionButton.setIcon(icon);
	}
}
