import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * ConversationsGUI
 * <p>
 * Represents the gui that allows the user to view all conversations
 *
 * @author Luka Narisawa, McKenna O'Hara
 * @version April 19, 2021
 */

public class ConversationsGUI extends JComponent implements Runnable {
    // static variable of all currently logged in users conversations
    public ArrayList<Conversation> usersConversations;
    private Conversation selected;
    // Fields
    private String action;
    WindowListener windowListener = new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent evt) {
            action = "closed";
        }
    };
    private JFrame conversationsFrame;
    private JButton editAccountButton;
    private JButton logoutButton;
    private JButton createButton;
    private JButton importConversationButton;
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
            } else if (e.getSource() == importConversationButton) {
                conversationsFrame.setVisible(false);
                action = "import";
            } else if (e.getSource() == logoutButton) {
                conversationsFrame.setVisible(false);
                action = "logout";
            } // end if
        }
    }; // actionListener

    // conversation that is to be deleted as parameter
    public static void writeToHiddenConversationFile(Conversation conversation) {

        // Create hidden conversation file // format of Hiddenconvos|id
        String filename = "Hiddenconvos|" + MessageClient.getUser().getID() + ".txt";
        conversation.writeToFile(filename);
    }

    public String getAction() {
        return action;
    }

    public Conversation getSelected() {
        return selected;
    }

    public void run() {
        // frame, container for when conversations is clicked
        conversationsFrame = new JFrame("Conversations");
        Container conversationsContent = conversationsFrame.getContentPane();
        conversationsContent.setLayout(new BorderLayout());
        conversationsFrame.setSize(400, 600);
        conversationsFrame.setLocationRelativeTo(null);
        conversationsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        conversationsFrame.addWindowListener(windowListener);

        // create new message, delete account button
        JPanel panel1 = new JPanel();
        panel1.setPreferredSize(new Dimension(400, 30));
        editAccountButton = new JButton("Edit or delete Account");
        editAccountButton.addActionListener(actionListener);
        panel1.add(editAccountButton);
        logoutButton = new JButton("Logout");
        logoutButton.addActionListener(actionListener);
        panel1.add(logoutButton);
        JPanel panel2 = new JPanel();
        panel2.setPreferredSize(new Dimension(400, 30));
        createButton = new JButton("Create new message");
        createButton.addActionListener(actionListener);
        panel2.add(createButton);
        importConversationButton = new JButton("Import Conversation");
        importConversationButton.addActionListener(actionListener);
        panel2.add(importConversationButton);

        // Display conversations list
        JPanel scrollPanel = new JPanel();
        scrollPanel.setLayout(new BoxLayout(scrollPanel, BoxLayout.PAGE_AXIS));

        if (usersConversations.size() != 0) {
            JLabel title = new JLabel("Your Conversations");
            title.setFont(new Font("Verdana", Font.PLAIN, 18));
            title.setBackground(Color.GRAY);
            title.setOpaque(true);
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER)) {
                @Override
                public Dimension getMaximumSize() {
                    return getPreferredSize();
                }
            };
            panel.setPreferredSize(new Dimension(400, 40));
            panel.add(title);
            scrollPanel.add(panel);

            // Array for select buttons
            JButton[] selectButtons = new JButton[usersConversations.size()];
            int count = 0;
            // each Conversation
            for (Conversation conversation : usersConversations) {

                JLabel conversationsLabel = null;

                // Read through conversations and don't display any conversations that have been deleted
                try (BufferedReader bfr = new BufferedReader(new FileReader("Hiddenconvos|" +
                        MessageClient.getUser().getID() + ".txt"))) {
                    boolean deleted = false;
                    String hiddenConversationsLine = bfr.readLine();
                    while (hiddenConversationsLine != null) {
                        if (conversation.getFilename().equals(hiddenConversationsLine)) {
                            deleted = true;
                        }
                        hiddenConversationsLine = bfr.readLine();
                    }
                    if (!deleted) {
                        // Create string of all users usernames with space in between
                        StringBuilder usersString = new StringBuilder();
                        for (User user : conversation.getConvoUsers()) {
                            if (MessageClient.getUser().getID() != user.getID()) {
                                usersString.append(user.getUsername()).append(" ");
                            } // end if
                        } // end for
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
                                conversationsFrame.setVisible(false);
                            }
                        }
                    });

                    // Create inner panel and add to scrollable panel
                    JPanel labelAndButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)) {
                        @Override
                        public Dimension getMaximumSize() {
                            return getPreferredSize();
                        }
                    };
                    labelAndButtonPanel.setPreferredSize(new Dimension(400, 40));
                    labelAndButtonPanel.add(selectButtons[count]);
                    labelAndButtonPanel.add(conversationsLabel);
                    scrollPanel.add(labelAndButtonPanel);
                }
                count++;
            } // end for
        } else {
            JLabel title = new JLabel("No Conversations");
            title.setFont(new Font("Verdana", Font.PLAIN, 18));
            title.setBackground(Color.GRAY);
            title.setOpaque(true);
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER)) {
                @Override
                public Dimension getMaximumSize() {
                    return getPreferredSize();
                }
            };
            panel.setPreferredSize(new Dimension(400, 40));
            panel.add(title);
            scrollPanel.add(panel);
        }
        JScrollPane jsp = new JScrollPane(scrollPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JPanel topPanel = new JPanel();
        topPanel.add(panel1);
        topPanel.add(panel2);
        topPanel.setPreferredSize(new Dimension(400, 80));
        conversationsContent.add(topPanel, BorderLayout.NORTH);
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
                if (loggedIN.getID() == MessageClient.getUser().getID()) {
                    usersConversations.add(conversation);
                }
            }
        }
        return usersConversations;
    }

}
