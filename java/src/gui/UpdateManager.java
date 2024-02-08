package gui;


public class UpdateManager {
	
	private GameBrowserPanel pgbp;
	private GameEditPanel gep;
	private GameBrowserPanel gbp;
	private MainViewer mv;
	private ReviewPanel rp;
	private NotePanel np;
	
	public UpdateManager(GameEditPanel gameScreen, GameBrowserPanel gameBrowserPanel, MainViewer mainViewer, NotePanel notePanel, ReviewPanel reviewPanel, GameBrowserPanel personalGameBrowserPanel) {
		this.pgbp = personalGameBrowserPanel;
		this.gep = gameScreen;
		this.gbp = gameBrowserPanel;
		this.mv = mainViewer ;
		this.rp = reviewPanel;
		this.np = notePanel;
	}
	
	public void UserUpdate() {
		this.gep.redoPersonalGames();
		this.gep.redoStoredGameList();
		this.gbp.UpdateView();
		this.mv.UserLoginUpdate();
		this.rp.redoPersonalGames();
		this.pgbp.UpdateView();
	}
	
	public void GameUpdate() {
		this.gbp.UpdateView();
		this.pgbp.UpdateView();
		this.rp.redoPersonalGames();
		this.np.redoPersonalGames();
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