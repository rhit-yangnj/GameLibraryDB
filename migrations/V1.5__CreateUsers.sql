--creating the user table
create table [dbo].[User] (
    [Salt] [binary](16) NOT NUll,
    [Username] [varchar](20) PRIMARY KEY,
    [HashPass] [varchar](32) 
);
go

--adding user to the notes table
Alter table [dbo].[Notes]
Add [NoteWriter] [varchar](20) NOT NULL,
FOREIGN KEY (NoteWriter) REFERENCES [User](Username)
go

--adding user to the reviews table
Alter table [dbo].[Reviews]
Add [ReviewWriter] [varchar](20) NOT NULL,
FOREIGN KEY (ReviewWriter) REFERENCES [User](Username)
go

--creating the UserHasGame table
create table [dbo].[UserHasGame](
    [User] [varchar](20),
    [Game] [int],
    FOREIGN KEY ([User]) REFERENCES [User](Username),
    FOREIGN KEY ([Game]) REFERENCES [Game](ID),
    Primary Key ([User], [Game])
)
