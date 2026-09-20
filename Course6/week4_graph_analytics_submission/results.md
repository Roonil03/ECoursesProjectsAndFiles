# Graph Analytics Peer Review Report

## 1. Modeling Chat Data using a Graph Data Model

Our graph model maps the internal company communications into nodes and relationships to facilitate deep network analysis. 
* **Nodes**: We model four core entities: `User`, `Team`, `TeamChatSession`, and `ChatItem`. Every node has an indexed, unique `id` property derived exactly from the CSV inputs.
* **Relationships**:
  * `(User)-[:CreatesSession]->(TeamChatSession)`: Tracks which user initiated a chat session.
  * `(TeamChatSession)-[:OwnedBy]->(Team)`: Connects the session to the overarching project team.
  * `(User)-[:Joins]->(TeamChatSession)` & `(User)-[:Leaves]->(TeamChatSession)`: Tracks user participation timeline within sessions.
  * `(User)-[:CreateChat]->(ChatItem)`: Links a specific message back to its author.
  * `(ChatItem)-[:PartOf]->(TeamChatSession)`: Connects the individual message into the session context.
  * `(ChatItem)-[:Mentioned]->(User)`: Indicates an explicit `@mention` in the chat, allowing us to build direct interaction graphs.
  * `(ChatItem)-[:ResponseTo]->(ChatItem)`: Maps conversation threads. A newer chat item points back to the older chat item it replies to.
* **Properties**: Every structural edge generated from the raw logs carries a `timeStamp` property (parsed safely as a float). This allows for temporal sequence reconstruction and ensures data is preserved rather than discarded during import.

---

## 2. Creation of the Graph Database for Chats

The raw data was provided as 6 individual, headerless CSV files. During the `LOAD CSV` process, we accessed columns by index (e.g. `row[0]`) rather than assuming `WITH HEADERS`. 

**Constraints**:
```cypher
CREATE CONSTRAINT ON (u:User) ASSERT u.id IS UNIQUE;
CREATE CONSTRAINT ON (t:Team) ASSERT t.id IS UNIQUE;
CREATE CONSTRAINT ON (c:TeamChatSession) ASSERT c.id IS UNIQUE;
CREATE CONSTRAINT ON (i:ChatItem) ASSERT i.id IS UNIQUE;
```

**Loading Process**:
All files were imported using idempotent `MERGE` commands, mapping string types into clean Integers and Floats. Here is a sample of the `LOAD CSV` command mapping the interactions:
```cypher
LOAD CSV FROM "file:///chat_item_team_chat.csv" AS row
MERGE (u:User {id: toInteger(row[0])})
MERGE (c:TeamChatSession {id: toInteger(row[1])})
MERGE (i:ChatItem {id: toInteger(row[2])})
MERGE (u)-[:CreateChat {timeStamp: toFloat(row[3])}]->(i)
MERGE (i)-[:PartOf {timeStamp: toFloat(row[3])}]->(c);
```

**Validation Totals**:
Running `MATCH (n) RETURN count(n)` yielded exactly **45,463** nodes.
Running `MATCH ()-[r]->() RETURN count(r)` yielded exactly **118,502** relationships.
These totals perfectly match the expected outcome, verifying successful and complete ingestion of all 6 log datasets.
*(Reference to graph structural screenshot: Check `artifacts/screenshot_instructions.md` for generation instructions if required.)*

---

## 3. Finding the Longest Conversation Chain and Its Participants

To trace the deepest conversational thread, we walked the directed `ResponseTo` relationship chain until it terminated.
**Query:**
```cypher
MATCH p=(start:ChatItem)-[:ResponseTo*]->(end:ChatItem)
WHERE NOT ()-[:ResponseTo]->(start) AND NOT (end)-[:ResponseTo]->()
WITH p, length(p) as PathLength
ORDER BY PathLength DESC LIMIT 1
UNWIND nodes(p) AS chatItem
MATCH (u:User)-[:CreateChat]->(chatItem)
RETURN PathLength, PathLength + 1 AS ChatItemsInvolved, count(distinct u) AS UniqueUsers;
```
**Interpretation & Results**:
The query traverses all `ResponseTo` paths, strictly finding roots (`start`) that have no inbound replies and leaves (`end`) that reply to nothing else. It calculates path length, sorts descending to find the longest chain, and then unwinds those nodes to distinctly count the authors of those specific items.
- **Longest Path Length**: 9 
- **Number of chat items involved**: 10 
- **Number of unique participating users**: 5

Identifying long conversation chains highlights sticky internal discussions or complex project bottlenecks. If 10 consecutive replies exist across 5 users, that specific topic likely generated significant organizational friction or deep collaboration that might warrant a formal meeting rather than endless text chatter. 

