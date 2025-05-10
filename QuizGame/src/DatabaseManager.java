import java.sql.*;
import javax.swing.JOptionPane;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/quiz_game?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "SQLPa$$w0rd"; 
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                "MySQL JDBC Driver not found. Please ensure the driver is included in your classpath.",
                "Driver Error",
                JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException("Failed to load MySQL JDBC driver", e);
        }
    }
    
    public Connection getConnection() throws SQLException {
        System.out.println("Connecting to database...");
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected successfully to: " + conn.getCatalog());
            return conn;
        } catch (SQLException e) {
            System.err.println("Connection failed:");
            throw e;
        }
    }
    
    public void initializeDatabase() throws SQLException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            String createQuestionsTable = "CREATE TABLE IF NOT EXISTS questions (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "question_text TEXT NOT NULL, " +
                "option_a VARCHAR(255) NOT NULL, " +
                "option_b VARCHAR(255) NOT NULL, " +
                "option_c VARCHAR(255) NOT NULL, " +
                "option_d VARCHAR(255) NOT NULL, " +
                "correct_answer CHAR(1) NOT NULL, " +
                "difficulty INT DEFAULT 1, " +
                "category VARCHAR(50))";
            
       
            String createScoresTable = "CREATE TABLE IF NOT EXISTS scores (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "player_name VARCHAR(100) NOT NULL, " +
                "score INT NOT NULL, " +
                "date_played TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
            
            stmt.execute(createQuestionsTable);
            stmt.execute(createScoresTable);
            
            System.out.println("Database tables initialized successfully");
        }
    }
    
    public void testDatabaseConnection() throws SQLException {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            if (!conn.isValid(2)) {
                throw new SQLException("Connection is not valid");
            }
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS count FROM questions");
            rs.next();
            int questionCount = rs.getInt("count");
            System.out.println("Found " + questionCount + " questions in database");
            
            if (questionCount == 0) {
                System.out.println("No questions found. Inserting sample questions...");
                stmt.executeUpdate("INSERT INTO questions (question_text, option_a, option_b, option_c, option_d, correct_answer, difficulty, category) VALUES " +
                    "('What is the capital of France?', 'London', 'Berlin', 'Paris', 'Madrid', 'C', 1, 'Geography')," +
                    "('Which planet is known as the Red Planet?', 'Venus', 'Mars', 'Jupiter', 'Saturn', 'B', 1, 'Science')");
                System.out.println("Sample questions inserted");
            }
        }
    }
    public boolean isDatabaseAvailable() {
        try {
            testDatabaseConnection();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}