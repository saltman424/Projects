package mbti.app.user;

import java.util.ArrayList;
import java.util.LinkedList;

import mbti.app.*;

public class QuestionHashMap {
	public final static int STORAGE_AMOUNT = 8;
	
	private ArrayList<LinkedList<Question>> questionLists = new ArrayList<LinkedList<Question>>(STORAGE_AMOUNT);
	private int[] hashIndices = new int[STORAGE_AMOUNT];
	private int nextIndex = 0;
	boolean overwriting = false;
	
	public Question get(MBTIPair key) {
		int hash = key.hashCode();
		for (int i = 0; i < STORAGE_AMOUNT; ++i)
			if (hashIndices[i] == hash && questionLists.get(i).size() > 0) return questionLists.get(i).removeFirst();
		return null;
	}
	
	public void add(MBTIPair key, LinkedList<Question> values) {
		hashIndices[nextIndex] = key.hashCode();
		if (overwriting) questionLists.set(nextIndex, values);
		else questionLists.add(values);
		if (++nextIndex == STORAGE_AMOUNT) {
			nextIndex = 0;
			overwriting = true;
		}
	}
	
	public void remove(Question question) {
		for (LinkedList<Question> list : questionLists) list.remove(question);
	}
}
