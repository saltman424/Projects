package com.jsand.apps;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;

public class TSPAlgorithm {
	public static final short MAX_BRUTE_FORCE_POINTS = 8;
	public static final int[] FACTORIALS = { 0, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880 };

	private TSPAlgorithm() {}
	
	public static TSPPointList sort(TSPPointList points) {
		if (points.size() <= 3) return points;
		
		ArrayList<TSPTriangle> triangles = new ArrayList<>();
		
		for (short i = 0; i < points.size(); ++i) {
			for (short j = 0; j < points.size() - 1; ++j) {
				if (j == i) continue;
				for (short k = (short) (j + 1); k < points.size(); ++k) {
					if (k == i) continue;
					short closerID, furtherID;
					if (distanceBetween(points.get(i), points.get(j)) <= distanceBetween(points.get(i), points.get(k))) {
						closerID = points.get(j).ID;
						furtherID = points.get(k).ID;
					} else {
						closerID = points.get(k).ID;
						furtherID = points.get(j).ID;
					}
					triangles.add(new TSPTriangle(points.get(i).ID, closerID, furtherID,
							angleBetween(points.get(i), points.get(j), points.get(k))));
				}
			}
		}
		
		Collections.sort(triangles);
		
		for (TSPTriangle triangle : triangles) {
			TSPPoint center = points.findPoint(triangle.getCenterID());
			TSPPoint first = points.findPoint(triangle.getFirstID());
			TSPPoint second = points.findPoint(triangle.getSecondID());
			
			if (center.hasOpenConnection() && first.hasOpenConnection()) {
				center.addConnection(first.ID);
				first.addConnection(center.ID);
			}
			if (center.hasOpenConnection() && second.hasOpenConnection()) {
				center.addConnection(second.ID);
				second.addConnection(center.ID);
			}
			
			points.set(points.findIndex(center.ID), center);
			points.set(points.findIndex(first.ID), first);
			points.set(points.findIndex(second.ID), second);
		}
		
		TSPPointList path = new TSPPointList();
		TSPPoint lastPoint = points.get(0), currentPoint = points.findPoint(lastPoint.getConnection());
		path.add(lastPoint);
		path.add(currentPoint);
		
		for (short i = 2; i < points.size(); ++i) {
			short lastID = lastPoint.ID;
			lastPoint = currentPoint;
			currentPoint = points.findPoint(currentPoint.getOtherConnection(lastID));
			path.add(currentPoint);
		}
		
		return path;
	}
	
	/*private static TSPPointList buildPath(TSPPointList path, TSPPointList points) {
		if (path.size() == points.size() - 1) {
			for (TSPPoint p : points)
				if (!path.contains(p)) {
					path.add(p);
					return path;
				}
		}
		
		TSPPoint widestA = new TSPPoint(), widestB = new TSPPoint();
		double widestAngle = -1;
		
		for (short i = 0; i < points.size(); ++i) {
			for (short j = 0; j < points.size(); ++j) {
				if (i == j) continue;
				
				double currentAngle = angleBetween(path.get(path.size() - 1), points.get(i), points.get(j));
				if (currentAngle > widestAngle) {
					widestA = points.get(i);
					widestB = points.get(j);
					widestAngle = currentAngle;
				}
			}
		}
		
		if (path.contains(widestA) && path.contains(widestB)) {
			for (TSPPoint p : points)
				if (!path.contains(p))
					path.add(p); //NEEDS TO BE REFINED	
		}
		else if (path.contains(widestB) || 
				(distanceBetween(path.get(path.size() - 1), widestA) <= 
				distanceBetween(path.get(path.size() - 1), widestB)
				&! path.contains(widestA)))
			path.add(widestA);
		else path.add(widestB);
		
		return path;
	}*/
	
	private static double angleBetween(TSPPoint center, TSPPoint a, TSPPoint b) {
		return Math.acos(((a.x - center.x) * (b.x - center.x) +
				(a.y - center.y) * (b.y - center.y))
				/ (distanceBetween(a, center) * distanceBetween(b, center)));
	}
	
	private static double distanceBetween(TSPPoint a, TSPPoint b) {
		return Math.sqrt((a.x - b.x) * (a.x - b.x) +
				(a.y - b.y) * (a.y - b.y));
	}
	
	
	
	public static double totalDistance(TSPPointList points) {
		double total = 0;
		
		for (short i = 0; i < points.size(); ++i) {
			Point a = points.get(i), b;
			if (i == points.size() - 1) b = points.get(0);
			else b = points.get(i + 1);
			
			total += Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
		}
		
		return total;
	}
	
	public static TSPPointList bruteForce(TSPPointList points) {
		if (points.size() > MAX_BRUTE_FORCE_POINTS) return new TSPPointList();
		
		TSPPointList current, best = new TSPPointList(points);
		
		for (short i = 1; i <= FACTORIALS[points.size()]; ++i) {
			current = getPermutation(i, points);
			if (totalDistance(current) < totalDistance(best)) best = current;
		}
		
		return best;
	}
	
	private static TSPPointList getPermutation(short number, TSPPointList points) {
		TSPPointList permutation = new TSPPointList(points);
		
		short quotient = number;
		ArrayList<Short> switches = new ArrayList<>();
		
		for (short i = 2; i < points.size() && quotient > 0; ++i) {
			quotient /= i;
			switches.add((short) (quotient % i));
		}
		
		for (short i = (short) (switches.size() - 1); i >= 0; --i) {
			short index = (short) (points.size() - (i + 2)),
					switchIndex  = (short) (index + switches.get(i));
			
			TSPPoint switchPoint = permutation.get(switchIndex);
			permutation.set(switchIndex, permutation.get(index));
			permutation.set(index, switchPoint);
		}
		
		return permutation;
	}
	
	public static double bruteForceDistance(TSPPointList points) {
		return totalDistance(bruteForce(points));
	}
}