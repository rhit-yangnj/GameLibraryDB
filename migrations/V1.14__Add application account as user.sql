CREATE USER [gameLibrary] FOR LOGIN [gameLibrary]
GO
ALTER ROLE [db_datareader] ADD MEMBER [gameLibrary]
GO
USE [GameLibraryDB]
GO
ALTER ROLE [db_datawriter] ADD MEMBER [gameLibrary]
GO
