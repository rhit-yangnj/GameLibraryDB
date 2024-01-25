--Add Game Procedure
CREATE OR ALTER PROCEDURE addGame (
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

--Add Studio Procedure
CREATE OR ALTER PROCEDURE addStudio (
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

--Add Genre Procedure
CREATE OR ALTER PROCEDURE addGenre (
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

--Add Note Procedure
CREATE OR ALTER PROCEDURE addNote (
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

--Add Review Procedure
CREATE OR ALTER PROCEDURE addReview (
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

--Add Platform Procedure
CREATE OR ALTER PROCEDURE addPlatform (
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

--Add Game to Genre Procedure
CREATE OR ALTER PROCEDURE addGameToGenre (
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

--Add Game to Platform Procedure
CREATE OR ALTER PROCEDURE addGameToPlatform (
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

--Add Studio to Platform Procedure
CREATE OR ALTER PROCEDURE addStudioToPlatform (
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