import java.io.*;
import java.nio.file.ProviderNotFoundException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Conversation
 *
 * Represents a Conversation object
 *
 * @author Mohini Roplekar, McKenna O'Hara
 * @version April 19, 2021
 */

public class Conversation {
	private ArrayList<Message> messages;
	private final ArrayList<User> convoUsers;
	// static varibale of all conversations
	public static ArrayList<Conversation> conversations = new ArrayList<Conversation>();

	public Conversation(ArrayList<Message> messages, User ...users) {
		this.messages = messages;
		this.convoUsers = new ArrayList<User>();
		for (User user : users) {
			convoUsers.add(user);
		}
	}

	public Conversation(ArrayList<Message> messages, ArrayList<User> convoUsers) {
		this.messages = messages;
		this.convoUsers = convoUsers;
	}

	public ArrayList<Message> getMessages() {
		return messages;
	}

	public ArrayList<User> getConvoUsers() {
		return convoUsers;
	}

	public void writeToFile(String file) {
		ArrayList<String> lines = new ArrayList<String>();
		for (Message message : messages) {
			System.out.println(message);
			lines.add(message.toString());
		}
		String filename = "";
		for (User user : convoUsers) {
			filename = filename + user.getID() + "|";
		}
		filename = filename.substring(0, filename.length() - 1) + ".txt";

		try (PrintWriter writer = new PrintWriter(new FileOutputStream(filename, true))) {
			for (String line : lines) {
				writer.println(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		try (PrintWriter writer = new PrintWriter(new FileOutputStream(file, true))) {
			try (BufferedReader bfr = new BufferedReader(new FileReader(file))) {
				String line = bfr.readLine();

				if (line == null) {
					writer.println(filename);
				}
				while (line != null) {
					if (!filename.equals(line)) {
						writer.println(filename);
					}
					line = bfr.readLine();
				}
			} catch(IOException e) {
				e.printStackTrace();
			}


		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public static Conversation readFromFile(String filename) {
		ArrayList<Message> messages = new ArrayList<Message>();
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			String line = reader.readLine();
			while (line != null) {
				messages.add(Message.fromString(line));
				line = reader.readLine();
			}
			int period = filename.indexOf(".");
			filename = filename.substring(0, period);
			String[] userIDs = filename.split("\\|");
			ArrayList<User> convoUsers = new ArrayList<>();
			for (String ID : userIDs) {
				convoUsers.add(User.getUserByID(Integer.parseInt(ID)));
			}

			return new Conversation(messages, convoUsers);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void readAllConversations() {
		ArrayList<String> filenames = new ArrayList<String>();

		try (BufferedReader reader = new BufferedReader(new FileReader("Conversations.txt"))) {
			String filename = reader.readLine();
			while (filename != null) {
				filenames.add(filename);
				filename = reader.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (filenames != null) {
			for (String filename : filenames) {
				conversations.add(readFromFile(filename));
			}
		}

	}

	public String toString() {
		return messages.toString() + "##SEPARATIONISHERE##!"  + convoUsers.toString();

	}

	public static Conversation fromString(String string) {

		ArrayList<Message> messageArrayList = new ArrayList<>();
		ArrayList<User> userArrayList = new ArrayList<>();

		String[] split = string.split("##SEPARATIONISHERE##!");
		String messages = split[0];
		String users = split[1];

		String[] messagesArray = messages.split(", ");
		for (String message : messagesArray) {
			messageArrayList.add(Message.fromString(message));
		}

		String[] usersArray = users.split(", ");
		for (String user : usersArray) {
			userArrayList.add(User.fromString(user));
		}



		return new Conversation(messageArrayList, userArrayList);

	}
}
