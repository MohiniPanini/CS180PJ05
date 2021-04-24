import javax.swing.*;
import java.awt.*;

/**
 * src.editGUI
 *
 * Represents the gui if the user chooses to edit their messages
 *
 * @author
 * @version April 19, 2021
 */

public class EditGUI extends JComponent implements Runnable {

    // Fields
    private JFrame editFrame;

    // run
    public void run() {

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
    } // run
}
