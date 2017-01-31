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
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public class Paddle extends Rectangle {
	//GRADE-START
	public static final int WIDTH = 120, HEIGHT = 30, BORDER_SIZE = 3, ARC_WIDTH = 100, ARC_HEIGHT = 28,
			DX = 4;
	public static final Color BACKGROUND = new Color(0x00FFAA), BORDER = new Color(0x00AA66);
	public enum Direction { LEFT, RIGHT, NONE }
	private Direction currentDirection = Direction.NONE;
	
	//set method for direction
	public void setDirection(Direction direction) { currentDirection = direction; }

	//constructor
	public Paddle(int x, int y) {
		super(x - WIDTH / 2, y - HEIGHT / 2, WIDTH, HEIGHT);
	}
	
	//moves ball based on direction
	public void move() {
		switch(currentDirection) {
			case LEFT:
				if (x >= DX) x -= DX;
				else x = 0;
				break;
			case RIGHT:
				if (x + width <= BreakoutFrame.WIDTH - DX) x += DX;
				else x = BreakoutFrame.WIDTH - width;
				break;
		}
	}
	
	//draws paddle on graphics
	public void draw(Graphics g) {
		g.setColor(BORDER);
		g.fillRoundRect(x, y, width, height, ARC_WIDTH, ARC_HEIGHT);
		g.setColor(BACKGROUND);
		g.fillRoundRect(x + BORDER_SIZE, y + BORDER_SIZE,
				width - BORDER_SIZE * 2, height - BORDER_SIZE * 2,
				ARC_WIDTH, ARC_HEIGHT);
	}
	//GRADE-END
}
