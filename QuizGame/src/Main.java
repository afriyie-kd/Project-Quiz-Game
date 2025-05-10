import javax.swing.*;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Warning: Couldn't set system look and feel");
        }

        SwingUtilities.invokeLater(() -> {
            try {
                DatabaseManager dbManager = new DatabaseManager();
                dbManager.initializeDatabase();
                try {
                    dbManager.testDatabaseConnection();
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, 
                        "Database connection failed.\nRunning in offline mode with limited functionality.\nError: " + e.getMessage(),
                        "Connection Warning",
                        JOptionPane.WARNING_MESSAGE);
                }
                QuizGameGUI quizGame = new QuizGameGUI();
                quizGame.show();
                
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                    "Failed to initialize application: " + e.getMessage(),
                    "Startup Error",
                    JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}