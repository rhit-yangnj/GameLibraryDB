package gui;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;

public class GameSearchResultEntry {
	private String gameName;
	private String description;
	private String studioName;
	private HashSet<String> platformNames;
	private HashSet<String> genres;
	private String releaseDate;
	
	public GameSearchResultEntry(String gameName, String description, String studioName, String platformName, String genre, String releaseDate) {
		this.gameName = gameName;
		this.description = description;
		this.studioName = studioName;
		this.platformNames = new HashSet<String>();
		this.platformNames.add(platformName);
		this.genres = new HashSet<String>();
		this.genres.add(genre);
		this.releaseDate = releaseDate;
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

	public HashSet<String> getPlatformNames() {
		return platformNames;
	}

	public HashSet<String> getGenres() {
		return genres;
	}

	public String getReleaseDate() {
		return releaseDate;
	}
	
	public void addPlatformName(String platformName) {
		this.platformNames.add(platformName);
	}
	
	public void addGenre(String genre) {
		this.genres.add(genre);
	}

}
