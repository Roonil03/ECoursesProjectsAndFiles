--2. Modify an existing script to import the dataset into Neo4j.
LOAD CSV WITH HEADERS FROM "file:////datasets/test.csv" AS line
MERGE (n:MyNode {Name: line.Source})
MERGE (m:MyNode {Name: line.Target})
MERGE (n) -[:TO {dist: toInteger(line.distance)}]-> (m)

match (n)-[r]-() delete n, r;
match (n) delete n

match (n)-[r]->(m) 
where m <> n 
return distinct n, m, count(r)

--Check it out later.