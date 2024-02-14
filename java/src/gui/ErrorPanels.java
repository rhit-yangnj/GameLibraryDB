package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class ErrorPanels{
	private static final int TIME_VISIBLE = 2000;

	public static void createInfoDialogue(String message) {
		JOptionPane pane = new JOptionPane(message,
          JOptionPane.INFORMATION_MESSAGE);
		ErrorPanels.createDialogue(pane);
	}
	
	public static void createWarningDialogue(String message) {
		JOptionPane pane = new JOptionPane(message,
		          JOptionPane.WARNING_MESSAGE);
		ErrorPanels.createDialogue(pane);
	}

	private static void createDialogue(JOptionPane pane) {
		// TODO Auto-generated method stub
		JDialog dialog = pane.createDialog(null, "Title");
	      dialog.setModal(false);
	      dialog.setVisible(true);
	      
	      new Timer(TIME_VISIBLE, new ActionListener() {
	          @Override
	          public void actionPerformed(ActionEvent e) {
	            dialog.setVisible(false);
	            dialog.dispose();
	          }
	        }).start();
	}

	public static void createErrorDialogue(String message) {
		JOptionPane pane = new JOptionPane(message,
		          JOptionPane.ERROR_MESSAGE);
		ErrorPanels.createDialogue(pane);
	}
}