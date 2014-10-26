package VamixProject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * This is a singleton as there should only exist one MainFrame at one given time.
 * The main frame is the bulk of VAMIX as it hold all the GUI components together
 * and connects the user to all of its functionality.
 * 
 * @author Chester Booker and Frankie Lam
 */

public class MainFrame extends JFrame {

	// Holds the singleton instance.
	private static MainFrame _mainFrameInstance = null;

	// Makes sure only one singleton exists.
	public static MainFrame getInstance() {
		if(_mainFrameInstance == null) {
			_mainFrameInstance = new MainFrame();
		}
		return _mainFrameInstance;
	}

	// Main Panel/Pane(s)
	private JPanel _topFeatures;
	JPanel _bottomFeatures;

	// Holds the user-friendly buttons.
	private JPanel _bigButtons; 

	// Series of big user-friendly buttons.
	private JButton _selectionButton; 
	private JButton _downloadButton; 
	private JButton _addSubtitlesButton;
	private JButton _addEffectsButton;
	private JButton _audioEditButton;
	private JButton _videoEditButton;
	private JButton _textEditButton;
	private JButton _adjustSpeedButton;

	// The different panes in the tabbed pane section.
	private TitlePane _titlePane;
	private DownloadPane _downloadPane;
	private EffectsPane _effectsPane;
	private TextEditPane _textEditPane;
	private AudioEditMaster _audioEditPane;
	private SubtitlePane _subtitlePane;
	private VideoEditPane _videoEditPane;
	private SpeedAdjustPane _adjustSpeedPane;

	// File chooser and the chosen file.
	private JFileChooser _chooser = new JFileChooser();
	private File _playingFile;

	// Video Player.
	private VideoPlayer _completeVideoPlayer;
	
	// Image URLs
	private URL _selectURL = getClass().getResource("Select.png");
	private URL _downloadURL =  getClass().getResource("Download.png");
	private URL _audioURL =  getClass().getResource("AudioEdit.png");
	private URL _effectURL =  getClass().getResource("Effects.png");
	private URL _videoURL =  getClass().getResource("VideoEdit.png");
	private URL _textURL =  getClass().getResource("EditText.png");
	private URL _subURL =  getClass().getResource("Subtitles.png");
	private URL _speedURL =  getClass().getResource("AdjustSpeed.png");
	
