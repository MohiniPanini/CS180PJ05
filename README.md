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

  Functionality:
  
  Testing:
  
  Relationships:

// CreateGUI.java \\

  Functionality:
  
  Testing:
  
  Relationships:

// DeleteMessageGUI.java \\

  Functionality:
  
  Testing:
  
  Relationships:

// EditAccountGUI.java \\

  Functionality:
  
  Testing:
  
  Relationships:
  
// EditMessageGUI.java \\

  Functionality:
  
  Testing:
  
  Relationships:

// LoginGUI.java \\

  Functionality:
  
  Testing:
  
  Relationships:

// Message.java \\

  Functionality: A class that represnets a Message object. Each Message object contains a private final User (represents the user that send the message), a private String (represents the message itself) and a private final LocalDateTime (represents the time the message was sent). This class contains 
  
  Testing:
  
  Relationships:

// MessageGUI.java \\

  Functionality:
  
  Testing:
  
  Relationships:

// User.java \\

  Functionality:
  
  Testing:
  
  Relationships:

// UserNotFoundException.java \\

  Functionality:
  
  Testing:
  
  Relationships:
