package VamixProject.optionPanels.effects;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.BevelBorder;

import VamixProject.optionPanels.BashCommandPanel;
import VamixProject.mainSystem.MainFrame;

/**
 * 
 * This panel adds several different affects to a video. This is for quirky effects on a video.
 * @author Chester Booker
 * 
 **/
public class EffectsPane extends BashCommandPanel {

	// Set up gui
	private JPanel _topSection;
	private JPanel _middleSection;
	private JPanel _bottomSection;
	private JPanel _progressPanel;

	private JButton _vFlipButton;
	private JButton _hFlipButton;
	private JButton _blurButton;
	private JButton _waterMarkButton;

	private JPanel _imageHolderPanel;
	private JButton _imageChooserButton;
	private JTextField _imageChooserField;

	private JPanel _addHolderPanel;
	private JButton _addbutton;
	private JPanel _dynamicPanel;
	private JLabel _dynamicLabel;
	private JButton _cancelButton;

	private JProgressBar _progressBar;
	private String _filePath;
	private File _waterMarkFile;
	private JFileChooser _watermarkChooser = new JFileChooser();
	private EffectWorker _addEffectWorker = null;
	
	private URL _blurURL = getClass().getResource("Blur.png");
	private URL _vURL =  getClass().getResource("VFlip.png");
	private URL _hURL =  getClass().getResource("HFlip.png");
	private URL _waterURL =  getClass().getResource("Watermark.png");
	
	private ImageIcon _blur = new ImageIcon(_blurURL);
	private ImageIcon _v =  new ImageIcon(_vURL);
	private ImageIcon _h =  new ImageIcon(_hURL);
	private ImageIcon _water =  new ImageIcon(_waterURL);

