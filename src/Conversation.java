import java.io.*;
import java.util.ArrayList;

public class Conversation {
	private ArrayList<Message> messages;
	private final User user1;
	private final User user2;

	public Conversation(ArrayList<Message> messages, User user1, User user2) {
		this.messages = messages;
		this.user1 = user1;
		this.user2 = user2;
	}

	public ArrayList<Message> getMessages() {
		return messages;
	}

	public User[] getUsers() {
		User[] userArray = {user1, user2};
		return userArray;
	}

	public void writeToFile() {
		ArrayList<String> lines = new ArrayList<String>();
		for (Message message : messages) {
			lines.add(message.toString());
		}

		String filename = user1.getID() + "|" + user2.getID() + ".txt";

		try (PrintWriter writer = new PrintWriter(new FileOutputStream(filename))) {
			for (String line : lines) {
				writer.println(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Conversation readFromFile(User user1, User user2) {
		ArrayList<Message> messages = new ArrayList<Message>();

		String filename = user1.getID() + "|" + user2.getID() + ".txt";

		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			String line = reader.readLine();
			while (line != null) {
				messages.add(Message.fromString(line));
				line = reader.readLine();
			}

			return new Conversation(messages, user1, user2);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
