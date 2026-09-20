# Screenshot Generation Instructions

Since this analysis was executed entirely in a headless Docker environment using `cypher-shell`, graphical screenshots cannot be natively exported. If you are required to embed PNGs of the graph in your assignment, follow these steps to quickly render them in Neo4j Browser:

1. Bring the Neo4j container up if it is not already running: `docker compose up -d`
2. Open your web browser and navigate to `http://localhost:7474`. (No authentication is required as it was explicitly disabled).

### To capture a representative graph model screenshot (Q2)
Run this query to randomly sample a subset of nodes and their connecting relationships:
```cypher
MATCH p=()-[r:CreatesSession|Joins|Leaves|PartOf|Mentioned|ResponseTo]->()
RETURN p LIMIT 25
```
Once the graph renders, disable any physics/forces, arrange the nodes visually so the labels are distinct, and take your screenshot.

### To capture the longest conversation path (Q1)
Run this query to isolate precisely the 9-edge / 10-node chain:
```cypher
MATCH p=(start:ChatItem)-[:ResponseTo*]->(end:ChatItem)
WHERE NOT ()-[:ResponseTo]->(start) AND NOT (end)-[:ResponseTo]->()
WITH p, length(p) as PathLength
ORDER BY PathLength DESC LIMIT 1
RETURN p
```
Take a screenshot of the resulting linear node chain.
