package vamixProject.optionPanels;

import javax.swing.JPanel;

/**
 * This command is the parent of all the panels as it allows them to create bash command lines.
 * @author Chester and Frankie
 */
@SuppressWarnings("serial")
public class BashCommandPanel extends JPanel {

	public static Process runBashCommand(String cmd) throws Exception {
		
		// Builds a process through java and runs command lines through them.
		ProcessBuilder s = new ProcessBuilder("/bin/bash", "-c",cmd);
		Process sProcess = s.start();
		return sProcess;
	}
}