	// Singleton constructor.
	protected MainFrame() {

		// A panel containing button panel on the left, and the complete video player on the right.
		_topFeatures = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		_topFeatures.setPreferredSize(new Dimension(230 + 640,100 + 360));
		_topFeatures.setBackground(Color.LIGHT_GRAY);
		add(_topFeatures, BorderLayout.NORTH);

		// A panel containing the area where all the panels are contained.
		_bottomFeatures = new JPanel(new BorderLayout(0,0));
		_bottomFeatures.setPreferredSize(new Dimension(200 + 640, 250));
		add(_bottomFeatures, BorderLayout.SOUTH);

		//Initialize title pane
		_titlePane = new TitlePane();
		_bottomFeatures.add(_titlePane, BorderLayout.CENTER);                       

		// Initialize download pane
		_downloadPane = new DownloadPane();

		// Initialize audioEdit pane
		_audioEditPane = new AudioEditMaster();

		// Initialize effects pane
		_effectsPane = new EffectsPane();

		// Initialize videoEdit pane
		_videoEditPane = new VideoEditPane();

		// Initialize textEdit pane
		_textEditPane = new TextEditPane();

		// Initialize subtitle pane
		_subtitlePane = new SubtitlePane();

		// Initialize adjust speed pane
		_adjustSpeedPane = new SpeedAdjustPane();

		// A panel containing the five buttons: Select Media, Download Media, Edit Media, Edit Text, Save.
		_bigButtons = new JPanel(new GridLayout(4,2,0,0));
		_bigButtons.setPreferredSize(new Dimension(230,460));
		_bigButtons.setBackground(Color.LIGHT_GRAY);
		_topFeatures.add(_bigButtons, BorderLayout.NORTH);

		// A panel containing the actual media player at the top, a progress bar in the middle and a series of buttons at the bottom.
		_completeVideoPlayer = new VideoPlayer();
		_completeVideoPlayer.setPreferredSize(new Dimension(640,460));
		_topFeatures.add(_completeVideoPlayer, BorderLayout.EAST);

		// The buttons on the right.
		_selectionButton = new JButton("<html><center><u>S</u>elect<br>Media</center></html>");
		_downloadButton = new JButton("<html><center><u>D</u>ownload<br>Media</center></html>");
		_addSubtitlesButton = new JButton("<html><center>Add<br>S<u>u</u>btitles</center></html>");
		_addEffectsButton = new JButton("<html><center>Add<br><u>E</u>ffects</center></html>");
		_audioEditButton = new JButton("<html><center>Edit the<br><u>A</u>udio</center></html>");
		_textEditButton = new JButton("<html><center>Edit the<br><u>T</u>ext</center></html>");
		_videoEditButton = new JButton("<html><center>Edit the<br><u>V</u>ideo</center></html>");
		_adjustSpeedButton = new JButton("<html><center>Ad<u>j</u>ust the<br>Speed</center></html>");

		// Potential for tool tips.
//		_selectionButton.setToolTipText("Select the \nmedia to edit!");
//		_downloadButton.setToolTipText("Download a media from the internet!");
//		_addSubtitlesButton.setToolTipText("Add subtitles to a video!");
//		_addEffectsButton.setToolTipText("Add effects to a video!");
//		_audioEditButton.setToolTipText("Edit the audio of a video!");
//		_textEditButton.setToolTipText("Add text to beginning or\n end scenes!");
//		_videoEditButton.setToolTipText("Either extract or add a fade to a video!");
//		_adjustSpeedButton.setToolTipText("Adjust the speed of a video!");

		// Adding the circle buttons.
		_bigButtons.add(_selectionButton, BorderLayout.CENTER);
		_bigButtons.add(_downloadButton, BorderLayout.CENTER);
		_bigButtons.add(_addSubtitlesButton, BorderLayout.CENTER);
		_bigButtons.add(_addEffectsButton, BorderLayout.CENTER);
		_bigButtons.add(_audioEditButton, BorderLayout.CENTER);
		_bigButtons.add(_textEditButton, BorderLayout.CENTER);
		_bigButtons.add(_videoEditButton, BorderLayout.CENTER);
		_bigButtons.add(_adjustSpeedButton, BorderLayout.CENTER);

		// Select the media file via a file-get
		_selectionButton.addActionListener(new chooserHandler());

		// Go to the download pane.
		_downloadButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Remove panels and add download panel.
				_bottomFeatures.removeAll();
				_bottomFeatures.add(_downloadPane, BorderLayout.CENTER); 
				_bottomFeatures.repaint();
				_bottomFeatures.validate();
			}
		});
		
		_selectionButton.addMouseListener(new MouseAdapter()
		{
			public void mouseEntered(MouseEvent evt)
			{
				ImageIcon icon = new ImageIcon(_selectURL);
				_selectionButton.setText("");
				_selectionButton.setIcon(icon);
			}
			public void mouseExited(MouseEvent evt)
			{
				ImageIcon icon = new ImageIcon("");
				_selectionButton.setText("<html><center><u>S</u>elect<br>Media</center></html>");
				_selectionButton.setIcon(icon);
			}
		});

		_downloadButton.addMouseListener(new MouseAdapter()
		{
			public void mouseEntered(MouseEvent evt)
			{
				ImageIcon icon = new ImageIcon(_downloadURL);
				_downloadButton.setText("");
				_downloadButton.setIcon(icon);
			}
			public void mouseExited(MouseEvent evt)
			{
				ImageIcon icon = new ImageIcon("");
				_downloadButton.setText("<html><center><u>D</u>ownload<br>Media</center></html>");
				_downloadButton.setIcon(icon);
			}
		});

		_addSubtitlesButton.addMouseListener(new MouseAdapter()
		{
			public void mouseEntered(MouseEvent evt)
			{
				ImageIcon icon = new ImageIcon(_subURL);
				_addSubtitlesButton.setText("");
				_addSubtitlesButton.setIcon(icon);
			}
			public void mouseExited(MouseEvent evt)
			{
				ImageIcon icon = new ImageIcon("");
				_addSubtitlesButton.setText("<html><center>Add<br>S<u>u</u>btitles</center></html>");
				_addSubtitlesButton.setIcon(icon);
			}
		});

		_addEffectsButton.addMouseListener(new MouseAdapter()
		{
			public void mouseEntered(MouseEvent evt)
			{
				ImageIcon icon = new ImageIcon(_effectURL);
				_addEffectsButton.setText("");
				_addEffectsButton.setIcon(icon);
			}
			public void mouseExited(MouseEvent evt)
			{
				ImageIcon icon = new ImageIcon("");
				_addEffectsButton.setText("<html><center>Add<br><u>E</u>ffects</center></html>");
				_addEffectsButton.setIcon(icon);
			}
		});

		_audioEditButton.addMouseListener(new MouseAdapter()
		{
			public void mouseEntered(MouseEvent evt)
			{
				ImageIcon icon = new ImageIcon(_audioURL);
				_audioEditButton.setText("");
				_audioEditButton.setIcon(icon);
			}
			public void mouseExited(MouseEvent evt)
			{
				ImageIcon icon = new ImageIcon("");
				_audioEditButton.setText("<html><center>Edit the<br><u>A</u>udio</center></html>");
				_audioEditButton.setIcon(icon);
			}
		});

		_textEditButton.addMouseListener(new MouseAdapter()
		{
			public void mouseEntered(MouseEvent evt)
			{
				ImageIcon icon = new ImageIcon(_textURL);
				_textEditButton.setText("");
				_textEditButton.setIcon(icon);
			}
			public void mouseExited(MouseEvent evt)
			{
				ImageIcon icon = new ImageIcon("");
				_textEditButton.setText("<html><center>Edit the<br><u>T</u>ext</center></html>");
				_textEditButton.setIcon(icon);
			}
		});

		_videoEditButton.addMouseListener(new MouseAdapter()
		{
			public void mouseEntered(MouseEvent evt)
			{
				ImageIcon icon = new ImageIcon(_videoURL);
				_videoEditButton.setText("");
				_videoEditButton.setIcon(icon);
			}
			public void mouseExited(MouseEvent evt)
			{
				ImageIcon icon = new ImageIcon("");
				_videoEditButton.setText("<html><center>Edit the<br><u>V</u>ideo</center></html>");
				_videoEditButton.setIcon(icon);
			}
		});

		_adjustSpeedButton.addMouseListener(new MouseAdapter()
		{
			public void mouseEntered(MouseEvent evt)
			{
				ImageIcon icon = new ImageIcon(_speedURL);
				_adjustSpeedButton.setText("");
				_adjustSpeedButton.setIcon(icon);
			}
			public void mouseExited(MouseEvent evt)
			{
				ImageIcon icon = new ImageIcon("");
				_adjustSpeedButton.setText("<html><center>Ad<u>j</u>ust the<br>Speed</center></html>");
				_adjustSpeedButton.setIcon(icon);
			}
		});

		// Go to the download pane.
		_addEffectsButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Remove panels and add download panel.
				_bottomFeatures.removeAll();
				_bottomFeatures.add(_effectsPane, BorderLayout.CENTER); 
				_bottomFeatures.repaint();
				_bottomFeatures.validate();
			}
		});

		// Go to the video edit pane.
		_addSubtitlesButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {		
				// Remove panels and add video edit panel.
				_bottomFeatures.removeAll();
				_bottomFeatures.add(_subtitlePane, BorderLayout.CENTER); 
				_bottomFeatures.repaint();
				_bottomFeatures.validate();					
			}	
		});

		// Go to the video edit pane.
		_audioEditButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {		
				// Remove panels and add video edit panel.
				_bottomFeatures.removeAll();
				_bottomFeatures.add(_audioEditPane, BorderLayout.CENTER); 
				_bottomFeatures.repaint();
				_bottomFeatures.validate();		
			}	
		});



		// Go to the text edit pane.
		_textEditButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Remove panels and add text edit.
				_bottomFeatures.removeAll();
				_bottomFeatures.add(_textEditPane, BorderLayout.CENTER); 
				_bottomFeatures.repaint();
				_bottomFeatures.validate();
			}
		});

		_videoEditButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Remove panels and add text edit.
				_bottomFeatures.removeAll();
				_bottomFeatures.add(_videoEditPane, BorderLayout.CENTER); 
				_bottomFeatures.repaint();
				_bottomFeatures.validate();
			}
		});

		_adjustSpeedButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Remove panels and add text edit.
				_bottomFeatures.removeAll();
				_bottomFeatures.add(_adjustSpeedPane, BorderLayout.CENTER); 
				_bottomFeatures.repaint();
				_bottomFeatures.validate();
			}
		});

		/**
		Creates ALT hot keys which simulate a button press for each of the options.
		If ALT and any other key is pressed it returns to the HOME page.
		This is great for advanced users who want to increase their editing time.
		 **/
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			@Override
			public boolean dispatchKeyEvent(KeyEvent e) {
				if(e.getID() == KeyEvent.KEY_PRESSED) {
					if(e.isAltDown() && e.getKeyCode() == 83) {
						_selectionButton.doClick();
					}
					else if(e.isAltDown() && e.getKeyCode() == 68) {
						_downloadButton.doClick();
					}
					else if(e.isAltDown() && e.getKeyCode() == 85) {
						_addSubtitlesButton.doClick();
					}
					else if(e.isAltDown() && e.getKeyCode() == 69) {
						_addEffectsButton.doClick();
					}
					else if(e.isAltDown() && e.getKeyCode() == 65) {
						_audioEditButton.doClick();
					}
					else if(e.isAltDown() && e.getKeyCode() == 84) {
						_textEditButton.doClick();
					}
					else if(e.isAltDown() && e.getKeyCode() == 86) {
						_videoEditButton.doClick();
					}
					else if(e.isAltDown() && e.getKeyCode() == 74) {
						_adjustSpeedButton.doClick();
					}
					else if(e.isAltDown() && e.getKeyCode() != 18) {
						_bottomFeatures.removeAll();
						_bottomFeatures.add(_titlePane, BorderLayout.CENTER); 
						_bottomFeatures.repaint();
						_bottomFeatures.validate();
					}
				}
				return false;
			}
		});

		// Completing the Mainframe setup.
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
		pack();
	}

	class chooserHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {

			int returnVal = _chooser.showOpenDialog(_completeVideoPlayer);

			if (returnVal == JFileChooser.APPROVE_OPTION) {

				_playingFile = _chooser.getSelectedFile();

				try {
					//Check if file is valid or not, and if it isn't then complain
					Process checkFile = BashCommandPanel.runBashCommand("file -ib "+"\"" + _playingFile.getPath() + "\"" + " | grep \"video\\|audio\\|mpeg\\|octet-stream\"");
					checkFile.waitFor();
					if (checkFile.exitValue()!=0) {
						JOptionPane.showMessageDialog(_completeVideoPlayer, "Please select a media file");
					}
					else{
						_audioEditPane.update(_playingFile);
						_completeVideoPlayer.playMedia(_playingFile.getPath());		
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	// Video player getter.
	public VideoPlayer getVideoPlayer() {
		return _completeVideoPlayer;
	}

	// Video file getter.
	public File getVideoFile() {
		return _playingFile;
	}

	/**
	 * Calls the toggleOverlay() method in _videoEditPane
	 * @param status
	 */
	public void toggleOverlay(boolean status){
		_audioEditPane.toggleOverlay(status);
	}

	/**
	 * Calls the toggleExtract() method in _videoEditPane
	 * @param status
	 */
	public void toggleExtract(boolean status){
		_audioEditPane.toggleExtract(status);
	}

}
