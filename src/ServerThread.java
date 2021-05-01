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
    InputStream inputStream;
    OutputStream outputStream;

    public ServerThread(Server server, Socket client) throws IOException {
        this.server = server;
        this.client = client;
	try {
		this.inputStream = client.getInputStream();
		this.outputStream = client.getOutputStream();
	} catch (IOException e) {
		throw e;
	}
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
	try {
       		 // get output
       		 out = new PrintWriter(client.getOutputStream(), true);
       		 // get input
	        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
	      	  String action = in.readLine();
	
		/* what is this supposed to be? i can't figure it out
       		 if (isSendClicked() == true) {

		}
		*/
	} catch (IOException e) {
		throw e;
	}
    }
}
