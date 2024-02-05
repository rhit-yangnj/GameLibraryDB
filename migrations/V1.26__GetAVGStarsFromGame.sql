CREATE OR ALTER PROCEDURE GetAverageStarsForGame
    @GameName NVARCHAR(100)
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @GameID INT;

    -- Get the GameID for the given GameName
    SELECT @GameID = ID
    FROM Game
    WHERE Name = @GameName;

    IF @GameID IS NOT NULL
    BEGIN
        -- Calculate the average number of stars for the specified game
        SELECT AVG(NumberOfStars) AS AverageStars
        FROM Reviews
        WHERE GameID = @GameID;
    END
    ELSE
    BEGIN
        -- Game not found
        PRINT 'Game not found.';
    END
END
