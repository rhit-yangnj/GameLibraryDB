
/****** Object:  StoredProcedure [dbo].[updateReview]    Script Date: 2/1/2024 6:28:56 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
--Update Review Procedure
--If @NewText or @NewNumberOfStars is null, the previous value for the relevant variable will be kept
ALTER   PROCEDURE [dbo].[updateReview] (
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