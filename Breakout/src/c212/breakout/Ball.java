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
import java.util.Random;

public class Ball extends Rectangle {
	//GRADE-START
	public static final int SIZE = 30, BORDER_SIZE = 2, DEFAULT_DX = 2, MAX_VELOCITY = 4;
	public static final float MINIMUM_DY = -0.2f;
	public static final Color BACKGROUND = new Color(0x00FFFF), BORDER = new Color(0x0088AA);
	
	private float fx, fy, dx, dy; //fx and fy allow for float velocities to be used
	
	//get & set methods for dx
	public float getDX() { return dx; }
	public void setDX(float dx) { this.dx = dx; }
	
	//get & set methods for dy
	public float getDY() { return dy; }
	public void setDY(float dy) { this.dy = dy; }
	
	//get & set methods for fx
	public float getFX() { return fx; }
	public void setFX(float fx) { this.fx = fx; }
	
	//get & set methods for fy
	public float getFY() { return fy; }
	public void setFY(float fy) { this.fy = fy; }

	//constructor
	public Ball(int x, int y) {
		super(x - SIZE / 2, y - SIZE / 2, SIZE, SIZE);
		fx = x - SIZE / 2;
		fy = y - SIZE / 2;
		
		Random random = new Random();
		dx = DEFAULT_DX * 2 * random.nextFloat() - DEFAULT_DX;
		dy = (float) Math.sqrt(MAX_VELOCITY * MAX_VELOCITY - dx * dx);
	}
	
	//moves ball by dx and dy
	public void move() {
		fx += dx;
		fy += dy;
		x = (int) fx;
		y = (int) fy;
	}
	
	//gives ball new dx and dy based on a given counter force
	public void normalize(float ndx, float ndy) {
		float scale = 2 * (ndx * dx + ndy * dy) / (ndx * ndx + ndy * ndy);
		dx = scale * ndx - dx;
		dy = scale * ndy - dy;
	}
	
	//draws ball on graphics
	public void draw(Graphics g) {
		g.setColor(BORDER);
		g.fillOval(x, y, width, height);
		g.setColor(BACKGROUND);
		g.fillOval(x + BORDER_SIZE, y + BORDER_SIZE, 
				width - BORDER_SIZE * 2, height - BORDER_SIZE * 2);
	}
	//GRADE-END
}
