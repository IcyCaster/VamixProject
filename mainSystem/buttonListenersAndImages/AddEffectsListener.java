package vamixProject.mainSystem.buttonListenersAndImages;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class AddEffectsListener extends MouseAdapter{
	
	private URL _effectURL = getClass().getResource("buttonImages/Effects.png");
	private JButton _addEffectsButton;
	
	public AddEffectsListener(JButton addEffectsButton) {
		_addEffectsButton = addEffectsButton;
	}
	
	public void mouseEntered(MouseEvent evt)
	{
		ImageIcon icon = new ImageIcon(_effectURL);
		_addEffectsButton.setText("");
		_addEffectsButton.setIcon(icon);
	}
	public void mouseExited(MouseEvent evt)
	{
		ImageIcon icon = new ImageIcon("");
		_addEffectsButton.setText("<html><center>Add<br><u>E</u>ffects</center></html>");
		_addEffectsButton.setIcon(icon);
	}

}
