package VamixProject.optionPanels.audioEdit;


import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import VamixProject.optionPanels.BashCommandPanel;
import VamixProject.mainSystem.MainFrame;

/**
 * 
 * Used from Assignment 3 with help from my partner. Was tweaked to suit the final by me.
 * A class that performs the audio extract functionality, as well as containing all the 
 * GUI components for said task
 * @author Chester Booker and Frankie Lam 
 *
 */
public class AudioEditExtract extends BashCommandPanel{

	private JButton _stripAudio;
	private	JButton _cancelButton;
	private	File _playingFile;
	private	JTextField _output;
	private	exWorker _worker;
	private JCheckBox _muteOriginal;
	private JProgressBar _progressbar;
	private JLabel _label;

	public AudioEditExtract(){

		//Set the layout and dimensions of the panel
		setLayout(new FlowLayout());
		this.setPreferredSize(new Dimension(550,130));

		//Initialises components
		_stripAudio = new JButton ("Start Extract");
		_output = new JTextField("Extract Output Name");
		_cancelButton = new JButton("Cancel");
		_muteOriginal = new JCheckBox("Create muted copy of original?");
		_progressbar = new JProgressBar();
		_label= new JLabel("                                                                                                                                      Extract                                                                                                                                        ");
		_label.setFont(new Font("Ubuntu", Font.PLAIN, 16));

		//Sets the preffered sizes and ActionListeners
		_output.setPreferredSize(new Dimension(230,30));
		_stripAudio.addActionListener(new extractHandler());
		_cancelButton.addActionListener(new cancelHandler());
		_progressbar.setPreferredSize(new Dimension(200,20));

		//Adds the components
		add(_label);
		add(_stripAudio);
		add(_output);
		add(_muteOriginal);
		add(_cancelButton);
		add(_progressbar);
	}
	/**
	 * This method sets the playingFile field 
	 * @param playingFile
	 */
	public void update(File playingFile){
		_playingFile=playingFile;
	}

	/**
	 *  Method that toggles the state of the panel; setting the buttons to be disabled 
	 *  except the cancel button and set the progress bar to indeterminate if the parameter is false
	 *  , or doing and opposite if the parameter is true
	 * @param status
	 */
	public void toggleButtons(boolean status){
		if (status){
			_progressbar.setIndeterminate(false);
			_stripAudio.setEnabled(true);
			_output.setEnabled(true);
		}
		else{
			_progressbar.setIndeterminate(true);
			_stripAudio.setEnabled(false);
			_output.setEnabled(false);
		}
	}

	/**
	 * Toggles the activity of buttons, if status is true, all the buttons are turned on
	 * while if the status is false the buttons are turned off
	 * @param status
	 */
	public void toggleOn(boolean status){
		if (status){
			_cancelButton.setEnabled(true);
			_stripAudio.setEnabled(true);
			_output.setEnabled(true);

		}
		else{_cancelButton.setEnabled(false);
		_stripAudio.setEnabled(false);
		_output.setEnabled(false);
		}
	}

	/**
	 * The ActionListener for the cancel button. When the actionperformed is called, it will
	 * call the cancel method on the SwingWorker
	 * @author frankie
	 *
	 */
	class cancelHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			_worker.cancelWork();	
		}
	}

	/**
	 * The ActionListener for the extract button, including the functionality for checking
	 * if overwriting is needed
	 * @author frankie
	 *
	 */
	class extractHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				//Check if output file already exists, if yes then it asks whether or not the
				//user wants to overwrite
				Process oProcess=runBashCommand("[ -e "+"\""+_output.getText()+".mp3"+"\""+" ]");
				oProcess.waitFor();
				if (oProcess.exitValue()!=0){
					if(_playingFile!=null){

						//Disables the VideoEditOverlay panel as well as local buttons other than _cancelButton
						MainFrame.getInstance().toggleOverlay(false);
						toggleButtons(false);

						//Creates and executes the worker
						_worker =new exWorker();
						_worker.execute();
					}
				}
				//Performs the overwrite functionality
				else if(oProcess.exitValue()==0 && _output.getText()!=null) {
					Object[] options = {"Override", "Cancel"};

					//Create a JOptionPane to get user input on whether or not to overwrite
					int selection = JOptionPane.showOptionDialog(_output,
							"File with this name already exists","Error ecountered",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
					if(selection == JOptionPane.YES_OPTION){

						//Disables the VideoEditOverlay panel as well as local buttons other than _cancelButton
						MainFrame.getInstance().toggleOverlay(false);
						toggleButtons(false);

						//Creates and executes the worker
						_worker=new exWorker();
						_worker.execute();
					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * The SwingWorker that executes the extract audio task
	 * @author frankie
	 *
	 */
	class exWorker extends SwingWorker<Void,Integer>{
		Process _toDoProcess;

		/**
		 * Uses _muteOriginal to determine the task to be performed, if _muteOriginal is selected, then it will
		 * create an additional output that is the playing file except without audio
		 */
		@Override
		protected Void doInBackground()  {
			try {
				//Check if there are audio streams in the chosen file, and only perform tasks if there is
				if (MainFrame.getInstance().getVideoPlayer().getVideo().getAudioTrackCount()>=1){

					//Performs the audio extract if _muteOriginal is not selected
					if (!_muteOriginal.isSelected()){
						_toDoProcess=runBashCommand("avconv -y -i "+ "/"+_playingFile.getPath()+ " "+ _output.getText()+".mp3");
						_toDoProcess.waitFor();
					}
					//Performs the audio extract as well as creating an audio-less file if _muteOriginal is selected
					else{
						_toDoProcess=runBashCommand("avconv -y -i "+ "/"+_playingFile.getPath()+ " "+ _output.getText()+".mp3");
						_toDoProcess.waitFor();
						if(_toDoProcess.exitValue()==0){
							_toDoProcess=runBashCommand("avconv -y -i "+ "/"+_playingFile.getPath()+" -c:v copy -strict experimental -an "+_output.getText()+".mp4");
							_toDoProcess.waitFor();
						}
					}
				}
				else{
					//Show JOptionPane to tell user there was no audio streams in the chosen file
					JOptionPane.showMessageDialog(_output, "Error encountered: This video has no audio streams");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void done(){
			//Enables the VideoEditOverlay panel as well as local buttons other than _cancelButton
			MainFrame.getInstance().toggleOverlay(true);
			toggleButtons(true);
			//Show messages depending on the exit status of the process
			if(_toDoProcess.exitValue()==0){
				JOptionPane.showMessageDialog(_output, "Operation successful");
			}
			else {JOptionPane.showMessageDialog(_output, "Operation failed");}
		}

		/**
		 * Destroys process and sets the Overlay panel and buttons to be active again
		 */
		public void cancelWork(){
			if (_toDoProcess!=null){
				MainFrame.getInstance().toggleOverlay(true);
				_toDoProcess.destroy();
				toggleButtons(true);
			}
		}
	}
}