docker pull pramonettivega/neo4j-coursera

docker run --name neo4j-coursera -p 7474:7474 -p 7687:7687 -d -e NEO4J_AUTH=none pramonettivega/neo4j-coursera

