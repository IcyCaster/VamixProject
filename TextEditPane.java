package VamixProject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.BevelBorder;

/**
 * Used for text editing. Heavily edited from assignmennt 3.
 * Hase the capability to:
 * - Add a text
 * - Select size
 * - Select Color
 * - Select font
 * - Add shadow
 * - Select position
 * - Beginning or end
 * - Duration
 * @author chester
 *
 */
public class TextEditPane extends BashCommandPanel {

	// Overall big panels.
	private JPanel _optionsHolder;
	private JPanel _buttonHolderHolder;
	private JPanel _buttonHolder;

	// Text Options.
	private JPanel _textInsertPanel;
	private JPanel _textLabelPanel;
	private JTextField _textToBeAdded;

	// Scene and Duration Options.
	private JPanel _sceneAndDurationPanel;
	private JPanel _sceneLabelPanel;
	private JComboBox _openingOrClosing;
	private JPanel _durationLabelPanel;
	private JTextField _timePeriodOfText;

	// Font, Size and Colour Options.
	private JPanel _fontAndColorPanel;
	private JPanel _fontLabelPanel;
	private JComboBox _fontSelection;
	private JPanel _sizeLabelPanel;
	private JComboBox  _sizeSelection;
	private JPanel _colourLabelPanel;
	private JButton _colourChooserButton;
	private JPanel _colourDisplay;

	// Position and Shadow Options.
	private JPanel _positionAndShadowPanel;
	private JPanel _positionLabelPanel;
	private JComboBox _positionSelection;
	private JPanel _shadowLabelPanel;
	private JCheckBox _shadowCheck;

	// Other Panels.
	private JPanel _progressPanel;
	private JPanel _blankPanel;

	// Progress Bar.
	private JProgressBar _progressBar;

	// Text Labels.
	private JLabel _textLabel = new JLabel("Text to be added (Max 30 characters): ");
	private JLabel _sceneLabel = new JLabel("Add text to this scene: ");
	private JLabel _durationLabel = new JLabel("Duration: ");
	private JLabel _fontLabel = new JLabel("Select Font: ");
	private JLabel _sizeLabel = new JLabel("Select Size: ");
	private JLabel _colourLabel = new JLabel("Select Colour: ");
	private JLabel _positionLabel = new JLabel("Select Position: ");
	private JLabel _shadowLabel = new JLabel("Do you want the text to have a shadow?");

	// Buttons.
	private JButton _addTextButton;
	private JButton _showPreviewButton;

	private JButton _cancelButton;

	private JButton _saveStateButton;
	private JButton _loadStateButton;

	// Fields for command.
	private String _filePath;
	private String _fontFile;
	private String _fontColour;
	private String _shadow;
	private int _positionX;
	private int _positionY;
	private String _duration;

	// Other Fields.
	private String _hexColour = "0x010101";
	private String _drawTextInput;
	private boolean _shadowBoolean = false;

	// Swing Worker
	private TextEditorWorker _addText = null;


	// Locations where the file will be saved.
	private String _userHome = System.getProperty("user.home");
	private File _saveDirectory = new File(_userHome + "/.textEdit");
	private File _saveFile = new File(_userHome + "/.textEdit/savedEdit.txt");

