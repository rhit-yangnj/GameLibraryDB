package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class MainViewer {
	
	private ConnectionManager connectionManager;
	private UserManager userManager;
	
	public MainViewer(ConnectionManager connectionManager) {
		this.connectionManager = connectionManager;
		this.userManager = new UserManager();
	}
	
	
	public void viewerMain() {
		final String frameTitle = "Game Library - Register";
		final int frameWidth = 1080;
		final int frameHeight = 720;
		final int frameXLoc = 100;
		final int frameYLoc = 0;
		
		//Set up frame
		JFrame frame = new JFrame();
		frame.setTitle(frameTitle);
		frame.setSize(frameWidth, frameHeight);
		frame.setLocation(frameXLoc, frameYLoc);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent e) {
		        connectionManager.closeConnection();
		    }
		});
		
		//Create tabs
		JTabbedPane tabs = new JTabbedPane();
		
		//Add screens as new tabs
		RegisterPanel registerScreen = new RegisterPanel(connectionManager, userManager);
		tabs.addTab("Register", null, registerScreen, "Register an account");
		AddGamePanel gameScreen = new AddGamePanel(connectionManager, userManager);
		tabs.addTab("Games", null, gameScreen, "Browse your library");
		
		

		//Add Login screens as new tabs
		LoginPanel LoginScreen = new LoginPanel(connectionManager, userManager);
		tabs.addTab("Login Here", null, LoginScreen, "Login to your account here");
		
		
		frame.add(tabs);
		
		frame.setVisible(true);
	}
}
