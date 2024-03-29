USE [master]
GO

DROP DATABASE [GameLibraryDB_testDB]
GO

/****** Object:  Database [GameLibraryDB_yangnj]    Script Date: 1/18/2024 10:16:16 PM ******/
CREATE DATABASE [GameLibraryDB_testdb]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'GameLibraryDB_testdb', FILENAME = N'/var/opt/mssql/data/GameLibraryDB_testdb.mdf' , SIZE = 8192KB , MAXSIZE = 102400KB , FILEGROWTH = 10%)
 LOG ON 
( NAME = N'GameLibraryDB_testdb_log', FILENAME = N'/var/opt/mssql/data/GameLibraryDB_testdb_log.ldf' , SIZE = 1024KB , MAXSIZE = 102400KB , FILEGROWTH = 10%)
 WITH CATALOG_COLLATION = DATABASE_DEFAULT, LEDGER = OFF
GO

USE [GameLibraryDB_testdb]
GO
/****** Object:  Table [dbo].[flyway_schema_history]    Script Date: 1/18/2024 10:16:22 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[flyway_schema_history](
	[installed_rank] [int] NOT NULL,
	[version] [nvarchar](50) NULL,
	[description] [nvarchar](200) NULL,
	[type] [nvarchar](20) NOT NULL,
	[script] [nvarchar](1000) NOT NULL,
	[checksum] [int] NULL,
	[installed_by] [nvarchar](100) NOT NULL,
	[installed_on] [datetime] NOT NULL,
	[execution_time] [int] NOT NULL,
	[success] [bit] NOT NULL,
 CONSTRAINT [flyway_schema_history_pk] PRIMARY KEY CLUSTERED 
(
	[installed_rank] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Game]    Script Date: 1/18/2024 10:16:22 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Game](
	[Name] [varchar](50) NOT NULL,
	[ReleaseDate] [date] NOT NULL,
	[Description] [varchar](400) NULL,
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[Studio] [int] NOT NULL,
 CONSTRAINT [PK_Game] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[GameHasGenre]    Script Date: 1/18/2024 10:16:22 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[GameHasGenre](
	[GameID] [int] NOT NULL,
	[GenreName] [nvarchar](50) NOT NULL,
 CONSTRAINT [PK_GameHasGenre] PRIMARY KEY CLUSTERED 
(
	[GameID] ASC,
	[GenreName] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[GameOnPlatform]    Script Date: 1/18/2024 10:16:22 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[GameOnPlatform](
	[Game] [int] NOT NULL,
	[Platform] [int] NOT NULL,
 CONSTRAINT [PK_GameOnPlatform] PRIMARY KEY CLUSTERED 
(
	[Game] ASC,
	[Platform] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Genre]    Script Date: 1/18/2024 10:16:22 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Genre](
	[Name] [nvarchar](50) NOT NULL,
 CONSTRAINT [PK_Genre] PRIMARY KEY CLUSTERED 
(
	[Name] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Notes]    Script Date: 1/18/2024 10:16:22 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Notes](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[text] [varchar](1000) NULL,
	[GameID] [int] NOT NULL,
 CONSTRAINT [PK_Notes] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Platform]    Script Date: 1/18/2024 10:16:22 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Platform](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](50) NULL,
	[Website] [varchar](75) NULL,
 CONSTRAINT [PK_Platform] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Reviews]    Script Date: 1/18/2024 10:16:22 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Reviews](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[Text] [varchar](500) NULL,
	[NumberOfStars] [smallint] NOT NULL,
	[GameID] [int] NOT NULL,
 CONSTRAINT [PK_Reviews] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Studio]    Script Date: 1/18/2024 10:16:22 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Studio](
	[Name] [varchar](50) NOT NULL,
	[Website] [varchar](75) NULL,
	[ID] [int] IDENTITY(1,1) NOT NULL,
 CONSTRAINT [PK_Studio] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[StudioOnPlatform]    Script Date: 1/18/2024 10:16:22 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[StudioOnPlatform](
	[Studio] [int] NOT NULL,
	[Platform] [int] NOT NULL,
 CONSTRAINT [PK_StudioOnPlatform] PRIMARY KEY CLUSTERED 
(
	[Studio] ASC,
	[Platform] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Index [flyway_schema_history_s_idx]    Script Date: 1/18/2024 10:16:22 PM ******/
