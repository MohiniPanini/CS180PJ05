import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * src.createGUI
 * <p>
 * Represents the gui that allows the user to create new messages
 *
 * @author McKenna O'Hara, Luka Narisawa
 * @version May 3, 2021
 */

public class CreateGUI extends JComponent implements Runnable {

    private static JButton sendButton;
    // Fields
    private JFrame createFrame;
    private JList<String> sendToUsers;
    private JTextField messageTextField;
    private String sendClicked;
    // ActionListener
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == sendButton) {
                sendClicked = "true";
                createFrame.setVisible(false);
            }
        }
    };
    WindowListener windowListener = new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent evt) {
            sendClicked = "false";
        }
    };
    private String[] usernames;
    private StringBuilder selected;

    public JTextField getMessageTextField() {
        return messageTextField;
    }

    public String getSendClicked() {
        return sendClicked;
    }

    public String getSelected() {
        return String.valueOf(selected);
    }

    public void run() {

        // frame, container for when create is clicked
        createFrame = new JFrame("New Message");
        createFrame.addWindowListener(windowListener);
        Container createContent = createFrame.getContentPane();
        createContent.setLayout(new BorderLayout());
        createFrame.setSize(600, 400); // size
        createFrame.setLocationRelativeTo(null);
        createFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Labels and fields to add to inner panel
        JLabel sendToLabel = new JLabel("Send to: ");
        JLabel messageLabel = new JLabel("Message: ");
        messageTextField = new JTextField(10);
        usernames = new String[User.users.size() - 1];
        int i = 0;
        for (User user : User.users) {
            if (user.getID() != MessageClient.getUser().getID()) {
                usernames[i] = user.getUsername();
                i++;
            }
        }
        JList<String> selectUsers = new JList<String>(usernames);
        selectUsers.setFixedCellHeight(15);
        selectUsers.setFixedCellWidth(100);
        selectUsers.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        selectUsers.setVisibleRowCount(8);

        JButton addButton = new JButton("Add>>>");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] indices = selectUsers.getSelectedIndices();
                ArrayList<String> selectedList = new ArrayList<String>();
                selected = new StringBuilder();
                for (int i : indices) {
                    selectedList.add(usernames[i]);
                    selected.append(usernames[i] + ",");
                }
                String[] selectedArray = Arrays.copyOf(selectedList.toArray(), selectedList.toArray().length, String[].class);
                sendToUsers.setListData(selectedArray);
            }
        });

        sendToUsers = new JList<String>();
        sendToUsers.setFixedCellHeight(15);
        sendToUsers.setFixedCellWidth(100);
        selectUsers.setVisibleRowCount(8);
        selectUsers.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        add(new JScrollPane(sendToUsers));
        messageTextField = new JTextField(10);

        // top panel
        JPanel panelSend = new JPanel();
        panelSend.add(sendToLabel);
        panelSend.add(new JScrollPane(selectUsers));
        panelSend.add(addButton);
        panelSend.add(new JScrollPane(sendToUsers));

        // bottom panel
        JPanel panelMessage = new JPanel();
        panelMessage.add(messageLabel);
        panelMessage.add(messageTextField);

        // outer panel
        JPanel outerPanel = new JPanel();
        outerPanel.add(panelSend);
        outerPanel.add(panelMessage);
        sendButton = new JButton("SEND");
        sendButton.addActionListener(actionListener);
        outerPanel.add(sendButton);

        // add panels to container and make frame visible
        createContent.add(outerPanel);
        createFrame.getRootPane().setDefaultButton(sendButton);
        createFrame.setVisible(true);
    } // run

    // Creates conversation object when send button is clicked
    public Conversation createConversation(String receivers, String messageBox, User sender) {
        // Create array list of users
        ArrayList<User> users = new ArrayList<>();
        users.add(sender);

        // get usernames from text box
        int count = 0;
        String[] sendTo = null;

        // if there is only one receiver
        if (receivers.contains(",")) {
            sendTo = receivers.split(",");

            // Determine if the users they want to  send to have an account
            for (User user : User.users) {
                for (String userString : sendTo) {
                    if (user.getUsername().equals(userString)) {
                        users.add(user);
                        count++;
                    }
                }
            }

            // if there aren't multiple
        } else {
            for (User singleUser : User.users) {
                if (receivers.equals(singleUser.getUsername())) {
                    users.add(singleUser);
                    count++;
                }
            }
        }

        String filename = "";
        for (User user : users) {
            filename = filename + user.getID() + "|";
        }

        filename = filename.substring(0, filename.length() - 1) + ".txt";
        File f = new File(filename);
        if (f.exists()) {
            JOptionPane.showMessageDialog(null, "Conversation already exist",
                    "Create conversation", JOptionPane.ERROR_MESSAGE);
            return null;
        } else {
            // Create message object
            Message message = new Message(MessageClient.getUser(), messageBox);

            // Create conversation object
            ArrayList<Message> messageArrayList = new ArrayList<>();
            messageArrayList.add(message);

            // sort users in ascending order of userID
            Collections.sort(users, new SortByID());

            // return conversation
            return new Conversation(messageArrayList, users);
        }
    } // createConversation

    public class SortByID implements Comparator<User> {
        // Used for sorting in ascending order of userID
        public int compare(User a, User b) {
            return a.getID() - b.getID();
        }
    }
}
