package gui;

import java.awt.Dimension;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class GameBrowserPanel extends JPanel{
	private ConnectionManager connectionManager;
	private UserManager userManager;
	private UpdateManager updateManager;
	
	private JScrollPane scrollPane;
	private JTable table;
	private SearchBarPanel searchBar;
	
	Object[] columnNames = {"Name", "Release Date", "Description", "Genres", "Platforms", "Studio"};
	
	public GameBrowserPanel(ConnectionManager connectionManager, UserManager userManager) {
		this.searchBar = new SearchBarPanel(connectionManager);
		this.connectionManager = connectionManager;
		this.userManager = userManager;		
		this.table = new JTable(BuildTable(), columnNames);
		this.scrollPane = new JScrollPane(table);
		this.scrollPane.setPreferredSize(new Dimension(720, 480));
		this.table.setFillsViewportHeight(true);
		this.add(searchBar);
		this.add(scrollPane);
	}

	public void UpdateView() {
		this.remove(scrollPane);
		this.table = new JTable(BuildTable(), columnNames);
		this.scrollPane = new JScrollPane(table);
		this.scrollPane.setPreferredSize(new Dimension(720, 480));
		table.setFillsViewportHeight(true);
		this.add(scrollPane);
	}
	
	public Object[][] BuildTable() {		
		Connection connection = connectionManager.getConnection();
		
		ArrayList<Object[]> results = new ArrayList<Object[]>();
		
		if (this.searchBar.getMostRecentSearch() == null) {
			this.searchBar.blankSearch();
		}
						
		for (String key : this.searchBar.getMostRecentSearch().keySet()) {
			GameSearchResultEntry entry = this.searchBar.getMostRecentSearch().get(key);
			String CurrentGame = entry.getGameName();
			
			String CurrentDescription = entry.getDescription();
			if (CurrentDescription.equals("")) {
				CurrentDescription = "---";
			}
			
			String CurrentDate = entry.getReleaseDate();
			if (CurrentDate.equals("1969-12-31")) {
				CurrentDate = "---";
			}
			
			String CurrentGenres = "";
			for (String genre : entry.getGenres()) {
				CurrentGenres = CurrentGenres + ", " + genre;
			}
			CurrentGenres = CurrentGenres.substring(2, CurrentGenres.length());
			if (CurrentGenres.equals("null")) {
				CurrentGenres = "---";
			}
			
			String CurrentPlatforms = "";
			for (String platform : entry.getPlatformNames()) {
				CurrentPlatforms = CurrentPlatforms + ", " + platform;
			}
			CurrentPlatforms = CurrentPlatforms.substring(2, CurrentPlatforms.length());
			if (CurrentPlatforms.equals("null")) {
				CurrentPlatforms = "---";
			}
			
			String CurrentStudio = entry.getStudioName();
			if (CurrentStudio == null) {
				CurrentStudio = "---";
			}
			
			Object[] rowValues = {CurrentGame, CurrentDate, CurrentDescription, CurrentGenres, CurrentPlatforms, CurrentStudio};
			
			results.add(rowValues);
		}
	
		Object[][] dsf = new Object[results.size()][6];
		
		return results.toArray(dsf);
	}

	public void setUpdateManager(UpdateManager um) {
		this.updateManager = um;
		this.searchBar.setUpdateManager(um);
	}
}