CREATE NONCLUSTERED INDEX [flyway_schema_history_s_idx] ON [dbo].[flyway_schema_history]
(
	[success] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
ALTER TABLE [dbo].[flyway_schema_history] ADD  DEFAULT (getdate()) FOR [installed_on]
GO
ALTER TABLE [dbo].[Game]  WITH CHECK ADD  CONSTRAINT [FK_Game_Studio] FOREIGN KEY([Studio])
REFERENCES [dbo].[Studio] ([ID])
GO
ALTER TABLE [dbo].[Game] CHECK CONSTRAINT [FK_Game_Studio]
GO
ALTER TABLE [dbo].[Notes]  WITH CHECK ADD  CONSTRAINT [FK_Notes_Game] FOREIGN KEY([Id])
REFERENCES [dbo].[Game] ([ID])
GO
ALTER TABLE [dbo].[Notes] CHECK CONSTRAINT [FK_Notes_Game]
GO
ALTER TABLE [dbo].[Reviews]  WITH CHECK ADD  CONSTRAINT [FK_Reviews_Game] FOREIGN KEY([Id])
REFERENCES [dbo].[Game] ([ID])
GO
ALTER TABLE [dbo].[Reviews] CHECK CONSTRAINT [FK_Reviews_Game]
GO
ALTER TABLE [dbo].[StudioOnPlatform]  WITH CHECK ADD FOREIGN KEY([Platform])
REFERENCES [dbo].[Platform] ([ID])
GO
ALTER TABLE [dbo].[StudioOnPlatform]  WITH CHECK ADD FOREIGN KEY([Studio])
REFERENCES [dbo].[Studio] ([ID])
GO
ALTER TABLE [dbo].[Reviews]  WITH CHECK ADD  CONSTRAINT [CK_Reviews] CHECK  (([NumberOfStars]>=(0) AND [NumberOfStars]<=(5)))
GO
ALTER TABLE [dbo].[Reviews] CHECK CONSTRAINT [CK_Reviews]
GO
/****** Object:  StoredProcedure [dbo].[addGame]    Script Date: 1/18/2024 10:16:22 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

--Add Game Procedure
CREATE   PROCEDURE [dbo].[addGame] (
	@Name varchar(50),
	@ReleaseDate date,
	@Description varchar(400),
	@Studio varchar(50)
)
AS
	IF (@Name is null) BEGIN
		RAISERROR('Game name cannot be null.', 14, 1)
		RETURN 1
	END

	IF NOT (SELECT Name FROM Game WHERE Game.Name = @Name) is null BEGIN
		RAISERROR('Game already exists in database', 14, 1)
		RETURN 2
	END

	DECLARE @StudioID AS int
	SET @StudioID = (SELECT ID FROM Studio WHERE Studio.Name = @Studio)

	IF (@StudioID is null) BEGIN
		RAISERROR('Invalid studio name.', 14, 1)
		RETURN 3
	END

	INSERT INTO Game(Name, ReleaseDate, Description, Studio)
	VALUES (@Name, @ReleaseDate, @Description, @StudioID)
	RETURN 0
GO
/****** Object:  StoredProcedure [dbo].[addGameToGenre]    Script Date: 1/18/2024 10:16:22 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

--Add Game to Genre Procedure
CREATE   PROCEDURE [dbo].[addGameToGenre] (
	@GameName varchar(50),
	@Genre nvarchar(50)
)
AS
	IF (@GameName is null) BEGIN
		RAISERROR('Game name cannot be null.', 14, 1)
		RETURN 1
	END
	IF (@Genre is null) BEGIN
		RAISERROR('Genre name cannot be null.', 14, 1)
		RETURN 2
	END

	IF (SELECT Game.Name FROM Game WHERE Game.Name = @GameName) is null BEGIN
		RAISERROR('Invalid game name.', 14, 1)
		RETURN 3
	END
	IF (SELECT Genre.Name FROM Genre WHERE Genre.Name = @Genre) is null BEGIN
		RAISERROR('Invalid genre name.', 14, 1)
		RETURN 4
	END

	DECLARE @GameID AS int
	SET @GameID = (SELECT ID FROM Game WHERE Game.Name = @GameName)
	INSERT INTO GameHasGenre(GameID, GenreName)
	VALUES(@GameID, @Genre)
	RETURN 0
GO
/****** Object:  StoredProcedure [dbo].[addGameToPlatform]    Script Date: 1/18/2024 10:16:22 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

--Add Game to Platform Procedure
CREATE   PROCEDURE [dbo].[addGameToPlatform] (
	@GameName varchar(50),
	@PlatformName varchar(50)
)
AS
	IF (@GameName is null) BEGIN
		RAISERROR('Game name cannot be null.', 14, 1)
		RETURN 1
	END
	IF (@PlatformName is null) BEGIN
		RAISERROR('Platform name cannot be null.', 14, 1)
		RETURN 2
	END

	IF (SELECT Game.Name FROM Game WHERE Game.Name = @GameName) is null BEGIN
		RAISERROR('Invalid game name.', 14, 1)
		RETURN 3
	END
	IF (SELECT [Platform].Name FROM [Platform] WHERE [Platform].Name = @PlatformName) is null BEGIN
		RAISERROR('Invalid platform name.', 14, 1)
		RETURN 4
	END

	DECLARE @GameID AS int
	SET @GameID = (SELECT ID FROM Game WHERE Game.Name = @GameName)
	DECLARE @PlatformID AS int
	SET @PlatformID = (SELECT ID FROM [Platform] WHERE [Platform].Name = @PlatformName)
	INSERT INTO GameOnPlatform(Game, [Platform])
	VALUES(@GameID, @PlatformID)
	RETURN 0
GO
/****** Object:  StoredProcedure [dbo].[addGenre]    Script Date: 1/18/2024 10:16:22 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

--Add Genre Procedure
CREATE   PROCEDURE [dbo].[addGenre] (
	@Name nvarchar(50)
)
AS
	IF (@Name is null) BEGIN
		RAISERROR('Genre name cannot be null.', 14, 1)
		RETURN 1
	END
	IF NOT (SELECT Genre.Name FROM Genre WHERE Genre.Name = @Name) is null BEGIN
		RAISERROR('Genre already exists in database', 14, 1)
		RETURN 2
	END
	INSERT INTO Genre(Name)
	VALUES (@Name)
	RETURN 0
GO
/****** Object:  StoredProcedure [dbo].[addNote]    Script Date: 1/18/2024 10:16:22 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

--Add Note Procedure
CREATE   PROCEDURE [dbo].[addNote] (
	@GameName varchar(50),
	@Text varchar(1000)
)
AS
	IF (@Text is null) BEGIN
		RAISERROR('Cannot add an empty note.', 14, 1)
		RETURN 1
	END

	DECLARE @GameID AS int
	SET @GameID = (SELECT ID FROM Game WHERE Game.Name = @GameName)
	IF (@GameID is null) BEGIN
		RAISERROR('Invalid game name.', 14, 1)
		RETURN 2
	END

	INSERT INTO Notes(GameID, Text)
	VALUES (@GameID, @Text)
	RETURN 0
GO
/****** Object:  StoredProcedure [dbo].[addPlatform]    Script Date: 1/18/2024 10:16:22 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

--Add Platform Procedure
CREATE   PROCEDURE [dbo].[addPlatform] (
	@Name varchar(50),
	@Website varchar(75)
)
AS
	IF (@Name is null) BEGIN
		RAISERROR('Platform name cannot be null.', 14, 1)
		RETURN 1
	END
	
	IF NOT (SELECT [Platform].Name FROM [Platform] WHERE [Platform].Name = @Name) is null BEGIN
		RAISERROR('Platform already exists in database', 14, 1)
		RETURN 2
	END

	INSERT INTO [Platform](Name, Website)
	VALUES(@Name, @Website)
	RETURN 0
GO
/****** Object:  StoredProcedure [dbo].[addReview]    Script Date: 1/18/2024 10:16:22 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

--Add Review Procedure
CREATE   PROCEDURE [dbo].[addReview] (
	@GameName varchar(50),
	@NumOfStars smallint,
	@Text varchar(1000)
)
AS
	IF (@NumOfStars is null) BEGIN
		RAISERROR('Number of stars cannot be null.', 14, 1)
		RETURN 1
	END

	IF (@NumOfStars > 5 OR @NumOfStars < 0) BEGIN
		RAISERROR('Number of stars must be between 0 and 5 (inclusive).', 14, 1)
		RETURN 2
	END

	DECLARE @GameID AS int
	SET @GameID = (SELECT ID FROM Game WHERE Game.Name = @GameName)
	IF (@GameID is null) BEGIN
		RAISERROR('Invalid game name.', 14, 1)
		RETURN 3
	END

	INSERT INTO Reviews(GameID, NumberOfStars, Text)
	VALUES (@GameID, @NumOfStars, @Text)
	RETURN 0
GO
/****** Object:  StoredProcedure [dbo].[addStudio]    Script Date: 1/18/2024 10:16:22 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

--Add Studio Procedure
CREATE   PROCEDURE [dbo].[addStudio] (
	@Name varchar(50),
	@Website varchar(75)
)
AS
	IF (@Name is null) BEGIN
		RAISERROR('Studio name cannot be null.', 14, 1)
		RETURN 1
	END

	IF NOT (SELECT Studio.Name FROM Studio WHERE Studio.Name = @Name) is null BEGIN
		RAISERROR('Studio already exists in database', 14, 1)
		RETURN 2
	END
	INSERT INTO Studio(Name, Website)
	VALUES (@Name, @Website)
	RETURN 0
GO
/****** Object:  StoredProcedure [dbo].[addStudioToPlatform]    Script Date: 1/18/2024 10:16:22 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

--Add Studio to Platform Procedure
CREATE   PROCEDURE [dbo].[addStudioToPlatform] (
	@StudioName varchar(50),
	@PlatformName varchar(50)
)
AS
	IF (@StudioName is null) BEGIN
		RAISERROR('Studio name cannot be null.', 14, 1)
		RETURN 1
	END
	IF (@PlatformName is null) BEGIN
		RAISERROR('Platform name cannot be null.', 14, 1)
		RETURN 2
	END

	IF (SELECT Studio.Name FROM Studio WHERE Studio.Name = @StudioName) is null BEGIN
		RAISERROR('Invalid studio name.', 14, 1)
		RETURN 3
	END
	IF (SELECT [Platform].Name FROM [Platform] WHERE [Platform].Name = @PlatformName) is null BEGIN
		RAISERROR('Invalid platform name.', 14, 1)
		RETURN 4
	END

	DECLARE @StudioID AS int
	SET @StudioID = (SELECT ID FROM Studio WHERE Studio.Name = @StudioName)
	DECLARE @PlatformID AS int
	SET @PlatformID = (SELECT ID FROM [Platform] WHERE [Platform].Name = @PlatformName)
	INSERT INTO StudioOnPlatform(Studio, [Platform])
	VALUES(@StudioID, @PlatformID)
	RETURN 0
GO
/****** Object:  StoredProcedure [dbo].[deleteGame]    Script Date: 1/18/2024 10:16:22 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

--Delete Game Procedure
CREATE   PROCEDURE [dbo].[deleteGame] (
	@Name varchar(50)
)
AS
	IF (@Name is null) BEGIN
		RAISERROR('Game name cannot be null.', 14, 1)
		RETURN 1
	END

	IF (SELECT Name FROM Game WHERE Game.Name = @Name) is null BEGIN
		RAISERROR('Invalid game name', 14, 1)
		RETURN 2
	END

	DELETE FROM Game WHERE Game.Name = @Name
	RETURN 0
GO
/****** Object:  StoredProcedure [dbo].[deleteGameFromGenre]    Script Date: 1/18/2024 10:16:22 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE   PROCEDURE [dbo].[deleteGameFromGenre] (
	@GameName varchar(50),
	@Genre nvarchar(50)
)
AS
	IF (@GameName is null) BEGIN
		RAISERROR('Game name cannot be null.', 14, 1)
		RETURN 1
	END
	IF (@Genre is null) BEGIN
		RAISERROR('Genre name cannot be null.', 14, 1)
		RETURN 2
	END

	DECLARE @GameID AS int
	SET @GameID = (SELECT ID FROM Game WHERE Game.Name = @GameName)

	IF (SELECT GameHasGenre.GameID FROM GameHasGenre WHERE GameHasGenre.GameID = @GameID) is null BEGIN
		RAISERROR('Invalid game name.', 14, 1)
		RETURN 3
	END
	IF (SELECT GameHasGenre.GenreName FROM GameHasGenre WHERE GameHasGenre.GenreName = @Genre) is null BEGIN
		RAISERROR('Invalid genre name.', 14, 1)
		RETURN 4
	END

	DELETE FROM GameHasGenre WHERE GameHasGenre.GameID = @GameID AND GameHasGenre.GenreName = @Genre
	RETURN 0
GO
/****** Object:  StoredProcedure [dbo].[deleteGameFromPlatform]    Script Date: 1/18/2024 10:16:22 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE   PROCEDURE [dbo].[deleteGameFromPlatform] (
	@GameName varchar(50),
	@PlatformName varchar(50)
)
AS
	IF (@GameName is null) BEGIN
		RAISERROR('Game name cannot be null.', 14, 1)
		RETURN 1
	END
	IF (@PlatformName is null) BEGIN
		RAISERROR('Platform name cannot be null.', 14, 1)
		RETURN 2
	END

	DECLARE @GameID AS int
	SET @GameID = (SELECT ID FROM Game WHERE Game.Name = @GameName)
	DECLARE @PlatformID AS int
	SET @PlatformID = (SELECT ID FROM [Platform] WHERE [Platform].Name = @PlatformName)

	IF (SELECT GameOnPlatform.Game FROM GameOnPlatform WHERE GameOnPlatform.Game = @GameID) is null BEGIN
		RAISERROR('Invalid game name.', 14, 1)
		RETURN 3
	END
	IF (SELECT GameOnPlatform.[Platform] FROM GameOnPlatform WHERE GameOnPlatform.[Platform] = @PlatformID) is null BEGIN
		RAISERROR('Invalid platform name.', 14, 1)
		RETURN 4
	END

	DELETE FROM GameOnPlatform WHERE GameOnPlatform.Game = @GameID AND GameOnPlatform.[Platform] = @PlatformID
	RETURN 0
GO
/****** Object:  StoredProcedure [dbo].[deleteGenre]    Script Date: 1/18/2024 10:16:22 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE   PROCEDURE [dbo].[deleteGenre] (
	@Name nvarchar(50)
)
AS
	IF (@Name is null) BEGIN
		RAISERROR('Genre name cannot be null.', 14, 1)
		RETURN 1
	END

	IF (SELECT Genre.Name FROM Genre WHERE Genre.Name = @Name) is null BEGIN
		RAISERROR('Invalid genre name', 14, 1)
		RETURN 2
	END
	
	DELETE FROM Genre WHERE Genre.Name = @Name
	RETURN 0
GO
/****** Object:  StoredProcedure [dbo].[deleteNote]    Script Date: 1/18/2024 10:16:22 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE   PROCEDURE [dbo].[deleteNote] (
	@ID int
)
AS
	IF (SELECT Notes.Id FROM Notes WHERE Notes.Id = @ID) is null BEGIN
		RAISERROR('Invalid note id', 14, 1)
		RETURN 1
	END

	DELETE FROM Notes WHERE Notes.Id = @ID
	RETURN 0
GO
/****** Object:  StoredProcedure [dbo].[deletePlatform]    Script Date: 1/18/2024 10:16:22 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE   PROCEDURE [dbo].[deletePlatform] (
	@Name varchar(50)
)
AS
	IF (@Name is null) BEGIN
		RAISERROR('Platform name cannot be null.', 14, 1)
		RETURN 1
	END
	
	IF (SELECT [Platform].Name FROM [Platform] WHERE [Platform].Name = @Name) is null BEGIN
		RAISERROR('Invalid platform name', 14, 1)
		RETURN 2
	END

	DELETE FROM [Platform] WHERE [Platform].Name = @Name
	RETURN 0
GO
/****** Object:  StoredProcedure [dbo].[deleteReview]    Script Date: 1/18/2024 10:16:22 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE   PROCEDURE [dbo].[deleteReview] (
	@ID int
)
AS
	IF (SELECT Reviews.Id FROM Reviews WHERE Reviews.Id = @ID) is null BEGIN
		RAISERROR('Invalid note id', 14, 1)
		RETURN 1
	END

	DELETE FROM Reviews WHERE Reviews.Id = @ID
	RETURN 0
GO
/****** Object:  StoredProcedure [dbo].[deleteStudio]    Script Date: 1/18/2024 10:16:22 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

--Delete Studio Procedure
CREATE   PROCEDURE [dbo].[deleteStudio] (
	@Name varchar(50)
)
AS
	IF (@Name is null) BEGIN
		RAISERROR('Studio name cannot be null.', 14, 1)
		RETURN 1
	END

	IF (SELECT Studio.Name FROM Studio WHERE Studio.Name = @Name) is null BEGIN
		RAISERROR('Invalid studio name', 14, 1)
		RETURN 2
	END

	DELETE FROM Studio WHERE Studio.Name = @Name
	RETURN 0
GO
/****** Object:  StoredProcedure [dbo].[deleteStudioFromPlatform]    Script Date: 1/18/2024 10:16:22 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE   PROCEDURE [dbo].[deleteStudioFromPlatform] (
	@StudioName varchar(50),
	@PlatformName varchar(50)
)
AS
	IF (@StudioName is null) BEGIN
		RAISERROR('Studio name cannot be null.', 14, 1)
		RETURN 1
	END
	IF (@PlatformName is null) BEGIN
		RAISERROR('Platform name cannot be null.', 14, 1)
		RETURN 2
	END

	DECLARE @StudioID AS int
	SET @StudioID = (SELECT ID FROM Studio WHERE Studio.Name = @StudioName)
	DECLARE @PlatformID AS int
	SET @PlatformID = (SELECT ID FROM [Platform] WHERE [Platform].Name = @PlatformName)

	IF (SELECT StudioOnPlatform.Studio FROM StudioOnPlatform WHERE StudioOnPlatform.Studio = @StudioID) is null BEGIN
		RAISERROR('Invalid studio name.', 14, 1)
		RETURN 3
	END
	IF (SELECT StudioOnPlatform.[Platform] FROM StudioOnPlatform WHERE StudioOnPlatform.[Platform] = @PlatformID) is null BEGIN
		RAISERROR('Invalid platform name.', 14, 1)
		RETURN 4
	END
	
	DELETE FROM StudioOnPlatform WHERE StudioOnPlatform.Studio = @StudioID AND StudioOnPlatform.[Platform] = @PlatformID
	RETURN 0
GO
/****** Object:  StoredProcedure [dbo].[updateGame]    Script Date: 1/18/2024 10:16:22 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

--Update Game Procedure
--If @NewDescription or @NewStudioName is null, the previous value for the relevant variable will be kept
CREATE   PROCEDURE [dbo].[updateGame] (
	@GameName varchar(50),
	@NewDescription varchar(400),
	@NewStudioName varchar(50)
)
AS
	IF (@GameName is null) BEGIN
		RAISERROR('Game name cannot be null.', 14, 1)
		RETURN 1
	END

	IF (SELECT Name FROM Game WHERE Game.Name = @GameName) is null BEGIN
		RAISERROR('Game could not be found', 14, 1)
		RETURN 2
	END

	IF @NewStudioName is not null BEGIN
		DECLARE @StudioID AS int
		SET @StudioID = (SELECT ID FROM Studio WHERE Studio.Name = @NewStudioName)

		IF (@StudioID is null) BEGIN
			RAISERROR('Invalid studio name.', 14, 1)
			RETURN 3
		END

		UPDATE Game
		SET [Studio] = @StudioID
		WHERE [Name] = @GameName
	END

	IF @NewDescription is not null BEGIN
		UPDATE Game
		SET [Description] = @NewDescription
		WHERE [Name] = @GameName
	END
GO
/****** Object:  StoredProcedure [dbo].[updateNote]    Script Date: 1/18/2024 10:16:22 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

--Update Note Procedure
CREATE   PROCEDURE [dbo].[updateNote] (
	@NoteID int,
	@NewText varchar(1000)
)
AS
	IF (@NoteID is null) BEGIN
		RAISERROR('Note ID cannot be null.', 14, 1)
		RETURN 1
	END

	IF (SELECT ID FROM Notes WHERE Notes.ID = @NoteID) is null BEGIN
		RAISERROR('Note could not be found', 14, 1)
		RETURN 2
	END

	UPDATE Notes
	SET [Text] = @NewText
	WHERE [ID] = @NoteID
GO
/****** Object:  StoredProcedure [dbo].[updatePlatform]    Script Date: 1/18/2024 10:16:22 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

--Update Platform Procedure
--If @NewPlatformName or @NewWebsite is null, the previous value for the relevant variable will be kept
CREATE   PROCEDURE [dbo].[updatePlatform] (
	@OldPlatformName varchar(50),
	@NewPlatformName varchar(50),
	@NewWebsite varchar(75)
)
AS
	IF (@OldPlatformName is null) BEGIN
		RAISERROR('Cannot update a null platform.', 14, 1)
		RETURN 1
	END

	IF @NewPlatformName is not null BEGIN
		DECLARE @PlatformID AS int
		SET @PlatformID = (SELECT ID FROM Studio WHERE Studio.Name = @OldPlatformName)

		IF @PlatformID is null BEGIN
			RAISERROR('Platform could not be found', 14, 1)
			RETURN 2
		END

		UPDATE [Platform]
		SET [Name] = @NewPlatformName
		WHERE [Platform].ID = @PlatformID
	END

	IF @NewWebsite is not null BEGIN
		UPDATE [Platform]
		SET Website = @NewWebsite
		WHERE [Platform].ID = @PlatformID
	END
GO
/****** Object:  StoredProcedure [dbo].[updateReview]    Script Date: 1/18/2024 10:16:22 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

--Update Review Procedure
--If @NewText or @NewNumberOfStars is null, the previous value for the relevant variable will be kept
CREATE   PROCEDURE [dbo].[updateReview] (
	@ReviewID int,
	@NewText varchar(500),
	@NewNumberOfStars smallint
)
AS
	IF (@ReviewID is null) BEGIN
		RAISERROR('Review ID cannot be null.', 14, 1)
		RETURN 1
	END

	IF (SELECT ID FROM Reviews WHERE Reviews.ID = @ReviewID) is null BEGIN
		RAISERROR('Review could not be found', 14, 1)
		RETURN 2
	END

	IF @NewNumberOfStars is not null BEGIN
		UPDATE Reviews
		SET [NumberOfStars] = @NewNumberOfStars
		WHERE [ID] = @ReviewID
	END

	IF @NewText is not null BEGIN
		UPDATE Reviews
		SET [Text] = @NewText
		WHERE [ID] = @ReviewID
	END
GO
/****** Object:  StoredProcedure [dbo].[updateStudio]    Script Date: 1/18/2024 10:16:22 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

--Update Studio Procedure
--If @NewStudioName or @NewWebsite is null, the previous value for the relevant variable will be kept
CREATE   PROCEDURE [dbo].[updateStudio] (
	@OldStudioName varchar(50),
	@NewStudioName varchar(50),
	@NewWebsite varchar(75)
)
AS
	IF (@OldStudioName is null) BEGIN
		RAISERROR('Cannot update a null studio.', 14, 1)
		RETURN 1
	END

	IF @NewStudioName is not null BEGIN
		DECLARE @StudioID AS int
		SET @StudioID = (SELECT ID FROM Studio WHERE Studio.Name = @OldStudioName)

		IF @StudioID is null BEGIN
			RAISERROR('Studio could not be found', 14, 1)
			RETURN 2
		END

		UPDATE [Studio]
		SET [Name] = @NewStudioName
		WHERE [Studio].ID = @StudioID
	END

	IF @NewWebsite is not null BEGIN
		UPDATE [Studio]
		SET Website = @NewWebsite
		WHERE [Studio].ID = @StudioID
	END
GO
USE [master]
GO
ALTER DATABASE [GameLibraryDB_yangnj] SET  READ_WRITE 
GO
