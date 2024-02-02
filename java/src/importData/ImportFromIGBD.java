package importData;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections4.bag.SynchronizedSortedBag;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.api.igdb.apicalypse.APICalypse;
import com.api.igdb.exceptions.RequestException;
import com.api.igdb.request.IGDBWrapper;
import com.api.igdb.request.ProtoRequestKt;
import com.api.igdb.request.TwitchAuthenticator;
import com.api.igdb.utils.TwitchToken;
import com.google.protobuf.Timestamp;

import gui.ConnectionManager;
import proto.Company;
import proto.CompanyWebsite;
import proto.Game;
import proto.Genre;
import proto.InvolvedCompany;
import proto.Platform;
import proto.PlatformWebsite;
import proto.Website;

public class ImportFromIGBD {
	
	private List<Game> games;
	private Connection connection = null;
	private String serverName;
	private String databaseName;
	private String serverUsername;
	private String serverPassword;
	
	public static void main( String[] args )
    {
		ImportFromIGBD igdbImporter = new ImportFromIGBD();
		ConnectionManager connectionManager = new ConnectionManager("gameLibraryDB.properties");

		if (connectionManager.connect()) {
			igdbImporter.connection = connectionManager.getConnection();
			
			igdbImporter.requestGames(10);
			igdbImporter.importGames();
			
			connectionManager.closeConnection();
		} else {
			System.out.println("Failed to connect to the database.");
		}
        
    }
	
