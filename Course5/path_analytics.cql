MATCH (a)-[r]->() delete a,r;
MATCH (a) delete a

LOAD CSV WITH HEADERS FROM "file:////datasets/test.csv" AS line
MERGE (n:MyNode {Name: line.Source})
MERGE (m:MyNode {Name: line.Target})
MERGE (n) -[:TO {dist: toInteger(line.distance)}]-> (m)

--Query 1. Viewing the graph.
MATCH (n:MyNode)-[r]->(m)
RETURN n, r, m

--Query 2. Finding paths between specific nodes.
MATCH p=(a)-[:TO*]->(c) 
WHERE a.Name='H' and c.Name='P' 
RETURN p order by length(p) asc limit 1

--Query 3. Finding the length between specific nodes.
MATCH p=(a)-[:TO*]->(c) 
WHERE a.Name='H' AND c.Name='P' 
RETURN length(p) ORDER BY length(p) limit 1

--Query 4. Finding a shortest path between specific nodes.
MATCH p=shortestPath((a)-[:TO*]->(c)) 
WHERE a.Name='A' AND c.Name='P' 
RETURN p, length(p) limit 1

--Query 5. All Shortest Paths
MATCH (source {Name: 'A'}), (destination {Name: 'P'}),
      path = allShortestPaths((source)-[:TO*]->(destination))
RETURN [node IN NODES(path) | node.Name] AS Paths

--Query 6. All Shortest Paths with Path Conditions.
MATCH (source {Name: 'A'}), (destination {Name: 'P'}),
      p = allShortestPaths((source)-[:TO*]->(destination))
WHERE length(p) > 5
RETURN [n IN nodes(p) | n.Name] AS Paths, length(p)

--Query 7. Diameter of the graph.
match (n:MyNode), (m:MyNode) 
where n <> m 
with n, m 
match p=shortestPath((n)-[*]->(m)) 
return n.Name, m.Name, length(p)
order by length(p) desc limit 1

MATCH (n:MyNode), (m:MyNode) 
WHERE n <> m 
WITH n, m 
MATCH p=shortestPath((n)-[*]->(m)) 
RETURN n.Name, m.Name, length(p)
ORDER BY length(p) desc limit 5

--Query 8. Extracting and computing with node and properties:
MATCH p = (a)-[:TO*]->(c)
WHERE a.Name='H' AND c.Name='P' 
RETURN [n IN nodes(p) | n.Name] AS Nodes, length(p) AS PathLength,
REDUCE(s = 0, e IN relationships(p) | s + toInteger(e.dist)) AS PathDist 
ORDER BY PathDist asc LIMIT 1

--Query 9.Dijkstra's algorithm for a specific target node.
MATCH (from:MyNode {Name:'A'}), (to:MyNode {Name:'P'}),
path = shortestPath((from)-[:TO*]->(to))
WITH REDUCE(dist = 0, rel IN relationships(path) | dist + toInteger(rel.dist)) AS distance, path
RETURN path, distance

--Query 10. Dijkstra's algorithm - Single Source Shortest Path (SSSP).
MATCH (from: MyNode {Name:'A'}), (to: MyNode)
WHERE from <> to
MATCH path = shortestPath((from)-[:TO*]->(to))
WITH from, to, path, REDUCE(dist = 0, rel in relationships(path) | dist + toInteger(rel.dist)) AS distance
RETURN from, to, path, distance 
ORDER BY distance DESC

--Query 11. Graph not containing a selected node.
MATCH (n)-[r:TO]->(m) 
WHERE n.Name <> 'D' and m.Name <> 'D' 
RETURN n, r, m

--Query 12. Shortest path over a Graph not containing a selected node
MATCH p = shortestPath((a {Name: 'A'})-[:TO*]-(b {Name: 'P'}))
WHERE NOT 'D' IN [n IN nodes(p) | n.Name]
RETURN p, length(p)

--Query 13. Graph not containing the immediate neighborhood of a specified node.
MATCH (d {Name:'D'})-[:TO]-(b)
with collect(distinct b.Name) as neighbors
MATCH (n)-[r:TO]->(m)
WHERE 
NOT (n.Name in (neighbors+'D')) 
AND 
NOT (m.Name in (neighbors+'D')) 
RETURN n, r, m


MATCH (d {Name:'D'})-[:TO]-(b)
with collect(distinct b.Name) as neighbors
MATCH (n)-[r:TO]->(m)
WHERE 
NOT (n.Name in (neighbors+'D')) 
AND 
NOT (m.Name in (neighbors+'D')) 
MATCH (d {Name:'D'})-[:TO]-(b)-[:TO]->(leaf)
WHERE NOT((leaf)-->())
RETURN (leaf), n,r,m

MATCH (d {Name:'D'})-[:TO]-(b)
with collect(distinct b.Name) as neighbors
MATCH (n)-[r:TO]->(m)
WHERE 
NOT (n.Name in (neighbors+'D')) 
AND 
NOT (m.Name in (neighbors+'D')) 
MATCH (d {Name:'D'})-[:TO]-(b)<-[:TO]-(root)
WHERE NOT((root)<--())
RETURN (root), n,r,m

--Query 14. Graph not containing a selected neighborhood.
MATCH (a {Name: 'F'})-[:TO*..2]-(b) 
WITH collect(distinct b.Name) as MyList 
MATCH (n)-[r:TO]->(m) 
WHERE NOT(n.Name in MyList) AND NOT (m.Name in MyList) 
RETURN distinct n, r, m

