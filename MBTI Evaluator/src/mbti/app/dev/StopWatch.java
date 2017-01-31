package mbti.app.dev;

import java.util.ArrayList;

import mbti.app.Assertion;

public class StopWatch {
	public final static String ROOT_TAG = "Total";
	private boolean running = false;
	private long start = -1;
	private long end = -1;
	private String tag;
	private ArrayList<StopWatch> subintervals = new ArrayList<StopWatch>();
	private StopWatch parent = this;
	private StopWatch current = this;
	
	private StopWatch(String tag, StopWatch parent) {
		this.tag = tag;
		if (parent != null) this.parent = parent;
	}
	public StopWatch() { this(ROOT_TAG, null); }
	
	public StopWatch start() { 
		if (Assertion.check(!running, "Stopwatch is already running")) {
			start = System.currentTimeMillis();
			running = true;
		} 
		return this;
	}
	public StopWatch stop() { 
		if (Assertion.check(running, "Stopwatch is not running")) {
			end = System.currentTimeMillis();
			for (StopWatch sub : subintervals) if (sub.isRunning()) sub.stop();
			running = false;
		}
		return this;
	}
	public boolean isRunning() { return running; }
	public boolean hasSubRunning() { return current.running && !current.equals(current.parent); }
	public void startSub(String tag) throws Exception {
		Assertion.check(running, "Stopwatch is not running");
		StopWatch sub = new StopWatch(tag, current);
		current.subintervals.add(sub.start());
		current = sub;
	}
	public void stopSub(String tag) {
		if (Assertion.check(current.running && !current.equals(current.parent), "No subinterval is running")) {
			while (!current.tag.equals(tag)) {
				if (Assertion.check(!current.equals(current.parent), "Cannot find subinterval with that tag")) current = current.parent;
				else return;
			}
			while (!current.tag.equals(tag)) {
				current.stop();
				if (current.equals(current.parent)) break;
				else current = current.parent;
			}
			current.stop();
		}
	}
	
	public long getTime() throws Exception {
		Assertion.require(start > 0, "Stopwatch has not been started");
		if (running) return System.currentTimeMillis() - start;
		else return end - start;
	}
	
	public ArrayList<Long> getTimes(String tag) throws Exception {
		ArrayList<Long> times = new ArrayList<Long>();
		if (subintervals.size() > 0) {
			for (StopWatch sub : subintervals) {
				if (sub.tag.equals(tag)) times.add(sub.getTime());
				times.addAll(sub.getTimes(tag));
			}
		}
		return times;
	}
	
	@Override
	public boolean equals(Object other) {
		return other instanceof StopWatch && tag.equals(((StopWatch) other).tag) && 
				start == ((StopWatch) other).start && end == ((StopWatch) other).end;
	}
	
	@Override
	public String toString() {
		String str = tag + ": ";
		try {
			str += getTime();
		} catch(Exception e) {
			str += "not started";
		}
		str += "\n";
		for (StopWatch s : subintervals) str += s.toString();
		return str;
	}
}
