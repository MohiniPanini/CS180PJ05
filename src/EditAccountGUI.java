import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * DeleteAccountGUI
 *
 * Represents the gui for deleting account
 *
 * @author Luka Narisawa,
 * @version April 19, 2021
 */

public class EditAccountGUI extends JComponent implements Runnable {
    private String action;

    // JFrame Fields
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
            // if user wants change username
            if (e.getSource() == editUsernameButton) {
                frame.dispose();
                action = "username";
            } // end if

            // if user wants to change password
            if (e.getSource() == editPasswordButton) {
                frame.dispose();
                action = "password";
            } // end if

            // if user wants to delete account
            if (e.getSource() == deleteButton) {
                frame.dispose();
                action = "delete";
            } // end if
        }
    }; // actionListener

    WindowListener windowListener = new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent evt) {
            action = "closed";
        }
    };

    public void run() {
        frame = new JFrame("Edit Account"); // JFrame
        frame.addWindowListener(windowListener);
        Container content = frame.getContentPane(); // Container for panels
        content.setLayout(new BorderLayout()); // Set layout to border
        frame.setSize(300, 200); // size
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

