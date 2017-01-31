package mbti.app.dev;

import mbti.app.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

public class DeveloperGUI extends JFrame {
	public final static int BORDER_MIN = 20, BORDER_PREF = 40, BORDER_MAX = 80, 
			HA_MIN = 20, HA_PREF = 50, HA_MAX = 100, 
			HB_MIN = 15, HB_PREF = 30, HB_MAX = 60, 
			VA_MIN = 10, VA_PREF = 20, VA_MAX = 40,
			VB_MIN = 10, VB_PREF = 20, VB_MAX = 40,
			VC_MIN = 5, VC_PREF = 10, VC_MAX = 20;
	public final static String TITLE = "MBTI Question Writer", LABEL_FONT_FAMILY = "Arial", TEXTBOX_FONT_FAMILY = LABEL_FONT_FAMILY, 
			LIST_FONT_FAMILY = TEXTBOX_FONT_FAMILY, MISC_FONT_FAMILY = TEXTBOX_FONT_FAMILY;
	public final static Dimension DEFAULT_SIZE = new Dimension(1000, 800), MIN_SIZE = new Dimension(500, 400),
			QUESTION_LIST_SIZE = new Dimension(100, 500), QUESTION_BOX_SIZE = new Dimension(250, 150),
			ANSWER_LIST_SIZE = new Dimension(160, 350), ANSWER_BOX_SIZE = new Dimension(250, 80), 
			CHOICES_BOX_SIZE = new Dimension(ANSWER_LIST_SIZE.width, 15), CONFIDENCE_TABLE_SIZE = new Dimension(50, 300);
	public final static Color BACKGROUND = new Color(0x111122), LABEL_BACKGROUND = BACKGROUND,
			LABEL_COLOR = new Color(0xD0D0DF), MENU_BACKGROUND = new Color(0xF5F5FF);
	public final static Font LABEL_FONT = new Font(LABEL_FONT_FAMILY, Font.BOLD, 16),
			TEXTBOX_FONT = new Font(TEXTBOX_FONT_FAMILY, Font.BOLD, 14), LIST_FONT = new Font(LIST_FONT_FAMILY, Font.BOLD, 12),
			TABLE_HEADER_FONT = new Font(MISC_FONT_FAMILY, Font.BOLD, 16), TABLE_LEFT_FONT = new Font(LIST_FONT_FAMILY, Font.BOLD, 14),
			TABLE_RIGHT_FONT = new Font(LIST_FONT_FAMILY, Font.PLAIN, 14), TABLE_TOTAL_FONT = new Font(MISC_FONT_FAMILY, Font.BOLD, 15);

	private DeveloperDatabase database = new DeveloperDatabase();
	public DeveloperDatabase getDatabase() { return database; }

	private DeveloperPanel panel;
	public DeveloperPanel getPanel() { return panel; }
	
	public final LoadMonitor loadMonitor = new LoadMonitor();

	public DeveloperGUI() throws Exception {
		super(TITLE);
		
		loadMonitor.beginLoading();

		setMinimumSize(MIN_SIZE);
		setPreferredSize(DEFAULT_SIZE);
		setSize(DEFAULT_SIZE);
		setBackground(BACKGROUND);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		panel = new DeveloperPanel(this);
		load();
		setJMenuBar(new DeveloperMenuBar(database, panel.getQuestionList(), panel.getAnswerList()));

		add(panel);
		setVisible(true);
		
		loadMonitor.endLoading();
	}
	
	public void load() {
		loadMonitor.beginLoading();
		database.load();
		panel.init();
		loadMonitor.endLoading();
	}

	public static void main(String[] args) {
		try {
			DeveloperGUI devGUI = new DeveloperGUI();
		} catch (Exception e) {
			//System.err.println("Failed to load database");
		}
	}
}

class LoadMonitor {
	private boolean loading = true;
	public boolean isLoading() { return loading; }
	public void beginLoading() { loading = true; }
	public void endLoading() { loading = false; }
}

// MENU

