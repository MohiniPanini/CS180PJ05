import javax.swing.*;
import java.awt.*;

/**
<<<<<<< HEAD:EditMessageGUI.java
 * EditMessageGUI
=======
 * editGUI
>>>>>>> 642f23be6a20f2e983bba2a1bc6d9b86cc2d447c:EditGUI.java
 *
 * Represents the gui if the user chooses to edit their messages
 *
 * @author
 * @version April 19, 2021
 */

<<<<<<< HEAD:EditMessageGUI.java
public class EditMessageGUI extends JComponent implements Runnable {
=======
public class editGUI extends JComponent implements Runnable {
>>>>>>> 642f23be6a20f2e983bba2a1bc6d9b86cc2d447c:EditGUI.java

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
