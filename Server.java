import java.io.*;
import java.net.*;

/* Server login system. I have not updated the program to connect to mySQL yet. */

public class Server extends Thread {

    public static final int PORT = 3332;
    public static final int BUFFER_SIZE = 626;

	//Creating server
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            while (true) {
                Socket s = serverSocket.accept();
                saveAuthenticationAttempt(s);
            }
        } catch (Exception e) {
			System.out.println("IOException??"); // needs to be written to a log file, containing connection IP Address and other relevant info.
        }
    }

    private void saveAuthenticationAttempt(Socket socket) throws Exception {
        InputStream inputStream = socket.getInputStream();
        FileOutputStream fOutputStream = new FileOutputStream("C:\\Users\\Michael\\Desktop\\Evony\\authenticationReceived.sql");

        byte[] serverBArray = new byte[1024]; // fix the hard coded value here.
        System.out.println("Reading file from server...");
        BufferedOutputStream bOutputStream = new BufferedOutputStream(fOutputStream);
        int bytesRead;
        while ((bytesRead = inputStream.read(serverBArray)) != -1) {
        	bOutputStream.write(serverBArray);
        }
        bOutputStream.close();
        System.out.println("Writing file complete...");
		
		/* Connect to mySQL DB and send file (prepared statement inside SQL file) */
		/* Do NOT close to Socket! */
		
    }

    public static void main(String[] args) {
        new Server().start();
    }
}