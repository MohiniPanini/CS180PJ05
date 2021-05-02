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
    private Conversation selected;
    // Fields
    private String action;
    private JFrame conversationsFrame;
    private JButton createButton;
    private JButton editAccountButton;

    // static variable of all currently logged in users conversations
    public ArrayList<Conversation> usersConversations;

    public String getAction() {
        return action;
    }

    public Conversation getSelected() {
        return selected;
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
            for (Conversation conversation : usersConversations) {
                // each JLabel
                ArrayList<User> users = conversation.getConvoUsers();;

                // Create string of all users usernames with space in between
                StringBuilder usersString = new StringBuilder();
                for (int i = 1; i < users.size(); i++) {
                    usersString.append(users.get(i).getUsername()).append(" ");
                }

                JLabel conversationsLabel = null;

                // Read through conversations and don't display any conversations that have been deleted
                try (BufferedReader bfr = new BufferedReader(new FileReader("Hiddenconvos|" + MessageClient.getUser().getID() + ".txt"))) {
                    boolean deleted = false;
                    String hiddenConversationsLine = bfr.readLine();
                    while (hiddenConversationsLine != null) {
                        if (conversation.getFilename().equals(hiddenConversationsLine)) {
                            deleted = true;
                        }
                        hiddenConversationsLine = bfr.readLine();
                    }
                    if (!deleted) {
                        // Add label and button
                        conversationsLabel = new JLabel(String.valueOf(usersString));
                        selectButtons[count] = new JButton("Select");
                    }
                } catch (IOException ie) {
                    ie.printStackTrace();
                }

                // each button
                if (selectButtons[count] != null) {
                    int finalCount = count;
                    selectButtons[count].addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                            // if select is clicked open associated messages
                            if (e.getSource() == selectButtons[finalCount]) {
                                // Get messages for conversation button that was clicked
                                selected = conversation;
                                action = "view";
                            }
                        }
                    });

                    // Create inner panel and add to scrollable panel
                    JPanel labelAndButtonPanel = new JPanel();
                    labelAndButtonPanel.add(conversationsLabel);
                    labelAndButtonPanel.add(selectButtons[count]);
                    scrollPanel.add(labelAndButtonPanel);
                }
                count++;
            }
        } else {
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

        // Create hidden conversation file // format of Hiddenconvos|username
        String filename = "Hiddenconvos|" + MessageClient.getUser().getID() + ".txt";
        conversation.writeToFile(filename);
    }

}
