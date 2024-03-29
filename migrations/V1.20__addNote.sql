
/****** Object:  StoredProcedure [dbo].[addNote]    Script Date: 2/1/2024 8:27:54 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
--Add Note Procedure
CREATE OR ALTER   PROCEDURE [dbo].[addNote] (
	@GameName varchar(50),
	@Text varchar(1000),
	@User varchar(50)
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

	INSERT INTO Notes(GameID, Text, NoteWriter)
	VALUES (@GameID, @Text, @User)
	RETURN 0