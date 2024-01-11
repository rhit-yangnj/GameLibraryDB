USE [GameLibraryDB]
GO

/****** Object:  Table [dbo].[Reviews]    Script Date: 1/10/2024 8:18:45 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Reviews](
	[Id] [int] NOT NULL,
	[Text] [varchar](500) NULL,
	[NumberOfStars] [smallint] NOT NULL,
	[GameID] [int] NOT NULL,
 CONSTRAINT [PK_Reviews] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[Reviews]  WITH CHECK ADD  CONSTRAINT [FK_Reviews_Game] FOREIGN KEY([Id])
REFERENCES [dbo].[Game] ([ID])
GO

ALTER TABLE [dbo].[Reviews] CHECK CONSTRAINT [FK_Reviews_Game]
GO

ALTER TABLE [dbo].[Reviews]  WITH CHECK ADD  CONSTRAINT [CK_Reviews] CHECK  (([NumberOfStars]>=(0) AND [NumberOfStars]<=(5)))
GO

ALTER TABLE [dbo].[Reviews] CHECK CONSTRAINT [CK_Reviews]
GO

