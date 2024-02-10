package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types; 
import java.util.ArrayList;

public class ReviewPanel extends JPanel {
	private ConnectionManager connectionManager;
	private UserManager userManager;
	private UpdateManager updateManager;
	private JPanel gamePanel = new JPanel();
//	private JComboBox<String> gameList = new JComboBox<>();
//	private JButton chooseButton = new JButton("Choose");
	private JLabel infoLabel;
	private JLabel addLabel = new JLabel("Add the review using this textbox on the left.");
	private JLabel noteLabel = new JLabel("Review of this game is on the textbox on the right");
	
	private JLabel starInputLabel = new JLabel("Add the number of starts of this game on the left");
	private JLabel starOutputLabel = new JLabel("Number of stars of this game is on the textbox on the right");
	private JPanel actionPanel = new JPanel();
	private JTextField reviewInput = new JTextField(20);
	private JTextField reviewOutput = new JTextField(20);
	private JButton addReviewButton = new JButton("Add");
	private JButton deleteReviewButton = new JButton("Delete");
	private JButton updateReviewButton = new JButton("Update");
	private JTextField numberOfStarInput = new JTextField(20);
	private JTextField numberOfStarOutput = new JTextField(20);
	private String selectedGame= "";
	private int currentGameReviewID;
	private JPanel starPanel = new JPanel();
	public ReviewPanel(ConnectionManager connectionManager, UserManager userManager, String gameName, UpdateManager updateManager) {
		this.connectionManager = connectionManager;
		this.userManager = userManager;
		this.updateManager = updateManager;
		this.selectedGame = gameName;
		this.infoLabel = new JLabel("Current Game: " + gameName);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

//		gameList.setEditable(true);
//		gamePanel.add(gameList);
//		gamePanel.add(chooseButton);
		gamePanel.add(infoLabel);
		add(gamePanel);

		actionPanel.add(addLabel);
		actionPanel.add(reviewInput);
		actionPanel.add(noteLabel);
		actionPanel.add(reviewOutput);
		actionPanel.add(addReviewButton);
		actionPanel.add(deleteReviewButton);
		actionPanel.add(updateReviewButton);
		add(actionPanel);

		
		starPanel.add(starInputLabel);
		starPanel.add(numberOfStarInput);
		starPanel.add(starOutputLabel);
		starPanel.add(numberOfStarOutput);
		add(starPanel);
		
//		chooseButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                selectedGame = (String) gameList.getSelectedItem(); // Store the selected game
//                JOptionPane.showMessageDialog(null, "Selected game: " + selectedGame);
//                currentGameReviewID = chooseGameGetReviewID();
//                if(currentGameReviewID==-1) {
//                	JOptionPane.showMessageDialog(null, "This game does not have a review yet");
//                }
//                else {
//                reviewOutput.setText(readReviewFromDatabase(currentGameReviewID));
//                numberOfStarOutput.setText(readReviewStarFromDatabase(currentGameReviewID));}
//            }
//        });
		currentGameReviewID = chooseGameGetReviewID();
        if (currentGameReviewID==-1) {
        	JOptionPane.showMessageDialog(null, "This game does not have a review yet");
        } else {
	        reviewOutput.setText(readReviewFromDatabase(currentGameReviewID));
	        numberOfStarOutput.setText(readReviewStarFromDatabase(currentGameReviewID));
        }
    

		addReviewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addReviewToDatabase();
			}
		});
		
		deleteReviewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteReviewFromDatabase(currentGameReviewID);
			}
		}
				
				);
		
		updateReviewButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        String newText = reviewInput.getText();
		        String newStar = numberOfStarInput.getText();
		        try {
					System.out.println(connectionManager.getConnection().isClosed());
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		        updateReviewInDatabase(currentGameReviewID, newText, newStar);
		    }
		});


//		redoPersonalGames();
	}

