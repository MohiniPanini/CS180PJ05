import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * MessageGUI
 *
 * Represents the gui that gives user messaging choices
 *
 * @author
 * @version April 19, 2021
 */

public class MessageGUI extends JComponent implements Runnable {
    private Conversation conversation;
    private String action;

    // Fields
    private JFrame messagesFrame;
    private JPanel messagesScrollPanel;
    private JButton deleteConversationButton;
    private int x;
    private int y;

    public MessageGUI(Conversation conversation) {
        this.conversation = conversation;
    }

    public String getAction() {
        return action;
    }

    // actionListener
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == deleteConversationButton) {
                if (JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to delete conversation?", "Delete Conversation",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    ConversationsGUI.writeToHiddenConversationFile(conversation);
                    JOptionPane.showMessageDialog(null, "Your conversation has been deleted",
                            "Conversation Deleted", JOptionPane.INFORMATION_MESSAGE);
                    action = "delete";
                    messagesFrame.setVisible(false);
                } // end if
            }
        }
    };

    WindowListener windowListener = new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent evt) {
            action = "closed";
        }
    };

    public void run() {
        // Create new frame for messages
        messagesFrame = new JFrame();
        Container messageContent = messagesFrame.getContentPane();
        messageContent.setLayout(new BorderLayout());
        messagesFrame.setSize(400, 600);
        messagesFrame.setLocationRelativeTo(null);
        messagesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // scroll panel for messages
        messagesScrollPanel = new JPanel();
        messagesScrollPanel.setLayout(new BoxLayout(messagesScrollPanel, BoxLayout.PAGE_AXIS));

        for (Message message : conversation.getMessages()) {
            // show each message
            JLabel username = new JLabel(message.getUser().getUsername());
            JButton messageButton = new JButton(message.getMessage());
            JLabel time = new JLabel(message.getTime().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT)));

            // if user clicks the button
            messageButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    x = e.getX();
                    y = e.getY();
                    JPopupMenu popupmenu = new JPopupMenu("Message");

                    JMenuItem edit = new JMenuItem("Edit");
                    JMenuItem delete = new JMenuItem("Delete");
                    popupmenu.add(edit);
                    popupmenu.add(delete);

                    // add the popup to the frame
                    popupmenu.show(messageButton, x, y);
                    edit.addActionListener(new ActionListener(){
                        public void actionPerformed(ActionEvent e) {
                            JFrame editMessagesFrame = new JFrame();
                            Container messageContent = editMessagesFrame.getContentPane();
                            messageContent.setLayout(new BorderLayout());
                            editMessagesFrame.setSize(400, 200);
                            editMessagesFrame.setLocationRelativeTo(null);
                            editMessagesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            JPanel editPanel = new JPanel();
                            // text field for each message so they can edit
                            JTextField messageTextField = new JTextField(10);
                            messageTextField.setText(message.getMessage());
                            JButton editButton = new JButton("Submit Edit");
                            editPanel.add(messageTextField);
                            editPanel.add(editButton);
                            // if user clicks the button then the associated message is updated
                            editButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    // update message in that conversation messages
                                    message.setMessage(messageTextField.getText());
                                    conversation.writeToFile(conversation.getFilename());
                                    action = "edit";
                                    editMessagesFrame.setVisible(false);
                                    messagesFrame.setVisible(false);
                                }
                            });
                            editMessagesFrame.add(editPanel);
                            editMessagesFrame.setVisible(true);
                        }
                    });
                    delete.addActionListener(new ActionListener(){
                        public void actionPerformed(ActionEvent e) {

                        }
                    });
                }
            });


            JPanel textAndButtonPanel = new JPanel();
            textAndButtonPanel.add(username);
            textAndButtonPanel.add(messageButton);
            textAndButtonPanel.add(time);
            messagesScrollPanel.add(textAndButtonPanel);
        }

        // make frame visible
        JScrollPane messagesJSP = new JScrollPane(messagesScrollPanel,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        deleteConversationButton = new JButton("Delete Conversation");
        // make deleteConversationButton usable
        deleteConversationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConversationsGUI.writeToHiddenConversationFile(conversation);
                if (JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to delete conversation?", "Delete Conversation",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(null, "Your conversation has been deleted",
                            "Conversation Deleted", JOptionPane.INFORMATION_MESSAGE);
                    action = "delete";
                }
            }
        });
        JButton exportConversationButton = new JButton("Export Conversation");
        JPanel topPanel = new JPanel();
        topPanel.add(deleteConversationButton);
        topPanel.add(exportConversationButton);
        messageContent.add(topPanel, BorderLayout.NORTH);
        messageContent.add(messagesJSP, BorderLayout.CENTER);
        messagesFrame.setVisible(true);
    } // run

}

