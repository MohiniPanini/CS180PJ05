import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 * MessageClient
 *
 * Client for the Messaging app.
 *
 * @author
 * @version April 17, 2021
 */
public class MessageClient {
    private static String username;
    private static String password;
    private static String action;

    //login GUI fields
    static JFrame loginFrame;
    static JTextField usernameField;
    static JTextField passwordField;
    static JButton loginButton;
    static JButton createAccountButton;
    static JButton editAccountButton;


    public static void main(String[] args) {
        //connect to server
        try (Socket socket = new Socket("localhost", 1234)) {

            //writing to server
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            //reading from server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //Log in GUI
            //login with username and password, or select "Create account" or "Edit or delete account"
            boolean loggedIn = false;
            while(!loggedIn) {
                LoginGUI loginGUI = new LoginGUI();
                SwingUtilities.invokeLater(loginGUI);
                while (action == null) {
                    Thread.onSpinWait();
                } //end while
                System.out.println(action);
                out.write(action);
                out.println();
                System.out.println(username);
                out.flush();
                //login option
                if (action.equals("login")) {
                    String usernamePassword = String.format("%s, %s", username, password);
                    out.write(usernamePassword);
                    out.println();
                    out.flush();
                    String loggedin = in.readLine();
                    if (!loggedin.equals("loggedIn")) {
                        JOptionPane.showMessageDialog(null, loggedin,
                                "Message Login", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Login successful",
                                "Message Login", JOptionPane.INFORMATION_MESSAGE);
                        loggedIn = true;
                    } //end if
                }
                //create account option
                else if (action.equals("create")) {
                    boolean created = false;
                    while(!created) {
                        username = null;
                        password = null;
                        while (username == null || username.equals("")) {
                            username = JOptionPane.showInputDialog(null, "Enter username",
                                    "Create account", JOptionPane.QUESTION_MESSAGE);
                        } //end while
                        out.write(username);
                        out.println();
                        out.flush();
                        String alreadyExist = in.readLine();
                        System.out.println(alreadyExist);
                        if (alreadyExist.equals("created")) {
                            while (password == null || password.equals("")) {
                                password = JOptionPane.showInputDialog(null, "Enter password",
                                        "Create account", JOptionPane.QUESTION_MESSAGE);
                            } //end while
                            String usernamePassword = String.format("%s, %s", username, password);
                            out.write(usernamePassword);
                            out.println();
                            out.flush();
                            created = true;
                            JOptionPane.showMessageDialog(null, "Account created successfully",
                                    "Create account", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, alreadyExist,
                                    "Create account", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    loggedIn = true;
                }
            } //login complete
            
            // Display gui to give user messaging choices
            MessageGUI messageGUI = new MessageGUI();
            SwingUtilities.invokeLater(messageGUI);

            //
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class LoginGUI extends JComponent implements Runnable {
        public LoginGUI() {
            action = null;
            username = null;
            password = null;
        }
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //login GUI
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
                if (e.getSource() == editAccountButton) {
                    action = "edit";
                    loginFrame.dispose();
                }

                //
            }
        };

        public void run() {
            loginFrame = new JFrame("Message Login");
            Container content = loginFrame.getContentPane();
            content.setLayout(new BorderLayout());
            loginFrame.setSize(400, 200);
            loginFrame.setLocationRelativeTo(null);
            loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            //username, password, login button on the CENTER
            JLabel labelU = new JLabel("Username");
            JLabel labelP = new JLabel("Password");
            usernameField = new JTextField(10); //text field for username
            usernameField.addActionListener(actionListener);
            passwordField = new JTextField(10); //text field for password
            passwordField.addActionListener(actionListener);
            loginButton = new JButton("Login"); //login button
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
            JLabel labelOr = new JLabel("   Or");
            panel3.add(labelOr);
            content.add(panel3, BorderLayout.CENTER);
            // create account and edit or delete button on SOUTH
            createAccountButton = new JButton("Create account");
            createAccountButton.addActionListener(actionListener);
            editAccountButton = new JButton("Edit or delete account");
            editAccountButton.addActionListener(actionListener);
            JPanel panel4 = new JPanel();
            panel4.add(createAccountButton);
            panel4.add(editAccountButton);
            content.add(panel4, BorderLayout.SOUTH);
            loginFrame.setVisible(true);
        }
    }
}
