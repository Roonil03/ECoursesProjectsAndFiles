MATCH p=(start:ChatItem)-[:ResponseTo*]->(end:ChatItem)
WHERE NOT ()-[:ResponseTo]->(start) AND NOT (end)-[:ResponseTo]->()
WITH p, length(p) as PathLength
ORDER BY PathLength DESC LIMIT 1
UNWIND nodes(p) AS chatItem
MATCH (u:User)-[:CreateChat]->(chatItem)
RETURN PathLength, PathLength + 1 AS ChatItemsInvolved, count(distinct u) AS UniqueUsers;