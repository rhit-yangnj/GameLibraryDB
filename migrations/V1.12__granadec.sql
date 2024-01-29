
Create OR ALTER Procedure getAllGames
as
    SELECT Name FROM [Game]

GO

Create OR ALTER Procedure getPersonalGames(
	@Username varchar(20)
)
AS
	IF (@Username is null or not exists(SELECT Username from [User] where Username = @Username)) BEGIN
		Print('Username cannot be null or nonexsistant.')
		RETURN 1
	END

    Select Name
    FROM [Game] g
    Join [UserHasGame] u on u.Game = g.ID
    Where u.[User] = @Username
GO

CREATE PROCEDURE RemoveGameFromUser (
	@GameName varchar(50),
	@Username varchar(50)
)
AS
	IF (@GameName is null) BEGIN
		RAISERROR('Game name cannot be null.', 14, 1)
		RETURN 1
	END
	IF (SELECT Game.Name FROM Game WHERE Game.Name = @GameName) is null BEGIN
		RAISERROR('Invalid Game name.', 14, 1)
		RETURN 2
	END

	IF (@Username is null) BEGIN
		RAISERROR('Username name cannot be null.', 14, 1)
		RETURN 3
	END
	IF (SELECT [User].UserName FROM [User] WHERE [User].Username = @Username) is null BEGIN
		RAISERROR('Invalid User name.', 14, 1)
		RETURN 4
	END

	DECLARE @GameID AS int
	SET @GameID = (SELECT ID FROM [Game] WHERE [Game].Name = @GameName)

    IF(not exists(Select * from UserHasGame Where [User] = @Username and [Game] = @GameID)) BEGIN
		RAISERROR('User does not have game', 14, 1)
		RETURN 5
    END

    Delete From UserHasGame
    Where [User] = @Username and [Game] = @GameID
	RETURN 0
GO