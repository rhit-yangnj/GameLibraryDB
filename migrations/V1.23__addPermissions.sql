CREATE USER linf FROM LOGIN linf; 

exec sp_addrolemember 'db_owner', 'linf'; 

CREATE USER granadec FROM LOGIN [granadec]; 

exec sp_addrolemember 'db_owner', 'granadec'; 