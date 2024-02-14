CREATE OR ALTER PROCEDURE GetUserCredentials
    @Username NVARCHAR(50),
    @Salt VARBINARY(64) OUTPUT,
    @HashPass NVARCHAR(256) OUTPUT
AS
BEGIN
    SELECT @Salt = Salt, @HashPass = HashPass
    FROM [User]
    WHERE Username = @Username;
END;