class DeveloperMenuBar extends JMenuBar {
	public DeveloperMenuBar(DeveloperDatabase database, QuestionList questions, AnswerList answers) {
		setBackground(DeveloperGUI.MENU_BACKGROUND);

		JMenu fileMenu = new JMenu("File");
		JMenuItem newFile = new JMenuItem("New");
		JMenuItem saveFile = new JMenuItem("Save");
		JMenuItem loadFile = new JMenuItem("Load");
		newFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("New");
			};
		});
		saveFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				database.save();
			};
		});
		loadFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Load");
			};
		});
		fileMenu.add(newFile);
		fileMenu.add(saveFile);
		fileMenu.add(loadFile);

		JMenu findMenu = new JMenu("Find");
		JMenuItem findQuestion = new JMenuItem("Question");
		JMenuItem findAnswer = new JMenuItem("Answer");
		JMenuItem findAnywhere = new JMenuItem("Anywhere");
		findQuestion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Find question");
			};
		});
		findAnswer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Find answer");
			};
		});
		findAnywhere.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Find anywhere");
			};
		});
		findMenu.add(findQuestion);
		findMenu.add(findAnswer);
		findMenu.add(findAnywhere);

		add(fileMenu);
		add(new ListMenu("Questions", questions));
		add(new ListMenu("Answers", answers));
		add(findMenu);
	}
}

class ListMenu extends JMenu {
	public ListMenu(String text, LabeledList list) {
		super(text);

		JMenuItem add = new JMenuItem("Add");
		add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				list.addItem();
			}
		});

		JMenu delete = new JMenu("Delete");
		JMenuItem deleteCurrent = new JMenuItem("Current");
		JMenuItem deleteSelection = new JMenuItem("Selection");
		deleteCurrent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				list.deleteCurrent();
			}
		});
		deleteSelection.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				list.deleteSelection();
			}
		});
		delete.add(deleteCurrent);
		delete.add(deleteSelection);

		add(add);
		add(delete);
	}
}

// PANEL

class DeveloperPanel extends JPanel {
	public final DeveloperGUI parent;
	private GroupLayout layout;

	private QuestionList questionList;
	public QuestionList getQuestionList() { return questionList; }
	public void setQuestionList(QuestionList questionList) { this.questionList = questionList; }

	private QuestionBox questionBox;
	public QuestionBox getQuestionBox() { return questionBox; }
	public void setQuestionBox(QuestionBox questionBox) { this.questionBox = questionBox; }

	private AnswerList answerList;
	public AnswerList getAnswerList() { return answerList; }
	public void setAnswerList(AnswerList answerList) { this.answerList = answerList; }
	
	private AnswerBox answerBox;
	public AnswerBox getAnswerBox() { return answerBox; }
	public void setAnswerBox(AnswerBox answerBox) { this.answerBox = answerBox; }

	private ChoicesField choicesField;
	public ChoicesField getChoicesField() { return choicesField; }
	public void setChoicesField(ChoicesField choicesField) { this.choicesField = choicesField; }
	
	private ConfidenceTable confidenceTable;
	public ConfidenceTable getConfidenceTable() { return confidenceTable; }
	public void setConfidenceTable(ConfidenceTable confidenceTable) { this.confidenceTable = confidenceTable; }

	public DeveloperPanel(DeveloperGUI parent) {
		this.parent = parent;
	}

