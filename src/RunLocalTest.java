import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * A framework to run public test cases.
 *
 * <p>Purdue University -- CS18000 -- Spring 2021</p>
 *
 * @author Purdue CS
 * @version January 19, 2021
 */
public class RunLocalTest {
    public static void main(String[] args) {
        try {
            String username = "username";
            String password = "password";
            int id = 12345678;
            User user = new User(username, password, id);
            if (!username.equals(user.getUsername())) {
                System.out.println("user.getUsername() is wrong");
                System.out.println("Failed");
                return;
            } else if (!password.equals(user.getPassword())) {
                System.out.println("user.getPassword() is wrong");
                System.out.println("Failed");
                return;
            } else if (id != user.getID()) {
                System.out.println("user.getID() is wrong");
                System.out.println("Failed");
                return;
            } else if (!user.toString().equals("username|password|12345678")) {
                System.out.println("user.toString() is wrong");
                System.out.println("Failed");
                return;
            } else if (user.getConvosUserIsIn(user).size() != 0) {
                System.out.println("user.getConvosUserIsIn(User user) is wrong");
                System.out.println("Failed");
                return;
            }
            username = "username1";
            password = "password1";
            user.setUsername(username);
            user.setPassword(password);
            if (!username.equals(user.getUsername())) {
                System.out.println("user.setUsername() is wrong");
                System.out.println("Failed");
                return;
            } else if (!password.equals(user.getPassword())) {
                System.out.println("user.setPassword() is wrong");
                System.out.println("Failed");
                return;
            }
            ArrayList<String> usersLines = new ArrayList<String>();
            try (BufferedReader bfr = new BufferedReader(new FileReader("Users.txt"))) {
                String line = bfr.readLine();
                while (line != null) {
                    usersLines.add(line);
                    line = bfr.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            ArrayList<String> conversationsLines = new ArrayList<String>();
            try (BufferedReader bfr = new BufferedReader(new FileReader("Conversations.txt"))) {
                String line = bfr.readLine();
                while (line != null) {
                    conversationsLines.add(line);
                    line = bfr.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try (PrintWriter pw = new PrintWriter(new FileOutputStream("Users.txt", false))) {
                pw.write(user.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            User.getAllUsersFromFile();
            boolean failed = true;
            for (User user1 : User.users) {
                if (user.getID() == user1.getID()) {
                    failed = false;
                }
            }
            if (failed) {
                System.out.println("User.getAllUsersFromFile() is wrong");
                System.out.println("Failed");
                return;
            }
            User user2 = User.getUserByID(user.getID());
            if (user.getID() != user2.getID()) {
                System.out.println("User.getUserByID(int ID) is wrong");
                System.out.println("Failed");
                return;
            }
            User.writeAllUsersToFile();
            try (BufferedReader bfr = new BufferedReader(new FileReader("Users.txt"))) {
                String line = bfr.readLine();
                if (line == null) {
                    System.out.println("User.writeAllUsersToFile() is wrong");
                    System.out.println("Failed");
                    return;
                }
                line = bfr.readLine();
                if (line != null) {
                    System.out.println("User.writeAllUsersToFile() is wrong");
                    System.out.println("Failed");
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            String messageString = "message";
            LocalDateTime time = LocalDateTime.now();
            String timeString = time.toString();
            Message message = new Message(user, messageString);
            Message message1 = new Message(user, messageString, time);
            String messageToString = message.toString();
            String messageExportToString = message.exportToString();
            if (!messageToString.contains("12345678|message|")) {
                System.out.println("message.toString() is wrong");
                System.out.println("Failed");
                return;
            } else if (!messageExportToString.contains("12345678,message,")) {
                System.out.println("message.exportToString() is wrong");
                System.out.println("Failed");
                return;
            } else if (!messageString.equals(message.getMessage())) {
                System.out.println("message.getMessage() is wrong");
                System.out.println("Failed");
                return;
            } else if (user != message.getUser()) {
                System.out.println("message.getUser() is wrong");
                System.out.println("Failed");
                return;
            } else if (message.equals(Message.fromString(messageToString))) {
                System.out.println("Message.fromString(String messageToString) is wrong");
                System.out.println("Failed");
                return;
            } else if (message.equals(Message.importFromString(messageExportToString))) {
                System.out.println("Message.importFromString(String messageToString) is wrong");
                System.out.println("Failed");
                return;
            }
            message.setMessage("message1");
            if (!message.getMessage().equals("message1")) {
                System.out.println("message.setMessage(String message) is wrong");
                System.out.println("Failed");
                return;
            }

            ArrayList<Message> messages = new ArrayList<Message>();
            messages.add(message);
            ArrayList<User> users = new ArrayList<User>();
            users.add(user);
            Conversation conversation = new Conversation(messages, users);
            if (!conversation.getMessages().equals(messages)) {
                System.out.println("conversation.getMessages() is wrong");
                System.out.println("Failed");
                return;
            } else if (!conversation.getConvoUsers().equals(users)) {
                System.out.println("conversation.getConvoUsers() is wrong");
                System.out.println("Failed");
                return;
            }
            if (!conversation.toString().equals(messages.toString() + "##SEPARATIONISHERE##!" + users.toString())) {
                System.out.println("conversation.toString() is wrong");
                System.out.println("Failed");
                return;
            }
            String filename = "TestFile.csv";
            conversation.addMessage(message1);
            if (conversation.getMessages().size() != 2) {
                System.out.println("conversation.addMessage() is wrong");
                System.out.println("Failed");
                return;
            }
            conversation.deleteMessage(message1);
            if (!conversation.getMessages().equals(messages)) {
                System.out.println("conversation.deleteMessage() is wrong");
                System.out.println("Failed");
                return;
            }

            conversation.exportToFile(filename);
            Conversation exportedConversation = Conversation.importFromFile(filename);
            if (!conversation.toString().equals(exportedConversation.toString())) {
                System.out.println("Conversation.importFromFile(String filename) is wrong");
                System.out.println("Failed");
                return;
            }
            conversation.writeToFile(conversation.getFilename());
            Conversation readConversation = Conversation.readFromFile(conversation.getFilename());
            if (!conversation.toString().equals(readConversation.toString())) {
                System.out.println("Conversation.readFromFile(String filename) is wrong");
                System.out.println("Failed");
                return;
            }

            CreateGUI createGUI = new CreateGUI();
            Conversation createConversation = createGUI.createConversation("username1", "message12",
                    user2);
            if (!createConversation.toString().equals("[null]##SEPARATIONISHERE##![username1|password1|12345678," +
                    " username1|password1|12345678]")) {
                System.out.println("createGUI.createConversation() is wrong");
                System.out.println("Failed");
                return;
            }

            ConversationsGUI conversationsGUI = new ConversationsGUI();
            conversationsGUI.usersConversations = conversationsGUI.usersConversations();
            if (conversationsGUI.usersConversations.size() != 0) {
                System.out.println("conversationsGUI.usersConversations() is wrong");
                System.out.println("Failed");
                return;
            }
            try (PrintWriter pw = new PrintWriter(new FileOutputStream("Users.txt", false))) {
                for (String line : usersLines) {
                    pw.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try (PrintWriter pw = new PrintWriter(new FileOutputStream("Conversations.txt", false))) {
                for (String line : conversationsLines) {
                    pw.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Test ran successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
