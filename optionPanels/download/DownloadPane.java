package vamixProject.optionPanels.download;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.BevelBorder;

import vamixProject.optionPanels.BashCommandPanel;


/**
 * 
 * Used from Assignment 3 with help from my partner. Was tweaked to suit the final by me.
 * This is the download pane and is used for downloading media files
 * from the internet. All you need to do is have the media's URL.
 * Make sure that the media is open-source though!
 * 
 * @author Chester Booker and Frankie Lam
 */

@SuppressWarnings("serial")
public class DownloadPane extends BashCommandPanel {
	
	// Three main sections.
	private JPanel _blankPanel1;
	private JPanel _centerPanel;
	private JPanel _blankPanel2;

	// Three sections inside the center section.
	private JPanel _upperSection;
	private JPanel _midSection;
	private JPanel _lowerSection;

	// URL holder.
	private JLabel _URLLabel;
	private JLabel _openSourceLabel;

	// Check box and downloading buttons and bar.
	private JTextField _url;
	private JCheckBox _openSource;
	private JButton _dlButton;
	private JButton _cancelButton;
	private JProgressBar _bar;
	
	// Download worker.
	private InnerWorker _inner = null;

	/**
	 * This is the constructor for creating and downloading media files from
	 * the internet. So long as they are confirmed to be open-source.
	 */
	public DownloadPane() {

		// Creates and sets up the GUI
		setBorder(BorderFactory.createEtchedBorder(BevelBorder.RAISED));
		setLayout(new BorderLayout(0,0));

		// A place for holding the URL
		_url = new JTextField("-Enter a URL here-");
		_url.setPreferredSize(new Dimension(200,30));

		// Sets up the open-source check box.
		_openSource = new JCheckBox();
		_dlButton = new JButton("Download");
		_cancelButton = new JButton("Cancel");
		_cancelButton.setVisible(false);

		// Sets up the progress bar during a download.
		_bar = new JProgressBar(0,100);
		_bar.setValue(0);
		_bar.setStringPainted(true);
		_bar.setPreferredSize(new Dimension(250,20));
		_bar.setVisible(false);

		// Hide the download button until open source is selected.
		_dlButton.setEnabled(false);

		_URLLabel = new JLabel("URL:");
		_URLLabel.setFont(new Font("Ubuntu", Font.PLAIN, 16));

		_openSourceLabel = new JLabel("Is it Open-Source?");
		_openSourceLabel.setFont(new Font("Ubuntu", Font.PLAIN, 16));

		_blankPanel1 = new JPanel();
		_blankPanel1.setPreferredSize(new Dimension(865,65));

		_centerPanel = new JPanel();
		_centerPanel.setLayout(new BoxLayout(_centerPanel, BoxLayout.PAGE_AXIS));
		_centerPanel.setPreferredSize(new Dimension(865,120));

		_blankPanel2 = new JPanel();
		_blankPanel2.setPreferredSize(new Dimension(865,65));

		_upperSection = new JPanel();
		_midSection = new JPanel();
		_lowerSection = new JPanel();

		// Adds the parts.
		add(_blankPanel1, BorderLayout.NORTH);
		add(_centerPanel, BorderLayout.CENTER);
		add(_blankPanel2, BorderLayout.SOUTH);

		// Adds the sections into the center panel.
		_centerPanel.add(_upperSection);
		_centerPanel.add(_midSection);
		_centerPanel.add(_lowerSection);

		// Add the URL parts
		_upperSection.add(_URLLabel);
		_upperSection.add(_url);

		// Adds the open source selection parts
		_midSection.add(_openSourceLabel);
		_midSection.add(_openSource);
		_midSection.add(_dlButton);

		// Adds the cancel and progress bar, they are initally invisible
		_lowerSection.add(_cancelButton);
		_lowerSection.add(_bar);

		// Creates the action listeners to take care of the different buttons.
		_dlButton.addActionListener(new dlButtonListener());
		_openSource.addActionListener(new boxHandler());
		_cancelButton.addActionListener(new cancelHandler());
	}

	// Method that toggles the state of the panel; setting the buttons to be disabled 
	// except the cancel button, or doing the opposite if the status is true
	public void toggleButtons(boolean status){
		if (!status){
			_dlButton.setEnabled(false);
			_openSource.setEnabled(false);
		}
		else{
			_dlButton.setEnabled(true);
			_openSource.setEnabled(true);
			_bar.setValue(0);
		}
	}

	// Download button which checks all the fields are correct and starts the download.
	class dlButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {

