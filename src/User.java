import java.io.*;
import java.util.ArrayList;

/**
 * ConversationsGUI
 *
 * Represents a user object
 *
 * @author Mohini Roplekr
 * @version April 19, 2021
 */

public class User {
	//since usernames and passwords can both change, users should be searched/referred to by ID, which cannot change
	private String username;
	private String password;
	private final int ID;
	public static ArrayList<User> users = new ArrayList<User>();

	public User(String username, String password, int ID) {
		this.username = username;
		this.password = password;
		this.ID = ID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getID() {
		return ID;
	}

	@Override
	public String toString() {
		return username + "|" + password + "|" + ID;
	}

	public static void getAllUsersFromFile() {//format: [username|password|ID]
		ArrayList<User> users = new ArrayList<User>();
		try (BufferedReader reader = new BufferedReader(new FileReader("Users.txt"))) {
			String line = reader.readLine();
			while (line != null) {
				int firstSplit = line.indexOf("|");
		                int secondSplit = line.lastIndexOf("|");
		                String[] parts = {line.substring(0, firstSplit), line.substring(firstSplit + 1, secondSplit),
           			     line.substring(secondSplit + 1, line.length())};
				users.add(new User(parts[0], parts[1], Integer.parseInt(parts[2])));
				line = reader.readLine();
			}
			User.users = users;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void writeAllUsersToFile() {
		ArrayList<User> users = User.users;
		try (PrintWriter writer = new PrintWriter(new FileOutputStream("users.txt"))) {
			for (User user : users) {
				writer.println(user.getUsername() + "|" + user.getPassword() + "|" + user.getID());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static User getUserByID(int ID) throws UserNotFoundException {
		User userReturned = null;
		for (User user : User.users) {
			if (user.getID() == ID) {
				userReturned = user;
			}
		}

		if (userReturned == null) {
			throw new UserNotFoundException();
		}

		return userReturned;
	}

	public ArrayList<Conversation> getConvosUserIsIn(User user) {
		ArrayList<Conversation> userConvos =  new ArrayList<Conversation>();

		for (Conversation conversation : Conversation.conversations) {
			for (User convoUser : conversation.getConvoUsers()) {
				if (user.getID() == convoUser.getID()) {
					userConvos.add(conversation);
				}
			}
		}

		return userConvos;
	}

	public static User fromString(String string) {
		String[] components = string.split("\\|");
		String username = components[0];
		String password = components[1];
		int ID = Integer.parseInt(components[2]);
		return new User(username, password, ID);

	}

}
