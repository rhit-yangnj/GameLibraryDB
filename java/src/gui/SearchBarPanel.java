package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SearchBarPanel extends JPanel {
	private ConnectionManager connectionManager;
	private UserManager userManager;
	
	private JTextField gameNameInput;
	private JTextField studioInput;
	private JTextField platformInput;
	private JTextField genreInput;
	private JButton searchButton;
	
	private HashMap<String, GameSearchResultEntry> mostRecentSearch;
	
	public SearchBarPanel(ConnectionManager connectionManager, UserManager userManager) {
		this(connectionManager);
		this.userManager = userManager;
	}
	
	public SearchBarPanel(ConnectionManager connectionManager) {
		this.connectionManager = connectionManager;
		this.gameNameInput = new JTextField(20);
		this.studioInput = new JTextField(20);
		this.platformInput = new JTextField(20);
		this.genreInput = new JTextField(20);
		gameNameInput.setText("Game Name");
		gameNameInput.setForeground(Color.GRAY);
		gameNameInput.addFocusListener(new FocusListener() {
		    @Override
		    public void focusGained(FocusEvent e) {
		        if (gameNameInput.getText().equals("Game Name")) {
		        	gameNameInput.setText("");
		        	gameNameInput.setForeground(Color.BLACK);
		        }
		    }
		    @Override
		    public void focusLost(FocusEvent e) {
		        if (gameNameInput.getText().isEmpty()) {
		        	gameNameInput.setForeground(Color.GRAY);
		        	gameNameInput.setText("Game Name");
		        }
		    }
		    });
		studioInput.setText("Studio Name");
		studioInput.setForeground(Color.GRAY);
		studioInput.addFocusListener(new FocusListener() {
		    @Override
		    public void focusGained(FocusEvent e) {
		        if (studioInput.getText().equals("Studio Name")) {
		        	studioInput.setText("");
		        	studioInput.setForeground(Color.BLACK);
		        }
		    }
		    @Override
		    public void focusLost(FocusEvent e) {
		        if (studioInput.getText().isEmpty()) {
		        	studioInput.setForeground(Color.GRAY);
		        	studioInput.setText("Studio Name");
		        }
		    }
		    });
		platformInput.setText("Platform Name");
		platformInput.setForeground(Color.GRAY);
		platformInput.addFocusListener(new FocusListener() {
		    @Override
		    public void focusGained(FocusEvent e) {
		        if (platformInput.getText().equals("Platform Name")) {
		        	platformInput.setText("");
		        	platformInput.setForeground(Color.BLACK);
		        }
		    }
		    @Override
		    public void focusLost(FocusEvent e) {
		        if (platformInput.getText().isEmpty()) {
		        	platformInput.setForeground(Color.GRAY);
		        	platformInput.setText("Platform Name");
		        }
		    }
		    });
		genreInput.setText("Genre");
		genreInput.setForeground(Color.GRAY);
		genreInput.addFocusListener(new FocusListener() {
		    @Override
		    public void focusGained(FocusEvent e) {
		        if (genreInput.getText().equals("Genre")) {
		        	genreInput.setText("");
		        	genreInput.setForeground(Color.BLACK);
		        }
		    }
		    @Override
		    public void focusLost(FocusEvent e) {
		        if (genreInput.getText().isEmpty()) {
		        	genreInput.setForeground(Color.GRAY);
		        	genreInput.setText("Genre");
		        }
		    }
		    });
		this.searchButton = new JButton("Search");
		this.searchButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (userManager != null) {
					searchGames(userManager.getUser(), gameNameInput.getText(), studioInput.getText(), platformInput.getText(), genreInput.getText());
				} else {
					searchGames(null, gameNameInput.getText(), studioInput.getText(), platformInput.getText(), genreInput.getText());
				}
			}
		}); 
		
		this.add(gameNameInput);
		this.add(studioInput);
		this.add(platformInput);
		this.add(genreInput);
		this.add(searchButton);
	}
	
	private void searchGames(String username, String gameName, String studio, String platform, String genre) {
		Connection connection = connectionManager.getConnection();
		
		CallableStatement stmt;
		try {
			stmt = connection.prepareCall("{call SearchGames(?,?,?,?,?)}");
			stmt.setString(1, username);
			if (!gameName.equals("Game Name")) {
				stmt.setString(2, gameName);
			} else {
				stmt.setNull(2, Types.VARCHAR);
			}
			if (!studio.equals("Studio Name")) {
				stmt.setString(3, studio);
			} else {
				stmt.setNull(3, Types.VARCHAR);
			}
			if (!platform.equals("Platform Name")) {
				stmt.setString(4, platform);
			} else {
				stmt.setNull(4, Types.VARCHAR);
			}
			if (!genre.equals("Genre")) {
				stmt.setString(5, genre);
			} else {
				stmt.setNull(5, Types.NVARCHAR);
			}
			ResultSet rs = stmt.executeQuery();
			this.mostRecentSearch = parseResults(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	private HashMap<String, GameSearchResultEntry> parseResults(ResultSet rs) {
		HashMap<String, GameSearchResultEntry> results = new HashMap<String, GameSearchResultEntry>();
		try {
			int gameNameIndex = rs.findColumn("Name");
			int descriptionIndex = rs.findColumn("Description");
			int studioNameIndex = rs.findColumn("StudioName");
			int platformNameIndex = rs.findColumn("PlatformName");
			int genreIndex = rs.findColumn("GenreName");
			int releaseDateIndex = rs.findColumn("ReleaseDate");
			while (rs.next()) {
				String gameName = rs.getString(gameNameIndex);
				String description = rs.getString(descriptionIndex);
				String studioName = rs.getString(studioNameIndex);
				String platformName = rs.getString(platformNameIndex);
				String genre = rs.getString(genreIndex);
				String releaseDate = rs.getString(releaseDateIndex);
				if (results.containsKey(gameName)) {
					results.get(gameName).addGenre(gameName);
					results.get(gameName).addPlatform(platformName);
				} else {
					GameSearchResultEntry newEntry = new GameSearchResultEntry(gameName, 
							description, 
							studioName, 
							platformName, 
							genre, 
							releaseDate);
					results.put(gameName, newEntry);
				}
			}
			return results;
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null,
					"An error ocurred while retrieving games.");
			ex.printStackTrace();
			return new HashMap<String, GameSearchResultEntry>();
		}

	}
	
	public HashMap<String, GameSearchResultEntry> getMostRecentSearch() {
		return this.mostRecentSearch;
	}
	
}
