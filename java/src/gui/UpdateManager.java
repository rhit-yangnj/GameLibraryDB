package gui;


public class UpdateManager {
	
	private GameBrowserPanel pgbp;
	private GameEditPanel gep;
	private GameBrowserPanel gbp;
	private MainViewer mv;
	private ReviewPanel rp;
	private NotePanel np;
	private SingleGameBrowsingPanel sgbp;
	
	public UpdateManager(GameEditPanel gameScreen, GameBrowserPanel gameBrowserPanel, MainViewer mainViewer, GameBrowserPanel personalGameBrowserPanel, SingleGameBrowsingPanel SGBP) {
		this.pgbp = personalGameBrowserPanel;
		this.gep = gameScreen;
		this.gbp = gameBrowserPanel;
		this.mv = mainViewer ;
		this.sgbp = SGBP;
//		this.rp = reviewPanel;
//		this.np = notePanel;
	}
	
	public void UserUpdate() {
		this.gep.redoPersonalGames();
		this.gep.redoStoredGameList();
		this.gbp.UpdateView();
		this.mv.UserLoginUpdate();
//		this.rp.redoPersonalGames();
		this.pgbp.UpdateView();
		this.sgbp.redoStoredGames();
	}
	
	public void GameBrowserUpdate() {
		this.gbp.UpdateView();
		this.pgbp.UpdateView();
	}

	public void redoSearch() {
		this.gbp.redoSearch();
		this.pgbp.redoSearch();
	}

}