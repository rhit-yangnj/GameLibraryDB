
/****** Object:  StoredProcedure [dbo].[readReview]    Script Date: 2/1/2024 8:35:24 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE OR ALTER PROCEDURE [dbo].[readReview] 
    @Id INT
AS
BEGIN
    SELECT * FROM Reviews WHERE Id = @Id;
END;