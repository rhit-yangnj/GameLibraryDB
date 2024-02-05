package gui;


public class UpdateManager {
	
	private FullTablePanel ftp;
	private GameEditPanel gep;
	private GameBrowserPanel gbp;
	
	public UpdateManager(FullTablePanel FTP, GameEditPanel GEP, GameBrowserPanel GBP) {
		this.ftp = FTP;
		this.gep = GEP;
		this.gbp = GBP;
	}
	
	public void UserUpdate() {
		this.ftp.UpdateView();
		this.gep.redoPersonalGames();
		this.gep.redoStoredGameList();
		this.gbp.UpdateView();
	}
	
	public void GameUpdate() {
		this.ftp.UpdateView();
	}
	
	public void GameBrowserUpdate() {
		this.gbp.UpdateView();
	}

}