// A. Mentions
MATCH (u1:User)-[:CreateChat]->(i:ChatItem)-[:Mentioned]->(u2:User)
MERGE (u1)-[:InteractsWith]->(u2);

// B. Responses
MATCH (u1:User)-[:CreateChat]->(i1:ChatItem)-[:ResponseTo]->(i2:ChatItem)<-[:CreateChat]-(u2:User)
MERGE (u1)-[:InteractsWith]->(u2);

// C. Remove self-loops
MATCH (u:User)-[r:InteractsWith]->(u)
DELETE r;

// D. Clustering Coefficient for Top 10 Chattiest Users
MATCH (u:User)-[:CreateChat]->(i:ChatItem)
WITH u, count(i) AS ChatCount
ORDER BY ChatCount DESC LIMIT 10
OPTIONAL MATCH (u)-[:InteractsWith]->(neighbor:User)
WITH u, collect(DISTINCT neighbor) AS neighbors
WITH u, neighbors, size(neighbors) AS k
UNWIND (CASE WHEN k = 0 THEN [null] ELSE neighbors END) AS n1
UNWIND (CASE WHEN k = 0 THEN [null] ELSE neighbors END) AS n2
OPTIONAL MATCH (n1)-[r:InteractsWith]->(n2)
WITH u, k, count(DISTINCT r) AS distinct_edges
RETURN u.id AS UserId, 
       CASE 
         WHEN k < 2 THEN 0.0 
         ELSE toFloat(distinct_edges) / toFloat(k * (k - 1)) 
       END AS ClusteringCoefficient
ORDER BY ClusteringCoefficient DESC;
