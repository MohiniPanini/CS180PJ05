import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * ConversationsGUI
 *
 * Represents the gui that allows the user to view all conversations
 *
 * @author
 * @version April 19, 2021
 */

public class ConversationsGUI extends JComponent implements Runnable {
    // Fields
    private String action;
    private EditAccountGUI editAccountGUI;
    private JFrame conversationsFrame;
    private JButton createButton;
    private JButton editAccountButton;


    // static varibale of all currently logged in users conversations
    public static ArrayList<Conversation> usersConversations;

    private JFrame messagesFrame;

    public String getAction() {
        return action;
    }

    public EditAccountGUI getEditAccountGUI() {
        return editAccountGUI;
    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // if user wants to create message
            if (e.getSource() == createButton) {
                conversationsFrame.setVisible(false);
                CreateGUI create = new CreateGUI();
                SwingUtilities.invokeLater(create);
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
        conversationsFrame.setSize(400, 600);
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

        // conversations list
        JLabel title = new JLabel("Your Conversations");
        JPanel panel2 = new JPanel();
        panel2.add(title);

        // Display conversations
        JPanel scrollPanel = new JPanel();
        JScrollPane jsp = new JScrollPane(scrollPanel);
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);


        if (usersConversations != null) {
            for (Conversation conversation : usersConversations) {

                // each JLabel
                ArrayList<User> users = conversation.getConvoUsers();
                String usersString = users.toString();
                JLabel conversationsLabel = new JLabel(usersString);

                // each button
                JButton conversationButton = new JButton("Select");
                conversationButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        // if select is clicked open associated messages
                        if (e.getSource() == conversationButton) {

                        }

                    }
                });

                // Create inner panel and add to scrollable panel
                JPanel labelAndButtonPanel = new JPanel();
                labelAndButtonPanel.add(conversationsLabel);
                labelAndButtonPanel.add(conversationButton);
                jsp.add(labelAndButtonPanel);
            }
        }


        conversationsContent.add(panel1, BorderLayout.NORTH);
        conversationsContent.add(panel2, BorderLayout.CENTER);
        conversationsContent.add(jsp, BorderLayout.SOUTH);
        conversationsFrame.setVisible(true);
    } // run

    // return the conversations of the currently logged in user
    public static ArrayList<Conversation> usersConversations() {
        // for each conversation in application
        ArrayList<Conversation> userConversations = new ArrayList<>();


        // Get all conversations in file into array
        Conversation.readAllConversations();

        // Determine which of the conversations are associated with currently logged in user
        for (Conversation conversation : Conversation.conversations) {

            // for each user in the conversation
            for (User loggedIN : conversation.getConvoUsers()) {
                System.out.println(loggedIN);
                System.out.println(conversation);

                if (loggedIN.getUsername().equals(LoginGUI.username)) {
                    System.out.println("test");
                    System.out.println(conversation);
                    userConversations.add(conversation);
                }
            }
        }

        System.out.println(Conversation.conversations.toString());
        System.out.println(userConversations.toString());

        return userConversations;

    }

}