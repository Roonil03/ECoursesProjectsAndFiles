# KNIME Workflow Plan

1. **CSV Reader**
   - **Label/Annotation**: Read combined_data.csv (4619 rows)
   - **Configuration**: Point to `combined_data.csv`. Set missing value string to "NULL".

2. **Row Filter**
   - **Label/Annotation**: Keep sessions with purchases
   - **Configuration**: Filter column `avg_price` to "Exclude rows by attribute value" -> "only missing values match". Resulting count: 1411 rows.

3. **Numeric Binner**
   - **Label/Annotation**: Define PennyPincher and HighRoller
   - **Configuration**: Bin `avg_price`. Range [-∞, 5.0] = "PennyPincher". Range (5.0, ∞] = "HighRoller". Target column name: `buyer_class`.

4. **Column Filter**
   - **Label/Annotation**: Remove leakage and IDs
   - **Configuration**: Exclude `userId`, `userSessionId`, and `avg_price`. Keep all other features plus `buyer_class`.

5. **Color Manager**
   - **Label/Annotation**: HighRoller (Green), PennyPincher (Red)
   - **Configuration**: Target column `buyer_class`. Assign distinct colors.

6. **Partitioning**
   - **Label/Annotation**: Stratified 60/40 Split
   - **Configuration**: 60% relative size. Stratified sampling on `buyer_class`. Use random seed: 1466016757670.

7. **Decision Tree Learner**
   - **Label/Annotation**: Train model with MDL Pruning
   - **Configuration**: Class column `buyer_class`. Enable "Use MDL for pruning".

8. **Decision Tree Predictor**
   - **Label/Annotation**: Predict test set
   - **Configuration**: Predict on the 40% test partition.

9. **Scorer**
   - **Label/Annotation**: Confusion Matrix & Accuracy
   - **Configuration**: Compare `buyer_class` with `Prediction (buyer_class)`.
