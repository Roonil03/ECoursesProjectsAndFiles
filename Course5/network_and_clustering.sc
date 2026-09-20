class PlaceNode(val name: String) extends Serializable
// defined class PlaceNode

case class Metro(override val name: String, population: Int) extends PlaceNode(name)
// defined class Metro

case class Country(override val name: String) extends PlaceNode(name)
// defined class Country

val metros: RDD[(VertexId, PlaceNode)] =
  sc.textFile("./EOADATA/metro.csv").
    filter(! _.startsWith("#")).
    map {line =>
      val row = line split ','
      (0L + row(0).toInt, Metro(row(1), row(2).toInt))
    }
// metros: org.apache.spark.rdd.RDD[(org.apache.spark.graphx.VertexId, PlaceNode)] = MapPartitionsRDD[11] at map at <console>:39


val countries: RDD[(VertexId, PlaceNode)] =
  sc.textFile("./EOADATA/country.csv").
    filter(! _.startsWith("#")).
    map {line =>
      val row = line split ','
      (100L + row(0).toInt, Country(row(1)))
    }
// countries: org.apache.spark.rdd.RDD[(org.apache.spark.graphx.VertexId, PlaceNode)] = MapPartitionsRDD[15] at map at <console>:39


val mclinks: RDD[Edge[Int]] =
  sc.textFile("./EOADATA/metro_country.csv").
    filter(! _.startsWith("#")).
    map {line =>
      val row = line split ','
      Edge(0L + row(0).toInt, 100L + row(1).toInt, 1)
    }
// mclinks: org.apache.spark.rdd.RDD[org.apache.spark.graphx.Edge[Int]] = MapPartitionsRDD[21] at map at <console>:36


Source.fromFile("./EOADATA/continent.csv").getLines().take(5).foreach(println)
/*
#continent_id,name
1,Asia
2,Africa
3,North America
4,South America
*/


Source.fromFile("./EOADATA/country_continent.csv").getLines().take(5).foreach(println)
/*
#country_id,continent_id
1,1
2,1
3,1
4,1
*/


case class Continent(override val name: String) extends PlaceNode(name)
// defined class Continent


val continents: RDD[(VertexId, PlaceNode)] =
  sc.textFile("./EOADATA/continent.csv").
    filter(! _.startsWith("#")).
    map {line =>
      val row = line split ','
      (200L + row(0).toInt, Continent(row(1))) // Add 200 to the VertexId to keep the indexes unique
    }
// continents: org.apache.spark.rdd.RDD[(org.apache.spark.graphx.VertexId, PlaceNode)] = MapPartitionsRDD[3] at map at <console>:39


val cclinks: RDD[Edge[Int]] =
  sc.textFile("./EOADATA/country_continent.csv").
    filter(! _.startsWith("#")).
    map {line =>
      val row = line split ','
      Edge(100L + row(0).toInt, 200L + row(1).toInt, 1)
    }
// cclinks: org.apache.spark.rdd.RDD[org.apache.spark.graphx.Edge[Int]] = MapPartitionsRDD[7] at map at <console>:36


val cnodes = metros ++ countries ++ continents
// cnodes: org.apache.spark.rdd.RDD[(org.apache.spark.graphx.VertexId, PlaceNode)] = UnionRDD[17] at $plus$plus at <console>:38

val clinks = mclinks ++ cclinks
// clinks: org.apache.spark.rdd.RDD[org.apache.spark.graphx.Edge[Int]] = UnionRDD[22] at $plus$plus at <console>:36


val countriesGraph = Graph(cnodes, clinks)
// countriesGraph: org.apache.spark.graphx.Graph[PlaceNode,Int] = org.apache.spark.graphx.impl.GraphImpl@319f6039


import org.graphstream.graph.implementations._
val graph: SingleGraph = new SingleGraph("countriesGraph")

for ((id:VertexId, place:PlaceNode) <- countriesGraph.vertices.collect())
{
  val node = graph.addNode(id.toString).asInstanceOf[SingleNode]
  node.addAttribute("name", place.name)
  node.addAttribute("ui.label", place.name)

  if (place.isInstanceOf[Metro])
    node.addAttribute("ui.class", "metro")
  else if(place.isInstanceOf[Country])
    node.addAttribute("ui.class", "country")
  else if(place.isInstanceOf[Continent])
    node.addAttribute("ui.class", "continent")
}


for (Edge(x,y,_) <- countriesGraph.edges.collect()) {
  graph.addEdge(x.toString ++ y.toString, x.toString, y.toString, true).asInstanceOf[AbstractEdge]
}



import org.graphstream.stream.file.FileSinkGraphML
val sink = new FileSinkGraphML()
val graphMLPath = "countriesGraph.graphml"

try {
  sink.writeAll(graph, graphMLPath)
  println(s"Graph exported to GraphML at: $graphMLPath")
} catch {
  case e: Exception => e.printStackTrace()
}
// Graph exported to GraphML at: countriesGraph.graphml


/* To be done on Spark Shell:
spark-shell --jars lib/gs-core-1.2.jar,lib/gs-ui-1.2.jar,lib/jcommon-1.0.16.jar,lib/jfreechart-1.0.13.jar,lib/breeze_2.10-0.9.jar,lib/breeze-viz_2.10-0.9.jar,lib/pherd-1.0.jar -i Facebook.scala
docker cp graphx-coursera:/facebookGraph.graphml ./facebookGraph.graphml
*/