	public TextEditPane() {

		/**
		 * This creates all of the PRIMARY GUI for this panel.
		 */

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(BorderFactory.createEtchedBorder(BevelBorder.RAISED));

		// Creates the options holder.
		_optionsHolder = new JPanel(new GridLayout(4,1));
		_optionsHolder.setPreferredSize(new Dimension(840,160));
		add(_optionsHolder);

		// Creates the options panels.
		_textInsertPanel = new JPanel();
		_sceneAndDurationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		_sceneAndDurationPanel.setPreferredSize(new Dimension(840,40));
		_fontAndColorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		_positionAndShadowPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

		// Adds the options panels.
		_optionsHolder.add(_textInsertPanel);
		_optionsHolder.add(_sceneAndDurationPanel);
		_optionsHolder.add(_fontAndColorPanel);
		_optionsHolder.add(_positionAndShadowPanel);

		// Progress Panel.
		_progressPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		_progressPanel.setBackground(Color.LIGHT_GRAY);
		_progressPanel.setPreferredSize(new Dimension(840,15));
		add(_progressPanel);

		// A space holder panel, is blank.
		_blankPanel = new JPanel();
		_blankPanel.setBackground(Color.WHITE);
		_blankPanel.setPreferredSize(new Dimension(840,5));
		add(_blankPanel);

		// Create the button holder.
		_buttonHolderHolder = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		_buttonHolderHolder.setPreferredSize(new Dimension(740,50));
		_buttonHolder = new JPanel(new GridLayout(1,4));
		_buttonHolderHolder.add(_buttonHolder, BorderLayout.CENTER);
		add(_buttonHolderHolder);

		/**
		 * This creates all of the SECONDARY (detailed) GUI for this panel.
		 */

		// First ROW
		_textLabelPanel = new JPanel();
		_textToBeAdded = new JTextField("-Insert Text Here-", 30);

		_textLabel.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		_textLabelPanel.add(_textLabel, BorderLayout.CENTER);


		// Second ROW 
		_sceneLabelPanel = new JPanel();
		_sceneLabel.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		_sceneLabelPanel.add(_sceneLabel, BorderLayout.CENTER);

		String[] sceneOptions = new String[3];
		sceneOptions[0] = ("-Select-");
		sceneOptions[1] = ("Opening Scene");
		sceneOptions[2] = ("Closing Scene");
		_openingOrClosing = new JComboBox(sceneOptions);

		JPanel blank1 = new JPanel();
		blank1.setPreferredSize(new Dimension(20,20));

		_durationLabelPanel = new JPanel();
		_durationLabel.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		_durationLabelPanel.add(_durationLabel, BorderLayout.CENTER);

		_timePeriodOfText = new JTextField("60", 6);

		// Accepts only backspace and digits 
		_timePeriodOfText.addKeyListener(new KeyAdapter() { 
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


		// Third ROW
		_fontLabelPanel = new JPanel();
		_fontLabel.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		_fontLabelPanel.add(_fontLabel, BorderLayout.CENTER);

		String[] fontOptions = new String[6];
		fontOptions[0] = ("Mono (Regular)");
		fontOptions[1] = ("Mono (Bold)");
		fontOptions[2] = ("Sans (Regular)");
		fontOptions[3] = ("Sans (Bold)");
		fontOptions[4] = ("Serif (Regular)");
		fontOptions[5] = ("Serif (Bold)");
		_fontSelection = new JComboBox(fontOptions);

		JPanel blank2 = new JPanel();
		blank2.setPreferredSize(new Dimension(20,20));

		_sizeLabelPanel = new JPanel();
		_sizeLabel.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		_sizeLabelPanel.add(_sizeLabel, BorderLayout.CENTER);

		String[] sizeOptions = new String[65];
		for(int i = 8; i < 73; i++) {
			sizeOptions[i-8] = Integer.toString(i);
		}
		_sizeSelection = new JComboBox(sizeOptions);

		JPanel blank3 = new JPanel();
		blank3.setPreferredSize(new Dimension(20,20));

		_colourLabelPanel = new JPanel();
		_colourLabel.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		_colourLabelPanel.add(_colourLabel, BorderLayout.CENTER);

		_colourChooserButton = new JButton("Choose Colour:");
		_colourDisplay = new JPanel();
		_colourDisplay.setPreferredSize(new Dimension(25,25));
		_colourDisplay.setBorder(BorderFactory.createEtchedBorder(BevelBorder.RAISED));
		_colourDisplay.setBackground(Color.BLACK);


		// Fourth ROW
		_positionLabelPanel = new JPanel();
		_positionLabel.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		_positionLabelPanel.add(_positionLabel, BorderLayout.CENTER);

		String[] positionOptions = new String[6];
		positionOptions[0] = ("Top-Left");
		positionOptions[1] = ("Top-Center");
		positionOptions[2] = ("Middle-Left");
		positionOptions[3] = ("Middle-Center");
		positionOptions[4] = ("Bottom-Left");
		positionOptions[5] = ("Bottom-Center");
		_positionSelection = new JComboBox(positionOptions);

		JPanel blank4 = new JPanel();
		blank4.setPreferredSize(new Dimension(20,20));

		_shadowLabelPanel = new JPanel();
		_shadowLabel.setFont(new Font("Ubuntu", Font.PLAIN, 16));
		_shadowLabelPanel.add(_shadowLabel, BorderLayout.CENTER);

		_shadowCheck = new JCheckBox();


		// Buttons
		_addTextButton = new JButton("Add this text!");
		_addTextButton.setPreferredSize(new Dimension(150,50));
		_showPreviewButton = new JButton("Preview this text!");
		_cancelButton = new JButton("Cancel!");		
		Color cbColour = Color.decode("#ff7900");
		_cancelButton.setBackground(cbColour);
		_cancelButton.setFont(new Font("Ubuntu", Font.BOLD, 20));
		_cancelButton.setOpaque(true);
		_cancelButton.setVisible(false);
		_saveStateButton = new JButton("Save Current Edit");
		_loadStateButton = new JButton("Load Last Edit");

		_progressBar = new JProgressBar();
		_progressBar.setPreferredSize(new Dimension(870,20));
		_progressBar.setStringPainted(true);
		_progressBar.setIndeterminate(true);
		_progressBar.setVisible(false);

		// Adding Swing Components to the several rows.
		_textInsertPanel.add(_textLabelPanel);
		_textInsertPanel.add(_textToBeAdded);

		_sceneAndDurationPanel.add(_sceneLabel);
		_sceneAndDurationPanel.add(_openingOrClosing);
		_sceneAndDurationPanel.add(blank1);
		_sceneAndDurationPanel.add(_durationLabel);
		_sceneAndDurationPanel.add(_timePeriodOfText);

		_fontAndColorPanel.add(_fontLabel);
		_fontAndColorPanel.add(_fontSelection);
		_fontAndColorPanel.add(blank2);
		_fontAndColorPanel.add(_sizeLabel);
		_fontAndColorPanel.add(_sizeSelection);
		_fontAndColorPanel.add(blank3);
		_fontAndColorPanel.add(_colourLabel);
		_fontAndColorPanel.add(_colourChooserButton);
		_fontAndColorPanel.add(_colourDisplay);

		_positionAndShadowPanel.add(_positionLabelPanel);
		_positionAndShadowPanel.add(_positionSelection);
		_positionAndShadowPanel.add(blank4);
		_positionAndShadowPanel.add(_shadowLabelPanel);
		_positionAndShadowPanel.add(_shadowCheck);

		_progressPanel.add(_progressBar);

		_buttonHolder.add(_addTextButton);
		_buttonHolder.add(_showPreviewButton);
		_buttonHolder.add(_cancelButton);
		_buttonHolder.add(_saveStateButton);
		_buttonHolder.add(_loadStateButton);

		_colourChooserButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// Creates the color chooser.
				Color c = JColorChooser.showDialog(null, "Choose a Color", _colourDisplay.getForeground());
				if (c != null) {	
					// Displays chosen color.
					_colourDisplay.setBackground(c);
					String hexCode = String.format("%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
					_hexColour = "0x" + hexCode;
				}
			}
		});

		_addTextButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				// Makes sure there is a video to add text to.
				if(MainFrame.getInstance().getVideoPlayer().getVideo().getVideoOutputs() < 1) {
					JOptionPane.showMessageDialog(new JFrame(), "No Video Selected!", "ERROR", JOptionPane.WARNING_MESSAGE);
				}
				else {
					// Makes sure text is being added.
					if(_textToBeAdded.getText().equals("")) {
						JOptionPane.showMessageDialog(new JFrame(), "No text added!", "ERROR", JOptionPane.WARNING_MESSAGE);
					}
					// Makes sure text is less than 30 characters.
					else if(_textToBeAdded.getText().length() >= 30) {
						JOptionPane.showMessageDialog(new JFrame(), "Maximum 30 characters!", "ERROR", JOptionPane.WARNING_MESSAGE);
					}
					else {
						// Making sure that there is a selection in the opening/closing scene option
						if(_openingOrClosing.getSelectedItem( ).equals( "-Select-" )) {
							JOptionPane.showMessageDialog(new JFrame(), "Scene Selection Missing!", "ERROR", JOptionPane.WARNING_MESSAGE);
						}
						else {
							// Determines if it is at the opening or closing scene.
							if(_openingOrClosing.getSelectedItem().equals( "Opening Scene")) {
								_drawTextInput = "lt";
							}
							else {
								_drawTextInput = "gt";
							}
							if(_timePeriodOfText.getText().equals("")) {
								JOptionPane.showMessageDialog(new JFrame(), "Time Period Missing!", "ERROR", JOptionPane.WARNING_MESSAGE);
							}
							else {
								// Appends the time to either lt or gt.
								if(_drawTextInput.equals("lt")) {
									_duration = "(t," + _timePeriodOfText.getText() + ")";
								}
								else {
									_duration = "(t," + Integer.toString((int)(MainFrame.getInstance().getVideoPlayer().getVideo().getLength()/1000)
											- Integer.parseInt(_timePeriodOfText.getText())) + ")"; // GET THE VIDEO's LENGTH and minus the video.getLength
								}

								_fontColour = ":fontcolor=" + _hexColour;

								// Decides the font type.
								if(_fontSelection.getSelectedItem().equals("Mono (Regular)")) {
									_fontFile = "FreeMono.ttf";
								}
								else if(_fontSelection.getSelectedItem().equals("Mono (Bold)")) {
									_fontFile = "FreeMonoBold.ttf";
								}
								else if(_fontSelection.getSelectedItem().equals("Sans (Regular)")) {
									_fontFile = "FreeSans.ttf	";
								}
								else if(_fontSelection.getSelectedItem().equals("Sans (Bold)")) {
									_fontFile = "FreeSansBold.ttf";
								}
								else if(_fontSelection.getSelectedItem().equals("Serif (Regular)")) {
									_fontFile = "FreeSerif.ttf";
								}
								else if(_fontSelection.getSelectedItem().equals("Serif (Bold)")) {
									_fontFile = "FreeSerifBold.ttf";
								}

								// Select the position
								if(_positionSelection.getSelectedItem().equals("Top-Left")) {
									_positionX = 0;
									_positionY = 0;
								}
								else if(_positionSelection.getSelectedItem().equals("Top-Center")) {
									_positionX = MainFrame.getInstance().getVideoPlayer().getVideo().getVideoDimension().width/2;
									_positionY = 0;	
								}
								else if(_positionSelection.getSelectedItem().equals("Middle-Left")) {
									_positionX = 0;
									_positionY = MainFrame.getInstance().getVideoPlayer().getVideo().getVideoDimension().height/2;
								}
								else if(_positionSelection.getSelectedItem().equals("Middle-Center")) {
									_positionX = MainFrame.getInstance().getVideoPlayer().getVideo().getVideoDimension().width/2;
									_positionY = MainFrame.getInstance().getVideoPlayer().getVideo().getVideoDimension().height/2;
								}
								else if(_positionSelection.getSelectedItem().equals("Bottom-Left")) {
									_positionX = 0;
									_positionY = MainFrame.getInstance().getVideoPlayer().getVideo().getVideoDimension().height;
								}
								else if(_positionSelection.getSelectedItem().equals("Bottom-Center")) {
									_positionX = MainFrame.getInstance().getVideoPlayer().getVideo().getVideoDimension().width/2;
									_positionY = MainFrame.getInstance().getVideoPlayer().getVideo().getVideoDimension().height;
								}

								// Checks shadow
								if(_shadowCheck.isSelected()) {
									_shadow = ":shadowx=3:shadowy=3";
								}
								else if(!_shadowCheck.isSelected()) {
									_shadow = ":shadowx=0:shadowy=0";
								}							

								int n = JOptionPane.showConfirmDialog(
										new JFrame(), "Once text is added to a video"
												+ " it is permanent!\n"
												+ "Is that okay???",
												"Confirm!",
												JOptionPane.YES_NO_OPTION);

								if (n == 0) {
									// Creates the swing worker and sets the progress bar to indeterminate.
									_progressBar.setIndeterminate(true);
									_progressBar.setVisible(true);
									_progressBar.setString("Adding text...");
									_progressBar.repaint();
									_cancelButton.setVisible(true);
									_filePath = MainFrame.getInstance().getVideoFile().getPath();
									_addText = new TextEditorWorker();
									_addText.execute();
								}						
							}
						}
					}
				}
			}
		});

		// Shows a preview of the text.
		_showPreviewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				// Makes sure there is a video to add text to.
				if(MainFrame.getInstance().getVideoPlayer().getVideo().getVideoOutputs() < 1) {
					JOptionPane.showMessageDialog(new JFrame(), "No Video Selected!", "ERROR", JOptionPane.WARNING_MESSAGE);
				}
				else {
					// Makes sure text is being added.
					if(_textToBeAdded.getText().equals("")) {
						JOptionPane.showMessageDialog(new JFrame(), "No text added!", "ERROR", JOptionPane.WARNING_MESSAGE);
					}
					// Makes sure text is less than 30 characters.
					else if(_textToBeAdded.getText().length() > 30) {
						JOptionPane.showMessageDialog(new JFrame(), "Maximum 30 characters!", "ERROR", JOptionPane.WARNING_MESSAGE);
					}
					else {
						// Making sure that there is a selection in the opening/closing scene option
						if(_openingOrClosing.getSelectedItem( ).equals( "-Select-" )) {
							JOptionPane.showMessageDialog(new JFrame(), "Scene Selection Missing!", "ERROR", JOptionPane.WARNING_MESSAGE);
						}
						else {
							// Determines if it is at the opening or closing scene.
							if(_openingOrClosing.getSelectedItem().equals( "Opening Scene")) {
								_drawTextInput = "lt";
							}
							else {
								_drawTextInput = "gt";
							}
							if(_timePeriodOfText.getText().equals("")) {
								JOptionPane.showMessageDialog(new JFrame(), "Time Period Missing!", "ERROR", JOptionPane.WARNING_MESSAGE);
							}
							else {
								// Appends the time to either lt or gt.
								if(_drawTextInput.equals("lt")) {
									_duration = "(t," + _timePeriodOfText.getText() + ")";
								}
								else {
									// This is the duration of the text and the end of scene. The total length of clip(milliseconds) / 1000 [converted to seconds]
									// and then minus the desired duration. This gives the time to start adding the text to the video.
									_duration = "(t," + Integer.toString((int)(MainFrame.getInstance().getVideoPlayer().getVideo().getLength()/1000)
											- Integer.parseInt(_timePeriodOfText.getText())) + ")"; 
								}

								_fontColour = ":fontcolor=" + _hexColour;

								// Decides the font type.
								if(_fontSelection.getSelectedItem().equals("Mono (Regular)")) {
									_fontFile = "FreeMono.ttf";
								}
								else if(_fontSelection.getSelectedItem().equals("Mono (Bold)")) {
									_fontFile = "FreeMonoBold.ttf";
								}
								else if(_fontSelection.getSelectedItem().equals("Sans (Regular)")) {
									_fontFile = "FreeSans.ttf	";
								}
								else if(_fontSelection.getSelectedItem().equals("Sans (Bold)")) {
									_fontFile = "FreeSansBold.ttf";
								}
								else if(_fontSelection.getSelectedItem().equals("Serif (Regular)")) {
									_fontFile = "FreeSerif.ttf";
								}
								else if(_fontSelection.getSelectedItem().equals("Serif (Bold)")) {
									_fontFile = "FreeSerifBold.ttf";
								}

								// Select the position
								if(_positionSelection.getSelectedItem().equals("Top-Left")) {
									_positionX = 0;
									_positionY = 0;
								}
								else if(_positionSelection.getSelectedItem().equals("Top-Center")) {
									_positionX = MainFrame.getInstance().getVideoPlayer().getVideo().getVideoDimension().width/2;
									_positionY = 0;	
								}
								else if(_positionSelection.getSelectedItem().equals("Middle-Left")) {
									_positionX = 0;
									_positionY = MainFrame.getInstance().getVideoPlayer().getVideo().getVideoDimension().height/2;
								}
								else if(_positionSelection.getSelectedItem().equals("Middle-Center")) {
									_positionX = MainFrame.getInstance().getVideoPlayer().getVideo().getVideoDimension().width/2;
									_positionY = MainFrame.getInstance().getVideoPlayer().getVideo().getVideoDimension().height/2;
								}
								else if(_positionSelection.getSelectedItem().equals("Bottom-Left")) {
									_positionX = 0;
									_positionY = MainFrame.getInstance().getVideoPlayer().getVideo().getVideoDimension().height;
								}
								else if(_positionSelection.getSelectedItem().equals("Bottom-Center")) {
									_positionX = MainFrame.getInstance().getVideoPlayer().getVideo().getVideoDimension().width/2;
									_positionY = MainFrame.getInstance().getVideoPlayer().getVideo().getVideoDimension().height;
								}

								// Checks shadow
								if(_shadowCheck.isSelected()) {
									_shadow = ":shadowx=3:shadowy=3";
								}
								else if(!_shadowCheck.isSelected()) {
									_shadow = ":shadowx=0:shadowy=0";
								}

								_filePath = MainFrame.getInstance().getVideoFile().getPath();

								final JFrame previewText = new JFrame();
								previewText.setSize(640,460);  

								VideoPlayer previewVideoPlayer = new VideoPlayer();
								previewVideoPlayer.setPreferredSize(new Dimension(640, 460));
								previewText.add(previewVideoPlayer, BorderLayout.CENTER);
								previewText.setVisible(true);
								previewText.pack();

								Process sp;

								try {

									sp = runBashCommand("avconv -i " + _filePath + " -vf \"drawtext=fontfile='/usr/share/" +
											"fonts/truetype/freefont/" + _fontFile + "':text='" + _textToBeAdded.getText() + "':fontsize=" 
											+ _sizeSelection.getSelectedItem() + _fontColour + ":x=" + _positionX + ":y=" + _positionY + 
											_shadow + "\" -strict experimental -t 3 " + _filePath + "preview.mp4");

									sp.waitFor();

									previewVideoPlayer.playMedia(_filePath + "preview.mp4");

								} catch (Exception e) {
									e.printStackTrace();
								}

								// When the screen closes dispose to the frame and delete the preview file.
								previewText.addWindowListener(new java.awt.event.WindowAdapter() {
									@Override
									public void windowClosing(WindowEvent e) {
										Process sp;
										try {
											sp = runBashCommand("rm " + _filePath + "preview.mp4");
										} catch (Exception e1) {
											e1.printStackTrace();
										}
										previewText.dispose();
									}
								});
							}
						}
					}
				}
			}
		});

		// The cancel button for cancelling adding text.
		_cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(_addText != null) {
					_addText.cancelWork();
				}
			}
		});

		// Saves the edit state.
		_saveStateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Deletes the old save file.
				if(_saveFile.exists()) {
					try {
						Files.delete(_saveFile.toPath());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				// Checks to make sure there is directory to save to.
				if(!_saveDirectory.exists()) {
					// Makes a new log file and directory.
					_saveDirectory.mkdir();
					try {
						_saveFile.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				// Checks to make sure there is a file to save to.
				else if(!_saveFile.exists()) {
					try {
						// Just makes a new directory.
						_saveFile.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				if(_shadowCheck.isSelected()) {
					_shadowBoolean = true;
				}
				else if(!_shadowCheck.isSelected()) {
					_shadowBoolean = false;
				}

				// Writes the relevant information to the log file.
				PrintWriter writer;
				try {
					writer = new PrintWriter(_saveFile, "UTF-8");

					writer.println(_textToBeAdded.getText());
					writer.println(_openingOrClosing.getSelectedItem());
					writer.println(_timePeriodOfText.getText());
					writer.println(_fontSelection.getSelectedItem());
					writer.println(_sizeSelection.getSelectedItem());
					writer.println(_hexColour);
					writer.println(_positionSelection.getSelectedItem());
					writer.println(_shadowBoolean);

					writer.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				JOptionPane.showMessageDialog(new JFrame(), "Text Edit State Saved!", "SAVED", JOptionPane.INFORMATION_MESSAGE);
			}
		});

		// Loads the last saved state.
		_loadStateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Makes sure there is a previous saved state.
				if (_saveDirectory.exists() && _saveFile.exists()) {				
					try {
						// Reads the file.
						BufferedReader br = new BufferedReader(new FileReader(_saveFile));

						_textToBeAdded.setText(br.readLine());
						_openingOrClosing.setSelectedItem(br.readLine());
						_timePeriodOfText.setText(br.readLine());
						_fontSelection.setSelectedItem(br.readLine());
						_sizeSelection.setSelectedItem(br.readLine());
						_hexColour = br.readLine();		
						_positionSelection.setSelectedItem(br.readLine());
						if(br.readLine().equals("true")) {
							_shadowBoolean = true;
						}
						else {
							_shadowBoolean = false;
						}
						Color cdColour = Color.decode(_hexColour);
						_colourDisplay.setBackground(cdColour);

					} catch (IOException e1) {
						e1.printStackTrace();
					}

					if(_shadowBoolean) {
						_shadowCheck.setSelected(true);
					}
					else if(!_shadowBoolean) {
						_shadowCheck.setSelected(false);
					}
				}
				else {
					JOptionPane.showMessageDialog(new JFrame(), "No Previous State Saved!", "ERROR", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
	}

	// The Swing worker for adding text.
	class TextEditorWorker extends SwingWorker<Void, Void> {

		Process sProcess;
		boolean isCancelled = false;

		@Override
		protected Void doInBackground() throws Exception {

			try {

				sProcess = runBashCommand("avconv -i " + _filePath + " -vf \"drawtext=fontfile='/usr/share/" +
						"fonts/truetype/freefont/" + _fontFile + "':text='" + _textToBeAdded.getText() + "':fontsize=" 
						+ _sizeSelection.getSelectedItem() + _fontColour + ":x=" + _positionX + ":y=" + _positionY + 
						_shadow + ":draw='" + _drawTextInput + _duration + "'\" -strict experimental " + _filePath + ".mp4");

				sProcess.waitFor();
				if(!isCancelled) {
					// Removes old video copy.
					sProcess = runBashCommand("rm " + _filePath);

					// Changes the name of the new copy to the same as the old copy.
					sProcess = runBashCommand("mv " + _filePath + ".mp4 " + _filePath);
				}

			} catch (Exception e) {
				e.printStackTrace();
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
				JOptionPane.showMessageDialog(new JFrame(), "Text Addition Cancelled", "CANCELLED", JOptionPane.INFORMATION_MESSAGE);
			}
			// Successful adding text.
			else {
				_progressBar.setIndeterminate(false);
				_progressBar.setString("...Completed!");
				_progressBar.validate();
				JOptionPane.showMessageDialog(new JFrame(), "Text Addition Successful", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
			}

			_progressBar.setVisible(false);
			_cancelButton.setVisible(false);

			MainFrame.getInstance().getVideoPlayer().getVideoPlayer().getMediaPlayer().playMedia(_filePath);
		}

		public void cancelWork(){
			// Destroys the process
			if (sProcess != null) {
				isCancelled = true;
				sProcess.destroy();

				// Removes generated temp file.
				try {
					sProcess = runBashCommand("rm " + _filePath + ".mp4 ");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
