import java.io.BufferedReader;
import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.OutputStream;
import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
public class Server extends Thread {
    private final int serverPort;

    private ArrayList<ServerThread> workerList = new ArrayList<>();

    public Server(int serverPort) {
        this.serverPort = serverPort;
    }

    public ArrayList<ServerThread> getWorkerList() {
        return workerList;
    }

    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(serverPort);
            serverSocket.setReuseAddress(true);
            // loop for multiple clients
            while (true) {
                Socket client = serverSocket.accept();
                ServerThread serverThread = new ServerThread(this, client);
                workerList.add(serverThread);
                serverThread.start();
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
        }
    }
}
