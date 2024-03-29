CREATE OR ALTER PROCEDURE SearchGames
(
	@Username varchar(20),
	@GameName varchar(50),
	@StudioName varchar(50),
	@PlatformName varchar(50),
	@Genre nvarchar(50)
)
AS

DECLARE @StudioID int
SELECT @StudioID = ID FROM Studio WHERE Studio.Name = @StudioName


SELECT *
FROM Game g
LEFT JOIN UserHasGame uhg ON uhg.Game = g.ID
JOIN GameHasGenre ghg ON ghg.GameID = g.ID
JOIN GameOnPlatform gop ON gop.Game = g.ID
WHERE (@GameName is null OR g.[Name] = @GameName) AND
	(@Username is null OR @Username = uhg.[User]) AND
	(@StudioID is null OR @StudioID = g.Studio) AND
	(@PlatformName is null OR @PlatformName = gop.[Platform]) AND
	(@Genre is null OR @Genre = ghg.GenreName)