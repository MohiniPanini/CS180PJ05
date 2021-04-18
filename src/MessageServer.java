import java.io.*;
import java.net.*;
/**
 * MessageServer
 *
 * Server for the Messaging app.
 *
 * @author
 * @version April 17, 2021
 */
public class MessageServer {
    //runs multiple threads for clients
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(1234);
            serverSocket.setReuseAddress(true);
            //loop for multiple clients
            while (true) {
                Socket client = serverSocket.accept();

                //create a thread
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
                } //end try
            } //end if
        } //end try
    } //main


    private static class MessageThread implements Runnable {
        private final Socket clientSocket;

        public MessageThread(Socket socket) {
            this.clientSocket = socket;
        }
        //run method for server actions
        public void run() {
            PrintWriter out = null;
            BufferedReader in = null;
            try {
                //get output
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                //get input
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                boolean loggedIn = false;
                //loop until successful login
                while(!loggedIn) {
                    String action;
                    action = in.readLine();

                    //log in
                    if (action.equals("login")) {
                        String usernamePassword = in.readLine();
                        BufferedReader bfr = new BufferedReader(new FileReader("userList.txt"));
                        String line = bfr.readLine();
                        while (line != null) {
                            if (line.equals(usernamePassword)) {
                                loggedIn = true;
                            }
                            line = bfr.readLine();
                        } //end while
                        if (loggedIn) {
                            out.write("loggedIn");
                            out.println();
                            out.flush();
                        } else {
                            out.write("Username or Password is incorrect");
                            out.println();
                            out.flush();
                        } //end if
                    } //end if, end login

                    //create account
                    else if (action.equals("create")) {
                        boolean created = false;
                        while (!created) {
                            String username = in.readLine();
                            System.out.println(username);
                            BufferedReader bfr = new BufferedReader(new FileReader("userList.txt"));
                            String line = bfr.readLine();
                            boolean alreadyExist = false;
                            while (line != null) {
                                if (line.contains(username)) {
                                    alreadyExist = true;
                                }
                                line = bfr.readLine();
                            } //end while
                            System.out.println(alreadyExist);
                            if (alreadyExist) {
                                out.write("Username already exist");
                                out.println();
                                out.flush();
                            } else {
                                out.write("created");
                                out.println();
                                out.flush();
                                String usernamePassword = in.readLine();
                                PrintWriter pw = new PrintWriter(new FileOutputStream("userList.txt", true));
                                pw.println(usernamePassword);
                                pw.close();
                                loggedIn = true;
                            } //end if
                        } //end while
                    } //end if, end create account

                    //edit or delete account
                    else if (action.equals("edit")) {

                    }
                }

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