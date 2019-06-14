import java.io.*;
import java.net.*;
import java.util.Date;
import java.sql.*; 
import java.util.Scanner;
import java.sql.Driver;
/* Server login system for EvoA3 */

public class Server extends Thread {

    public static final int PORT = 3332;
    public static final int BUFFER_SIZE = 10;
	Scanner scan = null;

	//Creating server  
    public void run() {
		System.out.println("Server started successfully");
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
		Date date = new Date();
		long time = date.getTime();
		Timestamp ts = new Timestamp(time);
		System.out.println("Authentication Start: " + ts);
        InputStream inputStream = socket.getInputStream();
        FileOutputStream fOutputStream = new FileOutputStream("C:\\Users\\Michael\\Desktop\\Evony\\authenticationReceived.sql");

        byte[] serverBArray = new byte[1]; // fix the hard coded value here.
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
		try{
			System.out.println("Starting New Connection!");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/evonydb","root","<removed>");
			System.out.println("Connection established to DB!");
			Statement stmt = conn.createStatement();
			File f = new File("authenticationReceived.sql");
			
			scan = new Scanner(f);
			String query = scan.nextLine();
			System.out.println(query);
			ResultSet rs = stmt.executeQuery(query);
			
			if(rs.getInt(1) == 1){
				System.out.println("Authentication Success");
				
				//Send update reply to client
				
				conn.close();				
			}
			else{
				System.out.println("Authentication Failed. Retry username and password.");
				conn.close();
				return;
			}
		}
		catch(SQLException sqlExce){
			System.out.println("SQL Exception Occured."); // Write to logfile.
			sqlExce.printStackTrace();
			return;
		}
		
		
		/* Time Completed */

		inputStream.close();
		
		Date date2 = new Date();
		long time2 = date.getTime();
		Timestamp ts2 = new Timestamp(time2);
		System.out.println("Authentication Complete: " + ts2 + "\n\n");
    }

    public static void main(String[] args) {
        new Server().start();
    }
}
