package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SearchBarPanel extends JPanel {
	private ConnectionManager connectionManager;
	
	private JTextField gameNameInput;
	private JTextField studioInput;
	private JTextField platformInput;
	private JTextField genreInput;
	private JButton searchButton;
	private String username;
	
	public SearchBarPanel(ConnectionManager connectionManager, String username) {
		this(connectionManager);
		this.username = username;
	}
	
	public SearchBarPanel(ConnectionManager connectionManager) {
		this.connectionManager = connectionManager;
		this.gameNameInput = new JTextField(20);
		this.studioInput = new JTextField(20);
		this.platformInput = new JTextField(20);
		this.genreInput = new JTextField(20);
		gameNameInput.setText("Game Name");
		studioInput.setText("Studio Name");
		genreInput.setText("Genre");
		this.searchButton = new JButton("Search");
		this.searchButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				searchGames(username, gameNameInput.getText(), studioInput.getText(), platformInput.getText(), genreInput.getText());
			}
			
		}); 
		
		this.add(gameNameInput);
		this.add(studioInput);
		this.add(genreInput);
		this.add(searchButton);
	}
	
	private void searchGames(String username, String gameName, String studio, String platform, String genre) {
		Connection connection = connectionManager.getConnection();
		
		CallableStatement stmt;
		try {
			stmt = connection.prepareCall("{call SearchGames(?,?,?,?,?)}");
			stmt.setString(1, username);
			stmt.setString(2, gameName);
			stmt.setString(3, studio);
			stmt.setString(4, platform);
			stmt.setString(5, genre);
			ResultSet rs = stmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
}
