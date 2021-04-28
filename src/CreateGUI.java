import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.time.LocalDateTime;

/**
 * src.createGUI
 *
 * Represents the gui that allows the user to create new messages
 *
 * @author
 * @version April 19, 2021
 */

public class CreateGUI extends  JComponent implements Runnable {

    // Fields
    private JFrame createFrame;
    private JTextField sendToTextField;
    private JTextField messageTextField;
    private JButton sendButton;

    // Actionlistener
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == sendButton) {
                // Create conversation object with info and write to conversations file
                CreateGUI create = new CreateGUI();
                Conversation newConversation = create.createConversation(sendToTextField.getText(),
                        messageTextField.getText());
                newConversation.writeToFile();
                createFrame.setVisible(false);
            }
        }
    };

    public void run() {

        // frame, container for when create is clicked
        createFrame = new JFrame("New Message");
        Container createContent = createFrame.getContentPane();
        createContent.setLayout(new BorderLayout());
        createFrame.setSize(600, 400); // size
        createFrame.setLocationRelativeTo(null);
        createFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Labels and fields to add to inner panel
        JLabel sendToLabel = new JLabel("Send to: ");
        JLabel messageLabel = new JLabel("Message: ");
        sendToTextField = new JTextField(10);
        messageTextField = new JTextField(10);

        // top panel
        JPanel panelSend = new JPanel();
        panelSend.add(sendToLabel);
        panelSend.add(sendToTextField);
        // send to text field must have the usernames seperated by commas

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
        createFrame.setVisible(true);

    } // run

    // Creates conversation object when send button is clicked
    public Conversation createConversation(String receivers, String messageBox) {


        // Create array list of users
        ArrayList<User> users = new ArrayList<>();

        // obtain array list of all users that are in the application
        ArrayList<User> usersInApplication = new ArrayList<User>();
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line = reader.readLine();
            while (line != null) {
                int firstSplit = line.indexOf("|");
                int secondSplit = line.lastIndexOf("|");
                String[] parts = {line.substring(0, firstSplit), line.substring(firstSplit + 1, secondSplit),
                        line.substring(secondSplit + 1, line.length())};
                usersInApplication.add(new User(parts[0], parts[1], Integer.parseInt(parts[2])));
                line = reader.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // get usernames from text box
        int count = 0;
        String[] sendTo = null;

        // if there is only one receiver
        if (receivers.contains(",")) {
            sendTo = receivers.split(",");

            // Determine if the users they want to  send to have an account
            for (User user : usersInApplication) {
                for (String userString : sendTo) {
                    if (user.getUsername().equals(userString)) {
                        users.add(user);
                        count++;
                    }
                }
            }

        // if there are multiple
        } else {
            for (User singleUser : usersInApplication) {
                if (receivers.equals(singleUser.getUsername())) {
                    users.add(singleUser);
                    count++;
                }
            }
        }


        // if they don't dislpay an error mesage
        if (count == 0) {
            JOptionPane.showMessageDialog(null, "Not a valid user!", "Invalid User",
                    JOptionPane.ERROR_MESSAGE);
        }

        // Create message object
        User currentUser = null;
        for (User loggedIN : usersInApplication) {
            if (loggedIN.getUsername().equals(LoginGUI.username)) {
                currentUser = loggedIN;
            }
        }

        Message message = new Message(currentUser, messageBox);

        // Create conversation object
        ArrayList<Message> messageArrayList = new ArrayList<>();
        messageArrayList.add(message);

        Conversation returnedConversation = new Conversation(messageArrayList, users);
        Conversation.conversations.add(returnedConversation);

        // return conversatoin
        return returnedConversation;
    } // createConversation
}
