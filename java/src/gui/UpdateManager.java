package gui;


public class UpdateManager {
	
	private FullTablePanel ftp;
	private GameEditPanel gep;
	private GameBrowserPanel gbp;
	private MainViewer mv;
	
	public UpdateManager(FullTablePanel FTP, GameEditPanel GEP, GameBrowserPanel GBP, MainViewer MV) {
		this.ftp = FTP;
		this.gep = GEP;
		this.gbp = GBP;
		this.mv = MV;
	}
	
	public void UserUpdate() {
		this.ftp.UpdateView();
		this.gep.redoPersonalGames();
		this.gep.redoStoredGameList();
		this.gbp.UpdateView();
		this.mv.UserLoginUpdate();
	}
	
	public void GameUpdate() {
		this.ftp.UpdateView();
	}
	
	public void GameBrowserUpdate() {
		this.gbp.UpdateView();
	}

}