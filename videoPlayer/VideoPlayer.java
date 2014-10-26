package VamixProject.videoPlayer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import VamixProject.mainSystem.MainFrame;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

/**
 * Class that contains the GUI components for playing and controlling media. This class uses the 
 * VLCJ api for playing media
 * @author Chester Booker Frankie Lam 
 *
 */
public class VideoPlayer extends JPanel {

	private EmbeddedMediaPlayerComponent _videoPlayer; 
	private EmbeddedMediaPlayer _video;

	private JPanel _barAndFunctionHolder;
	private JProgressBar _videoPlayerBar;
	private JPanel _videoPlayerFunctions;
	private JPanel _videoControls;

	private JButton _fullScreenButton;
	private RoundButton _rewindButton; 
	private RoundButton _playPauseButton; 
	private RoundButton _fastforwardButton; 
	private JButton _muteButton; 
	private JSlider _volumeControl;
	
	private URL _fullScreenImageURL = getClass().getResource("FullScreen.png");
	private URL _RRImageURL =  getClass().getResource("Rewind.png");
	private URL _playImageURL =  getClass().getResource("PlayButton.png");
	private URL _pauseImageURL =  getClass().getResource("PauseButton.png");
	private URL _FFImageURL =  getClass().getResource("FastForward.png");
	private URL _soundImageURL =  getClass().getResource("Sound.png");
	private URL _noSoundImageURL =  getClass().getResource("NoSound.png");
	
	private ImageIcon _fullScreenImage = new ImageIcon(_fullScreenImageURL);
	private ImageIcon _RRImage =  new ImageIcon(_RRImageURL);
	private ImageIcon _playImage =  new ImageIcon(_playImageURL);
	private ImageIcon _pauseImage =  new ImageIcon(_pauseImageURL);
	private ImageIcon _FFImage =  new ImageIcon(_FFImageURL);
	private ImageIcon _soundImage =  new ImageIcon(_soundImageURL);
	private ImageIcon _noSoundImage =  new ImageIcon(_noSoundImageURL);

	private boolean _isPlaying = true;
	private boolean _isSound = true;
	private boolean _isFastForwarding = false;
	private boolean _isRewinding = false;

	private VideoSkipWorker _fastForwardWorker = null;
	private VideoSkipWorker _rewindingWorker = null;

	private String _videoName; 

	private Timer _progress;
	
	private JFrame _fullscreenFrame;
	private VideoPlayer _fullScreenVideoPlayer;

