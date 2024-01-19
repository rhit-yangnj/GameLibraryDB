package importData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ImportFromFile {
	
	private final String URL = "jdbc:sqlserver://${dbServer};databaseName=${dbName};user=${user};password={${pass}}";

	private String filePath = "C:\\Users\\yangnj\\Documents\\CSSE333\\GameLibraryDB\\java\\src\\Sample Data.xlsx";
	private Connection connection = null;
	
	private String serverName;
	private String databaseName;
	private String serverUsername;
	private String serverPassword;
	
	private SimpleDateFormat fromUser = new SimpleDateFormat("MM/dd/yyyy");
	private SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	public static void main(String[] args) {
		ImportFromFile fileImporter = new ImportFromFile();
		fileImporter.importFile();
	}
	
	public ImportFromFile() {
		try {
			loadProperties("gameLibraryDB.properties");
		} catch (IOException e) {
			System.out.println("Could not find properties file");
			e.printStackTrace();
		}
	}
	
	public void importFile() {
		
		int batchSize = 20;
        
        try {
        	long start = System.currentTimeMillis();
        	
        	FileInputStream inputStream = new FileInputStream(filePath);
    		Workbook workbook = new XSSFWorkbook(inputStream);
			Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = firstSheet.iterator();
            
			connection = DriverManager.getConnection(makeURL());
			connection.setAutoCommit(false);

			CallableStatement addStudioStmt = connection.prepareCall("{? = call addStudio(?,null)}");
			CallableStatement addGameStmt = connection.prepareCall("{? = call addGame(?,?,?,?)}");
			CallableStatement addPlatformStmt = connection.prepareCall("{? = call addPlatform(?,null)}");
			CallableStatement addGenreStmt = connection.prepareCall("{? = call addGenre(?)}");
			CallableStatement addGameToPlatformStmt = connection.prepareCall("{? = call addGameToPlatform(?,?)}");
			CallableStatement addStudioToPlatformStmt = connection.prepareCall("{? = call addStudioToPlatform(?,?)}");
			CallableStatement addGameToGenreStmt = connection.prepareCall("{? = call addGameToGenre(?,?)}");
			addStudioStmt.registerOutParameter(1, Types.INTEGER);
			addGameStmt.registerOutParameter(1, Types.INTEGER);
			addPlatformStmt.registerOutParameter(1, Types.INTEGER);
			addGenreStmt.registerOutParameter(1, Types.INTEGER);
			addGameToPlatformStmt.registerOutParameter(1, Types.INTEGER);
			addStudioToPlatformStmt.registerOutParameter(1, Types.INTEGER);
			addGameToGenreStmt.registerOutParameter(1, Types.INTEGER);
	         
	        int count = 0;
	        
	        rowIterator.next();
	        
	        while (rowIterator.hasNext()) {
                Row nextRow = rowIterator.next();
                Iterator<Cell> cellIterator = nextRow.cellIterator();
                
                String gameName = null;
                Date releaseDate = null;
                String description = null;
                String platformNameListString = null;
                String studioName = null;
                String genreListString = null;
 
                while (cellIterator.hasNext()) {
                    Cell nextCell = cellIterator.next();
 
                    int columnIndex = nextCell.getColumnIndex();
 
                    switch (columnIndex) {
                    case 0:
                        gameName = nextCell.getStringCellValue();
                        break;
                    case 1:
//                    	String reformattedStr = null;
//                    	try {
//                    	    reformattedStr = myFormat.format(fromUser.parse(nextCell.getStringCellValue()));
//                    	} catch (ParseException e) {
//                    	    e.printStackTrace();
//                    	}
//                        releaseDate = Date.valueOf(reformattedStr);
                    	releaseDate = new java.sql.Date(nextCell.getDateCellValue().getTime());
                    	break;
                    case 2:
                    	description = nextCell.getStringCellValue();
                    	break;
                    case 3:
                    	platformNameListString = nextCell.getStringCellValue();
                    	break;
                    case 4:
                    	studioName = nextCell.getStringCellValue();
                    	break;
                    case 5:
                    	genreListString = nextCell.getStringCellValue();
                    	break;
                    }
                    
                    
 
                }
                List<String> platformNameList = Arrays.asList(platformNameListString.split(",\\s*"));
                List<String> genreList = Arrays.asList(genreListString.split(",\\s*"));

                
                //Attempt addStudio
                try {
                	addStudioStmt.setString(2, studioName);
	                addStudioStmt.executeUpdate();
                } catch (SQLException e) {
                	int returnValue = addStudioStmt.getInt(1);
                	if (returnValue == 1) {
                		System.out.println("Studio name was null, skipping...");
                	} else if (returnValue == 2) {
                		System.out.println("Studio with name " + studioName + " already exists, skipping...");
                	}
                }
                //Attempt addGame
                try {
                	addGameStmt.setString(2, gameName);
                    addGameStmt.setDate(3, releaseDate);
                    addGameStmt.setString(4, description);
                    addGameStmt.setString(5, studioName);
                	addGameStmt.executeUpdate();
				} catch (SQLException e) {
					int returnValue = addGameStmt.getInt(1);
                	if (returnValue == 1) {
                		System.out.println("Game name was null, skipping...");
                	} else if (returnValue == 2) {
                		System.out.println("Game with name " + gameName + " already exists, skipping...");
                	}
				}
               
                String platformName = null;
                int i = 0;
                //Attempt addPlatform
                while (i < platformNameList.size()) {
	                try {
	                	platformName = platformNameList.get(i);
	                	addPlatformStmt.setString(2, platformName);
	                	addPlatformStmt.executeUpdate();
	                	i++;
					} catch (SQLException e) {
						int returnValue = addPlatformStmt.getInt(1);
						if (returnValue == 1) {
	                		System.out.println("Platform name was null, skipping...");
	                	} else if (returnValue == 2) {
	                		System.out.println("Platform with name " + platformName + " already exists, skipping...");
	                	}
						i++;
					}
                }
                //Attempt addGenre
                String genre = null;
                i = 0;
                while (i < genreList.size())
                try {
                	genre = genreList.get(i);
                	addGenreStmt.setString(2, genre);
                	addGenreStmt.executeUpdate();
                	i++;
				} catch (SQLException e) {
					int returnValue = addGenreStmt.getInt(1);
					if (returnValue == 1) {
                		System.out.println("Genre name was null, skipping...");
                	} else if (returnValue == 2) {
                		System.out.println("Genre with name " + genre + " already exists, skipping...");
                	}
					i++;
				}
                //Attempt addGameToPlatform
                i = 0;
                addGameToPlatformStmt.setString(2, gameName);
                while (i < platformNameList.size())
                try {
                	platformName = platformNameList.get(i);
                	addGameToPlatformStmt.setString(3, platformName);
                	addGameToPlatformStmt.executeUpdate();
                	i++;
				} catch (SQLException e) {
					int returnValue = addGameToPlatformStmt.getInt(1);
					if (returnValue == 1) {
						System.out.println("Game name was null, skipping...");
					} else if (returnValue == 2) {
						System.out.println("Platform name was null, skipping...");
					} else if (returnValue == 3) {
						System.out.println("Could not find a game with name " + gameName);
					} else if (returnValue == 4) {
						System.out.println("Could not find a platform with name " + platformName);
					}
					i++;
				}
                //Attempt addStudioToPlatform
                i = 0;
                addStudioToPlatformStmt.setString(2, studioName);
                while (i < platformNameList.size())
                try {
                	platformName = platformNameList.get(i);
                	addStudioToPlatformStmt.setString(3, platformName);
                	addStudioToPlatformStmt.executeUpdate();
                	i++;
				} catch (SQLException e) {
					int returnValue = addStudioToPlatformStmt.getInt(1);
					if (returnValue == 1) {
						System.out.println("Studio name was null, skipping...");
					} else if (returnValue == 2) {
						System.out.println("Platform name was null, skipping...");
					} else if (returnValue == 3) {
						System.out.println("Could not find a studio with name " + studioName);
					} else if (returnValue == 4) {
						System.out.println("Could not find a platform with name " + platformName);
					}
					i++;
				}
                //Attempt addGameToGenre
                i = 0;
                addGameToGenreStmt.setString(2, gameName);
                while (i < genreList.size())
                    try {
                    	genre = genreList.get(i);
                    	addGameToGenreStmt.setString(3, genre);
                    	addGameToGenreStmt.executeUpdate();
                    	i++;
    				} catch (SQLException e) {
    					int returnValue = addGameToGenreStmt.getInt(1);
    					if (returnValue == 1) {
    						System.out.println("Game name was null, skipping...");
    					} else if (returnValue == 2) {
    						System.out.println("Genre name was null, skipping...");
    					} else if (returnValue == 3) {
    						System.out.println("Could not find a game with name " + gameName);
    					} else if (returnValue == 4) {
    						System.out.println("Could not find a genre with name " + genre);
    					}
    					i++;
    				}
 
            }
 
            workbook.close();
  
            connection.commit();
            closeConnection();
             
            long end = System.currentTimeMillis();
            System.out.printf("Import done in %d ms\n", (end - start));
            
        } catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private String makeURL() {
		return URL.replace("${dbServer}", serverName)
				.replace("${dbName}", databaseName)
				.replace("${user}", serverUsername)
				.replace("${pass}", serverPassword)
				+ ";encrypt=true" 
				+ ";trustServerCertificate=true";
	}
	
	public void closeConnection() {
		try {
			if (this.connection != null && !this.connection.isClosed()) {
				this.connection.close();
			}
		} catch (SQLException e) {
			//Do nothing
		}
	}
	
	private void loadProperties(String propertiesFilePath) throws IOException {
		Properties prop = readPropertiesFile(propertiesFilePath);
	    this.serverName = prop.getProperty("serverName");
	    this.databaseName = prop.getProperty("databaseName");
	    this.serverUsername = prop.getProperty("serverUsername");
	    this.serverPassword = prop.getProperty("serverPassword");
	}
	
	private Properties readPropertiesFile(String propertiesFilePath) throws IOException {
	InputStream stream = null;
	Properties prop = null;
	try {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();           
		stream = loader.getResourceAsStream(propertiesFilePath);
		prop = new Properties();
	    prop.load(stream);
	} catch(FileNotFoundException fnfe) {
	    fnfe.printStackTrace();
	} catch(IOException ioe) {
	    ioe.printStackTrace();
	} finally {
	    stream.close();
	}
	  return prop;
	}
}
