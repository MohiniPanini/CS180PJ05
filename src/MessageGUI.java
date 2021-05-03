import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * MessageGUI
 * <p>
 * Represents the gui that gives user messaging choices
 *
 * @author Luka Narisawa
 * @version May 3, 2021
 */
public class MessageGUI extends JComponent implements Runnable {
    private final Conversation conversation;
    private String action;
    WindowListener windowListener = new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent evt) {
            action = "closed";
        }
    };
    // Fields
    private JFrame messagesFrame;
    private JPanel messagesScrollPanel;
    private JButton deleteConversationButton;
    private JButton exportConversationButton;
    private JButton sendButton;
    private JTextField messageText;
    // actionListener
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == sendButton) {
                Message message = new Message(MessageClient.getUser(), messageText.getText());
                conversation.addMessage(message);
                action = "send";
                conversation.writeToFile(conversation.getFilename());
                messagesFrame.setVisible(false);
            } else if (e.getSource() == exportConversationButton) {
                // export conversation process
                action = "export";
                messagesFrame.setVisible(false);
            }
        }
    };

    public MessageGUI(Conversation conversation) {
        this.conversation = conversation;
    }

    public String getAction() {
        return action;
    }

    public void run() {
        // Create new frame for messages
        messagesFrame = new JFrame();
        Container messageContent = messagesFrame.getContentPane();
        messageContent.setLayout(new BorderLayout());
        messagesFrame.setSize(400, 600);
        messagesFrame.setLocationRelativeTo(null);
        messagesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        messagesFrame.addWindowListener(windowListener);

        // scroll panel for messages
        messagesScrollPanel = new JPanel();
        messagesScrollPanel.setLayout(new BoxLayout(messagesScrollPanel, BoxLayout.Y_AXIS));

        for (Message message : conversation.getMessages()) {
            // show each message
            JLabel username = new JLabel(message.getUser().getUsername());
            JButton messageButton = new JButton(message.getMessage());
            messageButton.setPreferredSize(new Dimension(200, 30));
            messageButton.setBackground(Color.WHITE);
            messageButton.setOpaque(true);
            messageButton.setBorderPainted(false);
            messageButton.setHorizontalAlignment(SwingConstants.LEFT);
            JLabel time = new JLabel(message.getTime().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT,
                    FormatStyle.SHORT)));

            // if user clicks the their own message
            if (message.getUser().getID() == MessageClient.getUser().getID()) {
                messageButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        int x = e.getX();
                        int y = e.getY();
                        JPopupMenu popupmenu = new JPopupMenu("Message");

                        JMenuItem edit = new JMenuItem("Edit");
                        JMenuItem delete = new JMenuItem("Delete");
                        popupmenu.add(edit);
                        popupmenu.add(delete);

                        // add the popup to the frame
                        popupmenu.show(messageButton, x, y);
                        edit.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                JFrame editMessagesFrame = new JFrame();
                                Container messageContent = editMessagesFrame.getContentPane();
                                messageContent.setLayout(new BorderLayout());
                                editMessagesFrame.setSize(300, 100);
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
                        delete.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                JFrame deleteMessagesFrame = new JFrame();
                                Container messageContent = deleteMessagesFrame.getContentPane();
                                messageContent.setLayout(new BorderLayout());
                                deleteMessagesFrame.setSize(300, 100);
                                deleteMessagesFrame.setLocationRelativeTo(null);
                                deleteMessagesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                JPanel deletePanel = new JPanel();
                                // text field for each message so they can edit
                                JButton yesButton = new JButton("Yes");
                                JButton noButton = new JButton("NO");
                                deletePanel.add(yesButton);
                                deletePanel.add(noButton);
                                // if user clicks the button then the associated message is updated
                                yesButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        conversation.deleteMessage(message);
                                        conversation.writeToFile(conversation.getFilename());
                                        action = "deleteMessage";
                                        deleteMessagesFrame.setVisible(false);
                                        messagesFrame.setVisible(false);
                                    }
                                });
                                noButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        deleteMessagesFrame.setVisible(false);
                                    }
                                });
                                JPanel panel = new JPanel();
                                JLabel confirmation = new JLabel("Delete this message?");
                                panel.add(confirmation);
                                panel.add(deletePanel);
                                deleteMessagesFrame.add(panel);
                                deleteMessagesFrame.setVisible(true);
                            }
                        });
                    }
                });
            }

            JPanel textAndButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)) {
                @Override
                public Dimension getMaximumSize() {
                    return getPreferredSize();
                }
            };
            textAndButtonPanel.setPreferredSize(new Dimension(400, 30));

            textAndButtonPanel.add(username);
            textAndButtonPanel.add(messageButton);
            textAndButtonPanel.add(time);
            messagesScrollPanel.add(textAndButtonPanel);
        }
        JScrollPane messagesJSP = new JScrollPane(messagesScrollPanel);
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
                    messagesFrame.setVisible(false);
                    action = "delete";
                }
            }
        });
        exportConversationButton = new JButton("Export Conversation");
        exportConversationButton.addActionListener(actionListener);
        JPanel topPanel = new JPanel();
        topPanel.add(deleteConversationButton);
        topPanel.add(exportConversationButton);
        JPanel sendMessagePanel = new JPanel();
        messageText = new JTextField(10);
        sendButton = new JButton("SEND");
        sendButton.addActionListener(actionListener);
        sendMessagePanel.add(messageText);
        sendMessagePanel.add(sendButton);
        messageContent.add(topPanel, BorderLayout.NORTH);
        messageContent.add(messagesJSP, BorderLayout.CENTER);
        messageContent.add(sendMessagePanel, BorderLayout.SOUTH);
        messagesFrame.getRootPane().setDefaultButton(sendButton);
        messagesFrame.setVisible(true);
    } // run
}