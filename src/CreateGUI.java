import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * src.createGUI
 *
 * Represents the gui that allows the user to create new messages
 *
 * @author McKenna O'Hara, Luka Narisawa
 * @version April 19, 2021
 */

public class CreateGUI extends JComponent implements Runnable {

    // Fields
    private JFrame createFrame;
    private JTextField sendToTextField;
    private JTextField messageTextField;
    private static JButton sendButton;

    private String sendClicked;

    public JTextField getSendToTextField() {
        return sendToTextField;
    }

    public JTextField getMessageTextField() {
        return messageTextField;
    }

    public String getSendClicked() {
        return sendClicked;
    }

    // ActionListener
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == sendButton) {
                // Create conversation object with info and write to conversations file
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

        // if they don't display an error message
        if (count == 0) {
            JOptionPane.showMessageDialog(null, "Not a valid user!", "Invalid User",
                    JOptionPane.ERROR_MESSAGE);
        }

        // Create message object
        Message message = new Message(MessageClient.getUser(), messageBox);

        // Create conversation object
        ArrayList<Message> messageArrayList = new ArrayList<>();
        messageArrayList.add(message);

        // return conversation
        return new Conversation(messageArrayList, users);
    } // createConversation


}
