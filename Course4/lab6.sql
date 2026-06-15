CREATE DATABASE football_club;
USE football_club;
CREATE TABLE players (
    playerID INT, 
    playerName VARCHAR(50), 
    age INT
);
SHOW tables;
SHOW columns FROM players;
CREATE TABLE games (
    gameID INT, 
    gameDate DATE, 
    score INT
);
SHOW tables;
SHOW columns FROM games;
