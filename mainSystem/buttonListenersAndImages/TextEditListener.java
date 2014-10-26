package vamixProject.mainSystem.buttonListenersAndImages;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class TextEditListener extends MouseAdapter{
	
	private URL _textURL = getClass().getResource("buttonImages/EditText.png");
	private JButton _textEditButton;
	
	public TextEditListener(JButton textEditButton) {
		_textEditButton = textEditButton;
	}
	public void mouseEntered(MouseEvent evt)
	{
		ImageIcon icon = new ImageIcon(_textURL);
		_textEditButton.setText("");
		_textEditButton.setIcon(icon);
	}
	public void mouseExited(MouseEvent evt)
	{
		ImageIcon icon = new ImageIcon("");
		_textEditButton.setText("<html><center>Edit the<br><u>T</u>ext</center></html>");
		_textEditButton.setIcon(icon);
	}

}
