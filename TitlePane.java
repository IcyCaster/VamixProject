package VamixProject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/**
 * The title pane hold the aesthetically pleasing image of VAMIX which gives the users
 * a nice visual representation of the simplicity of using such a complex trailer editor.
 * 
 * @author Chester Booker and Frankie Lam
 */

public class TitlePane extends BashCommandPanel {

	private URL _vamixImageURL = getClass().getResource("Title1.png");
	private ImageIcon _vamixImage = new ImageIcon(_vamixImageURL);
	private JLabel _vamixTitle = new JLabel("", _vamixImage, JLabel.CENTER);

	public TitlePane() {

		setBorder(BorderFactory.createEtchedBorder(BevelBorder.RAISED));
		// Loads the VAMIX image and puts it onto the pane.
		setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
		_vamixTitle.setPreferredSize(new Dimension(865,245));
		add(_vamixTitle);
	}
}
