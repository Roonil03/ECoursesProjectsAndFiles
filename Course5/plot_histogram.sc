import org.knowm.xchart.{XYChartBuilder, SwingWrapper, BitmapEncoder, CategoryChart, CategoryChartBuilder}
import org.knowm.xchart.style.markers.SeriesMarkers
import org.knowm.xchart.BitmapEncoder.BitmapFormat

def degreeHistogram(net: Graph[PlaceNode, Int]): Array[(Int, Int)] =
  net.degrees.
    filter { case (vid, count) => vid >= 100 }.
    map(t => (t._2,t._1)).
    groupByKey.map(t => (t._1,t._2.size)).
    sortBy(_._1).collect()
// degreeHistogram: (net: org.apache.spark.graphx.Graph[PlaceNode,Int])Array[(Int, Int)]


val nn = metrosGraph.vertices.filter{ case (vid, count) => vid >= 100 }.count()
// nn: Long = 28


val metroDegreeDistribution = degreeHistogram(metrosGraph).map({case(d,n) => (d,n.toDouble/nn)})
// metroDegreeDistribution: Array[(Int, Double)] = Array((1,0.6428571428571429), (2,0.14285714285714285), 
// (3,0.07142857142857142), (5,0.07142857142857142), (9,0.03571428571428571), (14,0.03571428571428571))


val xData = metroDegreeDistribution.map(_._1.toDouble)
val yData = metroDegreeDistribution.map(_._2)

val chart = new XYChartBuilder().width(800).height(600).title("Degree Distribution").xAxisTitle("Degrees").yAxisTitle("Distribution").build()
chart.getStyler.setMarkerSize(6)
val series = chart.addSeries("Degree Distribution", xData, yData)
series.setMarker(SeriesMarkers.CIRCLE)
BitmapEncoder.saveBitmap(chart, "./Degree_Distribution.png", BitmapFormat.PNG)

val chart = new CategoryChartBuilder().width(800).height(600).title("Degree Histogram of Node Degrees").xAxisTitle("Degrees").yAxisTitle("Frequency").build()
chart.addSeries("Frequency", xData.map(_.toDouble), yData.map(_.toDouble)) 
BitmapEncoder.saveBitmap(chart, "./Degree_Histogram.png", BitmapFormat.PNG)

exit