
/****** Object:  StoredProcedure [dbo].[readNote]    Script Date: 2/1/2024 8:35:21 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
Create OR ALTER PROCEDURE [dbo].[readNote] 
    @Id INT
AS
BEGIN
    SELECT * FROM Notes WHERE Id = @Id;
END;