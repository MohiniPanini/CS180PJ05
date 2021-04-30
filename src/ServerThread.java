import java.io.BufferedReader;
import java.io.*;
import java.net.Socket;
import java.io.OutputStream;
import java.util.List;
import java.io.IOException;
public class ServerThread extends Thread {
    private final Socket client;

    public ServerThread(Socket client) {
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
