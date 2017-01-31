package mbti.app.dev;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import mbti.app.*;
import mbti.app.dev.*;
import mbti.app.user.*;

class IRange {
	private int min, max;
	
	public IRange(int min, int max) {
		assert min <= max : "Invalid values: min > max";
		this.min = min;
		this.max = max;
	}
	
	public int min() { return min; }
	public int max() { return max; }
	public int range() { return max - min; }
}
class DRange {
private double min, max;
	
	public DRange(double min, double max) {
		assert min <= max : "Invalid values: min > max";
		this.min = min;
		this.max = max;
	}
	
	public double min() { return min; }
	public double max() { return max; }
	public double range() { return max - min; }
}
public class DatabaseTester {
	public final static File TEST_FILE = new File("./data/test.xml");
	public final static int QUESTIONS = 100;
	public final static IRange ANSWERS = new IRange(6, 12), CHOICES = new IRange(2, 5);
	public final static DRange CONFIDENCES = new DRange(0.04, 0.3);
	
	private QuestionDatabase database;
	
	public DatabaseTester(QuestionDatabase database) {
		this.database = database;
	}
	
	public DatabaseTester(File file) {
		try {
			database = new DeveloperDatabase(file);
		} catch (Exception e) {
			System.err.println("Error importing database");
			database = new DeveloperDatabase();
		}
	}
	
	public DatabaseTester() {
		this(TEST_FILE);
	}
	
	public void randomizeDatabase(int numQuestions, IRange answerR, IRange choicesR, DRange confidenceR) {
		Random r = new Random();
		
		ArrayList<Question> questions = new ArrayList<>();
		
		for (int i = 0; i < numQuestions; ++i) {
			String qText = "Question " + String.valueOf(i);
			int aSize = r.nextInt(answerR.range() + 1) + answerR.min();
			int choices = Math.min(aSize, r.nextInt(choicesR.range() + 1) + choicesR.min());
			ArrayList<Answer> A = new ArrayList<Answer>();
			for (int j = 0; j < aSize; ++j) {
				String aText = "";
				MBTIConfidence[] C = new MBTIConfidence[MBTI.COUNT];
				
				int[] indices = new int[MBTI.COUNT];
				boolean[] taken = new boolean[MBTI.COUNT];
				for (int k = 0; k < MBTI.COUNT; ++k) taken[k] = false;
				for (int k = 0; k < MBTI.COUNT; ++k) {
					int index = r.nextInt(MBTI.COUNT);
					while (taken[index]) index = r.nextInt(MBTI.COUNT);
					indices[k] = index;
					taken[index] = true;
				}
				
				double remaining = 1;
				boolean first = true;
				for (int k = 0; k < MBTI.COUNT; ++k) {
					String mbti = MBTI.values()[indices[k]].name();
					double confidence = r.nextDouble();
					if (remaining > 0) {
						while (confidence > confidenceR.max()) confidence = r.nextDouble();
						if (confidence < confidenceR.min()) confidence = 0;
						if (confidence > remaining) confidence = remaining;
						remaining -= confidence;
						
						if (confidence > 0) {
							String aTextPre = "";
							if (first) first = false;
							else aTextPre = " | ";
							aText += aTextPre + mbti + " " + String.valueOf(confidence);
						}
					} else confidence = 0;
					
					C[indices[k]] = new MBTIConfidence(mbti, confidence);
				}
				A.add(new Answer(aText, C));
			}
			questions.add(new Question(qText, choices, A));
		}
		database.setQuestions(questions);
		database.save();
	}
	
	public StopWatch time() throws Exception {
		Assertion.require(database instanceof UserDatabase, "Timing can only be done on databases that are instances of UserDatabase");
		final String NEXT_QUESTION_TAG = "Determining next question", APPLYING_ANSWERS_TAG = "Applying answer";
		MBTIConfidenceList results = new MBTIConfidenceList();
		Random r = new Random();
		StopWatch stopwatch = new StopWatch();
		stopwatch.start();
		for (Question q : database) {
			if (stopwatch.hasSubRunning()) stopwatch.stopSub(NEXT_QUESTION_TAG);
			System.out.println(q);
			Answer[] A = new Answer[q.getChoices()];
			for (int i = 0; i < A.length; ++i) {
				A[i] = q.getAnswer(r.nextInt(q.getAnswers().size()));
				System.out.println(A[i]);
			}
			stopwatch.startSub(APPLYING_ANSWERS_TAG);
			results.apply(A);
			stopwatch.stopSub(APPLYING_ANSWERS_TAG);
			stopwatch.startSub(NEXT_QUESTION_TAG);
			
		}
		return stopwatch.stop();
	}

	public static void main(String[] args) {				
		try {
			DatabaseTester tester = new DatabaseTester(new UserDatabase(new Quiz()));
			tester.randomizeDatabase(QUESTIONS, ANSWERS, CHOICES, CONFIDENCES);
			System.out.println(tester.time());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
