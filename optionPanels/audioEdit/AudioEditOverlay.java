package vamixProject.optionPanels.audioEdit;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import vamixProject.mainSystem.MainFrame;
import vamixProject.optionPanels.BashCommandPanel;


/**
 * 
 * Used from Assignment 3 with help from my partner. Was tweaked to suit the final by me.
 * A class that performs the Overlay/Replace audio functionality, as well as containing all the 
 * GUI components for said task
 * @author Chester Booker and Frankie Lam 
 *
 */
public class AudioEditOverlay extends BashCommandPanel {

	// Makes all the GUI fields.
	private File _playingFile;
	private JButton _overlayButton;
	private JTextField _outputName;
	private JTextField _chosenName;
	private JButton _cancelButton;
	private JButton _chooserButton;
	private JButton _replaceButton;
	private JProgressBar _progressbar;

	private JFileChooser _chooser;
	private File _chosenAudio;
	private JLabel _titleLabel;
	private editWorker _worker;	

	public AudioEditOverlay() {

		//Initialises the fields and dimension of the panel
		this.setPreferredSize(new Dimension(600,100));
		this.setLayout(new FlowLayout());
		_titleLabel = new JLabel("                                                                                                                                Replace/Overlay                                                                                                                                ");
		_titleLabel.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		_overlayButton = new JButton("Overlay audio");
		_replaceButton = new JButton("Replace audio");
		_outputName = new JTextField("Enter output name");
		_chosenName = new JTextField();
		_chooser = new JFileChooser();
		_cancelButton = new JButton("Cancel");
		_chooserButton = new JButton("Choose audio");
		_progressbar = new JProgressBar();

		//Add ActionListeners and sets sizes of the components 
		_chosenName.setEnabled(false);
		_chooserButton.addActionListener(new chooserHandler());
		_progressbar.setPreferredSize(new Dimension(200,20));
		_outputName.setPreferredSize(new Dimension(150,30));
		_chosenName.setPreferredSize(new Dimension(150,30));
		_overlayButton.addActionListener(new overlayHandler());
		_replaceButton.addActionListener(new replaceHandler());
		_cancelButton.addActionListener(new cancelHandler());

		//Adds the components
		add(_titleLabel);
		add(_chooserButton);
		add(_chosenName);
		add(_outputName);
		add(_overlayButton);
		add(_replaceButton);
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
			_replaceButton.setEnabled(true);
			_overlayButton.setEnabled(true);
			_outputName.setEnabled(true);
		}
		else {
			_progressbar.setIndeterminate(true);
			_overlayButton.setEnabled(false);
			_outputName.setEnabled(false);
			_replaceButton.setEnabled(false);
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
			_replaceButton.setEnabled(true);
			_overlayButton.setEnabled(true);
			_outputName.setEnabled(true);
		}
		else {
			_cancelButton.setEnabled(false);
			_overlayButton.setEnabled(false);
			_outputName.setEnabled(false);
			_replaceButton.setEnabled(false);
		}

	}
	/**
	 * 
	 * Made by my partner.
	 * The Actionlistener for the filechooser, which opens the filechooser and checks 
	 * for valid chosen file
	 * 
	 * @author frankie
	 *
	 */
	class chooserHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			//Opens the filechooser
			int returnVal=_chooser.showOpenDialog(_chooserButton);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File audio = _chooser.getSelectedFile();
				try {
					//Check if file is valid or not, and if it isn't then complain
					Process checkFile= BashCommandPanel.runBashCommand("file -ib "+"\""+audio.getPath()+"\""+" | grep \"mpeg\\|octet-stream\"");
					checkFile.waitFor();
					if (checkFile.exitValue()!=0)
						JOptionPane.showMessageDialog(_chosenName, "Please select an audio file");
					else{
						//Set the fields based on the audio
						_chosenAudio = audio;
						_chosenName.setText(_chosenAudio.getPath());
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 
	 * Made by my partner.
	 * The ActionListener for the replace button, including the functionality for checking
	 * if overwriting is needed
	 * @author frankie
	 *
	 */
	class replaceHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				//Check if output file already exists, if yes then it asks whether or not the
				//user wants to overwrite
				Process oProcess=runBashCommand("[ -e "+"\""+_outputName.getText()+".mp4"+"\""+" ]");
				oProcess.waitFor();
				if (oProcess.exitValue()!=0){
					if(_chosenAudio!=null){

						//Disables the VideoEditExtract panel as well as local buttons other than _cancelButton
						MainFrame.getInstance().toggleExtract(false);
						toggleButtons(false);

						//Create a new SwingWorker and execute the task on that
						_worker = new editWorker(false);
						_worker.execute();
					}
				}
				//Performs the overwrite functionality
				else if(oProcess.exitValue()==0 && _outputName.getText()!=null) {
					Object[] options = {"Override", "Cancel"};

					//Opens an OptionPane to ask the user for input on whether or not to overwrite
					int selection = JOptionPane.showOptionDialog(_outputName,
							"File with this name already exists","Error ecountered",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
					if(selection == JOptionPane.YES_OPTION){

						//Disables the VideoEditExtract panel as well as local buttons other than _cancelButton
						MainFrame.getInstance().toggleExtract(false);
						toggleButtons(false);

						//Create a new SwingWorker and execute the task on that
						_worker=new editWorker(false);
						_worker.execute();
					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	/**
	 * 
	 * Made by my partner.
	 * The ActionListener for the overlay button, including the functionality for checking
	 * if overwriting is needed
	 * @author frankie
	 *
	 */
	class overlayHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				//Check if output file already exists, if yes then it asks whether or not the
				//user wants to overwrite
				Process oProcess=runBashCommand("[ -e "+"\""+_outputName.getText()+".mp4"+"\""+" ]");
				oProcess.waitFor();
				if (oProcess.exitValue()!=0){
					if(_chosenAudio!=null){

						//Disables the VideoEditExtract panel as well as local buttons other than _cancelButton
						MainFrame.getInstance().toggleExtract(false);
						toggleButtons(false);

						//Create a new SwingWorker and execute the task on that
						_worker =new editWorker(true);
						_worker.execute();
					}
				}
				//Performs the overwrite functionality
				else if(oProcess.exitValue()==0 && _outputName.getText()!=null) {
					Object[] options = {"Override", "Cancel"};

					//Create a JOptionPane to get user input on whether or not to overwrite
					int selection = JOptionPane.showOptionDialog(_outputName,
							"File with this name already exists","Error ecountered",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
					if(selection == JOptionPane.YES_OPTION){

						//Disables the VideoEditExtract panel as well as local buttons other than _cancelButton
						MainFrame.getInstance().toggleExtract(false);
						toggleButtons(false);

						//Create a new SwingWorker and execute the task on that
						_worker=new editWorker(true);
						_worker.execute();
					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
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
			if (_worker!=null){
				_worker.cancelWork();
			}
		}
	}

	/**
	 * The SwingWorker that executes the overlay and replace audio task
	 * @author frankie
	 *
	 */
	class editWorker extends SwingWorker<Void,Integer>{

		private Process _toDoProcess;
		private boolean _isOverlay;

		/**
		 * isOverlay specifies the task for the worker to perform, true for overlay, false for replacement
		 * @param isOverlay
		 */
		public editWorker(boolean isOverlay){
			_isOverlay=isOverlay;
		}

		/**
		 * Uses _isOverlay to determine the task to be performed, if _isOverlay is true, then it will
		 * perform overlay, otherwise it will perform replacement 
		 */
		@Override
		protected Void doInBackground()  {
			try {
				//Performs replace action if _isOverlay is false
				if(!_isOverlay){
					_toDoProcess=runBashCommand("avconv -y -i /"+_chosenAudio.getPath()+ " -i /"+_playingFile.getPath()+ " -c copy "+ _outputName.getText()+".mp4");
					_toDoProcess.waitFor();
				}
				//Performs overlay action if _isOverlay is true
				else {
					_toDoProcess=runBashCommand("avconv -y -i /"+_chosenAudio.getPath()+ " -i /"+_playingFile.getPath()+ " -filter_complex amix=inputs=2:duration=longest:dropout_transition=3 -strict experimental "+ _outputName.getText()+".mp4");
					_toDoProcess.waitFor();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void done(){
			//Enables the VideoEditExtract panel as well as local buttons
			MainFrame.getInstance().toggleExtract(true);
			toggleButtons(true);
			//Show messages depending on the exit status of the process
			if(_toDoProcess.exitValue()==0){
				JOptionPane.showMessageDialog(_overlayButton, "Operation successful");
			}
			else {JOptionPane.showMessageDialog(_overlayButton, "Operation failed");}
		}

		/**
		 * Destroys process and sets the Overlay and buttons to be active again
		 */
		public void cancelWork(){

			if (_toDoProcess!=null){
				_toDoProcess.destroy();
				MainFrame.getInstance().toggleExtract(true);
				toggleButtons(true);
			}
		}
	}
}