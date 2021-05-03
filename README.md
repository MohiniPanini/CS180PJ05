# CS180PJ05

How to Compile: You can compile with an IDE.

Submitted Report: McKenna O'Hara
Submitted Presentation: Luka Narisawa
Submitted Vocareum workspace: McKenna O'Hara

// MessageServer.java \\
  
  Functionality: Reporesents a Server for this messaging app, it accepts multiple client at the same time and run on thread.
  
  Testing: Run Local Test
  
  Relationships:

// MessageClient.java \\

  Functionality: Represents a Client for this messaging app. This is where all components of the project come together and a client of the server can run the application.
  
  Testing: Run Local test
  
  Relationships:

// Conversation.java \\

  Functionality: Represents a Conversation object. Each conversation has an array list of message objects that represent the messages of a conversation, an array list of User objects that represent the users in the conversation, and a String that represnts the conversation's filename.
 
  Testing: Run Local Test
  
  Relationships:

// ConversationGUI.java \\

  Functionality: Represents the GUI the user sees once they log in to the application. The GUI contains a "Create new message button", an "Edit or delete Account" button, a "logout" button, an "import conversation" button, and a list of all the users conversations which they can enter by selecting a "select" button.
  
  Testing: Able to select the conversation as the user wants. Manual testing, ran through the application as a user would to catch any possible errors.
  
  Relationships: Extends class JComponent and implements interface Runnable.

// CreateGUI.java \\

  Functionality: Represents the GUI that appears after the user clicks the "Create new message" button of the ConversationGUI. Contains list of existing user to choose who you are sending to and add to see users added on right side box, a text field for the message being sent, and a send button. This class also contains a public createConversation() method that takes the both String contents from createGUI and user object of who is creating a conversation as parameters, and returns a Conversation based on the contents.
  
  Testing: Create both private and group chat, and send message. Manual testing, ran through the application as a user would to catch any possible errors.
  
  Relationships: Extends class JComponent and implements interface Runnable.

// EditAccountGUI.java \\

  Functionality: Represents the GUI that appears when the user clicks the "Edit or delete Account" on the ConversationsGUI. Contains 3 buttons that give user the ability to change username, change password, or delete account. Change username will ask for new username, and change if the input was valid. Change password will ask for new password, and changes if the input was valid. delete account asks to make sure the user wants to delete account, then delete account.
  
  Testing: Change the username of the current account, change the password of the current account, and delete the current account. Manual testing, ran through the application as a user would to catch any possible errors.
  
  Relationships: Extends class JComponent and implements interface Runnable.
  
// LoginGUI.java \\

  Functionality: Represents the first GUI the user sees when running the program. The user is prompted to enter username and password if they already have an account or create an account if they do not. When create account is selected, it ask for the username first and if the input is valid, which means it's not empty, does not contain " ", ",", "|", and it's not already exist, then it will ask for the password which must be at least 8 characters including lowercase, uppercase, and number, but not including " ", ",", "|". If valid password was entered, It will create a new account.
  
  Testing: Login with the incorrect username and password, login with the correct username and password. Create account with exist username, and test the format of password. Manual testing, ran through the application as a user would to catch any possible errors.
  
  Relationships: Extends class JComponent and implements interface Runnable.

// Message.java \\

  Functionality: A class that represnets a Message object. Each Message object contains a private final User (represents the user that send the message), a private String (represents the message itself) and a private final LocalDateTime (represents the time the message was sent). This class contains getter methods, a setter method for String message field, a toString() method, and public static fromString(String string) method that returns Message (reverse of toString()).
  
  Testing: Run Local Test
  
  Relationships:
  
  // MessageGUI.java \\
  
  Functionality: Represents the GUI the user sees after entering a conversation. Here they can see all messages, user who sent each message, and the time the message was sent. Addtionally, here the user can edit or delete messages, send a new message, delete the conversation, or export the conversation through a txt file.
  
  Testing: delete conversation and export the message out with csv file. Manual testing, ran through the application as a user would to catch any possible errors.
  
  Relationships: Extends class JComponent and implements interface Runnable.

// User.java \\

  Functionality: A class that represents a User object. Each User contains a private String username, private String password, private String ID (the generated ID associated with the user) and a public static ArrayList<User> (represnts all users in the application). This class contains getter methods, setter methods for username and password,toString and fromString methods a public static void getAllUsersFromFile() method that reads the "Users.txt" file that contains all users in the application and adds each user to the static ArrayList<User>, a public static void writeAllUsersToFile() that writes each user in the static ArrayList<User> to the "Users.txt" file, and a public static getUserById(int ID) method that returns the user associated with the int ID parameter and throws a UserNotFoundException.
  
  Testing: Run Local Test
  
  Relationships:

// UserNotFoundException.java \\

  Functionality: Represents a UserNotFoundException and is thrown when a user is not found.
  
  Testing: Run Local Test
  
  Relationships: Extend class exception.
  

