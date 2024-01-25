--Update Game Procedure
--If @NewDescription or @NewStudioName is null, the previous value for the relevant variable will be kept
CREATE OR ALTER PROCEDURE updateGame (
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

--Update Note Procedure
CREATE OR ALTER PROCEDURE updateNote (
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

--Update Platform Procedure
--If @NewPlatformName or @NewWebsite is null, the previous value for the relevant variable will be kept
CREATE OR ALTER PROCEDURE updatePlatform (
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

--Update Review Procedure
--If @NewText or @NewNumberOfStars is null, the previous value for the relevant variable will be kept
CREATE OR ALTER PROCEDURE updateReview (
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

--Update Studio Procedure
--If @NewStudioName or @NewWebsite is null, the previous value for the relevant variable will be kept
CREATE OR ALTER PROCEDURE updateStudio (
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