package vamixProject.mainSystem;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


/**
 * This is the main method of our VAMIX project.
 * Firstly it sets the look-and-feel of the system.
 * Next it creates a singleton of the system in a new thread.
 * 
 * @author Chester Booker
 *
 */
public class SystemStartup {

	/**
	 * The Main Method
	 */
	public static void main(String[] args) {

		try {
			// Sets the look-and feel of the system.
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");

		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {

			// Catches all the stack traces.
			e.printStackTrace();
		}

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// Creates the new mainframe object since this is the first call to the singleton.
				MainFrame.getInstance();
			}
		});
	}
}