	public void init() {
		questionBox = new QuestionBox(this);
		answerBox = new AnswerBox(this);
		choicesField = new ChoicesField(parent.loadMonitor, this);
		confidenceTable = new ConfidenceTable(parent.loadMonitor, this);
		answerList = new AnswerList(this);
		questionList = new QuestionList(parent.getDatabase().getQuestions(), this);

		layout = new GroupLayout(this);

		setBackground(DeveloperGUI.BACKGROUND);
		setLayout(layout);

		layout.setHorizontalGroup(
				layout.createSequentialGroup()
				.addGap(DeveloperGUI.BORDER_MIN, DeveloperGUI.BORDER_PREF, DeveloperGUI.BORDER_MAX)
				.addComponent(questionList)
				.addGap(DeveloperGUI.HA_MIN, DeveloperGUI.HA_PREF, DeveloperGUI.HA_MAX)
				.addGroup(
						layout.createParallelGroup()
						.addComponent(questionBox)
						.addComponent(answerBox)
						.addGroup(
								layout.createSequentialGroup()
								.addGroup(
										layout.createParallelGroup()
										.addComponent(answerList)
										.addComponent(choicesField)
										)
								.addGap(DeveloperGUI.HB_MIN, DeveloperGUI.HB_PREF, DeveloperGUI.HB_MAX)
								.addComponent(confidenceTable)
								)
						)
				.addGap(DeveloperGUI.BORDER_MIN, DeveloperGUI.BORDER_PREF, DeveloperGUI.BORDER_MAX)
				);
		
		layout.setVerticalGroup(
				layout.createSequentialGroup()
				.addGap(DeveloperGUI.BORDER_MIN, DeveloperGUI.BORDER_PREF, DeveloperGUI.BORDER_MAX)
				.addGroup(
						layout.createParallelGroup()
						.addComponent(questionList)
						.addGroup(
								layout.createSequentialGroup()
								.addComponent(questionBox)
								.addGap(DeveloperGUI.VA_MIN, DeveloperGUI.VA_PREF, DeveloperGUI.VA_MAX)
								.addComponent(answerBox)
								.addGap(DeveloperGUI.VB_MIN, DeveloperGUI.VB_PREF, DeveloperGUI.VB_MAX)
								.addGroup(
										layout.createParallelGroup()
										.addGroup(
												layout.createSequentialGroup()
												.addComponent(answerList)
												.addGap(DeveloperGUI.VC_MIN, DeveloperGUI.VC_PREF, DeveloperGUI.VC_MAX)
												.addComponent(choicesField)
												)
										.addComponent(confidenceTable)
										)
								)
						)
				.addGap(DeveloperGUI.BORDER_MIN, DeveloperGUI.BORDER_PREF, DeveloperGUI.BORDER_MAX)
				);

		setFocusTraversalPolicy(new DeveloperPanelFocusTraversalPolicy(
				new JComponent[] { questionBox, answerBox, answerList, choicesField, confidenceTable, questionList }));
	}

	public void setQuestion(int index) {
		Question question = parent.getDatabase().getQuestion(index);
		questionBox.setText(question.toString());
		choicesField.setText(String.valueOf(question.getChoices()));
		answerList.setAll(question.getAnswers());
	}

	public void setAnswer(int index) {
		Answer answer = parent.getDatabase().getAnswer(index);
		answerBox.setText(answer.toString());
		confidenceTable.setConfidences(answer.getConfidences());
	}
	
	public void setQuestionText(String text) {
		if (!parent.loadMonitor.isLoading()) {
			parent.getDatabase().setQuestionText(text.trim());
			int index = questionList.getSelectedIndex();
			if (index >= 0) questionList.set(index, parent.getDatabase().getQuestion());
		}
	}
	
	public void setAnswerText(String text) {
		if (!parent.loadMonitor.isLoading()) {
			parent.getDatabase().setAnswerText(text.trim());
			int index = answerList.getSelectedIndex();
			if (index >= 0) answerList.set(index, parent.getDatabase().getAnswer());
		}
	}
	
	public void setChoices(int choices) {
		parent.getDatabase().setChoices(choices);
	}
	public int getChoices() {
		return parent.getDatabase().getChoices();
	}
	public void setConfidence(MBTI mbti, double value) {
		parent.getDatabase().setConfidence(mbti, value);
	}
	
	
	public void removeQuestion(int index) {
		parent.getDatabase().removeQuestion(index);
	}

	public void removeAnswer(int index) {
		parent.getDatabase().removeAnswer(index);
	}
}

class DeveloperPanelFocusTraversalPolicy extends FocusTraversalPolicy {
	private JComponent[] components;

	public DeveloperPanelFocusTraversalPolicy(JComponent[] components) {
		this.components = components;
	}

	@Override
	public Component getComponentAfter(Container panel, Component component) {
		for (int i = 0; i < components.length - 1; ++i)
			if (component.equals(components[i]))
				return components[i + 1];
		return components[0];
	}

	@Override
	public Component getComponentBefore(Container panel, Component component) {
		for (int i = 1; i < components.length; ++i)
			if (component.equals(components[i]))
				return components[i - 1];
		return components[components.length - 1];
	}

	@Override
	public Component getDefaultComponent(Container panel) {
		return components[0];
	}

	@Override
	public Component getFirstComponent(Container panel) {
		return components[0];
	}

