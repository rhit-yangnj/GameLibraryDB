package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

public class SingleGameBrowsingPanel extends JPanel {
    private ConnectionManager connectionManager;
    private UserManager userManager;
    private JComboBox<String> gameList = new JComboBox<>();
    private JButton chooseButton = new JButton("Choose");
    private JLabel infoLabel = new JLabel("Pick a game to view reviews and notes");
    private DefaultTableModel noteTableModel;
    private DefaultTableModel reviewTableModel;
    private String selectedGameName = "";

    public SingleGameBrowsingPanel(ConnectionManager connectionManager, UserManager userManager) {
        this.connectionManager = connectionManager;
        this.userManager = userManager;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel gamePanel = new JPanel();
        gamePanel.add(infoLabel);

        // Populate the gameList JComboBox with stored games
        String[] games = getAllStoredGames();
        for (String game : games) {
            gameList.addItem(game);
        }

        gamePanel.add(gameList);
        gamePanel.add(chooseButton);
        add(gamePanel);

        // Create note output table
        JPanel notePanel = new JPanel(new BorderLayout());
        notePanel.add(new JLabel("Notes:"), BorderLayout.NORTH);
        noteTableModel = new DefaultTableModel(new Object[]{"Notes", "Users"}, 0);
        JTable noteOutput = new JTable(noteTableModel);
        JScrollPane noteScrollPane = new JScrollPane(noteOutput);
        notePanel.add(noteScrollPane, BorderLayout.CENTER);
        add(notePanel);

        // Create review output table
        JPanel reviewPanel = new JPanel(new BorderLayout());
        reviewPanel.add(new JLabel("Reviews:"), BorderLayout.NORTH);
        reviewTableModel = new DefaultTableModel(new Object[]{"Reviews", "Users"}, 0);
        JTable reviewOutput = new JTable(reviewTableModel);
        JScrollPane reviewScrollPane = new JScrollPane(reviewOutput);
        reviewPanel.add(reviewScrollPane, BorderLayout.CENTER);
        add(reviewPanel);

        JLabel ratingLabel = new JLabel("Average Rating:");
        JLabel averageRating = new JLabel("Choose a game to view its rating");
//        add(ratingLabel);
//        add(averageRating);

        chooseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedGameName = (String) gameList.getSelectedItem();
				ErrorPanels.createInfoDialogue("Selected game: " + selectedGameName);
                //JOptionPane.showMessageDialog(null, "Selected game: " + selectedGameName);
                addNotesToTable(selectedGameName); // Call addNotesToTable method with the selected game name
                addReviewsToTable(selectedGameName); // Call addReviewsToTable method with the selected game name
                displayAverageRating(selectedGameName); // Call displayAverageRating method with the selected game name
            }
        });
    }

    public void redoStoredGames() {
        this.gameList.removeAllItems();
        String[] currentList = getAllStoredGames();

        for (int i = 0; i < currentList.length; i++) {
            this.gameList.addItem(currentList[i]);
        }
    }

    private String[] getAllStoredGames() {
        if (userManager.getUser() == null) {
            String[] current = {"Log In to use"};
            return current;
        }

        Connection connection = connectionManager.getConnection();

        ArrayList<String> results = new ArrayList<String>();

        CallableStatement stmt;
        try {
            stmt = connection.prepareCall("{call getAllGames}");
            ResultSet rs = stmt.executeQuery();

            int gameNameIndex = rs.findColumn("Name");

            while (rs.next()) {
                String currentGame = rs.getString(gameNameIndex);
                results.add(currentGame);
            }

            String[] dsf = new String[results.size()];
            return results.toArray(dsf);

        } catch (SQLException e) {
            e.printStackTrace();
            String[] current = {"Error Encountered"};
            return current;
        }
    }

    public void addNotesToTable(String selectedGameName) {
        Connection connection = connectionManager.getConnection();
        ArrayList<String[]> noteData = new ArrayList<>();

        CallableStatement stmt;
        try {
            // Call the stored procedure to retrieve notes for the selected game
            stmt = connection.prepareCall("{call GetNotesForGame(?)}");
            stmt.setString(1, selectedGameName);
            ResultSet rs = stmt.executeQuery();

            // Populate noteData with the retrieved notes data
            while (rs.next()) {
                String note = rs.getString("Note");
                String user = rs.getString("NoteWriter");
                noteData.add(new String[]{note, user});
            }

            // Update the note output table with the retrieved data
            updateNoteTable(noteData);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQLException
        }
    }

    public void addReviewsToTable(String selectedGameName) {
        Connection connection = connectionManager.getConnection();
        ArrayList<String[]> reviewData = new ArrayList<>();

        CallableStatement stmt;
        try {
            // Call the stored procedure to retrieve reviews for the selected game
            stmt = connection.prepareCall("{call GetReviewsForGame(?)}");
            stmt.setString(1, selectedGameName);
            ResultSet rs = stmt.executeQuery();

            // Populate reviewData with the retrieved reviews data
            while (rs.next()) {
                String review = rs.getString("Review");
                String user = rs.getString("ReviewWriter");
                reviewData.add(new String[]{review, user});
            }

            // Update the review output table with the retrieved data
            updateReviewTable(reviewData);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQLException
        }
    }

    // Method to update note output table
    public void updateNoteTable(ArrayList<String[]> noteData) {
        noteTableModel.setRowCount(0); // Clear existing data
        for (String[] row : noteData) {
            noteTableModel.addRow(row);
        }
    }

    // Method to update review output table
    public void updateReviewTable(ArrayList<String[]> reviewData) {
        reviewTableModel.setRowCount(0); // Clear existing data
        for (String[] row : reviewData) {
            reviewTableModel.addRow(row);
        }
    }

    public void displayAverageRating(String selectedGameName) {
        Connection connection = connectionManager.getConnection();
        double averageRating = 0.0;

        CallableStatement stmt;
        try {
            // Call the stored procedure to retrieve the average rating for the selected game
            stmt = connection.prepareCall("{call GetAverageStarsForGame(?)}");
            stmt.setString(1, selectedGameName);

            ResultSet rs = stmt.executeQuery();

            // Retrieve the result set containing the average rating
            if (rs.next()) {
                averageRating = rs.getDouble(1); // Assuming the average rating is in the first column of the result set
            }

            // Update the infoLabel with the average rating
            infoLabel.setText("Average Rating: " + averageRating);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQLException
        }
    }

}
