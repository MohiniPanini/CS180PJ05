import java.io.BufferedReader;
import java.io.*;
import java.net.Socket;
import java.io.OutputStream;
import java.util.List;
import java.io.IOException;
public class ServerThread extends Thread {
    private final Socket client;
    private final Server server;

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

    //Change string to GUI methods
    private void handleSocket() throws IOException, InterruptedException {
        InputStream inputStream = client.getInputStream();
        OutputStream outputStream = client.getOutputStream();

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
}
