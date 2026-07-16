SELECT t.Name AS TrackName, a.Title AS AlbumTitle FROM Track t INNER JOIN Album a ON t.AlbumId = a.AlbumId;

SELECT ar.Name AS ArtistName, al.Title AS AlbumTitle FROM Artist ar LEFT JOIN Album al ON ar.ArtistId = al.ArtistId;

SELECT al.Title AS AlbumTitle, ar.Name AS ArtistName FROM Album al RIGHT JOIN Artist ar ON al.ArtistId = ar.ArtistId;

SELECT g.Name AS GenreName FROM Genre g JOIN Track t ON g.GenreId = t.GenreId GROUP BY g.GenreId, g.Name HAVING COUNT(t.TrackId) >= 50 UNION SELECT g.Name AS GenreName FROM Genre g JOIN Track t ON g.GenreId = t.GenreId GROUP BY g.GenreId, g.Name HAVING COUNT(t.TrackId) < 50;

SELECT c.CustomerId, c.FirstName, c.LastName, SUM(i.Total) AS TotalSpending,
       CASE 
           WHEN SUM(i.Total) > 100 THEN 'High'
           WHEN SUM(i.Total) BETWEEN 50 AND 100 THEN 'Medium'
           ELSE 'Low'
       END AS SpenderCategory FROM Customer c JOIN Invoice i ON c.CustomerId = i.CustomerId GROUP BY c.CustomerId, c.FirstName, c.LastName;