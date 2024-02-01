package gui;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class FullTablePanel extends JPanel {
	
	private ConnectionManager connectionManager;
	private UserManager userManager;
	
	private JScrollPane scrollPane;
	private JTable table;
	
	String[] columnNames = {"Name", "Release Date", "Description", "Genres"};
	
	public FullTablePanel(ConnectionManager connectionManager, UserManager userManager) {
		this.connectionManager = connectionManager;
		this.userManager = userManager;		
	}

	public void UpdateView() {
		this.table = new JTable(BuildTable(), columnNames);
		this.scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
	}
	
	public Object[][] BuildTable() {
		if(userManager.getUser() == null) {
			Object[][] current = {{"Log In to use"}};
			return current;
		}
		
		Connection connection = connectionManager.getConnection();
		
		ArrayList<Object[]> results = new ArrayList<Object[]>();
						
		CallableStatement stmt;
		try {
			stmt = connection.prepareCall("{call getPersonalGames(?)}");
			stmt.setString(1, userManager.getUser());
			
			ResultSet rs = stmt.executeQuery();
			
			int gameNameIndex = rs.findColumn("Name");
			int descriptionIndex = rs.findColumn("Description");
			int releaseDateIndex = rs.findColumn("ReleaseDate");
			int genresIndex = rs.findColumn("Genres");
			
			while (rs.next()) {
				String CurrentGame = rs.getString(gameNameIndex);
				String CurrentDescription = rs.getString(descriptionIndex);
				Date CurrentDate = rs.getDate(releaseDateIndex);
				String CurrentGenres = rs.getString(genresIndex);
				
				Object[] rowValues = {CurrentGame, CurrentDate, CurrentDescription, CurrentGenres};
				
				results.add(rowValues);
			}
			
			Object[][] dsf = new Object[results.size()][4];
			
			return results.toArray(dsf);
			
		} catch (SQLException e) {
			e.printStackTrace();
			Object[][] current = {{"Error Encountered"}};
			return current;
		}
	}
}