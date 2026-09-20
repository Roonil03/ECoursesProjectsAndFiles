# Node Annotations (Copy-Ready)

**CSV Reader**
Read combined_data.csv (4619 rows)

**Row Filter**
Keep sessions with purchases

**Numeric Binner**
Define PennyPincher and HighRoller

**Column Filter**
Remove leakage and IDs

**Color Manager**
HighRoller (Green), PennyPincher (Red)

**Partitioning**
Stratified 60/40 Split

**Decision Tree Learner**
Train model with MDL Pruning

**Decision Tree Predictor**
Predict test set

**Scorer**
Confusion Matrix & Accuracy