	public void importGames() {
        try {
        	long start = System.currentTimeMillis();
        	Iterator<Game> gameIterator = games.iterator();
			connection.setAutoCommit(false);

			CallableStatement addStudioStmt = connection.prepareCall("{? = call addStudio(?,?)}");
			CallableStatement addGameStmt = connection.prepareCall("{? = call addGame(?,?,?)}");
			CallableStatement addPlatformStmt = connection.prepareCall("{? = call addPlatform(?,?)}");
			CallableStatement addGenreStmt = connection.prepareCall("{? = call addGenre(?)}");
			CallableStatement addGameToPlatformStmt = connection.prepareCall("{? = call addGameToPlatform(?,?)}");
			CallableStatement addStudioToPlatformStmt = connection.prepareCall("{? = call addStudioToPlatform(?,?)}");
			CallableStatement addGameToGenreStmt = connection.prepareCall("{? = call addGameToGenre(?,?)}");
			CallableStatement addGameToStudioStmt = connection.prepareCall("{? = call addGameToStudio(?,?)}");
			addStudioStmt.registerOutParameter(1, Types.INTEGER);
			addGameStmt.registerOutParameter(1, Types.INTEGER);
			addPlatformStmt.registerOutParameter(1, Types.INTEGER);
			addGenreStmt.registerOutParameter(1, Types.INTEGER);
			addGameToPlatformStmt.registerOutParameter(1, Types.INTEGER);
			addStudioToPlatformStmt.registerOutParameter(1, Types.INTEGER);
			addGameToGenreStmt.registerOutParameter(1, Types.INTEGER);
			addGameToStudioStmt.registerOutParameter(1, Types.INTEGER);
	        
	        while (gameIterator.hasNext()) {
                Game nextGame = gameIterator.next();
                String gameName = null;
                java.util.Date releaseUtilDate = null;
                java.sql.Date releaseSQLDate = null;
                String description = null;
                List<Platform> platformList = null;
                List<InvolvedCompany> studioList = null;
                List<Genre> genreList = null;
 
                gameName = nextGame.getName();
                Timestamp releaseTimestamp = nextGame.getFirstReleaseDate();
//                Instant releaseInstant = Instant.ofEpochSecond(releaseTimestamp.getSeconds(), releaseTimestamp.getNanos());
//                releaseUtilDate = java.util.Date.from(releaseInstant);
                releaseSQLDate = new java.sql.Date(releaseTimestamp.getSeconds() * 1000);
                description = nextGame.getSummary();
                platformList = nextGame.getPlatformsList();
                studioList = nextGame.getInvolvedCompaniesList();
                genreList = nextGame.getGenresList();
                
//                System.out.println("Name: " + gameName + "\nRelease Date: " + releaseDate + "\nDescription: " + description + "\n Platform List: " + platformList + "\nStudio List: " + studioList + "\nGenre List: " + genreList + "\n------");
                
                //Attempt addGame
                try {
                	addGameStmt.setString(2, gameName);
                    addGameStmt.setDate(3, releaseSQLDate);
                    addGameStmt.setString(4, description);
                	addGameStmt.executeUpdate();
				} catch (SQLException e) {
					int returnValue = addGameStmt.getInt(1);
                	if (returnValue == 1) {
                		System.out.println("Game name was null, skipping...");
                	} else if (returnValue == 2) {
                		System.out.println("Game with name " + gameName + " already exists, skipping...");
                	}
				}
                
                //Attempt addStudio
                addGameToStudioStmt.setString(2, gameName);
            	for (InvolvedCompany studioID : studioList) {
            		Company studio = requestStudio(studioID.getId());
            		if (studio != null) {
	            		try {
	            			addStudioStmt.setString(2, studio.getName());
	            			if (studio.getWebsitesList().size() > 0) {
	            				CompanyWebsite website = requestStudioWebsite(studio.getWebsites(0).getId());
	            				addStudioStmt.setString(3, website.getUrl());
	            			} else {
	            				addStudioStmt.setNull(3, Types.VARCHAR);
	            			}
	    	                addStudioStmt.executeUpdate();
	    	                
	    	                addGameToStudioStmt.setString(3, studio.getName());
	    	                addGameToStudioStmt.executeUpdate();
	            		} catch (SQLException e) {
	            			e.printStackTrace();
	                    	int returnValue = addStudioStmt.getInt(1);
	                    	if (returnValue == 1) {
	                    		System.out.println("Studio name was null, skipping...");
	                    	} else if (returnValue == 2) {
	                    		System.out.println("Studio with name " + studio.getName() + " already exists, skipping...");
	                    	}
	                    	
	                    	returnValue = addGameToStudioStmt.getInt(1);
	                    	if (returnValue == 1) {
	    						System.out.println("Game name was null, skipping...");
	    					} else if (returnValue == 2) {
	    						System.out.println("Studio name was null, skipping...");
	    					} else if (returnValue == 3) {
	    						System.out.println("Could not find a game with name " + gameName);
	    					} else if (returnValue == 4) {
	    						System.out.println("Could not find a studio with name " + studio.getName());
	    					}
	                    }
            		}
            	}
               
                //Attempt addPlatform
                addGameToPlatformStmt.setString(2, gameName);
                for (Platform platformID : platformList) {
                	Platform platform = requestPlatform(platformID.getId());
            		try {
            			addPlatformStmt.setString(2, platform.getName());
            			if (platform.getWebsitesList().size() > 0) {
            				PlatformWebsite website = requestPlatformWebsite(platform.getWebsites(0).getId());
            				addPlatformStmt.setString(3, website.getUrl());
            			} else {
            				addPlatformStmt.setNull(3, Types.VARCHAR);
            			}
    	                addPlatformStmt.executeUpdate();
    	                
    	                addGameToPlatformStmt.setString(3, platform.getName());
                    	addGameToPlatformStmt.executeUpdate();
            		} catch (SQLException e) {
            			e.printStackTrace();
//                    	int returnValue = addPlatformStmt.getInt(1);
//                    	if (returnValue == 1) {
//                    		System.out.println("Platform name was null, skipping...");
//                    	} else if (returnValue == 2) {
//                    		System.out.println("Platform with name " + platform.getName() + " already exists, skipping...");
//                    	}
//                    	
//                    	returnValue = addGameToPlatformStmt.getInt(1);
//    					if (returnValue == 1) {
//    						System.out.println("Game name was null, skipping...");
//    					} else if (returnValue == 2) {
//    						System.out.println("Platform name was null, skipping...");
//    					} else if (returnValue == 3) {
//    						System.out.println("Could not find a game with name " + gameName);
//    					} else if (returnValue == 4) {
//    						System.out.println("Could not find a platform with name " + platform.getName());
//    					}
                    }
            	}
                
                //Attempt addGenre
                addGameToGenreStmt.setString(2, gameName);
                for (Genre genreID : genreList) {
                	Genre genre = requestGenre(genreID.getId());
            		try {
            			addGenreStmt.setString(2, genre.getName());
    	                addGenreStmt.executeUpdate();
    	                
                    	addGameToGenreStmt.setString(3, genre.getName());
                    	addGameToGenreStmt.executeUpdate();
            		} catch (SQLException e) {
                    	int returnValue = addGenreStmt.getInt(1);
                    	if (returnValue == 1) {
                    		System.out.println("Genre name was null, skipping...");
                    	} else if (returnValue == 2) {
                    		System.out.println("Genre with name " + genre.getName() + " already exists, skipping...");
                    	}
                    	
                    	returnValue = addGameToGenreStmt.getInt(1);
    					if (returnValue == 1) {
    						System.out.println("Game name was null, skipping...");
    					} else if (returnValue == 2) {
    						System.out.println("Genre name was null, skipping...");
    					} else if (returnValue == 3) {
    						System.out.println("Could not find a game with name " + gameName);
    					} else if (returnValue == 4) {
    						System.out.println("Could not find a genre with name " + genre.getName());
    					}
            		}	
            	}
                
                //Attempt addGameToPlatform
//                i = 0;
//                addGameToPlatformStmt.setString(2, gameName);
//                while (i < platformNameList.size())
//                try {
//                	platformName = platformNameList.get(i);
//                	addGameToPlatformStmt.setString(3, platformName);
//                	addGameToPlatformStmt.executeUpdate();
//                	i++;
//				} catch (SQLException e) {
//					int returnValue = addGameToPlatformStmt.getInt(1);
//					if (returnValue == 1) {
//						System.out.println("Game name was null, skipping...");
//					} else if (returnValue == 2) {
//						System.out.println("Platform name was null, skipping...");
//					} else if (returnValue == 3) {
//						System.out.println("Could not find a game with name " + gameName);
//					} else if (returnValue == 4) {
//						System.out.println("Could not find a platform with name " + platformName);
//					}
//					i++;
//				}
                
                //Attempt addStudioToPlatform
//                i = 0;
//                addStudioToPlatformStmt.setString(2, studioName);
//                while (i < platformNameList.size())
//                try {
//                	platformName = platformNameList.get(i);
//                	addStudioToPlatformStmt.setString(3, platformName);
//                	addStudioToPlatformStmt.executeUpdate();
//                	i++;
//				} catch (SQLException e) {
//					int returnValue = addStudioToPlatformStmt.getInt(1);
//					if (returnValue == 1) {
//						System.out.println("Studio name was null, skipping...");
//					} else if (returnValue == 2) {
//						System.out.println("Platform name was null, skipping...");
//					} else if (returnValue == 3) {
//						System.out.println("Could not find a studio with name " + studioName);
//					} else if (returnValue == 4) {
//						System.out.println("Could not find a platform with name " + platformName);
//					}
//					i++;
//				}
                
                for (InvolvedCompany studioID : studioList) {
                	Company studio = requestStudio(studioID.getId());
                	if (studio != null) {
	            		for (Platform platformID : platformList) {
	            			Platform platform = requestPlatform(platformID.getId());
	            			try {
	            				addStudioToPlatformStmt.setString(2, studio.getName());
	                			addStudioToPlatformStmt.setString(3, platform.getName());
	                			addStudioToPlatformStmt.executeUpdate();
	            			} catch (SQLException e) {
	            				e.printStackTrace();
	            				int returnValue = addStudioToPlatformStmt.getInt(1);
	        					if (returnValue == 1) {
	        						System.out.println("Studio name was null, skipping...");
	        					} else if (returnValue == 2) {
	        						System.out.println("Platform name was null, skipping...");
	        					} else if (returnValue == 3) {
	        						System.out.println("Could not find a studio with name " + studio.getName());
	        					} else if (returnValue == 4) {
	        						System.out.println("Could not find a platform with name " + platform.getName());
	        					}
	            			}
	            		}
                	}
                }
                
                //Attempt addGameToGenre
//                i = 0;
//                addGameToGenreStmt.setString(2, gameName);
//                while (i < genreList.size())
//                    try {
//                    	genre = genreList.get(i);
//                    	addGameToGenreStmt.setString(3, genre);
//                    	addGameToGenreStmt.executeUpdate();
//                    	i++;
//    				} catch (SQLException e) {
//    					int returnValue = addGameToGenreStmt.getInt(1);
//    					if (returnValue == 1) {
//    						System.out.println("Game name was null, skipping...");
//    					} else if (returnValue == 2) {
//    						System.out.println("Genre name was null, skipping...");
//    					} else if (returnValue == 3) {
//    						System.out.println("Could not find a game with name " + gameName);
//    					} else if (returnValue == 4) {
//    						System.out.println("Could not find a genre with name " + genre);
//    					}
//    					i++;
//    				}
// 
            }

            connection.commit();
             
            long end = System.currentTimeMillis();
            System.out.printf("Import done in %d ms\n", (end - start));
            
        } catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void requestGames(int amount) {
		TwitchAuthenticator tAuth = TwitchAuthenticator.INSTANCE;
        TwitchToken token = tAuth.requestTwitchToken("2jjrqaw46pfhnnmrb2e5tf1f6rd6et", "fn4lep8xs6okv0oxakjd16kop0eoi3");
        
        IGDBWrapper wrapper = IGDBWrapper.INSTANCE;
        wrapper.setCredentials("2jjrqaw46pfhnnmrb2e5tf1f6rd6et", token.getAccess_token());
        APICalypse apicalypse = new APICalypse().fields("*").limit(amount);
        games = new ArrayList<Game>();
        try{
        	System.out.println("Attempting to request games from IGDB");
        	games = ProtoRequestKt.games(wrapper, apicalypse);
        	return;
        } catch(RequestException e) {
        	e.printStackTrace();
        	games = null;
        	return;
        }
	}
	
	private Genre requestGenre(long id) {
		TwitchAuthenticator tAuth = TwitchAuthenticator.INSTANCE;
        TwitchToken token = tAuth.requestTwitchToken("2jjrqaw46pfhnnmrb2e5tf1f6rd6et", "fn4lep8xs6okv0oxakjd16kop0eoi3");
        
        IGDBWrapper wrapper = IGDBWrapper.INSTANCE;
        wrapper.setCredentials("2jjrqaw46pfhnnmrb2e5tf1f6rd6et", token.getAccess_token());
        APICalypse apicalypse = new APICalypse().fields("*").where("id = " + id);
        try{
        	System.out.println("Attempting to request genre from IGDB");
        	List<Genre> genres = ProtoRequestKt.genres(wrapper, apicalypse);
        	for (Genre genre : genres) {
        		return genre;
        	}
        } catch(RequestException e) {
        	e.printStackTrace();
        	return null;
        }
		return null;
	}

	private Platform requestPlatform(long id) {
		TwitchAuthenticator tAuth = TwitchAuthenticator.INSTANCE;
	    TwitchToken token = tAuth.requestTwitchToken("2jjrqaw46pfhnnmrb2e5tf1f6rd6et", "fn4lep8xs6okv0oxakjd16kop0eoi3");
	    
	    IGDBWrapper wrapper = IGDBWrapper.INSTANCE;
	    wrapper.setCredentials("2jjrqaw46pfhnnmrb2e5tf1f6rd6et", token.getAccess_token());
	    APICalypse apicalypse = new APICalypse().fields("*").where("id = " + id);
	    try{
	    	System.out.println("Attempting to request platform from IGDB");
	    	List<Platform> platforms = ProtoRequestKt.platforms(wrapper, apicalypse);
	    	for (Platform platform : platforms) {
	    		return platform;
	    	}
	    } catch(RequestException e) {
	    	e.printStackTrace();
	    	return null;
	    }
		return null;
	}
	
	private Company requestStudio(long id) {
		TwitchAuthenticator tAuth = TwitchAuthenticator.INSTANCE;
	    TwitchToken token = tAuth.requestTwitchToken("2jjrqaw46pfhnnmrb2e5tf1f6rd6et", "fn4lep8xs6okv0oxakjd16kop0eoi3");
	    
	    IGDBWrapper wrapper = IGDBWrapper.INSTANCE;
	    wrapper.setCredentials("2jjrqaw46pfhnnmrb2e5tf1f6rd6et", token.getAccess_token());
	    APICalypse apicalypse = new APICalypse().fields("*").where("id = " + id);
	    try{
	    	System.out.println("Attempting to request studio from IGDB");
	    	List<InvolvedCompany> involvedCompanies = ProtoRequestKt.involvedCompanies(wrapper, apicalypse);
	    	for (InvolvedCompany involvedCompany : involvedCompanies) {
	    		if (involvedCompany.getDeveloper()) {
		    		apicalypse = new APICalypse().fields("*").where("id = " + involvedCompany.getCompany().getId());
		    		List<Company> companies = ProtoRequestKt.companies(wrapper, apicalypse);
		    		for (Company company : companies) {
		    			return company;
		    		}
	    		}
	    	}
	    } catch(RequestException e) {
	    	e.printStackTrace();
	    	return null;
	    }
		return null;
	}
	
	private PlatformWebsite requestPlatformWebsite(long id) {
		TwitchAuthenticator tAuth = TwitchAuthenticator.INSTANCE;
	    TwitchToken token = tAuth.requestTwitchToken("2jjrqaw46pfhnnmrb2e5tf1f6rd6et", "fn4lep8xs6okv0oxakjd16kop0eoi3");
	    
	    IGDBWrapper wrapper = IGDBWrapper.INSTANCE;
	    wrapper.setCredentials("2jjrqaw46pfhnnmrb2e5tf1f6rd6et", token.getAccess_token());
	    APICalypse apicalypse = new APICalypse().fields("*").where("id = " + id);
	    try{
	    	System.out.println("Attempting to request platform website from IGDB");
	    	List<PlatformWebsite> platformWebsites = ProtoRequestKt.platformWebsites(wrapper, apicalypse);
	    	for (PlatformWebsite platformWebsite : platformWebsites) {
	    		return platformWebsite;
	    	}
	    } catch(RequestException e) {
	    	e.printStackTrace();
	    	return null;
	    }
		return null;
	}
	
	private CompanyWebsite requestStudioWebsite(long id) {
		TwitchAuthenticator tAuth = TwitchAuthenticator.INSTANCE;
	    TwitchToken token = tAuth.requestTwitchToken("2jjrqaw46pfhnnmrb2e5tf1f6rd6et", "fn4lep8xs6okv0oxakjd16kop0eoi3");
	    
	    IGDBWrapper wrapper = IGDBWrapper.INSTANCE;
	    wrapper.setCredentials("2jjrqaw46pfhnnmrb2e5tf1f6rd6et", token.getAccess_token());
	    APICalypse apicalypse = new APICalypse().fields("*").where("id = " + id);
	    try{
	    	System.out.println("Attempting to request studio website from IGDB");
	    	List<CompanyWebsite> companyWebsites = ProtoRequestKt.companyWebsites(wrapper, apicalypse);
	    	for (CompanyWebsite companyWebsite : companyWebsites) {
	    		return companyWebsite;
	    	}
	    } catch(RequestException e) {
	    	e.printStackTrace();
	    	return null;
	    }
		return null;
	}
}