	public EffectsPane() {

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(BorderFactory.createEtchedBorder(BevelBorder.RAISED));

		_topSection = new JPanel(new GridLayout(1,6,0,0));
		_topSection.setPreferredSize(new Dimension(500,120));

		_middleSection = new JPanel();
		_middleSection.setPreferredSize(new Dimension(500,40));

		_bottomSection = new JPanel();
		_bottomSection.setPreferredSize(new Dimension(500,40));

		_progressPanel = new JPanel();
		_progressPanel.setPreferredSize(new Dimension(500,20));

		JPanel blank1 = new JPanel();
		JPanel blank2 = new JPanel();

		_vFlipButton = new JButton();
		_hFlipButton = new JButton();
		_blurButton = new JButton();
		_waterMarkButton = new JButton();
		
		_vFlipButton.setIcon(_v);
		_hFlipButton.setIcon(_h);
		_blurButton.setIcon(_blur);
		_waterMarkButton.setIcon(_water);

		_imageHolderPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		_imageHolderPanel.setPreferredSize(new Dimension(400,40));
		_imageHolderPanel.setVisible(false);
		_imageChooserButton = new JButton("Choose Watermark:");
		_imageChooserButton.setPreferredSize(new Dimension(150,40));
		_imageChooserField = new JTextField("-No current watermark selected-");
		_imageChooserField.setPreferredSize(new Dimension(250,40));
		_imageChooserField.setEnabled(false);

		_addHolderPanel = new JPanel(new GridLayout(1,3,0,0));
		_addHolderPanel.setPreferredSize(new Dimension(360,40));
		_addbutton = new JButton("Add Effect!");
		_dynamicPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
		_dynamicLabel = new JLabel("-No Effect-");
		_dynamicLabel.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		_cancelButton = new JButton("Cancel!");
		_cancelButton.setEnabled(false);

		_progressBar = new JProgressBar();
		_progressBar.setIndeterminate(true);
		_progressBar.setPreferredSize(new Dimension(360,15));
		_progressBar.setStringPainted(true);
		_progressBar.setVisible(false);


		add(_topSection);
		add(_middleSection);
		add(_bottomSection);
		add(_progressPanel);

		_topSection.add(blank1);
		_topSection.add(_vFlipButton);
		_topSection.add(_hFlipButton);
		_topSection.add(_blurButton);
		_topSection.add(_waterMarkButton);
		_topSection.add(blank2);

		_middleSection.add(_imageHolderPanel, BorderLayout.CENTER);
		_imageHolderPanel.add(_imageChooserButton);
		_imageHolderPanel.add(_imageChooserField);

		_bottomSection.add(_addHolderPanel, BorderLayout.CENTER);
		_addHolderPanel.add(_addbutton);
		_addHolderPanel.add(_dynamicPanel, BorderLayout.CENTER);
		_dynamicPanel.add(_dynamicLabel);
		_addHolderPanel.add(_cancelButton);

		_progressPanel.add(_progressBar, BorderLayout.CENTER);


		_vFlipButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_dynamicLabel.setText("Vertical-Flip");
				_dynamicLabel.validate();
				_imageHolderPanel.setVisible(false);
			}	
		});

		// When the Horizontal Flip Button is pressed.
		_hFlipButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_dynamicLabel.setText("Horizontal-Flip");
				_dynamicLabel.validate();
				_imageHolderPanel.setVisible(false);
			}	
		});

		// When the Horizontal Blur is pressed.
		_blurButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_dynamicLabel.setText("Blur");
				_dynamicLabel.validate();
				_imageHolderPanel.setVisible(false);
			}	
		});

		// When the Horizontal Watermark is pressed.
		_waterMarkButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_dynamicLabel.setText("Watermark");
				_dynamicLabel.validate();
				_imageHolderPanel.setVisible(true);
			}	
		});

		// Lets the user choose an image for the watermark.
		_imageChooserButton.addActionListener(new chooserHandler());

		_addbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// Makes sure there is a video to add text to.
				if(MainFrame.getInstance().getVideoPlayer().getVideo().getVideoOutputs() < 1) {
					JOptionPane.showMessageDialog(new JFrame(), "No Video Selected!", "ERROR", JOptionPane.WARNING_MESSAGE);
				}
				else {
					if(_dynamicLabel.getText().equals("Vertical-Flip") || _dynamicLabel.getText().equals("Horizontal-Flip") || _dynamicLabel.getText().equals("Blur") || _dynamicLabel.getText().equals("Watermark")) {

						// Checks to make sure that if the watermark option is selected, that a specific watermark is chosen.
						if(_dynamicLabel.getText().equals("Watermark") && _imageChooserField.getText().equals("-No current watermark selected-")) {
							JOptionPane.showMessageDialog(new JFrame(), "No Watermark image Selected!", "ERROR", JOptionPane.WARNING_MESSAGE);
						}
						else {
							_filePath = MainFrame.getInstance().getVideoFile().getPath();

							_cancelButton.setEnabled(true);
							_progressBar.setVisible(true);
							_progressBar.setIndeterminate(true);
							_progressBar.setString("Adding Effects...");
							_progressBar.validate();

							_addEffectWorker = new EffectWorker();

							// Completes process based on selected button
							if(_dynamicLabel.getText().equals("Vertical-Flip")) {
								_addEffectWorker.Command = "avconv -y -i " + _filePath + " -vf \"vflip\" -strict experimental " + _filePath + ".withVflip.mp4";
								_addEffectWorker.effect = Effect.vFlip;
							}
							else if(_dynamicLabel.getText().equals("Horizontal-Flip")) {
								_addEffectWorker.Command = "avconv -y -i " + _filePath + " -vf \"hflip\" -strict experimental " + _filePath + ".withHflip.mp4";
								_addEffectWorker.effect = Effect.hFlip;
							}
							else if(_dynamicLabel.getText().equals("Blur")) {
								_addEffectWorker.Command = "avconv -y -i " + _filePath + " -vf boxblur=2:1:0:0:0:0 -strict experimental " + _filePath + ".withBlur.mp4";
								_addEffectWorker.effect = Effect.blur;
							}
							else if(_dynamicLabel.getText().equals("Watermark") ) {
								_addEffectWorker.Command = "avconv -y -i " + _filePath + " -vf \"movie=" + _waterMarkFile.getPath() + " [watermark]; [in][watermark] overlay=10:10 [out]\" -c:v libx264 -strict experimental " + _filePath + ".withWatermark.mp4";
								_addEffectWorker.effect = Effect.watermark;
							}
							_addEffectWorker.execute();
						}
					}
					else {
						JOptionPane.showMessageDialog(new JFrame(), "No Effect Selected!", "ERROR", JOptionPane.WARNING_MESSAGE);
					}
				}
			}	
		});

		// The cancel button for cancelling adding text.
		_cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(_addEffectWorker != null) {
					_addEffectWorker.cancelWork();
				}
			}
		});

	}

	class chooserHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {

			int returnVal = _watermarkChooser.showOpenDialog(new JPanel());

			if (returnVal == JFileChooser.APPROVE_OPTION) {

				_waterMarkFile = _watermarkChooser.getSelectedFile();

				try {
					//Check if file is valid or not, and if it isn't then complain
					Process checkFile = BashCommandPanel.runBashCommand("file -ib "+"\"" + _waterMarkFile.getPath() + "\"" + " | grep \"image\"");
					checkFile.waitFor();
					if (checkFile.exitValue() != 0) {
						JOptionPane.showMessageDialog(new JPanel(), "Please select a image file!");
					}
					else{
						_imageChooserField.setText(_waterMarkFile.getPath());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public enum Effect { vFlip, hFlip, blur, watermark };

	// The Swing worker for adding effects.
	class EffectWorker extends SwingWorker<Void, Void> {

		String Command;
		Process sProcess;
		boolean _isCancelled = false;
		Effect effect = null;


		@Override
		protected Void doInBackground() throws Exception {

			try {
				sProcess = runBashCommand(Command);
				sProcess.waitFor();

			} catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				return null;	
			}
		}

		@Override
		protected void done() {

			_progressBar.setIndeterminate(false);
			if(sProcess.exitValue() != 0 && !_isCancelled) {
				_progressBar.setString("...ERROR!");
				_progressBar.validate();
				JOptionPane.showMessageDialog(new JFrame(), "Error! Image either invalid or too big!", "ERROR", JOptionPane.ERROR_MESSAGE);
			}	
			else if(_isCancelled) {
				if(effect == Effect.watermark) {
					try {
						sProcess = runBashCommand("rm " + _filePath + ".withWatermark.mp4");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else if(effect == Effect.vFlip) {
					try {
						sProcess = runBashCommand("rm " + _filePath + ".withVflip.mp4");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else if(effect == Effect.hFlip) {
					try {
						sProcess = runBashCommand("rm " + _filePath + ".withHflip.mp4");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else if(effect == Effect.blur) {
					try {
						sProcess = runBashCommand("rm " + _filePath + ".withBlur.mp4");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				_progressBar.setString("...Cancelled!");
				_progressBar.validate();
				JOptionPane.showMessageDialog(new JFrame(), "Effect Addition Cancelled!", "CANCELLED", JOptionPane.INFORMATION_MESSAGE);
			}
			else if(effect == Effect.watermark) {
				_progressBar.setString("...Successful!");
				_progressBar.validate();
				JOptionPane.showMessageDialog(new JFrame(), "Watermark Addition Successful\n" + "File saved as: " + _filePath + ".withWatermark.mp4", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
				MainFrame.getInstance().getVideoPlayer().playMedia(_filePath + ".withWatermark.mp4");	
				MainFrame.getInstance().getVideoPlayer().setFilePath(_filePath + ".withWatermark.mp4");
			}
			else if(effect == Effect.vFlip) {
				_progressBar.setString("...Successful!");
				_progressBar.validate();
				JOptionPane.showMessageDialog(new JFrame(), "Vertical Flip Successful!\n" + "File saved as: " + _filePath + ".withVflip.mp4", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
				MainFrame.getInstance().getVideoPlayer().playMedia(_filePath + ".withVflip.mp4");
				MainFrame.getInstance().getVideoPlayer().setFilePath(_filePath + ".withVflip.mp4");
			}
			else if(effect == Effect.hFlip) {
				_progressBar.setString("...Successful!");
				_progressBar.validate();
				JOptionPane.showMessageDialog(new JFrame(), "Horizontal Flip Successful!\n" + "File saved as: " + _filePath + ".withHflip.mp4", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
				MainFrame.getInstance().getVideoPlayer().playMedia(_filePath + ".withHflip.mp4");
				MainFrame.getInstance().getVideoPlayer().setFilePath(_filePath + ".withHflip.mp4");
			}
			else if(effect == Effect.blur) {
				_progressBar.setString("...Successful!");
				_progressBar.validate();
				JOptionPane.showMessageDialog(new JFrame(), "Blur Addition Successful!\n" + "File saved as: " + _filePath + ".withBlur.mp4", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
				MainFrame.getInstance().getVideoPlayer().playMedia(_filePath + ".withBlur.mp4");
				MainFrame.getInstance().getVideoPlayer().setFilePath(_filePath + ".withBlur.mp4");
			}
			_cancelButton.setEnabled(false);
			_progressBar.setVisible(false);

		}

		public void cancelWork() {
			// Destroys the process
			if (sProcess != null) {
				_isCancelled = true;
				sProcess.destroy();
			}
		}
	}
}
