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
 * @version May 3, 2021
 */
public class MessageClient {
    private static User user = null;

    public static User getUser() {
        return user;
    }

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
            while(true) {
                while (!loggedIn) {
                    LoginGUI loginGUI = new LoginGUI();
                    SwingUtilities.invokeLater(loginGUI);
                    while (loginGUI.getAction() == null) {
                        Thread.onSpinWait();
                    } // end while
                    out.write(loginGUI.getAction());
                    out.println();
                    out.flush();
                    // login options
                    switch (loginGUI.getAction()) {
                        case ("login"): {
                            String usernamePassword = String.format("%s|%s", loginGUI.getUsername(), loginGUI.getPassword());
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
                                user = User.fromString(in.readLine());
                                loggedIn = true;
                            } // end if
                            break;
                        }

                        // create account option
                        case ("create"): {
                            boolean created = false;
                            while (!created) {
                                String password = null;
                                String username = JOptionPane.showInputDialog(null, "Enter username",
                                        "Create account", JOptionPane.QUESTION_MESSAGE);
                                if (username == null) {
                                    out.write("Go back to login");
                                    out.println();
                                    out.flush();
                                    break;
                                } // end if
                                out.write(username);
                                out.println();
                                out.flush();
                                String alreadyExist = in.readLine();
                                String invalid = null;
                                if (alreadyExist.equals("validUsername")) {
                                    while (password == null || password.equals("") ||
                                            invalid.equals("invalid")) {
                                        password = JOptionPane.showInputDialog(null,
                                                "Enter password", "Create account",
                                                JOptionPane.QUESTION_MESSAGE);
                                        if (password == null) {
                                            created = true;
                                            out.write("Go back to login");
                                            out.println();
                                            out.flush();
                                            break;
                                        } else {
                                            out.write(password);
                                            out.println();
                                            out.flush();
                                        }
                                        invalid = in.readLine();
                                        if (invalid.equals("invalid char")) {
                                            JOptionPane.showMessageDialog(null,
                                                    "Do not use space ( ), comma (,), or vertical bar (|)", "Create account",
                                                    JOptionPane.ERROR_MESSAGE);
                                        }
                                        if (invalid.equals("invalid")) {
                                            JOptionPane.showMessageDialog(null,
                                                    "Password has to be at least 8 characters including " +
                                                            "Lowercase, Uppercase, and number", "Create account",
                                                    JOptionPane.ERROR_MESSAGE);
                                        } else {
                                            user = User.fromString(in.readLine());
                                            created = true;
                                            loggedIn = true;
                                            // Give each user a hidden conversations file
                                            new FileOutputStream("Hiddenconvos|" + user.getID() + ".txt");
                                            JOptionPane.showMessageDialog(null,
                                                    "Account created successfully", "Create account",
                                                    JOptionPane.INFORMATION_MESSAGE);
                                        } // end if
                                    } // end while
                                } else if (alreadyExist.equals("Invalid username")) {
                                    JOptionPane.showMessageDialog(null,
                                            "Do not use space ( ), comma (,), or vertical bar (|)", "Create account",
                                            JOptionPane.ERROR_MESSAGE);
                                } else {
                                    JOptionPane.showMessageDialog(null, alreadyExist,
                                            "Create account", JOptionPane.ERROR_MESSAGE);
                                } // end if
                            } // end while
                            break;
                        }
                        case ("closed"): {
                            return;
                        }
                    } // end switch
                } // login complete

