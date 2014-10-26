package VamixProject;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * This is the main method of our VAMIX project.
 * It starts up the MainFrame singleton and sets the "lookAndFeel"
 * of the system and it's GUI.
 * 
 * @author Chester Booker
 *
 */
public class SystemStartup {

	public static void main(String[] args) {

		try {
			// Sets the look-and feel of the system.
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");

		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {

			// Catches all the stack traces.
			e.printStackTrace();
		}

		// Creates the new mainframe.
		MainFrame.getInstance();
		
	}
}
