import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private String newUsername;
    private String newPassword;
    private int confirm = -1;

    // JFrame Fields
    private JFrame frame;
    private JButton editUsernameButton;
    private JButton editPasswordButton;
    private JButton deleteButton;

    public String getAction() {
        return action;
    }

    public String getNewUsername() {
        return newUsername;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public int getConfirm() {
        return confirm;
    }

    // actionListener
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // if user wants change username
            if (e.getSource() == editUsernameButton) {
                frame.dispose();
                action = "username";
                newUsername = JOptionPane.showInputDialog(null, "Enter new username",
                        "Change Username", JOptionPane.QUESTION_MESSAGE);
            } // end if

            // if user wants to change password
            if (e.getSource() == editPasswordButton) {
                frame.dispose();
                action = "password";
                newPassword = JOptionPane.showInputDialog(null, "Enter new password",
                        "Change Password", JOptionPane.QUESTION_MESSAGE);
            } // end if

            // if user wants to delete account
            if (e.getSource() == deleteButton) {
                frame.dispose();
                action = "delete";
                confirm = JOptionPane.showConfirmDialog(null, "Enter new password",
                        "Delete account", JOptionPane.YES_NO_OPTION);
            } // end if
        }
    }; // actionListener

    public void run() {
        frame = new JFrame("Edit Account"); // JFrame
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

