///////////////////////////////////////////////////////////////////////////////////
//
//  C212 Spring 16
//  Final Project
//
//  Author  Sander Altman saaltman
//  Last Edited: 4/22/16
//
//////////////////////////////////////////////////////////////////////////////////
package c212.breakout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class HelpFrame extends JFrame {
	public static final int WIDTH = 360, HEIGHT = 580, TOP_MARGIN = 25, BOTTOM_MARGIN = 20, BORDER_SIZE = 6,
			TITLE_SIZE = 30, TITLE_MARGIN = 20, BUTTON_MARGIN = 20;
	public static final Color BACKGROUND = new Color(0x202020), BORDER_COLOR = new Color(0xDDFFFF),
			TITLE_COLOR = new Color(0x90FFFF);
	public static final Font TITLE_FONT = new Font("Arial", Font.BOLD | Font.ITALIC, TITLE_SIZE);
	public static final String CONTROL_DIVIDER_SYMBOL = "----";
	
	//class for displaying the text in the help menu
	private class TextPanel extends JPanel {
		public static final int HEADER_SIZE = 20, TEXT_SIZE = 16, GAP = 6, TOP_MARGIN = 20, 
				SIDE_MARGIN = 10, BOTTOM_MARGIN = 10, BORDER_SIZE = 4;
		public final Font headerFont = new Font("Arial", Font.BOLD, HEADER_SIZE),
				textFont = new Font("Lucida Sans", Font.PLAIN, TEXT_SIZE);
		public final Color headerColor = new Color(0xCCCCFF), textColor = new Color(0xCCDDFF),
				borderColor = new Color(0xC8DFDF);
		
		private JLabel header;
		private JTextPane leftText = new JTextPane(), centerText = new JTextPane(), rightText = new JTextPane();
		private JPanel textContainer = new JPanel();
		
		//main panel constructor
		private TextPanel(String header, boolean first) {
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			setBackground(BACKGROUND);
			if (first) setBorder(BorderFactory.createMatteBorder(
	                BORDER_SIZE, 0, BORDER_SIZE, 0, borderColor));
			else setBorder(BorderFactory.createMatteBorder(
	                0, 0, BORDER_SIZE, 0, borderColor));
			setAlignmentX(CENTER_ALIGNMENT);
			
			this.header = new JLabel(header);
			this.header.setFont(headerFont);
			this.header.setForeground(headerColor);
			this.header.setAlignmentX(CENTER_ALIGNMENT);
			
			leftText.setFont(textFont);
			leftText.setForeground(textColor);
			leftText.setBackground(BACKGROUND);
			leftText.setEditable(false);
			
			centerText.setFont(textFont);
			centerText.setForeground(textColor);
			centerText.setBackground(BACKGROUND);
			centerText.setEditable(false);
			
			rightText.setFont(textFont);
			rightText.setForeground(textColor);
			rightText.setBackground(BACKGROUND);
			rightText.setEditable(false);
			
			textContainer.setLayout(new BoxLayout(textContainer, BoxLayout.X_AXIS));
			textContainer.setBackground(BACKGROUND);
			
			add(Box.createRigidArea(new Dimension(1, TOP_MARGIN)));
			add(this.header);
			add(textContainer);
			add(Box.createRigidArea(new Dimension(1, BOTTOM_MARGIN)));
		}
		
		//constructor for text panel with one column of text
		public TextPanel(String header, String centerString, boolean first) {
			this(header, first);
			centerText.setText(centerString);
			centerText.setMargin(new Insets(GAP, SIDE_MARGIN, 0, SIDE_MARGIN));
			
			StyledDocument doc = centerText.getStyledDocument();
			SimpleAttributeSet alignmentAttrbs = new SimpleAttributeSet();
			StyleConstants.setAlignment(alignmentAttrbs, StyleConstants.ALIGN_CENTER);
			doc.setParagraphAttributes(0, doc.getLength(), alignmentAttrbs, false);
			
			textContainer.add(centerText);
		}
		
		//constructor for text panel with three columns of text
		public TextPanel(String header, String[] leftStrings, String[] rightStrings, boolean first) {
			this(header, first);
			
			if (leftStrings.length > 0) {
				leftText.setText(leftStrings[0]);
				centerText.setText(CONTROL_DIVIDER_SYMBOL);
				rightText.setText(rightStrings[0]);
				
				for (int i = 1; i < leftStrings.length; ++i) {
					leftText.setText(leftText.getText() + "\n" + leftStrings[i]);
					centerText.setText(centerText.getText() + "\n" + CONTROL_DIVIDER_SYMBOL);
					rightText.setText(rightText.getText() + "\n" + rightStrings[i]);
				}
			}
			
			leftText.setMargin(new Insets(GAP, SIDE_MARGIN, 0, 0));
			centerText.setMargin(new Insets(GAP, 0, 0, 0));
			rightText.setMargin(new Insets(GAP, 0, 0, SIDE_MARGIN));
			
			SimpleAttributeSet alignmentAttrbs = new SimpleAttributeSet();
			StyledDocument doc;
			
			StyleConstants.setAlignment(alignmentAttrbs, StyleConstants.ALIGN_LEFT);
			doc = leftText.getStyledDocument();
			doc.setParagraphAttributes(0, doc.getLength(), alignmentAttrbs, false);
			
			StyleConstants.setAlignment(alignmentAttrbs, StyleConstants.ALIGN_CENTER);
			doc = centerText.getStyledDocument();
			doc.setParagraphAttributes(0, doc.getLength(), alignmentAttrbs, false);
			
			StyleConstants.setAlignment(alignmentAttrbs, StyleConstants.ALIGN_RIGHT);
			doc = rightText.getStyledDocument();
			doc.setParagraphAttributes(0, doc.getLength(), alignmentAttrbs, false);
			
			textContainer.add(leftText);
			textContainer.add(centerText);
			textContainer.add(rightText);
		}
	}

	private JPanel panel = new JPanel();
	private JLabel title = new JLabel("Breakout");
	private TextPanel objectivePanel = new TextPanel("Objective",
			"Using the left and right arrow keys, move the paddle " + 
			"at the bottom of the screen to hit the ball towards the " +
			"bricks at the top of the screen. When the ball hits a " +
			"brick, the brick will be destroyed. The goal is to eliminate " +
			"as many bricks as you can.", true);
	private TextPanel controlsPanel = new TextPanel("Controls",
			new String[] {"<-- & -->",
					"Space", "R", "N", "H", "0 - 9"}, 
			new String[] {"Moves paddle.", "Pauses game.",
					"Restarts game.", "Advances level.",
					"Shows help menu.", "Sets speed."}, false);
	private JButton okBtn = new JButton("Ok");

	//constructor for frame
	public HelpFrame() {
		super("Help");
		setSize(WIDTH, HEIGHT);
		setResizable(false);
		setUndecorated(true);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(BACKGROUND);
		panel.setBorder(BorderFactory.createMatteBorder(
                BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_COLOR));
		
		title.setFont(TITLE_FONT);
		title.setForeground(TITLE_COLOR);
		title.setAlignmentX(CENTER_ALIGNMENT);
		
		okBtn.setAlignmentX(CENTER_ALIGNMENT);
		okBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BreakoutFrame.hideHelp();
			}
		});
		
		panel.add(Box.createRigidArea(new Dimension(1, TOP_MARGIN)));
		panel.add(title);
		panel.add(Box.createRigidArea(new Dimension(1, TITLE_MARGIN)));
		panel.add(objectivePanel);
		panel.add(controlsPanel);
		panel.add(Box.createRigidArea(new Dimension(1, BUTTON_MARGIN)));
		panel.add(okBtn);
		panel.add(Box.createRigidArea(new Dimension(1, BOTTOM_MARGIN)));
		
		add(panel);
	}
	
	//shows help frame
	public void display(int x, int y) {
		setLocation(x, y);
		setVisible(true);
	}
}
