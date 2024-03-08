CREATE TABLE IF NOT EXISTS PUBLIC.Roles
(
    ID              INTEGER         AUTO_INCREMENT  NOT NULL,
    Name            VARCHAR(255)    NOT NULL UNIQUE,
    Description     VARCHAR(255)    NOT NULL,
    PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.Permissions
(
    ID              INTEGER         AUTO_INCREMENT  NOT NULL,
    Name            VARCHAR(255)    NOT NULL UNIQUE,
    Description     VARCHAR(255)    NOT NULL,
    PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.RoleHasPermissions
(
    ID              INTEGER     AUTO_INCREMENT  NOT NULL,
    RoleID          INTEGER     NOT NULL,
    PermissionID    INTEGER     NOT NULL,
    PRIMARY KEY (ID),
    UNIQUE (RoleID, PermissionID),
    FOREIGN KEY (RoleID) REFERENCES PUBLIC.Roles(ID),
    FOREIGN KEY (PermissionID) REFERENCES PUBLIC.Permissions(ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.Users
(
    ID              INTEGER     AUTO_INCREMENT NOT NULL,
    Username        VARCHAR(255) NOT NULL UNIQUE,
    Email           VARCHAR(255) NOT NULL UNIQUE,
    Password        VARCHAR(255) NOT NULL, /*hashed password*/
    RoleID          INTEGER NOT NULL,
    Score           INTEGER,
    PRIMARY KEY (ID),
    FOREIGN KEY (RoleID) REFERENCES PUBLIC.Roles(ID)
);