	@Override
	public Component getLastComponent(Container panel) {
		return components[components.length - 1];
	}
}

// COMPONENTS

class LabeledComponent extends JPanel {
	private boolean unset = true;
	protected LoadMonitor loadMonitor;

	public LabeledComponent(String label, Dimension size, boolean horizontal, LoadMonitor loadMonitor) {
		this.loadMonitor = loadMonitor;
		
		setLayout(new BoxLayout(this, horizontal ? BoxLayout.X_AXIS : BoxLayout.Y_AXIS));
		setPreferredSize(size);
		setSize(size);
		setBackground(DeveloperGUI.LABEL_BACKGROUND);

		JLabel jLabel = new JLabel(label);
		jLabel.setFont(DeveloperGUI.LABEL_FONT);
		jLabel.setForeground(DeveloperGUI.LABEL_COLOR);
		jLabel.setMaximumSize(jLabel.getPreferredSize());
		add(jLabel);
		if (horizontal) add(Box.createHorizontalGlue());
	}
	
	public LabeledComponent(String label, Dimension size, LoadMonitor loadMonitor) {
		this(label, size, false, loadMonitor);
	}

	protected void setComponent(JComponent component) {
		if (unset) {
			component.setBorder(new BevelBorder(BevelBorder.LOWERED));
			add(component);
		} else
			System.err.println("Component is already set");
	}
}

// LISTS

abstract class LabeledList<Data> extends LabeledComponent implements ListSelectionListener, KeyListener {
	protected final static int INITIAL_SELECTION = 0;
	private DefaultListModel<Data> listModel = new DefaultListModel<Data>();
	protected DeveloperPanel panel;
	protected JList<Data> list;
	private final Data lastItem;

	public void setAll(ArrayList<Data> data) {
		listModel.clear();
		if (data != null) {
			for (Data d : data)
				listModel.addElement(d);
			list.setSelectedIndex(INITIAL_SELECTION);
		}
		listModel.addElement(lastItem);
	}

	abstract public void addItem();
	abstract public void deleteCurrent();
	abstract public void deleteSelection();

	public int listSize() { return listModel.size(); }
	public void add(Data data) { listModel.insertElementAt(data, listModel.size() - 1); }
	public void remove(Data data) { listModel.removeElement(data); }
	public Data get(int index) { return listModel.getElementAt(index); }
	public void set(int index, Data data) { listModel.set(index, data); }

	public void update(Data[] data) {
		listModel.removeRange(0, listModel.size());
		for (Data value : data)
			listModel.addElement(value);
	}

	public int getSelectedIndex() { return list.getSelectedIndex(); }
	public int[] getSelectedIndices() { return list.getSelectedIndices(); }
	public void setSelectedIndex(int index) { list.setSelectedIndex(index); }

	public LabeledList(String label, Dimension size, ArrayList<Data> data, Data lastItem, DeveloperPanel panel) {
		super(label, size, panel.parent.loadMonitor);
		this.panel = panel;
		this.lastItem = lastItem;
		list = new JList<Data>(listModel);
		list.setFont(DeveloperGUI.LIST_FONT);
		list.addListSelectionListener(this);
		list.addKeyListener(this);
		setAll(data);
		setComponent(new JScrollPane(list));
	}

	public LabeledList(String label, Dimension size, Data lastItem, DeveloperPanel panel) {
		this(label, size, null, lastItem, panel);
	}

	protected void addItem(Data data) {
		add(data);
		setSelectedIndex(listSize() - 2);
	}

	public void delete(int index) {
		listModel.remove(index);
		list.setSelectedIndex(INITIAL_SELECTION);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_DELETE:
			deleteSelection();
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}
	@Override
	public void keyTyped(KeyEvent e) {}
}

class QuestionList extends LabeledList<Question> {
	public final static String LABEL = "Question List";

	public QuestionList(ArrayList<Question> questions, DeveloperPanel panel) {
		super(LABEL, DeveloperGUI.QUESTION_LIST_SIZE, questions, new Question(true), panel);
	}

	public QuestionList(DeveloperPanel panel) {
		super(LABEL, DeveloperGUI.QUESTION_LIST_SIZE, new Question(true), panel);
	}

