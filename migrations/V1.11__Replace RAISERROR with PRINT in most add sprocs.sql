--Add Game procedure
ALTER PROCEDURE [dbo].[addGame] (
	@Name varchar(50),
	@ReleaseDate date,
	@Description varchar(400)
)
AS
	IF (@Name is null) BEGIN
		PRINT('Game name cannot be null.')
		RETURN 1
	END

	IF NOT (SELECT Name FROM Game WHERE Game.Name = @Name) is null BEGIN
		PRINT('Game already exists in database')
		RETURN 2
	END

	INSERT INTO Game(Name, ReleaseDate, Description)
	VALUES (@Name, @ReleaseDate, @Description)
	RETURN 0
GO

--Add Game to Genre Procedure
ALTER   PROCEDURE [dbo].[addGameToGenre] (
	@GameName varchar(50),
	@Genre nvarchar(50)
)
AS
	IF (@GameName is null) BEGIN
		PRINT('Game name cannot be null.')
		RETURN 1
	END
	IF (@Genre is null) BEGIN
		PRINT('Genre name cannot be null.')
		RETURN 2
	END

	IF (SELECT Game.Name FROM Game WHERE Game.Name = @GameName) is null BEGIN
		PRINT('Invalid game name.')
		RETURN 3
	END
	IF (SELECT Genre.Name FROM Genre WHERE Genre.Name = @Genre) is null BEGIN
		PRINT('Invalid genre name.')
		RETURN 4
	END

	DECLARE @GameID AS int
	SET @GameID = (SELECT ID FROM Game WHERE Game.Name = @GameName)

	IF EXISTS(SELECT * FROM GameHasGenre WHERE GameID = @GameID AND GenreName = @Genre) BEGIN
		PRINT('This studio has already been added to this platform.')
		RETURN 5
	END

	INSERT INTO GameHasGenre(GameID, GenreName)
	VALUES(@GameID, @Genre)
	RETURN 0
GO

--Add Game to Platform Procedure
ALTER   PROCEDURE [dbo].[addGameToPlatform] (
	@GameName varchar(50),
	@PlatformName varchar(50)
)
AS
	IF (@GameName is null) BEGIN
		PRINT('Game name cannot be null.')
		RETURN 1
	END
	IF (@PlatformName is null) BEGIN
		PRINT('Platform name cannot be null.')
		RETURN 2
	END

	IF (SELECT Game.Name FROM Game WHERE Game.Name = @GameName) is null BEGIN
		PRINT('Invalid game name.')
		RETURN 3
	END
	IF (SELECT [Platform].Name FROM [Platform] WHERE [Platform].Name = @PlatformName) is null BEGIN
		PRINT('Invalid platform name.')
		RETURN 4
	END

	DECLARE @GameID AS int
	SET @GameID = (SELECT ID FROM Game WHERE Game.Name = @GameName)
	DECLARE @PlatformID AS int
	SET @PlatformID = (SELECT ID FROM [Platform] WHERE [Platform].Name = @PlatformName)

	IF EXISTS(SELECT * FROM GameOnPlatform WHERE Game = @GameID AND [Platform] = @PlatformID) BEGIN
		PRINT('This game has already been added to this platform.')
		RETURN 5
	END

	INSERT INTO GameOnPlatform(Game, [Platform])
	VALUES(@GameID, @PlatformID)
	RETURN 0
GO

