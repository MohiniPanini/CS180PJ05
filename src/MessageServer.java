import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.Random;

/**
 * MessageServer
 *
 * Server for the Messaging app.
 *
 * @author
 * @version April 17, 2021
 */
public class MessageServer {
    // runs multiple threads for clients
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(1234);
            serverSocket.setReuseAddress(true);
            // loop for multiple clients
            while (true) {
                Socket client = serverSocket.accept();

                // create a thread
                MessageThread messageThread = new MessageThread(client);
                new Thread(messageThread).start();
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
        } // end try
    } // main


    private static class MessageThread implements Runnable {
        private final Socket clientSocket;

        public MessageThread(Socket socket) {
            this.clientSocket = socket;
        }
        // run method for server actions
        public void run() {
            PrintWriter out = null;
            BufferedReader in = null;
            try {
                // get output
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                // get input
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                boolean loggedIn = false;
                User user = null;

                // loop until successful login
                while(!loggedIn) {
                    String action;
                    action = in.readLine();
                    // log in
                    if (action.equals("login")) {
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
                        } else {
                            out.write("Username or Password is incorrect");
                            out.println();
                            out.flush();
                        } // end if
                    } // end if, end login

                    // create account
                    else if (action.equals("create")) {
                        boolean created = false;
                        while (true) {
                            String username = in.readLine();
                            BufferedReader bfr = new BufferedReader(new FileReader("Users.txt"));
                            String line = bfr.readLine();
                            boolean alreadyExist = false;
                            while (line != null) {
                                int bar = line.indexOf("|");
                                String existUsername = line.substring(0, bar);
                                if (existUsername.equals(username)) {
                                    alreadyExist = true;
                                }
                                line = bfr.readLine();
                            } // end while
                            if (alreadyExist) {
                                out.write("Username already exist");
                                out.println();
                                out.flush();
                            } else {
                                out.write("validUsername");
                                out.println();
                                out.flush();
                                while (true) {
                                    String password = in.readLine();
                                    boolean length = false;
                                    boolean digit = false;
                                    boolean Uppercase = false;
                                    boolean Lowercase = false;
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
                                            } // end if
                                        } // end for
                                    } // end if
                                    if (length && digit && Uppercase && Lowercase) {
                                        out.write("valid");
                                        out.println();
                                        out.flush();
                                        boolean unique = false;
                                        int id = 0;
                                        while (!unique) {
                                            Random r = new Random();
                                            id = 10000000 + r.nextInt(90000000);
                                            unique = true;
                                            BufferedReader bfr2 = new BufferedReader(new FileReader("Users.txt"));
                                            String line2 = bfr2.readLine();
                                            while (line2 != null) {
                                                int currentId = Integer.parseInt(line2.substring(line2.lastIndexOf("|") + 1));
                                                if (currentId == id) {
                                                    unique = false;
                                                } // end if
                                                line2 = bfr2.readLine();
                                            } // end while
                                        } // end while
                                        user = new User(username, password, id);
                                        PrintWriter writer = new PrintWriter(new FileOutputStream("Users.txt", true));
                                        writer.println(user.getUsername() + "|" + user.getPassword() + "|" + user.getID());
                                        writer.close();
                                        loggedIn = true;
                                        break;
                                    } else {
                                        out.write("invalid");
                                        out.println();
                                        out.flush();
                                    } // end if
                                } // end while
                                break;
                            } // end if
                        } // end while
                    } // end create account

                    // edit or delete account
                    else if (action.equals("edit")) {
                        while (!loggedIn) {
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
                            } else {
                                out.write("Username or Password is incorrect");
                                out.println();
                                out.flush();
                            } // end if
                        } // end login
                        String editAction = in.readLine();
                        if (editAction.equals("username")) {
                            String username = in.readLine();
                            user.setUsername(username);
                            User.writeAllUsersToFile();
                        } else if (editAction.equals("password")) {
                            String password = in.readLine();
                            user.setUsername(password);
                            User.writeAllUsersToFile();
                        } else {
                            String confirm = in.readLine();
                            if (confirm.equals("yes")) {
                                for (int i = 0; i < User.users.size(); i++) {
                                    if (User.users.get(i) == user) {
                                        User.users.remove(i);
                                    } // end if
                                } // end for
                                User.writeAllUsersToFile();
                            } // end if
                        } // end if
                    } // end edit or delete account
                } // end login process


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
        }
    }
}