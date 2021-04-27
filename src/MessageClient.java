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

    // login GUI fields
    static JFrame loginFrame;
    static JTextField usernameField;
    static JTextField passwordField;
    static JButton loginButton;
    static JButton createAccountButton;
    static JButton editAccountButton;

    public static void main(String[] args) {
        // connect to server
        try (Socket socket = new Socket("localhost", 1234)) {

            // writing to server
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            // reading from server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Log in GUI
            // login with username and password, or select "Create account" or "Edit or delete account"
            boolean loggedIn = false;
            while(!loggedIn) {
                LoginGUI loginGUI = new LoginGUI();
                SwingUtilities.invokeLater(loginGUI);
                while (action == null) {
                    Thread.onSpinWait();
                } // end while
                out.write(action);
                out.println();
                out.flush();
                // login option
                if (action.equals("login")) {
                    String usernamePassword = String.format("%s|%s", username, password);
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
                    } // end if
                } // end login option

                // create account option
                else if (action.equals("create")) {
                    boolean created = false;
                    while(!created) {
                        username = null;
                        password = null;
                        while (created == false && (username == null || username.equals(""))) {
                            username = JOptionPane.showInputDialog(null, "Enter username",
                                    "Create account", JOptionPane.QUESTION_MESSAGE);
                            if (username == null) {
                                created = true;
                            } // end if
                        } // end while
                        if (!created) {
                            out.write(username);
                            out.println();
                            out.flush();
                            String alreadyExist = in.readLine();
                            String invalid = null;
                            if (alreadyExist.equals("validUsername")) {
                                while (!created && (password == null || password.equals("") ||
                                        invalid.equals("invalid"))) {
                                    password = JOptionPane.showInputDialog(null,
                                            "Enter password", "Create account",
                                            JOptionPane.QUESTION_MESSAGE);
                                    if (password == null) {
                                        created = true;
                                    } else {
                                        out.write(password);
                                        out.println();
                                        out.flush();
                                    }
                                    invalid = in.readLine();
                                    if (invalid.equals("invalid")) {
                                        JOptionPane.showMessageDialog(null,
                                                "Password has to be at least 8 characters including " +
                                                        "Lowercase, Uppercase, and number", "Create account",
                                                JOptionPane.ERROR_MESSAGE);
                                    } // end if
                                } // end while
                                created = true;
                                loggedIn = true;
                                JOptionPane.showMessageDialog(null,
                                        "Account created successfully", "Create account",
                                        JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, alreadyExist,
                                        "Create account", JOptionPane.ERROR_MESSAGE);
                            } // end if
                        } // end if
                    } // end while
                } // create account complete

                // edit or delete account option
                else if (action.equals("edit")) {
                    username = null;
                    password = null;
                    boolean noInput = false;
                    while (!loggedIn) {
                        EditLoginGUI editLoginGUI = new EditLoginGUI();
                        SwingUtilities.invokeLater(editLoginGUI);
                        while (editLoginGUI.getPassword() == null) {
                            Thread.onSpinWait();
                        } // end while
                        username = editLoginGUI.getUsername();
                        password = editLoginGUI.getPassword();
                        if (username != null && password != null) {
                            String usernamePassword = String.format("%s|%s", username, password);
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
                            } // end if
                        } // end if
                    } // end while
                    EditAccountGUI editAccountGUI = new EditAccountGUI();
                    SwingUtilities.invokeLater(editAccountGUI);
                    while (editAccountGUI.getAction() == null) {
                        Thread.onSpinWait();
                    } // end while
                    String action = editAccountGUI.getAction();
                    out.write(action);
                    out.println();
                    out.flush();
                    if (action.equals("username")) {
                        String newUsername = JOptionPane.showInputDialog(null, "Enter new username",
                                "Edit account", JOptionPane.QUESTION_MESSAGE);
                        out.write(newUsername);
                    } else if (action.equals("password")) {
                        String newPassword = JOptionPane.showInputDialog(null, "Enter new password",
                                "Edit account", JOptionPane.QUESTION_MESSAGE);
                        out.write(newPassword);
                    } else {
                        int confirm = JOptionPane.showConfirmDialog(null, "Enter new password",
                                "Edit account", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            out.write("yes");
                        } else if (confirm == JOptionPane.NO_OPTION) {
                            out.write("no");
                        }
                    } // end if
                    out.println();
                    out.flush();
                } // end edit or delete account
            } // login complete

            // Display gui to give user messaging choices
            MessageGUI messageGUI = new MessageGUI();
            SwingUtilities.invokeLater(messageGUI);
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
            JLabel labelOr = new JLabel("Or");
            panel3.add(labelOr);
            content.add(panel3, BorderLayout.CENTER);
            //  create account and edit or delete button on SOUTH
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

    public class ServerThread implements Runnable {
        private Socket socket;
        private String name;
        private BufferedReader serverIn;
        private BufferedReader userIn;
        private PrintWriter out;


        Thread server = new Thread(new ServerThread(socket, name));

        public void init() {
            try {
                Socket socket = new Socket("localhost", 1234);
                server.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public ServerThread(Socket socket, String name) {
            this.socket = socket;
            this.name = name;
        }

        public void run() {
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                userIn = new BufferedReader(new InputStreamReader(System.in));

                while (!socket.isClosed()) {
                    if (serverIn.ready()) {
                        String input = serverIn.readLine();
                        if (input != null) {
                            System.out.println(input);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
