///////////////////////////////////////////////////////////////////////////////////
//
//  C212 Spring 16
//  Final Project
//
//  Author  Sander Altman saaltman
//  Last Edited: 4/22/16
//
//////////////////////////////////////////////////////////////////////////////////

/* Dear Grader:
 * 
 * The following project contains tons of code that is not relevant to the given assignment
 * So to make grading easier for you, I have added the comments '//GRADE-START' and
 * '//GRADE-END' to signify areas of the code that are relevant to the assignment. All
 * classes except for HelpFrame contain these area of codes (that class is entirely irrelevant
 * to the assignment); Brick, Ball, and Paddle are entirely relevant to the assignment. You are 
 * welcome to read any of the code outside of those areas, but unless there is something relevant 
 * to the assignment that cannot be found in the area of code that has been marked, the rest of 
 * the code probably won't be very helpful in grading.
 * 
 * Thank you for your time, I hope you enjoy this project,
 * -Sander Altman
 */
package c212.breakout;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

public class BreakoutFrame extends JFrame implements ComponentListener, WindowListener {
	//GRADE-START
	public static final int WIDTH = 1000, HEIGHT = 800;
	
	private BreakoutPanel panel = new BreakoutPanel();
	
	private static BreakoutFrame frame = new BreakoutFrame();
	
	//constructor
	public BreakoutFrame() {
		super("Breakout");
		setSize(WIDTH, HEIGHT);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addComponentListener(this);
		addWindowListener(this);
		add(panel);
	}
	
	//shows frame
	public static void main(String[] args) {
		frame.setVisible(true);
	}
	//GRADE-END
	
	//allows other classes (BreakoutPanel) to access the frame's x value
	public static int x() { return frame.getX(); }
	
	//allows other classes (BreakoutPanel) to access the frame's y value
	public static int y() { return frame.getY(); }
	
	//restarts the game
	private void start() { panel.restart(); }
	
	//allows other classes (GameOverFrame) to restart the game
	public static void restart() { frame.start(); }
	
	//allows other classes (GameOverFrame) to close the application
	public static void close() {
		frame.setVisible(false);
		frame.dispose();
	}
	
	//closes the GameOverFrame
	private void closeGOF(boolean dispose) { panel.closeGOF(dispose); }
	
	//allows other classes (GameOverFrame) to close the GameOverFrame
	public static void closeGameOverFrame(boolean dispose) { frame.closeGOF(dispose); }
	
	//allows other classes (HelpFrame) to hide the HelpFrame
	public static void hideHelp() { frame.panel.hideHelp(); }

	//moves HelpFrame & GameOverFrame
	@Override
	public void componentMoved(ComponentEvent e) {
		panel.onFrameMove();
	}
	
	//sets HelpFrame & GameOverFrame on top
	@Override
	public void windowActivated(WindowEvent e) {
		panel.setSubFramesOnTop(true);
	}

	//sets HelpFrame & GameOverFrame not on top
	@Override
	public void windowDeactivated(WindowEvent e) {
		panel.setSubFramesOnTop(false);
	}
	
	
	
	@Override
	public void componentHidden(ComponentEvent e) {}

	@Override
	public void componentResized(ComponentEvent e) {}

	@Override
	public void componentShown(ComponentEvent e) {}
	
	@Override
	public void windowClosed(WindowEvent e) {}

	@Override
	public void windowClosing(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowOpened(WindowEvent e) {}
}
