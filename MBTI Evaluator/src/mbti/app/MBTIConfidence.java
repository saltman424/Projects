package mbti.app;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MBTIConfidence implements Comparable<MBTIConfidence> {
	public static final int BASE_WEIGHT = 10, POSITION_WEIGHT = 2, CHOICES_WEIGHT = 1;
	private String mbti;
	private double confidence;
	private int weight;
	
	public MBTI getMBTI() { return MBTI.valueOf(mbti); }
	public double getConfidence() { return confidence; }
	public void setConfidence(double confidence) { this.confidence = confidence; }
	public int getWeight() { return weight; }
	public void setWeight(int weight) { this.weight = weight; }
	
	public MBTIConfidence(String mbti, double confidence, int weight) {
		this.mbti = mbti;
		this.confidence = confidence;
		this.weight = weight;
	}
	
	public MBTIConfidence(String mbti, double confidence) {
		this(mbti, confidence, 0);
	}
	
	public void modifyBy(MBTIConfidence other) {
		assert other.mbti.equals(mbti);
		
		confidence = (confidence * weight + other.confidence * other.weight) / (weight + other.weight);
		weight = weight + other.weight;
	}
	
	@Override
	public int compareTo(MBTIConfidence other) {
		if (confidence > other.confidence) return -1;
		else if (confidence < other.confidence) return 1;
		else if (weight > other.weight) return -1;
		else if (weight < other.weight) return 1;
		else return 0;
	}
	@Override
	public String toString() {
		return mbti + ": " + format(confidence);
	}
	private String format(double confidence) {
		DecimalFormat rounder = new DecimalFormat("#.######");
		rounder.setRoundingMode(RoundingMode.HALF_DOWN);
		return rounder.format(confidence);
	}
}
