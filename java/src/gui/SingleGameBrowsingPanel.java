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
    private JButton refreshGameListButton = new JButton("Refresh Game List");
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

        String[] games = getAllStoredGames();
        for (String game : games) {
            gameList.addItem(game);
        }

        gamePanel.add(gameList);
        gamePanel.add(chooseButton);
        gamePanel.add(refreshGameListButton);
        add(gamePanel);


        JPanel notePanel = new JPanel(new BorderLayout());
        notePanel.add(new JLabel("Notes:"), BorderLayout.NORTH);
        noteTableModel = new DefaultTableModel(new Object[]{"Notes", "Users"}, 0);
        JTable noteOutput = new JTable(noteTableModel);
        JScrollPane noteScrollPane = new JScrollPane(noteOutput);
        notePanel.add(noteScrollPane, BorderLayout.CENTER);
        add(notePanel);


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

        refreshGameListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                redoStoredGames();
            }
        });

        chooseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedGameName = (String) gameList.getSelectedItem();
                JOptionPane.showMessageDialog(null, "Selected game: " + selectedGameName);
                addNotesToTable(selectedGameName); 
                addReviewsToTable(selectedGameName); 
                displayAverageRating(selectedGameName); 
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

            stmt = connection.prepareCall("{call GetNotesForGame(?)}");
            stmt.setString(1, selectedGameName);
            ResultSet rs = stmt.executeQuery();

 
            while (rs.next()) {
                String note = rs.getString("Note");
                String user = rs.getString("NoteWriter");
                noteData.add(new String[]{note, user});
            }


            updateNoteTable(noteData);
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public void addReviewsToTable(String selectedGameName) {
        Connection connection = connectionManager.getConnection();
        ArrayList<String[]> reviewData = new ArrayList<>();

        CallableStatement stmt;
        try {

            stmt = connection.prepareCall("{call GetReviewsForGame(?)}");
            stmt.setString(1, selectedGameName);
            ResultSet rs = stmt.executeQuery();


            while (rs.next()) {
                String review = rs.getString("Review");
                String user = rs.getString("ReviewWriter");
                reviewData.add(new String[]{review, user});
            }


            updateReviewTable(reviewData);
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }


    public void updateNoteTable(ArrayList<String[]> noteData) {
        noteTableModel.setRowCount(0); 
        for (String[] row : noteData) {
            noteTableModel.addRow(row);
        }
    }


    public void updateReviewTable(ArrayList<String[]> reviewData) {
        reviewTableModel.setRowCount(0); 
        for (String[] row : reviewData) {
            reviewTableModel.addRow(row);
        }
    }

    public void displayAverageRating(String selectedGameName) {
        Connection connection = connectionManager.getConnection();
        double averageRating = 0.0;

        CallableStatement stmt;
        try {

            stmt = connection.prepareCall("{call GetAverageStarsForGame(?)}");
            stmt.setString(1, selectedGameName);

            ResultSet rs = stmt.executeQuery();


            if (rs.next()) {
                averageRating = rs.getDouble(1); 
            }


            infoLabel.setText("Average Rating: " + averageRating);
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

}
