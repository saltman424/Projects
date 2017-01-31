package com.jsand.apps;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Window;

import javax.swing.JFrame;

public class TSPFrame extends JFrame {
	public static final short DEFAULT_WIDTH = 1000, DEFAULT_HEIGHT = 800,
			MINIMUM_WIDTH = 500, MINIMUM_HEIGHT = 400;
	
	public static TSPFrame frame = new TSPFrame();
	
	private TSPFrame() {
		super("Traveling Salesman Solver");
		setSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		setMinimumSize(new Dimension(MINIMUM_WIDTH, MINIMUM_HEIGHT));
		setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		add(TSPMainPanel.panel);
		
		setVisible(true);
	}
	
	public static void main(String[] args) {
		TSPMainPanel.init();
	}
}
