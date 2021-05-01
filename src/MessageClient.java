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
 * @author Luka Narisawa,
 * @version April 17, 2021
 */
public class MessageClient {
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
                while (loginGUI.getAction() == null) {
                    Thread.onSpinWait();
                } // end while
                out.write(loginGUI.getAction());
                out.println();
                out.flush();
                // login option
                if (loginGUI.getAction().equals("login")) {
                    String usernamePassword = String.format("%s|%s", loginGUI.getUsername(), loginGUI.getPassword());
                    out.write(usernamePassword);
                    out.println();
                    out.flush();
                    String loggedin = in.readLine();
		    while (loggedin == null) {
			loggedin = in.readLine();
		    }
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
                else if (loginGUI.getAction().equals("create")) {
                    boolean created = false;
                    while(!created) {
                        String username = null;
                        String password = null;
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
            } // login complete

            boolean quit = false;
            // go back to conversationGUI until user closes app
            while (!quit) {
                // Display gui to give user conversations list
                ConversationsGUI conversationsGUI = new ConversationsGUI();
                User.getAllUsersFromFile();
                ConversationsGUI.usersConversations = conversationsGUI.usersConversations();
                SwingUtilities.invokeLater(conversationsGUI);
                while (conversationsGUI.getAction() == null) {
                    Thread.onSpinWait();
                } // end while
                out.write(conversationsGUI.getAction());
                out.println();
                out.flush();
                if (conversationsGUI.getAction().equals("create")) {
                    CreateGUI create = new CreateGUI();
                    SwingUtilities.invokeLater(create);
                    while (!create.isSendClicked()) {
                        Thread.onSpinWait();
                    } // end while
                    String userString = in.readLine();
                    int firstBar = userString.indexOf("|");
                    int secondBar = userString.lastIndexOf("|");
                    String username = userString.substring(0, firstBar);
                    String password = userString.substring(firstBar + 1, secondBar);
                    int id = Integer.parseInt(userString.substring(secondBar + 1));
                    User user = new User(username, password, id);
                    Conversation newConversation = create.createConversation(create.getSendToTextField().getText(),
                            create.getMessageTextField().getText(), user);
                    newConversation.writeToFile();
                }
                // edit or delete account
                else if (conversationsGUI.getAction().equals("edit")) {
                    EditAccountGUI editAccountGUI = new EditAccountGUI();
                    SwingUtilities.invokeLater(editAccountGUI);
                    while (editAccountGUI.getAction() == null) {
                        Thread.onSpinWait();
                    } // end while
                    out.write(editAccountGUI.getAction());
                    out.println();
                    out.flush();
                    if (editAccountGUI.getAction().equals("username")) {
                        while (editAccountGUI.getNewUsername() == null) {
                            Thread.onSpinWait();
                        } // end while
                        String newUsername = editAccountGUI.getNewUsername();
                        out.write(newUsername);
                    } else if (editAccountGUI.getAction().equals("password")) {
                        while (editAccountGUI.getNewPassword() == null) {
                            Thread.onSpinWait();
                        } // end while
                        String newPassword = editAccountGUI.getNewPassword();
                        out.write(newPassword);
                    } else {
                        while (editAccountGUI.getConfirm() == -1) {
                            Thread.onSpinWait();
                        } // end while
                        int confirm = editAccountGUI.getConfirm();
                        if (confirm == JOptionPane.YES_OPTION) {
                            out.write("yes");
                        } else if (confirm == JOptionPane.NO_OPTION) {
                            out.write("no");
                        }
                    } // end if
                    out.println();
                    out.flush();
                    // end edit or delete account
                }
                // viewing selected conversation
                else {
                    MessageGUI messageGUI = new MessageGUI();
                    SwingUtilities.invokeLater(messageGUI);
                }
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
