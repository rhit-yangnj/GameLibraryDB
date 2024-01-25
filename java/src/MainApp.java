import gui.ConnectionManager;
import gui.MainViewer;
import gui.UserManager;

public class MainApp {

	public static void main(String[] args) {
		ConnectionManager connectionManager = new ConnectionManager("gameLibraryDB.properties");
		connectionManager.connect();
		MainViewer registerViewer = new MainViewer(connectionManager);
		registerViewer.viewerMain();
	}
	
	

}
