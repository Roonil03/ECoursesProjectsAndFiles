MATCH (n) RETURN count(n) AS NodeCount;
MATCH ()-[r]->() RETURN count(r) AS RelCount;