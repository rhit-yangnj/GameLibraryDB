USE [GameLibraryDB]
GO

/****** Object:  Table [dbo].[StudioOnPlatform]    Script Date: 1/10/2024 8:18:17 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[StudioOnPlatform](
	[Studio] [int] NOT NULL,
	[Platform] [int] NOT NULL,
 CONSTRAINT [PK_StudioOnPlatform] PRIMARY KEY CLUSTERED 
(
	[Studio] ASC,
	[Platform] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[StudioOnPlatform]  WITH CHECK ADD FOREIGN KEY([Platform])
REFERENCES [dbo].[Platform] ([ID])
GO

ALTER TABLE [dbo].[StudioOnPlatform]  WITH CHECK ADD FOREIGN KEY([Studio])
REFERENCES [dbo].[Studio] ([ID])
GO

