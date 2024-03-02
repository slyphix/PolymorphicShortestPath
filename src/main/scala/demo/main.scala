package demo

import extensions.*

given CommutativeMonoid[Double] = AdditiveMonoid[Double]


def preprocessedBestExchangeRate[E, G](conversionGraph: G, have: Int, want: Int)(using GraphLike[G, Int, E], EdgeLike[E, Int], AssignedWeights[E, Double]): (Double, String) =
  // apply edge transformation
  val logGraph = PrimitiveGraph(
    conversionGraph.nodes,
    conversionGraph.edges.map(e => PrimitiveEdge(e.from, e.to, -math.log(e.weight)))
  )
  val Result(dists, preds) = shortestPath(logGraph, have)
  (math.exp(-dists(want)), extractPath(have, want, preds).mkString(" -> "))

def polymorphicBestExchangeRate[E, G](conversionGraph: G, have: Int, want: Int)(using GraphLike[G, Int, E], EdgeLike[E, Int], AssignedWeights[E, Double]): (Double, String) =
  // change context to multiplication and reversed comparison
  given MultiplicativeMonoid[Double] = MultiplicativeMonoid[Double]
  given OrderingWithInfinity[Double] = doubleInfRev
  val Result(dists, preds) = shortestPath(conversionGraph, have)
  (dists(want), extractPath(have, want, preds).mkString(" -> "))


@main def main(): Unit =
  // four currencies with their exchange rates
  val currencyConversionGraph = PrimitiveGraph(0 to 3, IndexedSeq(
    PrimitiveEdge(0, 1, 0.7), PrimitiveEdge(0, 2, 0.5),
    PrimitiveEdge(1, 2, 2.1), PrimitiveEdge(1, 3, 1.3),
    PrimitiveEdge(2, 3, 0.9),
    PrimitiveEdge(3, 0, 0.1),
  ))

  println(preprocessedBestExchangeRate(currencyConversionGraph, 0, 3))
  println(polymorphicBestExchangeRate(currencyConversionGraph, 0, 3))
