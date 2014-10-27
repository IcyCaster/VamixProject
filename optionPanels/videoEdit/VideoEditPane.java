package vamixProject.optionPanels.videoEdit;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import vamixProject.mainSystem.MainFrame;
import vamixProject.optionPanels.BashCommandPanel;

/**
 * Used for editing the video in two ways. Either adding fades or 
 * @author chester
 *
 */
@SuppressWarnings("serial")
public class VideoEditPane extends BashCommandPanel {

	// Two main panels
	private JPanel _fadePanel;
	private JPanel _extractPanel;

	// The sections of the fade panel
	private JPanel _fadeTopSection;
	private JPanel _fadeMidSection;
	private JPanel _fadeBottomSection;

	// The sections of the extract panel
	private JPanel _extractTopSection;
	private JPanel _extractMidSection;
	private JPanel _extractBottomSection;
	private JPanel _extractProgressSection;

	// The fade buttons and the timer fields
	private JButton _fadeInButton;
	private JButton _fadeOutButton;
	private JLabel _fadeInLabel;
	private JLabel _fadeOutLabel;
	private JTextField _fadeInTime;
	private JTextField _fadeOutTime;
	private JButton _fadeCancelButton;
	private JProgressBar _fadeProgressBar;

	// The timer labels
	private JLabel _startTimeLabel;
	private JLabel _timeLengthLabel;
	private JLabel _outputNameLabel;
	private JTextField _outputName;
	private JLabel _mp3Label;

	// Combo boxes for the start and length time
	private JComboBox<?> _comboStartHours;
	private JComboBox<?> _comboLengthHours;

	private JComboBox<?> _comboStartMinutes;
	private JComboBox<?> _comboLengthMinutes;

	private JComboBox<?> _comboStartSeconds;
	private JComboBox<?> _comboLengthSeconds;

	private JButton _extractButton;
	private JButton _extractCancelButton;
	private JProgressBar _extractProgressBar;

	private String _filePath;
	
	// The add and extract workers
	private AddFadeWorker _fadeWorker;
	private ExtractWorker _extractWorker;

	// Locations where the file will be saved.
	private String _userHome = System.getProperty("user.home");

