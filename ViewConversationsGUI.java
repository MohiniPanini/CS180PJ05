import javax.swing.*;
import java.awt.*;

/**
 * viewConversationsGUI
 *
 * Represents the gui that allows the user to view all conversations
 *
 * @author
 * @version April 19, 2021
 */

public class viewConversationsGUI extends JComponent implements Runnable {

    // Fields
    private JFrame conversationsFrame;

    public void run() {

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
    } // run
}
