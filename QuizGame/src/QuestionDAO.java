import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDAO {
    private DatabaseManager dbManager;
    
    public QuestionDAO(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }
    
    public void addQuestion(Question question) throws SQLException {
        String sql = "INSERT INTO questions (question_text, option_a, option_b, option_c, option_d, correct_answer, difficulty, category) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, question.getQuestionText());
            pstmt.setString(2, question.getOptionA());
            pstmt.setString(3, question.getOptionB());
            pstmt.setString(4, question.getOptionC());
            pstmt.setString(5, question.getOptionD());
            pstmt.setString(6, String.valueOf(question.getCorrectAnswer()));
            pstmt.setInt(7, question.getDifficulty());
            pstmt.setString(8, question.getCategory());
            
            pstmt.executeUpdate();
        }
    }
    public Question getQuestion(int id) throws SQLException {
        String sql = "SELECT * FROM questions WHERE id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Question(
                    rs.getInt("id"),
                    rs.getString("question_text"),
                    rs.getString("option_a"),
                    rs.getString("option_b"),
                    rs.getString("option_c"),
                    rs.getString("option_d"),
                    rs.getString("correct_answer").charAt(0),
                    rs.getInt("difficulty"),
                    rs.getString("category")
                );
            }
        }
        return null;
    }
    public List<Question> getAllQuestions() throws SQLException {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT * FROM questions";
        
        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                questions.add(new Question(
                    rs.getInt("id"),
                    rs.getString("question_text"),
                    rs.getString("option_a"),
                    rs.getString("option_b"),
                    rs.getString("option_c"),
                    rs.getString("option_d"),
                    rs.getString("correct_answer").charAt(0),
                    rs.getInt("difficulty"),
                    rs.getString("category")
                ));
            }
        }
        return questions;
    }
    public void updateQuestion(Question question) throws SQLException {
        String sql = "UPDATE questions SET question_text = ?, option_a = ?, option_b = ?, " +
                     "option_c = ?, option_d = ?, correct_answer = ?, difficulty = ?, category = ? " +
                     "WHERE id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, question.getQuestionText());
            pstmt.setString(2, question.getOptionA());
            pstmt.setString(3, question.getOptionB());
            pstmt.setString(4, question.getOptionC());
            pstmt.setString(5, question.getOptionD());
            pstmt.setString(6, String.valueOf(question.getCorrectAnswer()));
            pstmt.setInt(7, question.getDifficulty());
            pstmt.setString(8, question.getCategory());
            pstmt.setInt(9, question.getId());
            
            pstmt.executeUpdate();
        }
    }
    public void deleteQuestion(int id) throws SQLException {
        String sql = "DELETE FROM questions WHERE id = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
    public Question getRandomQuestion() throws SQLException {
        String sql = "SELECT * FROM questions ORDER BY RAND() LIMIT 1";
        
        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return new Question(
                    rs.getInt("id"),
                    rs.getString("question_text"),
                    rs.getString("option_a"),
                    rs.getString("option_b"),
                    rs.getString("option_c"),
                    rs.getString("option_d"),
                    rs.getString("correct_answer").charAt(0),
                    rs.getInt("difficulty"),
                    rs.getString("category")
                );
            }
        }
        return null;
    }
}