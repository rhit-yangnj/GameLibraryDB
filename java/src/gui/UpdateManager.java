package gui;


public class UpdateManager {
	
	private FullTablePanel ftp;
	private GameEditPanel gep;
	private GameBrowserPanel gbp;
	private MainViewer mv;
	private ReviewPanel rp;
	private NotePanel np;
	
	public UpdateManager(FullTablePanel FTP, GameEditPanel GEP, GameBrowserPanel GBP, MainViewer MV, NotePanel NP, ReviewPanel RP) {
		this.ftp = FTP;
		this.gep = GEP;
		this.gbp = GBP;
		this.mv = MV;
		this.rp = RP;
		this.np = NP;
	}
	
	public void UserUpdate() {
		this.ftp.UpdateView();
		this.gep.redoPersonalGames();
		this.gep.redoStoredGameList();
		this.gbp.UpdateView();
		this.mv.UserLoginUpdate();
		this.rp.redoPersonalGames();
	}
	
	public void GameUpdate() {
		this.ftp.UpdateView();
		this.rp.redoPersonalGames();
		this.np.redoPersonalGames();
	}
	
	public void GameBrowserUpdate() {
		this.gbp.UpdateView();
	}

}