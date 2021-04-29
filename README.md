# CS180PJ05

How to Compile:

Submitted Report:
Submitted Presentation:
Submitted Vocareum workspace:

// MessageServer.java \\
  
  Functionality:
  
  Testing:
  
  Relationships:

// MessageClient.java \\

  Functionality:
  
  Testing:
  
  Relationships:

// Conversation.java \\

  Functionality: Represents a Conversation object. Each conversation contains a private ArrayList<Message> (represents all messages in the conversation), a private final ArrayList<User> (represents all users apart of the conversation), and a public static ArrayList<Conversation> that represents all conversations in the application. This class contains getter methods, a public void writeToFile() method that writes each Message object of the conversation to an individual conversation file. This individual conversation file is then added to a file "conversations.txt" that contains the names of all individual conversation files, a public static readFromFile(String filename) method that returns a Conversation object that was contained inside an individual conversation file, and a public static void readAllConversations() methods that reads the "conversations.txt" file and adds each conversation in the file to the public static ArrayList<Conversation> that represents all conversations in the application.
 
  Testing:
  
  Relationships:

// ConversationGUI.java \\

  Functionality: Represents the GUI the user sees once they log in to the application. The GUI contains a "Create new message button", an "Edit or delete Account" button, and a list of the users conversations. 
  
  Testing:
  
  Relationships: When the "Create new message" button is clicked a CreateGUI appears. When the "Edit or delete Account" button is clicked an EditAccountGUI appears.

// CreateGUI.java \\

  Functionality: Represents the GUI that appears after the user clicks the "Create new message" button of the ConversationGUI. Contians a text field for receivers of the message, a text field for the message being sent, and a send button. This class also contains a public createConversation() method that takes the both text field contents as parameters and returns a Conversation based on the contents.
  
  Testing:
  
  Relationships: Creates the GUI that appears when the user clicks the "Create new message" button from the ConversationGUI.

// EditAccountGUI.java \\

  Functionality: Represents the GUI that appears when the user clicks the "Edit or delete Account" on the ConversationsGUI. Contains 3 buttons that give user the ability to change username, change password, or delete account.
  
  Testing:
  
  Relationships: Appears when the user clicks the "Edit or delete Account" button from the ConversationsGUI.
  
// LoginGUI.java \\

  Functionality: Represents the first GUI the user sees when running the program. The user is prompted to enter username and password if they already have an account or create an account if they do not.
  
  Testing:
  
  Relationships:

// Message.java \\

  Functionality: A class that represnets a Message object. Each Message object contains a private final User (represents the user that send the message), a private String (represents the message itself) and a private final LocalDateTime (represents the time the message was sent). This class contains getter methods, a setter method for String message field, a toString() method, and public static fromString(String string) method that returns Message (reverse of toString()).
  
  Testing:
  
  Relationships:

// User.java \\

  Functionality: A class that represents a User object. Each User contains a private String username, private String password, private String ID (the generated ID associated with the user) and a public static ArrayList<User> (represnts all users in the application). This class contains getter methods, setter methods for username and password, a public static void getAllUsersFromFile() method that reads the "Users.txt" file that contains all users in the application and adds each user to the static ArrayList<User>, a public static void writeAllUsersToFile() that writes each user in the static ArrayList<User> to the "Users.txt" file, and a public static getUserById(int ID) method that returns the user associated with the int ID parameter and throws a UserNotFoundException.
  
  Testing:
  
  Relationships:

// UserNotFoundException.java \\

  Functionality: Represents a UserNotFoundException and is thrown when a user is not found.
  
  Testing:
  
  Relationships: Is thrown in the getUserById(int ID) method of the User class.