	public VideoPlayer() {

		
		setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		setBackground(Color.LIGHT_GRAY);

		// Adds the media player component.
		_videoPlayer = new EmbeddedMediaPlayerComponent();
		_videoPlayer.setPreferredSize(new Dimension(640,360));
		add(_videoPlayer, BorderLayout.CENTER);
		_video = _videoPlayer.getMediaPlayer();

		_barAndFunctionHolder = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		_barAndFunctionHolder.setPreferredSize(new Dimension(640,100));
		_barAndFunctionHolder.setBackground(Color.LIGHT_GRAY);
		add(_barAndFunctionHolder, BorderLayout.CENTER);
		
		// Adds the video time bar.
		_videoPlayerBar = new JProgressBar();
		_videoPlayerBar.setPreferredSize(new Dimension(640,25));
		_barAndFunctionHolder.add(_videoPlayerBar);

		// Adds the panel which holds the buttons grid and the volume control.
		_videoPlayerFunctions = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		_videoPlayerFunctions.setPreferredSize(new Dimension(640,70));
		_videoPlayerFunctions.setBackground(Color.LIGHT_GRAY);
		_barAndFunctionHolder.add(_videoPlayerFunctions);

		// Clicking on the progress bar.
		_progress = new Timer(200, new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int i = (int)(_video.getPosition()*100);

				int totaltime = (int) (_video.getLength()/1000);
				int currenttime = (int) (_video.getTime()/1000);
				int h = (totaltime/3600)%24;
				int m = (totaltime/60)%60;
				int s = (totaltime%60);

				int hh = (currenttime/3600)%24;
				int mm = (currenttime/60)%60;
				int ss = (currenttime%60);
				_videoPlayerBar.setString(String.format("%02d", hh)+":"+String.format("%02d", mm)+":"+String.format("%02d", ss)+"/"+String.format("%02d", h)+":"+String.format("%02d", m)+":"+String.format("%02d", s));
				
				if (i == 100){
					_videoPlayer.getMediaPlayer().playMedia(_videoName);
				}
				else {
					_videoPlayerBar.setValue(i);
				}
			}
		});

		// VideoControls panel holds the buttons.
		_videoControls = new JPanel(new GridLayout(1,6,0,0));
		_videoControls.setPreferredSize(new Dimension(360,70));
		_videoControls.setBackground(Color.LIGHT_GRAY);
		_videoPlayerFunctions.add(_videoControls);

		// Creating the buttons.
		_volumeControl = new JSlider(0,200);

		_fullScreenButton = new JButton(_fullScreenImage);
		_fullScreenButton.setPreferredSize(new Dimension(30,30));
		JPanel fullScreenButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
		fullScreenButtonPanel.setBackground(Color.LIGHT_GRAY);
		fullScreenButtonPanel.add(_fullScreenButton);

		_rewindButton = new RoundButton(_RRImage);
		JPanel rewindButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
		rewindButtonPanel.setBackground(Color.LIGHT_GRAY);
		rewindButtonPanel.add(_rewindButton);

		_playPauseButton = new RoundButton(_pauseImage); // This doesn't need the additional panel because it's the perfect size.

		_fastforwardButton = new RoundButton(_FFImage);
		JPanel fastforwardButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
		fastforwardButtonPanel.setBackground(Color.LIGHT_GRAY);
		fastforwardButtonPanel.add(_fastforwardButton);

		_muteButton = new JButton(_soundImage);
		_muteButton.setPreferredSize(new Dimension(30,30));
		JPanel muteButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
		muteButtonPanel.setBackground(Color.LIGHT_GRAY);
		muteButtonPanel.add(_muteButton);

		// Adding the buttons.
		_videoControls.add(fullScreenButtonPanel);
		_videoControls.add(rewindButtonPanel);
		_videoControls.add(_playPauseButton);
		_videoControls.add(fastforwardButtonPanel);
		_videoControls.add(muteButtonPanel);
		_videoControls.add(_volumeControl);

		// Adding the Listeners to the buttons.
		_playPauseButton.addActionListener(new PauseHandler());
		_videoPlayerBar.addMouseListener(new ForwardHandler());
		_muteButton.addActionListener(new MuteHandler());
		_volumeControl.addChangeListener(new VolumeHandler());
		_volumeControl.addMouseListener(new VolumeSeekHandler());

		_rewindButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				_isFastForwarding = false;
				if(!_isRewinding) {
					_isRewinding = true;
					_rewindingWorker = new VideoSkipWorker();
					_rewindingWorker.execute();
				}
				else {
					_rewindingWorker.cancel(true);
				}
			}
		});

		_fastforwardButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				_isRewinding = false;
				if(!_isFastForwarding) {
					_isFastForwarding = true;
					_fastForwardWorker = new VideoSkipWorker();
					_fastForwardWorker.execute();
				}
				else {
					_fastForwardWorker.cancel(true);
				}
			}
		});
		
		_fullScreenButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				int width = (int)screenSize.getWidth();
				int height = (int)screenSize.getHeight();
				
				_fullscreenFrame = new JFrame();
				_fullscreenFrame.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
				_fullscreenFrame.setSize(width-100,height-100);  
				
				_fullScreenVideoPlayer = new VideoPlayer();
				_fullScreenVideoPlayer.setPreferredSize(new Dimension(width,height));
				_fullScreenVideoPlayer.getVideoPlayer().setPreferredSize(new Dimension(width-100,height - 130));
				
				_fullscreenFrame.add(_fullScreenVideoPlayer, BorderLayout.CENTER);
				
				_fullscreenFrame.setVisible(true);
				_fullscreenFrame.pack();
				
				if(MainFrame.getInstance().getVideoPlayer().isPlaying()) {
					MainFrame.getInstance().getVideoPlayer()._playPauseButton.doClick();
				}
				
				_fullScreenVideoPlayer.getFSButton().setVisible(false);
				
				_fullScreenVideoPlayer.getVideoPlayer().getMediaPlayer().playMedia(MainFrame.getInstance().getVideoPlayer().getVideoName());
				_fullScreenVideoPlayer.getTimer().start();
				
				
				
				// When the screen closes dispose to the frame and delete the preview file.
				_fullscreenFrame.addWindowListener(new java.awt.event.WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						if(_fullScreenVideoPlayer.isPlaying()) {
							_fullScreenVideoPlayer._playPauseButton.doClick();
						}
						_fullscreenFrame.dispose();
					}
				});
				
			}
			
		});
	}

	// Method which plays the selected media.
	public void playMedia(String media){
		_videoName = media;
		_videoPlayer.getMediaPlayer().playMedia(_videoName);
		_progress.start();
		_isPlaying = true;
		_playPauseButton.setIcon(_pauseImage);
	}

	// Pause/Plays the video.
	class PauseHandler implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {

			_isFastForwarding = false;
			_isRewinding = false;

			if(_video.getPosition() >= 1){
				_video.playMedia(_videoName);
				_isPlaying = false;
			}
			else{
				_video.pause();
			}
			if(_isPlaying) {
				_playPauseButton.setIcon(_playImage);
				_playPauseButton.validate();
				_isPlaying = false;
			}
			else {
				_playPauseButton.setIcon(_pauseImage);
				_playPauseButton.validate();
				_isPlaying = true;
			}
		}
	}

	// Fast forwards the video.
	class ForwardHandler extends MouseAdapter{
		@Override
		public void mousePressed(MouseEvent e) {
			
				float f = (float)(e.getX())/(float) (_videoPlayerBar.getWidth());
				_video.setPosition(f);
			
		}

		public void mouseEntered(MouseEvent e){
			_videoPlayerBar.setStringPainted(true);

		}

		public void mouseExited(MouseEvent e){
			_videoPlayerBar.setStringPainted(false);
		}
	}

	// Mutes the video.
	class MuteHandler implements ActionListener{
		public void actionPerformed(ActionEvent arg0) {
			_video.mute();
			if(_isSound) {
				_muteButton.setIcon(_noSoundImage);
				_isSound = false;
			}
			else {		
				_muteButton.setIcon(_soundImage);
				_isSound = true;
			}
		}
	}

	// Sets the volume.
	class VolumeSeekHandler extends MouseAdapter{
		public void mousePressed(MouseEvent e) {
			_volumeControl.setValue(e.getX());
		}
	}
	class VolumeHandler implements ChangeListener{
		@Override
		public void stateChanged(ChangeEvent e) {
			JSlider source = (JSlider)e.getSource();
			_video.setVolume(source.getValue());

		}
	}

	class VideoSkipWorker extends SwingWorker<Void, Long> {

		@Override
		protected Void doInBackground() throws Exception {

			// Will loop if fast forwarding and if the current time of the video is under 97/100ths of the video
			while(_isFastForwarding && _video.getLength() >= _video.getTime() + (3*_video.getLength()/100)) {
				Thread.sleep(60);
				publish(_video.getLength()/100);	
			}
			while(_isRewinding && _video.getTime() - (3*_video.getLength()/100) >= 0) {
				Thread.sleep(60);
				publish(-_video.getLength()/100);
			}
			return null;
		}

		// Skips the video.
		protected void process(List<Long> chunks) {
			for(int i = 0; i < chunks.size(); i++) {
				_video.skip(chunks.get(i));
			}
		}

		@Override
		protected void done() {
			_isFastForwarding = false;
			_isRewinding = false;
		}
	}
	
	public EmbeddedMediaPlayer getVideo() {
		 return _video;
	}
	
	public EmbeddedMediaPlayerComponent getVideoPlayer(){
		return _videoPlayer;
	}
	
	public String getVideoName() {
		return _videoName;
	}
	
	public Timer getTimer() {
		return _progress;
	}
	
	public boolean isPlaying() {
		return _isPlaying;
	}
	
	public JButton getFSButton() {
		return _fullScreenButton;
	}
	
	public void setFilePath(String filePath) {
		_videoName = filePath;
	}
}