
CREATE OR ALTER PROCEDURE GetReviewsForGame
    @GameName NVARCHAR(100)
AS
BEGIN
    DECLARE @GameID INT;

    -- Get the GameID for the given GameName
    SELECT @GameID = ID
    FROM Game
    WHERE Name = @GameName;

    IF @GameID IS NOT NULL
    BEGIN
        -- Retrieve the notes for the specified game
        SELECT R.[Text] AS Review, R.ReviewWriter
        FROM Reviews R
        WHERE R.GameID = @GameID;
    END
    ELSE
    BEGIN
        
        PRINT 'Game not found.';
    END
END
