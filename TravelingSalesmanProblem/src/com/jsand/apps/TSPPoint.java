package com.jsand.apps;

import java.awt.Point;

public class TSPPoint extends Point {
	private static short currentID = 1;
	private static short ID() { return currentID++; }
	
	private short connection1 = 0, connection2 = 0;
	public final short ID = ID();
	
	public TSPPoint() { super(); }
	public TSPPoint(int x, int y) { super(x, y); }

	public boolean hasOpenConnection() {
		if (connection1 == 0 || connection2 == 0) return true;
		return false;
	}
	
	public void addConnection(short connection) {
		if (connection1 == connection || connection2 == connection) return;
		if (connection1 == 0) connection1 = connection;
		else if (connection2 == 0) connection2 = connection;
	}
	
	public short getConnection() {
		return connection2;
	}
	
	public short getOtherConnection(short connection) {
		if (connection1 == connection) return connection2;
		else return connection1;
	}
}
