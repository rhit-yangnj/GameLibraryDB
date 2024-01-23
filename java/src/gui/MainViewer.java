package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class MainViewer {
	
	private ConnectionManager connectionManager;
	
	public MainViewer(ConnectionManager connectionManager) {
		this.connectionManager = connectionManager;
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
		
		//Create tabs
		JTabbedPane tabs = new JTabbedPane();
		
		//Add screens as new tabs
		RegisterPanel registerScreen = new RegisterPanel();
		tabs.addTab("Register", null, registerScreen, "Register an account");
		
		
		frame.add(tabs);
		
		frame.setVisible(true);
	}
}