	@Override
	public void addItem() {
		Question question = new Question();
		panel.parent.getDatabase().addQuestion(question);
		addItem(question);
	}
	
	@Override
	public void deleteCurrent() {
		int index = list.getSelectedIndex();
		delete(index);
		panel.removeQuestion(index);
	}

	@Override
	public void deleteSelection() {
		int[] indices = list.getSelectedIndices();
		for (int i = indices.length - 1; i >= 0; --i) {
			if (listSize() > 2) {
				delete(indices[i]);
				panel.removeQuestion(indices[i]);
			}
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		int index = getSelectedIndex();
		if (index == listSize() - 1 && get(index).getText().equals(Question.LAST_TEXT)) addItem();
		else {
			try {
				panel.setQuestion(index);
			} catch (Exception ex) {
				//System.err.println("Failed to change question");
			}
		}
	}
}

class AnswerList extends LabeledList<Answer> {
	public final static String LABEL = "Answers";

	public AnswerList(ArrayList<Answer> answers, DeveloperPanel panel) {
		super(LABEL, DeveloperGUI.ANSWER_LIST_SIZE, answers, new Answer(true), panel);
	}

	public AnswerList(DeveloperPanel panel) {
		super(LABEL, DeveloperGUI.ANSWER_LIST_SIZE, new Answer(true), panel);
	}

	@Override
	public void addItem() {
		if (panel.parent.getDatabase().getQuestion() != null) {
			Answer answer = new Answer();
			panel.parent.getDatabase().addAnswer(answer);
			addItem(answer);
		}
	}
	
	@Override
	public void deleteCurrent() {
		int index = list.getSelectedIndex();
		delete(index);
		panel.removeAnswer(index);
	}

	@Override
	public void deleteSelection() {
		int[] indices = list.getSelectedIndices();
		for (int i = indices.length - 1; i >= 0; --i) {
			if (listSize() > 2) {
				delete(indices[i]);
				panel.removeAnswer(indices[i]);
			}
		}
	}


	@Override
	public void valueChanged(ListSelectionEvent e) {
		int index = getSelectedIndex();
		if (index == listSize() - 1 && get(index).getText().equals(Answer.LAST_TEXT)) addItem();
		else {
			try {
				panel.setAnswer(index);
			} catch (Exception ex) {
				//System.err.println("Failed to change answer");
			}
		}
	}
}

// TEXTBOXES

abstract class LabeledTextBox<Data> extends LabeledComponent implements DocumentListener {
	protected JTextArea textBox = new JTextArea();
	protected DeveloperPanel panel;

	public LabeledTextBox(String label, Dimension size, String text, DeveloperPanel panel) {
		super(label, size, panel.parent.loadMonitor);
		this.panel = panel;
		if (text != null)
			textBox.setText(text);
		textBox.setFont(DeveloperGUI.TEXTBOX_FONT);
		textBox.setLineWrap(true);
		textBox.getDocument().addDocumentListener(this);

		setComponent(new JScrollPane(textBox));
	}
}

class QuestionBox extends LabeledTextBox<Question> {
	public final static String LABEL = "Question";

	public QuestionBox(String text, DeveloperPanel panel) {
		super(LABEL, DeveloperGUI.QUESTION_BOX_SIZE, text, panel);
	}

	public QuestionBox(DeveloperPanel panel) {
		this(null, panel);
	}

	public void setText(String question) {
		textBox.setText(question);
	}
	
	@Override
	public void changedUpdate(DocumentEvent e) { panel.setQuestionText(textBox.getText()); }

	@Override
	public void insertUpdate(DocumentEvent e) { panel.setQuestionText(textBox.getText()); }

	@Override
	public void removeUpdate(DocumentEvent e) { panel.setQuestionText(textBox.getText()); }
}

class AnswerBox extends LabeledTextBox<Answer> {
	public final static String LABEL = "Answer";

	public AnswerBox(String text, DeveloperPanel panel) {
		super(LABEL, DeveloperGUI.ANSWER_BOX_SIZE, text, panel);
	}

	public AnswerBox(DeveloperPanel panel) {
		this(null, panel);
	}

	public void setText(String answer) {
		textBox.setText(answer);
	}
	
