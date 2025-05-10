import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class QuizGameGUI {
    private JFrame mainFrame;
    private JPanel currentPanel;
    private DatabaseManager dbManager;
    private QuestionDAO questionDAO;
    private int currentScore = 0;
    private int questionsAnswered = 0;
    private final int TOTAL_QUESTIONS = 10;
    
    public QuizGameGUI() {
        try {
            dbManager = new DatabaseManager();
            dbManager.initializeDatabase();
            questionDAO = new QuestionDAO(dbManager);
            
            initializeMainFrame();
            showMainMenu();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                "Database initialization failed: " + e.getMessage(),
                "Startup Error",
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
    
    private void initializeMainFrame() {
        mainFrame = new JFrame("Quiz Game");
        StyleUtils.styleFrame(mainFrame);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(900, 700);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setLocationRelativeTo(null);
        
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/quiz_icon.png"));
            mainFrame.setIconImage(icon.getImage());
        } catch (Exception e) {
            System.out.println("Icon not found, using default");
        }
    }
    
    private void showMainMenu() {
        JPanel menuPanel = new JPanel(new GridBagLayout());
        StyleUtils.stylePanel(menuPanel);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel titleLabel = new JLabel("Quiz Game", SwingConstants.CENTER);
        StyleUtils.styleTitleLabel(titleLabel);
        
        JButton startButton = new JButton("Start Quiz");
        JButton manageButton = new JButton("Manage Questions");
        JButton scoresButton = new JButton("View High Scores");
        JButton exitButton = new JButton("Exit");  
        StyleUtils.styleButton(startButton);
        StyleUtils.styleButton(manageButton);
        StyleUtils.styleButton(scoresButton);
        StyleUtils.styleButton(exitButton);
                // Set button text colors
        startButton.setForeground(Color.BLUE);
        manageButton.setForeground(Color.BLUE);
        scoresButton.setForeground(Color.BLUE);
        exitButton.setForeground(Color.BLUE); 

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        menuPanel.add(titleLabel, gbc);
        
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        menuPanel.add(Box.createVerticalStrut(30), gbc);
        
        gbc.gridy = 2;
        menuPanel.add(startButton, gbc);
        
        gbc.gridy = 3;
        menuPanel.add(manageButton, gbc);
        
        gbc.gridy = 4;
        menuPanel.add(scoresButton, gbc);
        
        gbc.gridy = 5;
        menuPanel.add(exitButton, gbc);
        
        startButton.addActionListener(e -> startQuiz());
        manageButton.addActionListener(e -> showQuestionManagement());
        scoresButton.addActionListener(e -> showHighScores());
        exitButton.addActionListener(e -> System.exit(0));
        
        switchPanel(menuPanel);
    }
    
    private void startQuiz() {
        currentScore = 0;
        questionsAnswered = 0;
        askQuestion();
    }
    
    private void askQuestion() {
        try {
            Question question = questionDAO.getRandomQuestion();
            
            if (question == null) {
                JOptionPane.showMessageDialog(mainFrame, "No questions available!");
                showMainMenu();
                return;
            }
            
            JPanel questionPanel = new JPanel(new BorderLayout(10, 15));
            StyleUtils.stylePanel(questionPanel);
            
            // Header panel with question counter and score
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(StyleUtils.PANEL_BACKGROUND);
            
            JLabel counterLabel = new JLabel("Question " + (questionsAnswered + 1) + " of " + TOTAL_QUESTIONS);
            counterLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            counterLabel.setForeground(StyleUtils.TEXT_COLOR);
            counterLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            headerPanel.add(counterLabel, BorderLayout.EAST);
            
            JLabel scoreLabel = new JLabel("Score: " + currentScore);
            scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            scoreLabel.setForeground(StyleUtils.TEXT_COLOR);
            headerPanel.add(scoreLabel, BorderLayout.WEST);
            
            questionPanel.add(headerPanel, BorderLayout.NORTH);
            
            // Main content panel
            JPanel contentPanel = new JPanel(new BorderLayout(10, 20));
            contentPanel.setBackground(StyleUtils.PANEL_BACKGROUND);
            
            // Question label
            JLabel questionLabel = new JLabel("<html><div style='text-align: center; padding: 10px;'>" + 
                question.getQuestionText() + "</div></html>", SwingConstants.CENTER);
            StyleUtils.styleQuestionLabel(questionLabel);
            contentPanel.add(questionLabel, BorderLayout.NORTH);
            
            // Options panel
            ButtonGroup optionsGroup = new ButtonGroup();
            JRadioButton optionA = new JRadioButton("<html><div style='width: 300px;'>A: " + question.getOptionA() + "</div></html>");
            JRadioButton optionB = new JRadioButton("<html><div style='width: 300px;'>B: " + question.getOptionB() + "</div></html>");
            JRadioButton optionC = new JRadioButton("<html><div style='width: 300px;'>C: " + question.getOptionC() + "</div></html>");
            JRadioButton optionD = new JRadioButton("<html><div style='width: 300px;'>D: " + question.getOptionD() + "</div></html>");
            
            StyleUtils.styleRadioButton(optionA);
            StyleUtils.styleRadioButton(optionB);
            StyleUtils.styleRadioButton(optionC);
            StyleUtils.styleRadioButton(optionD);
            
            optionsGroup.add(optionA);
            optionsGroup.add(optionB);
            optionsGroup.add(optionC);
            optionsGroup.add(optionD);
            
            JPanel optionsPanel = new JPanel(new GridLayout(4, 1, 10, 10));
            optionsPanel.setBackground(StyleUtils.PANEL_BACKGROUND);
            optionsPanel.setBorder(new EmptyBorder(20, 50, 20, 50));
            optionsPanel.add(optionA);
            optionsPanel.add(optionB);
            optionsPanel.add(optionC);
            optionsPanel.add(optionD);
            
            contentPanel.add(optionsPanel, BorderLayout.CENTER);
            
            // Button panel
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            buttonPanel.setBackground(StyleUtils.PANEL_BACKGROUND);
            
            JButton submitButton = new JButton("Submit Answer");
            StyleUtils.styleButton(submitButton);
            submitButton.setForeground(Color.BLACK);
            
            JButton returnButton = StyleUtils.createStyledButton("Return to Menu", StyleUtils.ERROR_COLOR);
            returnButton.setForeground(Color.BLACK);
            
            buttonPanel.add(returnButton);
            buttonPanel.add(submitButton);
            
            questionPanel.add(contentPanel, BorderLayout.CENTER);
            questionPanel.add(buttonPanel, BorderLayout.SOUTH);
            
            submitButton.addActionListener(e -> {
                char selectedAnswer = ' ';
                if (optionA.isSelected()) selectedAnswer = 'A';
                else if (optionB.isSelected()) selectedAnswer = 'B';
                else if (optionC.isSelected()) selectedAnswer = 'C';
                else if (optionD.isSelected()) selectedAnswer = 'D';
                
                if (selectedAnswer == ' ') {
                    JOptionPane.showMessageDialog(mainFrame, "Please select an answer!");
                    return;
                }
                
                boolean isCorrect = question.isCorrect(selectedAnswer);
                questionsAnswered++;
                
                if (isCorrect) {
                    currentScore += 10 * question.getDifficulty();
                    showFeedbackDialog("Correct!", "+" + (10 * question.getDifficulty()) + " points", true);
                } else {
                    showFeedbackDialog("Incorrect!", "The correct answer was " + question.getCorrectAnswer(), false);
                }
            });
            
            returnButton.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(
                    mainFrame, 
                    "Are you sure you want to return to the menu? Your progress will be lost.",
                    "Confirm Exit",
                    JOptionPane.YES_NO_OPTION
                );
                
                if (confirm == JOptionPane.YES_OPTION) {
                    showMainMenu();
                }
            });
            
            switchPanel(questionPanel);
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(mainFrame, "Error loading question: " + ex.getMessage());
        }
    }
    
    private void showFeedbackDialog(String title, String message, boolean isCorrect) {
        JDialog feedbackDialog = new JDialog(mainFrame, title, true);
        feedbackDialog.setSize(400, 200);
        feedbackDialog.setLayout(new BorderLayout());
        feedbackDialog.setLocationRelativeTo(mainFrame);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(isCorrect ? StyleUtils.SUCCESS_COLOR : StyleUtils.ERROR_COLOR);
        
        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(Color.WHITE);
        panel.add(label, BorderLayout.CENTER);
        
        JButton nextButton = new JButton(questionsAnswered < TOTAL_QUESTIONS ? "Next Question" : "View Results");
        StyleUtils.styleButton(nextButton);
        nextButton.setBackground(isCorrect ? StyleUtils.SUCCESS_COLOR.darker() : StyleUtils.ERROR_COLOR.darker());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(nextButton);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        feedbackDialog.add(panel);
        
        nextButton.addActionListener(e -> {
            feedbackDialog.dispose();
            if (questionsAnswered < TOTAL_QUESTIONS) {
                askQuestion();
            } else {
                showQuizResult();
            }
        });
        
        feedbackDialog.setVisible(true);
    }
    
    private void showQuizResult() {
        JPanel resultPanel = new JPanel(new BorderLayout(10, 10));
        StyleUtils.stylePanel(resultPanel);
        
        JLabel resultLabel = new JLabel("Quiz Completed!", SwingConstants.CENTER);
        resultLabel.setFont(StyleUtils.TITLE_FONT);
        resultLabel.setForeground(StyleUtils.TEXT_COLOR);
        
        JLabel scoreLabel = new JLabel("Your Score: " + currentScore, SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        scoreLabel.setForeground(StyleUtils.TEXT_COLOR);
        
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        inputPanel.setBackground(StyleUtils.PANEL_BACKGROUND);
        
        JLabel nameLabel = new JLabel("Enter your name: ");
        nameLabel.setForeground(StyleUtils.TEXT_COLOR);
        inputPanel.add(nameLabel);
        
        JTextField nameField = new JTextField(20);
        StyleUtils.styleTextField(nameField);
        inputPanel.add(nameField);
        
        JButton saveButton = new JButton("Save Score");
        StyleUtils.styleButton(saveButton);
        saveButton.setForeground(Color.GRAY);
        inputPanel.add(saveButton);
        
        JButton menuButton = new JButton("Back to Main Menu");
        StyleUtils.styleButton(menuButton);
        menuButton.setForeground(Color.GRAY);
        
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 20));
        centerPanel.setBackground(StyleUtils.PANEL_BACKGROUND);
        centerPanel.add(scoreLabel);
        centerPanel.add(inputPanel);
        
        resultPanel.add(resultLabel, BorderLayout.NORTH);
        resultPanel.add(centerPanel, BorderLayout.CENTER);
        resultPanel.add(menuButton, BorderLayout.SOUTH);
        
        saveButton.addActionListener(e -> {
            String playerName = nameField.getText().trim();
            if (!playerName.isEmpty()) {
                try {
                    String sql = "INSERT INTO scores (player_name, score) VALUES (?, ?)";
                    try (Connection conn = dbManager.getConnection();
                         PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        
                        pstmt.setString(1, playerName);
                        pstmt.setInt(2, currentScore);
                        pstmt.executeUpdate();
                        
                        JOptionPane.showMessageDialog(mainFrame, 
                            "Score saved successfully!", 
                            "Success", 
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(mainFrame, 
                        "Error saving score: " + ex.getMessage(), 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(mainFrame, 
                    "Please enter your name to save your score", 
                    "Input Required", 
                    JOptionPane.WARNING_MESSAGE);
            }
        });
        
        menuButton.addActionListener(e -> showMainMenu());
        
        switchPanel(resultPanel);
    }
    
private void showQuestionManagement() {
    try {
        List<Question> questions = questionDAO.getAllQuestions();
        
        JPanel managementPanel = new JPanel(new BorderLayout());
        StyleUtils.stylePanel(managementPanel);
        
        String[] columnNames = {"ID", "Question", "Option A", "Option B", "Option C", "Option D", "Correct", "Difficulty", "Category"};
        Object[][] data = new Object[questions.size()][columnNames.length];
        
        for (int i = 0; i < questions.size(); i++) {
            Question q = questions.get(i);
            data[i][0] = q.getId();
            data[i][1] = q.getQuestionText();
            data[i][2] = q.getOptionA();
            data[i][3] = q.getOptionB();
            data[i][4] = q.getOptionC();
            data[i][5] = q.getOptionD();
            data[i][6] = q.getCorrectAnswer();
            data[i][7] = q.getDifficulty();
            data[i][8] = q.getCategory();
        }
        
        JTable questionTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(questionTable);
        
        JPanel buttonPanel = new JPanel();
        StyleUtils.stylePanel(buttonPanel);
        
        JButton addButton = new JButton("Add Question");
        JButton editButton = new JButton("Edit Selected");
        JButton deleteButton = new JButton("Delete Selected");
        JButton backButton = new JButton("Back to Menu");
        StyleUtils.styleButton(addButton);
        StyleUtils.styleButton(editButton);
        StyleUtils.styleButton(deleteButton);
        StyleUtils.styleButton(backButton);
        
        // Set button text colors to GREY
        addButton.setForeground(Color.BLACK);
        editButton.setForeground(Color.BLACK);
        deleteButton.setForeground(Color.BLACK);
        backButton.setForeground(Color.BLACK);
        

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);
        
        managementPanel.add(scrollPane, BorderLayout.CENTER);
        managementPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        addButton.addActionListener(e -> showAddQuestionDialog());
        editButton.addActionListener(e -> {
            int selectedRow = questionTable.getSelectedRow();
            if (selectedRow >= 0) {
                int questionId = (int) questionTable.getValueAt(selectedRow, 0);
                try {
                    Question question = questionDAO.getQuestion(questionId);
                    showEditQuestionDialog(question);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Please select a question to edit");
            }
        });
        deleteButton.addActionListener(e -> {
            int selectedRow = questionTable.getSelectedRow();
            if (selectedRow >= 0) {
                int questionId = (int) questionTable.getValueAt(selectedRow, 0);
                try {
                    questionDAO.deleteQuestion(questionId);
                    showQuestionManagement();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Please select a question to delete");
            }
        });
        backButton.addActionListener(e -> showMainMenu());
        
        switchPanel(managementPanel);
        
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(mainFrame, "Error loading questions: " + ex.getMessage());
    }
}
    
    private void showAddQuestionDialog() {
        JDialog dialog = new JDialog(mainFrame, "Add New Question", true);
        dialog.setSize(500, 400);
        dialog.setLayout(new BorderLayout(10, 10));
        
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        StyleUtils.stylePanel(formPanel);
        
        JTextField questionField = new JTextField();
        JTextField optionAField = new JTextField();
        JTextField optionBField = new JTextField();
        JTextField optionCField = new JTextField();
        JTextField optionDField = new JTextField();
        JTextField correctAnswerField = new JTextField();
        JSpinner difficultySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
        JTextField categoryField = new JTextField();
        
        formPanel.add(new JLabel("Question Text:"));
        formPanel.add(questionField);
        formPanel.add(new JLabel("Option A:"));
        formPanel.add(optionAField);
        formPanel.add(new JLabel("Option B:"));
        formPanel.add(optionBField);
        formPanel.add(new JLabel("Option C:"));
        formPanel.add(optionCField);
        formPanel.add(new JLabel("Option D:"));
        formPanel.add(optionDField);
        formPanel.add(new JLabel("Correct Answer (A-D):"));
        formPanel.add(correctAnswerField);
        formPanel.add(new JLabel("Difficulty (1-5):"));
        formPanel.add(difficultySpinner);
        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryField);
        
        JButton saveButton = new JButton("Save Question");
        JButton cancelButton = new JButton("Cancel");
        
        StyleUtils.styleButton(saveButton);
        StyleUtils.styleButton(cancelButton);

        saveButton.setForeground(Color.BLACK);
        cancelButton.setForeground(Color.BLACK);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(StyleUtils.PANEL_BACKGROUND);
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        saveButton.addActionListener(e -> {
            try {
                Question newQuestion = new Question(
                    0,
                    questionField.getText(),
                    optionAField.getText(),
                    optionBField.getText(),
                    optionCField.getText(),
                    optionDField.getText(),
                    correctAnswerField.getText().toUpperCase().charAt(0),
                    (Integer) difficultySpinner.getValue(),
                    categoryField.getText()
                );
                
                questionDAO.addQuestion(newQuestion);
                JOptionPane.showMessageDialog(dialog, "Question added successfully!");
                dialog.dispose();
                showQuestionManagement();
                
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "Error adding question: " + ex.getMessage());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid input. Please check all fields.");
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setVisible(true);
    }
    
    private void showEditQuestionDialog(Question question) {
        JDialog dialog = new JDialog(mainFrame, "Edit Question", true);
        dialog.setSize(500, 400);
        dialog.setLayout(new BorderLayout(10, 10));
        
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        StyleUtils.stylePanel(formPanel);
        
        JTextField questionField = new JTextField(question.getQuestionText());
        JTextField optionAField = new JTextField(question.getOptionA());
        JTextField optionBField = new JTextField(question.getOptionB());
        JTextField optionCField = new JTextField(question.getOptionC());
        JTextField optionDField = new JTextField(question.getOptionD());
        JTextField correctAnswerField = new JTextField(String.valueOf(question.getCorrectAnswer()));
        JSpinner difficultySpinner = new JSpinner(new SpinnerNumberModel(question.getDifficulty(), 1, 5, 1));
        JTextField categoryField = new JTextField(question.getCategory());
        
        formPanel.add(new JLabel("Question Text:"));
        formPanel.add(questionField);
        formPanel.add(new JLabel("Option A:"));
        formPanel.add(optionAField);
        formPanel.add(new JLabel("Option B:"));
        formPanel.add(optionBField);
        formPanel.add(new JLabel("Option C:"));
        formPanel.add(optionCField);
        formPanel.add(new JLabel("Option D:"));
        formPanel.add(optionDField);
        formPanel.add(new JLabel("Correct Answer (A-D):"));
        formPanel.add(correctAnswerField);
        formPanel.add(new JLabel("Difficulty (1-5):"));
        formPanel.add(difficultySpinner);
        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryField);
        
        JButton saveButton = new JButton("Save Changes");
        JButton cancelButton = new JButton("Cancel");
        StyleUtils.styleButton(saveButton);
        StyleUtils.styleButton(cancelButton);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(StyleUtils.PANEL_BACKGROUND);
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        saveButton.addActionListener(e -> {
            try {
                Question updatedQuestion = new Question(
                    question.getId(),
                    questionField.getText(),
                    optionAField.getText(),
                    optionBField.getText(),
                    optionCField.getText(),
                    optionDField.getText(),
                    correctAnswerField.getText().toUpperCase().charAt(0),
                    (Integer) difficultySpinner.getValue(),
                    categoryField.getText()
                );
                
                questionDAO.updateQuestion(updatedQuestion);
                JOptionPane.showMessageDialog(dialog, "Question updated successfully!");
                dialog.dispose();
                showQuestionManagement();
                
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "Error updating question: " + ex.getMessage());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid input. Please check all fields.");
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setVisible(true);
    }
    
    private void showHighScores() {
        try {
            String sql = "SELECT player_name, score, date_played FROM scores ORDER BY score DESC LIMIT 10";
            
            JPanel scoresPanel = new JPanel(new BorderLayout());
            StyleUtils.stylePanel(scoresPanel);
            
            String[] columnNames = {"Rank", "Player Name", "Score", "Date Played"};
            
            try (Connection conn = dbManager.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                
                List<Score> scores = new ArrayList<>();
                while (rs.next()) {
                    scores.add(new Score(
                        0, // ID not used in display
                        rs.getString("player_name"),
                        rs.getInt("score"),
                        rs.getTimestamp("date_played")
                    ));
                }
                
                Object[][] data = new Object[scores.size()][];
                for (int i = 0; i < scores.size(); i++) {
                    Score s = scores.get(i);
                    data[i] = new Object[] {
                        i + 1, // rank
                        s.getPlayerName(),
                        s.getScore(),
                        s.getDatePlayed()
                    };
                }
                
                JTable scoresTable = new JTable(data, columnNames);
                JScrollPane scrollPane = new JScrollPane(scoresTable);
                
                JButton backButton = new JButton("Back to Menu");
                StyleUtils.styleButton(backButton);
                backButton.setForeground(Color.BLACK);
                
                scoresPanel.add(scrollPane, BorderLayout.CENTER);
                
                JPanel buttonPanel = new JPanel();
                buttonPanel.setBackground(StyleUtils.PANEL_BACKGROUND);
                buttonPanel.add(backButton);
                scoresPanel.add(buttonPanel, BorderLayout.SOUTH);
                
                backButton.addActionListener(e -> showMainMenu());
                
                switchPanel(scoresPanel);
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(mainFrame, "Error loading scores: " + ex.getMessage());
        }
    }
    
    private void switchPanel(JPanel newPanel) {
        if (currentPanel != null) {
            mainFrame.remove(currentPanel);
        }
        currentPanel = newPanel;
        mainFrame.add(currentPanel, BorderLayout.CENTER);
        mainFrame.revalidate();
        mainFrame.repaint();
    }
    
    public void show() {
        mainFrame.setVisible(true);
    }
}