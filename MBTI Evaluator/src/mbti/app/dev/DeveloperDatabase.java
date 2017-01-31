package mbti.app.dev;

import java.io.File;

import mbti.app.*;

public class DeveloperDatabase extends QuestionDatabase {
	private Question currentQ;
	private Answer currentA;
	
	public Question getQuestion() { return currentQ; }
	@Override
	public Question getQuestion(int index) {
		currentQ = questions.get(index);
		return currentQ;
	}

	public Answer getAnswer() { return currentA; }
	public Answer getAnswer(int index) { 
		currentA = currentQ.getAnswer(index);
		return currentA;
	}
	
	public void addAnswer(Answer answer) {
		currentQ.addAnswer(answer);
	}
	public void removeAnswer(int index) {
		currentQ.removeAnswer(index);
	}
	
	public DeveloperDatabase(File file) throws Exception {
		super(file);
	}
	public DeveloperDatabase() {
		super();
	}
	
	public void setQuestionText(String text) { currentQ.setText(text.length() > 0 ? text : " "); }
	public void setAnswerText(String text) { currentA.setText(text.length() > 0 ? text : " "); }
	public void setChoices(int choices) { currentQ.setChoices(choices); }
	public int getChoices() { return currentQ.getChoices(); }
	public void setConfidence(MBTI mbti, double value) { currentA.setConfidence(mbti, value); }
}
