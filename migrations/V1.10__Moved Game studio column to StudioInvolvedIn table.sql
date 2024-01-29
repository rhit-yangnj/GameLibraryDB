--remove Studio column from Game
ALTER TABLE GAME
DROP CONSTRAINT FK_Game_Studio
GO

ALTER TABLE Game
DROP COLUMN Studio;
GO

--Add StudioInvolvedIn table
CREATE TABLE StudioInvolvedIn(
	GameID int REFERENCES Game(ID),
	StudioID int REFERENCES Studio(ID),
	PRIMARY KEY(GameID, StudioID)
);
GO

--create addGameToStudio sproc
CREATE OR ALTER PROCEDURE addGameToStudio(
	@GameName varchar(50),
	@StudioName varchar(50)
)
AS
	IF (@GameName is null) BEGIN
		RAISERROR('Game name cannot be null.', 14, 1)
		RETURN 1
	END
	IF (@StudioName is null) BEGIN
		RAISERROR('Studio name cannot be null.', 14, 1)
		RETURN 2
	END

	IF (SELECT Game.Name FROM Game WHERE Game.Name = @GameName) is null BEGIN
		RAISERROR('Invalid game name.', 14, 1)
		RETURN 3
	END
	IF (SELECT [Studio].Name FROM [Studio] WHERE [Studio].Name = @StudioName) is null BEGIN
		RAISERROR('Invalid studio name.', 14, 1)
		RETURN 4
	END

	DECLARE @GameID AS int
	SET @GameID = (SELECT ID FROM Game WHERE Game.Name = @GameName)
	DECLARE @StudioID AS int
	SET @StudioID = (SELECT ID FROM [Studio] WHERE [Studio].Name = @StudioName)
	INSERT INTO StudioInvolvedIn(GameID, StudioID)
	VALUES(@GameID, @StudioID)
	RETURN 0
GO

--Remove @Studio parameter from addGame
ALTER PROCEDURE [dbo].[addGame] (
	@Name varchar(50),
	@ReleaseDate date,
	@Description varchar(400)
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

	INSERT INTO Game(Name, ReleaseDate, Description)
	VALUES (@Name, @ReleaseDate, @Description)
	RETURN 0
GO