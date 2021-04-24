import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * DeleteAccountGUI
 *
 * Represents the gui for deleting account
 *
 * @author
 * @version April 19, 2021
 */

public class EditAccountGUI extends JComponent implements Runnable {
    private String action;

    // Fields
    private JFrame frame;
    private JButton editUsernameButton;
    private JButton editPasswordButton;
    private JButton deleteButton;

    public String getAction() {
        return action;
    }

    // actionListener
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // if user wants to create message
            if (e.getSource() == editUsernameButton) {
                frame.dispose();
                action = "username";
            } // end if

            // if user wants to edit message
            if (e.getSource() == editPasswordButton) {
                frame.dispose();
                action = "password";
            } // end if

            // if user wants to delete message
            if (e.getSource() == deleteButton) {
                frame.dispose();
                action = "delete";
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
        editUsernameButton = new JButton("Change username");
        editUsernameButton.addActionListener(actionListener);
        editPasswordButton = new JButton("Change password");
        editPasswordButton.addActionListener(actionListener);
        deleteButton = new JButton("Delete Account");
        deleteButton.addActionListener(actionListener);

        // panel
        JPanel panel = new JPanel();
        panel.add(editUsernameButton);
        panel.add(editPasswordButton);
        panel.add(deleteButton);
        content.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    } // run

}

