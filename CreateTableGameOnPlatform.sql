USE [GameLibraryDB]
GO

/****** Object:  Table [dbo].[GameOnPlatform]    Script Date: 1/10/2024 8:18:20 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[GameOnPlatform](
	[Game] [int] NOT NULL,
	[Platform] [int] NOT NULL,
 CONSTRAINT [PK_GameOnPlatform] PRIMARY KEY CLUSTERED 
(
	[Game] ASC,
	[Platform] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

