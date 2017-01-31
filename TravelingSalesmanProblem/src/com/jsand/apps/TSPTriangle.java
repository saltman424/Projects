package com.jsand.apps;

public class TSPTriangle implements Comparable {

	private short centerID, firstID, secondID;
	private double angle;
	
	public short getCenterID() { return centerID; }
	public short getFirstID() { return firstID; }
	public short getSecondID() { return secondID; }
	
	public TSPTriangle(short centerID, short firstID, short secondID, double angle) {
		this.centerID = centerID;
		this.firstID = firstID;
		this.secondID = secondID;
		this.angle = angle;
	}
	
	@Override
	public int compareTo(Object other) {
		if (angle > ((TSPTriangle) other).angle) return -1;
		else if (angle < ((TSPTriangle) other).angle) return 1;
		else return 0;
	}

}
