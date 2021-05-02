import java.io.*;
import java.nio.file.ProviderNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
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
	private final String filename;
	// static variable of all conversations
	public static ArrayList<Conversation> conversations = new ArrayList<Conversation>();

	public Conversation(ArrayList<Message> messages, User ...users) {
		this.messages = messages;
		this.convoUsers = new ArrayList<User>();
		convoUsers.addAll(Arrays.asList(users));
		String filename = "";
		for (User user : convoUsers) {
			filename = filename + user.getID() + "|";
		}
		filename = filename.substring(0, filename.length() - 1) + ".txt";
		this.filename = filename;
	}

	public Conversation(ArrayList<Message> messages, ArrayList<User> convoUsers) {
		this.messages = messages;
		this.convoUsers = convoUsers;
		String filename = "";
		for (User user : convoUsers) {
			filename = filename + user.getID() + "|";
		}
		filename = filename.substring(0, filename.length() - 1) + ".txt";
		this.filename = filename;
	}

	public ArrayList<Message> getMessages() {
		return messages;
	}

	public ArrayList<User> getConvoUsers() {
		return convoUsers;
	}

	public String getFilename() {
		return filename;
	}

	public void addMessage(Message message) {
		messages.add(message);
	}

	public void deleteMessage(Message message) {
		messages.remove(message);
	}

	public void writeToFile(String file) {
		ArrayList<String> lines = new ArrayList<String>();
		for (Message message : messages) {
			lines.add(message.toString());
		}

		try (PrintWriter writer = new PrintWriter(new FileOutputStream(filename, false))) {
			for (String line : lines) {
				writer.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (file.equals("Conversations.txt") || file.contains("Hiddenconvos|")) {
			try (PrintWriter writer = new PrintWriter(new FileOutputStream(file, true))) {
				try (BufferedReader bfr = new BufferedReader(new FileReader(file))) {
					String line = bfr.readLine();

					if (line == null) {
						writer.println(filename);
					} else {
						boolean exist = false;
						while (line != null) {
							if (filename.equals(line)) {
								exist = true;
							}
							line = bfr.readLine();
						}
						if (!exist) {
							writer.println(filename);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}


			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static Conversation readFromFile(String filename) {
		ArrayList<Message> messages = new ArrayList<Message>();
		try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
			String line = reader.readLine();
			while (line != null) {
				if (Message.fromString(line) != null) {
					messages.add(Message.fromString(line));
				}
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
			return null;
		}
	}

	public static void readAllConversations() {
		ArrayList<String> filenames = new ArrayList<String>();

		try (BufferedReader reader = new BufferedReader(new FileReader("Conversations.txt"))) {
			String filename = reader.readLine();
			while (filename != null && !filename.equals("")) {
				filenames.add(filename);
				filename = reader.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		conversations.clear();
		for (String filename : filenames) {
			if (readFromFile(filename) != null) {
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
