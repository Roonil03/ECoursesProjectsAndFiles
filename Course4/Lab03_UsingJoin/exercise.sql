SELECT al.Title AS AlbumTitle, ar.Name AS ArtistName FROM Album AS al INNER JOIN Artist AS ar ON al.ArtistId = ar.ArtistId;

SELECT ar.Name AS ArtistName, al.Title AS AlbumTitle FROM Artist AS ar LEFT JOIN Album AS al ON ar.ArtistId = al.ArtistId;

SELECT al.Title AS AlbumTitle, ar.Name AS ArtistName FROM Artist AS ar RIGHT JOIN Album AS al ON ar.ArtistId = al.ArtistId;
