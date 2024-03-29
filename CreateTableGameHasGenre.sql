USE [GameLibraryDB]
GO

/****** Object:  Table [dbo].[GameHasGenre]    Script Date: 1/10/2024 8:18:06 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[GameHasGenre](
	[GameID] [int] NOT NULL,
	[GenreName] [nvarchar](50) NOT NULL,
 CONSTRAINT [PK_GameHasGenre] PRIMARY KEY CLUSTERED 
(
	[GameID] ASC,
	[GenreName] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

