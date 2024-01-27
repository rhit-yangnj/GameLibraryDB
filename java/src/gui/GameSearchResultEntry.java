package gui;

import java.util.ArrayList;

public class GameSearchResultEntry {
	private String gameName;
	private String description;
	private String studioName;
	private ArrayList<String> platformNames;
	private ArrayList<String> genres;
	private String releaseDate;
	
	public GameSearchResultEntry(String gameName, String description, String studioName, String platformName, String genre, String releaseDate) {
		this.gameName = gameName;
		this.description = description;
		this.studioName = studioName;
		this.platformNames = new ArrayList<String>();
		this.platformNames.add(platformName);
		this.genres = new ArrayList<String>();
		this.genres.add(genre);
		this.releaseDate = releaseDate;
	}
	
	public void addPlatform(String platformName) {
		this.platformNames.add(platformName);
	}
	
	public void addGenre(String genre) {
		this.genres.add(genre);
	}

	public String getGameName() {
		return gameName;
	}

	public String getDescription() {
		return description;
	}

	public String getStudioName() {
		return studioName;
	}

	public ArrayList<String> getPlatformNames() {
		return platformNames;
	}

	public ArrayList<String> getGenres() {
		return genres;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

}
