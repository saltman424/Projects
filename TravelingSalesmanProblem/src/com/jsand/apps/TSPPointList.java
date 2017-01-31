package com.jsand.apps;

import java.util.ArrayList;
import java.util.Collection;

public class TSPPointList extends ArrayList<TSPPoint> {
	public TSPPointList() {}
	public TSPPointList(TSPPointList list) { super(list); }
	
	public TSPPoint findPoint(short ID) {
		for (TSPPoint p : this) if (p.ID == ID) return p;
		return null;
	}
	
	public short findIndex(short ID) {
		for (short i = 0; i < size(); ++i) if (get(i).ID == ID) return i;
		return 0;
	}
}