*(Reference: Follow the Q1 steps in `screenshot_instructions.md` to visually browse this exact 9-edge chain).*

---

## 4. Analyzing the Relationship Between Top 10 Chattiest Users and Top 10 Chattiest Teams

**Top-Three Chattiest Users**
| Users | Number of Chats |
| ----- | --------------: |
| 394   | 115             |
| 2067  | 111             |
| 1087  | 109             |

**Query (Users)**:
```cypher
MATCH (u:User)-[:CreateChat]->(i:ChatItem)
RETURN u.id AS UserId, count(i) AS NumberOfChats
ORDER BY NumberOfChats DESC LIMIT 10;
```

**Top-Three Chattiest Teams**
| Teams | Number of Chats |
| ----- | --------------: |
| 82    | 1324            |
| 185   | 1036            |
| 112   | 957             |

**Query (Teams)**:
```cypher
MATCH (i:ChatItem)-[:PartOf]->(c:TeamChatSession)-[:OwnedBy]->(t:Team)
RETURN t.id AS TeamId, count(i) AS NumberOfChats
ORDER BY NumberOfChats DESC LIMIT 10;
```

**User/Team Overlap Evaluation**:
To determine overlap between the two top-10 lists, we explicitly checked if any top 10 user belonged to a top 10 team (defined as the user having a `CreatesSession` or `Joins` relationship to a session owned by that team).

**Yes, there is overlap.** Upon calculating the full top ten lists, we found that User **999** (the 7th chattiest user with 105 chats) directly participated in sessions belonging to Team **52** (the 7th chattiest team with 788 chats). 
This demonstrates that while high chat volume isn't uniformly distributed, hyper-active teams occasionally cultivate hyper-active individual participants. Identifying this overlap enables management to identify their primary communication "hubs" or internal evangelists.

---

## 5. How Active Are Groups of Users

To evaluate network closeness, we derived a new abstract relationship (`InteractsWith`) linking users directly to each other based on Mentions and Responses, executing the following queries:

```cypher
// Mentions 
MATCH (u1:User)-[:CreateChat]->(i:ChatItem)-[:Mentioned]->(u2:User)
MERGE (u1)-[:InteractsWith]->(u2);

// Responses (Responder u1 interacts with Original Author u2)
MATCH (u1:User)-[:CreateChat]->(i1:ChatItem)-[:ResponseTo]->(i2:ChatItem)<-[:CreateChat]-(u2:User)
MERGE (u1)-[:InteractsWith]->(u2);

// Remove Self-Loops
MATCH (u:User)-[r:InteractsWith]->(u)
DELETE r;
```

**Clustering Coefficient for Top 10 Chattiest Users**:
We define the neighborhood structurally via outgoing `InteractsWith` relationships. Multiple edges are implicitly ignored via the initial `MERGE` setup, ensuring distinct edge calculations without duplication.
```cypher
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
RETURN u.id AS UserId, CASE WHEN k < 2 THEN 0.0 ELSE toFloat(distinct_edges) / toFloat(k * (k - 1)) END AS ClusteringCoefficient
ORDER BY ClusteringCoefficient DESC;
```

**Top Three Coefficients**:
| User ID | Coefficient |
| ------- | ----------: |
| 209     | 1.0         |
| 668     | 1.0         |
| 999     | 0.9464      |

**Interpretation**:
User 209 and 668 exhibit a perfect 1.0 clustering coefficient, meaning every single user they interact with also interacts with everyone else in their immediate sub-network. This indicates tightly-knit "cliques" or highly cohesive operational pods where information flows incredibly smoothly. While this doesn't strictly prove causation for performance, isolating tightly clustered groups is immensely valuable for community management: it highlights robust communication patterns that could be replicated or leveraged for rapid internal messaging and strong retention strategies.

---

## 6. Reproducibility and Handoff

* **Docker details**: Executed using `neo4j:4.4` completely isolated within Docker Compose to prevent host-system pollution. APOC was omitted since standard Cypher perfectly supported clustering coefficients.
* **Commands used**: 
  - `docker compose up -d` to deploy Neo4j and bind-mount the data directory.
  - `docker exec -i [container] cypher-shell` to systematically stream the 7 Cypher query scripts.
  - `docker compose down` was successfully executed to gracefully dismantle the architecture, fully deleting the Neo4j instance. 
* **Retained Deliverables**: `results.md`, `README.md`, `docker-compose.yml`, `.gitignore`, `screenshot_instructions.md`, `cypher/*.cypher`, and `artifacts/` (containing CSV validations).
* **Deleted Execution Assets**: The host execution powershell script (`run.ps1`) was immediately deleted upon termination of Docker Compose to maintain a perfectly clean handoff environment. No inputs were touched or altered and Git was entirely bypassed.
