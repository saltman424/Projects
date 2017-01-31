package mbti.app;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public abstract class QuestionDatabase implements Iterable<Question> {
	public static final File DEFAULT_FILE = new File("./data/questions.xml");
	protected File file = DEFAULT_FILE;
	protected ArrayList<Question> questions = new ArrayList<Question>();
	protected XMLProcessor xmlProcessor = new XMLProcessor();
	
	public int size() { return questions.size(); }
	public ArrayList<Question> getQuestions() { return questions; }
	public Question getQuestion(int index) { return questions.get(index); }
	public ArrayList<Answer> getAnswers(int index) { return questions.get(index).getAnswers(); }
	public Answer getAnswer(int questionIndex, int answerIndex) { return questions.get(questionIndex).getAnswer(answerIndex); }
	public MBTIConfidenceList getConfidences(int questionIndex, int answerIndex) { return questions.get(questionIndex).getAnswer(answerIndex).getConfidences(); }
	public void setQuestion(int index, Question question) { questions.set(index, question); }
	public void setQuestions(ArrayList<Question> questions) { this.questions = questions; }
	public void addQuestion(Question question) { questions.add(question); }
	public void removeQuestion(int index) { questions.remove(index); }
	
	protected QuestionDatabase(File file, boolean load) throws Exception { 
		this.file = file;
		if (load) load(file);
	}
	protected QuestionDatabase(File file) throws Exception { 
		this(file, true);
	}
	protected QuestionDatabase(boolean load) throws Exception { 
		this(DEFAULT_FILE, load);
	}
	protected QuestionDatabase() {}
	
	public void load() {
		try {
			questions = xmlProcessor.load(file);
		} catch(Exception e) {
			questions = new ArrayList<Question>() {{ add(new Question()); }};
			System.err.println("Error importing database");
		}
	}
	
	public void load(File file) {
		this.file = file;
		load();
	}
	
	public void save() {
		xmlProcessor.save(questions, file);
	}
	
	@Override
	public Iterator<Question> iterator() { return questions.iterator(); }
}
