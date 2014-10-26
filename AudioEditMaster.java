package VamixProject;

import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/**
 * 
 * Used from Assignment 3 with help from my partner. Was tweaked to suit the final by me.
 * A class that holds a VideoEditOverlay and VideoEditExtract panel 
 * @author Chester Booker and Frankie Lam 
 *
 */
public class AudioEditMaster extends BashCommandPanel {

	// Add both panels.
	AudioEditOverlay _overlay;
	AudioEditExtract _extract;

	public AudioEditMaster(){

		// Creates the pretty boarder.
		setBorder(BorderFactory.createEtchedBorder(BevelBorder.RAISED));

		//Initialises the panels
		_overlay = new AudioEditOverlay();
		_extract = new AudioEditExtract();

		//Adds the panels
		add(_overlay);
		add(_extract);

	}

	/**
	 * Calls the update methods of the overlay and extract panels with the parameter playingFile as the 
	 * arguments for the update methods of the overy and extract panels
	 * @param playingFile
	 */
	public void update(File playingFile){
		_overlay.update(playingFile);
		_extract.update(playingFile);
	}

	/**
	 * Calls the toggleOn method on _overlay, with status as the parameter for toggleOn
	 * @param status
	 */
	public void toggleOverlay(boolean status){
		_overlay.toggleOn(status);
	}

	/**
	 * Calls the toggleOn method on _extract, with status as the parameter for toggleOn
	 * @param status
	 */
	public void toggleExtract(boolean status){
		_extract.toggleOn(status);
	}
}