	@Override
	public void changedUpdate(DocumentEvent e) { panel.setAnswerText(textBox.getText()); }

	@Override
	public void insertUpdate(DocumentEvent e) { panel.setAnswerText(textBox.getText()); }

	@Override
	public void removeUpdate(DocumentEvent e) { panel.setAnswerText(textBox.getText()); }
}

//CHOICES FIELD

class ChoicesField extends LabeledComponent implements DocumentListener, FocusListener {
	public final static String LABEL = "Choices", MAX_FIELD_STRING = "9999";
	private final static int MARGIN = 5;
	
	private DeveloperPanel panel;
	private JTextField textField;

	public ChoicesField(String text, LoadMonitor loadMonitor, DeveloperPanel panel) {
		super(LABEL, DeveloperGUI.CHOICES_BOX_SIZE, true, loadMonitor);
		
		this.panel = panel;
		
		textField = new JTextField(text);
		textField.setHorizontalAlignment(JTextField.RIGHT);
		
		Font font = DeveloperGUI.TEXTBOX_FONT;
		textField.setFont(font);
		
		FontMetrics metrics = textField.getFontMetrics(textField.getFont());
		Dimension size = new Dimension(metrics.stringWidth(MAX_FIELD_STRING) + MARGIN, metrics.getHeight() + MARGIN);
		textField.setMinimumSize(size);
		textField.setPreferredSize(size);
		textField.setMaximumSize(size);
		
		textField.getDocument().addDocumentListener(this);
		textField.addFocusListener(this);
				
		setComponent(textField);
	}

	public ChoicesField(LoadMonitor loadMonitor, DeveloperPanel panel) {
		this(null, loadMonitor, panel);
	}

	public void setText(String choices) {
		textField.setText(choices);
	}

	@Override
	public void changedUpdate(DocumentEvent arg0) { textChange(); }

	@Override
	public void insertUpdate(DocumentEvent e) { textChange(); }

	@Override
	public void removeUpdate(DocumentEvent e) { textChange(); }

	private void textChange() {
		String text = textField.getText();
		int choices;
		try {
			choices = Integer.parseInt(text);
		} catch(Exception e) {
			choices = 1;
		}
		panel.setChoices(choices);
	}

	@Override
	public void focusGained(FocusEvent e) {}

	@Override
	public void focusLost(FocusEvent e) {
		textField.setText(String.valueOf(panel.getChoices()));
	}
}

// CONFIDENCE TABLE

class ConfidenceTable extends LabeledComponent {
	public final static int TOTAL_FIELD_HEIGHT_MARGIN = 8;
	public final static String LABEL = "Confidences", DEFAULT_CELL_VALUE = "0.0", TOTAL_LABEL = "Total";
	public final static Color TABLE_LEFT_COLUMN = new Color(0xF4F4F8), TOTAL_FOREGROUND = new Color(0x000000);

	final DeveloperPanel panel;
	private JTable table;
	private JTextField totalField;
	private double total = 0;

