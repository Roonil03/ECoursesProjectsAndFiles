# PySpark K-Means Clustering Workflow
# (Unexecuted Blueprint)

from pyspark.sql import SparkSession
from pyspark.ml.feature import VectorAssembler, StandardScaler
from pyspark.ml.clustering import KMeans
import pandas as pd

def run_clustering():
    spark = SparkSession.builder.appName("Eglence-Clustering").getOrCreate()
    
    # 1. Read Data
    df = spark.read.csv("data/w3_clustering.csv", header=True, inferSchema=True)
    
    # 2. Assemble Features
    features = ["total_game_clicks", "total_ad_clicks", "total_revenue"]
    assembler = VectorAssembler(inputCols=features, outputCol="unscaled_features")
    df_assembled = assembler.transform(df)
    
    # 3. Standardize Features
    scaler = StandardScaler(inputCol="unscaled_features", outputCol="features", withMean=True, withStd=True)
    scaler_model = scaler.fit(df_assembled)
    df_scaled = scaler_model.transform(df_assembled)
    
    # 4. Fit K-Means
    kmeans = KMeans(k=3, seed=42)
    model = kmeans.fit(df_scaled)
    predictions = model.transform(df_scaled)
    
    # 5. Extract Centers
    centers = model.clusterCenters()
    print("Standardized Cluster Centers:")
    for center in centers:
        print(center)

if __name__ == "__main__":
    run_clustering()
