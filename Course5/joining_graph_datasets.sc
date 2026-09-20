/* Docker Shell:
docker exec -it graphx-coursera /bin/sh
spark-shell
*/


import org.apache.log4j.Logger
import org.apache.log4j.Level

Logger.getLogger("org").setLevel(Level.ERROR)
Logger.getLogger("akka").setLevel(Level.ERROR)

import org.apache.spark.graphx._
import org.apache.spark.rdd._

val airports: RDD[(VertexId, String)] = sc.parallelize(
    List((1L, "Los Angeles International Airport"),
      (2L, "Narita International Airport"),
      (3L, "Singapore Changi Airport"),
      (4L, "Charles de Gaulle Airport"),
      (5L, "Toronto Pearson International Airport")))
// airports: org.apache.spark.rdd.RDD[(org.apache.spark.graphx.VertexId, String)] = ParallelCollectionRDD[0] at parallelize at <console>:32

val flights: RDD[Edge[String]] = sc.parallelize(
  List(Edge(1L,4L,"AA1123"),
    Edge(2L, 4L, "JL5427"),
    Edge(3L, 5L, "SQ9338"),
    Edge(1L, 5L, "AA6653"),
    Edge(3L, 4L, "SQ4521")))
// flights: org.apache.spark.rdd.RDD[org.apache.spark.graphx.Edge[String]] = ParallelCollectionRDD[1] at parallelize at <console>:32

val flightGraph = Graph(airports, flights)
// flightGraph: org.apache.spark.graphx.Graph[String,String] = org.apache.spark.graphx.impl.GraphImpl@770f3c94

flightGraph.triplets.foreach(t => println("Departs from: " + t.srcAttr + " - Arrives at: " + t.dstAttr + " - Flight Number: " + t.attr))
/*
Departs from: Los Angeles International Airport - Arrives at: Charles de Gaulle Airport - Flight Number: AA1123
Departs from: Narita International Airport - Arrives at: Charles de Gaulle Airport - Flight Number: JL5427
Departs from: Singapore Changi Airport - Arrives at: Toronto Pearson International Airport - Flight Number: SQ9338
Departs from: Los Angeles International Airport - Arrives at: Toronto Pearson International Airport - Flight Number: AA6653
Departs from: Singapore Changi Airport - Arrives at: Charles de Gaulle Airport - Flight Number: SQ4521
*/

case class AirportInformation(city: String, code: String)
// defined class AirportInformation


val airportInformation: RDD[(VertexId, AirportInformation)] = sc.parallelize(
  List((2L, AirportInformation("Tokyo", "NRT")),
    (3L, AirportInformation("Singapore", "SIN")),
    (4L, AirportInformation("Paris", "CDG")),
    (5L, AirportInformation("Toronto", "YYZ")),
    (6L, AirportInformation("London", "LHR")),
    (7L, AirportInformation("Hong Kong", "HKG"))))
// airportInformation: org.apache.spark.rdd.RDD[(org.apache.spark.graphx.VertexId, AirportInformation)] = ParallelCollectionRDD[19] at parallelize at <console>:34

def appendAirportInformation(id: VertexId, name: String, airportInformation: AirportInformation): String = name + ":"+ airportInformation.city
// appendAirportInformation: (id: org.apache.spark.graphx.VertexId, name: String, airportInformation: AirportInformation)String

val flightJoinedGraph =  flightGraph.joinVertices(airportInformation)(appendAirportInformation)
flightJoinedGraph.vertices.foreach(println)
/*
(1,Los Angeles International Airport)
(4,Charles de Gaulle Airport:Paris)
(2,Narita International Airport:Tokyo)
(3,Singapore Changi Airport:Singapore)
(5,Toronto Pearson International Airport:Toronto)
*/

val flightOuterJoinedGraph = flightGraph.outerJoinVertices(airportInformation)((_,name, airportInformation) => (name, airportInformation))
flightOuterJoinedGraph.vertices.foreach(println)
/*
(1,(Los Angeles International Airport,None))
(4,(Charles de Gaulle Airport,Some(AirportInformation(Paris,CDG))))
(2,(Narita International Airport,Some(AirportInformation(Tokyo,NRT))))
(3,(Singapore Changi Airport,Some(AirportInformation(Singapore,SIN))))
(5,(Toronto Pearson International Airport,Some(AirportInformation(Toronto,YYZ))))
*/

val flightOuterJoinedGraphTwo = flightGraph.outerJoinVertices(airportInformation)((_, name, airportInformation) => (name, airportInformation.getOrElse(AirportInformation("NA","NA"))))
flightOuterJoinedGraphTwo.vertices.foreach(println)
/*
(3,(Singapore Changi Airport,AirportInformation(Singapore,SIN)))
(1,(Los Angeles International Airport,AirportInformation(NA,NA)))
(5,(Toronto Pearson International Airport,AirportInformation(Toronto,YYZ)))
(4,(Charles de Gaulle Airport,AirportInformation(Paris,CDG)))
(2,(Narita International Airport,AirportInformation(Tokyo,NRT)))
*/

case class Airport(name: String, city: String, code: String)
// defined class Airport

  val flightOuterJoinedGraphThree = flightGraph.outerJoinVertices(airportInformation)((_, name, b) => b match {
  case Some(airportInformation) => Airport(name, airportInformation.city, airportInformation.code)
  case None => Airport(name, "", "")
})
flightOuterJoinedGraphThree.vertices.foreach(println)
/*
(1,Airport(Los Angeles International Airport,,))
(3,Airport(Singapore Changi Airport,Singapore,SIN))
(2,Airport(Narita International Airport,Tokyo,NRT))
(4,Airport(Charles de Gaulle Airport,Paris,CDG))
(5,Airport(Toronto Pearson International Airport,Toronto,YYZ))
*/

