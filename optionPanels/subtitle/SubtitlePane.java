package vamixProject.optionPanels.subtitle;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import vamixProject.mainSystem.MainFrame;
import vamixProject.optionPanels.BashCommandPanel;


public class SubtitlePane extends BashCommandPanel {

	// Sets up GUI and fields
	private JPanel _optionsPanel;
	private JScrollPane _editSubtitleScrollPanel;

	private JTextArea _editableSubtitleDisplay;

	private JPanel _topSection;
	private JPanel _midSection;
	private JPanel _bottomSection;
	private JPanel _progressPanel;
	private JProgressBar _progressBar;

	private JPanel _selectPanel;
	private JButton _selectButton;
	private JTextField _subtitlePathDisplay;
	private JPanel _loadPanel;
	private JButton _loadSubButton;

	private JComboBox _subSelectionBox;

	private JPanel _bottomButtonPanel;
	private JButton _addSubtitleButton;
	private JButton _removeSubtitleButton;
	private JButton _cancelButton;

	private String _userHome = System.getProperty("user.home");
	private File _tempSubFile = new File(_userHome + "/tempSubFile.srt");

	private String _videoFilePath;
	private JFileChooser _subtitleChooser = new JFileChooser();
	private File _subtitleFile = null;

	private AddRemoveSubtitleWorker _addRemoveSubtitle = null;
	private SubtitleBoxWorker _boxWorker = null;

	public SubtitlePane() {

		// Adds a boarder
		setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		setBorder(BorderFactory.createEtchedBorder(BevelBorder.RAISED));

		_optionsPanel = new JPanel();
		_optionsPanel.setLayout(new BoxLayout(_optionsPanel, BoxLayout.PAGE_AXIS));
		_optionsPanel.setPreferredSize(new Dimension(560,250));

		_editableSubtitleDisplay = new JTextArea("1 \n00:00:00,000 --> 00:00:01,000\n -Subtitle Text Here-");
		_editSubtitleScrollPanel = new JScrollPane(_editableSubtitleDisplay);
		_editSubtitleScrollPanel.setPreferredSize(new Dimension(305,245));
		Border editableSubsTitle = BorderFactory.createTitledBorder("Editable Subtitles");
		_editSubtitleScrollPanel.setBorder(editableSubsTitle);

		_topSection = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		_topSection.setPreferredSize(new Dimension(560,100));

		_midSection = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		_midSection.setPreferredSize(new Dimension(560,50));

		_bottomSection = new JPanel();
		_bottomSection.setSize(new Dimension(560,50));

		_progressPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		_progressPanel.setPreferredSize(new Dimension(560,20));
		_progressBar = new JProgressBar();
		_progressBar.setPreferredSize(new Dimension(560,20));
		_progressBar.setStringPainted(true);
		_progressBar.setIndeterminate(true);
		_progressBar.setVisible(false);


		_selectPanel = new JPanel();
		_selectPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		_selectPanel.setPreferredSize(new Dimension(350,100));

		_selectButton = new JButton("Select a Subtitle File:");
		_selectButton.setFont(new Font("Ubuntu", Font.BOLD, 16));
		_selectButton.setPreferredSize(new Dimension(350,60));

		_subtitlePathDisplay = new JTextField();
		_subtitlePathDisplay.setPreferredSize(new Dimension(350,40));
		_subtitlePathDisplay.setText("No Current Subtitle file selected!");
		_subtitlePathDisplay.setEnabled(false);  

		_loadPanel = new JPanel(new BorderLayout(0,0));
		_loadPanel.setPreferredSize(new Dimension(210,100));

		_loadSubButton = new JButton("<html><center>Edit Selected<br/>Subtitle File:</center></html>");
		_loadSubButton.setFont(new Font("Ubuntu", Font.BOLD, 16));
		_loadSubButton.setSize(new Dimension(75,75));

		String[] options = new String[3];
		options[0] = ("-Select-");
		options[1] = ("Selected File");
		options[2] = ("Editable Box");
		_subSelectionBox = new JComboBox(options);
		Border comboBox = BorderFactory.createTitledBorder("Add Subtitle From:");
		_subSelectionBox.setBorder(comboBox);
		_subSelectionBox.setPreferredSize(new Dimension(210, 50));	

		_bottomButtonPanel = new JPanel(new GridLayout(1,3,0,0));
		_bottomButtonPanel.setPreferredSize(new Dimension(410, 40));

		_addSubtitleButton = new JButton("Add Subtitle!");
		_removeSubtitleButton = new JButton("Remove Subtitle!");
		_cancelButton = new JButton("Cancel!");
		_cancelButton.setEnabled(false);

		add(_optionsPanel);
		add(_editSubtitleScrollPanel);

		_optionsPanel.add(_topSection);
		_optionsPanel.add(_midSection);
		_optionsPanel.add(_bottomSection);
		_optionsPanel.add(_progressPanel);
		_progressPanel.add(_progressBar);

		_topSection.add(_selectPanel);
		_selectPanel.add(_selectButton, BorderLayout.CENTER);
		_selectPanel.add(_subtitlePathDisplay);
		_topSection.add(_loadPanel);
		_loadPanel.add(_loadSubButton, BorderLayout.CENTER);

		_midSection.add(_subSelectionBox, BorderLayout.CENTER);

		_bottomSection.add(_bottomButtonPanel);

		_bottomButtonPanel.add(_addSubtitleButton);
		_bottomButtonPanel.add(_removeSubtitleButton);
		_bottomButtonPanel.add(_cancelButton);


		_selectButton.addActionListener(new chooserHandler());

		// Loads the selected subtitle file.
		_loadSubButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(_subtitleFile != null) {
					_loadSubButton.setEnabled(false);
					_progressBar.setIndeterminate(true);
					_progressBar.setVisible(true);
					_progressBar.setString("Loading subtitle...");
					_progressBar.validate();
					_cancelButton.setEnabled(true);
					_cancelButton.validate();
					
					_editableSubtitleDisplay.setText("");
					_boxWorker = new SubtitleBoxWorker();
					_boxWorker.execute();
				}
				else {
					JOptionPane.showMessageDialog(new JFrame(), "No Subtitle File Selected!", "ERROR", JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		// The cancel button for canceling removing/adding subtitles.
		_cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(_addRemoveSubtitle != null) {
					_addRemoveSubtitle.cancelWork();
				}
				if(_boxWorker != null) {
					_boxWorker.cancel(true);
				}
				if(!_loadSubButton.isEnabled()) {
					_loadSubButton.setEnabled(true);
				}


			}
		});

