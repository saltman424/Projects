package mbti.app.user;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import mbti.app.*;

public interface UI {
	public Answer[] askQuestion(Question question);
	void showResults(MBTIConfidenceList results);
}
