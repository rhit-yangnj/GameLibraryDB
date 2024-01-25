package gui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.sql.Array;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class AddGamePanel extends JPanel {
	JPanel textPanel = new JPanel();
	JPanel buttonPanel = new JPanel();

	JComboBox<String> GameList; 
	JButton submitButton = new JButton("Submit");
	
	public AddGamePanel() {
		GameList = new JComboBox<String>(getAllStoredGames());
		GameList.setEditable(true);
			
		textPanel.add(GameList, BorderLayout.NORTH);
		buttonPanel.add(submitButton);
		
		this.add(textPanel);
		this.add(buttonPanel);
	}
	
	private String[] getAllStoredGames() {
		//TODO actually implement some request command to get all the games in the list
		String[] current = {"None", "Doom", "Baloons Tower Defence 5"};
		return current;
	}
}