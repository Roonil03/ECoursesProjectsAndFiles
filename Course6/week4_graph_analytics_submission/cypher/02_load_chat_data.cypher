LOAD CSV FROM "file:///chat_create_team_chat.csv" AS row
MERGE (u:User {id: toInteger(row[0])})
MERGE (t:Team {id: toInteger(row[1])})
MERGE (c:TeamChatSession {id: toInteger(row[2])})
MERGE (u)-[:CreatesSession {timeStamp: toFloat(row[3])}]->(c)
MERGE (c)-[:OwnedBy {timeStamp: toFloat(row[3])}]->(t);

LOAD CSV FROM "file:///chat_join_team_chat.csv" AS row
MERGE (u:User {id: toInteger(row[0])})
MERGE (c:TeamChatSession {id: toInteger(row[1])})
MERGE (u)-[:Joins {timeStamp: toFloat(row[2])}]->(c);

LOAD CSV FROM "file:///chat_leave_team_chat.csv" AS row
MERGE (u:User {id: toInteger(row[0])})
MERGE (c:TeamChatSession {id: toInteger(row[1])})
MERGE (u)-[:Leaves {timeStamp: toFloat(row[2])}]->(c);

LOAD CSV FROM "file:///chat_item_team_chat.csv" AS row
MERGE (u:User {id: toInteger(row[0])})
MERGE (c:TeamChatSession {id: toInteger(row[1])})
MERGE (i:ChatItem {id: toInteger(row[2])})
MERGE (u)-[:CreateChat {timeStamp: toFloat(row[3])}]->(i)
MERGE (i)-[:PartOf {timeStamp: toFloat(row[3])}]->(c);

LOAD CSV FROM "file:///chat_mention_team_chat.csv" AS row
MERGE (i:ChatItem {id: toInteger(row[0])})
MERGE (u:User {id: toInteger(row[1])})
MERGE (i)-[:Mentioned {timeStamp: toFloat(row[2])}]->(u);

LOAD CSV FROM "file:///chat_respond_team_chat.csv" AS row
MERGE (i1:ChatItem {id: toInteger(row[0])})
MERGE (i2:ChatItem {id: toInteger(row[1])})
MERGE (i1)-[:ResponseTo {timeStamp: toFloat(row[2])}]->(i2);