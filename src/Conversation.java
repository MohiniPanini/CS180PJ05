import java.io.*;
import java.util.ArrayList;

public class Conversation {
	private ArrayList<Message> messages;
	private final ArrayList<User> convoUsers;
	public static ArrayList<Conversation> conversations = new ArrayList<Conversation>();

	public Conversation(ArrayList<Message> messages, User ...users) {
		this.messages = messages;
		this.convoUsers = new ArrayList<User>();
		for (User user : users) {
			convoUsers.add(user);
		}
	}

	public ArrayList<Message> getMessages() {
		return messages;
	}

	public ArrayList<User> getConvoUsers() {
		return convoUsers;
	}

	public void writeToFile() {
		ArrayList<String> lines = new ArrayList<String>();
		for (Message message : messages) {
			lines.add(message.toString());
		}

		String filename = "";
		for (User user : convoUsers) {
			filename = filename + user.getID() + "|";
		}
		filename = filename.substring(0, filename.length() - 1) + ".txt";

		try (PrintWriter writer = new PrintWriter(new FileOutputStream(filename))) {
			for (String line : lines) {
				writer.println(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Conversation readFromFile(User ...users) {
		ArrayList<Message> messages = new ArrayList<Message>();

		String filename = "";
                for (User user : users) {
                        filename = filename + user.getID() + "|";
                }
                filename = filename.substring(0, filename.length() - 1) + ".txt";

		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			String line = reader.readLine();
			while (line != null) {
				messages.add(Message.fromString(line));
				line = reader.readLine();
			}

			return new Conversation(messages, users);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
