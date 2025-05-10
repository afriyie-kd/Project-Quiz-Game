import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class StyleUtils {
    public static final Color MAIN_BACKGROUND = new Color(45, 45, 45);     
    public static final Color PANEL_BACKGROUND = new Color(60, 60, 60);     
    public static final Color PRIMARY_COLOR = new Color(0, 120, 215);      
    public static final Color TEXT_COLOR = new Color(240, 240, 240);        
    public static final Color RADIO_BG = new Color(70, 70, 70);             
    public static final Color SUCCESS_COLOR = new Color(50, 200, 50);       
    public static final Color ERROR_COLOR = new Color(255, 80, 80);        
    

    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 32);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font QUESTION_FONT = new Font("Segoe UI", Font.BOLD, 18);  
    public static final Font OPTION_FONT = new Font("Segoe UI", Font.PLAIN, 16);
    public static void styleButton(JButton button) {
        button.setFont(BUTTON_FONT);
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new CompoundBorder(
            new LineBorder(new Color(0, 80, 150), 2), 
            new EmptyBorder(10, 25, 10, 25)         
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    public static void styleTitleLabel(JLabel label) {
        label.setFont(TITLE_FONT);
        label.setForeground(TEXT_COLOR);
        label.setHorizontalAlignment(SwingConstants.CENTER);
    }
    public static void stylePanel(JPanel panel) {
        panel.setBackground(PANEL_BACKGROUND);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        setComponentForeground(panel, TEXT_COLOR);
    }
    public static void styleQuestionLabel(JLabel label) {
        label.setFont(QUESTION_FONT);
        label.setForeground(new Color(255, 255, 180));  // Light yellow for better contrast
        label.setBorder(new EmptyBorder(0, 0, 25, 0));  // More bottom margin
        label.setHorizontalAlignment(SwingConstants.CENTER);
    }
    public static void styleRadioButton(JRadioButton radio) {
        radio.setFont(OPTION_FONT);
        radio.setBackground(RADIO_BG);  // Different from panel for contrast
        radio.setForeground(TEXT_COLOR);
        radio.setFocusPainted(false);
        radio.setBorder(new EmptyBorder(8, 15, 8, 15));  // More padding
    }
    public static void styleTextField(JTextField textField) {
        textField.setFont(OPTION_FONT);
        textField.setBackground(Color.WHITE);
        textField.setForeground(Color.BLACK);
        textField.setBorder(new CompoundBorder(
            new LineBorder(new Color(180, 180, 180)),
            new EmptyBorder(8, 12, 8, 12)
        ));
    }
    public static JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorder(new CompoundBorder(
            new LineBorder(bgColor.darker(), 2),
            new EmptyBorder(10, 25, 10, 25)
        ));
        return button;
    }
    public static JLabel createStyledLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }
    private static void setComponentForeground(Container container, Color color) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JLabel || comp instanceof AbstractButton) {
                comp.setForeground(color);
            }
            if (comp instanceof Container) {
                setComponentForeground((Container)comp, color);
            }
        }
    }
    public static void styleFrame(JFrame frame) {
        frame.getContentPane().setBackground(MAIN_BACKGROUND);
    }
}