CREATE USER [gameLibrary] FOR LOGIN [gameLibrary]
GO
ALTER ROLE [db_datareader] ADD MEMBER [gameLibrary]
GO
ALTER ROLE [db_datawriter] ADD MEMBER [gameLibrary]
GO
GRANT EXECUTE ON [dbo].[SearchGames] TO [gameLibrary] AS [dbo]
GO
GRANT EXECUTE ON [dbo].[getAllGames] TO [gameLibrary] AS [dbo]
GO
GRANT EXECUTE ON [dbo].[getPersonalGames] TO [gameLibrary] AS [dbo]
GO