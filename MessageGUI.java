import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * MessageGUI
 *
 * Represents the messaging GUI
 *
 * @author
 * @version April 19, 2021
 */

public class MessageGUI extends JComponent {

    // Fields
    // options
    private JFrame frame;
    private JButton createButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton conversationsButton;

    // create
    private JFrame createFrame;
    private JTextField sendToTextField;
    private JTextField messageTextField;
    private JButton sendButton;

    // edit
    private JFrame editFrame;

    // delete
    private JFrame deleteFrame;

    // viewConversations
    private JFrame conversationsFrame;

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // if create
            if (e.getSource() == createButton) {
                frame.setVisible(false);
                createFrame();

            } // end if

            // if edit
            if (e.getSource() == editButton) {
                frame.setVisible(false);
                editFrame();

            } // end if

            // if delete
            if (e.getSource() == deleteButton) {
                frame.setVisible(false);
                deleteFrame();

            } // end if

            // if conversations
            if (e.getSource() == conversationsButton) {
                frame.setVisible(false);
                viewConversationsFrame();

            } // end if

            // if create
            if (e.getSource() == sendButton) {

            } // end if
         }
    }; // action listener

    // Methods
    public void optionsFrame() {

        frame = new JFrame("Messages"); // JFrame
        Container content = frame.getContentPane(); // Container for panels
        content.setLayout(new BorderLayout()); // Set layout to border
        frame.setSize(600, 100); // size
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // default close

        // buttons
        createButton = new JButton("Create new message");
        createButton.addActionListener(actionListener);
        editButton = new JButton("Edit existing messages");
        editButton.addActionListener(actionListener);
        deleteButton = new JButton("Delete existing messages");
        deleteButton.addActionListener(actionListener);
        conversationsButton = new JButton("View Conversations");
        conversationsButton.addActionListener(actionListener);

        // panel
        JPanel panel = new JPanel();
        panel.add(createButton);
        panel.add(editButton);
        panel.add(deleteButton);
        panel.add(conversationsButton);

        content.add(panel, BorderLayout.CENTER);

        frame.setVisible(true);


    } // options


    public void createFrame() {

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

    } // create

    public void editFrame() {

        // frame, container for when edit is clicked
        editFrame = new JFrame("Edit messages");
        Container editContent = editFrame.getContentPane();
        editContent.setLayout(new BorderLayout());
        editFrame.setSize(600, 400); // size
        editFrame.setLocationRelativeTo(null);
        editFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Label and panel
        JLabel title = new JLabel("Choose a message to edit");
        JPanel panel = new JPanel();
        panel.add(title);
        editContent.add(panel, BorderLayout.CENTER);
        editFrame.setVisible(true);

    } // edit

    public void deleteFrame() {

        // frame, container for when delete is clicked
        deleteFrame = new JFrame("Delete messages");
        Container deleteContent = deleteFrame.getContentPane();
        deleteContent.setLayout(new BorderLayout());
        deleteFrame.setSize(600, 400); // size
        deleteFrame.setLocationRelativeTo(null);
        deleteFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // label and panel
        JLabel title = new JLabel("Choose a message to delete");
        JPanel panel = new JPanel();
        panel.add(title);
        deleteContent.add(panel, BorderLayout.CENTER);
        deleteFrame.setVisible(true);

    } // delete

    public void viewConversationsFrame() {

        // frame, container for when conversations is clicked
        conversationsFrame = new JFrame("Conversations");
        Container conversationsContent = conversationsFrame.getContentPane();
        conversationsContent.setLayout(new BorderLayout());
        conversationsFrame.setSize(600, 400);
        conversationsFrame.setLocationRelativeTo(null);
        conversationsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // label and panel
        JLabel title = new JLabel("Your Conversations");
        JPanel panel = new JPanel();
        panel.add(title);
        conversationsContent.add(panel, BorderLayout.CENTER);
        conversationsFrame.setVisible(true);
    } // viewConversations

}
