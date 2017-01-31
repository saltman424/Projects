package mbti.app.user;

import java.util.Arrays;

import mbti.app.*;

public class Quiz {
	public final static int DEFAULT_LENGTH = 16;
	public final static UI DEFAULT_UI = new TextUI();

	private UserDatabase database;
	private UI ui;
	private MBTIConfidenceList results;
	
	private int asked = 0, totalAsking;
	public int asked() { return asked; }
	public int asking() { return totalAsking - asked; }
	public int totalAsking() { return totalAsking; }
	
	public MBTIConfidenceList getResults() { return results; }
	public MBTIPair getTop() { return new MBTIPair(results.get(0).getMBTI(), results.get(1).getMBTI()); } 
	
	public boolean hasStarted() { return asked > 0; }
	public boolean isFinished() { return asked >= totalAsking; }
	
	public Quiz(int length, UI ui) throws Exception {
		database = new UserDatabase(this);
		totalAsking = length;
		this.ui = ui;
		results = MBTIConfidenceList.emptyList();
	}
	
	public Quiz(int length) throws Exception {
		this(length, DEFAULT_UI);
	}
	
	public Quiz(UI ui) throws Exception {
		this(DEFAULT_LENGTH, ui);
	}
	
	public Quiz() throws Exception {
		this(DEFAULT_LENGTH, DEFAULT_UI);
	}
	
	
	public void begin() {
		for (Question q : database)  {
			results.apply(ui.askQuestion(q)); 
			asked++;
		}
		ui.showResults(results);
	}
	
	
	private static Quiz processArgs(String[] args) throws Exception {
		if (args.length <= 0) return new Quiz();
		else {
			int length = DEFAULT_LENGTH;
			UI ui = DEFAULT_UI;
			
			for (String arg : args) {
				switch (arg) {
					case "text":
						ui = new TextUI();
						break;
					case "graphical":
						ui = new GraphicalUI();
						break;
					default:
						try {
							length = Integer.parseInt(arg);
						} catch(Exception e) {
							System.err.println("Invalid argument");
						}
				}
			}			
			
			return new Quiz(length, ui);
		}
	}
	
	public static void main(String[] args) {
		try {
			Quiz quiz = processArgs(args);
			quiz.begin();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
