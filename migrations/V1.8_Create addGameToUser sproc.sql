--Add Game to User Procedure
CREATE PROCEDURE addGametoUser (
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
	INSERT INTO UserHasGame([User], [Game])
	VALUES(@Username, @GameID)
	RETURN 0
GO