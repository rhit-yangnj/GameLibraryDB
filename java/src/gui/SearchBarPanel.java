package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
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
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SearchBarPanel extends JPanel {
	private ConnectionManager connectionManager;
	private UserManager userManager;
	private UpdateManager updateManager;
	
	private JLabel searchLabel = new JLabel("Search Filters:");
	private JTextField gameNameInput = new JTextField(20);
	private JTextField studioInput = new JTextField(20);
	private JTextField platformInput = new JTextField(20);
	private JTextField genreInput = new JTextField(20);
	private JButton searchButton = new JButton("Search");
	
	private String[] previousSearchArray = {null, null, null, null};
	
	private HashMap<String, GameSearchResultEntry> mostRecentSearch;
	
	public SearchBarPanel(ConnectionManager connectionManager, UserManager userManager) {
		this(connectionManager);
		this.userManager = userManager;
	}
	
	public SearchBarPanel(ConnectionManager connectionManager) {
		this.connectionManager = connectionManager;
		this.userManager = null;
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
		
		this.searchButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] searchArray = {gameNameInput.getText(), studioInput.getText(), platformInput.getText(), genreInput.getText()};
				previousSearchArray = searchArray;
				redoSearch();
			}
		}); 
		
		this.add(searchLabel);
		this.add(gameNameInput);
		this.add(studioInput);
		this.add(platformInput);
		this.add(genreInput);
		this.add(searchButton);
		
	}
	
	public void blankSearch() {
		String[] searchArray = {null, null, null, null};
		previousSearchArray = searchArray;
		if (userManager != null) {
			if(userManager.getUser() != null) {
				searchGames(userManager.getUser(), null, null, null, null);
			}
		} else {
			searchGames(null, null, null, null, null);
		}
	}
	
	private void searchGames(String username, String gameName, String studio, String platform, String genre) {
		Connection connection = connectionManager.getConnection();
		
		CallableStatement stmt;
		try {
			stmt = connection.prepareCall("{call SearchGames(?,?,?,?,?)}");
			stmt.setString(1, username);
			if (gameName != null && !gameName.equals("Game Name")) {
				stmt.setString(2, gameName);
			} else {
				stmt.setNull(2, Types.VARCHAR);
			}
			if (studio != null && !studio.equals("Studio Name")) {
				stmt.setString(3, studio);
			} else {
				stmt.setNull(3, Types.VARCHAR);
			}
			if (platform != null && !platform.equals("Platform Name")) {
				stmt.setString(4, platform);
			} else {
				stmt.setNull(4, Types.VARCHAR);
			}
			if (genre != null && !genre.equals("Genre")) {
				stmt.setString(5, genre);
			} else {
				stmt.setNull(5, Types.NVARCHAR);
			}
			ResultSet rs = stmt.executeQuery();
			this.mostRecentSearch = parseResults(rs);
			if (this.updateManager != null) {
				this.updateManager.GameBrowserUpdate();
			}
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
				GameSearchResultEntry newEntry = new GameSearchResultEntry(gameName, 
						description, 
						studioName, 
						platformName, 
						genre, 
						releaseDate);
				results.put(gameName, newEntry);				
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
	
	public void setUpdateManager(UpdateManager um) {
		this.updateManager = um;
	}
	
	public void removeRowFromSearch(String gameName) {
		System.out.println("Removing game: " + gameName);
		this.mostRecentSearch.remove(gameName);
	}

	public void redoSearch() {
		if (userManager != null) {
			searchGames(userManager.getUser(), previousSearchArray[0], previousSearchArray[1], previousSearchArray[2], previousSearchArray[3]);
		} else {
			searchGames(null, previousSearchArray[0], previousSearchArray[1], previousSearchArray[2], previousSearchArray[3]);
		}
	}
	
}
