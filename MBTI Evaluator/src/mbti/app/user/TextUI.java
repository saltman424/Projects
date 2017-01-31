package mbti.app.user;

import java.util.NoSuchElementException;
import java.util.Scanner;

import mbti.app.*;

public class TextUI implements UI {

	@Override
	public Answer[] askQuestion(Question question) {
		Scanner input = new Scanner(System.in);
		System.out.println(question);
		System.out.println(" (Pick " + question.getChoices() + " answers in order. Use the corresponding numbers to select answers, and use spaces to separate selections.)");
		for (int i = 0; i < question.getAnswers().size(); ++i) {
			System.out.println((i + 1) + ": " + question.getAnswer(i));
		}
		
		boolean retry = true;
		Answer[] result = new Answer[question.getChoices()];
		boolean[] open = new boolean[question.getAnswers().size()];
		while (retry) {
			for (int i = 0; i < open.length; ++i) open[i] = true;
			Scanner answers = new Scanner(input.nextLine());
			try {
				for (int i = 0; i < question.getChoices(); ++i) {
					int chosen = Integer.parseInt(answers.next()) - 1;
					if (open[chosen]) {
						result[i] = question.getAnswer(chosen);
						open[chosen] = false;
					} else throw new Exception();
				}
				retry = false;
			} catch(NoSuchElementException e) {
				System.err.println("Not enough answers: Try again");
			} catch(Exception e) {
				System.err.println("Invalid answer: Try again");
			}
		}
		return result;
	}

	@Override
	public void showResults(MBTIConfidenceList results) {
		System.out.println("Here are your results:");
		System.out.println(results);
	}

}
