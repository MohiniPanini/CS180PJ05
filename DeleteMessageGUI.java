import javax.swing.*;
import java.awt.*;

/**
 * DeleteMessageGUI
 *
 * Represents the gui that gives user options to delete messages
 *
 * @author
 * @version April 19, 2021
 */

public class DeleteMessageGUI extends JComponent implements Runnable {

    // Fields
    private JFrame deleteFrame;

    public void run() {
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
    } // run
}

