# Answer:
## Part 1:
1. Use another attribute called clickID as the primary key for this table.
2. Let missionID  and sessionID be a long and isHit be a boolean.

This is the final table that we can get:
| clickID: long |
| userID: long |
| timestamp: dateTime |
| clickedPoint: coordinate |
| missionID: long |
| isHit: boolean |

here, clickID is the primary key which is a long., missionID should also be long since it's numeric and stays numeric. Finally, isHit becomes a boolean since the only two things it can take are "yes" and "no" (similar to true and false.)

### Finally,the table looks this:
| <u>clickID</u>: long | userID: long | sessionID: long | timestamp: dateTime | clickedPoint: coordinate | missionID: long | isHit: boolean |
| -------------------- | -----------: | --------------: | ------------------- | ------------------------ | --------------: | -------------- |
| 1                    |          100 |            4356 | 10/12/2015 14:15:09 | (4,8)                    |              13 | true           |
| 2                    |          101 |            3241 | 10/23/2015 14:15:19 | (20,5)                   |              18 | false          |
| 3                    |          102 |            4537 | 11/4/2015 14:15:20  | (17,43)                  |              21 | false          |


## Part 2:
Node Properties:
1. User
2. Session
3. Text

Edge Properties:
Joins (1 -> 2)
Leaves (1 -> 2)
Starts (1 -> 2)
Contains(2 -> 3)
Writes (1 -> 3)
Mention (3 -> 1)

With this
1. Team conversations can be found by counting chat sessions between teams
2. Chat activity before leaving a team can be studied by comparing message volume before and after team change using a timestamp attribute that we can add to the tables.
3. Dominant terms can come from analyzing the text of messages in a session during a chosen time range.
4. Active users can be found out by counting messages they wrote in a session
5. Simultaneous participation can be found by overlapping join and leave intervals across sessions.

## Part 3:
We can add more details to the flamingo-subtype level, because that is the most specific game object. We can add details such descriptive details (beak-color, body-color, leg-color, wing-pattern, size) and gameplay properties (movement-speed, hitbox-radius, point-value, rarity) make the game runnable and analyzable. Using a stable flamingo-subtype_id lets semistructured game tree connect cleanly to the relational user-click data and also the chart graph.