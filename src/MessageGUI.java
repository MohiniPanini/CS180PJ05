import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * MessageGUI
 *
 * Represents the gui that gives user messaging choices
 *
 * @author
 * @version April 19, 2021
 */

public class MessageGUI extends JComponent implements Runnable {

    // Fields
    private JFrame frame;
    private JButton createButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton conversationsButton;
    private static String action;

    // actionListener
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // if user wants to create message
            if (e.getSource() == createButton) {
                frame.setVisible(false);
                CreateGUI create = new CreateGUI();
                SwingUtilities.invokeLater(create);
                action = "create message";

            } // end if

            // if user wants to edit message
            if (e.getSource() == editButton) {
                frame.setVisible(false);
                EditMessageGUI edit = new EditMessageGUI();
                SwingUtilities.invokeLater(edit);
                action = "edit message";
            } // end if

            // if user wants to delete message
            if (e.getSource() == deleteButton) {
                frame.setVisible(false);
                DeleteMessageGUI delete = new DeleteMessageGUI();
                SwingUtilities.invokeLater(delete);
                action = "delete message";
            } // end if

            // if user wants to view conversations
            if (e.getSource() == conversationsButton) {
                frame.setVisible(false);
                ConversationsGUI conversations = new ConversationsGUI();
                SwingUtilities.invokeLater(conversations);
                action = "view conversations";
            } // end if
        }
    }; // actionListener

    public void run() {
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
    } // run

}