			try {
				//Checks whether or not the file already exists
				String url =_url.getText();
				Process sProcess=runBashCommand("basename "+"\""+url+"\"");
				sProcess.waitFor();
				InputStream stdout = sProcess.getInputStream();
				BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
				String name = stdoutBuffered.readLine();

				sProcess = runBashCommand("[ -e "+"\""+name+"\""+" ]");
				sProcess.waitFor();

				//Performs download when the file doesn't already exist
				if(sProcess.exitValue() != 0){
					_cancelButton.setVisible(true);
					_bar.setVisible(true);
					_inner = new InnerWorker(new ProcessBuilder("wget","--progress=dot",url));
					_inner.execute();
					toggleButtons(false);
				}

				//Implementation for when file already exists
				else{
					//Create JOptionPane to ask for user decision
					Object[] options = {"Overwrite","Continue","Cancel"};
					int output = JOptionPane.showOptionDialog(_url, "Please pick an option"
							,"File already exists" ,JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE,null,options,options[2]);
					if(output == JOptionPane.YES_OPTION){
						//Implementation for overwrite
						_cancelButton.setVisible(true);
						_bar.setVisible(true);
						Process temppro=runBashCommand("rm "+"\""+name+"\"");
						temppro.waitFor();
						_inner = new InnerWorker(new ProcessBuilder("wget","--progress=dot",url));
						_inner.execute();
						toggleButtons(false);

					}else if(output == JOptionPane.NO_OPTION){
						//Implementation for continue	
						_cancelButton.setVisible(true);
						_bar.setVisible(true);
						_inner = new InnerWorker(new ProcessBuilder("wget", "--progress=dot", "-c", url));
						_inner.execute();
						toggleButtons(false);
					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			};
		}
	}

	// Initiates the cancel worker when the cancel button is pressed.
	class cancelHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			_inner.cancelWork();

		}
	}

	// Switches the download button on and off when the checked box is ticked or not.
	class boxHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (_dlButton.isEnabled()) {
				_dlButton.setEnabled(false); 
			}
			else {
				_dlButton.setEnabled(true);
			}
		}
	}


	/**
	 * This is the download worker.
	 * It executes the downloading task and can be cancelled at any time.
	 * Reports back any errors in the download to the user.
	 * @author chester
	 *
	 */
	class InnerWorker extends SwingWorker<Void,String>{
		boolean _isCancelled;
		Process sProcess;
		ProcessBuilder _psb;

		public InnerWorker(){
			_isCancelled = false;
		}

		@Override
		protected Void doInBackground() throws Exception {
			//Starts the download and reads the output, calling findPercentage to publish it
			sProcess = _psb.start();
			InputStream stderr = sProcess.getErrorStream();
			BufferedReader stderrRead = new BufferedReader(new InputStreamReader(stderr));
			String line = null;

			while ((line = stderrRead.readLine()) != null ) {
				findPercentage(line);
			}

			return null;
		}

		//Formats the output string into numbers only and publishes them
		public void findPercentage(String line){
			Pattern p = Pattern.compile("\\d{1,}%");
			Matcher m = p.matcher(line);
			if(m.find()){
				String percent=m.group(0).replace("%", "");
				publish(percent);
			}
		}

		/**
		 * Messages the user about the status of the completed download.
		 * If it is completed/cancelled/has an error.
		 */
		protected void done(){
			if(_isCancelled){
				JOptionPane.showMessageDialog(_url, "Cancelled");
				toggleButtons(true);

			}
			else if (sProcess.exitValue()==0){
				JOptionPane.showMessageDialog(_url, "Download Successful");
				toggleButtons(true);

			}
			else if(sProcess.exitValue()==1)  {

				JOptionPane.showMessageDialog(_url, "Error occured");
				toggleButtons(true);
			}
			else if(sProcess.exitValue()==2)  {

				JOptionPane.showMessageDialog(_url, "Parsing error");
				toggleButtons(true);
			}
			else if(sProcess.exitValue()==3)  {

				JOptionPane.showMessageDialog(_url, "File I/O error");
				toggleButtons(true);
			}
			else if(sProcess.exitValue()==4)  {

				JOptionPane.showMessageDialog(_url, "Network failure");
				toggleButtons(true);
			}
			else if(sProcess.exitValue()==5)  {

				JOptionPane.showMessageDialog(_url, "SSL verification error");
				toggleButtons(true);
			}
			else if(sProcess.exitValue()==6)  {

				JOptionPane.showMessageDialog(_url, "Username/password authentication error");
				toggleButtons(true);
			}
			else if(sProcess.exitValue()==7)  {

				JOptionPane.showMessageDialog(_url, "Protocol errors");
				toggleButtons(true);
			}
			else if(sProcess.exitValue()==8)  {

				JOptionPane.showMessageDialog(_url, "Server error response");
				toggleButtons(true);
			}
			_cancelButton.setVisible(false);
			_bar.setVisible(false);	
		}

		/**
		 * Processes the percentage and updates the progress bar.
		 */
		@Override
		protected void process(List<String> chunks){
			for (int i=0;i<chunks.size();i++){
				String s=chunks.get(i);
				_bar.setValue(Integer.parseInt(s));
			}
		}

		public InnerWorker( ProcessBuilder psb){
			sProcess = null;
			_psb = psb;
		}

		/**
		 * Destroys the process and resets the buttons so they are enabled.
		 */
		public void cancelWork(){
			if (sProcess!=null){
				_isCancelled=true;
				sProcess.destroy();
				toggleButtons(true);
				_cancelButton.setVisible(false);
				_bar.setVisible(false);
			}
		}
	}
}