	public ConfidenceTable(MBTIConfidenceList confidences, LoadMonitor loadMonitor, DeveloperPanel panel) {
		super(LABEL, DeveloperGUI.CONFIDENCE_TABLE_SIZE, loadMonitor);

		this.panel = panel;
		
		String[][] data = new String[MBTI.COUNT][2];
		for (int i = 0; i < MBTI.COUNT; ++i)
			data[i] = new String[] { MBTI.values()[i].name(), confidences == null ? DEFAULT_CELL_VALUE
					: String.valueOf(confidences.get(MBTI.values()[i]).getConfidence()) };
		String[] headers = new String[] { "MBTI", "Value" };
		table = new JTable(new ConfidenceTableModel(data, headers, this));

		ConfidenceCellRenderer renderer = new ConfidenceCellRenderer();
		ConfidenceCellEditor editor = new ConfidenceCellEditor();
		TableColumn col1 = table.getColumnModel().getColumn(0);
		TableColumn col2 = table.getColumnModel().getColumn(1);

		col1.setCellRenderer(renderer);
		col1.setCellEditor(editor);
		col2.setCellRenderer(renderer);
		col2.setCellEditor(editor);

		JPanel jpanel = new JPanel();
		jpanel.setLayout(new BoxLayout(jpanel, BoxLayout.Y_AXIS));

		table.getTableHeader().setFont(DeveloperGUI.TABLE_HEADER_FONT);
		table.setBackground(TABLE_LEFT_COLUMN);

		JScrollPane scrollPane = new JScrollPane(table);
		jpanel.add(scrollPane);

		JLabel totalLabel = new JLabel(TOTAL_LABEL);
		totalLabel.setFont(DeveloperGUI.TABLE_TOTAL_FONT);
		totalLabel.setForeground(TOTAL_FOREGROUND);

		if (confidences != null) {
			for (MBTIConfidence confidence : confidences) {
				total += confidence.getConfidence();
			}
		}
		totalField = new JTextField(String.valueOf(total));

		totalField.setMinimumSize(new Dimension(getMinimumSize().width,
				(int) totalField.getPreferredSize().getHeight() + TOTAL_FIELD_HEIGHT_MARGIN));
		totalField.setHorizontalAlignment(JTextField.RIGHT);
		totalField.setFont(DeveloperGUI.TABLE_TOTAL_FONT);
		totalField.setBorder(null);
		totalField.setForeground(TOTAL_FOREGROUND);
		totalField.setOpaque(false);
		totalField.setPreferredSize(new Dimension(1000, 10));

		JPanel totalPanel = new JPanel();
		totalPanel.setLayout(new BoxLayout(totalPanel, BoxLayout.X_AXIS));
		totalPanel.add(totalLabel);
		totalPanel.add(totalField);

		jpanel.add(totalPanel);

		setComponent(jpanel);
	}

	public ConfidenceTable(LoadMonitor loadMonitor, DeveloperPanel panel) {
		this(null, loadMonitor, panel);
	}

	public void updateTotal(double oldValue, double newValue) {
		if (newValue > oldValue)
			total += newValue - oldValue;
		else if (newValue < oldValue)
			total -= oldValue - newValue;
		totalField.setText(String.valueOf(total));
	}

	public void setConfidences(MBTIConfidenceList confidences) {
		double total = 0;
		for (int i = 0; i < MBTI.COUNT; ++i) {
			double confidence = confidences.get(MBTI.values()[i]).getConfidence();
			total += confidence;
			table.getModel().setValueAt(String.valueOf(confidence), i, 1);
		}
		totalField.setText(String.valueOf(total));
	}
}

class ConfidenceTableModel extends DefaultTableModel {
	ConfidenceTable table;

	public ConfidenceTableModel(String[][] data, String[] headers, ConfidenceTable table) {
		super(data, headers);
		this.table = table;
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return col == 1;
	}

	@Override
	public void setValueAt(Object aValue, int row, int column) {
		double newValue = Double.parseDouble((String) aValue);
		table.updateTotal(Double.parseDouble((String) getValueAt(row, column)), newValue);
		table.panel.setConfidence(MBTI.values()[row], newValue);
		super.setValueAt(aValue, row, column);
	}
}

class ConfidenceCellRenderer extends DefaultTableCellRenderer {
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		if (column == 0) {
			JLabel component = new JLabel(" " + (String) value);
			component.setFont(DeveloperGUI.TABLE_LEFT_FONT);
			return component;
		} else {
			JTextField component = new JTextField((String) value + " ");
			component.setFont(DeveloperGUI.TABLE_RIGHT_FONT);
			component.setBorder(null);
			component.setHorizontalAlignment(RIGHT);
			return component;
		}
	}

}

class ConfidenceCellEditor extends DefaultCellEditor implements TableCellEditor {
	private String data = ConfidenceTable.DEFAULT_CELL_VALUE;

	public ConfidenceCellEditor() {
		super(new JTextField());
		((JTextField) editorComponent).setBorder(null);
	}

	@Override
	public Object getCellEditorValue() {
		return data;
	};

	@Override
	public boolean stopCellEditing() {
		try {
			JTextField textField = (JTextField) getComponent();
			String text = textField.getText().trim();
			double cellValue = Double.parseDouble(text);

			if (cellValue >= 0 && cellValue <= 1) {
				data = String.valueOf(Double.parseDouble(text));
				return super.stopCellEditing();
			}
		} finally {
			return false;
		}
	}
}
