import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * LoginGUI
 * <p>
 * Represents the login GUI
 *
 * @author Luka Narisawa
 * @version April 19, 2021
 */

public class LoginGUI extends JComponent implements Runnable {
    public static String username;
    // login GUI fields
    static JFrame loginFrame;
    static JTextField usernameField;
    static JTextField passwordField;
    static JButton loginButton;
    static JButton createAccountButton;
    private static String password;
    private static String action;
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // login GUI
            if (e.getSource() == loginButton) {
                username = usernameField.getText();
                password = passwordField.getText();
                action = "login";
                loginFrame.dispose();
            }
            if (e.getSource() == createAccountButton) {
                action = "create";
                loginFrame.dispose();
            }
            //
        }
    };
    WindowListener windowListener = new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent evt) {
            action = "closed";
        }
    };

    public LoginGUI() {
        action = null;
        username = null;
        password = null;
    }

    public String getAction() {
        return action;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void run() {
        loginFrame = new JFrame("Message Login");
        Container content = loginFrame.getContentPane();
        content.setLayout(new BorderLayout());
        loginFrame.setSize(400, 200);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        loginFrame.addWindowListener(windowListener);
        // username, password, login button on the CENTER
        JLabel labelU = new JLabel("Username");
        JLabel labelP = new JLabel("Password");
        usernameField = new JTextField(10); // text field for username
        usernameField.addActionListener(actionListener);
        passwordField = new JTextField(10); // text field for password
        passwordField.addActionListener(actionListener);
        loginButton = new JButton("Login"); // login button
        loginButton.addActionListener(actionListener);

        JPanel panel1 = new JPanel();
        panel1.add(labelU);
        panel1.add(usernameField);

        JPanel panel2 = new JPanel();
        panel2.add(labelP);
        panel2.add(passwordField);

        JPanel panel3 = new JPanel();
        panel3.setLayout(new BoxLayout(panel3, BoxLayout.PAGE_AXIS));
        panel3.add(panel1);
        panel3.add(panel2);
        panel3.add(loginButton);
        content.add(panel3, BorderLayout.CENTER);
        //  create account and edit or delete button on SOUTH
        createAccountButton = new JButton("Create account");
        createAccountButton.addActionListener(actionListener);
        JPanel panel4 = new JPanel();
        JLabel create = new JLabel("Don't have an account?");
        panel4.add(create);
        panel4.add(createAccountButton);
        content.add(panel4, BorderLayout.SOUTH);
        loginFrame.getRootPane().setDefaultButton(loginButton);
        loginFrame.setVisible(true);
    }
}