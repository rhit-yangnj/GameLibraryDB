USE [GameLibraryDB]
GO
/****** Object:  StoredProcedure [dbo].[SearchGames]    Script Date: 2/10/2024 10:31:10 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER PROCEDURE [dbo].[SearchGames]
(
	@Username varchar(20),
	@GameName varchar(50),
	@StudioName varchar(50),
	@PlatformName varchar(50),
	@Genre nvarchar(50),
	@MinStars float
)
AS

--DECLARE @StudioID int
--SELECT @StudioID = ID FROM Studio WHERE Studio.[Name] = @StudioName

DECLARE @PlatformID int
SELECT @PlatformID = ID FROM [Platform] WHERE [Platform].[Name] = @PlatformName

SELECT g.[Name], g.[Description], s.[Name] as StudioName, STRING_AGG(p.[Name], ', ') as PlatformName, STRING_AGG(ghg.GenreName, ', ') as GenreName, g.ReleaseDate, AVG(r.NumberOfStars) as AverageScore
FROM Game g
LEFT JOIN UserHasGame uhg ON uhg.Game = g.ID
LEFT JOIN GameHasGenre ghg ON ghg.GameID = g.ID
LEFT JOIN GameOnPlatform gop ON gop.Game = g.ID
LEFT JOIN StudioInvolvedIn sii ON sii.GameID = g.ID
LEFT JOIN Studio s ON sii.StudioID = s.ID
LEFT JOIN [Platform] p ON p.ID = gop.[Platform]
LEFT JOIN Reviews r ON r.GameID = g.ID
WHERE (@GameName is null OR g.[Name] LIKE CONCAT('%', @GameName, '%')) AND
	(@Username is null OR @Username = uhg.[User]) AND
	(@StudioName is null OR s.[Name] LIKE CONCAT('%', @StudioName, '%')) AND
	(@PlatformName is null OR p.[Name] LIKE CONCAT('%', @PlatformName, '%')) AND
	(@Genre is null OR ghg.GenreName LIKE CONCAT('%', @Genre, '%'))
GROUP BY g.[Name], g.[Description], s.[Name], g.ReleaseDate
HAVING AVG(r.NumberOfStars) >= @MinStars OR @MinStars is null