--Add Game to User Procedure
ALTER PROCEDURE [dbo].[addGametoUser] (
	@GameName varchar(50),
	@Username varchar(50)
)
AS
	IF (@GameName is null) BEGIN
		PRINT('Game name cannot be null.')
		RETURN 1
	END
	IF (SELECT Game.Name FROM Game WHERE Game.Name = @GameName) is null BEGIN
		PRINT('Invalid Game name.')
		RETURN 2
	END

	IF (@Username is null) BEGIN
		PRINT('Username name cannot be null.')
		RETURN 3
	END
	IF (SELECT [User].UserName FROM [User] WHERE [User].Username = @Username) is null BEGIN
		PRINT('Invalid User name.')
		RETURN 4
	END

	DECLARE @GameID AS int
	SET @GameID = (SELECT ID FROM [Game] WHERE [Game].Name = @GameName)

	IF (SELECT [User] FROM UserHasGame WHERE UserHasGame.Game = @GameID AND UserHasGame.[User] = @Username) is not null BEGIN
		PRINT('User already owns this game.')
		RETURN 5
	END

	INSERT INTO UserHasGame([User], [Game])
	VALUES(@Username, @GameID)
	RETURN 0