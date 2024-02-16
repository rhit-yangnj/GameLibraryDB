Create or Alter Function SelectPlatformAgg (@GameID int, @PlatformName varchar(50)) returns nvarchar(MAX)
as
begin
Declare @ReturnString nvarchar(MAX)
select @ReturnString = STRING_AGG(p.[Name], ', ') 
from Game g
LEFT JOIN GameOnPlatform gop ON gop.Game = g.ID
LEFT JOIN [Platform] p ON p.ID = gop.[Platform]
where (g.ID = @GameID)
return @returnString

end

go

Create or Alter Function SelectGenreAgg (@GameID int, @GenreName varchar(50)) returns nvarchar(MAX)
as
begin
Declare @ReturnString nvarchar(MAX)
select @ReturnString = STRING_AGG(ghg.GenreName, ', ') 
from Game g
LEFT JOIN GameHasGenre ghg ON ghg.GameID = g.ID
where (g.ID = @GameID)
return @returnString

end

go


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

SELECT g.[Name], g.[Description], s.[Name] as StudioName, dbo.SelectPlatformAgg(g.ID, @PlatformName) as PlatformName, dbo.SelectGenreAgg(g.ID, @Genre) as GenreName, g.ReleaseDate, AVG(r.NumberOfStars) as AverageScore
FROM Game g
LEFT JOIN UserHasGame uhg ON uhg.Game = g.ID
LEFT JOIN StudioInvolvedIn sii ON sii.GameID = g.ID
LEFT JOIN Studio s ON sii.StudioID = s.ID
LEFT JOIN Reviews r ON r.GameID = g.ID
WHERE (@GameName is null OR g.[Name] LIKE CONCAT('%', @GameName, '%')) AND
	(@Username is null OR @Username = uhg.[User]) AND
	(@StudioName is null OR s.[Name] LIKE CONCAT('%', @StudioName, '%')) AND
	(@Genre is null OR dbo.SelectGenreAgg(g.ID, @Genre) LIKE CONCAT('%', @Genre, '%')) AND
	(@PlatformName is null OR dbo.SelectPlatformAgg(g.ID, @PlatformName) LIKE CONCAT('%', @PlatformName, '%'))
GROUP BY g.[Name], g.[Description], s.[Name], g.ReleaseDate, g.ID
HAVING AVG(r.NumberOfStars) >= @MinStars OR @MinStars is null