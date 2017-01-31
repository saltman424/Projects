package mbti.app;

public class Answer {
	public final static String DEFAULT_TEXT = "New answer", LAST_TEXT = "<Add answer>";
	private String text;
	public String getText() { return text; }
	public void setText(String text) { this.text = text; }
	
	private MBTIConfidenceList confidences;
	public MBTIConfidenceList getConfidences() { return confidences; }
	public void setConfidences(MBTIConfidenceList confidences) { this.confidences = confidences; }
	public void setConfidence(MBTI mbti, double confidence) { confidences.set(mbti, confidence); }
	public void setConfidence(MBTIConfidence confidence) { confidences.set(confidence); }
	
	public Answer(String text, MBTIConfidenceList confidences) {
		this.text = text;
		this.confidences = confidences;
	}
	
	public Answer(String text, MBTIConfidence[] confidences) {
		this(text, new MBTIConfidenceList(confidences));
	}
	
	public Answer(boolean lastItem) {
		this(lastItem ? LAST_TEXT : DEFAULT_TEXT, new MBTIConfidenceList());
	}
	
	public Answer() {
		this(false);
	}

	@Override
	public String toString() { return text; }
}
