/****** Object:  StoredProcedure [dbo].[GetReviewID]    Script Date: 2/1/2024 8:00:13 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE OR ALTER PROCEDURE [dbo].[GetReviewID]
    @GameName NVARCHAR(50),
	@User NVARCHAR(20)
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @GameID INT;
	

    -- Get the GameID corresponding to the selected GameName
   SET @GameID = (SELECT ID FROM Game WHERE Game.Name = @GameName)
	IF (@GameID is null) BEGIN
		RAISERROR('Invalid game name.', 14, 1)
		RETURN 1
	END


    -- Select the NoteID based on the GameID
    SELECT Id 
	FROM Reviews 
	WHERE GameID = @GameID AND ReviewWriter = @User;
	
END;