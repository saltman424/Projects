package mbti.app;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLProcessor {
	public final static String XML_ENCODING = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n", ROOT_TAG = "Database",
			QUESTION_TAG = "Question", TEXT_TAG = "Text", CHOICES_TAG = "Choices", ANSWER_TAG = "Answer",
			CONFIDENCE_TAG = "Confidence", MBTI_ATTR = "MBTI";
	static int indents = 0;

	// LOADING
	public ArrayList<Question> load(File file) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(file);
		doc.getDocumentElement().normalize();

		ArrayList<Question> questions = new ArrayList<Question>();
		NodeList questionList = doc.getElementsByTagName(QUESTION_TAG);
		for (int i = 0; i < questionList.getLength(); ++i) {
			questions.add(parseQuestionText((Element) questionList.item(i)));
		}
		return questions;
	}

	private Question parseQuestionText(Element e) {
		String text = e.getElementsByTagName(TEXT_TAG).item(0).getTextContent();
		int choices = Integer.parseInt(e.getElementsByTagName(CHOICES_TAG).item(0).getTextContent());
		ArrayList<Answer> answers = parseAnswersText(e.getElementsByTagName(ANSWER_TAG));
		return new Question(text, choices, answers);
	}

	private ArrayList<Answer> parseAnswersText(NodeList list) {
		ArrayList<Answer> answers = new ArrayList<Answer>();
		for (int i = 0; i < list.getLength(); ++i) {
			answers.add(parseAnswerText((Element) list.item(i)));
		}
		return answers;
	}

	private Answer parseAnswerText(Element e) {
		String text = e.getElementsByTagName(TEXT_TAG).item(0).getTextContent();
		MBTIConfidence[] confidences = parseConfidencesText(e.getElementsByTagName(CONFIDENCE_TAG));
		return new Answer(text, confidences);
	}

	private MBTIConfidence[] parseConfidencesText(NodeList list) {
		MBTIConfidence[] confidences = new MBTIConfidence[list.getLength()];
		for (int i = 0; i < list.getLength(); ++i) {
			confidences[i] = parseConfidenceText((Element) list.item(i));
		}
		return confidences;
	}

	private MBTIConfidence parseConfidenceText(Element e) {
		String mbti = e.getAttribute(MBTI_ATTR);
		double confidences = Double.parseDouble(e.getTextContent());
		return new MBTIConfidence(mbti, confidences);
	}

	// SAVING
	public void save(ArrayList<Question> questions, File file) {
		String xmlText = XML_ENCODING + parseQuestions(questions);
		try {
			appendToFile(xmlText, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void appendToFile(String str, File file) throws IOException {
		FileWriter writer = new FileWriter(file);
		writer.write(str);
		writer.close();
	}

	private String indent() {
		String str = "";
		for (int i = 0; i < indents; ++i) str += "\t";
		return str;
	}
	private String startTag(String tagName, String attrbName, String attrbValue, boolean part) {
		String str = indent() + "<" + tagName + ((attrbName != null && attrbValue != null)
				? (" " + attrbName + " = \"" + attrbValue + "\">") : ">") + (part ? "" : "\n");
		if (!part) indents++;
		return str;
	}
	private String startTag(String tagName, String attrbName, String attrbValue) {
		return startTag(tagName, attrbName, attrbValue, false);
	}
	private String startTag(String tagName) {
		return startTag(tagName, null, null, false);
	}
	private String endTag(String tagName, boolean part) {
		String pre = "";
		if (!part) {
			indents--;
			pre += indent();
		}
		return pre + "</" + tagName + ">\n";
	}
	private String endTag(String tagName) {
		return endTag(tagName, false);
	}
	private String fullTag(String tagName, String tagValue, String attrbName, String attrbValue) {
		return startTag(tagName, attrbName, attrbValue, true) + tagValue + endTag(tagName, true);
	}
	private String fullTag(String tagName, String tagValue) {
		return fullTag(tagName, tagValue, null, null);
	}

	private String parseQuestions(ArrayList<Question> questions) {
		String str = startTag(ROOT_TAG);
		for (Question q : questions) str += parseQuestion(q);
		return str + endTag(ROOT_TAG);
	}

	private String parseQuestion(Question question) {
		String str = startTag(QUESTION_TAG) + 
				fullTag(TEXT_TAG, question.getText()) +
				fullTag(CHOICES_TAG, String.valueOf(question.getChoices()));
		for (Answer a : question.getAnswers()) str += parseAnswer(a);
		return str + endTag(QUESTION_TAG);
	}

	private String parseAnswer(Answer answer) {
		String str = startTag(ANSWER_TAG) + 
				fullTag(TEXT_TAG, answer.getText());
		for (MBTIConfidence c : answer.getConfidences()) str += fullTag(CONFIDENCE_TAG, String.valueOf(c.getConfidence()), MBTI_ATTR, c.getMBTI().name());
		return str + endTag(ANSWER_TAG);
	}
}