	public VideoEditPane() {

		setLayout(new GridLayout(1,2));
		setBorder(BorderFactory.createEtchedBorder(BevelBorder.RAISED));

		_fadePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		Border fadeBorder = BorderFactory.createTitledBorder("Add Fade to Video");
		_fadePanel.setBorder(fadeBorder);

		_extractPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		Border extractBorder = BorderFactory.createTitledBorder("Extract Video");
		_extractPanel.setBorder(extractBorder);

		JPanel bp11 = new JPanel();
		bp11.setPreferredSize(new Dimension(400,65));

		_fadeTopSection = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		_fadeTopSection.setPreferredSize(new Dimension(400,40));

		_fadeMidSection = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		_fadeMidSection.setPreferredSize(new Dimension(400,40));

		_fadeBottomSection = new JPanel();
		_fadeBottomSection.setLayout(new BoxLayout(_fadeBottomSection, BoxLayout.LINE_AXIS));
		_fadeBottomSection.setPreferredSize(new Dimension(395,40));

		JPanel bp12 = new JPanel();
		bp12.setPreferredSize(new Dimension(400,25));

		_extractTopSection = new JPanel();
		_extractTopSection.setPreferredSize(new Dimension(400,40));

		_extractMidSection = new JPanel();
		_extractMidSection.setPreferredSize(new Dimension(400,40));

		_extractBottomSection = new JPanel();
		_extractBottomSection.setPreferredSize(new Dimension(400,40));

		_extractProgressSection = new JPanel();
		_extractProgressSection.setPreferredSize(new Dimension(400,40));

		// Fade in setting
		_fadeInButton = new JButton("Add Fade-In to beginning!");
		_fadeInButton.setPreferredSize(new Dimension(200,40));
		JPanel bp31 = new JPanel();
		bp31.setPreferredSize(new Dimension(10,40));
		_fadeInLabel = new JLabel("Time Length (secs):");
		_fadeInLabel.setFont(new Font("Ubuntu", Font.PLAIN, 15));
		JPanel bp32 = new JPanel();
		bp32.setPreferredSize(new Dimension(10,40));
		_fadeInTime = new JTextField("10");
		_fadeInTime.setPreferredSize(new Dimension(40,30));

		// Fade out setting
		_fadeOutButton = new JButton("Add Fade-Out to end!");
		_fadeOutButton.setPreferredSize(new Dimension(200,40));
		JPanel bp41 = new JPanel();
		bp41.setPreferredSize(new Dimension(10,40));
		_fadeOutLabel = new JLabel("Time Length (secs):");
		_fadeOutLabel.setFont(new Font("Ubuntu", Font.PLAIN, 15));
		JPanel bp42 = new JPanel();
		bp42.setPreferredSize(new Dimension(10,40));
		_fadeOutTime = new JTextField("10");
		_fadeOutTime.setPreferredSize(new Dimension(40,30));

		// Cancel the fade
		_fadeCancelButton = new JButton("Cancel!");
		_fadeCancelButton.setPreferredSize(new Dimension(150,20));
		_fadeCancelButton.setVisible(false);

		// Progress bar of the fade
		_fadeProgressBar = new JProgressBar();
		_fadeProgressBar.setPreferredSize(new Dimension(150,20));
		_fadeProgressBar.setIndeterminate(true);
		_fadeProgressBar.setVisible(false);
		_fadeProgressBar.setStringPainted(true);

		// The output name of the extract
		_outputNameLabel = new JLabel("Output Name:");
		_outputNameLabel.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		_outputName = new JTextField("-Enter Name Here-");
		_outputName.setPreferredSize(new Dimension(140,30));

		// Shows that .mp4 is appended to the extracted file
		_mp3Label = new JLabel(".mp4");
		_mp3Label.setFont(new Font("Ubuntu", Font.PLAIN, 16));


		JPanel bp5 = new JPanel();
		bp5.setPreferredSize(new Dimension(10,30));

		// Start time for the extract
		_startTimeLabel = new JLabel("Start Time:");
		_startTimeLabel.setFont(new Font("Ubuntu", Font.PLAIN, 16));

		// Time length for the extract
		_timeLengthLabel = new JLabel("Time Length:");
		_timeLengthLabel.setFont(new Font("Ubuntu", Font.PLAIN, 16));

		// Extract cancel button
		_extractButton  = new JButton("Extract!");
		_extractCancelButton  = new JButton("Cancel!");
		_extractCancelButton.setEnabled(false);

		// Extract progress bar
		_extractProgressBar = new JProgressBar();
		_extractProgressBar.setIndeterminate(true);
		_extractProgressBar.setVisible(false);

		// Adding to the ComboBoxes
		String[] hours = new String[101];
		hours[0] = "Hours";
		for (int h = 0; h < 100; h++) {
			if ( h < 10 ) {
				hours[h + 1] = "0" + Integer.toString(h);
			} 
			else {
				hours[h + 1] = Integer.toString(h);
			}
		}
		_comboStartHours = new JComboBox<Object>(hours);
		_comboLengthHours = new JComboBox<Object>(hours);

		String[] minutes = new String[62];
		minutes[0] = "Minutes";
		for (int m = 0; m < 61; m++) {
			if ( m < 10 ) {
				minutes[m + 1] = "0" + Integer.toString(m);
			} 
			else {
				minutes[m + 1] = Integer.toString(m);
			}
		}
		_comboStartMinutes = new JComboBox<Object>(minutes);
		_comboLengthMinutes = new JComboBox<Object>(minutes);

		String[] seconds = new String[62];
		seconds[0] = "Seconds";
		for (int s = 0; s < 61; s++) {
			if ( s < 10 ) {
				seconds[s + 1] = "0" + Integer.toString(s);
			} 
			else {
				seconds[s + 1] = Integer.toString(s);
			}
		}
		_comboStartSeconds = new JComboBox<Object>(seconds);
		_comboLengthSeconds = new JComboBox<Object>(seconds);

		// Accepts only backspace and digits 
		_fadeInTime.addKeyListener(new KeyAdapter() { 
			public void keyTyped(KeyEvent kEvent) { 

				char c = kEvent.getKeyChar(); 

				if((!(Character.isDigit(c))) && (c != '\b') )
				{ 
					kEvent.consume(); 
				} 
			} 
			public void keyReleased(KeyEvent e){} // Blank
			public void keyPressed(KeyEvent e){} // Blank
		});

		// Accepts only backspace and digits 
		_fadeOutTime.addKeyListener(new KeyAdapter() { 
			public void keyTyped(KeyEvent kEvent) { 

				char c = kEvent.getKeyChar(); 

				if((!(Character.isDigit(c))) && (c != '\b') )
				{ 
					kEvent.consume(); 
				} 
			} 
			public void keyReleased(KeyEvent e){} // Blank
			public void keyPressed(KeyEvent e){} // Blank
		});

		add(_fadePanel);
		add(_extractPanel);

		_fadePanel.add(bp11);
		_fadePanel.add(_fadeTopSection);
		_fadePanel.add(_fadeMidSection);
		_fadePanel.add(_fadeBottomSection);

		_fadeTopSection.add(_fadeInButton);
		_fadeTopSection.add(bp31);
		_fadeTopSection.add(_fadeInLabel);
		_fadeTopSection.add(bp32);
		_fadeTopSection.add(_fadeInTime);

		_fadeMidSection.add(_fadeOutButton);
		_fadeMidSection.add(bp41);
		_fadeMidSection.add(_fadeOutLabel);
		_fadeMidSection.add(bp42);
		_fadeMidSection.add(_fadeOutTime);

		_fadeBottomSection.add(_fadeCancelButton);
		_fadeBottomSection.add(_fadeProgressBar);

		_extractPanel.add(bp12);
		_extractPanel.add(_extractTopSection);
		_extractPanel.add(_extractMidSection);
		_extractPanel.add(_extractBottomSection);
		_extractPanel.add(_extractProgressSection);

		_extractTopSection.add(_outputNameLabel);
		_extractTopSection.add(_outputName);
		_extractTopSection.add(_mp3Label);

		_extractMidSection.add(bp5);
		_extractMidSection.add(_startTimeLabel);
		_extractMidSection.add(_comboStartHours);
		_extractMidSection.add(_comboStartMinutes);
		_extractMidSection.add(_comboStartSeconds);

		_extractBottomSection.add(_timeLengthLabel);
		_extractBottomSection.add(_comboLengthHours);
		_extractBottomSection.add(_comboLengthMinutes);
		_extractBottomSection.add(_comboLengthSeconds);

		_extractProgressSection.add(_extractButton);
		_extractProgressSection.add(_extractCancelButton);
		_extractProgressSection.add(_extractProgressBar);

		_fadeInButton.addActionListener(new fadeHandler());
		_fadeOutButton.addActionListener(new fadeHandler());

		// The cancel button for cancelling adding text.
		_extractCancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(_extractWorker != null) {
					_extractWorker.cancel(true);
				}
			}
		});

		// The cancel button for cancelling adding text.
		_fadeCancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(_fadeWorker != null) {
					_fadeWorker.cancelWork();
				}
			}
		});



		_extractButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Makes sure there is a video to add text to.
				if(MainFrame.getInstance().getVideoPlayer().getVideo().getVideoOutputs() < 1) {
					JOptionPane.showMessageDialog(new JFrame(), "No Video Selected!", "ERROR", JOptionPane.WARNING_MESSAGE);
				}
				else {
					if(_outputName.getText().equals("")) {
						JOptionPane.showMessageDialog(new JFrame(), "Missing Output Name!", "ERROR", JOptionPane.WARNING_MESSAGE);
					}
					else {
						if(_comboStartHours.getSelectedIndex() == 0 || _comboStartMinutes.getSelectedIndex() == 0 || _comboStartSeconds.getSelectedIndex() == 0 ||
								_comboLengthHours.getSelectedIndex() == 0 || _comboLengthMinutes.getSelectedIndex() == 0 || _comboLengthSeconds.getSelectedIndex() == 0) {
							JOptionPane.showMessageDialog(new JFrame(), "Missing time values!", "ERROR", JOptionPane.WARNING_MESSAGE);
						}
						else {
							_filePath = MainFrame.getInstance().getVideoFile().getPath();
							_extractProgressBar.setIndeterminate(true);
							_extractProgressBar.setString("Extracting...");
							_extractProgressBar.validate();
							_extractProgressBar.setVisible(true);
							_extractCancelButton.setEnabled(true);
							_extractCancelButton.validate();
							_extractWorker = new ExtractWorker();


							_extractWorker._ss = (String)_comboStartHours.getSelectedItem() + ":" + (String)_comboStartMinutes.getSelectedItem() + ":" + (String)_comboStartSeconds.getSelectedItem();
							_extractWorker._tt = (String)_comboLengthHours.getSelectedItem() + ":" + (String)_comboLengthMinutes.getSelectedItem() + ":" + (String)_comboLengthSeconds.getSelectedItem();
							_extractWorker._songName = _filePath;
							_extractWorker._output = _userHome + "/" + _outputName.getText();
							_extractWorker.execute();
						}
					}
				}
			}
		});
	}

	/**
	 * Adds a fade handler which either adds a fade in to the beginning or a fade out to the end.
	 * @author chester
	 *
	 */
	class fadeHandler implements ActionListener {
		String _type;
		String _startFrames;
		String _numberFrames;

		@Override
		public void actionPerformed(ActionEvent arg0) {

			// Makes sure there is a video to add text to.
			if(MainFrame.getInstance().getVideoPlayer().getVideo().getVideoOutputs() < 1) {
				JOptionPane.showMessageDialog(new JFrame(), "No Video Selected!", "ERROR", JOptionPane.WARNING_MESSAGE);
			}
			else {
				if((arg0.getSource() == _fadeInButton && _fadeInTime.getText().equals("")) || (arg0.getSource() == _fadeOutButton && _fadeOutTime.getText().equals(""))) {
					JOptionPane.showMessageDialog(new JFrame(), "Please select a time length!", "ERROR", JOptionPane.WARNING_MESSAGE);
				}
				else {
					if(arg0.getSource() == _fadeInButton) {
						_type = "in";
						_startFrames = "0";
						int timeIn = Integer.parseInt(_fadeInTime.getText());
						int fPS = (int) MainFrame.getInstance().getVideoPlayer().getVideo().getFps();
						_numberFrames = Integer.toString(fPS * timeIn);
					}
					if(arg0.getSource() == _fadeOutButton) {
						_type = "out";
						int timeOut = Integer.parseInt(_fadeOutTime.getText());
						int fPS = (int) MainFrame.getInstance().getVideoPlayer().getVideo().getFps();
						_numberFrames = Integer.toString(fPS * timeOut);
						int videoLengthSeconds = (int) (MainFrame.getInstance().getVideoPlayer().getVideo().getLength()/1000);
						_startFrames = Integer.toString((videoLengthSeconds * fPS) - (fPS * timeOut));
					}

					_filePath = MainFrame.getInstance().getVideoFile().getPath();
					@SuppressWarnings("unused")
					Process sProcess;
					try {
						_fadeProgressBar.setIndeterminate(true);
						_fadeProgressBar.setString("Adding FADE...");
						_fadeProgressBar.validate();
						_fadeProgressBar.setVisible(true);
						_fadeCancelButton.setVisible(true);
						_fadeCancelButton.validate();
						_fadeWorker = new AddFadeWorker();
						_fadeWorker.command = "avconv -y -i " + _filePath + " -vf fade=" + _type + ":" + _startFrames + ":" + _numberFrames + " -strict experimental " + _filePath + ".withFade.mp4";
						_fadeWorker.execute();

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	// This is the fade orker
	class AddFadeWorker extends SwingWorker<Void, Void> {

		boolean isCancelled = false;
		Process sProcess;
		int _returnValue;
		String command;

		@Override
		protected Void doInBackground() throws Exception {
			try {
				// Remove any existing subtitles.
				sProcess = runBashCommand(command);

				sProcess.waitFor();

				if(!isCancelled) {
					sProcess = runBashCommand("rm " + _filePath);
					sProcess = runBashCommand("mv " + _filePath + ".withFade.mp4 " + _filePath);
				}


			} finally {

			}
			return null;
		}

		@Override
		protected void done() {
			// Cancel adding text.
			if(isCancelled) {
				_fadeProgressBar.setIndeterminate(false);
				_fadeProgressBar.setString("...Cancelled!");
				_fadeProgressBar.validate();

				JOptionPane.showMessageDialog(new JFrame(), "Fade Addition Cancelled", "CANCELLED", JOptionPane.INFORMATION_MESSAGE);
			}
			// Error in input.
			else if (_returnValue != 0) {
				_fadeProgressBar.setIndeterminate(false);
				_fadeProgressBar.setString("...ERROR!");
				_fadeProgressBar.validate();
				JOptionPane.showMessageDialog(new JFrame(), "Error Adding fade!", "ERROR", JOptionPane.WARNING_MESSAGE);
			}
			// Successful adding text.
			else {
				_fadeProgressBar.setIndeterminate(false);
				_fadeProgressBar.setString("...Completed!");
				_fadeProgressBar.validate();

				JOptionPane.showMessageDialog(new JFrame(), "Fade Addition Successful", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
			}

			_fadeProgressBar.setVisible(false);
			_fadeCancelButton.setVisible(false);
			_fadeCancelButton.validate();

			MainFrame.getInstance().getVideoPlayer().playMedia(_filePath);
		}

		public void cancelWork(){
			// Destroys the process
			if (sProcess != null) {
				isCancelled = true;
				sProcess.destroy();

				// Removes generated temp file.
				try {
					sProcess = runBashCommand("rm " + _filePath + ".withFade.mp4");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}


	// Swing Worker Constructor
	class ExtractWorker extends SwingWorker<Void, Void> {

		boolean isCancelled = false;
		Process sProcess;
		int _returnValue;
		int _status;
		String _songName;
		String _ss;
		String _tt;
		String _output;


		@Override
		protected Void doInBackground() throws Exception {


			try {
				sProcess = runBashCommand("avconv -y -i " + _songName + " -ss " + _ss + " -t " + _tt + " -strict experimental " + _output + ".mp4");

				sProcess.waitFor();
				_status = sProcess.exitValue();

			} catch (IOException IOE) {
				IOE.printStackTrace();
			}
			return null;
		}

		@Override
		protected void done() {

			_extractProgressBar.setIndeterminate(false);

			if(isCancelled()) {
				try {
					sProcess = runBashCommand("rm " + _userHome + "/" + _outputName.getText() + ".mp4");
				} catch (Exception e) {
					e.printStackTrace();
				}
				JOptionPane.showMessageDialog(new JFrame(), "<html><center>Operation Cancelled!</center></html>", "Operation Cancelled!", JOptionPane.INFORMATION_MESSAGE);
			}

			else {
				if(_status != 0) { // Extract had an error.
					_extractProgressBar.setString("Error!");
					_extractProgressBar.setStringPainted(true);
					JOptionPane.showMessageDialog(new JFrame(), "<html><center>Error in Extract!</center></html>", "Error in Extract!", JOptionPane.INFORMATION_MESSAGE);
				}
				else { // Extract was successful.
					_extractProgressBar.setString("Complete!");
					_extractProgressBar.setStringPainted(true);
					JOptionPane.showMessageDialog(new JFrame(), "Extract Complete!\nIs saved in: " + _userHome + "/" + _outputName.getText(), "Extract Complete!", JOptionPane.INFORMATION_MESSAGE);
				}
			}
			// Removes the Progress Bar.
			_extractProgressBar.setVisible(false);
			_extractCancelButton.setEnabled(false);
		}
	}
}