--addGameToStudio sproc
ALTER   PROCEDURE [dbo].[addGameToStudio](
	@GameName varchar(50),
	@StudioName varchar(50)
)
AS
	IF (@GameName is null) BEGIN
		PRINT('Game name cannot be null.')
		RETURN 1
	END
	IF (@StudioName is null) BEGIN
		PRINT('Studio name cannot be null.')
		RETURN 2
	END

	IF (SELECT Game.Name FROM Game WHERE Game.Name = @GameName) is null BEGIN
		PRINT('Invalid game name.')
		RETURN 3
	END
	IF (SELECT [Studio].Name FROM [Studio] WHERE [Studio].Name = @StudioName) is null BEGIN
		PRINT('Invalid studio name.')
		RETURN 4
	END

	DECLARE @GameID AS int
	SET @GameID = (SELECT ID FROM Game WHERE Game.Name = @GameName)
	DECLARE @StudioID AS int
	SET @StudioID = (SELECT ID FROM [Studio] WHERE [Studio].Name = @StudioName)

	IF EXISTS(SELECT * FROM StudioInvolvedIn WHERE StudioID = @StudioID AND [GameID] = @GameID) BEGIN
		PRINT('This game has already been added to this studio.')
		RETURN 5
	
	END
	INSERT INTO StudioInvolvedIn(GameID, StudioID)
	VALUES(@GameID, @StudioID)
	RETURN 0
GO

--Add Genre Procedure
ALTER   PROCEDURE [dbo].[addGenre] (
	@Name nvarchar(50)
)
AS
	IF (@Name is null) BEGIN
		PRINT('Genre name cannot be null.')
		RETURN 1
	END
	IF NOT (SELECT Genre.Name FROM Genre WHERE Genre.Name = @Name) is null BEGIN
		PRINT('Genre already exists in database')
		RETURN 2
	END
	INSERT INTO Genre(Name)
	VALUES (@Name)
	RETURN 0
GO

--Add Platform Procedure
ALTER   PROCEDURE [dbo].[addPlatform] (
	@Name varchar(50),
	@Website varchar(75)
)
AS
	IF (@Name is null) BEGIN
		PRINT('Platform name cannot be null.')
		RETURN 1
	END
	
	IF NOT (SELECT [Platform].Name FROM [Platform] WHERE [Platform].Name = @Name) is null BEGIN
		PRINT('Platform already exists in database')
		RETURN 2
	END

	INSERT INTO [Platform](Name, Website)
	VALUES(@Name, @Website)
	RETURN 0
GO

--Add Studio Procedure
ALTER   PROCEDURE [dbo].[addStudio] (
	@Name varchar(50),
	@Website varchar(75)
)
AS
	IF (@Name is null) BEGIN
		PRINT('Studio name cannot be null.')
		RETURN 1
	END

	IF NOT (SELECT Studio.Name FROM Studio WHERE Studio.Name = @Name) is null BEGIN
		PRINT('Studio already exists in database')
		RETURN 2
	END
	INSERT INTO Studio(Name, Website)
	VALUES (@Name, @Website)
	RETURN 0
GO

--Add Studio to Platform Procedure
ALTER   PROCEDURE [dbo].[addStudioToPlatform] (
	@StudioName varchar(50),
	@PlatformName varchar(50)
)
AS
	IF (@StudioName is null) BEGIN
		PRINT('Studio name cannot be null.')
		RETURN 1
	END
	IF (@PlatformName is null) BEGIN
		PRINT('Platform name cannot be null.')
		RETURN 2
	END

	IF (SELECT Studio.Name FROM Studio WHERE Studio.Name = @StudioName) is null BEGIN
		PRINT('Invalid studio name.')
		RETURN 3
	END
	IF (SELECT [Platform].Name FROM [Platform] WHERE [Platform].Name = @PlatformName) is null BEGIN
		PRINT('Invalid platform name.')
		RETURN 4
	END

	DECLARE @StudioID AS int
	SET @StudioID = (SELECT ID FROM Studio WHERE Studio.Name = @StudioName)
	DECLARE @PlatformID AS int
	SET @PlatformID = (SELECT ID FROM [Platform] WHERE [Platform].Name = @PlatformName)

	IF EXISTS(SELECT * FROM StudioOnPlatform WHERE Studio = @StudioID AND [Platform] = @PlatformID) BEGIN
		PRINT('This studio has already been added to this platform.')
		RETURN 5
	END

	INSERT INTO StudioOnPlatform(Studio, [Platform])
	VALUES(@StudioID, @PlatformID)
	RETURN 0
GO