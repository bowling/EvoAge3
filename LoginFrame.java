/* Socket and IO imports */
import java.io.*;
import java.net.*;

/* Encryption Imports */
import javax.crypto.*;
import java.util.Base64;
import java.security.*;
import java.nio.charset.StandardCharsets;


/* GUI Imports */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/* Author: Michael Redbourne
 * Game: Evony Age III (A variant of Evony Age II, with less bugs and a secure login system!)
 * Credit to baeldung for the custom hex conversion system. (https://www.baeldung.com/sha-256-hashing-java)
 * Questions? Personal Email: mredbourne12@gmail.com - If you're coming from Academia or Professional Positions: mredbour@unb.ca (Please no spam!)
 * This program is by no means "complete". I still have an awful lot of work to do with it, like making the server side application for this, 
 * creating XML or JSON strings for the login requests and much more. However, the hashing system does work, which can be confirmed by other online hash systems.
 * Additionally, I have implemented no length checks (as of yet). Your computed hash will show up in the HashData label, though it's not long enough to see the entire hash.
 * At some point, I would like to use the "haveibeenpwned" API to check (and refuse) any passwords that have previously been breached.
 */

public class LoginFrame {

	private JFrame frmEvonyAgeIii;
	private JTextField JTFUserName;
	private JPasswordField pfPassword;
	public JPasswordField pwConfirmPassword;
	public JRadioButton rdbtnLogin;
	public JRadioButton rdbtnCreateAccount;
	private JLabel lblNewLabel;
	private JLabel lblConnectionStatus;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			String serverAddress = "142.162.21.9";
			int serverPort = 5432;

			/* Run the Application */
			public void run() {
				try {
					LoginFrame window = new LoginFrame();
					window.frmEvonyAgeIii.setVisible(true);
					Image icon = Toolkit.getDefaultToolkit().getImage("C:\\Users\\Michael\\Desktop\\Icon.jpg");
					window.frmEvonyAgeIii.setIconImage(icon);
					/*
					try {
						Socket clientSocket = new Socket(serverAddress, serverPort);
						OutputStream outToServer = clientSocket.getOutputStream();
						DataOutputStream outData = new DataOutputStream(outToServer);
						
						
						
					}
					catch(IOException e){
						
					}
					*/
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LoginFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmEvonyAgeIii = new JFrame();
		frmEvonyAgeIii.setTitle("Evony Age III Login");
		frmEvonyAgeIii.setBounds(100, 100, 645, 357);
		frmEvonyAgeIii.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmEvonyAgeIii.getContentPane().setLayout(null);
		ButtonGroup gAccount = new ButtonGroup(); 
		
		
		JTFUserName = new JTextField();
		JTFUserName.setBounds(135, 63, 96, 20);
		frmEvonyAgeIii.getContentPane().add(JTFUserName);
		JTFUserName.setColumns(20);
		
		pfPassword = new JPasswordField();
		pfPassword.setBounds(135, 94, 96, 20);
		frmEvonyAgeIii.getContentPane().add(pfPassword);
		
		JLabel lblUsername = new JLabel("Email:");
		lblUsername.setHorizontalAlignment(SwingConstants.RIGHT);
		lblUsername.setBounds(60, 66, 65, 15);
		frmEvonyAgeIii.getContentPane().add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPassword.setBounds(60, 97, 65, 15);
		frmEvonyAgeIii.getContentPane().add(lblPassword);
		
		rdbtnLogin = new JRadioButton("Login to an Existing Account");
		rdbtnLogin.setBounds(0, 7, 210, 23);
		rdbtnLogin.setSelected(true);
		frmEvonyAgeIii.getContentPane().add(rdbtnLogin);
		
		rdbtnCreateAccount = new JRadioButton("Create a New Account");
		rdbtnCreateAccount.setBounds(0, 33, 159, 23);
		frmEvonyAgeIii.getContentPane().add(rdbtnCreateAccount);
		
		JButton btnLoginCreate = new JButton("Login/Create Account");

		btnLoginCreate.setBounds(75, 156, 210, 23);
		frmEvonyAgeIii.getContentPane().add(btnLoginCreate);
		
		pwConfirmPassword = new JPasswordField();
		pwConfirmPassword.setEnabled(false);
		pwConfirmPassword.setBounds(135, 125, 96, 20);
		frmEvonyAgeIii.getContentPane().add(pwConfirmPassword);
		
		JLabel lblConfirmPassword = new JLabel("Confirm Password:");
		lblConfirmPassword.setHorizontalAlignment(SwingConstants.RIGHT);
		lblConfirmPassword.setBounds(0, 128, 125, 15);
		frmEvonyAgeIii.getContentPane().add(lblConfirmPassword);
		gAccount.add(rdbtnLogin);
		gAccount.add(rdbtnCreateAccount);
		
		lblNewLabel = new JLabel("Hash Data");
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel.setBounds(10, 203, 609, 20);
		frmEvonyAgeIii.getContentPane().add(lblNewLabel);
		
		lblConnectionStatus = new JLabel("Connection Status: ");
		lblConnectionStatus.setBounds(237, 37, 382, 14);
		frmEvonyAgeIii.getContentPane().add(lblConnectionStatus);
		
		
		
		/* Action Listener for Radio Buttons */
		rdbtnLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pwConfirmPassword.setEnabled(false);	
			}
		});
		rdbtnCreateAccount.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				pwConfirmPassword.setEnabled(true);
			}
		});
		
		btnLoginCreate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				/* Hashing each password (SHA3 256) */
				String password1= new String(pfPassword.getPassword());
				String password2 = new String(pwConfirmPassword.getPassword());
				
				/* Password Hashing */
				String pw1Hashed = encryptData(password1);
				String pw2Hashed = encryptData(password2);
				password1 = "NULL";
				password2 = "NULL";
				
				lblNewLabel.setText(pw1Hashed);
				if(rdbtnCreateAccount.isSelected() == true) {
					if(pw1Hashed.equals(pw2Hashed)) {
						//Open Connections
						/* Create User */
						lblNewLabel.setText(pw1Hashed);
						
					}
				}
				else if(rdbtnLogin.isSelected() == true) {
					/* Send Login Request to server */
					lblNewLabel.setText(pw1Hashed);
				}
			}
		});
		
	}
	
	/* Encryption using 512bit SHA3. */					
	public String encryptData(String plaintextPW){
		try {
		MessageDigest digest = MessageDigest.getInstance("SHA3-512");
		byte[] hashbytes = digest.digest(plaintextPW.getBytes(StandardCharsets.UTF_8));
		String HPW = bytesToHex(hashbytes);
		return HPW;
		}
		catch(NoSuchAlgorithmException e) {
			System.out.println("Failed to create new Password.");
			return null;
		}
		
	}
	/* Credit to baeldung - https://www.baeldung.com/sha-256-hashing-java */
	public static String bytesToHex(byte[] hash) {
	    StringBuffer hexString = new StringBuffer();
	    for (int i = 0; i < hash.length; i++) {
	    String hex = Integer.toHexString(0xff & hash[i]);
	    if(hex.length() == 1) hexString.append('0');
	        hexString.append(hex);
	    }
	    return hexString.toString();
	}
}
