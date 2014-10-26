package Vamix206;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.SwingWorker;
import javax.swing.border.BevelBorder;

/**
 * 
 * Gives the option of speeding up the video or slowing it down.
 * Minimum of divided by 16 and Maximum of 16 times
 * @author chester
 *
 */
public class SpeedAdjustPane extends BashCommandPanel {

	private JPanel _speedTop;
	private JPanel _speedBottom;
	private JButton _speedButton;
	private JLabel _speedLabel;
	private JSlider _speedSlider;
	private JButton _cancelButton;
	private JProgressBar _progressBar;

	private String _filePath;

	private EffectWorker _addEffectWorker = null;

	public SpeedAdjustPane() {

		setBorder(BorderFactory.createEtchedBorder(BevelBorder.RAISED));
		setLayout(new GridLayout(4,1));

		JPanel bp1 = new JPanel();
		bp1.setPreferredSize(new Dimension(840,50));

		_speedTop = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		_speedTop.setPreferredSize(new Dimension(840,50));

		_speedBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
		_speedBottom.setPreferredSize(new Dimension(840,50));

		_speedButton = new JButton("Speed up/down a video");
		_speedButton.setPreferredSize(new Dimension(160,40));
		JPanel bp31 = new JPanel();
		bp31.setPreferredSize(new Dimension(30,40));
		_speedLabel = new JLabel("Multiplier:");
		_speedLabel.setFont(new Font("Ubuntu", Font.PLAIN, 15));
		JPanel bp32 = new JPanel();
		bp32.setPreferredSize(new Dimension(10,40));


		_speedSlider = new JSlider(-16,16);
		_speedSlider.setPreferredSize(new Dimension(160,60));

		// Create the label table for the slider to help the user decide the speed.
		Hashtable labelTable = new Hashtable();
		labelTable.put( new Integer( 0 ), new JLabel("Normal") );
		labelTable.put( new Integer( -16 ), new JLabel("÷16") );
		labelTable.put( new Integer( 16 ), new JLabel("x16") );
		_speedSlider.setPaintTicks(false);
		_speedSlider.setLabelTable(labelTable);
		_speedSlider.setPaintLabels(true);

		JPanel bp2 = new JPanel();
		bp2.setPreferredSize(new Dimension(840,50));

		_cancelButton = new JButton("Cancel!");
		_cancelButton.setPreferredSize(new Dimension(60,40));
		_cancelButton.setVisible(false);

		JPanel bp3 = new JPanel();
		bp3.setPreferredSize(new Dimension(20,40));

		_progressBar = new JProgressBar();
		_progressBar.setPreferredSize(new Dimension(150,20));
		_progressBar.setIndeterminate(true);
		_progressBar.setVisible(false);
		_progressBar.setStringPainted(true);

		add(bp1);
		add(_speedTop);
		add(_speedBottom);
		add(bp2);

		_speedTop.add(_speedButton);
		_speedTop.add(bp31);
		_speedTop.add(_speedLabel);
		_speedTop.add(bp32);
		_speedTop.add(_speedSlider);

		_speedBottom.add(_cancelButton);
		_speedBottom.add(bp3);
		_speedBottom.add(_progressBar);

		// The cancel button for canceling removing/adding subtitles.
		_cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(_addEffectWorker != null) {
					_addEffectWorker.cancelWork();
				}
			}
		});


		_speedButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(MainFrame.getInstance().getVideoPlayer().getVideo().getVideoOutputs() < 1) {
					JOptionPane.showMessageDialog(new JFrame(), "No Video Selected!", "ERROR", JOptionPane.WARNING_MESSAGE);
				}
				else {
					if(_speedSlider.getValue() == -1 || _speedSlider.getValue() == 0 || _speedSlider.getValue() == 1) {
						JOptionPane.showMessageDialog(new JFrame(), "Video has remained on normal speed.", "MESSAGE", JOptionPane.INFORMATION_MESSAGE);
					}
					else {
						_filePath = MainFrame.getInstance().getVideoFile().getPath();
						_progressBar.setIndeterminate(true);
						_addEffectWorker = new EffectWorker();

						// Determines where speed up or speed down as the avconv and sox have different ways of speeding up/slowing down
						if(_speedSlider.getValue() > 0) {
							_progressBar.setString("Speeding up...");
							if(_speedSlider.getValue() > 10) {
								_addEffectWorker._videoSpeedValue = "0" + Integer.toString(1000/_speedSlider.getValue());
							}
							else {
								_addEffectWorker._videoSpeedValue = Integer.toString(1000/_speedSlider.getValue());
							}
							_addEffectWorker._musicSpeedValue = Integer.toString(_speedSlider.getValue());							
							_addEffectWorker._isSpedUp = true;
						}
						else {	
							_progressBar.setString("Slowing down...");
							if(_speedSlider.getValue() < -10) {
								_addEffectWorker._musicSpeedValue = "0" + Integer.toString(-1*1000/_speedSlider.getValue());
							}
							else {
								_addEffectWorker._musicSpeedValue = Integer.toString(-1*1000/_speedSlider.getValue());
							}
							_addEffectWorker._videoSpeedValue = Integer.toString(-1*_speedSlider.getValue());
							_addEffectWorker._isSpedUp = false;
						}

						_progressBar.validate();
						_progressBar.setVisible(true);
						_cancelButton.setVisible(true);
						_cancelButton.validate();

						_addEffectWorker.execute();
					}
				}
			}
		});
	}

	// The Swing worker for adding effects.
	class EffectWorker extends SwingWorker<Void, Void> {

		Process sProcess;
		boolean _isCancelled = false;
		boolean _isSpedUp;
		String _musicSpeedValue;
		String _videoSpeedValue;

		@Override
		protected Void doInBackground() throws Exception {

			try {
				// Extract Video
				sProcess = runBashCommand("avconv -y -i " + _filePath + " -an -vcodec copy " + _filePath + ".video.avi");
				sProcess.waitFor();

				// Extract Audio
				if(!_isCancelled) {
					sProcess = runBashCommand("avconv -y -i " + _filePath + " " + _filePath + ".audio.wav");
					sProcess.waitFor();
				}

				// Speed Video
				if(!_isCancelled) {
					if(_isSpedUp) {
						sProcess = runBashCommand("avconv -y -i " + _filePath + ".video.avi -filter:v \"setpts=0." + _videoSpeedValue + "*PTS\" -b 1080 " + _filePath + ".spedup.avi");
					}
					else {
						sProcess = runBashCommand("avconv -y -i " + _filePath + ".video.avi -filter:v \"setpts=" + _videoSpeedValue + ".0*PTS\" -b 1080 " + _filePath + ".spedup.avi");
					}
					sProcess.waitFor();
				}

				// Speed Audio
				if(!_isCancelled) {
					if(_isSpedUp) {
						sProcess = runBashCommand("sox " + _filePath + ".audio.wav " +  _filePath + ".spedup.wav speed " + _musicSpeedValue + ".0");
					}
					else {
						sProcess = runBashCommand("sox " + _filePath + ".audio.wav " +  _filePath + ".spedup.wav speed " + "0." + _musicSpeedValue);
					}

					sProcess.waitFor();
				}

				// Merge Audio and Video
				if(!_isCancelled) {
					sProcess = runBashCommand("avconv -y -i " + _filePath + ".spedup.wav -i " + _filePath + ".spedup.avi -c copy "+ _filePath + "SpeedAdjustment.avi");
					sProcess.waitFor();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				return null;	
			}
		}

		public void cancelWork() {
			if(_addEffectWorker != null) {
				_addEffectWorker.sProcess.destroy();
				_addEffectWorker._isCancelled = true;
			}		
		}

		@Override
		protected void done() {
			_progressBar.setIndeterminate(false);
			
			try {
				sProcess = runBashCommand("rm " + _filePath + ".audio.wav");
				sProcess = runBashCommand("rm " + _filePath + ".spedup.wav");
				sProcess = runBashCommand("rm " + _filePath + ".spedup.avi");
				sProcess = runBashCommand("rm " + _filePath + ".video.avi");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(_isCancelled) {
				JOptionPane.showMessageDialog(new JFrame(), "Speed Adjust Cancelled", "CANCELLED", JOptionPane.INFORMATION_MESSAGE);
				_progressBar.setString("...Cancelled!");
			}
			else {
				JOptionPane.showMessageDialog(new JFrame(), "Speed Adjust Completed!\nSaved as: " + _filePath + "SpeedAdjustment.avi", "SUCCESSFUL", JOptionPane.INFORMATION_MESSAGE);
				_progressBar.setString("...Completed!");
				MainFrame.getInstance().getVideoPlayer().playMedia(_filePath + "SpeedAdjustment.avi");
			}
			
			_progressBar.setVisible(false);
			_cancelButton.setVisible(false);
			_cancelButton.validate();		
		}
	}
}
