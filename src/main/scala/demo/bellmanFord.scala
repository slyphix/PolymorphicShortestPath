package demo

import scala.collection.mutable
import scala.math.Ordering.Implicits.infixOrderingOps

import extensions.*


case class Result[N, W](distances: Map[N, W], predecessors: Map[N, N])


private def relax[N, E, W](dists: mutable.Map[N, W], edge: E)(using CommutativeMonoid[W], Ordering[W], EdgeLike[E, N], AssignedWeights[E, W]): Boolean =
  if (dists(edge.from) + edge.weight < dists(edge.to))
    dists(edge.to) = dists(edge.from) + edge.weight
    true
  else
    false


def shortestPath[N, E, W, G](graph: G, start: N)(using CommutativeMonoid[W], OrderingWithInfinity[W], GraphLike[G, N, E], EdgeLike[E, N], AssignedWeights[E, W]): Result[N, W] =
  val dists = graph.nodes.map(n => n -> infinity).to(mutable.Map)
  dists(start) = unit
  val preds = mutable.Map.empty[N, N]

  for (_ <- 1 until graph.nodes.size)
    for (e <- graph.edges)
      if (relax(dists, e))
        preds(e.to) = e.from
  Result(dists.toMap, preds.toMap)


def extractPath[N](origin: N, target: N, predecessor: Map[N, N]): IndexedSeq[N] =
  LazyList.iterate(target)(predecessor).takeWhile(_ != origin).appended(origin).reverse.toIndexedSeq


def hasNegativeCycle[N, E, W, G](graph: G, start: N)(using CommutativeMonoid[W], OrderingWithInfinity[W], GraphLike[G, N, E], EdgeLike[E, N], AssignedWeights[E, W]): Boolean =
  val dists = graph.nodes.map(n => n -> infinity).to(mutable.Map)

  for (_ <- 1 until graph.nodes.size)
    for (e <- graph.edges)
      relax(dists, e)
  graph.edges.exists(relax(dists, _))
