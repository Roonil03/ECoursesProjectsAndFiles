match (a)-[r]->() delete a,r;
match (a) delete a

load CSV WITH HEADERS from "file:////datasets/test.csv" as line
merge (n:MyNode {Name: line.Source})
merge (m:MyNode {Name: line.Target})
merge (n) -[:TO {dist: toInteger(line.distance)}]-> (m)

--Query 1: Counting the number of nodes
match (n:MyNode)
return count(n)

match (n:MyNode) return n

--Query 2: Counting the number of edges
match (n:MyNode)-[r]->()
return count(r)

--Query 3: Finding leaf nodes.
match (n:MyNode)-[r:TO]->(m)
where not ((m)-->())
return m

--Query 4: Finding root nodes.
match (m)-[r:TO]->(n:MyNode)
where not (()-->(m))
return m

--Query 5: Finding triangles.
match (a)-[:TO]->(b)-[:TO]->(c)-[:TO]->(a)
return distinct a, b, c

--Query 6: Finding 2nd neighbors of D.
match (a)-[:TO*..2]-(b)
where a.Name='D'
return distinct a, b



match (a)-[r]->() delete a,r;
match (a) delete a

load CSV WITH HEADERS from "file:////datasets/terrorist_data_subset.csv" as row
merge (c:Country {Name:row.Country})
merge (a:Actor {Name: row.ActorName, Aliases: row.Aliases, Type: row.ActorType})
merge (o:Organization {Name: row.AffiliationTo})
merge (a)-[:AFFILIATED_TO {Start: row.AffiliationStartDate, End: row.AffiliationEndDate}]->(o)
merge(c)<-[:IS_FROM]-(a);

--Query 7: Finding the types of a node.
match (n)
where n.Name = 'Afghanistan'
return labels(n)

--Query 8. Finding the label of an edge
match (n {Name: 'Afghanistan'})<-[r]-()
return distinct type(r)

--Query 9. Finding all properties of a node
match (n:Actor)
return * limit 20

match (a)-[r]->() delete a,r;
match (a) delete a

load CSV WITH HEADERS from 'file:////datasets/test2.csv' as line
merge (n:MyNode {Name: line.Source})
merge (m:MyNode {Name: line.Target})
merge (n)-[:TO {dist: line.Distance}]->(m)

--Query 10. Finding loops.
match (n)-[r]->(n)
return n, r limit 10

--Query 11. Finding multigraphs.
match (n)-[r1]->(m), (n)-[r2]-(m)
where r1 <> r2
return n, r1, r2, m

--Query 12. Finding the induced subgraph given a set of nodes.
match (n)-[r:TO]-(m)
where n.Name in ['A', 'B', 'C', 'D', 'E'] and m.Name in ['A', 'B', 'C', 'D', 'E']
return n, r, m

