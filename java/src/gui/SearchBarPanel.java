package gui;

import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SearchBarPanel extends JPanel {
	private ConnectionManager connectionManager;
	
	private JTextField gameNameInput;
	private JTextField studioInput;
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
		this.genreInput = new JTextField(20);
		gameNameInput.setText("Game Name");
		studioInput.setText("Studio Name");
		genreInput.setText("Genre");
		this.searchButton = new JButton("Search");
		
		this.add(gameNameInput);
		this.add(studioInput);
		this.add(genreInput);
		this.add(searchButton);
	}
	
	private String buildSearchQueryString(String gameName, String studio, String genre) {
		String sqlStatement = "SELECT * \nFROM Game\n";
		ArrayList<String> wheresToAdd = new ArrayList<String>();

		if (gameName != null) {
			wheresToAdd.add("Restaurant = ?");
		}
		if (studio != null) {
			wheresToAdd.add("Soda = ?");
		}
		if (genre != null) {
			
		}
		boolean isFirst = true;
		while (wheresToAdd.size() > 0) {
			if (isFirst) {
				sqlStatement = sqlStatement + " WHERE " + wheresToAdd.remove(0);
				isFirst = false;
			} else {
				sqlStatement = sqlStatement + " AND " + wheresToAdd.remove(0);
			}
		}
		return sqlStatement;
	}
	
}
