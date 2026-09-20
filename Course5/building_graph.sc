import org.apache.log4j.Logger
import org.apache.log4j.Level
Logger.getLogger("org").setLevel(Level.ERROR)
Logger.getLogger("akka").setLevel(Level.ERROR)


import org.apache.spark.graphx._
import org.apache.spark.rdd._
import scala.io.Source

Source.fromFile("./EOADATA/metro.csv").getLines().take(5).foreach(println)
/*
#metro_id,name,population
 1,Tokyo,36923000
 2,Seoul,25620000
 3,Shanghai,24750000
 4,Guangzhou,23900000
*/


Source.fromFile("./EOADATA/country.csv").getLines().take(5).foreach(println)
/*
#country_id,name
1,Japan
2,South Korea
3,China
4,India
*/

Source.fromFile("./EOADATA/metro_country.csv").getLines().take(5).foreach(println)
/*
#metro_id,country_id
1,1
2,2
3,3
4,3
*/

class PlaceNode(val name: String) extends Serializable
/*
defined class PlaceNode
*/

case class Metro(override val name: String, population: Int) extends PlaceNode(name)
/*
defined class Metro
*/

case class Country(override val name: String) extends PlaceNode(name)
/*
defined class Country
*/

val metros: RDD[(VertexId, PlaceNode)] =
  sc.textFile("./EOADATA/metro.csv").
    filter(! _.startsWith("#")).
    map {line =>
      val row = line split ','
      (0L + row(0).toInt, Metro(row(1), row(2).toInt))
    }
/*
metros: org.apache.spark.rdd.RDD[(org.apache.spark.graphx.VertexId, PlaceNode)] = MapPartitionsRDD[3] at map at <console>:39
*/

val countries: RDD[(VertexId, PlaceNode)] =
  sc.textFile("./EOADATA/country.csv").
    filter(! _.startsWith("#")).
    map {line =>
      val row = line split ','
      (100L + row(0).toInt, Country(row(1)))
    }
/*
countries: org.apache.spark.rdd.RDD[(org.apache.spark.graphx.VertexId, PlaceNode)] = MapPartitionsRDD[7] at map at <console>:39
*/

val mclinks: RDD[Edge[Int]] =
  sc.textFile("./EOADATA/metro_country.csv").
    filter(! _.startsWith("#")).
    map {line =>
      val row = line split ','
      Edge(0L + row(0).toInt, 100L + row(1).toInt, 1)
    }
/*
mclinks: org.apache.spark.rdd.RDD[org.apache.spark.graphx.Edge[Int]] = MapPartitionsRDD[11] at map at <console>:36
*/

val nodes = metros ++ countries
/*
nodes: org.apache.spark.rdd.RDD[(org.apache.spark.graphx.VertexId, PlaceNode)] = UnionRDD[12] at $plus$plus at <console>:36
*/

val metrosGraph = Graph(nodes, mclinks)
/*
metrosGraph: org.apache.spark.graphx.Graph[PlaceNode,Int] = org.apache.spark.graphx.impl.GraphImpl@a859cad
*/

metrosGraph.vertices.take(5)
/*
res5: Array[(org.apache.spark.graphx.VertexId, PlaceNode)] = Array((52,Metro(Ankara,5150072)), 
(56,Metro(Boston,4732161)), (4,Metro(Guangzhou,23900000)), (112,Country(United Kingdom)), 
(120,Country(Colombia)))
*/

metrosGraph.edges.take(5)
/*
res6: Array[org.apache.spark.graphx.Edge[Int]] = Array(Edge(1,101,1), Edge(2,102,1), 
Edge(3,103,1), Edge(4,103,1), Edge(5,104,1))
*/


metrosGraph.edges.filter(_.srcId == 1).map(_.dstId).collect()
/*
res7: Array[org.apache.spark.graphx.VertexId] = Array(101)
*/

metrosGraph.edges.filter(_.dstId == 103).map(_.srcId).collect()
/*
res11: Array[org.apache.spark.graphx.VertexId] = Array(3, 4, 7, 24, 34)
*/