                // go back to conversationGUI until user closes app
                while (true) {
                    // Display gui to give user conversations list
                    ConversationsGUI conversationsGUI = new ConversationsGUI();
                    User.getAllUsersFromFile();
                    conversationsGUI.usersConversations = conversationsGUI.usersConversations();
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
                        while (create.getSendClicked() == null) {
                            Thread.onSpinWait();
                        } // end while
                        if (create.getSendClicked().equals("true")) {
                            Conversation newConversation = create.createConversation( create.getSelected(),
                                    create.getMessageTextField().getText(), user);
                            if (newConversation != null) {
                                newConversation.writeToFile("Conversations.txt");
                            }
                        }
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
                            boolean changed = false;
                            while(!changed) {
                                String username = JOptionPane.showInputDialog(null, "Enter username",
                                        "Create account", JOptionPane.QUESTION_MESSAGE);
                                if (username == null) {
                                    out.write("Go back to ConversationGUI");
                                    out.println();
                                    out.flush();
                                    break;
                                } // end if
                                out.write(username);
                                out.println();
                                out.flush();
                                String alreadyExist = in.readLine();
                                String invalid = null;
                                if (alreadyExist.equals("validUsername")) {
                                    changed = true;
                                    JOptionPane.showMessageDialog(null, "Username changed",
                                            "Change Username", JOptionPane.INFORMATION_MESSAGE);
                                } else if (alreadyExist.equals("Invalid username")) {
                                    JOptionPane.showMessageDialog(null,
                                            "Do not use space ( ), comma (,), or vertical bar (|)",
                                            "Change Username", JOptionPane.ERROR_MESSAGE);
                                } else {
                                    JOptionPane.showMessageDialog(null, alreadyExist,
                                            "Change Username", JOptionPane.ERROR_MESSAGE);
                                } // end if
                            } // end while
                        } else if (editAccountGUI.getAction().equals("password")) {
                            boolean changed = false;
                            while (!changed) {
                                String newPassword = null;
                                newPassword = JOptionPane.showInputDialog(null, "Enter new password",
                                        "Change Password", JOptionPane.QUESTION_MESSAGE);
                                if (newPassword == null) {
                                    out.write("Go back to ConversationGUI");
                                    out.println();
                                    out.flush();
                                    break;
                                } else {
                                    out.write(newPassword);
                                    out.println();
                                    out.flush();
                                }
                                String invalid = in.readLine();

                                if (invalid.equals("invalid char")) {
                                    JOptionPane.showMessageDialog(null,
                                            "Do not use space ( ), comma (,), or vertical bar (|)",
                                            "Change Password", JOptionPane.ERROR_MESSAGE);
                                }
                                if (invalid.equals("invalid")) {
                                    JOptionPane.showMessageDialog(null,
                                            "Password has to be at least 8 characters including " +
                                                    "Lowercase, Uppercase, and number", "Change Password",
                                            JOptionPane.ERROR_MESSAGE);
                                } else {
                                    changed = true;
                                    JOptionPane.showMessageDialog(null, "Password changed",
                                            "Change Password", JOptionPane.INFORMATION_MESSAGE);
                                } // end if
                            } // end while
                        } else if (editAccountGUI.getAction().equals("delete")) {
                            int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete account?",
                                    "Delete account", JOptionPane.YES_NO_OPTION);
                            if (confirm == JOptionPane.YES_OPTION) {
                                out.write("yes");
                                out.println();
                                out.flush();
                                for (Conversation conversation : conversationsGUI.usersConversations) {
                                    ConversationsGUI.writeToHiddenConversationFile(conversation);
                                }
                                loggedIn = false;
                                break;
                            } else {
                                out.write("no");
                                out.println();
                                out.flush();
                            }
                        } else if (editAccountGUI.getAction().equals("import")) {
                            // import conversation process here!!









                        }// end if
                        // end edit or delete account
                    } else if (conversationsGUI.getAction().equals("logout")) {
                        loggedIn = false;
                        break;
                    } else if (conversationsGUI.getAction().equals("closed")) {
                        return;
                    }
                    // viewing selected conversation
                    else if (conversationsGUI.getAction().equals("view")) {
                        while (true) {
                            MessageGUI messageGUI = new MessageGUI(conversationsGUI.getSelected());
                            SwingUtilities.invokeLater(messageGUI);
                            while (messageGUI.getAction() == null) {
                                Thread.onSpinWait();
                            }
                            if (messageGUI.getAction().equals("export")) {
                                System.out.println("export");
                            } else if (messageGUI.getAction().equals("delete") || messageGUI.getAction().equals("closed")) {
                                System.out.println("closed");
                                break;
                            }
                        }
                    }
                }
            } // end while
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
