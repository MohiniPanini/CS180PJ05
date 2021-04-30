import java.io.*;
import java.nio.file.ProviderNotFoundException;
import java.util.ArrayList;

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

		try (PrintWriter writer = new PrintWriter(new FileOutputStream(filename, true))) {
			for (String line : lines) {
				writer.println(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		try (PrintWriter writer = new PrintWriter(new FileOutputStream("Conversations.txt", true))) {
			try (BufferedReader bfr = new BufferedReader(new FileReader("Conversations.txt"))) {
				String line = bfr.readLine();

				if (line == null) {
					writer.println(filename);
				}
				while (line != null) {
					if (!filename.equals(line)) {
						System.out.println("filename was new");
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

		for (String filename : filenames) {
			conversations.add(readFromFile(filename));
		}
	}


}