		// Removing subtitles.
		_removeSubtitleButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Makes sure there is a video to add text to.
				if(MainFrame.getInstance().getVideoPlayer().getVideo().getVideoOutputs() < 1) {
					JOptionPane.showMessageDialog(new JFrame(), "No Video Selected!", "ERROR", JOptionPane.WARNING_MESSAGE);
				}
				else {

					_videoFilePath = MainFrame.getInstance().getVideoFile().getPath();
					_progressBar.setIndeterminate(true);
					_progressBar.setVisible(true);
					_progressBar.setString("Removing subtitle...");
					_progressBar.validate();
					_cancelButton.setEnabled(true);
					_cancelButton.validate();

					_addRemoveSubtitle = new AddRemoveSubtitleWorker();
					_addRemoveSubtitle.isRemove = true;
					_addRemoveSubtitle._isEditableBox = false;
					_addRemoveSubtitle.execute();
				}
			}
		});

		// Adding subtitles.
		_addSubtitleButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Makes sure there is a video to add text to.
				if(MainFrame.getInstance().getVideoPlayer().getVideo().getVideoOutputs() < 1) {
					JOptionPane.showMessageDialog(new JFrame(), "No Video Selected!", "ERROR", JOptionPane.WARNING_MESSAGE);
				}
				else {

					_videoFilePath = MainFrame.getInstance().getVideoFile().getPath();

					// Decides where to get the subtitles from.
					if(!_subSelectionBox.getSelectedItem().equals("-Select-")) {
						
						if(_subSelectionBox.getSelectedItem().equals("Selected File")) {
							
							// Makes sure there is a selected file
							if(_subtitleFile != null) {

								_progressBar.setIndeterminate(true);
								_progressBar.setVisible(true);
								_progressBar.setString("Adding subtitle...");
								_progressBar.validate();
								_cancelButton.setEnabled(true);
								_cancelButton.validate();

								_addRemoveSubtitle = new AddRemoveSubtitleWorker();
								_addRemoveSubtitle.isRemove = false;
								_addRemoveSubtitle._isEditableBox = false;
								_addRemoveSubtitle.execute();

							}
							else {
								JOptionPane.showMessageDialog(new JFrame(), "No Subtitle File Selected!", "ERROR", JOptionPane.WARNING_MESSAGE);
							}
						}
						else if (_subSelectionBox.getSelectedItem().equals("Editable Box")) {
							if(!_editableSubtitleDisplay.getText().equals("")) {

								_progressBar.setIndeterminate(true);
								_progressBar.setVisible(true);
								_progressBar.setString("Adding subtitle...");
								_progressBar.validate();
								_cancelButton.setEnabled(true);
								_cancelButton.validate();

								_addRemoveSubtitle = new AddRemoveSubtitleWorker();
								_addRemoveSubtitle.isRemove = false;
								_addRemoveSubtitle._isEditableBox = true;
								_addRemoveSubtitle.execute();

							}
							else {
								JOptionPane.showMessageDialog(new JFrame(), "Empty Editable Subtitle Box!", "ERROR", JOptionPane.WARNING_MESSAGE);
							}
						}
					}	
					else {
						JOptionPane.showMessageDialog(new JFrame(), "Select where the subtitles come from!", "ERROR", JOptionPane.WARNING_MESSAGE);
					}
				}	
			}
		});
	}

	class chooserHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {

			int returnVal = _subtitleChooser.showOpenDialog(new JPanel());

			if (returnVal == JFileChooser.APPROVE_OPTION) {

				_subtitleFile = _subtitleChooser.getSelectedFile();

				try {
					//Check if file is valid or not, and if it isn't then complain
					Process checkFile = BashCommandPanel.runBashCommand("file -ib "+"\"" + _subtitleFile.getPath() + "\"" + " | grep \"text\"");
					checkFile.waitFor();
					if (checkFile.exitValue() != 0) {
						JOptionPane.showMessageDialog(new JPanel(), "Please select a subtitle file (.srt)");
					}
					else{
						_subtitlePathDisplay.setText(_subtitleFile.getPath());

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	class AddRemoveSubtitleWorker extends SwingWorker<Void, Void> {

		boolean _isEditableBox = false;
		boolean isRemove = false;
		boolean isCancelled = false;
		Process sProcess;
		int _returnValue;

		@Override
		protected Void doInBackground() throws Exception {
			try {
				// Remove any existing subtitles.
				sProcess = runBashCommand("avconv -i " + _videoFilePath + " -map 0:0 -map 0:1 -vcodec copy -acodec libmp3lame " + _videoFilePath + ".withoutSubtitles.mkv");
				
				sProcess.waitFor();

				// Determines if only wanting subtitles removed.
				if(!isRemove) {

					// Add Subtitles from file.
					if(!_isEditableBox) {
						sProcess = runBashCommand("avconv -i " + _videoFilePath + ".withoutSubtitles.mkv" + " -i " + _subtitleFile.getPath() + 
								" -vcodec h264 -acodec ac3 -scodec ass -metadata:s:s:0 language=eng " + _videoFilePath + ".withoutSubtitles.mkv.fromFile.mkv");
						
						
						sProcess.waitFor();
						_returnValue = sProcess.exitValue();
						
						System.out.println(_returnValue);

						
					}
					// Add Subtitles from edit box.
					else {
						// Writes the relevant information to the log file.
						_tempSubFile.createNewFile();
						PrintWriter writer;
						try {
							writer = new PrintWriter(_tempSubFile, "UTF-8");
							String[] lines = _editableSubtitleDisplay.getText().split("\\n"); 
							for(int i = 0; i < lines.length; i++) {
								writer.println(lines[i]);
							}		
							writer.close();
						}
						finally {

						}
						// Add the subtitles.
						sProcess = runBashCommand("avconv -i " + _videoFilePath + ".withoutSubtitles.mkv" + " -i " + _tempSubFile.getPath() + 
								" -vcodec h264 -acodec ac3 -scodec ass -metadata:s:s:0 language=eng " + _videoFilePath + ".withoutSubtitles.mkv.fromBox.mkv");

						
						sProcess.waitFor();
						_returnValue = sProcess.exitValue();

						// Delete temp subtitle file
						_tempSubFile.delete();
					}


					if(!isCancelled && _returnValue == 0) {
						// Removes old video copy.
						sProcess = runBashCommand("rm " + _videoFilePath);
						sProcess = runBashCommand("rm " + _videoFilePath + ".withoutSubtitles.mkv");

						// Changes the name of the new copy to the same as the old copy.
						if(!_isEditableBox) {
							sProcess = runBashCommand("mv " + _videoFilePath + ".withoutSubtitles.mkv.fromFile.mkv " + _videoFilePath);
						}
						else {
							sProcess = runBashCommand("mv " + _videoFilePath + ".withoutSubtitles.mkv.fromBox.mkv " + _videoFilePath);
						}
					}
					else if(_returnValue != 0) {
						sProcess = runBashCommand("rm " + _videoFilePath + ".withoutSubtitles.mkv");
					}
				}
				// Replace old file with subtitles with the new one without.
				else {
					if(!isCancelled) {
						sProcess = runBashCommand("rm " + _videoFilePath);
						sProcess = runBashCommand("mv " + _videoFilePath + ".withoutSubtitles.mkv " + _videoFilePath);
					}
				}
			} finally {

			}
			return null;
		}

		@Override
		protected void done() {
			// Cancel adding text.
			if(isCancelled) {
				_progressBar.setIndeterminate(false);
				_progressBar.setString("...Cancelled!");
				_progressBar.validate();
				if(!isRemove) {
					JOptionPane.showMessageDialog(new JFrame(), "Subtitle Addition Cancelled", "CANCELLED", JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					JOptionPane.showMessageDialog(new JFrame(), "Subtitle Removal Cancelled", "CANCELLED", JOptionPane.INFORMATION_MESSAGE);
				}

			}
			// Error in input.
			else if (_returnValue != 0) {
				_progressBar.setIndeterminate(false);
				_progressBar.setString("...ERROR!");
				_progressBar.validate();
				JOptionPane.showMessageDialog(new JFrame(), "Error adding subtitles. Make sure subtitle input is correct!", "ERROR", JOptionPane.WARNING_MESSAGE);
			}
			// Successful adding text.
			else {
				_progressBar.setIndeterminate(false);
				_progressBar.setString("...Completed!");
				_progressBar.validate();
				if(!isRemove) {
					JOptionPane.showMessageDialog(new JFrame(), "Subtitle Addition Successful", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					JOptionPane.showMessageDialog(new JFrame(), "Subtitle Removal Successful", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
				}

			}

			_progressBar.setVisible(false);
			_cancelButton.setEnabled(false);
			_cancelButton.validate();

			MainFrame.getInstance().getVideoPlayer().getVideoPlayer().getMediaPlayer().playMedia(_videoFilePath);
		}

		public void cancelWork(){
			// Destroys the process
			if (sProcess != null) {
				isCancelled = true;
				sProcess.destroy();

				// Removes generated temp file.
				try {
					sProcess = runBashCommand("rm " + _videoFilePath + ".withoutSubtitles.mkv");
					sProcess = runBashCommand("rm " + _videoFilePath + ".withoutSubtitles.mkv.fromFile.mkv ");
					sProcess = runBashCommand("rm " + _videoFilePath + ".withoutSubtitles.mkv.fromFile.mkv ");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	class SubtitleBoxWorker extends SwingWorker<Void, String> {
		BufferedReader br = null;
		
		@Override
		protected Void doInBackground() throws Exception {

			br = new BufferedReader(new FileReader(_subtitleFile));
			String line = null;
			while ((line = br.readLine()) != null) {
				_editableSubtitleDisplay.setText(_editableSubtitleDisplay.getText() + line + "\n");
				_editableSubtitleDisplay.validate();
			}
			return null;
		}

		@Override
		protected void done() {
			try {
				br.close();
				if(!isCancelled()) {
					_progressBar.setIndeterminate(false);
					_progressBar.setString("...Completed!");
					_progressBar.validate();
					JOptionPane.showMessageDialog(new JFrame(), "Subtitle Load Successful", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					_progressBar.setIndeterminate(false);
					_progressBar.setString("...Cancelled!");
					_progressBar.validate();
					JOptionPane.showMessageDialog(new JFrame(), "Subtitle Load Cancelled", "CANCEL", JOptionPane.INFORMATION_MESSAGE);
				}
				_loadSubButton.setEnabled(true);
				_progressBar.setVisible(false);
				_cancelButton.setEnabled(false);
				_cancelButton.validate();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}