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
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.Timer;

import c212.breakout.Paddle.Direction;

public class BreakoutPanel extends JPanel implements KeyListener, ActionListener {
	//GRADE-START
	public static final int FPS = 60, DFPS = 30, SCORE_TEXT_SIZE = 30, SCORE_TEXT_MARGIN = 25, 
			DEFAULT_BRICK_ROW_SIZE = 5, DEFAULT_BRICK_COLUMN_SIZE = 3, 
			DELTA_BRICK_ROW_SIZE = 1, DELTA_BRICK_COLUMN_SIZE = 1;
	public static final Color BACKGROUND = new Color(0x101010), SCORE_TEXT_COLOR = new Color(0xFFFFFF);
	public static final Font SCORE_TEXT_FONT = new Font("Arial", Font.BOLD, SCORE_TEXT_SIZE);

	private ArrayList<Brick> bricks;
	private Ball ball;
	private Paddle paddle;
	private Timer timer;
	private GameOverFrame gameOverFrame = new GameOverFrame();
	private HelpFrame helpFrame = new HelpFrame();
	private boolean paused;
	private int score, level;
	
	//constructor
	public BreakoutPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(BACKGROUND);
		setFocusable(true);
		addKeyListener(this);
		
		showHelp();
		restart();
	}
	
	//moves ball and paddle, and checks for collisions (and repaints)
	@Override
	public void actionPerformed(ActionEvent e) {
		if (!paused) {
			paddle.move();
			checkCollision();
			ball.move();
			
			repaint();
		}
	}
	
	//calls the below check collision methods
	private void checkCollision() {
		checkBorderCollision();
		if (!checkPaddleCollision()) checkBrickCollision();
	}
	
	//self explanatory (checks for border collision, and reflects ball accordingly)
	private void checkBorderCollision() {
		if (ball.y + ball.height >= getHeight()) end();
		else if (ball.y <= 0 && ball.getDY() < 0) ball.setDY(-ball.getDY());
		else if ((ball.x <= 0 && ball.getDX() < 0) || 
				(ball.x + ball.width >= getWidth() && ball.getDX() > 0))
			ball.setDX(-ball.getDX());
	}
	
	//self explanatory (checks for paddle collision, and reflects ball accordingly)
	private boolean checkPaddleCollision() {
		if (!ball.intersects(paddle) || ball.getDY() < Ball.MINIMUM_DY ||
				ball.getCenterY() >= paddle.y + paddle.height - Paddle.ARC_HEIGHT / 2) return false;
		
		//used when the ball hits the flat part of the left or right side of the paddle
		if (ball.getCenterY() >= paddle.y + Paddle.ARC_HEIGHT / 2) {
			if (ball.getCenterX() < paddle.getCenterX()) {
				if (ball.getDX() > 0) ball.setDX(-ball.getDX());
				else ball.setFX(paddle.x - ball.width);
			} else {
				if (ball.getDX() < 0) ball.setDX(-ball.getDX());
				else ball.setFX(paddle.x + paddle.width);
			}
		} 
		//used when the ball hits either of the rounded corners on the top of the paddle.
		// essentially this method uses some trigonometry to determine where the ball is hitting the paddle, 
		// then it uses that information, along with some more trigonometry, to find the variables necessary
		// to find the normal vector (using some calculus) at the point of contact between the ball and the 
		// paddle, then it feeds that information to the ball's normalize method which uses a  formula for 
		// reflecting a vector to produce the ball's new vectors.
		// Slope of an ellipse: http://www.oocities.org/web_sketches/ellipse_notes/ellipse_slope/ellipse_slope_formula.html
		// Reflecting a vector: https://en.wikipedia.org/wiki/Reflection_%28mathematics%29#Reflection_across_a_line_in_the_plane
		else if (ball.getCenterX() < paddle.x + Paddle.ARC_WIDTH / 2
				|| ball.getCenterX() > paddle.x + paddle.width - Paddle.ARC_WIDTH / 2) {
			
			double contactDX, ballCenterX;
			if(ball.getCenterX() < paddle.getCenterX()) {
				ballCenterX = Math.max(ball.getCenterX(), paddle.x);
				contactDX = ballCenterX - (paddle.x + Paddle.ARC_HEIGHT / 2);
			}
			else {
				ballCenterX = Math.min(ball.getCenterX(), paddle.x + paddle.width);
				contactDX = ballCenterX - (paddle.x + paddle.width - Paddle.ARC_HEIGHT / 2);
			}
			float contactX = (float) (ballCenterX - 
					Math.cos(Math.atan2(paddle.y + Paddle.ARC_HEIGHT / 2 - ball.getCenterY(), 
							contactDX)) * ball.width / 2);
			float semiMajorAxis = Paddle.ARC_WIDTH / 2, semiMinorAxis = Paddle.ARC_HEIGHT / 2,
					arcX = (float) (contactX - paddle.getCenterX());
			if (arcX < 0) arcX = Math.max(arcX + (paddle.width - Paddle.ARC_WIDTH) / 2, -semiMajorAxis);
			else arcX = Math.min(arcX - (paddle.width - Paddle.ARC_WIDTH) / 2, semiMajorAxis);
			double pitch = Math.acos(arcX / semiMajorAxis);
			float arcY = (float) (semiMinorAxis * Math.sin(pitch));
			
			float ndx = semiMajorAxis * semiMajorAxis * arcY, ndy = semiMinorAxis * semiMinorAxis * arcX;
			ball.normalize(ndx, ndy);
			
			if (ball.getDY() > 0) {
				ball.setDY(-ball.getDY());
			}
			
		} else ball.setDY(-ball.getDY()); //this is when the ball hits the flat part of the top of the paddle
		
		return true;
	}
	
	//self explanatory (checks for brick collision, adds to score, and reflects ball accordingly)
	private void checkBrickCollision() {
		for(Brick brick : bricks) {
			if (ball.intersects(brick)) {
				++score;
				
				if (bricks.size() <= 1) {
					nextLevel();
					break;
				}
				bricks.remove(brick);		
				
				if (ball.getCenterX() >= brick.x && ball.getCenterX() <= brick.x + brick.width)
					ball.setDY(-ball.getDY());
				else if (ball.getCenterY() >= brick.y && ball.getCenterY() <= brick.y + brick.height)
					ball.setDX(-ball.getDX());
				else {
					if ((ball.getDX() > 0 && ball.getCenterX() < brick.getCenterX()) ||
							(ball.getDX() < 0 && ball.getCenterX() > brick.getCenterX()))
							ball.setDX(-ball.getDX());
					if ((ball.getDY() > 0 && ball.getCenterY() < brick.getCenterY()) ||
							(ball.getDY() < 0 && ball.getCenterY() > brick.getCenterY()))
							ball.setDY(-ball.getDY());
				}
				
				break;
			}
		}
	}

	//used for moving the paddle,
	// as well as restarting the game, advancing the level, 
	// showing the help menu, and setting the game speed
	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == 0x30) setSpeed(10);
		else if (keyCode >= 0x31 && e.getKeyCode() <= 0x39) setSpeed(keyCode - 0x31);
		else switch(keyCode) {
			case KeyEvent.VK_R:
				timer.stop();
				restart();
				break;
			case KeyEvent.VK_N: //FOR DEBUGGING
				nextLevel();
				break;
			case KeyEvent.VK_H:
				paused = true;
				showHelp();
			case KeyEvent.VK_LEFT:
				paddle.setDirection(Direction.LEFT);
				break;
			case KeyEvent.VK_RIGHT:
				paddle.setDirection(Direction.RIGHT);
				break;
		}
	}

	//used for pausing game and stopping the paddle 
	// when the user releases the left or right arrows
	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()) {
			case KeyEvent.VK_SPACE:
				pause();
				break;
			case KeyEvent.VK_LEFT:
				paddle.setDirection(Direction.NONE);
				break;
			case KeyEvent.VK_RIGHT:
				paddle.setDirection(Direction.NONE);
				break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	//paints the whole display
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(SCORE_TEXT_COLOR);
		g.setFont(SCORE_TEXT_FONT);
		g.drawString(String.valueOf(score), 
				BreakoutFrame.WIDTH - SCORE_TEXT_MARGIN -
				g.getFontMetrics(SCORE_TEXT_FONT).stringWidth(String.valueOf(score)), 
				BreakoutFrame.HEIGHT - SCORE_TEXT_MARGIN -
				g.getFontMetrics(SCORE_TEXT_FONT).getHeight());
		
		for (Brick brick : bricks) brick.draw(g);
		paddle.draw(g);
		ball.draw(g);
	}
	//GRADE-END
	
	public void onFrameMove() { 
		gameOverFrame.setLocation(BreakoutFrame.x() + (BreakoutFrame.WIDTH - GameOverFrame.WIDTH) / 2,
				BreakoutFrame.y() + (BreakoutFrame.HEIGHT - GameOverFrame.HEIGHT) / 2);
		helpFrame.setLocation(BreakoutFrame.x() + (BreakoutFrame.WIDTH - helpFrame.WIDTH) / 2,
				BreakoutFrame.y() + (BreakoutFrame.HEIGHT - helpFrame.HEIGHT) / 2);
	}
	
	public void setSubFramesOnTop(boolean alwaysOnTop) {
		gameOverFrame.setAlwaysOnTop(alwaysOnTop);
		helpFrame.setAlwaysOnTop(alwaysOnTop);
	}
	
	public void restart() {
		score = 0;
		level = 1;
		timer = new Timer(1000/FPS, this);
		start();
	}
	
	private void start() {
		paused = true;
		bricks = Brick.createBrickLayout(0, 0, BreakoutFrame.WIDTH, 
				BreakoutFrame.HEIGHT / 4, DEFAULT_BRICK_ROW_SIZE + (level - 1) * DELTA_BRICK_ROW_SIZE,
				DEFAULT_BRICK_COLUMN_SIZE + (level - 1) * DELTA_BRICK_COLUMN_SIZE);
		paddle = new Paddle(BreakoutFrame.WIDTH / 2, BreakoutFrame.HEIGHT * 6 / 7);
		ball = new Ball(BreakoutFrame.WIDTH / 2, BreakoutFrame.HEIGHT / 2);
		timer.start();
		repaint();
	}
	
	private void setSpeed(int speed) {
		timer.stop();
		timer = new Timer(1000/(FPS + DFPS * speed), this);
		timer.start();
	}
	
	private void nextLevel() {
		timer.stop();
		++level;
		start();
	}
	
	private void pause() {
		paused = !paused;
	}
	
	private void end() {
		timer.stop();
		setFocusable(false);
		gameOverFrame.display(BreakoutFrame.x() + (BreakoutFrame.WIDTH - GameOverFrame.WIDTH) / 2,
				BreakoutFrame.y() + (BreakoutFrame.HEIGHT - GameOverFrame.HEIGHT) / 2);
	}
	
	public void closeGOF(boolean dispose) { 
		gameOverFrame.setVisible(false);
		if (dispose) gameOverFrame.dispose();
		else {
			setFocusable(true);
			requestFocus();
		}
	}
	
	public void hideHelp() { helpFrame.setVisible(false); }
	public void showHelp() {
		int frameX, frameY;
		try {
			frameX = BreakoutFrame.x();
			frameY = BreakoutFrame.y();
		} catch (Exception e) {
			frameX = 0;
			frameY = 0;
		}
		helpFrame.display(frameX + (BreakoutFrame.WIDTH - HelpFrame.WIDTH) / 2,
				frameY + (BreakoutFrame.HEIGHT - HelpFrame.HEIGHT) / 2);
	}
}
