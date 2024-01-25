--Delete Game Procedure
CREATE OR ALTER PROCEDURE deleteGame (
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

--Delete Studio Procedure
CREATE OR ALTER PROCEDURE deleteStudio (
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

--Delete Genre Procedure
CREATE OR ALTER PROCEDURE deleteGenre (
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

--Delete Note Procedure
CREATE OR ALTER PROCEDURE deleteNote (
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

--Delete Review Procedure
CREATE OR ALTER PROCEDURE deleteReview (
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

--Delete Platform Procedure
CREATE OR ALTER PROCEDURE deletePlatform (
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

--Delete Game from Genre Procedure
CREATE OR ALTER PROCEDURE deleteGameFromGenre (
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

-- Delete Game from Platform Procedure
CREATE OR ALTER PROCEDURE deleteGameFromPlatform (
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

--Delete Studio from Platform Procedure
CREATE OR ALTER PROCEDURE deleteStudioFromPlatform (
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