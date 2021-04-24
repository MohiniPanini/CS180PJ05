import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditLoginGUI extends JComponent implements Runnable {
    private String username;
    private String password;

    JFrame loginFrame;
    JTextField usernameField;
    JTextField passwordField;
    JButton loginButton;

    public EditLoginGUI() {
        username = null;
        password = null;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // login GUI
            if (e.getSource() == loginButton) {
                username = usernameField.getText();
                password = passwordField.getText();
                loginFrame.dispose();
            }
        }
    };

    public void run() {
        loginFrame = new JFrame("Message Login");
        Container content = loginFrame.getContentPane();
        content.setLayout(new BorderLayout());
        loginFrame.setSize(400, 200);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
        loginFrame.setVisible(true);
    }
}