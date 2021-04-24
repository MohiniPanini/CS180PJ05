import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
}
