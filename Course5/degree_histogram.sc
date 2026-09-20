metrosGraph.numEdges
// res9: Long = 65


metrosGraph.numVertices
// res10: Long = 93


def max(a: (VertexId, Int), b: (VertexId, Int)): (VertexId, Int) = {
  if (a._2 > b._2) a else b
}
// max: (a: (org.apache.spark.graphx.VertexId, Int), b: (org.apache.spark.graphx.VertexId, Int))(org.apache.spark.graphx.VertexId, Int)


def min(a: (VertexId, Int), b: (VertexId, Int)): (VertexId, Int) = {
  if (a._2 <= b._2) a else b
}
// min: (a: (org.apache.spark.graphx.VertexId, Int), b: (org.apache.spark.graphx.VertexId, Int))(org.apache.spark.graphx.VertexId, Int)


metrosGraph.outDegrees.reduce(max)
// res11: (org.apache.spark.graphx.VertexId, Int) = (44,1)


metrosGraph.vertices.filter(_._1 == 44).collect()
// res12: Array[(org.apache.spark.graphx.VertexId, PlaceNode)] = Array((44,Metro(Toronto,6055724)))


metrosGraph.inDegrees.reduce(max)
// res13: (org.apache.spark.graphx.VertexId, Int) = (108,14)


metrosGraph.vertices.filter(_._1 == 108).collect()
// res14: Array[(org.apache.spark.graphx.VertexId, PlaceNode)] = Array((108,Country(United States)))


metrosGraph.outDegrees.filter(_._2 <= 1).count
// res15: Long = 65


metrosGraph.degrees.reduce(max)
// res16: (org.apache.spark.graphx.VertexId, Int) = (108,14)


metrosGraph.degrees.reduce(min)
// res17: (org.apache.spark.graphx.VertexId, Int) = (19,1)


metrosGraph.degrees.
  filter { case (vid, count) => vid >= 100 }. // Apply filter so only VertexId < 100 (countries) are included
  map(t => (t._2,t._1)).
  groupByKey.map(t => (t._1,t._2.size)).
  sortBy(_._1).collect()
// res18: Array[(Int, Int)] = Array((1,18), (2,4), (3,2), (5,2), (9,1), (14,1))


