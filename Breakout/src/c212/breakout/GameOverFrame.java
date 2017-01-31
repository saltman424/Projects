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
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class GameOverFrame extends JFrame {
	public static final int WIDTH = 300, HEIGHT = 180, TOP_MARGIN = 35, BOTTOM_MARGIN = 35, BORDER_SIZE = 6;
	public static final Color BACKGROUND = new Color(0x202020), BORDER = new Color(0xDDFFFF);
	
	private class TextPanel extends JPanel {
		public static final int TEXT_SIZE = 30;
		public final Font font = new Font("Arial", Font.BOLD, TEXT_SIZE);
		public final Color textColor = new Color(0xDDDDFF);
		
		private JLabel gameOver = new JLabel("Game Over!");
		
		//constructor for text panel (in case I had decided to display more than just "Game Over!")
		public TextPanel() {
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			setBackground(BACKGROUND);
			setAlignmentX(CENTER_ALIGNMENT);
			
			gameOver.setFont(font);
			gameOver.setForeground(textColor);
			gameOver.setAlignmentX(CENTER_ALIGNMENT);
			
			add(Box.createRigidArea(new Dimension(1, TOP_MARGIN)));
			add(gameOver);
			add(Box.createGlue());
		}
	}
	
	private class ButtonPanel extends JPanel {
		public static final int SIDE_MARGIN = 45, BUTTON_WIDTH = 80, BUTTON_HEIGHT = 30;
		
		private JButton btnExit = new JButton("Exit"), btnRestart = new JButton("Restart");
		
		//constructor for button panel
		public ButtonPanel() {
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			setBackground(BACKGROUND);
			setAlignmentX(CENTER_ALIGNMENT);
			
			btnExit.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
			btnRestart.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
			
			btnExit.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					BreakoutFrame.close();
					BreakoutFrame.closeGameOverFrame(true);
					dispose();
				}
			});
			btnRestart.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					BreakoutFrame.restart();
					BreakoutFrame.closeGameOverFrame(false);
				}
			});
			
			add(Box.createRigidArea(new Dimension(SIDE_MARGIN, 1)));
			add(btnExit);
			add(Box.createHorizontalGlue());
			add(btnRestart);
			add(Box.createRigidArea(new Dimension(SIDE_MARGIN, 1)));
		}
	}
	
	//GRADE-START (The above is relevant to the frame,
	//              but I'm assuming you just care that I have a frame, not what's on it)
	private TextPanel textPanel = new TextPanel();
	private ButtonPanel buttonPanel = new ButtonPanel();
	private JPanel panel = new JPanel();

	//constructor for frame
	public GameOverFrame() {
		super("Game Over");
		setSize(WIDTH, HEIGHT);
		setResizable(false);
		setUndecorated(true);
		setAlwaysOnTop(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(BACKGROUND);
		panel.setBorder(BorderFactory.createMatteBorder(
                BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER));
		
		panel.add(textPanel);
		panel.add(buttonPanel);
		panel.add(Box.createRigidArea(new Dimension(1, BOTTOM_MARGIN)));
		
		add(panel);
	}
	
	//displays the game over frame
	public void display(int x, int y) {
		setLocation(x, y);
		setVisible(true);
	}
	//GRADE-END
}
