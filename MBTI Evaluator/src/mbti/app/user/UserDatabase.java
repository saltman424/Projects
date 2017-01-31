package mbti.app.user;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import mbti.app.*;

public class UserDatabase extends QuestionDatabase implements Iterator<Question> {	
	public final static double SUFFICIENT_SEPERATION = 0.1;
	
	private Quiz quiz;
	private QuestionHashMap hashMap = new QuestionHashMap();
	
	public UserDatabase(Quiz quiz) throws Exception {
		super(true);
		this.quiz = quiz;
	}

	@Override
	public boolean hasNext() {
		return !quiz.isFinished() && questions.size() >= 1;
	}
	
	private Question determineNext(MBTIPair pair) {
		//first question
		if (!quiz.hasStarted()) {
			Random r = new Random();
			return questions.remove(r.nextInt(questions.size()));
		}
				
		LinkedList<Question> list = new LinkedList<Question>();
		int listLength = quiz.asking();
		Double[] separations = new Double[listLength];
		int[] indices = new int[listLength];
		
		//POSSIBLY CHANGE SEPERATION TO MEDIAN VALUE?
		for (int i = 0; i < questions.size(); ++i) {
			//sort questions by separation value
			Question q  = questions.get(i);
			double separation = 0;
			Answer[][] possibleAnswers = q.possibleAnswers();
			for (int j = 0; j < possibleAnswers.length; ++j) {
				separation += quiz.getResults().separation(possibleAnswers[j]);
			}
			double avgSeparation = (double) separation / (double) possibleAnswers.length;
			int index = i;
			
			for (int j = 0; j < listLength; ++j) {
				if (separations[j] == null) {
					separations[j] = avgSeparation;
					indices[j] = index;
					break;
				}
				if (avgSeparation > separations[j]) {
					double tempD = separations[j];
					int tempI = indices[j];
					separations[j] = avgSeparation;
					indices[j] = index;
					avgSeparation = tempD;
					index = tempI;
				}
			}
			
			Double sep = separations[listLength - 1];
			if (sep != null && sep >= SUFFICIENT_SEPERATION) break;
		}
		
		//put questions in hashMap
		for (int j = 1; j < separations.length; ++j) {
			list.add(questions.get(indices[j]));
		}
		hashMap.add(quiz.getTop(), list);
		
		return questions.remove(indices[0]);
	}
	
	@Override
	public Question next() { 
		MBTIPair pair = quiz.getTop();
		Question next = hashMap.get(pair);
		if (next == null) next = determineNext(pair);
		else {
			questions.remove(next);
			hashMap.remove(next);
		}
		return next;
	}
	@Override
	public Iterator<Question> iterator() { return this; }
}
