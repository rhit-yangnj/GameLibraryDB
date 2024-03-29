
/****** Object:  StoredProcedure [dbo].[addReview]    Script Date: 2/1/2024 6:28:46 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
--Add Review Procedure
ALTER   PROCEDURE [dbo].[addReview] (
	@GameName varchar(50),
	@NumOfStars smallint,
	@Text varchar(1000),
	@User varchar(20)
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

	INSERT INTO Reviews(GameID, NumberOfStars, Text, ReviewWriter)
	VALUES (@GameID, @NumOfStars, @Text, @User)
	RETURN 0