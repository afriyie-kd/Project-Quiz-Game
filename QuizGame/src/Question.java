public class Question {
    private int id;
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private char correctAnswer;
    private int difficulty;
    private String category;
    
    public Question(int id, String questionText, String optionA, String optionB, 
                   String optionC, String optionD, char correctAnswer, 
                   int difficulty, String category) {
        this.id = id;
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAnswer = correctAnswer;
        this.difficulty = difficulty;
        this.category = category;
    }
    
    // Getter methods
    public int getId() {
        return id;
    }
    
    public String getQuestionText() {
        return questionText;
    }
    
    public String getOptionA() {
        return optionA;
    }
    
    public String getOptionB() {
        return optionB;
    }
    
    public String getOptionC() {
        return optionC;
    }
    
    public String getOptionD() {
        return optionD;
    }
    
    public char getCorrectAnswer() {
        return correctAnswer;
    }
    
    public int getDifficulty() {
        return difficulty;
    }
    
    public String getCategory() {
        return category;
    }
    
    // Setter methods (optional but useful)
    public void setId(int id) {
        this.id = id;
    }
    
    // ... add other setters as needed
    
    public boolean isCorrect(char answer) {
        return Character.toUpperCase(answer) == Character.toUpperCase(correctAnswer);
    }
}