CREATE USER linf FROM LOGIN linf; 

exec sp_addrolemember 'db_owner', 'linf'; 

GO

CREATE USER granadec FROM LOGIN granadec; 

exec sp_addrolemember 'db_owner', 'granadec'; 

GO

GRANT EXEC TO gameLibrary