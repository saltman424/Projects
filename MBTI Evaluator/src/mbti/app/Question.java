package mbti.app;

import java.util.ArrayList;
import java.util.Arrays;

public class Question {
	public final static String DEFAULT_TEXT = "New question", LAST_TEXT = "<Add question>";
	
	private String text;
	public String getText() { return text; }
	public void setText(String text) { this.text = text; }
	
	private int choices;
	public int getChoices() { return choices; }
	public void setChoices(int choices) { this.choices = choices; }
	
	private ArrayList<Answer> answers;
	public ArrayList<Answer> getAnswers() { return answers; }
	public Answer getAnswer(int index) { return answers.get(index); }
	public void setAnswers(ArrayList<Answer> answers) { this.answers = answers; }
	public void addAnswer(Answer answer) { answers.add(answer); }
	public void removeAnswer(int index) { answers.remove(index); }
	
	public Question(String text, int choices, ArrayList<Answer> answers) {
		this.text = text;
		this.choices = choices;
		this.answers = answers;
	}
	
	public Question(boolean lastItem) {
		this(lastItem ? LAST_TEXT : DEFAULT_TEXT,0, new ArrayList<Answer>() {{add(new Answer());}} );
	}
	
	public Question() {
		this(false);
	}

	public String toString() { return text; }
	
	private int nPr(int n, int r) {
		int result = 1;
		for (;r > 1;--r) result *= n--;
		return result * n;
	}
	private void swap(Answer[] A, int a, int b) {
		if (a != b) {
			Answer temp = A[a];
			A[a] = A[b];
			A[b] = temp;
		}
	}
	
	public Answer[][] possibleAnswers() {
		int nPr = nPr(answers.size(), choices);
		Answer[][] result = new Answer[nPr][choices];
		return helper(result, (ArrayList<Answer>) answers.clone(), 0, nPr, 0);
	}
	
	public Answer[][] helper(Answer[][] A, ArrayList<Answer> remainder, int rowBegin, int rowCount, int col) {
		int repeats = rowCount / remainder.size();
		for (int i = 0; i < remainder.size(); ++i) {
			ArrayList<Answer> newRemainder = (ArrayList<Answer>) remainder.clone();
			Answer answer = newRemainder.remove(i);
			int newRowBegin = rowBegin + i * repeats;
			for (int j = 0; j < repeats; ++j) A[newRowBegin + j][col] = answer;
			if (col < A[0].length - 1) A = helper(A, newRemainder, newRowBegin, repeats, col + 1);
		}
		return A;
	}
}