//	public void redoPersonalGames() {
//		this.gameList.removeAllItems();
//		String[] currentList = getAllPersonalGames();
//
//		for (int i = 0; i < currentList.length; i++) {
//			this.gameList.addItem(currentList[i]);
//		}
//	}

	private String[] getAllPersonalGames() {

		if (userManager.getUser() == null) {
			String[] current = { "Log In to use" };
			return current;
		}

		Connection connection = connectionManager.getConnection();

		ArrayList<String> results = new ArrayList<String>();

		CallableStatement stmt;
		try {
			stmt = connection.prepareCall("{call getPersonalGames(?)}");
			stmt.setString(1, userManager.getUser());

			ResultSet rs = stmt.executeQuery();

			int gameNameIndex = rs.findColumn("Name");

			while (rs.next()) {
				String CurrentGame = rs.getString(gameNameIndex);
				results.add(CurrentGame);
			}

			String[] dsf = new String[results.size()];
			return results.toArray(dsf);

		} catch (SQLException e) {
			e.printStackTrace();
			String[] current = { "Error Encountered" };
			return current;
		}
	}
	
	private int chooseGameGetReviewID() {
	    int reviewID = -1; // Default value if noteID is not found or an error occurs

	    if (selectedGame.isEmpty() || selectedGame.equals("Log In to use")) {
	        JOptionPane.showMessageDialog(this, "Please select a game first.", "Error", JOptionPane.ERROR_MESSAGE);
	        return reviewID;
	    }

	    

	    try {
	    	Connection connection= connectionManager.getConnection();
		    CallableStatement stmt;
		    ResultSet rs;
	        stmt = connection.prepareCall("{call GetReviewID(?, ?)}");
	        stmt.setString(1, selectedGame);
	        stmt.setString(2, userManager.getUser());

	        rs = stmt.executeQuery();

	        if (rs.next()) {
	            reviewID = rs.getInt("Id"); 

	        } 
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(this, "Error retrieving ReviewID: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	    }

	    return reviewID;
	}
	
	private String readReviewFromDatabase(int reviewId) {
	    String reviewText = null; 

	    try  {
	    	Connection conn = connectionManager.getConnection();
	        CallableStatement stmt = conn.prepareCall("{call readReview(?)}");
	        stmt.setInt(1, reviewId);
	        ResultSet rs = stmt.executeQuery();
	        
	        if (rs.next()) {
	            reviewText = rs.getString("text"); 
	        } else {
	            JOptionPane.showMessageDialog(this, "Review not found.", "Error", JOptionPane.ERROR_MESSAGE);
	        }

	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(this, "Error reading review: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	    }

	    return reviewText; 
	}
	
	private String readReviewStarFromDatabase(int reviewId) {
	    String reviewText = null; 

	    try  {
	    	Connection conn = connectionManager.getConnection();
	        CallableStatement stmt = conn.prepareCall("{call readReview(?)}");
	        stmt.setInt(1, reviewId);
	        ResultSet rs = stmt.executeQuery();
	        
	        if (rs.next()) {
	            reviewText = rs.getString("NumberOfStars"); 
	        } else {
	            JOptionPane.showMessageDialog(this, "Review star not found.", "Error", JOptionPane.ERROR_MESSAGE);
	        }

	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(this, "Error reading review star: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	    }

	    return reviewText; 
	}



	private void addReviewToDatabase() {
		String tempText="";
		String tempStar="";
		String currentUser = userManager.getUser();
		if (selectedGame.isEmpty()|| selectedGame=="" ||selectedGame=="Log In to use") {
	        JOptionPane.showMessageDialog(this, "Please select a game first.", "Error", JOptionPane.ERROR_MESSAGE);
	        return;
	    }
    
	    String reviewText = reviewInput.getText();
	    String reviewStar = numberOfStarInput.getText();

	    try  {
	    	Connection conn = connectionManager.getConnection();
	         CallableStatement stmt = conn.prepareCall("{call addReview(?, ?, ?, ?)}");
	        stmt.setString(1, selectedGame);
	        stmt.setString(2, reviewStar);
	        stmt.setString(3, reviewText);
	        stmt.setString(4, currentUser);
	        
	        stmt.executeUpdate();
            
	        tempText=reviewInput.getText();
	        tempStar = numberOfStarInput.getText();
	        reviewInput.setText("");
	        numberOfStarInput.setText("");
	        numberOfStarOutput.setText(tempStar);
	        reviewOutput.setText(tempText);
	        
	        JOptionPane.showMessageDialog(this, "Review added successfully.");
	        refreshGames();

	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(this, "Error adding review: " + ex.getMessage(), "Error",
	                JOptionPane.ERROR_MESSAGE);
	    }
	}
	
	private void deleteReviewFromDatabase(int currentReviewId) {
	    if (currentReviewId == -1) {
	        JOptionPane.showMessageDialog(this, "No note selected to delete.", "Error", JOptionPane.ERROR_MESSAGE);
	        return;
	    }

	    try  {
	    	Connection conn = connectionManager.getConnection();
	         CallableStatement stmt = conn.prepareCall("{call deleteReview(?)}");
	        stmt.setInt(1, currentReviewId);
	        stmt.executeUpdate();
	        reviewOutput.setText("");
	        numberOfStarOutput.setText("");
	        
	        JOptionPane.showMessageDialog(this, "Note deleted successfully.");
	        refreshGames();

	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(this, "Error deleting note: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}
	
	
	private void updateReviewInDatabase(int currentReviewId, String newText, String newStar) {
	    if (currentReviewId == -1) {
	        JOptionPane.showMessageDialog(this, "No note selected to update.", "Error", JOptionPane.ERROR_MESSAGE);
	        return;
	    }

	    try {
	    	Connection conn = connectionManager.getConnection();
	        CallableStatement stmt = conn.prepareCall("{call updateReview(?, ?, ?)}");
	    	
	        stmt.setInt(1, currentReviewId);
	        stmt.setString(2, newText);
	        stmt.setString(3, newStar);
	        
	        stmt.executeUpdate();
	        JOptionPane.showMessageDialog(this, "Review updated successfully.");
	        refreshGames();

	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(this, "Error updating review: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}
	
	private void refreshGames() {
		updateManager.redoSearch();
		updateManager.GameBrowserUpdate();
	}

	
	
	


	
	
}
