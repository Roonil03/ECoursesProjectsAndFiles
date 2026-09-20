// Chattiest Users
MATCH (u:User)-[:CreateChat]->(i:ChatItem)
RETURN u.id AS UserId, count(i) AS NumberOfChats
ORDER BY NumberOfChats DESC
LIMIT 10;

// Chattiest Teams
MATCH (i:ChatItem)-[:PartOf]->(c:TeamChatSession)-[:OwnedBy]->(t:Team)
RETURN t.id AS TeamId, count(i) AS NumberOfChats
ORDER BY NumberOfChats DESC
LIMIT 10;

// User-Team overlaps
MATCH (u:User)-[:CreatesSession|Joins]->(c:TeamChatSession)-[:OwnedBy]->(t:Team)
WITH u, t, count(*) AS Participation
RETURN u.id AS UserId, collect(DISTINCT t.id) AS TeamsBelongedTo;
