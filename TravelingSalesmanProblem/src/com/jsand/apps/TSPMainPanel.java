package com.jsand.apps;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;


public class TSPMainPanel extends JPanel {
	public static final short BORDER_WIDTH = 3, POINT_DIAMETER = 10, PADDING = 50, TEXT_PADDING = 30;
	public static final Color BACKGROUND = new Color(0xFFFFFF), BORDER_COLOR = new Color(0x404040),
			POINT_COLOR = new Color(0x000000), START_POINT_COLOR = new Color(0x0000FF),
			TEXT_COLOR = new Color(0x000000), GOOD_COLOR = new Color(0x00CC00), 
			BAD_COLOR = new Color(0xCC0000);
	public static final Font FONT = new Font("Arial", Font.BOLD, 18);
	
	public static TSPMainPanel panel = new TSPMainPanel();
	
	private short numPoints = 5;
	private TSPPointList points = new TSPPointList(), bruteForcePath = new TSPPointList();
	private JButton randomizer = new JButton("Randomize"), sorter = new JButton("Sort");
	private Random random = new Random();
	
	private TSPMainPanel() {
		setBackground(BACKGROUND);
		setBorder(new MatteBorder(BORDER_WIDTH, BORDER_WIDTH, 
				BORDER_WIDTH, BORDER_WIDTH, BORDER_COLOR));
		
		randomizer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				randomizePoints();
			}
		});
		sorter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				points = TSPAlgorithm.sort(points);
				repaint();
			}
		});
		
		add(randomizer);
		add(sorter);
	}
	
	public static void init() {
		panel.randomizePoints();
	}
	
	private void randomizePoints() {
		points.clear();
		for (short i = 0; i < numPoints; ++i) {
			TSPPoint point = new TSPPoint(PADDING + random.nextInt(getWidth() - PADDING * 2), 
					PADDING + random.nextInt(getHeight() - PADDING * 2));
			if (i == 0) point.addConnection((short) -1);
			points.add(point);
		}
		repaint();
	}
	
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		TSPPoint currentPoint, lastPoint;
		if (points.size() > 0) {
			lastPoint = points.get(0);
			for (short i = 1; i <= points.size(); ++i) {
				if (i == points.size()) currentPoint = points.get(0);
				else currentPoint = points.get(i);
				
				g.setColor(BAD_COLOR);
				g.drawLine(lastPoint.x, lastPoint.y, currentPoint.x, currentPoint.y);
				if (i < points.size()) g.setColor(POINT_COLOR);
				else g.setColor(START_POINT_COLOR);
				g.fillOval(currentPoint.x - POINT_DIAMETER / 2, 
						currentPoint.y - POINT_DIAMETER / 2, 
						POINT_DIAMETER, POINT_DIAMETER);
				
				lastPoint = currentPoint;
			}
			
			bruteForcePath = TSPAlgorithm.bruteForce(points);
			
			for (short i = 1; i <= bruteForcePath.size(); ++i) {
				if (i == points.size()) currentPoint = bruteForcePath.get(0);
				else currentPoint = bruteForcePath.get(i);
				
				g.setColor(GOOD_COLOR);
				g.drawLine(lastPoint.x, lastPoint.y, currentPoint.x, currentPoint.y);
				
				lastPoint = currentPoint;
			}
			
			g.setColor(TEXT_COLOR);
			g.setFont(FONT);
			
			double distance = TSPAlgorithm.totalDistance(points), 
					bruteForceDistance = TSPAlgorithm.totalDistance(bruteForcePath);
			
			g.drawString(String.valueOf(bruteForceDistance), 
					getWidth() - g.getFontMetrics().stringWidth(String.valueOf(bruteForceDistance)) - TEXT_PADDING, 
					getHeight() - FONT.getSize());
			
			if (distance == bruteForceDistance) g.setColor(GOOD_COLOR);
			else g.setColor(BAD_COLOR);
			g.drawString(String.valueOf(distance), 
					TEXT_PADDING, getHeight() - FONT.getSize());
		}
	}
}
