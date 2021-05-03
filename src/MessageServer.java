import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.ArrayList;
/**
 * MessageServer
 *
 * Server for the Messaging app.
 *
 * @author Luka Narisawa,
 * @version May 3, 2021
 */
public class MessageServer {
    public static ArrayList<MessageThread> messageThreadList = new ArrayList<>();

    public ArrayList<MessageThread> getMessageThreadList() {
        return messageThreadList;
    }

    // runs multiple threads for clients
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        MessageThread messageThread = null;
        try {
            serverSocket = new ServerSocket(1234);
            serverSocket.setReuseAddress(true);
            // loop for multiple clients
            while (true) {
                Socket client = serverSocket.accept();
                messageThread = new MessageThread(client);
                Thread thread = new Thread(messageThread);
                thread.start();
                messageThreadList.add(messageThread);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } // end try
            } // end if
        }// end try
    } // main

    private static class MessageThread implements Runnable {
        private final Socket clientSocket;
        private BufferedReader in;
        private PrintWriter out;

        public MessageThread(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        // run method for server actions
        public void run() {
            try {
                // get output
                out = new PrintWriter(this.clientSocket.getOutputStream(), true);
                // get input
                in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
                boolean loggedIn = false;
                User user = null;

                // loop until successful login
                while (true) {
                    while (!loggedIn) {
                        String action = in.readLine();
                        switch (action) {
                            case ("login"): {
                                // log in
                                String usernamePassword = in.readLine();
                                BufferedReader bfr = new BufferedReader(new FileReader("Users.txt"));
                                String line = bfr.readLine();
                                while (line != null) {
                                    String userInfo = line.substring(0, line.lastIndexOf("|"));
                                    if (userInfo.equals(usernamePassword)) {
                                        loggedIn = true;
                                        int id = Integer.parseInt(line.substring(line.lastIndexOf("|") + 1));
                                        User.getAllUsersFromFile();
                                        user = User.getUserByID(id);
                                    }
                                    line = bfr.readLine();
                                } // end while
                                if (loggedIn) {
                                    out.write("loggedIn");
                                    out.println();
                                    out.flush();
                                    out.write(user.toString());
                                } else {
                                    out.write("Username or Password is incorrect");
                                } // end if
                                out.println();
                                out.flush();
                                break;
                            }
                            case ("create"): {
                                // create account
                                boolean created = false;
                                while (!created) {
                                    String username = in.readLine();
                                    if (username.equals("Go back to login")) {
                                        break;
                                    }
                                    BufferedReader bfr = new BufferedReader(new FileReader("Users.txt"));
                                    String line = bfr.readLine();
                                    boolean invalid = false;
                                    boolean alreadyExist = false;
                                    if (username.equals("")) {
                                        invalid = true;
                                    } else if (username.contains(" ")) {
                                        invalid = true;
                                    } else if (username.contains(",")) {
                                        invalid = true;
                                    } else if (username.contains("|")) {
                                        invalid = true;
                                    }
                                    if (!invalid) {
                                        while (line != null) {
                                            int bar = line.indexOf("|");
                                            String existUsername = line.substring(0, bar);
                                            if (existUsername.equals(username)) {
                                                alreadyExist = true;
                                            }
                                            line = bfr.readLine();
                                        } // end while
                                    } // end if
                                    if (invalid) {
                                        out.write("Invalid username");
                                        out.println();
                                        out.flush();
                                    } else if (alreadyExist) {
                                        out.write("Username already exist");
                                        out.println();
                                        out.flush();
                                    } else {
                                        out.write("validUsername");
                                        out.println();
                                        out.flush();
                                        while (true) {
                                            String password = in.readLine();
                                            if (password.equals("Go back to login")) {
                                                created = true;
                                                break;
                                            } // end if
                                            boolean length = false;
                                            boolean digit = false;
                                            boolean Uppercase = false;
                                            boolean Lowercase = false;
                                            boolean invalidPassword = false;
                                            if (password.length() >= 8) {
                                                length = true;
                                                for (int i = 0; i < password.length(); i++) {
                                                    char c = password.charAt(i);
                                                    if (Character.isDigit(c)) {
                                                        digit = true;
                                                    } else if (Character.isUpperCase(c)) {
                                                        Uppercase = true;
                                                    } else if (Character.isLowerCase(c)) {
                                                        Lowercase = true;
                                                    } else {
                                                        if (password.contains(" ")) {
                                                            invalidPassword = true;
                                                        } else if (password.contains(",")) {
                                                            invalidPassword = true;
                                                        } else if (password.contains("|")) {
                                                            invalidPassword = true;
                                                        } // end if
                                                    } // end if
                                                } // end for
                                            } // end if
                                            if (invalidPassword) {
                                                out.write("invalid char");
                                                out.println();
                                                out.flush();
                                            } else if (length && digit && Uppercase && Lowercase) {
                                                out.write("valid");
                                                out.println();
                                                out.flush();
                                                boolean unique = false;
                                                int id = 0;
                                                while (!unique) {
                                                    Random r = new Random();
                                                    id = 10000000 + r.nextInt(90000000);
                                                    unique = true;
                                                    BufferedReader bfr2 = new BufferedReader(new FileReader(
                                                            "Users.txt"));
                                                    String line2 = bfr2.readLine();
                                                    while (line2 != null) {
                                                        int currentId = Integer.parseInt(line2.substring(line2.lastIndexOf(
                                                                "|") + 1));
                                                        if (currentId == id) {
                                                            unique = false;
                                                        } // end if
                                                        line2 = bfr2.readLine();
                                                    } // end while
                                                } // end while
                                                user = new User(username, password, id);
                                                User.users.add(user); // add user to static array of all users
                                                PrintWriter writer = new PrintWriter(new FileOutputStream(
                                                        "Users.txt", true));
                                                writer.println(user.getUsername() + "|" + user.getPassword() + "|" +
                                                        user.getID());
                                                writer.close();
                                                out.write(user.toString());
                                                out.println();
                                                out.flush();
                                                loggedIn = true;
                                                break;
                                            } else {
                                                out.write("invalid");
                                                out.println();
                                                out.flush();
                                            } // end if
                                        } // end while
                                        created = true;
                                    } // end if
                                } // end while
                                break;
                            }
                            case ("closed"): {
                                out.close();
                                in.close();
                                clientSocket.close();
                                return;
                            }
                        }
                    } // end login

                    // continue until user quits
                    while (true) {
                        //conversation process
                        out.write(user.toString());
                        out.println();
                        out.flush();
                        String conversationAction = in.readLine();
                        if (conversationAction.equals("create")) {
                        } else if (conversationAction.equals("edit")) {
                            String editAction = in.readLine();
                            if (editAction.equals("username")) {
                                boolean changed = false;
                                while (!changed) {
                                    String username = in.readLine();
                                    if (username.equals("Go back to ConversationGUI")) {
                                        break;
                                    }
                                    BufferedReader bfr = new BufferedReader(new FileReader("Users.txt"));
                                    String line = bfr.readLine();
                                    boolean invalid = false;
                                    boolean alreadyExist = false;
                                    if (username.equals("")) {
                                        invalid = true;
                                    } else if (username.contains(" ")) {
                                        invalid = true;
                                    } else if (username.contains(",")) {
                                        invalid = true;
                                    } else if (username.contains("|")) {
                                        invalid = true;
                                    }
                                    if (!invalid) {
                                        while (line != null) {
                                            int bar = line.indexOf("|");
                                            String existUsername = line.substring(0, bar);
                                            if (existUsername.equals(username)) {
                                                alreadyExist = true;
                                            }
                                            line = bfr.readLine();
                                        } // end while
                                    } // end if
                                    if (invalid) {
                                        out.write("Invalid username");
                                        out.println();
                                        out.flush();
                                    } else if (alreadyExist) {
                                        out.write("Username already exist");
                                        out.println();
                                        out.flush();
                                    } else {
                                        changed = true;
                                        user.setUsername(username);
                                        User.writeAllUsersToFile();
                                        out.write("validUsername");
                                        out.println();
                                        out.flush();
                                    } // end if
                                } // end while
                            } else if (editAction.equals("password")) {
                                while (true) {
                                    String password = in.readLine();
                                    if (password.equals("Go back to ConversationGUI")) {
                                        break;
                                    } // end if
                                    boolean length = false;
                                    boolean digit = false;
                                    boolean Uppercase = false;
                                    boolean Lowercase = false;
                                    boolean invalidPassword = false;
                                    if (password.length() >= 8) {
                                        length = true;
                                        for (int i = 0; i < password.length(); i++) {
                                            char c = password.charAt(i);
                                            if (Character.isDigit(c)) {
                                                digit = true;
                                            } else if (Character.isUpperCase(c)) {
                                                Uppercase = true;
                                            } else if (Character.isLowerCase(c)) {
                                                Lowercase = true;
                                            } else {
                                                if (password.contains(" ")) {
                                                    invalidPassword = true;
                                                } else if (password.contains(",")) {
                                                    invalidPassword = true;
                                                } else if (password.contains("|")) {
                                                    invalidPassword = true;
                                                } // end if
                                            } // end if
                                        } // end for
                                    } // end if
                                    if (invalidPassword) {
                                        out.write("invalid char");
                                        out.println();
                                        out.flush();
                                    }else if (length && digit && Uppercase && Lowercase) {
                                        user.setPassword(password);
                                        User.writeAllUsersToFile();
                                        out.write("valid");
                                        out.println();
                                        out.flush();
                                        break;
                                    } else {
                                        out.write("invalid");
                                        out.println();
                                        out.flush();
                                    } // end if
                                } // end while
                            } else if (editAction.equals("delete")) {
                                String confirm = in.readLine();
                                if (confirm.equals("yes")) {
                                    User.getAllUsersFromFile();
                                    for (int i = 0; i < User.users.size(); i++) {
                                        if (User.users.get(i).getID() == user.getID()) {
                                            User.users.remove(i);
                                        } // end if
                                    } // end for
                                    User.writeAllUsersToFile();
                                    loggedIn = false;
                                    break;
                                } // end if
                            }
                        } else if (conversationAction.equals("logout")) {
                            loggedIn = false;
                            break;
                        } else if (conversationAction.equals("closed")) {
                            out.close();
                            in.close();
                            clientSocket.close();
                            return;
                        } // end if
                    }
                } // end while
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                        clientSocket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } //end run
        public PrintWriter getWriter() {
            return out;
        }
    }
}

