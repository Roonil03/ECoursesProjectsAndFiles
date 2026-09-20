create (N1:ToyNode {name: 'Tom'}) - [:ToyRelation {relationship: 'knows'}] -> (N2:ToyNode {name: 'Harry'}), 
(N2) - [:ToyRelation {relationship: 'co-worker'}] -> (N3:ToyNode {name: 'Julian', job: 'plumber'}), 
(N2) - [:ToyRelation {relationship: 'wife'}] -> (N4:ToyNode {name: 'Michele', job: 'accountant'}), 
(N1) - [:ToyRelation {relationship: 'wife'}] -> (N5:ToyNode {name: 'Josephine', job: 'manager'}), 
(N4) - [:ToyRelation {relationship: 'friend'}] -> (N5) 
;
match (n:ToyNode)-[r]-(m) return n, r, m

match (n)-[r]-() delete n, r -- Delete all nodes and edges

match (n) delete n --Delete all nodes which have no edges

match (n:ToyNode) delete n --Delete only ToyNode nodes which have no edges

match (n)-[r]-() delete r --Delete all edges

match (n)-[r:ToyRelation]-() delete r --Delete only ToyRelation edges

match (n:ToyNode {name:'Julian'}) return n   --Selecting an existing single ToyNode node 
