CREATE OR ALTER PROCEDURE [dbo].[addUser] (
	@Username varchar(20),
	@PasswordSalt binary(16),
	@PasswordHash varchar(32)
)
AS
	IF (@Username is null) BEGIN
		PRINT('Username cannot be null.')
		RETURN 1
	END

	IF NOT (SELECT Username FROM [User] WHERE [User].Username = @Username) is null BEGIN
		PRINT('Username already exists in database.')
		RETURN 2
	END

	IF (@PasswordSalt is null) BEGIN
		PRINT('Password salt cannot be null.')
		RETURN 3
	END

	IF (@PasswordHash is null) BEGIN
		PRINT('Password hash cannot be null.')
		RETURN 4
	END

	INSERT INTO [User](Username, Salt, HashPass)
	VALUES (@Username, @PasswordSalt, @PasswordHash)
	RETURN 0