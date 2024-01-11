USE [GameLibraryDB]
GO

/****** Object:  Table [dbo].[Game]    Script Date: 1/10/2024 8:17:54 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[Game](
	[Name] [varchar](50) NOT NULL,
	[ReleaseDate] [date] NOT NULL,
	[Description] [varchar](400) NULL,
	[ID] [int] NOT NULL,
	[Studio] [nchar](50) NOT NULL,
 CONSTRAINT [PK_Game] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[Game]  WITH CHECK ADD  CONSTRAINT [FK_Game_Studio] FOREIGN KEY([ID])
REFERENCES [dbo].[Studio] ([ID])
GO

ALTER TABLE [dbo].[Game] CHECK CONSTRAINT [FK_Game_Studio]
GO

