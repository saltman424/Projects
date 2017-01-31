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
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Brick extends Rectangle {
	//GRADE-START
	public static final int GAP_WIDTH = 5, GAP_HEIGHT = 5, ARC_WIDTH = 10, ARC_HEIGHT = 15, MAX_HEIGHT = 40;
	public static final Color[] COLORS = new Color[] {new Color(0xFF0000), new Color(0xFFAA00), 
			new Color(0xFFFF00), new Color(0x00DD00), new Color(0x0000FF), new Color(0xAA00DD)};
	
	private int colorNumber;

	//private constructor
	private Brick(int x, int y, int width, int height, int colorNumber) {
		super(x, y, width, height);
		this.colorNumber = colorNumber;
	}
	
	//static method that returns a layout of bricks
	public static ArrayList<Brick> createBrickLayout(int x, int y, int width, int height,
			int perRow, int perCol) {
		ArrayList<Brick> bricks = new ArrayList<>();
		
		int colorDenom = 1;
		for (;COLORS.length * colorDenom < perCol; ++colorDenom);
		
		int brickWidth = (width - (perRow - 1) * GAP_WIDTH) / perRow,
				brickHeight = Math.min((height - (perCol - 1) * GAP_HEIGHT) / perCol, MAX_HEIGHT);
		
		for (int i = 0; i < perCol; ++i) {
			int colorNumber = i / colorDenom;
			for (int j = 0; j < perRow; ++j)
				bricks.add(new Brick(x + j * (brickWidth + GAP_WIDTH), 
						y + i * (brickHeight + GAP_HEIGHT),
						brickWidth, brickHeight, colorNumber));
		}
		
		return bricks;
	}
	
	//draws a brick on the graphics
	public void draw(Graphics g) {
		g.setColor(COLORS[colorNumber]);
		g.fillRoundRect(x, y, width, height, ARC_WIDTH, ARC_HEIGHT);
	}
	//GRADE-END
}
