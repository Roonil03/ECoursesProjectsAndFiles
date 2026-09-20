match (n:ToyNode {name:'Julian'}) 
merge (n)-[:ToyRelation {relationship: 'fiancee'}]->(m:ToyNode {name:'Joyce', job:'store clerk'})


match (n:ToyNode)-[r]-(m) return n, r, m

match (n)-[r]-() delete n, r --add node incorrectly to an existing graph

create (n:ToyNode {name:'Julian'})-[:ToyRelation {relationship: 'fiancee'}]->(m:ToyNode {name:'Joyce', job:'store clerk'})

match (n:ToyNode {name:'Joyce'})-[r]-(m) delete n, r, m



match (n:ToyNode) where n.name = 'Harry' set n.job = 'drummer'
match (n:ToyNode) where n.name = 'Harry' set n.job = n.job + ['lead guitarist']



