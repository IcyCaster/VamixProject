package Vamix206;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;

/**
 * Round button was used from a post made on "http://stackoverflow.com/questions/6735891/creating-custom-jbutton-from-images-containing-transparent-pixels"
 * Using these round buttons has enhanced our design to look more aesthetically pleasing. 
 * 
 * @author paranoid-android
 */

public class RoundButton extends JButton {

	// Constructors
	public RoundButton() {
		this(null, null);
	}

	public RoundButton(Icon icon) {
		this(null, icon);
	}

	public RoundButton(String text) {
		this(text, null);
	}

	public RoundButton(Action a) {
		this();
		setAction(a);
	}

	public RoundButton(String text, Icon icon) {
		setModel(new DefaultButtonModel());
		init(text, icon);
		if(icon == null) {
			return;
		}
		setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
		setContentAreaFilled(false);
		setFocusPainted(false);
		initShape();
	}

	protected Shape shape, base;

	protected void initShape() {
		if(!getBounds().equals(base)) {
			Dimension s = getPreferredSize();
			base = getBounds();
			shape = new Ellipse2D.Float(0, 0, s.width, s.height);
		}
	}

	@Override 
	public Dimension getPreferredSize() {
		Icon icon = getIcon();
		Insets i = getInsets();
		int iw = Math.max(icon.getIconWidth(), icon.getIconHeight());
		return new Dimension(iw+i.right+i.left, iw+i.top+i.bottom);
	}

	@Override 
	public boolean contains(int x, int y) {
		initShape();
		return shape.contains(x, y);
	}
}