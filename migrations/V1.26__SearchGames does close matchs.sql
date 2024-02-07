USE [GameLibraryDB]
GO
/****** Object:  StoredProcedure [dbo].[SearchGames]    Script Date: 2/7/2024 12:03:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER   PROCEDURE [dbo].[SearchGames]
(
	@Username varchar(20),
	@GameName varchar(50),
	@StudioName varchar(50),
	@PlatformName varchar(50),
	@Genre nvarchar(50)
)
AS

--DECLARE @StudioID int
--SELECT @StudioID = ID FROM Studio WHERE Studio.[Name] = @StudioName

DECLARE @PlatformID int
SELECT @PlatformID = ID FROM [Platform] WHERE [Platform].[Name] = @PlatformName

SELECT g.[Name], g.[Description], s.[Name] as StudioName, p.[Name] as PlatformName, ghg.GenreName, g.ReleaseDate
FROM Game g
LEFT JOIN UserHasGame uhg ON uhg.Game = g.ID
LEFT JOIN GameHasGenre ghg ON ghg.GameID = g.ID
LEFT JOIN GameOnPlatform gop ON gop.Game = g.ID
LEFT JOIN StudioInvolvedIn sii ON sii.GameID = g.ID
LEFT JOIN Studio s ON sii.StudioID = s.ID
LEFT JOIN [Platform] p ON p.ID = gop.[Platform]
WHERE (@GameName is null OR g.[Name] LIKE CONCAT('%', @GameName, '%')) AND
	(@Username is null OR @Username = uhg.[User]) AND
	(@StudioName is null OR s.[Name] LIKE CONCAT('%', @StudioName, '%')) AND
	(@PlatformName is null OR p.[Name] LIKE CONCAT('%', @PlatformName, '%')) AND
	(@Genre is null OR ghg.GenreName LIKE CONCAT('%', @Genre, '%'))