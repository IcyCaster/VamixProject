package vamixProject.optionPanels;

import javax.swing.JPanel;

/**
 * This is used from assignment three. It was used to set up and execute a command to the 
 * command line through bash.
 * 
 * @author Chester and Frankie
 *
 */
@SuppressWarnings("serial")
public class BashCommandPanel extends JPanel {

	public static Process runBashCommand(String cmd) throws Exception {
		ProcessBuilder s = new ProcessBuilder("/bin/bash", "-c",cmd);
		Process sProcess = s.start();
		return sProcess;
	}
}
