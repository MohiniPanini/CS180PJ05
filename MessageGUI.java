
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

    // actionlistener
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // if user wants to create message
            if (e.getSource() == createButton) {
                frame.setVisible(false);
                createGUI create = new createGUI();
                SwingUtilities.invokeLater(create);

            } // end if

            // if user wants to edit message
            if (e.getSource() == editButton) {
                frame.setVisible(false);
                editGUI edit = new editGUI();
                SwingUtilities.invokeLater(edit);
            } // end if

            // if user wants to delete message
            if (e.getSource() == deleteButton) {
                frame.setVisible(false);
                DeleteMessageGUI delete = new DeleteMessageGUI();
                SwingUtilities.invokeLater(delete);
            } // end if

            // if user wants to view conversations
            if (e.getSource() == conversationsButton) {
                frame.setVisible(false);
                viewConversationsGUI conversations = new viewConversationsGUI();
                SwingUtilities.invokeLater(conversations);
            } // end if
        }
    }; // actionlistener

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

