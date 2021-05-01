import java.io.BufferedReader;
import java.io.*;
import java.net.Socket;
import java.io.OutputStream;
import java.util.List;
import java.io.IOException;
public class ServerThread extends Thread {
    private final Socket client;
    private final Server server;
    private BufferedReader in;
    private PrintWriter out;
    InputStream inputStream = client.getInputStream();
    OutputStream outputStream = client.getOutputStream();

    public ServerThread(Server server, Socket client) {
        this.server = server;
        this.client = client;
    }

    public void run() {
        try {
            handleSocket();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //Change string vars to GUI methods
    private void handleSocket() throws IOException, InterruptedException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            if ("quit".equalsIgnoreCase(line)) {
                break;
            }
            String msg = "You: " + line;
            outputStream.write(msg.getBytes());
        }
        client.close();
    }

    //DM Handling
    private void handleMessage() throws IOException {
        // get output
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        // get input
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String action = in.readLine();

        if (isSendClicked() == true) {

        }
    }
}
