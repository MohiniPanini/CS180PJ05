import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

/**
 * ConversationsGUI
 *
 * Represents the gui that allows the user to view all conversations
 *
 * @author Luka Narisawa, McKenna O'Hara
 * @version April 19, 2021
 */

public class ConversationsGUI extends JComponent implements Runnable {
    // Fields
    private String action;
    private JFrame conversationsFrame;
    private JButton createButton;
    private JButton editAccountButton;


    // static variable of all currently logged in users conversations
    public static ArrayList<Conversation> usersConversations;

    private JFrame messagesFrame;

    public String getAction() {
        return action;
    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // if user wants to create message
            if (e.getSource() == createButton) {
                conversationsFrame.setVisible(false);
                action = "create";
            } else if (e.getSource() == editAccountButton) {
                conversationsFrame.setVisible(false);
                action = "edit";
            } // end if
        }
    }; // actionListener

    public void run() {
        // frame, container for when conversations is clicked
        conversationsFrame = new JFrame("Conversations");
        Container conversationsContent = conversationsFrame.getContentPane();
        conversationsContent.setLayout(new BorderLayout());
        conversationsFrame.setSize(600, 600);
        conversationsFrame.setLocationRelativeTo(null);
        conversationsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // create new message, delete account button
        JPanel panel1 = new JPanel();
        createButton = new JButton("Create new message");
        createButton.addActionListener(actionListener);
        panel1.add(createButton);
        editAccountButton = new JButton("Edit or delete Account");
        editAccountButton.addActionListener(actionListener);
        panel1.add(editAccountButton);
        JButton importConversationButton = new JButton("Import Conversation");
        panel1.add(importConversationButton);

        // conversations list
        JLabel title = new JLabel("Your Conversations");
        JPanel panel2 = new JPanel();
        panel2.add(title);


        // Display conversations
        JPanel scrollPanel = new JPanel();
        scrollPanel.setLayout(new BoxLayout(scrollPanel, BoxLayout.PAGE_AXIS));

        if (usersConversations != null) {
            // Array for select buttons
            JButton[] selectButtons = new JButton[usersConversations.size()];
            int count = 0;
            int numberOfConversationsDisplayed = 0;
            for (Conversation conversation : usersConversations) {
                // each JLabel
                ArrayList<User> users = conversation.getConvoUsers();

                String filename = "";
                for (User user : users) {
                    filename = filename + user.getID() + "|";
                }

                filename = filename.substring(0, filename.length() - 1) + ".txt";

                // Create string of all users usernames with space in between
                StringBuilder usersString = new StringBuilder();
                for (int i = 1; i < users.size(); i++) {
                    usersString.append(users.get(i).getUsername()).append(" ");
                }

                /*StringBuilder filename = new StringBuilder();
                for (int i = 1; i < users.size(); i++) {
                    filename.append(users.get(i).getID()).append("|");
                }
                int length = filename.length();
                filename.delete(length - 1, length);
                filename.append(".txt"); */

                JLabel conversationsLabel = null;

                // Read through conversations and don't display any conversations that have been deleted
                try (BufferedReader bfr = new BufferedReader(new FileReader("Hiddenconvos|" + MessageClient.getUser().getID() + ".txt"))) {
                    String hiddenConversationsLine = bfr.readLine();
                    if (!filename.equals(hiddenConversationsLine)) {
                        System.out.println(filename);
                        // Add label and button
                        ++numberOfConversationsDisplayed;
                        System.out.println(numberOfConversationsDisplayed);
                        conversationsLabel = new JLabel(String.valueOf(usersString));
                        selectButtons[count] = new JButton("Select ");
                    }
                } catch (IOException ie) {
                    ie.printStackTrace();
                }

                // each button
                int finalCount = count;
                JLabel finalConversationsLabel = conversationsLabel;
                selectButtons[count].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        // if select is clicked open associated messages
                        if (e.getSource() == selectButtons[finalCount]) {

                            // Get messages for conversation button that was clicked
                            ArrayList<Message> conversationMessages = conversation.getMessages();

                            // Create new frame for messages
                            messagesFrame = new JFrame();
                            Container messageContent = messagesFrame.getContentPane();
                            messageContent.setLayout(new BorderLayout());
                            messagesFrame.setSize(400, 600);
                            messagesFrame.setLocationRelativeTo(null);
                            messagesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                            // scroll panel for messages
                            JPanel messagesScrollPanel = new JPanel();
                            messagesScrollPanel.setLayout(new BoxLayout(messagesScrollPanel, BoxLayout.PAGE_AXIS));


                            if (conversationMessages != null) {
                                for (Message message : conversationMessages) {


                                    // each button to confirm update to message
                                    JLabel username = new JLabel(message.getUser().getUsername());
                                    JButton messageButton = new JButton(message.getMessage());
                                    JLabel time = new JLabel(message.getTime().toString());

                                    // if user clicks the button then the associated message is updated
                                    messageButton.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            JFrame editMessagesFrame = new JFrame();
                                            Container messageContent = editMessagesFrame.getContentPane();
                                            messageContent.setLayout(new BorderLayout());
                                            editMessagesFrame.setSize(400, 200);
                                            editMessagesFrame.setLocationRelativeTo(null);
                                            editMessagesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                            // text field for each message so they can edit
                                            JTextField messageTextField = new JTextField(10);
                                            messageTextField.setText(message.getMessage());
                                            // update message in that conversation messages
                                            message.setMessage(messageTextField.getText());
                                            JButton messageButton = new JButton("Submit Edit");

                                            // if user clicks the button then the associated message is updated
                                            messageButton.addActionListener(new ActionListener() {
                                                @Override
                                                public void actionPerformed(ActionEvent e) {

                                                    // update message in that conversation messages
                                                    message.setMessage(messageTextField.getText());
                                                }
                                            });
                                        }
                                    });

                                    JPanel textAndButtonPanel = new JPanel();
                                    textAndButtonPanel.add(username);
                                    textAndButtonPanel.add(messageButton);
                                    textAndButtonPanel.add(time);
                                    messagesScrollPanel.add(textAndButtonPanel);

                                }
                            }

                            // make frame visible
                            JScrollPane messagesJSP = new JScrollPane(messagesScrollPanel,
                                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                            JLabel messagesTitle = new JLabel(usersString + "Messages");
                            JButton deleteConversationButton = new JButton("Delete Conversation");
                            // make deleteConversationButton usable

                            deleteConversationButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {

                                    // Add conversation to hidden conversations file
                                    ConversationsGUI.writeToHiddenConversationFile(conversation);
                                    finalConversationsLabel.setVisible(false);
                                    selectButtons[finalCount].setVisible(false);
                                    messagesFrame.setVisible(false);
                                    JOptionPane.showMessageDialog(null, "Conversation Deleted",
                                            "Your conversation has been deleted", JOptionPane.INFORMATION_MESSAGE);

                                }
                            });
                            JButton exportConversationButton = new JButton("Export Conversation");
                            JPanel topPanel = new JPanel();
                            topPanel.add(messagesTitle);
                            topPanel.add(deleteConversationButton);
                            topPanel.add(exportConversationButton);
                            messageContent.add(topPanel, BorderLayout.NORTH);
                            messageContent.add(messagesJSP, BorderLayout.CENTER);
                            messagesFrame.setVisible(true);
                        }
                    }
                });

                // Create inner panel and add to scrollable panel
                JPanel labelAndButtonPanel = new JPanel();
                labelAndButtonPanel.add(conversationsLabel);
                labelAndButtonPanel.add(selectButtons[count]);
                scrollPanel.add(labelAndButtonPanel);
                count++;

            }
        } else {
            System.out.println("no");
            JLabel label = new JLabel("No conversations");
            scrollPanel.add(label);
        }
        JScrollPane jsp = new JScrollPane(scrollPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        conversationsContent.add(panel1, BorderLayout.NORTH);
        conversationsContent.add(panel2, BorderLayout.CENTER);
        conversationsContent.add(jsp, BorderLayout.CENTER);
        conversationsFrame.setVisible(true);
    } // run

    // return the conversations of the currently logged in user
    public ArrayList<Conversation> usersConversations() {
        // for each conversation in application
        ArrayList<Conversation> userConversationsCopy = new ArrayList<>();

        usersConversations = new ArrayList<>();
        // Get all conversations in file into array
        Conversation.readAllConversations();
        // Determine which of the conversations are associated with currently logged in user
        for (Conversation conversation : Conversation.conversations) {


            for (User loggedIN : conversation.getConvoUsers()) {
                if (loggedIN.getUsername().equals(LoginGUI.username)) {
                    usersConversations.add(conversation);
                }
            }
        }

        return usersConversations;

    }

    // conversation that is to be deleted as parameter
    public static void writeToHiddenConversationFile(Conversation conversation) {

        // Create hidden conversation file // format of Hiddenconvos|id
        String filename = "Hiddenconvos|" + MessageClient.getUser().getID() + ".txt";
        conversation.writeToFile(filename);
    }

}
