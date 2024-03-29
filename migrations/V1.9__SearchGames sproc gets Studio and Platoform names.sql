ALTER   PROCEDURE [dbo].[SearchGames]
(
	@Username varchar(20),
	@GameName varchar(50),
	@StudioName varchar(50),
	@PlatformName varchar(50),
	@Genre nvarchar(50)
)
AS

DECLARE @StudioID int
SELECT @StudioID = ID FROM Studio WHERE Studio.[Name] = @StudioName

DECLARE @PlatformID int
SELECT @PlatformID = ID FROM [Platform] WHERE [Platform].[Name] = @PlatformName

SELECT g.[Name], g.[Description], s.[Name] as StudioName, p.[Name] as PlatformName, ghg.GenreName, g.ReleaseDate
FROM Game g
LEFT JOIN UserHasGame uhg ON uhg.Game = g.ID
JOIN GameHasGenre ghg ON ghg.GameID = g.ID
JOIN GameOnPlatform gop ON gop.Game = g.ID
JOIN Studio s ON g.Studio = s.ID
JOIN [Platform] p ON p.ID = gop.[Platform]
WHERE (@GameName is null OR g.[Name] = @GameName) AND
	(@Username is null OR @Username = uhg.[User]) AND
	(@StudioID is null OR @StudioID = g.Studio) AND
	(@PlatformName is null OR @PlatformID = gop.[Platform]) AND
	(@Genre is null OR @Genre = ghg.GenreName)